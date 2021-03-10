package com.example.spring_security.demo.rest;

import com.example.spring_security.demo.exeptions.UserNotFoundException;
import com.example.spring_security.demo.model.Developer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperRestController {

    private static final Map<Long, Developer> DEVELOPERS = Stream.of(
            Developer.of(1L, "Ivan", "Ivanov"),
            Developer.of(2L, "Sergey", "Sergeev"),
            Developer.of(3L, "Petr", "Petrov")
    ).collect(toMap(Developer::getId, it -> it));

    @GetMapping
    public List<Developer> getAll() {
        return new ArrayList<>(DEVELOPERS.values());
    }

    @GetMapping("/{id}")
    public Developer getById(@PathVariable Long id) {
        return Optional.ofNullable(DEVELOPERS.get(id))
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %s not found", id)));
    }

}
