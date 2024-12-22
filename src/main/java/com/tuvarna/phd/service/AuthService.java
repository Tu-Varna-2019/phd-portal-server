package com.tuvarna.phd.service;

import com.tuvarna.phd.service.dto.UserDTO;
import io.smallrye.mutiny.tuples.Tuple2;

public interface AuthService {
  Tuple2<Object, String> login(UserDTO userDTO);
}
