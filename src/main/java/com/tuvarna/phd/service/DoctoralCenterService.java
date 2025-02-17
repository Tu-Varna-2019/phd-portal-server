package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateStatusDTO;
import com.tuvarna.phd.dto.RoleDTO;
import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import java.io.IOException;
import java.util.List;

public sealed interface DoctoralCenterService permits DoctoralCenterServiceImpl {

  void review(CandidateStatusDTO candidateStatusDTO) throws IOException;

  List<Candidate> getCandidates();

  void setUnauthorizedUserRole(List<UnauthorizedUsersDTO> usersDTO, String role);

  List<UnauthorizedUsers> getUnauthorizedUsers();

  List<UserDTO> getAuthorizedUsers();

  void deleteAuthorizedUser(String oid, RoleDTO role);
}
