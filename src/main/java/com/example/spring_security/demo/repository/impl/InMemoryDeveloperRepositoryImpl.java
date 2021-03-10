package com.example.spring_security.demo.repository.impl;

import com.example.spring_security.demo.model.Developer;
import com.example.spring_security.demo.repository.DeveloperRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@Repository
public class InMemoryDeveloperRepositoryImpl implements DeveloperRepository {

    private static final Map<Long, Developer> DEVELOPERS = Stream.of(
            Developer.of(1L, "Ivan", "Ivanov"),
            Developer.of(2L, "Sergey", "Sergeev"),
            Developer.of(3L, "Petr", "Petrov")
    ).collect(toMap(Developer::getId, it -> it));

    @Override
    public List<Developer> getAll() {
        return new ArrayList<>(DEVELOPERS.values());
    }

    @Override
    public Optional<Developer> getById(Long id) {
        return Optional.ofNullable(DEVELOPERS.get(id));
    }

    @Override
    public Developer save(Developer developer) {
        DEVELOPERS.put(developer.getId(), developer);
        return developer;
    }

    @Override
    public boolean deleteById(Long id) {
        return DEVELOPERS.remove(id) != null;
    }
}
