package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateEssentialDTO;
import com.tuvarna.phd.dto.CandidateStatusDTO;
import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.Supervisor;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.model.MailModel.TEMPLATES;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.DoctoralCenterRoleRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import com.tuvarna.phd.repository.SupervisorRepository;
import com.tuvarna.phd.repository.UnauthorizedUsersRepository;
import io.quarkus.cache.CacheResult;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class DoctoralCenterServiceImpl implements DoctoralCenterService {
  @Inject DoctoralCenterRepository doctoralCenterRepository;
  @Inject DoctoralCenterRoleRepository doctoralCenterRoleRepository;
  @Inject PhdRepository phdRepository;
  @Inject PhdStatusRepository phdStatusRepository;
  @Inject CommitteeRepository committeeRepository;
  @Inject SupervisorRepository supervisorRepository;
  @Inject CandidateRepository candidateRepository;
  @Inject UnauthorizedUsersRepository uRepository;
  @Inject CandidateMapper candidateMapper;
  @Inject DatabaseModel databaseModel;
  @Inject MailModel mailModel;

  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterServiceImpl.class);

  @ConfigProperty(name = "client.base-url")
  private String clientBaseURL;

  @Override
  @Transactional
  public void review(CandidateStatusDTO candidateStatusDTO) throws IOException {
    LOG.info(
        "Service received a request to update status for candidate: "
            + candidateStatusDTO.toString());
    Candidate candidate = this.candidateRepository.getByEmail(candidateStatusDTO.getEmail());

    switch (candidateStatusDTO.getStatus()) {
      case "approved" -> {
        candidate.setStatus("approved");

        LOG.info(
            "Candidate arroved! Now sending email to the candidate personal email about it...");

        this.mailModel.send(
            "Вашата кандидатура е одобрена!",
            TEMPLATES.ACCEPTED,
            candidateStatusDTO.getEmail(),
            Map.of("$APP_URL", clientBaseURL));

        LOG.info("Now sending email for the admins to create the phd user to the Azure AD...");

        List<String> adminEmails =
            this.databaseModel.selectMapString(
                "SELECT d.email FROM doctoralcenter d JOIN doctoralcenterrole dc ON(d.role ="
                    + " dc.id) WHERE dc.role = $1",
                Tuple.of("admin"),
                "email");

        adminEmails.forEach(
            email -> {
              try {
                this.mailModel.send(
                    "Заявка за създаване на докторант " + candidateStatusDTO.getEmail(),
                    TEMPLATES.CREATE_USER,
                    email,
                    Map.of("$PHD_USER", candidateStatusDTO.getEmail()));
              } catch (IOException exception) {
                LOG.error("Error in sending email to the admins: " + exception);
                throw new HttpException("Error in sending email to the admins!");
              }
            });
      }

      case "rejected" -> {
        candidate.setStatus("rejected");

        this.mailModel.send(
            "Вашата докторантска кандидатура в Ту-Варна",
            TEMPLATES.REJECTED,
            candidateStatusDTO.getEmail());
      }
      default ->
          throw new HttpException(
              "Status is invalid: "
                  + candidateStatusDTO.getStatus()
                  + " .Valid statuses are: accepted, declined");
    }
  }

  @Override
  public List<CandidateEssentialDTO> getCandidates() {
    LOG.info("Received a service request to retrieve all candidates ");

    List<Candidate> candidates =
        this.databaseModel.selectMapEntity(
            "SELECT name, email, biography, status FROM candidate", Tuple.of(""), new Candidate());

    List<CandidateEssentialDTO> candidateDTOs = new ArrayList<>();
    candidates.forEach(
        candidate -> candidateDTOs.add(this.candidateMapper.toEssentialDto(candidate)));

    return candidateDTOs;
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
  public void setUnauthorizedUserGroup(List<UnauthorizedUsersDTO> usersDTO, String group) {
    LOG.info(
        "Service received a request to set a role: "
            + group
            + "for unauthorized user: "
            + usersDTO.toString());

    for (UnauthorizedUsersDTO userDTO : usersDTO) {

      UnauthorizedUsers user = this.uRepository.getByOid(userDTO.getOid());
      switch (group) {
        // TODO: maybe move this create to separate method in client
        case "phd" -> {

          // TODO: Need to retrive the pin from Azure AD somehow...
          Phd phd = new Phd(userDTO.getOid(), userDTO.getName(), userDTO.getEmail(), "111111111");
          phd.setStatus(this.phdStatusRepository.getByStatus("enrolled"));
          // TODO: generate all reports
          this.phdRepository.save(phd);
        }

        case "committee" -> {
          Committee committee =
              new Committee(userDTO.getOid(), userDTO.getName(), userDTO.getEmail());
          this.committeeRepository.save(committee);
        }

        case "supervisor" -> {
          Supervisor supervisor =
              new Supervisor(userDTO.getOid(), userDTO.getName(), userDTO.getEmail());
          this.supervisorRepository.save(supervisor);
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
  @Transactional
  @CacheResult(cacheName = "doc-center-roles-cache")
  public List<String> getDoctoralCenterRoles() {
    LOG.info("Received a request to retrieve all doctoral center roles");
    List<String> docCenterPermitRoles = List.of("phd", "committee", "supervisor");

    LOG.info("All doc center roles have been retrieved: " + docCenterPermitRoles.toString());
    return docCenterPermitRoles;
  }
}
