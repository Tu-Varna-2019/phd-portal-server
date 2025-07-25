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
@Table(name = "subject")
public class Subject extends PanacheEntityBase implements IEntity<Subject>, Comparable<Subject> {

  @Id
  @SequenceGenerator(name = "subjectSequence", sequenceName = "subject_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjectSequence")
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false, unique = false)
  private Integer semester;

  @Column(nullable = false, unique = false)
  private Integer course;

  @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  private Set<Curriculum> curriculums;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "teacher", nullable = true)
  private Committee teacher;

  @Override
  public Subject toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Subject.class);
  }

  public Subject(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Name: " + this.name;
  }

  @Override
  public int compareTo(Subject subject) {
    return Integer.compare(this.course, subject.getCourse());
  }
}
