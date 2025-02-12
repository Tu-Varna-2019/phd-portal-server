package com.tuvarna.phd.service;

import com.tuvarna.phd.service.dto.UnauthorizedUsersDTO;
import io.smallrye.mutiny.tuples.Tuple2;

public sealed interface AuthService permits AuthServiceImpl {
  Tuple2<Object, String> login(UnauthorizedUsersDTO uDto);
}
