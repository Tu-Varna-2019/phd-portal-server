package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.IPBlock;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.net.InetAddress;

@ApplicationScoped
public class IPBlockRepository implements PanacheRepositoryBase<IPBlock, Integer> {

  public IPBlock getByIP(InetAddress inetAddress) {
    return find("ip", inetAddress).firstResultOptional().orElseGet(null);
  }

  public void save(IPBlock ipBlock) {
    ipBlock.persist();
  }

  public void deleteByIP(InetAddress inetAddress) {
    delete("ip", inetAddress);
  }
}
