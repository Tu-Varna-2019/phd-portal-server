package com.tuvarna.phd.repository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface RepositoryStrategy<T> {

  public void save(T repository);
}
