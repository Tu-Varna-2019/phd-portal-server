package com.tuvarna.phd.repository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface UserRepositoryStrategy<T> extends RepositoryStrategy<T> {
  T getByOid(String oid);

  T getByEmail(String email);

  void deleteByOid(String oid);

  T getFullByOid(String oid);
}
