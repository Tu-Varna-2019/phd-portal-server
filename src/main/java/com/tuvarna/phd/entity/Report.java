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
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// NOTE: Once created, it cannot be modified
@Entity
@Getter
@Setter
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report")
public class Report extends PanacheEntityBase implements IEntity<Report> {

  @Id
  @SequenceGenerator(name = "reportSequence", sequenceName = "report_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reportSequence")
  private Long id;

  @Column(nullable = true, unique = false)
  private String name;

  @Column(nullable = true, unique = false)
  // NOTE: Форма на провеждане
  private String conduct;

  // NOTE: Тримесечен и годишен
  // Автоматично генерирани след като докторанта е одобрен
  @Column(name = "start_date", nullable = true, unique = false)
  private Date startDate;

  @Column(name = "end_date", nullable = true, unique = false)
  private Date endDate;

  @Column(name = "order_number", nullable = true, unique = false)
  private Integer orderNumber;

  @Override
  public Report toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Report.class);
  }
}
