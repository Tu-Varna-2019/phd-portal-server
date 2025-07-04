package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.CommitteeDTO;
import com.tuvarna.phd.entity.Committee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface CommitteeMapper {
  CommitteeMapper mapperInstance = Mappers.getMapper(CommitteeMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  default CommitteeDTO toDto(Committee committee) {
    return new CommitteeDTO(
        committee.getOid(),
        committee.getName(),
        committee.getEmail(),
        committee.getPicture(),
        committee.getFaculty().getName(),
        committee.getRole().getRole());
  }
}
