package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.EvaluateGradeDTO;
import com.tuvarna.phd.dto.GradeDTO;
import java.util.List;

public sealed interface CommitteeService permits CommitteeServiceImpl {

  List<CandidateDTO> getCandidates(String fields);

  List<GradeDTO> getExams(String oid);

  void evaluateGrade(EvaluateGradeDTO evaluateGradeDTO, String evalUserType);
}
