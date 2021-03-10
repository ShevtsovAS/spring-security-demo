package com.example.spring_security.demo.rest;

import com.example.spring_security.demo.model.Developer;
import com.example.spring_security.demo.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/developers")
@RequiredArgsConstructor
public class DeveloperRestController {

    private final DeveloperService developerService;

    @GetMapping
    @PreAuthorize("hasAuthority('developers:read')")
    public List<Developer> getAll() {
        return developerService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('developers:read')")
    public Developer getById(@PathVariable Long id) {
        return developerService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('developers:write')")
    public Developer create(@RequestBody Developer developer) {
        return developerService.create(developer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('developers:write')")
    public void deleteById(@PathVariable Long id) {
        developerService.deleteById(id);
    }

}
