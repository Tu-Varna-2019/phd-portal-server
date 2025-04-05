package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.entity.Curriculum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface CurriculumMapper {
  CurriculumMapper mapperInstance = Mappers.getMapper(CurriculumMapper.class);

  @Mapping(target = "subjects", ignore = true)
  @Mapping(target = "yearPeriod", source = "curriculum.mode.yearPeriod")
  @Mapping(target = "mode", source = "curriculum.mode.mode")
  @Mapping(target = "faculty", source = "curriculum.faculty.name")
  CurriculumDTO toDto(Curriculum curriculum);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "faculty", ignore = true)
  @Mapping(target = "subjects", ignore = true)
  @Mapping(target = "mode", ignore = true)
  Curriculum toEntity(CurriculumDTO curriculumDTO);
}
