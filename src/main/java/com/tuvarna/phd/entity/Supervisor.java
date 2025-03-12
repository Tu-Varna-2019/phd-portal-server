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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supervisor")
public non-sealed class Supervisor extends PanacheEntityBase implements IUserEntity<Supervisor> {

  @Id
  @SequenceGenerator(
      name = "supervisorSequence",
      sequenceName = "supervisor_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supervisorSequence")
  private Long id;

  @Column(nullable = false, unique = true, updatable = false)
  private String oid;

  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = false)
  private String picture;

  @Transient private String pictureBlob;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "title", nullable = false)
  private SupervisorTitle title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "faculty", nullable = true)
  private Faculty faculty;

  // TODO: create a functionality where he manually adds diserattions topics
  @Column(nullable = true, unique = false)
  private List<String> dissertations;

  public Supervisor(String oid, String name, String email) {
    this.oid = oid;
    this.name = name;
    this.email = email;
  }

  @Override
  public Supervisor toEntity(Row row) {
    JsonObject jsonObject = row.toJson();

    return jsonObject.mapTo(Supervisor.class);
  }
}
