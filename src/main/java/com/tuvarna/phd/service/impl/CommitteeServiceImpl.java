package com.tuvarna.phd.service.impl;

import com.tuvarna.phd.entity.Committee;
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

  @Override
  @Transactional
  public void delete(Committee committee) {
    committeeRepository.delete(committee);
  }
}
