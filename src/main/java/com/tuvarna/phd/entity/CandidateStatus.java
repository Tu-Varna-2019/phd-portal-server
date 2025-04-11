package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Row;
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
@Table(name = "candidate_status")
public class CandidateStatus extends PanacheEntityBase implements IEntity<CandidateStatus> {
  @Id
  @SequenceGenerator(
      name = "candidateStatusSequence",
      sequenceName = "candidatestatus_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "candidateStatusSequence")
  private Long id;

  @Column(nullable = false, unique = true)
  private String status;

  @Override
  public CandidateStatus toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(CandidateStatus.class);
  }
}
