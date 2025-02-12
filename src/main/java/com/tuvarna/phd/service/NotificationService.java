package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.IdDTO;
import com.tuvarna.phd.dto.NotificationClientDTO;
import com.tuvarna.phd.dto.NotificationDTO;
import java.util.List;

public sealed interface NotificationService permits NotificationServiceImpl {
  void save(NotificationDTO notificationDTO);

  List<NotificationClientDTO> get(String oid);

  void delete(List<IdDTO> notificationDTOs);
}
