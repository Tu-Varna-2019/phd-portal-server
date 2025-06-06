package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.CandidateApplyDTO;
import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.CandidateStatus;
import com.tuvarna.phd.entity.Curriculum;
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
  @Mapping(target = "faculty", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  @Mapping(target = "examStep", ignore = true)
  @Mapping(target = "facultyName", ignore = true)
  @Mapping(target = "curriculumName", ignore = true)
  @Mapping(target = "statusName", ignore = true)
  @Mapping(target = "biographyBlob", ignore = true)
  @Mapping(target = "grades", ignore = true)
  Candidate toEntity(CandidateDTO candidateDTO);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "curriculum", ignore = true)
  @Mapping(target = "faculty", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  @Mapping(target = "examStep", ignore = true)
  @Mapping(target = "facultyName", ignore = true)
  @Mapping(target = "curriculumName", ignore = true)
  @Mapping(target = "statusName", ignore = true)
  @Mapping(target = "biographyBlob", ignore = true)
  @Mapping(target = "grades", ignore = true)
  Candidate toEntity(CandidateApplyDTO candidateDTO);

  @Mapping(target = "faculty", source = "facultyName")
  @Mapping(target = "status", source = "statusName")
  @Mapping(target = "curriculum", source = "curriculumName")
  CandidateDTO toDto(Candidate candidate);

  @Nullable
  default String map(@Nullable Curriculum curriculum) {
    return curriculum != null ? curriculum.getName() : null;
  }

  @Nullable
  default String map(CandidateStatus status) {
    return status != null ? status.getStatus() : null;
  }
}
