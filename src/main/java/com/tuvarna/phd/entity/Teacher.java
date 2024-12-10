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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher")
public class Teacher extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "teacherSequence", sequenceName = "teacher_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacherSequence")
  private Long id;

  @Column(nullable = false, unique = false)
  @NotNull
  private String name;

  @Column(nullable = false, unique = false)
  private String email;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Department", nullable = false)
  private Department department;
}
