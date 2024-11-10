package com.tuvarna.phd.repository;

import com.tuvarna.phd.entity.Teacher;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TeacherRepository implements PanacheRepository<Teacher> {}
