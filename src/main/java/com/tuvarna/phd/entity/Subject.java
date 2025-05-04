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
import java.sql.Date;
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
public class Subject extends PanacheEntityBase implements IEntity<Subject> {

  @Id
  @SequenceGenerator(name = "subjectSequence", sequenceName = "subject_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjectSequence")
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "grade", nullable = true)
  private Grade grade;

  @JoinColumn(name = "exam_date", nullable = true)
  private Date examDate;

  @ManyToMany(mappedBy = "subjects", fetch = FetchType.EAGER)
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  private Set<Curriculum> curriculums;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher", nullable = true)
  private Committee teacher;

  @Override
  public Subject toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Subject.class);
  }

  @Override
  public String toString() {
    return "Name: " + this.name;
  }
}
