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
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@Table(name = "doctoral_center_role")
public class DoctoralCenterRole extends PanacheEntityBase implements IEntity<DoctoralCenterRole> {

  @Id
  @JsonIgnore
  @SequenceGenerator(
      name = "doctoralCenterRoleSequence",
      sequenceName = "doctoralCenterRole_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctoralCenterRoleSequence")
  private Long id;

  @Column(nullable = false, unique = true)
  private String role;

  public DoctoralCenterRole(Long id) {
    this.id = id;
  }

  @Override
  public DoctoralCenterRole toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(DoctoralCenterRole.class);
  }
}
