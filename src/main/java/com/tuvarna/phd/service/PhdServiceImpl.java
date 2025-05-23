package com.tuvarna.phd.service;

import com.tuvarna.phd.mapper.PhdMapper;
import com.tuvarna.phd.repository.PhdRepository;
import com.tuvarna.phd.repository.PhdStatusRepository;
import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class PhdServiceImpl implements PhdService {
  @Inject PhdRepository pRepository;
  @Inject PhdStatusRepository sPhdRepository;
  @Inject PhdMapper pMapper;

  @Inject private Logger LOG = Logger.getLogger(PhdServiceImpl.class);

  @Override
  @CacheResult(cacheName = "curriculum-cache")
  public List<String> getCurriculum() {
    List<String> currriculums = new ArrayList<>();

    return currriculums;
  }

  @Override
  @CacheInvalidate(cacheName = "curriculum-cache")
  public void deleteCurriculum() {}
}
