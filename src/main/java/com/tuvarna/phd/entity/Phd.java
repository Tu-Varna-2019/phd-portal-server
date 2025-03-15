package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Row;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "phd")
public non-sealed class Phd extends PanacheEntityBase implements IUserEntity<Phd> {

  @Id
  @SequenceGenerator(name = "phdSequence", sequenceName = "phd_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phdSequence")
  private Long id;

  @Column(unique = true, updatable = false)
  private String oid;

  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = false)
  private String email;

  @Column(nullable = true, unique = false)
  private String picture;

  @Transient private String pictureBlob;

  @Column(nullable = true, unique = false)
  private String country;

  @Column(nullable = true, unique = false)
  private String city;

  @Column(nullable = true, unique = false)
  private String address;

  @Password
  @Column(nullable = false, unique = true, length = 10)
  // TODO: Encrypt this pls
  // ЕГН
  private String pin;

  @Column(nullable = true, unique = false)
  private String dissertation;

  @Column(name = "enroll_date", nullable = true, unique = false)
  private Date enrollDate;

  @Column(name = "graduation_date", nullable = true, unique = false)
  private Date graduationDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "status", nullable = false)
  private PhdStatus status;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "curriculum", nullable = true)
  private Curriculum curriculum;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "supervisor", nullable = true)
  private Supervisor supervisor;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "faculty", nullable = true)
  private Faculty faculty;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "report", nullable = true)
  private Report report;

  public Phd(String oid, String name, String email, String pin) {
    this.oid = oid;
    this.name = name;
    this.email = email;
    this.pin = pin;
  }

  @Override
  public Phd toEntity(Row row) {
    JsonObject jsonObject = row.toJson();

    return jsonObject.mapTo(Phd.class);
  }
}
