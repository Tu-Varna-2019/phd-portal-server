package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Row;
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
import java.util.Arrays;
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
public class Curriculum extends PanacheEntityBase implements IEntity<Curriculum> {

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mode", nullable = false)
  private Mode mode;

  public Curriculum setMode(Mode mode) {
    this.mode = mode;
    return this;
  }

  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  @JoinTable(
      name = "curriculum_subject",
      joinColumns = @JoinColumn(name = "curriculum_id"),
      inverseJoinColumns = @JoinColumn(name = "subject_id"))
  private Set<Subject> subjects;

  public Curriculum setSubjects(Set<Subject> subjects) {
    this.subjects = subjects;
    return this;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "faculty", nullable = true)
  private Faculty faculty;

  public Curriculum setFaculty(Faculty faculty) {
    this.faculty = faculty;
    return this;
  }

  @Column(name = "is_public", nullable = false)
  private Boolean isPublic;

  public Curriculum setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
    return this;
  }

  @Override
  public Curriculum toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Curriculum.class);
  }

  @Override
  public String toString() {
    return "Id: "
        + this.id
        + " Name: "
        + this.name
        + " Mode: "
        + this.mode.getMode()
        + " isPublic: "
        + this.isPublic
        + " Faculty: "
        + this.faculty
        + " Subjects: "
        + Arrays.toString(this.subjects.toArray());
  }

  public Curriculum(String name, Mode mode, Set<Subject> subjects) {
    this.name = name;
    this.mode = mode;
    this.subjects = subjects;
  }

  public Curriculum(String name) {
    this.name = name;
  }
}
