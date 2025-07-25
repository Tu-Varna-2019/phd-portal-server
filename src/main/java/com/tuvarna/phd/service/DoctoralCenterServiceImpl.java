package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.NameDTO;
import com.tuvarna.phd.dto.NotificationDTO;
import com.tuvarna.phd.dto.UnauthorizedDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Commission;
import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.Grade;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.Subject;
import com.tuvarna.phd.entity.Unauthorized;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.mapper.GradeMapper;
import com.tuvarna.phd.mapper.PhdMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.model.MailModel.TEMPLATES;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CandidateStatusRepository;
import com.tuvarna.phd.repository.CommissionRepository;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.CommitteeRoleRepository;
import com.tuvarna.phd.repository.CommitteeTitleRepository;
import com.tuvarna.phd.repository.CurriculumRepository;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.DoctoralCenterRoleRepository;
import com.tuvarna.phd.repository.FacultyRepository;
import com.tuvarna.phd.repository.GradeRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import com.tuvarna.phd.repository.ReportRepository;
import com.tuvarna.phd.repository.SubjectRepository;
import com.tuvarna.phd.repository.UnauthorizedRepository;
import com.tuvarna.phd.utils.GradeUtils;
import com.tuvarna.phd.utils.GradeUtils.EVAL_USER_TYPE;
import com.tuvarna.phd.utils.PhdUtils;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.vertx.core.impl.NoStackTraceThrowable;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class DoctoralCenterServiceImpl implements DoctoralCenterService {
  @Inject DoctoralCenterRepository doctoralCenterRepository;
  @Inject DoctoralCenterRoleRepository doctoralCenterRoleRepository;
  @Inject PhdRepository phdRepository;
  @Inject PhdStatusRepository phdStatusRepository;
  @Inject FacultyRepository facultyRepository;

  @Inject CommitteeRoleRepository committeeRoleRepository;
  @Inject CommitteeRepository committeeRepository;
  @Inject CommitteeTitleRepository committeeTitleRepository;

  @Inject CandidateRepository candidateRepository;
  @Inject CandidateStatusRepository candidateStatusRepository;
  @Inject GradeRepository gradeRepository;
  @Inject SubjectRepository subjectRepository;
  @Inject UnauthorizedRepository uRepository;
  @Inject ReportRepository reportRepository;
  @Inject CommissionRepository commissionRepository;
  @Inject CurriculumRepository curriculumRepository;

  @Inject PhdUtils phdUtils;
  @Inject GradeUtils gradeUtils;

  @Inject CandidateMapper candidateMapper;
  @Inject PhdMapper phdMapper;
  @Inject GradeMapper gradeMapper;

  @Inject DatabaseModel databaseModel;
  @Inject MailModel mailModel;

  @Inject NotificationService notificationService;

  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterServiceImpl.class);

  @ConfigProperty(name = "client.base-url")
  private String clientBaseURL;

  @Override
  @Transactional
  @CacheInvalidate(cacheName = "doc-center-candidates-cache")
  @CacheInvalidate(cacheName = "doc-center-exams-cache")
  @CacheInvalidate(cacheName = "committee-candidates-cache")
  public void review(String email, String status) throws IOException {
    LOG.info("Service received a request to review candidate: " + email);
    Candidate candidate = this.candidateRepository.getByEmail(email);

    if (status.equals("approved") && candidate.getStatus().getStatus().equals("rejected")) {
      LOG.error(
          "Doctoral center shouldn't be able to change candidate's status to accepted if he's been"
              + " rejected already!");
      throw new HttpException(
          "You are not able to change candidate's status to accepted if he's been"
              + " rejected already!");
    }

    switch (candidate.getExamStep()) {
      case 1 -> {
        if (status.equals("approved")) {
          LOG.info(
              "Candidate approved to go to the first draft of exams! Now sending email to the"
                  + " candidate personal email about it...");

          candidate.setExamStep(2);
          Set<Grade> grades =
              setMandatoryExams(
                  List.of("English", "Methods of Research and Development of dissertation"));

          candidate.setGrades(grades);
          this.candidateRepository.save(candidate);

          sendMailApproved(
              Tuple.of("expert", "manager"),
              candidate.getEmail(),
              "Вашата кандидатура е одобрена за изпит 1!",
              TEMPLATES.FIRST_EXAM_CANDIDATE,
              "Известие за кандидат е приет за изпит: " + email,
              TEMPLATES.NOTIFY_FIRST_EXAM_CANDIDATE);
        } else {
          candidate.setExamStep(1);
          this.candidateRepository.save(candidate);
          sendMailRejected(candidate);
        }
      }
      case 2 -> {
        if (status.equals("approved")) {
          checkIfGradesAreEvaluated(candidate.getGrades(), candidate.getExamStep());

          LOG.info(
              "Candidate approved to go to the second draft of exams! Now sending email to the"
                  + " candidate personal email about it...");

          candidate.setExamStep(3);

          Set<Grade> grades = setMandatoryExams(List.of(candidate.getCurriculum().getName()));
          grades.addAll(candidate.getGrades());
          candidate.setGrades(grades);
          this.candidateRepository.save(candidate);

          sendMailApproved(
              Tuple.of("expert", "manager"),
              candidate.getEmail(),
              "Вие минахте успешно първият изпит",
              TEMPLATES.SECOND_EXAM_CANDIDATE,
              "Известие за кандидат приет за втори изпит: " + email,
              TEMPLATES.NOTIFY_SECOND_EXAM_CANDIDATE);
        } else {
          candidate.setExamStep(1);
          this.candidateRepository.save(candidate);
          sendMailRejected(candidate);
        }
      }

      case 3 -> {
        if (status.equals("approved")) {
          checkIfGradesAreEvaluated(candidate.getGrades(), candidate.getExamStep());
          LOG.info(
              "Candidate approved to become phd! Now sending email to the"
                  + " candidate personal email about it...");

          sendMailApproved(
              Tuple.of("admin", " "),
              email,
              "Вие минахте успешно изпитите",
              TEMPLATES.THIRD_EXAM_CANDIDATE,
              "Заявка за създаване на докторант " + email,
              TEMPLATES.NOTIFY_THIRD_EXAM_CANDIDATE);

          Phd phd = this.phdMapper.toEntity(candidate);
          phd.setStatus(this.phdStatusRepository.getByStatus("enrolled"));
          this.candidateRepository.deleteById(candidate.getId());

          this.phdUtils.generateReport(phd);
          this.phdUtils.generateExams(phd);
          this.phdRepository.save(phd);
        } else {
          candidate.setExamStep(1);
          this.candidateRepository.save(candidate);
          sendMailRejected(candidate);
        }
      }
    }
  }

  private void checkIfGradesAreEvaluated(Set<Grade> grades, Integer examStep) {
    for (Grade grade : grades) {
      if (grade.getGrade() == null) {
        LOG.error("Cannot move to exam step: " + examStep + " because no grades were evaluated!");

        throw new HttpException(
            "Cannot move to exam step: " + examStep + " because no grades were evaluated!");
      }
    }
  }

  private Set<Grade> setMandatoryExams(List<String> subjectNames) {
    Set<Subject> subjects = new HashSet<Subject>();
    Set<Grade> grades = new HashSet<Grade>();

    subjectNames.forEach(
        name -> {
          subjects.add(this.subjectRepository.getByName(name));
        });

    subjects.forEach(
        subject -> {
          Date currentDate = new Date();
          currentDate.setMonth(currentDate.getMonth() + 1);
          Grade grade =
              new Grade(
                  new java.sql.Date(currentDate.getTime()),
                  "Задължителен изпит по " + subject.getName(),
                  subject);
          grades.add(grade);
          this.gradeRepository.save(grade);
        });

    return grades;
  }

  private void sendMailApproved(
      Tuple notifiedDocRoles,
      String candidateEmail,
      String candidateTitle,
      TEMPLATES candidateTemplate,
      String docTitle,
      TEMPLATES docTemplate) {
    List<String> docEmails =
        this.databaseModel.getListString(
            "SELECT d.email FROM doctoral_center d JOIN doctoral_center_role dc ON(d.role ="
                + " dc.id) WHERE dc.role = $1 OR dc.role = $2",
            notifiedDocRoles,
            "email");

    try {
      this.mailModel.send(
          candidateTitle, candidateTemplate, candidateEmail, Map.of("$CANDIDATE", candidateEmail));
    } catch (IOException | NoStackTraceThrowable exception) {
      LOG.error("Error in sending email to the canidate: " + exception);
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
          } catch (IOException | NoStackTraceThrowable exception) {
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
    } catch (IOException | NoStackTraceThrowable exception) {
      LOG.error("Error in sending email to the doc center pesonnel: " + exception);
      throw new HttpException("Error in sending email to the doc center pesonnel!");
    }
  }

  @Override
  // @CacheResult(cacheName = "doc-center-candidates-cache")
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

    List<Candidate> candidates = this.databaseModel.getListEntity(statement, new Candidate());
    List<CandidateDTO> candidateDTOs = new ArrayList<>();
    candidates.forEach(candidate -> candidateDTOs.add(this.candidateMapper.toDto(candidate)));

    LOG.info("All candidates retrieved!");
    return candidateDTOs;
  }

  @Override
  @Transactional
  // @CacheResult(cacheName = "doc-center-unauth-users-cache")
  public List<Unauthorized> getUnauthorizedUsers() {
    LOG.info("Service received to retrieve all unauthorized users");
    List<Unauthorized> unauthorizedUsers = this.uRepository.getAll();

    return unauthorizedUsers;
  }

  @Override
  @Transactional
  // @CacheResult(cacheName = "doc-center-exams-cache")
  public List<GradeDTO> getExams() {
    LOG.info("Service received to retrieve all grades");

    List<GradeDTO> gradeDTOs = new ArrayList<>();
    this.gradeRepository
        .getAll()
        .forEach(
            grade -> {
              UserDTO userDTO =
                  this.gradeUtils.queryEvaluatedUsers(grade.getId(), EVAL_USER_TYPE.phd_candidate);
              gradeDTOs.add(this.gradeMapper.toDto(grade, userDTO));
            });

    return gradeDTOs;
  }

  @Override
  @Transactional
  @CacheInvalidate(cacheName = "doc-center-exams-cache")
  public void setCommissionOnGrade(Long id, String name) {
    LOG.info("Received a service request to set commission: " + name + " to grade id: " + id);
    Grade grade = this.gradeRepository.getById(id);
    Commission commission = this.commissionRepository.getByName(name);

    grade.setCommission(commission);
    this.gradeRepository.save(grade);
    LOG.info(
        "Grade is successfully set to the commision! Now notifying the corresponding"
            + " committees...");
    notifyCommittees(commission.getMembers(), grade.getEvalDate());
  }

  private void notifyCommittees(Set<Committee> committees, Date evalDate) {
    List<String> committeeEmails = new ArrayList<>();

    committees.forEach(
        committee -> {
          try {
            // TODO:: Make the title more informative
            this.mailModel.send(
                "Вие сте добавен в изпит",
                TEMPLATES.COMMITTEE_ADDED_TO_EXAM,
                committee.getEmail(),
                Map.of("$EVAL_DATE", evalDate.toString()));
          } catch (IOException | NoStackTraceThrowable exception) {
            LOG.error(
                "Error in sending email to committee: "
                    + committee.getEmail()
                    + " with exception: "
                    + exception);
            throw new HttpException(
                "Error in sending email to the committee with email: " + committee.getEmail());
          }

          committeeEmails.add(committee.getEmail());
        });

    if (!committeeEmails.isEmpty()) {
      this.notificationService.save(
          new NotificationDTO(
              "Вие сте добавен в изпит за оценяване",
              "Вие сте добавен в изпит за оценяване. Крайна дата: " + evalDate.toString(),
              "info",
              new Timestamp(System.currentTimeMillis()),
              "single",
              committeeEmails));
    } else {
      LOG.warn("No emails to notify. Skipping...");
    }

    LOG.info("Committees have successfully received emails: " + committeeEmails.toString() + " !");
  }

  @Override
  @Transactional
  // @CacheResult(cacheName = "doc-center-commission-cache")
  public List<NameDTO> getCommisions() {
    LOG.info("Service received to retrieve all commisions");
    List<NameDTO> commisionNames = new ArrayList<>();

    this.databaseModel
        .getListString("SELECT name FROM commission", "name")
        .forEach(
            (name) -> {
              commisionNames.add(new NameDTO(name));
            });

    return commisionNames;
  }

  @Override
  @Transactional
  @CacheInvalidate(cacheName = "doc-center-unauth-users-cache")
  public void setUnauthorizedUserGroup(List<UnauthorizedDTO> usersDTO, String group) {
    LOG.info(
        "Service received a request to set a role: "
            + group
            + " for unauthorized user: "
            + usersDTO.toString());

    for (UnauthorizedDTO userDTO : usersDTO) {
      Unauthorized user = this.uRepository.getByOid(userDTO.getOid());
      switch (group) {
        case "phd" -> {
          Phd phd = new Phd(userDTO.getOid(), userDTO.getName(), userDTO.getEmail());
          phd.setStatus(this.phdStatusRepository.getByStatus("enrolled"));

          // NOTE: Have to set a curriculum for the reports to get generated
          phd.setCurriculum(
              this.curriculumRepository.getByNameAndModeId(
                  "Automated information processing and management systems",
                  this.databaseModel.getLong(
                      "SELECT id FROM mode WHERE mode = $1", Tuple.of("regular"), "id")));

          this.phdRepository.save(phd);
          this.phdUtils.generateReport(phd);
        }

        case "committee" -> {
          Committee committee =
              new Committee(userDTO.getOid(), userDTO.getName(), userDTO.getEmail());
          // NOTE: Give the option for the user to decide which faculty the committee belongs to
          committee.setFaculty(this.facultyRepository.getByName("Software engineering"));
          // NOTE: Give the option for the user to decide which role the committee belongs to
          committee.setRole(this.committeeRoleRepository.getByRole("member"));
          // NOTE: Give the option for the user to decide which title the committee belongs to
          committee.setTitle(this.committeeTitleRepository.getByTitle("assistant"));
          this.committeeRepository.save(committee);
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
  @CacheResult(cacheName = "doc-center-roles-cache")
  public List<String> getDoctoralCenterRoles() {
    LOG.info("Received a request to retrieve all doctoral center roles");
    List<String> docCenterPermitRoles = List.of("phd", "committee");

    LOG.info("All doc center roles have been retrieved: " + docCenterPermitRoles.toString());
    return docCenterPermitRoles;
  }
}
