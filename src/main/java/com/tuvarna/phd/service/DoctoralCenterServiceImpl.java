package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.RoleDTO;
import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.entity.DoctoralCenterRole;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.exception.CandidateException;
import com.tuvarna.phd.exception.DoctoralCenterException;
import com.tuvarna.phd.exception.UserException;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.model.MailModel.TEMPLATES;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.DoctoralCenterRoleRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import com.tuvarna.phd.repository.UnauthorizedUsersRepository;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class DoctoralCenterServiceImpl implements DoctoralCenterService {

  private final DoctoralCenterRepository doctoralCenterRepository;
  private final DoctoralCenterRoleRepository doctoralCenterRoleRepository;
  private final PhdRepository phdRepository;
  private final CommitteeRepository committeeRepository;
  private final CandidateRepository candidateRepository;
  private final UnauthorizedUsersRepository uRepository;
  private final DatabaseModel databaseModel;

  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterServiceImpl.class);
  MailModel mailModel;

  @ConfigProperty(name = "client.base-url")
  private String clientBaseURL;

  @Inject
  public DoctoralCenterServiceImpl(
      DatabaseModel databaseModel,
      MailModel mailModel,
      CandidateRepository candidateRepository,
      DoctoralCenterRepository doctoralCenterRepository,
      PhdRepository phdRepository,
      DoctoralCenterRoleRepository doctoralCenterRoleRepository,
      PhdStatusRepository sPhdRepository,
      CommitteeRepository committeeRepository,
      UnauthorizedUsersRepository uRepository) {
    this.databaseModel = databaseModel;
    this.mailModel = mailModel;
    this.candidateRepository = candidateRepository;
    this.doctoralCenterRepository = doctoralCenterRepository;
    this.phdRepository = phdRepository;
    this.doctoralCenterRoleRepository = doctoralCenterRoleRepository;
    this.uRepository = uRepository;
    this.committeeRepository = committeeRepository;
  }

  @Override
  @Transactional
  public void review(CandidateDTO candidateDTO) {
    LOG.info(
        "Service received a request to update status for candidate: " + candidateDTO.toString());
    Candidate candidate = this.candidateRepository.getByEmail(candidateDTO.getEmail());

    switch (candidateDTO.getStatus()) {
      case "approved" -> {
        candidate.setStatus("approved");
        LOG.info(
            "Candidate arroved! Now sending email to the candidate personal email about it...");

        this.sendEmail(
            "Добре дошли в Технически университет Варна!",
            TEMPLATES.ACCEPTED,
            candidateDTO.getEmail());

        LOG.info("Now sending email for the admins to create the phd user to the Azure AD...");

        List<String> adminEmails =
            this.databaseModel.selectMapString(
                "SELECT d.email FROM doctoralcenter d JOIN doctoralcenterrole dc ON(d.role = dc.id)"
                    + " WHERE dc.role = $1",
                Tuple.of("admin"),
                "email");

        adminEmails.forEach(
            email -> {
              this.sendEmail(
                  "Създаване на нов докторант: " + candidateDTO.getEmail(),
                  TEMPLATES.CREATE_USER,
                  email);
            });
      }

      case "rejected" -> {
        candidate.setStatus("rejected");
        this.sendEmail(
            "Вие не бяхте одобрен за вашата кандидатура в Ту-Варна",
            TEMPLATES.REJECTED,
            candidateDTO.getEmail());
      }
      default ->
          throw new CandidateException(
              "Status is invalid: "
                  + candidateDTO.getStatus()
                  + " .Valid statuses are: accepted, declined");
    }
  }

  @Override
  @Transactional
  public void deleteAuthorizedUser(String oid, RoleDTO role) {
    switch (role.getRole()) {
      case "phd" -> this.phdRepository.deleteByOid(oid);
      case "committee" -> this.committeeRepository.deleteByOid(oid);
      case "doctoralCenter" -> this.doctoralCenterRepository.deleteByOid(oid);

      default -> throw new UserException("Role is incorrect!", 400);
    }
  }

  @Override
  public void sendEmail(String title, TEMPLATES template, String email) {
    try {
      this.mailModel.send("Добре дошли в Технически университет Варна!", template, email);
    } catch (IOException exception) {
      LOG.error("Error in reading mail template: " + exception);
      throw new CandidateException("Error in sending email. Please try again later!");
    }
  }

  @Override
  @Transactional
  public List<UnauthorizedUsers> getUnauthorizedUsers() {
    LOG.info("Service received to retrieve all unauthorized users");
    List<UnauthorizedUsers> unauthorizedUsers = this.uRepository.getAll();

    return unauthorizedUsers;
  }

  @Override
  @Transactional
  public List<UserDTO> getAuthorizedUsers() {
    LOG.info("Service received to retrieve all unauthorized users");
    List<UserDTO> authenticatedUsers = new ArrayList<>();
    List<Phd> phds = this.phdRepository.getAll();
    List<Committee> committees = this.committeeRepository.getAll();
    List<DoctoralCenter> doctoralCenters = this.doctoralCenterRepository.getAll();

    for (Phd phd : phds) {
      UserDTO user = new UserDTO(phd.getId(), phd.getOid(), phd.getName(), phd.getEmail(), "phd");
      authenticatedUsers.add(user);
    }

    for (Committee commitee : committees) {
      UserDTO user =
          new UserDTO(
              commitee.getId(),
              commitee.getOid(),
              commitee.getName(),
              commitee.getEmail(),
              "committee");
      authenticatedUsers.add(user);
    }

    for (DoctoralCenter dCenter : doctoralCenters) {
      UserDTO user =
          new UserDTO(
              dCenter.getId(),
              dCenter.getOid(),
              dCenter.getName(),
              dCenter.getEmail(),
              dCenter.getRole().getRole());
      authenticatedUsers.add(user);
    }

    return authenticatedUsers;
  }

  @Override
  @Transactional
  public void setUnauthorizedUserRole(List<UnauthorizedUsersDTO> usersDTO, String role) {
    LOG.info(
        "Service received a request to set a role: "
            + role
            + "for unauthorized user: "
            + usersDTO.toString());

    for (UnauthorizedUsersDTO userDTO : usersDTO) {
      UnauthorizedUsers user = this.uRepository.getByOid(userDTO.getOid());
      this.createUserByRole(userDTO.getOid(), userDTO.getName(), userDTO.getEmail(), role);

      LOG.info(
          "User created for a role: "
              + role
              + " ! Now deleting him from unauthorized users table...");
      this.uRepository.delete(user);

      LOG.info("User " + user.getEmail() + " deleted from that table!");
    }
  }

  private void createUserByRole(String oid, String name, String email, String role) {
    switch (role) {
      case "expert", "manager", "admin":
        DoctoralCenter dCenter = new DoctoralCenter(oid, name, email);
        DoctoralCenterRole doctoralCenterRole = this.doctoralCenterRoleRepository.getByRole(role);
        dCenter.setRole(doctoralCenterRole);

        this.doctoralCenterRepository.save(dCenter);
        break;
      default:
        throw new DoctoralCenterException("Role: " + role + " doesn't exist!");
    }
  }
}
