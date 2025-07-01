package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.CommitteeGrade;
import com.tuvarna.phd.exception.HttpException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public final class CommitteeGradeRepository
    implements PanacheRepositoryBase<CommitteeGrade, Integer>, RepositoryStrategy<CommitteeGrade> {

  @Override
  public CommitteeGrade getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Committee grade with id: " + id + " doesn't exist!"));
  }

  public CommitteeGrade getByCommitteeId(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Committee with id: " + id + " doesn't exist!"));
  }

  public CommitteeGrade getByGradeId(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new HttpException("Grade with id: " + id + " doesn't exist!"));
  }

  @Override
  public void save(CommitteeGrade committeeGrade) {
    committeeGrade.persist();
  }

  @Override
  public List<CommitteeGrade> getAll() {
    return listAll();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }
}
