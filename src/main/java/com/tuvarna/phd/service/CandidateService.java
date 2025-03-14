package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import java.util.List;

public sealed interface CandidateService permits CandidateServiceImpl {
  void register(CandidateDTO candidateDTO);

  List<CurriculumDTO> getCurriculums();
}
