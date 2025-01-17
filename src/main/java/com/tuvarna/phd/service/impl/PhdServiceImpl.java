package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.exception.PhdException;
import com.tuvarna.phd.mapper.PhdMapper;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import com.tuvarna.phd.service.PhdService;
import com.tuvarna.phd.service.dto.PhdDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PhdServiceImpl implements PhdService {
  private final PhdRepository pRepository;
  private final PhdStatusRepository sPhdRepository;
  private final PhdMapper pMapper;

  @Inject private Logger LOG = Logger.getLogger(PhdServiceImpl.class);

  @Inject
  public PhdServiceImpl(
      PhdRepository pRepository, PhdStatusRepository sPhdRepository, PhdMapper pMapper) {
    this.pRepository = pRepository;
    this.pMapper = pMapper;
    this.sPhdRepository = sPhdRepository;
  }

  @Override
  @Transactional
  public void login(PhdDTO pDto) {
    LOG.info("Service received a request to login as a phd user: " + pDto);
    Phd phd = this.pMapper.toEntity(pDto);
    String defaultStatus = "enrolled";

    LOG.info("Setting default value for phd: " + defaultStatus);
    phd.setStatus(this.sPhdRepository.getByStatus(defaultStatus));

    this.syncPhdTableAAD(phd);
  }

  // TODO: add a config file describing all of the allowed phd users to use this system, if REQUIRED
  private void syncPhdTableAAD(Phd phd) {
    try {
      this.pRepository.getByOid(phd.getOid());
      LOG.info(
          "OK: Phd user: "
              + phd.getEmail()
              + " with oid: "
              + phd.getOid()
              + " is present in the Phd table.\n No need to add him. Moving on...");
    } catch (PhdException exception) {
      LOG.warn(
          "Phd user: "
              + phd.getEmail()
              + " with oid: "
              + phd.getOid()
              + " is NOT present in table. Adding him now...");
      this.pRepository.save(phd);
    }
  }
}
