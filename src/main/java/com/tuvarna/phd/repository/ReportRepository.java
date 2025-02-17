package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Report;
import com.tuvarna.phd.exception.ReportException;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ReportRepository
    implements PanacheRepositoryBase<Report, Integer>, RepositoryStrategy<Report> {
  @Override
  public Report getById(Long id) {
    return find("id", id)
        .firstResultOptional()
        .orElseThrow(() -> new ReportException("Report not found with id: " + id));
  }

  @Override
  public void save(Report report) {
    report.persist();
  }

  @Override
  public void deleteById(Long id) {
    delete("id", id);
  }

  @Override
  public List<Report> getAll() {
    return listAll();
  }
}
