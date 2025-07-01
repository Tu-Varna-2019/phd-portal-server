package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Row;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "committee_grade")
public class CommitteeGrade extends PanacheEntityBase implements IEntity<CommitteeGrade> {
  @Id
  @SequenceGenerator(
      name = "committeeGradeSequence",
      sequenceName = "committee_grade_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "committeeGradeSequence")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "committee_id", nullable = false)
  private Committee committee;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "grade_id", nullable = false)
  private Grade gradeEntry;

  @Column(nullable = false, unique = false)
  private Double grade;

  @Override
  public CommitteeGrade toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(CommitteeGrade.class);
  }
}
