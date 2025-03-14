package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.UnauthorizedDTO;
import com.tuvarna.phd.entity.Unauthorized;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface UnauthorizedMapper {
  UnauthorizedMapper uMapper = Mappers.getMapper(UnauthorizedMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  @Mapping(target = "allowed", ignore = true)
  Unauthorized toEntity(UnauthorizedDTO users);
}
