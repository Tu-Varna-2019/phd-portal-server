package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.dto.RoleDTO;
import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.model.MailModel.TEMPLATES;
import java.util.List;

public sealed interface DoctoralCenterService permits DoctoralCenterServiceImpl {
  void review(CandidateDTO candidateDTO, Long id);

  void setUnauthorizedUserRole(List<UnauthorizedUsersDTO> usersDTO, String role);

  List<UnauthorizedUsers> getUnauthorizedUsers();

  List<UserDTO> getAuthorizedUsers();

  void deleteAuthorizedUser(String oid, RoleDTO role);

  void sendEmail(String title, TEMPLATES template, String email);
}
