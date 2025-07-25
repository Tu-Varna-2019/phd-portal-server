package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.UnauthorizedDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.entity.DoctoralCenterRole;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.Unauthorized;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.model.S3Model;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.DoctoralCenterRoleRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import com.tuvarna.phd.repository.UnauthorizedRepository;
import io.quarkus.cache.CacheInvalidate;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class DoctoralCenterAdminServiceImpl implements DoctoralCenterAdminService {
  @Inject DoctoralCenterRepository doctoralCenterRepository;
  @Inject DoctoralCenterRoleRepository doctoralCenterRoleRepository;
  @Inject PhdRepository phdRepository;
  @Inject PhdStatusRepository phdStatusRepository;
  @Inject CommitteeRepository committeeRepository;
  @Inject CandidateRepository candidateRepository;
  @Inject UnauthorizedRepository uRepository;
  @Inject CandidateMapper candidateMapper;
  @Inject DatabaseModel databaseModel;
  @Inject MailModel mailModel;
  @Inject S3Model s3Model;

  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterServiceImpl.class);

  @ConfigProperty(name = "client.base-url")
  private String clientBaseURL;

  @Override
  @CacheInvalidate(cacheName = "auth-users-cache")
  @Transactional
  public void deleteAuthorizedUser(String oid, String group) {
    switch (group) {
      case "phd" -> this.phdRepository.deleteByOid(oid);
      case "committee" -> {
        Boolean isCommitteeATeacher =
            this.databaseModel.getBoolean(
                "SELECT EXISTS (SELECT 1 FROM subject WHERE teacher = (SELECT id FROM committee"
                    + " WHERE oid = $1))",
                Tuple.of(oid));

        if (isCommitteeATeacher) {
          throw new HttpException(
              "Cannot delete committe. He is a teacher in subject already!", 400);
        } else {
          this.committeeRepository.deleteByOid(oid);
        }
      }
      case "doctoral-center" -> this.doctoralCenterRepository.deleteByOid(oid);

      default -> throw new HttpException("Group is incorrect!", 400);
    }
  }

  @Override
  // @CacheResult(cacheName = "unauth-users-cache")
  @Transactional
  public List<Unauthorized> getUnauthorizedUsers() {
    LOG.info("Service received to retrieve all unauthorized users");
    List<Unauthorized> unauthorizedUsers = this.uRepository.getAll();

    return unauthorizedUsers;
  }

  @Override
  // @CacheResult(cacheName = "auth-users-cache")
  @Transactional
  public List<UserDTO> getAuthorizedUsers() {
    LOG.info("Service received to retrieve all authorized users");
    List<UserDTO> authenticatedUsers = new ArrayList<>();
    List<Phd> phds = this.phdRepository.getAll();
    List<Committee> committees = this.committeeRepository.getAll();
    List<DoctoralCenter> doctoralCenters = this.doctoralCenterRepository.getAll();

    for (Phd phd : phds) {
      UserDTO user =
          new UserDTO(
              phd.getOid(),
              phd.getName(),
              phd.getEmail(),
              "phd",
              this.s3Model.getDataUrlPicture(phd.getOid(), phd.getPicture()),
              0);
      authenticatedUsers.add(user);
    }

    for (Committee commitee : committees) {
      UserDTO user =
          new UserDTO(
              commitee.getOid(),
              commitee.getName(),
              commitee.getEmail(),
              "committee",
              this.s3Model.getDataUrlPicture(commitee.getOid(), commitee.getPicture()),
              0);
      authenticatedUsers.add(user);
    }

    for (DoctoralCenter dCenter : doctoralCenters) {
      String role = (dCenter.getRole() != null) ? dCenter.getRole().getRole() : "unknown";

      UserDTO user =
          new UserDTO(
              dCenter.getOid(),
              dCenter.getName(),
              dCenter.getEmail(),
              role,
              this.s3Model.getDataUrlPicture(dCenter.getOid(), dCenter.getPicture()),
              0);
      authenticatedUsers.add(user);
    }

    return authenticatedUsers;
  }

  @Override
  @CacheInvalidate(cacheName = "unauth-users-cache")
  @CacheInvalidate(cacheName = "auth-users-cache")
  @Transactional
  public void setUnauthorizedUserGroup(List<UnauthorizedDTO> usersDTO, String group) {
    LOG.info(
        "Service received a request to set a role: "
            + group
            + "for unauthorized user: "
            + usersDTO.toString());

    for (UnauthorizedDTO userDTO : usersDTO) {
      Unauthorized user = this.uRepository.getByOid(userDTO.getOid());
      switch (group) {
        case "expert", "manager", "admin" -> {
          DoctoralCenter dCenter =
              new DoctoralCenter(userDTO.getOid(), userDTO.getName(), userDTO.getEmail());
          DoctoralCenterRole doctoralCenterRole =
              this.doctoralCenterRoleRepository.getByRole(group);
          dCenter.setRole(doctoralCenterRole);

          this.doctoralCenterRepository.save(dCenter);
        }
        default -> throw new HttpException("Group: " + group + " doesn't exist!");
      }

      LOG.info(
          "User created for a role: "
              + group
              + " !Now deleting him from unauthorized users table...");
      this.uRepository.delete(user);

      LOG.info("User " + user.getEmail() + " deleted from that table!");
    }
  }

  @Override
  @CacheInvalidate(cacheName = "unauth-users-cache")
  @Transactional
  public void changeUnauthorizedUserIsAllowed(String oid, Boolean isAllowed) {
    LOG.info(
        "Service received a request to change isAllowed: "
            + isAllowed
            + " for oid unauthorized user: "
            + oid);

    Boolean doesOidExist =
        this.databaseModel.getBoolean(
            "SELECT EXISTS (SELECT 1 FROM unauthorized WHERE oid = $1)", Tuple.of(oid));

    if (!doesOidExist) {
      throw new HttpException("User with such oid doesn't exist!", 400);
    }

    this.databaseModel.execute(
        "UPDATE unauthorized SET allowed = $1 WHERE oid = $2", Tuple.of(isAllowed, oid));

    LOG.info("IsAllowed has been successfully changed!");
  }

  @Override
  @Transactional
  // @CacheResult(cacheName = "doc-center-roles-cache")
  public List<String> getDoctoralCenterRoles() {
    LOG.info("Received a request to retrieve all doctoral center roles");

    List<String> docCenterRoles =
        this.databaseModel.getListString(
            "SELECT role FROM doctoral_center_role", Tuple.tuple(), "role");

    LOG.info("All doc center roles have been retrieved: " + docCenterRoles.toString());
    return docCenterRoles;
  }
}
