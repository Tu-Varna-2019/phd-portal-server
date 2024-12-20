package com.tuvarna.phd.entity;

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
@Table(name = "grade")
public class Grade extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "gradeSequence", sequenceName = "grade_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gradeSequence")
  private Long id;

  @Column(nullable = false, unique = false)
  private Double grade;

  @Column(name = "date", nullable = false, unique = false)
  private Date date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Commission", nullable = true)
  private Commission commission;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Phd", nullable = false)
  private Phd phd;
}
