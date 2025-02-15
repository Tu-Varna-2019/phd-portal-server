package com.tuvarna.phd.service;

import com.tuvarna.phd.entity.IPBlock;
import com.tuvarna.phd.repository.IPBlockRepository;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.jboss.logging.Logger;

@ApplicationScoped
public final class IPBlockServiceImpl implements IPBlockService {
  @Inject IPBlockRepository ipBlockRepository;
  @Inject private Logger LOG;
  @Inject RoutingContext ipContext;

  @Override
  public boolean isClientIPBlocked() {
    try {
      InetAddress ip = InetAddress.getByName(this.getClientIP());
      IPBlock ipblock = this.ipBlockRepository.getByIP(ip);

      if (ipblock == null) {
        LOG.info(
            "Client with ip: "
                + ip.toString()
                + " is not present in the IPBlock table. Adding him now...");
        this.ipBlockRepository.save(new IPBlock(ip));
        return false;
      }

      LOG.warn(
          "Client with ip: "
              + ip.toString()
              + " is present in the ipBlock table! Blocking him now...");
      return true;
    } catch (UnknownHostException exception) {
      LOG.error("Error in retrieving the client ip: " + exception);
      return false;
    }
  }

  private String getClientIP() {
    return ((HttpServerRequest) ipContext.request()).remoteAddress().host();
  }
}
