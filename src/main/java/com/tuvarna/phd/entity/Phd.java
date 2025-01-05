package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.jpa.Password;
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
public class Phd extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "phdSequence", sequenceName = "phd_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phdSequence")
  private Long id;

  @Column(name = "oid", nullable = false, unique = true, updatable = false)
  private String oid;

  @Column(name = "first_name", nullable = false, unique = false)
  private String firstName;

  @Column(name = "middle_name", nullable = false, unique = false)
  private String middleName;

  @Column(name = "last_name", nullable = false, unique = false)
  private String lastName;

  @Column(nullable = false, unique = false)
  private String email;

  @Column(name = "picture", nullable = true, unique = false)
  private String picture;

  @Column(nullable = false, unique = false)
  private String country;

  @Column(nullable = false, unique = false)
  private String city;

  @Column(nullable = false, unique = false)
  private String address;

  @Password
  @Column(nullable = false, unique = true)
  // TODO: Encrypt this pls
  // ЕГН
  private String pin;

  @Column(name = "dissertation_topic", nullable = true, unique = false)
  private String dissertationTopic;

  @Column(name = "enroll_date", nullable = true, unique = false)
  private Date enrollDate;

  @Column(name = "grad_date", nullable = true, unique = false)
  private Date gradDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "PhdStatus", nullable = false)
  private PhdStatus status;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "Curriculum", nullable = true)
  private Curriculum curriculum;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "Supervisor", nullable = true)
  private Supervisor supervisor;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "Department", nullable = true)
  private Department department;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "Report", nullable = true)
  private Report report;

  public String getFullName() {
    return this.getFirstName() + this.getMiddleName() + this.getLastName();
  }
}
