package com.tuvarna.phd.mapper;

import com.tuvarna.phd.dto.NotificationClientDTO;
import com.tuvarna.phd.dto.NotificationDTO;
import com.tuvarna.phd.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface NotificationMapper {
  NotificationMapper mapperInstance = Mappers.getMapper(NotificationMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "recipient", ignore = true)
  Notification toEntity(NotificationDTO notificationDTO);

  NotificationClientDTO toDto(Notification notification);
}
