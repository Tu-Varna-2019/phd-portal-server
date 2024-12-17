package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
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
@Table(name = "statusPhd")
public class StatusPhd extends PanacheEntityBase {

  @Id
  @SequenceGenerator(
      name = "statusPhdSequence",
      sequenceName = "statusPhd_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusPhdSequence")
  private Long id;

  @Column(nullable = false, unique = false)
  private String status;
  // expert terminates ако изпита е pod 4.50
  // expert accpets ако изпита е pod 4.50
}
