package com.tuvarna.phd.repository;

import java.util.List;

public interface RepositoryStrategy<T> {

  T getById(Long id);

  void deleteById(Long id);

  void save(T repository);

  List<T> getAll();
}
