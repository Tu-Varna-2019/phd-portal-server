package com.tuvarna.phd.service;

import com.tuvarna.phd.service.dto.LogDTO;
import java.util.List;

public interface LogService {
  void save(LogDTO logDTO);

  void delete(List<LogDTO> logs);

  List<LogDTO> get(String role);
}
