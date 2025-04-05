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
import java.net.InetAddress;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
@Table(name = "ip_block")
public class IPBlock extends PanacheEntityBase implements IEntity<IPBlock> {
  @Id
  @SequenceGenerator(name = "ipblockSequence", sequenceName = "ipblock_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ipblockSequence")
  private Long id;

  @Column(nullable = false, unique = true)
  private InetAddress ip;

  @Column(name = "block_time", nullable = false, unique = false)
  private Timestamp blockTime;

  // NOTE: expiry is 1 month
  @Column(name = "block_expiry_time", nullable = false, unique = false)
  private Timestamp blockExpiryTime;

  public IPBlock(InetAddress ip, Timestamp blockTime) {
    this.ip = ip;
    this.blockTime = blockTime;
    this.blockExpiryTime = Timestamp.valueOf(LocalDateTime.now().plusMonths(1));
  }

  @Override
  public IPBlock toEntity(Row row) {
    JsonObject jsonObject = row.toJson();
    return jsonObject.mapTo(IPBlock.class);
  }
}
