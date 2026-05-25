package com.autorabit.pipeline.entity;

import com.autorabit.pipeline.enums.ChangeStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CodeChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String repositoryName;

    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    private ChangeStatus status;

    @ManyToOne
    @JoinColumn(name = "developer_id")
    private Developer developer;

    public CodeChange() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public ChangeStatus getStatus() {
        return status;
    }

    public void setStatus(ChangeStatus status) {
        this.status = status;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }
}