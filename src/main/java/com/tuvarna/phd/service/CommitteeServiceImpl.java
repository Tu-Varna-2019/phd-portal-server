package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Committee;
import com.tuvarna.phd.repository.CommitteeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class CommitteeServiceImpl implements CommitteeService {
  @Inject CommitteeRepository committeeRepository;

  @Inject private Logger LOG;

  @Override
  @Transactional
  public void delete(Committee committee) {
    committeeRepository.delete(committee);
  }
}
