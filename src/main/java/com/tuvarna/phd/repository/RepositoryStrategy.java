package com.tuvarna.phd.repository;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public interface RepositoryStrategy<T> {

  void save(T repository);

  List<T> getAll();
}
