package com.tuvarna.phd.mapper;

import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.service.dto.CandidateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface CandidateMapper {
  CandidateMapper mapperInstance = Mappers.getMapper(CandidateMapper.class);

  Candidate toEntity(CandidateDTO candidateDTO);
}
