package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Notification;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class NotificationRepository implements PanacheRepositoryBase<Notification, Integer> {

  public Notification getById(Integer id) throws HttpException {
    return findByIdOptional(id).orElseThrow(() -> new HttpException("Notification doesn't exist!"));
  }

  public List<Notification> getByRecipient(String oid) {
    return find("recipient", oid).list();
  }

  public void save(Notification notification) {
    notification.persist();
  }

  public List<Notification> getAll() {
    return listAll();
  }

  public void deleteById(Long id) {
    delete("id", id);
  }
}
