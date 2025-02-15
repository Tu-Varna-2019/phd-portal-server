package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.UserEntity;

// TODO: Convert this to a strategy pattern ?
public interface IUserRepository<T extends UserEntity<T>> extends RepositoryStrategy<T> {
  T getByOid(String oid);

  T getByEmail(String email);

  void deleteByOid(String oid);
}
