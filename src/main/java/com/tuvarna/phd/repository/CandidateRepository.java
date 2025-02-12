package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.exception.CandidateException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CandidateRepository implements PanacheRepositoryBase<Candidate, Integer> {

  public Candidate getByID(Candidate candidate) {
    return find("id", candidate.getId())
        .firstResultOptional()
        .orElseThrow(
            () -> new CandidateException("Candidate not found with id: " + candidate.getId()));
  }

  public Candidate getByEmail(Candidate candidate) {
    return find("email", candidate.getEmail()).firstResultOptional().orElseGet(null);
  }

  public void save(Candidate candidate) {
    candidate.persist();
  }

  public void deleteByID(Long id) {
    delete("id", id);
  }
}
