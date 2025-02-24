package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.IUserEntity;
import io.smallrye.mutiny.tuples.Tuple2;

public sealed interface AuthService permits AuthServiceImpl {
  Tuple2<IUserEntity<?>, String> login(String oid, String name, String email);

  IUserEntity<?> getAuthenticatedUser(String oid, String group);

  String getGroupByOid(String oid);

  void addToUnauthorizedTable(String oid, String name, String email, String group);
}
