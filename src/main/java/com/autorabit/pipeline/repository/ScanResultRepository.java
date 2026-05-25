package com.autorabit.pipeline.repository;

import com.autorabit.pipeline.entity.ScanResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScanResultRepository extends JpaRepository<ScanResult, Long> {

    ScanResult findTopByCodeChangeIdOrderByIdDesc(Long codeChangeId);
}