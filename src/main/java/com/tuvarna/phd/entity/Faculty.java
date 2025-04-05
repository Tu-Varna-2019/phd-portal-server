package com.tuvarna.phd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Row;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "faculty")
public class Faculty extends PanacheEntityBase implements IEntity<Faculty> {

  @Id
  @SequenceGenerator(name = "facultySequence", sequenceName = "faculty_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "facultySequence")
  @JsonIgnore
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Override
  public Faculty toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Faculty.class);
  }
}
