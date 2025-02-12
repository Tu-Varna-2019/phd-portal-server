package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;

public sealed interface CandidateService permits CandidateServiceImpl {
  void register(CandidateDTO candidateDTO);
}
