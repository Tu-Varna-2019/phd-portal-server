package com.tuvarna.phd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tuvarna.phd.entity.DoctoralCenter;
import com.tuvarna.phd.service.dto.DoctoralCenterDTO;

@Mapper(componentModel = "cdi")
public interface DoctoralCenterMapper {
    DoctoralCenterMapper mapperInstance = Mappers.getMapper(DoctoralCenterMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "passwordChangeRequired", ignore = true)
    @Mapping(target = "unhashedPassword", ignore = true)
    DoctoralCenter toEntity(DoctoralCenterDTO doctoralCenterDTO);
}
