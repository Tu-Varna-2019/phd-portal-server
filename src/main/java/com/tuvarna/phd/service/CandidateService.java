package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.BlobDataDTO;
import com.tuvarna.phd.dto.CandidateApplyDTO;
import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CurriculumCreateDTO;
import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.dto.SubjectDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Faculty;
import java.util.List;

public sealed interface CandidateService permits CandidateServiceImpl {

  void apply(CandidateApplyDTO candidateDTO);

  void createCurriculum(CurriculumDTO curriculumDTO);

  List<Faculty> getFaculties();

  List<CurriculumDTO> getCurriculums();

  List<SubjectDTO> getSubjectsByCurriculum(String curriculumName);

  List<SubjectDTO> getSubjectsByFaculty(String faculty);

  List<CandidateDTO> getContests();

  List<CandidateDTO> getCandidatesInReview();

  void uploadBiography(BlobDataDTO file, String candidateName);

  void sendCandidateApplyEmails(String candidateEmail);

  void checkIfCandidateEmailIsPresent(String candidateEmail);

  void registerCandidate(Candidate candidate, CurriculumCreateDTO curriculumCreateDTO);
}
