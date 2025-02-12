package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class Notification extends PanacheEntityBase {

  @Id
  @SequenceGenerator(
      name = "notificationSequence",
      sequenceName = "notification_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notificationSequence")
  private Long id;

  @Column(nullable = false, unique = false)
  private String title;

  @Column(nullable = false, unique = false)
  private String description;

  @Column(nullable = false, unique = false)
  private Timestamp creation;

  @Column(nullable = false, unique = false)
  private String severity;

  @Column(nullable = true, unique = false)
  private String recipient;

  public Notification(
      String title, String description, Timestamp creation, String severity, String recipient) {
    this.title = title;
    this.description = description;
    this.creation = creation;
    this.severity = severity;
    this.recipient = recipient;
  }
}
