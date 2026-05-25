package com.autorabit.pipeline.repository;

import com.autorabit.pipeline.entity.DeploymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeploymentLogRepository extends JpaRepository<DeploymentLog, Long> {

    List<DeploymentLog> findByCodeChangeId(Long codeChangeId);
}