package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.IUserEntity;

// TODO: Convert this to a strategy pattern ?
public interface IUserRepository<T extends IUserEntity<T>> extends RepositoryStrategy<T> {
  T getByOid(String oid);

  T getByEmail(String email);

  void deleteByOid(String oid);
}
