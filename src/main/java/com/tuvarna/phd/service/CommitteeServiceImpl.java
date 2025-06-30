package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.EvaluateGradeDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.mapper.GradeMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.repository.CommissionRepository;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.GradeRepository;
import com.tuvarna.phd.repository.ReportRepository;
import com.tuvarna.phd.utils.GradeUtils;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class CommitteeServiceImpl implements CommitteeService {
  @Inject CommitteeRepository committeeRepository;
  @Inject ReportRepository reportRepository;
  @Inject GradeRepository gradeRepository;
  @Inject CommissionRepository commissionRepository;

  @Inject DatabaseModel databaseModel;
  @Inject MailModel mailModel;

  @Inject GradeUtils gradeUtils;

  @Inject CandidateMapper candidateMapper;
  @Inject GradeMapper gradeMapper;

  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterServiceImpl.class);

  @Override
  @CacheResult(cacheName = "committee-candidates-cache")
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
  @CacheResult(cacheName = "committee-exams-cache")
  public List<GradeDTO> getExams(String oid) {
    LOG.info("Service received to retrieve all grades");

    List<Long> commissionIds =
        this.databaseModel.selectMapLong(
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
                UserDTO userDTO = this.gradeUtils.queryEvaluatedUser(grade.getId());
                gradeDTOs.add(this.gradeMapper.toDto(grade, userDTO));
              }
            });

    return gradeDTOs;
  }

  @Override
  @Transactional
  @CacheInvalidate(cacheName = "committee-exams-cache")
  public void evaluateGrade(EvaluateGradeDTO evaluateGradeDTO, String evalUserType) {
    LOG.info("Received a service request to evaluate user type: " + evalUserType);

    this.databaseModel.execute(
        "UPDATE grade SET grade = $1 WHERE subject = (SELECT s.id FROM subject s WHERE"
            + " s.name= $2) AND id = (SELECT gg.grade_id FROM "
            + evalUserType
            + "s_grades gg WHERE gg."
            + evalUserType
            + "_id = (SELECT pc.id FROM "
            + evalUserType
            + " pc WHERE pc.pin = $3))",
        Tuple.of(
            evaluateGradeDTO.getGrade(), evaluateGradeDTO.getSubject(), evaluateGradeDTO.getPin()));

    LOG.info("Changed grade to user: " + evalUserType);
  }
}
