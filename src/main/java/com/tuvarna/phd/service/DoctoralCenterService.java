package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.service.dto.CandidateDTO;
import com.tuvarna.phd.service.dto.RoleDTO;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.service.dto.UserDTO;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface DoctoralCenterService {
  void updateCandidateStatus(CandidateDTO candidateDTO, String oid);

  void setUnauthorizedUserRole(List<UnauthorizedUsersDTO> usersDTO, String role);

  List<UnauthorizedUsers> getUnauthorizedUsers();

  List<UserDTO> getAuthorizedUsers();

  void deleteAuthorizedUser(String oid, RoleDTO role);

  Uni<Void> sendEmail(String email);
}
