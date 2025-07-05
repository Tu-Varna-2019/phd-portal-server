package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.NameDTO;
import com.tuvarna.phd.dto.UnauthorizedDTO;
import com.tuvarna.phd.entity.Unauthorized;
import java.io.IOException;
import java.util.List;

public sealed interface DoctoralCenterService permits DoctoralCenterServiceImpl {

  void review(String email, String status) throws IOException;

  List<String> getDoctoralCenterRoles();

  List<CandidateDTO> getCandidates(String fields);

  void setUnauthorizedUserGroup(List<UnauthorizedDTO> usersDTO, String group);

  List<Unauthorized> getUnauthorizedUsers();

  List<GradeDTO> getExams();

  List<NameDTO> getCommisions();

  void setCommissionOnGrade(Long id, String commissionName);
}
