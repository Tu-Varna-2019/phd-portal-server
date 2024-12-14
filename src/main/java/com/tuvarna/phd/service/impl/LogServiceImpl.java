package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.service.LogService;
import com.tuvarna.phd.service.dto.LogDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class LogServiceImpl implements LogService {

  @Inject private Logger LOG = Logger.getLogger(LogServiceImpl.class);

  @Override
  @Transactional
  public void save(LogDTO logDTO) {
    LOG.info("Service received a request to save a log with timestamp" + logDTO.getTimestamp());
  }

  @Override
  @Transactional
  public List<LogDTO> fetch(String role) {
    return new ArrayList<>();
  }
}
