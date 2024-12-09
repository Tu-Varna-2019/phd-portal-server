package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.exception.DoctoralCenterRoleNotFoundException;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;

import io.smallrye.mutiny.Uni;


public interface DoctoralCenterService {
  DoctoralCenter create(DoctoralCenterDTO doctoralCenterDTO) throws DoctoralCenterRoleNotFoundException;

  Uni<Void> sendEmail(String email, String password);
}
