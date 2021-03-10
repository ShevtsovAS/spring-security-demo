package com.example.spring_security.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class Developer {
    private Long id;
    private String firstName;
    private String lastName;
}
