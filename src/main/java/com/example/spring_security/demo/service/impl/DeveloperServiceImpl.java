package com.example.spring_security.demo.service.impl;

import com.example.spring_security.demo.exeptions.UserNotFoundException;
import com.example.spring_security.demo.model.Developer;
import com.example.spring_security.demo.repository.DeveloperRepository;
import com.example.spring_security.demo.service.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    @Override
    public List<Developer> getAll() {
        return developerRepository.getAll();
    }

    @Override
    public Developer getById(Long id) {
        return developerRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id: %s not found", id)));
    }
}
