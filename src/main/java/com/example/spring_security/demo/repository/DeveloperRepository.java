package com.example.spring_security.demo.repository;

import com.example.spring_security.demo.model.Developer;

import java.util.List;
import java.util.Optional;

public interface DeveloperRepository {
    List<Developer> getAll();

    Optional<Developer> getById(Long id);

    Developer save(Developer developer);

    boolean deleteById(Long id);
}
