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
@Table(name = "doctoralCenter")
public class DoctoralCenter extends PanacheEntityBase {

  @Id
  @SequenceGenerator(
      name = "doctoralCenterSequence",
      sequenceName = "doctoralCenter_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctoralCenterSequence")
  private Long id;

  @Column(name = "oid", nullable = true, unique = true, updatable = false)
  private String oid;

  @Column(nullable = false, unique = false)
  private String name;

  @Column(nullable = false, unique = false)
  private String email;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role", nullable = true)
  private DoctoralCenterRole role;
}
