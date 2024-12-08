package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.exception.DoctoralCenterRoleNotFoundException;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;

public interface DoctoralCenterService {
  DoctoralCenter create(DoctoralCenterDTO doctoralCenterDTO) throws DoctoralCenterRoleNotFoundException;

  void sendEmail(String email, String password);
}
