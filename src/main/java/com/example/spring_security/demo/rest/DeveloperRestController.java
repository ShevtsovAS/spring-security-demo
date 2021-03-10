package com.example.spring_security.demo.rest;

import com.example.spring_security.demo.model.Developer;
import com.example.spring_security.demo.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
public class DeveloperRestController {

    private final DeveloperService developerService;

    @GetMapping
    public List<Developer> getAll() {
        return developerService.getAll();
    }

    @GetMapping("/{id}")
    public Developer getById(@PathVariable Long id) {
        return developerService.getById(id);
    }

}
