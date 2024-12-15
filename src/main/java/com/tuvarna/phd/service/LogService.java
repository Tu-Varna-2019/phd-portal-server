package com.tuvarna.phd.service;

import com.tuvarna.phd.service.dto.LogDTO;
import java.util.List;

public interface LogService {
  void save(LogDTO logDTO);

  void deleteLogs(List<LogDTO> logs);

  List<LogDTO> fetch(String role);
}
