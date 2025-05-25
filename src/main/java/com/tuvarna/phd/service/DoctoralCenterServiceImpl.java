package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.UnauthorizedDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.Supervisor;
import com.tuvarna.phd.entity.Unauthorized;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.mapper.PhdMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.model.MailModel.TEMPLATES;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CandidateStatusRepository;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.DoctoralCenterRoleRepository;
import com.tuvarna.phd.repository.GradeRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import com.tuvarna.phd.repository.SupervisorRepository;
import com.tuvarna.phd.repository.UnauthorizedRepository;
import io.quarkus.cache.CacheResult;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
  @Inject CandidateStatusRepository candidateStatusRepository;
  @Inject GradeRepository gradeRepository;
  @Inject UnauthorizedRepository uRepository;

  @Inject CandidateMapper candidateMapper;
  @Inject PhdMapper phdMapper;
  @Inject DatabaseModel databaseModel;
  @Inject MailModel mailModel;

  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterServiceImpl.class);

  @ConfigProperty(name = "client.base-url")
  private String clientBaseURL;

  @Override
  @Transactional
  public void review(String email, String status) throws IOException {
    LOG.info("Service received a request to review candidate: " + email);

    Candidate candidate = this.candidateRepository.getByEmail(email);
    candidate.setStatus(this.candidateStatusRepository.getByStatus(status));
    candidate.setExamStep(candidate.getExamStep() + 1);

    switch (candidate.getExamStep()) {
      case 1 -> {
        if (status.equals("approved")) {
          LOG.info(
              "Candidate approved to go to the first draft of exams! Now sending email to the"
                  + " candidate personal email about it...");

          candidate.setExamStep(candidate.getExamStep() + 1);
          this.candidateRepository.save(candidate);
          sendMailApproved(
              1,
              email,
              "Вашата кандидатура е одобрена за изпит 1!",
              TEMPLATES.FIRST_EXAM_CANDIDATE,
              "Известие за кандидат е приет за изпит: " + email,
              TEMPLATES.NOTIFY_FIRST_EXAM_CANDIDATE);
        } else {
          sendMailRejected(candidate);
        }
      }
      case 2 -> {
        if (status.equals("approved")) {
          LOG.info(
              "Candidate approved to go to the second draft of exams! Now sending email to the"
                  + " candidate personal email about it...");

          candidate.setExamStep(candidate.getExamStep() + 1);
          this.candidateRepository.save(candidate);
          sendMailApproved(
              1,
              email,
              "Вие минахте успешно първият изпит",
              TEMPLATES.SECOND_EXAM_CANDIDATE,
              "Известие за кандидат приет за втори изпит: " + email,
              TEMPLATES.NOTIFY_SECOND_EXAM_CANDIDATE);
        } else {
          sendMailRejected(candidate);
        }
      }

      case 3 -> {
        if (status.equals("approved")) {
          LOG.info(
              "Candidate approved to go to become phd! Now sending email to the"
                  + " candidate personal email about it...");

          sendMailApproved(
              1,
              email,
              "Вие минахте успешно изпитите",
              TEMPLATES.THIRD_EXAM_CANDIDATE,
              "Заявка за създаване на докторант " + email,
              TEMPLATES.NOTIFY_THIRD_EXAM_CANDIDATE);

          Phd phd = this.phdMapper.toEntity(candidate);
          this.phdRepository.save(phd);
        } else {
          sendMailRejected(candidate);
        }
      }
    }
  }

  private void sendMailApproved(
      Integer examStep,
      String candidateEmail,
      String candidateTitle,
      TEMPLATES candidateTemplate,
      String docTitle,
      TEMPLATES docTemplate) {
    Tuple docRoles = examStep == 3 ? Tuple.of("admin", " ") : Tuple.of("expert", "manager");
    List<String> docEmails =
        this.databaseModel.selectMapString(
            "SELECT d.email FROM doctoral_center d JOIN doctoral_center_role dc ON(d.role ="
                + " dc.id) WHERE dc.role = $1 OR dc.role = $2",
            docRoles,
            "email");

    try {
      this.mailModel.send(
          candidateTitle, candidateTemplate, candidateEmail, Map.of("$CANDIDATE", candidateEmail));
    } catch (IOException exception) {
      LOG.error("Error in sending email to the doc center pesonnel: " + exception);
      throw new HttpException("Error in sending email to the candidate!");
    }

    LOG.info(
        "Now sending email for the doc center personnel to let the candidate into the"
            + " mandatory exams...");

    docEmails.forEach(
        docEmail -> {
          try {
            this.mailModel.send(
                docTitle, docTemplate, candidateEmail, Map.of("$CANDIDATE", candidateEmail));
          } catch (IOException exception) {
            LOG.error("Error in sending email to the doc center pesonnel: " + exception);
            throw new HttpException("Error in sending email to the doc center pesonnel!");
          }
        });
  }

  private void sendMailRejected(Candidate candidate) {
    candidate.setStatus(this.candidateStatusRepository.getByStatus("rejected"));
    this.candidateRepository.save(candidate);
    try {
      this.mailModel.send(
          "Вашата докторантска кандидатура в Ту-Варна", TEMPLATES.REJECTED, candidate.getEmail());
    } catch (IOException exception) {
      LOG.error("Error in sending email to the doc center pesonnel: " + exception);
      throw new HttpException("Error in sending email to the doc center pesonnel!");
    }
  }

  @Override
  public List<CandidateDTO> getCandidates(String fields) {
    LOG.info("Received a service request to retrieve all candidates");
    List<String> fieldsList = Arrays.asList(fields.split(","));

    fieldsList.replaceAll(
        (field) -> {
          String fieldStripped = field.strip();
          return switch (fieldStripped) {
            case "status" -> "s.status AS statusname ";
            case "faculty" -> "f.name AS facultyname ";
            case "curriculum" -> "cu.name AS curriculumname ";
            default -> "c." + fieldStripped + " ";
          };
        });

    String statement =
        "SELECT "
            + String.join(",", fieldsList)
            + "FROM candidate c JOIN candidate_status s ON (c.status=s.id) JOIN faculty f ON"
            + " (c.faculty=f.id) JOIN curriculum cu ON (c.curriculum=cu.id)";

    List<Candidate> candidates = this.databaseModel.selectMapEntity(statement, new Candidate());
    List<CandidateDTO> candidateDTOs = new ArrayList<>();
    candidates.forEach(candidate -> candidateDTOs.add(this.candidateMapper.toDto(candidate)));

    LOG.info("All candidates retrieved!");
    return candidateDTOs;
  }

  @Override
  @Transactional
  public List<Unauthorized> getUnauthorizedUsers() {
    LOG.info("Service received to retrieve all unauthorized users");
    List<Unauthorized> unauthorizedUsers = this.uRepository.getAll();

    return unauthorizedUsers;
  }

  @Override
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
