package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.PhdDTO;
import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.entity.Phd;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface PhdMapper {
  PhdMapper mapperInstance = Mappers.getMapper(PhdMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "enrollDate", ignore = true)
  @Mapping(target = "graduationDate", ignore = true)
  @Mapping(target = "curriculum", ignore = true)
  @Mapping(target = "supervisor", ignore = true)
  @Mapping(target = "faculty", ignore = true)
  @Mapping(target = "report", ignore = true)
  @Mapping(target = "pictureBlob", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  Phd toEntity(PhdDTO pDto);

  default Phd toEntity(Candidate candidate) {
    return new Phd(
        "",
        candidate.getName(),
        candidate.getEmail(),
        candidate.getCountry(),
        candidate.getCity(),
        candidate.getAddress(),
        candidate.getPostCode(),
        candidate.getPin(),
        candidate.getCurriculum(),
        candidate.getFaculty());
  }
}
