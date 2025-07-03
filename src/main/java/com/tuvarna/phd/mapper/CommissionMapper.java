package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.CommissionDTO;
import com.tuvarna.phd.dto.CommissionRequestDTO;
import com.tuvarna.phd.dto.CommitteeDTO;
import com.tuvarna.phd.entity.Commission;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface CommissionMapper {
  CommissionMapper mapperInstance = Mappers.getMapper(CommissionMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  default CommissionDTO toDto(Commission commission) {
    List<CommitteeDTO> committeeDTOs = new ArrayList<>();

    commission
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

    return new CommissionDTO(commission.getName(), committeeDTOs);
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "toEntity", ignore = true)
  default Commission toEntity(CommissionRequestDTO commissionDTO) {
    return new Commission(commissionDTO.getName());
  }
}
