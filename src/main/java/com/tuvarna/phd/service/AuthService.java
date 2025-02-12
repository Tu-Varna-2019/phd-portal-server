package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.UserEntity;
import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import io.smallrye.mutiny.tuples.Tuple2;

public sealed interface AuthService permits AuthServiceImpl {
  Tuple2<UserEntity, String> login(UnauthorizedUsersDTO uDto);

  UserEntity authenticate(UnauthorizedUsersDTO uDto);

  String getGroupByOid(String oid);

  void addToUnauthorized(UnauthorizedUsersDTO unauthorizedUsersDTO);
}
