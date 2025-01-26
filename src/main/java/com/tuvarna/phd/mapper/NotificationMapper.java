package com.tuvarna.phd.mapper;

import com.tuvarna.phd.entity.Notification;
import com.tuvarna.phd.service.dto.NotificationClientDTO;
import com.tuvarna.phd.service.dto.NotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface NotificationMapper {
  NotificationMapper mapperInstance = Mappers.getMapper(NotificationMapper.class);

  @Mapping(target = "id", ignore = true)
  Notification toEntity(NotificationDTO notificationDTO);

  @Mapping(source = "id", target = "id")
  NotificationClientDTO toDto(Notification notification);
}
