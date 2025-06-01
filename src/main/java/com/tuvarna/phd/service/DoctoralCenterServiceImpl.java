package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.NameDTO;
import com.tuvarna.phd.dto.NotificationDTO;
import com.tuvarna.phd.dto.UnauthorizedDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Commission;
import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.Grade;
import com.tuvarna.phd.entity.Mode;
import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.entity.Report;
import com.tuvarna.phd.entity.Subject;
import com.tuvarna.phd.entity.Supervisor;
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
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.DoctoralCenterRoleRepository;
import com.tuvarna.phd.repository.GradeRepository;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import com.tuvarna.phd.repository.ReportRepository;
import com.tuvarna.phd.repository.SubjectRepository;
import com.tuvarna.phd.repository.SupervisorRepository;
import com.tuvarna.phd.repository.UnauthorizedRepository;
import io.quarkus.cache.CacheResult;
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
  @Inject CommitteeRepository committeeRepository;
  @Inject SupervisorRepository supervisorRepository;
  @Inject CandidateRepository candidateRepository;
  @Inject CandidateStatusRepository candidateStatusRepository;
  @Inject GradeRepository gradeRepository;
  @Inject SubjectRepository subjectRepository;
  @Inject UnauthorizedRepository uRepository;
  @Inject ReportRepository reportRepository;
  @Inject CommissionRepository commissionRepository;

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
          LOG.info(
              "Candidate approved to go to the second draft of exams! Now sending email to the"
                  + " candidate personal email about it...");

          candidate.setExamStep(3);
          Set<Grade> grades = setMandatoryExams(List.of(candidate.getCurriculum().getName()));

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

          generateReport(phd);
        } else {
          candidate.setExamStep(1);
          this.candidateRepository.save(candidate);
          sendMailRejected(candidate);
        }
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
        this.databaseModel.selectMapString(
            "SELECT d.email FROM doctoral_center d JOIN doctoral_center_role dc ON(d.role ="
                + " dc.id) WHERE dc.role = $1 OR dc.role = $2",
            notifiedDocRoles,
            "email");

    try {
      this.mailModel.send(
          candidateTitle, candidateTemplate, candidateEmail, Map.of("$CANDIDATE", candidateEmail));
    } catch (IOException exception) {
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

  private void generateReport(Phd phd) {
    Integer MONTHLY_REPORT_DELAY = 3;

    Set<Report> reports = new HashSet<>();
    Date currentDate = new Date();
    currentDate.setMonth(currentDate.getMonth() + Report.TIME_MONTH_DELAY_CANDIDATE_APPROVAL);
    phd.setEnrollDate(new java.sql.Date(currentDate.getTime()));

    for (Integer year = 0; year < phd.getCurriculum().getMode().getYearPeriod(); year++) {
      LOG.info("Now generating the report for year: " + year);
      for (Integer month = 0; month < 9; month += 3) {
        // TODO: Verify how to generate the order number
        // Currently it's unknown to me
        Date monthDate = new Date();
        monthDate.setTime(currentDate.getTime());
        monthDate.setMonth(month + MONTHLY_REPORT_DELAY);
        monthDate.setYear(currentDate.getYear() + year);

        Report report =
            new Report(
                "Индивидуален тримесечен учебен план за подготовка за докоторант",
                Mode.modeBGtoEN.get(phd.getCurriculum().getMode().getMode()),
                monthDate,
                month + 1);
        this.reportRepository.save(report);
        reports.add(report);
        LOG.info("Now generating the report for month: " + month);
      }

      Date yearDate = new Date();
      yearDate.setTime(currentDate.getTime());
      yearDate.setMonth(11);
      yearDate.setYear(currentDate.getYear() + year);

      Report report =
          new Report(
              "Индивидуален годишен учебен план за подготовка за докоторант",
              Mode.modeBGtoEN.get(phd.getCurriculum().getMode().getMode()),
              yearDate,
              4);
      this.reportRepository.save(report);
      reports.add(report);
    }

    phd.setReports(reports);
    this.phdRepository.save(phd);
    LOG.info("Phd report created successfully!");
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
  public List<GradeDTO> getExams() {
    LOG.info("Service received to retrieve all grades");

    List<GradeDTO> gradeDTOs = new ArrayList<>();
    this.gradeRepository
        .getAll()
        .forEach(
            grade -> {
              gradeDTOs.add(this.gradeMapper.toDto(grade));
            });

    return gradeDTOs;
  }

  @Override
  @Transactional
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

  private void notifyCommittees(List<Committee> committees, Date evalDate) {
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
          } catch (IOException exception) {
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
  public List<NameDTO> getCommision() {
    LOG.info("Service received to retrieve all commisions");
    List<NameDTO> commisionNames = new ArrayList<>();

    this.databaseModel
        .selectMapString("SELECT name FROM commission", "name")
        .forEach(
            (name) -> {
              commisionNames.add(new NameDTO(name));
            });

    return commisionNames;
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
