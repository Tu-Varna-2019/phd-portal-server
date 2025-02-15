package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.exception.CandidateException;
import com.tuvarna.phd.exception.IPBlockException;
import com.tuvarna.phd.mapper.CandidateMapper;
import com.tuvarna.phd.repository.CandidateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class CandidateServiceImpl implements CandidateService {
  @Inject CandidateRepository candidateRepository;
  @Inject CandidateMapper candidateMapper;
  @Inject IPBlockService ipBlockService;

  @Inject private Logger LOG;

  @Override
  public void register(CandidateDTO candidateDTO) {
    LOG.info("Recevived a service request to register a new candidate: " + candidateDTO.toString());
    Candidate candidate = this.candidateMapper.toEntity(candidateDTO);

    if (this.candidateRepository.getByEmail(candidate.getEmail()) != null) {
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
