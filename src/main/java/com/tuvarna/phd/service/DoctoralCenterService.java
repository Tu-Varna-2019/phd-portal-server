package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.service.dto.OidDTO;
import com.tuvarna.phd.service.dto.PhdDTO;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.service.dto.UserDTO;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface DoctoralCenterService {
  void updatePhdStatus(PhdDTO pDto, String status);

  void setUnauthorizedUserRole(List<UnauthorizedUsersDTO> usersDTO, String role);

  List<UnauthorizedUsers> getUnauthorizedUsers();

  List<UserDTO> getAuthenticatedUsers();

  void deleteUser(OidDTO oidDTO, String role);

  Uni<Void> sendEmail(String email);
}
