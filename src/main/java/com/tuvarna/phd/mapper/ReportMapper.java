package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.ReportResponseDTO;
import com.tuvarna.phd.entity.Report;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface ReportMapper {
  ReportMapper mapperInstance = Mappers.getMapper(ReportMapper.class);

  ReportResponseDTO toDto(Report report);
}
