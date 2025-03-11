package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Cacheable;
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
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Getter
@Setter
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "curriculum")
// ALWAYS individualen
public class Curriculum extends PanacheEntityBase {

  @Id
  @SequenceGenerator(
      name = "curriculumSequence",
      sequenceName = "curriculum_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curriculumSequence")
  private Long id;

  // NOTE: Cannot make it unique due to requiring yearPeirod to be both 3 and 4 for the 2 type of
  // modes
  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = false)
  private Long yearPeriod;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mode", nullable = false)
  private Mode mode;

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  @JoinTable(
      name = "curriculum_subject",
      joinColumns = @JoinColumn(name = "curriculum_id"),
      inverseJoinColumns = @JoinColumn(name = "subject_id"))
  private Set<Subject> subjects;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "faculty", nullable = true)
  private Faculty faculty;
}
