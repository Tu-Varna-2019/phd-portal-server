package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.RoleDTO;
import com.tuvarna.phd.dto.UnauthorizedDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Unauthorized;
import java.util.List;

public sealed interface DoctoralCenterAdminService permits DoctoralCenterAdminServiceImpl {
  void setUnauthorizedUserGroup(List<UnauthorizedDTO> usersDTO, String group);

  void changeUnauthorizedUserIsAllowed(String oid, Boolean isAllowed);

  List<Unauthorized> getUnauthorizedUsers();

  List<String> getDoctoralCenterRoles();

  List<UserDTO> getAuthorizedUsers();

  void deleteAuthorizedUser(String oid, RoleDTO role);
}
