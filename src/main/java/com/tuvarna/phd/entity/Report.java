package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report")
public class Report extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "reportSequence", sequenceName = "report_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reportSequence")
  private Long id;

  @Column(nullable = true, unique = false)
  private String name;

  @Column(nullable = true, unique = false)
  // Форма на провеждане
  private String type;

  // Тримесечен и годишен
  // Автоматично генерирани след като докторанта е одобрен
  @Column(nullable = true, unique = false)
  private Date startDate;

  @Column(nullable = true, unique = false)
  private Date endDate;

  @Column(nullable = true, unique = false)
  private Integer orderNum;
}
