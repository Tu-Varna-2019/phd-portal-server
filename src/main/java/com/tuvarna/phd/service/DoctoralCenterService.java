package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.UnauthorizedUsers;
import com.tuvarna.phd.service.dto.PhdDTO;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import io.smallrye.mutiny.Uni;
import java.util.List;

public interface DoctoralCenterService {
  void updatePhdStatus(PhdDTO pDto, String status);

  void setUnauthorizedUserRole(UnauthorizedUsersDTO usersDTO, String role);

  List<UnauthorizedUsers> getUnauthorizedUsers();

  Uni<Void> sendEmail(String email);
}
