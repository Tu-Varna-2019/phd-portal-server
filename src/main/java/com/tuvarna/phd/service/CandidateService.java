package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.dto.SubjectDTO;
import com.tuvarna.phd.entity.Faculty;
import java.util.List;

public sealed interface CandidateService permits CandidateServiceImpl {

  void apply(CandidateDTO candidateDTO);

  List<Faculty> getFaculties();

  List<CurriculumDTO> getCurriculums();

  List<SubjectDTO> getSubjects(String curriculumName);

  List<CandidateDTO> getContests();

  List<CandidateDTO> getCandidatesInReview();
}
