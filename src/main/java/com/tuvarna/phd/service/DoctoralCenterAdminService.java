package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.RoleDTO;
import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import java.util.List;

public sealed interface DoctoralCenterAdminService permits DoctoralCenterAdminServiceImpl {
  void setUnauthorizedUserGroup(List<UnauthorizedUsersDTO> usersDTO, String group);

  void changeUnauthorizedUserIsAllowed(String oid, Boolean isAllowed);

  List<UnauthorizedUsers> getUnauthorizedUsers();

  List<String> getDoctoralCenterRoles();

  List<UserDTO> getAuthorizedUsers();

  void deleteAuthorizedUser(String oid, RoleDTO role);
}
