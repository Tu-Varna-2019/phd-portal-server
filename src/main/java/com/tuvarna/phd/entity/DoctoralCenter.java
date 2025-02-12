package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// @UserDefinition
@Table(name = "doctoralcenter")
public final class DoctoralCenter extends PanacheEntityBase implements UserEntity {

  @Id
  @SequenceGenerator(
      name = "doctoralCenterSequence",
      sequenceName = "doctoralCenter_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctoralCenterSequence")
  private Long id;

  @Column(nullable = true, unique = true, updatable = false)
  private String oid;

  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = false)
  private String email;

  @Column(nullable = true, unique = false)
  private String picture;

  @Transient private String pictureBlob;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role", nullable = false)
  private DoctoralCenterRole role;

  public DoctoralCenter(String oid, String name, String email) {
    this.oid = oid;
    this.name = name;
    this.email = email;
  }
}
