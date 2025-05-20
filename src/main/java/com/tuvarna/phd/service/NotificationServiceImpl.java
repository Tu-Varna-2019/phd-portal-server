package com.tuvarna.phd.service;

import com.tuvarna.phd.dto.IdDTO;
import com.tuvarna.phd.dto.NotificationClientDTO;
import com.tuvarna.phd.dto.NotificationDTO;
import com.tuvarna.phd.entity.Notification;
import com.tuvarna.phd.exception.HttpException;
import com.tuvarna.phd.mapper.NotificationMapper;
import com.tuvarna.phd.model.DatabaseModel;
import com.tuvarna.phd.repository.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class NotificationServiceImpl implements NotificationService {

  @Inject private Logger LOG = Logger.getLogger(NotificationServiceImpl.class);
  @Inject NotificationRepository notificationRepository;
  @Inject NotificationMapper notificationMapper;
  @Inject DatabaseModel databaseModel;

  @Override
  @Transactional
  public void save(NotificationDTO notificationDTO) {
    notificationDTO.setCreation(new Timestamp(new Date().getTime()));

    if (notificationDTO.getScope().equals("group"))
      notificationDTO.addRecipients(this.getOidsByGroup(notificationDTO.getGroup()));

    LOG.info("Recipient oids to save: " + notificationDTO.getRecipients());

    for (String recipientOid : notificationDTO.getRecipients())
      this.notificationRepository.save(
          new Notification(
              notificationDTO.getTitle(),
              notificationDTO.getDescription(),
              notificationDTO.getCreation(),
              notificationDTO.getSeverity(),
              recipientOid));
  }

  @Override
  public List<String> getOidsByGroup(String group) {
    String statement =
        switch (group) {
          case "admin" -> {
            yield "SELECT d.oid FROM doctoral_center d JOIN doctoral_center_role role ON"
                      + " (d.id=role.id)  WHERE role.role='admin'";
          }

          case "expert-manager" -> {
            yield "SELECT d.oid FROM doctoral_center d JOIN doctoral_center_role role ON"
                      + " (d.id=role.id) WHERE role.role='expert' OR role.role='manager'";
          }

          case "expert" -> {
            yield "SELECT d.oid FROM doctoral_center d JOIN doctoral_center_role role ON"
                      + " (d.id=role.id) WHERE role.role='expert'";
          }

          case "manager" -> {
            yield "SELECT d.oid FROM doctoral_center d JOIN doctoral_center_role role ON"
                      + " (d.id=role.id) WHERE role.role='manager'";
          }

          default -> {
            throw new HttpException(
                "Notification error: cannot retrieve oids for unknown group: "
                    + group
                    + " Valid groups are: admin");
          }
        };

    return this.databaseModel.selectMapString(statement, "oid");
  }

  @Override
  @Transactional
  public List<NotificationClientDTO> get(String oid) {
    List<NotificationClientDTO> notificationClientDTOs = new ArrayList<>();
    List<Notification> notifications = this.notificationRepository.getByRecipient(oid);

    for (Notification notification : notifications)
      notificationClientDTOs.add(this.notificationMapper.toDto(notification));

    return notificationClientDTOs;
  }

  @Override
  @Transactional
  public void delete(List<IdDTO> notificationIds) {
    LOG.info("Service received a request to delete logs");

    for (IdDTO id : notificationIds) this.notificationRepository.deleteById(id.getId());

    LOG.info("Notifications deleted!");
  }
}
