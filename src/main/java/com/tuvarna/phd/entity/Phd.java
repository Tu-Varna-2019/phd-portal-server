package com.tuvarna.phd.entity;

import java.sql.Date;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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

  @Column(name = "first_name", nullable = false, unique = false)
  private String firstName;

  @Column(name = "middle_name", nullable = false, unique = false)
  private String middleName;

  @Column(name = "last_name", nullable = false, unique = false)
  private String lastName;

  @Column(nullable = false, unique = false)
  private String email;

  @Column(name = "enroll_date", nullable = false, unique = false)
  private Date enrollDate;

  @Column(name = "grad_date", nullable = false, unique = false)
  private Date gradDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "StatusPhd", nullable = false)
  private StatusPhd status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Curriculum", nullable = false)
  private Curriculum curriculum;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Supervisor", nullable = false)
  private Supervisor supervisor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Department", nullable = false)
  private Department department;
}
