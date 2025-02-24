package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.entity.IUserEntity;
import io.smallrye.mutiny.tuples.Tuple2;

public sealed interface AuthService permits AuthServiceImpl {
  Tuple2<IUserEntity<?>, String> login(UnauthorizedUsersDTO uDto);

  IUserEntity<?> getUser(String oid, String group);

  String getGroupByOid(String oid);

  void addToUnauthorized(UnauthorizedUsersDTO unauthorizedUsersDTO, String group);
}
