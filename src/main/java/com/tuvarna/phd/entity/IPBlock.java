package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.net.InetAddress;
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
@Table(name = "ipblock")
public class IPBlock extends PanacheEntityBase {

  @Id
  @SequenceGenerator(name = "ipblockSequence", sequenceName = "ipblock_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ipblockSequence")
  private Long id;

  @Column(nullable = false, unique = true)
  private InetAddress ip;

  public IPBlock(InetAddress ip) {
    this.ip = ip;
  }
}
