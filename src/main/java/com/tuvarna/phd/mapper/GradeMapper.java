package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.CommisionDTO;
import com.tuvarna.phd.dto.CommitteeDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Commission;
import com.tuvarna.phd.entity.Grade;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface GradeMapper {
  GradeMapper mapperInstance = Mappers.getMapper(GradeMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  default GradeDTO toDto(Grade grade, UserDTO examinedUser) {
    Optional<Commission> commission = Optional.ofNullable(grade.getCommission());
    List<CommitteeDTO> committeeDTOs = new ArrayList<>();

    if (commission.isPresent()) {
      grade
          .getCommission()
          .getMembers()
          .forEach(
              committee -> {
                committeeDTOs.add(
                    new CommitteeDTO(
                        committee.getOid(),
                        committee.getName(),
                        committee.getPicture(),
                        committee.getGrade(),
                        committee.getFaculty().getName(),
                        committee.getRole().getRole()));
              });

      return new GradeDTO(
          grade.getId(),
          grade.getGrade(),
          grade.getEvalDate(),
          new CommisionDTO(commission.get().getName(), committeeDTOs),
          grade.getReport(),
          grade.getAttachments(),
          examinedUser,
          grade.getSubject().getName());
    } else {
      return new GradeDTO(
          grade.getId(),
          grade.getGrade(),
          grade.getEvalDate(),
          grade.getReport(),
          grade.getAttachments(),
          examinedUser,
          grade.getSubject().getName());
    }
  }
}
