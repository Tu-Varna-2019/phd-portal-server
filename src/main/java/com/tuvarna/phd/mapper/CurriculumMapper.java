package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.CurriculumDTO;
import com.tuvarna.phd.entity.Curriculum;
import com.tuvarna.phd.entity.Faculty;
import com.tuvarna.phd.entity.Mode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface CurriculumMapper {
  CurriculumMapper mapperInstance = Mappers.getMapper(CurriculumMapper.class);

  CurriculumDTO toDto(Curriculum curriculum);

  default String map(Mode mode) {
    return mode.getMode();
  }

  default String map(Faculty faculty) {
    return faculty.getName();
  }
}
