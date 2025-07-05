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
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "committee")
public non-sealed class Committee extends PanacheEntityBase implements IUserEntity<Committee> {
  @Id
  @SequenceGenerator(
      name = "committeeSequence",
      sequenceName = "committee_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "committeeSequence")
  private Long id;

  @Column(nullable = false, unique = true, updatable = false)
  private String oid;

  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = true, unique = false)
  private String picture;

  @Transient private String pictureBlob;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "faculty", nullable = false)
  private Faculty faculty;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role", nullable = false)
  private CommitteeRole role;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "title", nullable = false)
  private CommitteeTitle title;

  // TODO: create a functionality where he manually adds diserattions topics
  @Column(nullable = true, unique = false)
  private List<String> dissertations;

  public Committee(String oid, String name, String email) {
    this.oid = oid;
    this.name = name;
    this.email = email;
  }

  public Committee(
      Long id,
      String oid,
      String name,
      String email,
      String picture,
      Faculty faculty,
      CommitteeRole committeeRole) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.picture = picture;
    this.faculty = faculty;
    this.role = committeeRole;
  }

  @Override
  public Committee toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(Committee.class);
  }

  public static List<String> getOids(Set<Committee> committees) {
    List<String> oids = new ArrayList<>();

    committees.forEach(
        committee -> {
          oids.add(committee.getOid());
        });

    return oids;
  }
}
