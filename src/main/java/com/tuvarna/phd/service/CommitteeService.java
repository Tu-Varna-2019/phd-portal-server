package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CommissionDTO;
import com.tuvarna.phd.dto.CommissionRequestDTO;
import com.tuvarna.phd.dto.EvaluateGradeDTO;
import com.tuvarna.phd.dto.GradeDTO;
import java.util.List;
import java.util.Optional;

public sealed interface CommitteeService permits CommitteeServiceImpl {

  List<CandidateDTO> getCandidates(String fields);

  List<GradeDTO> getExams(String oid);

  List<CommissionDTO> getCommissions(String oid);

  void createCommission(CommissionRequestDTO commissionDTO);

  void modifyCommission(CommissionRequestDTO commissionDTO, Optional<String> name);

  void evaluateGrade(EvaluateGradeDTO evaluateGradeDTO, String evalUserType, String oid);
}
