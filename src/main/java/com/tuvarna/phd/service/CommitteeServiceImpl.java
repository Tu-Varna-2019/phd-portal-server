package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CommissionDTO;
import com.tuvarna.phd.dto.CommissionRequestDTO;
import com.tuvarna.phd.dto.CommitteeDTO;
import com.tuvarna.phd.dto.EvaluateGradeDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Commission;
import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.entity.CommitteeGrade;
import com.tuvarna.phd.entity.Grade;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.mapper.CommissionMapper;
import com.tuvarna.phd.mapper.CommitteeMapper;
import com.tuvarna.phd.mapper.GradeMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.repository.CommissionRepository;
import com.tuvarna.phd.repository.CommitteeGradeRepository;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.GradeRepository;
import com.tuvarna.phd.repository.ReportRepository;
import com.tuvarna.phd.utils.GradeUtils;
import com.tuvarna.phd.utils.GradeUtils.EVAL_USER_TYPE;
import io.quarkus.cache.CacheInvalidate;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class CommitteeServiceImpl implements CommitteeService {
  @Inject CommitteeRepository committeeRepository;
  @Inject ReportRepository reportRepository;
  @Inject GradeRepository gradeRepository;
  @Inject CommissionRepository commissionRepository;
  @Inject CommitteeGradeRepository committeeGradeRepository;

  @Inject DatabaseModel databaseModel;
  @Inject MailModel mailModel;

  @Inject GradeUtils gradeUtils;

  @Inject CandidateMapper candidateMapper;
  @Inject GradeMapper gradeMapper;
  @Inject CommissionMapper commissionMapper;
  @Inject CommitteeMapper committeeMapper;

  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterServiceImpl.class);

  @Override
  // @CacheResult(cacheName = "committee-candidates-cache")
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
  // @CacheResult(cacheName = "committee-commissions-cache")
  public List<CommissionDTO> getCommissions(String oid) {
    LOG.info("Service received to retrieve all commissions");
    List<CommissionDTO> commissionDTOs = new ArrayList<>();

    this.commissionRepository
        .getAll()
        .forEach(
            commission -> {
              if (Committee.getOids(commission.getMembers()).contains(oid)) {
                commissionDTOs.add(this.commissionMapper.toDto(commission));
              }
            });

    return commissionDTOs;
  }

  @Override
  // @CacheResult(cacheName = "committee-committees-cache")
  public List<CommitteeDTO> getCommittees() {
    LOG.info("Service received to retrieve all committees");
    List<CommitteeDTO> committeeDTOs = new ArrayList<>();

    this.committeeRepository
        .getAll()
        .forEach(
            committee -> {
              committeeDTOs.add(this.committeeMapper.toDto(committee));
            });

    return committeeDTOs;
  }

  @Override
  @Transactional
  @CacheInvalidate(cacheName = "committee-commissions-cache")
  public void createCommission(CommissionRequestDTO commissionDTO) {
    LOG.info("Service received to create commission with name " + commissionDTO.getName());

    Boolean doesCommissionNameExists =
        this.databaseModel.getBoolean(
            "SELECT EXISTS (SELECT 1 FROM commission WHERE name = $1)",
            Tuple.of(commissionDTO.getName()));

    if (doesCommissionNameExists) {
      throw new HttpException("Commission name already exists!", 400);
    }

    Commission commission = new Commission();
    commission.setName(commissionDTO.getName());
    Set<Committee> committees = new HashSet<Committee>();

    commissionDTO
        .getCommittees()
        .forEach(
            committee -> {
              committees.add(this.committeeRepository.getByOid(committee.getOid()));
            });

    commission.setMembers(committees);
    this.commissionRepository.save(commission);
  }

  @Override
  @Transactional
  @CacheInvalidate(cacheName = "committee-commissions-cache")
  public void deleteCommission(String name) {
    LOG.info("Service received to delete commission with name " + name);

    this.commissionRepository.deleteByName(name);
  }

  @Override
  @Transactional
  @CacheInvalidate(cacheName = "committee-commissions-cache")
  public void modifyCommission(CommissionRequestDTO commissionDTO, Optional<String> name) {
    LOG.info("Service received to modify commission with name " + commissionDTO.getName());

    Commission commission = this.commissionRepository.getByName(commissionDTO.getName());

    Set<Committee> committees = new HashSet<Committee>();
    commissionDTO
        .getCommittees()
        .forEach(
            committee -> {
              committees.add(this.committeeRepository.getByOid(committee.getOid()));
            });

    commission.setMembers(committees);
    if (name.isPresent()) {
      commission.setName(name.get());
    }

    this.commissionRepository.save(commission);
  }

  @Override
  // @CacheResult(cacheName = "committee-exams-cache")
  public List<GradeDTO> getExams(String oid) {
    LOG.info("Service received to retrieve all grades");

    List<Long> commissionIds =
        this.databaseModel.getListLong(
            "SELECT cm.id FROM commission cm JOIN commission_committees cmc ON"
                + " (cm.id=cmc.commission_id) JOIN committee c ON (cmc.committee_id=c.id) WHERE"
                + " c.oid = $1",
            Tuple.of(oid),
            "id");

    if (commissionIds.isEmpty()) {
      LOG.info("Didn't find any commissions. Moving on...");
      return null;
    }

    List<GradeDTO> gradeDTOs = new ArrayList<>();
    this.gradeRepository
        .getAll()
        .forEach(
            grade -> {
              if (grade.getCommission() != null
                  && commissionIds.contains(grade.getCommission().getId())) {
                UserDTO userDTO =
                    this.gradeUtils.queryEvaluatedUsers(
                        grade.getId(), EVAL_USER_TYPE.phd_candidate);

                List<CommitteeDTO> committeeDTOs = new ArrayList<>();
                grade
                    .getCommission()
                    .getMembers()
                    .forEach(
                        committee -> {
                          Double committeeGrade = null;
                          try {
                            committeeGrade =
                                this.databaseModel.getDouble(
                                    "SELECT grade FROM committee_grade WHERE"
                                        + " committee_id = $1 AND grade_id = $2",
                                    Tuple.of(committee.getId(), grade.getId()),
                                    "grade");
                          } catch (NoSuchElementException exception) {
                            LOG.warn(
                                "Committee: "
                                    + committee.getName()
                                    + " hasn't evaluated grade with id: "
                                    + grade.getId()
                                    + " yet!");
                          }

                          committeeDTOs.add(
                              new CommitteeDTO(
                                  committee.getOid(),
                                  committee.getName(),
                                  committee.getEmail(),
                                  committee.getPicture(),
                                  committeeGrade,
                                  committee.getFaculty().getName(),
                                  committee.getRole().getRole()));
                        });

                GradeDTO gradeDTO =
                    new GradeDTO(
                        grade.getId(),
                        grade.getGrade(),
                        grade.getEvalDate(),
                        new CommissionDTO(grade.getCommission().getName(), committeeDTOs),
                        grade.getReport(),
                        grade.getAttachments(),
                        userDTO,
                        grade.getSubject().getName());

                // NOTE: Cannot use gradeMapper because we need to get the commitete grade from the
                // committee_grade table
                // this.gradeMapper.toDto(grade, userDTO)
                gradeDTOs.add(gradeDTO);
              }
            });

    return gradeDTOs;
  }

  @Override
  @Transactional
  @CacheInvalidate(cacheName = "committee-exams-cache")
  public void evaluateGrade(EvaluateGradeDTO evaluateGradeDTO, String evalUserType, String oid) {
    LOG.info("Received a service request to evaluate user type: " + evalUserType);
    Committee committee = this.committeeRepository.getByOid(oid);

    Long gradeID =
        this.databaseModel.getLong(
            "SELECT g.id FROM grade g WHERE g.subject = (SELECT s.id FROM subject s WHERE"
                + " s.name= $2) AND id = (SELECT gg.grade_id FROM "
                + evalUserType
                + "s_grades gg WHERE gg."
                + evalUserType
                + "_id = (SELECT pc.id FROM "
                + evalUserType
                + " pc WHERE pc.pin = $3) LIMIT 1)",
            Tuple.of(
                evaluateGradeDTO.getGrade(),
                evaluateGradeDTO.getSubject(),
                evaluateGradeDTO.getPin()),
            "id");

    LOG.info("Found grade id is: " + gradeID);

    Boolean isCommiteeModifyingGrade =
        this.databaseModel.getBoolean(
            "SELECT EXISTS (SELECT 1 FROM committee_grade cg WHERE cg.grade_id = $1 AND"
                + " cg.committee_id = $2)",
            Tuple.of(gradeID, committee.getId()));
    if (isCommiteeModifyingGrade) {
      LOG.info(
          "Grade has already been evaluated by committee: "
              + committee.getName()
              + ". Modifying grade to: "
              + evaluateGradeDTO.getGrade()
              + " now...");

      this.databaseModel.execute(
          "UPDATE committee_grade SET grade = $1 WHERE grade_id = $2 AND committee_id =" + " $3",
          Tuple.of(evaluateGradeDTO.getGrade(), gradeID, committee.getId()));
    } else {
      LOG.info(
          "Grade ISN'T evaluated by committee: "
              + committee.getName()
              + ". Creating grade to: "
              + evaluateGradeDTO.getGrade()
              + " now...");
      this.committeeGradeRepository.save(
          new CommitteeGrade(
              committee, this.gradeRepository.getById(gradeID), evaluateGradeDTO.getGrade()));
    }

    LOG.info("Now checking if all committees have evaluated the exam to calculate the medium");

    Boolean isAllCommitteesEvaluatedGrade =
        !this.databaseModel.getBoolean(
            "SELECT EXISTS (SELECT 1 FROM committee_grade cg JOIN commission_committees cc"
                + " ON(cg.committee_id=cc.committee_id) WHERE cg.grade_id = $1 AND cg.committee_id"
                + " = $2 AND cc.commission_id = $3 AND cg.grade IS NULL)",
            Tuple.of(gradeID, committee.getId(), committee.getId()));

    if (isAllCommitteesEvaluatedGrade) {
      LOG.info(
          "All committees have evaluated grade for the grade id: "
              + gradeID
              + ". Now creating a sum of the grades");

      Double gradeSum =
          this.databaseModel.getDouble(
              "SELECT AVG(cg.grade) AS grade_avg"
                  + " FROM committee_grade cg JOIN grade g ON(cg.grade_id=g.id) WHERE g.id = $1",
              Tuple.of(gradeID),
              "grade_avg");
      Grade grade = this.gradeRepository.getById(gradeID);
      grade.setGrade(gradeSum);
      this.gradeRepository.save(grade);
    } else {
      LOG.info(
          "All committees haven't yet evaluated grade for the grade id: " + gradeID + ". Skipping");
    }

    LOG.info("Changed grade to user: " + evalUserType);
  }
}
