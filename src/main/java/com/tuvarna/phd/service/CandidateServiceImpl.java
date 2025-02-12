package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.exception.CandidateException;
import com.tuvarna.phd.exception.IPBlockException;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.repository.CandidateRepository;
import com.tuvarna.phd.dto.CandidateDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class CandidateServiceImpl implements CandidateService {
  private final CandidateRepository candidateRepository;
  private final CandidateMapper candidateMapper;
  private final IPBlockService ipBlockService;

  @Inject private Logger LOG;

  @Inject
  public CandidateServiceImpl(
      IPBlockService ipBlockService,
      CandidateRepository candidateRepository,
      CandidateMapper candidateMapper) {
    this.candidateRepository = candidateRepository;
    this.candidateMapper = candidateMapper;
    this.ipBlockService = ipBlockService;
  }

  @Override
  public void register(CandidateDTO candidateDTO) {
    LOG.info("Recevived a service request to register a new candidate: " + candidateDTO.toString());
    Candidate candidate = this.candidateMapper.toEntity(candidateDTO);

    if (this.candidateRepository.getByEmail(candidate) != null) {
      LOG.error("Candidate email: " + candidate.getEmail() + " aleady exists!");
      throw new CandidateException("Error, email already exists!");
    }

    if (this.ipBlockService.isClientIPBlocked()) {
      throw new IPBlockException("Error, client is ip blocked!", 401);
    }

    LOG.info(
        "Good, candidate's email dosen't exist in the table neither he is ip blocked. Now adding"
            + " him...");
    this.candidateRepository.save(candidate);

    LOG.info("Candidate saved!");
  }
}
