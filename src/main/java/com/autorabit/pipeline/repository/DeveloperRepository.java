package com.autorabit.pipeline.repository;

import com.autorabit.pipeline.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
}