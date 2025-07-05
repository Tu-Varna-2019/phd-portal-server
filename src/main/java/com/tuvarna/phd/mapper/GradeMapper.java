package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.CommissionDTO;
import com.tuvarna.phd.dto.CommitteeDTO;
import com.tuvarna.phd.dto.GradeDTO;
import com.tuvarna.phd.dto.UserDTO;
import com.tuvarna.phd.entity.Commission;
import com.tuvarna.phd.entity.Grade;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface GradeMapper {
  GradeMapper mapperInstance = Mappers.getMapper(GradeMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  default GradeDTO toDto(Grade grade, UserDTO examinedUser) {
    Commission commission = grade.getCommission();
    System.out.println("Commission " + commission + " for graed: " + grade.getId());

    List<CommitteeDTO> committeeDTOs = new ArrayList<>();

    if (commission != null) {
      grade
          .getCommission()
          .getMembers()
          .forEach(
              committee -> {
                committeeDTOs.add(
                    new CommitteeDTO(
                        committee.getOid(),
                        committee.getName(),
                        committee.getEmail(),
                        committee.getPicture(),
                        committee.getFaculty().getName(),
                        committee.getRole().getRole()));
              });

      return new GradeDTO(
          grade.getId(),
          grade.getGrade(),
          grade.getEvalDate(),
          new CommissionDTO(commission.getName(), committeeDTOs),
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
