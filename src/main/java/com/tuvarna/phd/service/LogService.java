package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.LogDTO;
import java.util.List;

public sealed interface LogService permits LogServiceImpl {
  void save(LogDTO logDTO);

  void delete(List<LogDTO> logs);

  List<LogDTO> get();
}
