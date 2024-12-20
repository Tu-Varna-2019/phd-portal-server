package com.tuvarna.phd.mapper;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface DoctoralCenterMapper {
  DoctoralCenterMapper mapperInstance = Mappers.getMapper(DoctoralCenterMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "role", ignore = true)
  DoctoralCenter toEntity(DoctoralCenterDTO doctoralCenterDTO);
}
