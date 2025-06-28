package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import java.util.List;

public sealed interface CommitteeService permits CommitteeServiceImpl {

  List<CandidateDTO> getCandidates(String fields);
}
