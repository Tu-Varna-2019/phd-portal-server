package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Candidate;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CandidateRepository
    implements PanacheRepositoryBase<Candidate, Integer>, RepositoryStrategy<Candidate> {

  @Override
  public Candidate getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Candidate not found with id: " + id));
  }

  public Candidate getByEmail(String email) {
    return find("email", email)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Candidate not found with email: " + email));
  }

  @Override
  public void save(Candidate candidate) {
    candidate.persist();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }

  public void deleteByEmail(String email) {
    delete("email", email);
  }

  @Override
  public List<Candidate> getAll() {
    return listAll();
  }
}
