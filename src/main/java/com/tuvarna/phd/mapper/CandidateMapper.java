package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Curriculum;
import com.tuvarna.phd.entity.Faculty;
import io.smallrye.common.constraint.Nullable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface CandidateMapper {
  CandidateMapper mapperInstance = Mappers.getMapper(CandidateMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "curriculum", ignore = true)
  @Mapping(target = "biography", ignore = true)
  @Mapping(target = "biographyBlob", ignore = true)
  @Mapping(target = "faculty", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  Candidate toEntity(CandidateDTO candidateDTO);

  @Mapping(target = "biographyBlob", ignore = true)
  CandidateDTO toDto(Candidate candidate);

  @Nullable
  default String map(@Nullable Curriculum curriculum) {
    return curriculum != null ? curriculum.getName() : null;
  }

  @Nullable
  default String map(@Nullable Faculty faculty) {
    return faculty != null ? faculty.getName() : null;
  }
}
