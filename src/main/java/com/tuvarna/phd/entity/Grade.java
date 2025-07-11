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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "commision", nullable = true)
  private Commission commission;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "subject", nullable = false)
  private Subject subject;

  // NOTE: Change this to description ?
  @Column(nullable = false, unique = false)
  private String report;

  @Column(nullable = true, unique = false)
  private Set<String> attachments;

  @Transient private Set<String> attachmentsBlob;

  public Grade(Date evalDate, String report, Subject subject) {
    this.evalDate = evalDate;
    this.report = report;
    this.subject = subject;
  }

  public Grade(Long id, Double grade, Date evalDate, String report, Subject subject) {
    this.id = id;
    this.grade = grade;
    this.evalDate = evalDate;
    this.report = report;
    this.subject = subject;
  }

  public Grade(Long id) {
    this.id = id;
  }

  public Grade(String report, Date evalDate, Subject subject) {
    this.report = report;
    this.evalDate = evalDate;
    this.subject = subject;
  }

  @Override
  public Grade toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Grade.class);
  }
}
