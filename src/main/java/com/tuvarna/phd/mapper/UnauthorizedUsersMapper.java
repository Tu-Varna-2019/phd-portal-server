package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.UnauthorizedUsersDTO;
import com.tuvarna.phd.entity.UnauthorizedUsers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface UnauthorizedUsersMapper {
  UnauthorizedUsersMapper uMapper = Mappers.getMapper(UnauthorizedUsersMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  @Mapping(target = "isAllowed", ignore = true)
  UnauthorizedUsers toEntity(UnauthorizedUsersDTO users);
}
