package com.autorabit.pipeline.entity;

import jakarta.persistence.*;

@Entity
public class ScanResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean passed;

    private String issuesFound;

    @OneToOne
    @JoinColumn(name = "code_change_id")
    private CodeChange codeChange;

    public ScanResult() {
    }

    public Long getId() {
        return id;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getIssuesFound() {
        return issuesFound;
    }

    public void setIssuesFound(String issuesFound) {
        this.issuesFound = issuesFound;
    }

    public CodeChange getCodeChange() {
        return codeChange;
    }

    public void setCodeChange(CodeChange codeChange) {
        this.codeChange = codeChange;
    }
}