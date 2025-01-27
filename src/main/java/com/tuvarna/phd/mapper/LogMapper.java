package com.tuvarna.phd.mapper;

import com.tuvarna.phd.service.dto.LogDTO;
import com.tuvarna.phd.service.dto.sendLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface LogMapper {
  LogMapper mapperInstance = Mappers.getMapper(LogMapper.class);


  @Mapping(target = "userPrincipalDTO", ignore = true)
  LogDTO toDto(sendLogDTO sendLogDTO);
}
