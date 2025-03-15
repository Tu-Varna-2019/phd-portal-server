package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.CandidateStatus;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CandidateStatusRepository implements PanacheRepositoryBase<CandidateStatus, Integer> {

  public CandidateStatus getByStatus(String status) {
    return find("status", status)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Candidate status: " + status + " doesn't exist!"));
  }
}
