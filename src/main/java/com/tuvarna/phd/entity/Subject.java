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
import java.util.Date;
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

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "Grade", nullable = true)
  private Grade grade;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "examDate", nullable = true)
  private Date examDate;

  @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
  private Set<Curriculum> curriculums;

  @ManyToOne(fetch = FetchType.LAZY)
  // TODO: maybe set nullable to false again ?
  @JoinColumn(name = "Committee", nullable = true)
  private Committee teacher;
}
