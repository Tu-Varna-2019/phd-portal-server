package com.tuvarna.phd.entity;

import java.sql.Date;
import java.util.Set;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "curriculum")
public class Curriculum extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "curriculumSequence", sequenceName = "curriculum_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curriculumSequence")
  private Long id;

  @Column(nullable = false, unique = false)
  private String description;

  @Column(name = "start_date", nullable = false, unique = false)
  private Date startDate;

  @Column(name = "end_date", nullable = false, unique = false)
  private Date endDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Mode", nullable = false)
  private Mode mode;

  @ManyToMany
  @JoinTable(name = "curriculum_subject", joinColumns = @JoinColumn(name = "curriculum_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
  private Set<Subject> subjects;
}