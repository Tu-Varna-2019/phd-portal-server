package com.tuvarna.phd.entity;

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
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Table(name = "supervisor_title")
public class SupervisorTitle extends PanacheEntityBase implements IEntity<SupervisorTitle> {

  @Id
  @SequenceGenerator(
      name = "supervisortitleSequence",
      sequenceName = "supervisortitle_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supervisorTypeSequence")
  private Long id;

  @Column(nullable = false, unique = true)
  private String title;

  @Override
  public SupervisorTitle toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(SupervisorTitle.class);
  }
}
