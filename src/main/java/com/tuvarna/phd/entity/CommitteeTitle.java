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
@Table(name = "committee_title")
public class CommitteeTitle extends PanacheEntityBase implements IEntity<CommitteeTitle> {

  @Id
  @SequenceGenerator(
      name = "committeetitleSequence",
      sequenceName = "committeetitle_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "committeetitleSequence")
  private Long id;

  @Column(nullable = false, unique = true)
  private String title;

  @Override
  public CommitteeTitle toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(CommitteeTitle.class);
  }
}
