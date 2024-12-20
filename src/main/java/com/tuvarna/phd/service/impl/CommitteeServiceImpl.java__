package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.exception.CommitteeException;
import com.tuvarna.phd.repository.CommitteeRepository;
import com.tuvarna.phd.service.CommitteeService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public class CommitteeServiceImpl implements CommitteeService {
  private final CommitteeRepository committeeRepository;

  @Inject private Logger LOG;

  @Inject
  public CommitteeServiceImpl(CommitteeRepository committeeRepository) {
    this.committeeRepository = committeeRepository;
  }

  // @Override
  // @Transactional
  // public void login(CommitteeDTO committeeDTO) {
  //   LOG.info("Service received a request to login as a committee user: " + pDto);
  //   Phd phd = this.pMapper.toEntity(pDto);
  //   String defaultStatus = "enrolled";
  //
  //   LOG.info("Setting default value for phd: " + defaultStatus);
  //   phd.setStatus(this.sPhdRepository.getByStatus(defaultStatus));
  //
  //   this.syncPhdTableAAD(phd);
  // }

  @Override
  @Transactional
  public void delete(Committee committee) {
    committeeRepository.delete(committee);
  }

  private void syncCommitteeTableAAD(Committee committee) {
    try {
      this.committeeRepository.getByOid(committee.getOid());
      LOG.info(
          "OK: Committee user: "
              + committee.getEmail()
              + " with oid: "
              + committee.getOid()
              + " is present in the Committee table.\n No need to add him. Moving on...");
    } catch (CommitteeException exception) {
      LOG.warn(
          "Committee user: "
              + committee.getEmail()
              + " with oid: "
              + committee.getOid()
              + " is NOT present in table. Adding him now...");
      this.committeeRepository.save(committee);
    }
  }
}
