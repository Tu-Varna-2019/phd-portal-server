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
import jakarta.persistence.Transient;
import java.sql.Date;
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
@Table(name = "grade")
public class Grade extends PanacheEntityBase implements IEntity<Grade> {

  @Id
  @SequenceGenerator(name = "gradeSequence", sequenceName = "grade_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gradeSequence")
  private Long id;

  @Column(nullable = true, unique = false)
  private Double grade;

  @Column(name = "eval_date", nullable = false, unique = false)
  private Date evalDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = true)
  private Commission commission;

  @Column(nullable = false, unique = false)
  private String report;

  @Column(nullable = true, unique = false)
  private Set<String> attachments;

  @Transient private Set<String> attachmentsBlob;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Subject subject;

  public Grade(Date evalDate, String report, Subject subject) {
    this.evalDate = evalDate;
    this.report = report;
    this.subject = subject;
  }

  @Override
  public Grade toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Grade.class);
  }
}
