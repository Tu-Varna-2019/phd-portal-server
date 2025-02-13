package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface UserRepositoryStrategy<T extends UserEntity<T>> extends RepositoryStrategy<T> {
  T getByOid(String oid);

  T getByEmail(String email);

  void deleteByOid(String oid);

  T getFullByOid(String oid);
}
