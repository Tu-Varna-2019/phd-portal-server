package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateEssentialDTO;
import com.tuvarna.phd.dto.CandidateStatusDTO;
import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import java.io.IOException;
import java.util.List;

public sealed interface DoctoralCenterService permits DoctoralCenterServiceImpl {

  void review(CandidateStatusDTO candidateStatusDTO) throws IOException;

  List<String> getDoctoralCenterRoles();

  List<CandidateEssentialDTO> getCandidates();

  void setUnauthorizedUserGroup(List<UnauthorizedUsersDTO> usersDTO, String group);

  List<UnauthorizedUsers> getUnauthorizedUsers();
}
