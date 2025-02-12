package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.CandidateDTO;
import com.tuvarna.phd.entity.Candidate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface CandidateMapper {
  CandidateMapper mapperInstance = Mappers.getMapper(CandidateMapper.class);

  Candidate toEntity(CandidateDTO candidateDTO);
}
