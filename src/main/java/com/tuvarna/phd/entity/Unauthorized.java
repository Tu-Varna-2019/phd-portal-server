package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Row;
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
@Table(name = "unauthorized")
public non-sealed class Unauthorized extends PanacheEntityBase
    implements IUserEntity<Unauthorized> {

  @Id
  @SequenceGenerator(
      name = "unauthorizedUsersSequence",
      sequenceName = "unauthorizedUsers_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unauthorizedUsersSequence")
  private Long id;

  @Column(nullable = false, unique = true, updatable = false)
  private String oid;

  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = false)
  private Timestamp timestamp;

  @Column(nullable = false, unique = false)
  private Boolean allowed = false;

  public Unauthorized(String oid, String name, String email, Timestamp timestamp) {
    this.oid = oid;
    this.name = name;
    this.email = email;
    this.timestamp = timestamp;
  }

  public Unauthorized(String oid) {
    this.oid = oid;
  }

  @Override
  public Unauthorized toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Unauthorized.class);
  }
}
