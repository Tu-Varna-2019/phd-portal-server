package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.SubjectDTO;
import com.tuvarna.phd.entity.Faculty;
import java.util.List;

public sealed interface PhdService permits PhdServiceImpl {
  List<CurriculumDTO> getCurriculums();

  List<Faculty> getFaculties();

  List<GradeDTO> getGrades(String oid);

  void setAttachmentToGrade(Long gradeId, List<String> attachments);

  List<SubjectDTO> getSubjectsByCurriculum(String curriculumName);

  List<SubjectDTO> getSubjectsByFaculty(String faculty);
}
