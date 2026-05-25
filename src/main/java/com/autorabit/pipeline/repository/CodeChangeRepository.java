package com.autorabit.pipeline.repository;

import com.autorabit.pipeline.entity.CodeChange;
import com.autorabit.pipeline.enums.ChangeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeChangeRepository extends JpaRepository<CodeChange, Long> {

    List<CodeChange> findByStatus(ChangeStatus status);
}