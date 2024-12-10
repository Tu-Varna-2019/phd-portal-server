package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.mapper.PhdMapper;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.service.PhdService;
import com.tuvarna.phd.service.dto.PhdDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PhdServiceImpl implements PhdService {
  private final PhdRepository pRepository;
  private final PhdMapper pMapper;

  @Inject private static final Logger LOG = Logger.getLogger(PhdServiceImpl.class);

  @ConfigProperty(name = "ms.aad.tenant-id")
  private String tenantId;

  @Inject
  public PhdServiceImpl(PhdRepository pRepository, PhdMapper pMapper) {
    this.pRepository = pRepository;
    this.pMapper = pMapper;
  }

  @Override
  @Transactional
  public void login(PhdDTO pDto) {
    LOG.info("Service received a request to login as a phd user: " + pDto);
  }

  private void syncPhdTableADD() {}
}
