package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.CandidateStatusDTO;
import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.entity.Unauthorized;
import java.io.IOException;
import java.util.List;

public sealed interface DoctoralCenterService permits DoctoralCenterServiceImpl {

  void review(CandidateStatusDTO candidateStatusDTO) throws IOException;

  List<String> getDoctoralCenterRoles();

  List<CandidateDTO> getCandidates(String fields);

  void setUnauthorizedUserGroup(List<UnauthorizedUsersDTO> usersDTO, String group);

  List<Unauthorized> getUnauthorizedUsers();
}
