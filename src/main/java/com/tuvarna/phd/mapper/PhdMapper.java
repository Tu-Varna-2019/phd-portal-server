package com.tuvarna.phd.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.tuvarna.phd.entity.Phd;
import com.tuvarna.phd.service.dto.PhdDTO;

@Mapper(componentModel = "cdi")
public interface PhdMapper {
    PhdMapper mapperInstance = Mappers.getMapper(PhdMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "enrollDate", ignore = true)
    @Mapping(target = "gradDate", ignore = true)
    @Mapping(target = "curriculum", ignore = true)
    @Mapping(target = "supervisor", ignore = true)
    @Mapping(target = "department", ignore = true)
    Phd toEntity(PhdDTO pDto);
}