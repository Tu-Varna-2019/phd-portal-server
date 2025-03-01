package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Curriculum;
import com.tuvarna.phd.entity.Mode;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.mapper.CurriculumMapper;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.repository.CurriculumRepository;
import com.tuvarna.phd.repository.FacultyRepository;
import com.tuvarna.phd.repository.ModeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class CandidateServiceImpl implements CandidateService {
  @Inject CandidateRepository candidateRepository;
  @Inject CurriculumRepository curriculumRepository;
  @Inject FacultyRepository facultyRepository;
  @Inject ModeRepository modeRepository;
  @Inject IPBlockService ipBlockService;

  @Inject CandidateMapper candidateMapper;
  @Inject CurriculumMapper curriculumMapper;

  @Inject private Logger LOG;

  @Override
  public void register(CandidateDTO candidateDTO) {
    LOG.info("Recevived a service request to register a new candidate: " + candidateDTO.toString());

    if (this.candidateRepository.getByEmail(candidateDTO.getEmail()) != null) {
      LOG.error("Candidate email: " + candidateDTO.getEmail() + " aleady exists!");
      throw new HttpException("Error, email already exists!");
    }

    if (this.ipBlockService.isClientIPBlocked()) {
      throw new HttpException("Error, client is ip blocked!", 401);
    }

    LOG.info(
        "Good, candidate's email dosen't exist in the table neither he is ip blocked. Now"
            + " registering him...");

    Candidate candidate = this.candidateMapper.toEntity(candidateDTO);

    Mode modeFound = this.modeRepository.getByMode(candidateDTO.getCurriculum().getMode());
    candidate.setCurriculum(
        curriculumRepository.getByDescriptionAndModeId(
            candidateDTO.getCurriculum().getDescription(), modeFound.getId()));

    this.candidateRepository.save(candidate);

    LOG.info("Candidate saved!");
  }

  @Override
  public List<CurriculumDTO> getCurriculums() {
    LOG.info("Received a service request to retrieve all curriculums");
    List<Curriculum> curriculums = this.curriculumRepository.getAll();
    List<CurriculumDTO> curriculumDTOs = new ArrayList<>();

    curriculums.forEach(
        (curriculum) -> {
          curriculumDTOs.add(this.curriculumMapper.toDto(curriculum));
        });

    LOG.info("All curriculums retrieved!");
    return curriculumDTOs;
  }
}
