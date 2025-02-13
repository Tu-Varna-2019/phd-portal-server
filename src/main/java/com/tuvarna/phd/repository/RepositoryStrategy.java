package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public interface RepositoryStrategy<T extends UserEntity<T>> {

  void save(T repository);

  List<T> getAll();
}
