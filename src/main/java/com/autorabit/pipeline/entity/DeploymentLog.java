package com.autorabit.pipeline.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class DeploymentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer versionNumber;

    private boolean rolledBack;

    private LocalDateTime deployedAt;

    @ManyToOne
    @JoinColumn(name = "code_change_id")
    private CodeChange codeChange;

    public DeploymentLog() {
    }

    public Long getId() {
        return id;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public boolean isRolledBack() {
        return rolledBack;
    }

    public void setRolledBack(boolean rolledBack) {
        this.rolledBack = rolledBack;
    }

    public LocalDateTime getDeployedAt() {
        return deployedAt;
    }

    public void setDeployedAt(LocalDateTime deployedAt) {
        this.deployedAt = deployedAt;
    }

    public CodeChange getCodeChange() {
        return codeChange;
    }

    public void setCodeChange(CodeChange codeChange) {
        this.codeChange = codeChange;
    }
}