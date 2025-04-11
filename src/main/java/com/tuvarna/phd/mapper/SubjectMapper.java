package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.SubjectDTO;
import com.tuvarna.phd.entity.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface SubjectMapper {
  SubjectMapper mapperInstance = Mappers.getMapper(SubjectMapper.class);

  SubjectDTO toDto(Subject subject);
}
