package com.example.spring_security.demo.service;

import com.example.spring_security.demo.model.Developer;

import java.util.List;

public interface DeveloperService {
    List<Developer> getAll();
    Developer getById(Long id);
}
