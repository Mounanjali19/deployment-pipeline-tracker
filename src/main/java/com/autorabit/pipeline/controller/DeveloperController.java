package com.autorabit.pipeline.controller;

import com.autorabit.pipeline.entity.Developer;
import com.autorabit.pipeline.repository.DeveloperRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {

    private final DeveloperRepository developerRepository;

    public DeveloperController(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    @PostMapping
    public Developer createDeveloper(@RequestBody Developer developer) {
        return developerRepository.save(developer);
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }
}