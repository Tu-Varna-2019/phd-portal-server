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
@Table(name = "unauthorizedusers")
public class UnauthorizedUsers extends PanacheEntityBase {

  @Id
  @SequenceGenerator(
      name = "unauthorizedUsersSequence",
      sequenceName = "unauthorizedUsers_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unauthorizedUsersSequence")
  private Long id;

  @Column(name = "oid", nullable = true, unique = true, updatable = false)
  private String oid;

  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = false)
  // BUG: switching unique to true gives a stupid error: duplicate key value violates unique
  // constraint "unauthorizedusers_email_key"
  private String email;

  @Column(nullable = false, unique = false)
  private Timestamp timestamp;
}
