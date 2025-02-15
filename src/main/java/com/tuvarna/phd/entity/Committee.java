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
@Table(name = "committee")
public non-sealed class Committee extends PanacheEntityBase implements UserEntity<Committee> {

  @Id
  @SequenceGenerator(
      name = "committeeSequence",
      sequenceName = "committee_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "committeeSequence")
  private Long id;

  @Column(name = "oid", nullable = true, unique = true, updatable = false)
  private String oid;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false, unique = false)
  private String email;

  @Column(nullable = true, unique = false)
  private String picture;

  @Transient private String pictureBlob;

  @Column(nullable = false, unique = false)
  private Double grade;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Department department;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = true)
  private CommitteeRole role;

  public Committee(String oid, String name, String email) {
    this.oid = oid;
    this.name = name;
    this.email = email;
  }
}
