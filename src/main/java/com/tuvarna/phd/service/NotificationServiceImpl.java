package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.Notification;
import com.tuvarna.phd.mapper.NotificationMapper;
import com.tuvarna.phd.repository.DoctoralCenterRepository;
import com.tuvarna.phd.repository.NotificationRepository;
import com.tuvarna.phd.service.dto.IdDTO;
import com.tuvarna.phd.service.dto.NotificationClientDTO;
import com.tuvarna.phd.service.dto.NotificationDTO;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
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
  private final NotificationRepository notificationRepository;
  private final NotificationMapper notificationMapper;
  private final DoctoralCenterRepository doctoralCenterRepository;
  private final PgPool client;

  public NotificationServiceImpl(
      NotificationRepository notificationRepository,
      DoctoralCenterRepository doctoralCenterRepository,
      NotificationMapper notificationMapper,
      PgPool client) {
    this.notificationRepository = notificationRepository;
    this.doctoralCenterRepository = doctoralCenterRepository;
    this.client = client;
    this.notificationMapper = notificationMapper;
  }

  @Override
  @Transactional
  public void save(NotificationDTO notificationDTO) {
    notificationDTO.setCreation(new Timestamp(new Date().getTime()));

    if (notificationDTO.getScope().equals("group"))
      notificationDTO.addRecipients(
          this.getOidsByGroup(notificationDTO.getGroup()).await().indefinitely());

    LOG.info("Recipient oids to save : " + notificationDTO.getRecipients());

    for (String recipientOid : notificationDTO.getRecipients())
      this.notificationRepository.save(
          new Notification(
              notificationDTO.getTitle(),
              notificationDTO.getDescription(),
              notificationDTO.getCreation(),
              notificationDTO.getSeverity(),
              recipientOid));
  }

  private Uni<List<String>> getOidsByGroup(String group) {
    String statement = "";

    switch (group) {
      case "admin" ->
          statement =
              "select d.oid from doctoralcenter d join doctoralcenterrole role on d.id=role.id "
                  + " where role.role=$1";
    }

    return this.client
        .preparedQuery(statement)
        .execute(Tuple.of(group))
        .map(
            rowSet -> {
              List<String> oids = new ArrayList<String>();
              rowSet.forEach(row -> oids.add(row.getString("oid")));
              return oids;
            });
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
