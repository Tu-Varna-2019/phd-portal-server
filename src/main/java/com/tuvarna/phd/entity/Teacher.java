package com.tuvarna.phd.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
// @Cacheable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends PanacheEntity {

  @Column(nullable = false, unique = false)
  private String name;
}
