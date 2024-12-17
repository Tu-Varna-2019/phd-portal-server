package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
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

  // Докторантски изпит 1:
  // pdf на англииски език учебник - commission одобрил дали става за изпит - expert ili comisiya -
  // experta dobavq 2 член от комисията за оценка
  // ако е одобрен, се явява на изпит за Англииски език и получава оценка
  // ако е одобрен (над 4:50 оценка)
  //
  // Докторантски изпит 2: (по докторантската програма при кандидадстването) - experta dobavq 3 член
  // от комисията за оценка
  // phd изпраща презентация файл и реферат върху тема на изпита - 2 pdfa
  // ако е одобрен (над 4:50 оценка), тогава е приет

  @Id
  @SequenceGenerator(name = "gradeSequence", sequenceName = "grade_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gradeSequence")
  private Long id;

  @Column(nullable = false, unique = false)
  @NotNull
  private Double grade;

  @Column(name = "start_date", nullable = false, unique = false)
  private Date startDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Teacher", nullable = true)
  private Teacher teacher;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Commission", nullable = true)
  private Commission commission;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "GradeType", nullable = false)
  private GradeType gradeType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Phd", nullable = false)
  private Phd phd;
}
