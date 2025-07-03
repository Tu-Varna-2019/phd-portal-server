package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Row;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Table(name = "commission")
public class Commission extends PanacheEntityBase implements IEntity<Commission> {

  @Id
  @SequenceGenerator(
      name = "commissionSequence",
      sequenceName = "commission_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commissionSequence")
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToMany
  @JoinTable(
      name = "commission_committees",
      joinColumns = @JoinColumn(name = "commission_id"),
      inverseJoinColumns = @JoinColumn(name = "committee_id"))
  private Set<Committee> members;

  public Commission(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Commission(String name) {
    this.name = name;
  }

  @Override
  public Commission toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Commission.class);
  }
}
