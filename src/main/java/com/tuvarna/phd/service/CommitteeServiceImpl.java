package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.model.MailModel;
import com.tuvarna.phd.repository.CommissionRepository;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.repository.ReportRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class CommitteeServiceImpl implements CommitteeService {
  @Inject CommitteeRepository committeeRepository;
  @Inject ReportRepository reportRepository;
  @Inject CommissionRepository commissionRepository;

  @Inject DatabaseModel databaseModel;
  @Inject MailModel mailModel;
  @Inject CandidateMapper candidateMapper;

  @Inject private Logger LOG = Logger.getLogger(DoctoralCenterServiceImpl.class);

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
}
