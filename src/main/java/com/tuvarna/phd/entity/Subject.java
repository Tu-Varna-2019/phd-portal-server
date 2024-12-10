package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subject")
public class Subject extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "subjectSequence", sequenceName = "subject_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjectSequence")
  private Long id;

  @Column(nullable = false, unique = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Grade", nullable = false)
  private Grade grade;

  @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
  private Set<Curriculum> curriculums;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Teacher", nullable = false)
  private Teacher teacher;
}
