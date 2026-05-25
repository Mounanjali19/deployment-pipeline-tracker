package com.autorabit.pipeline.dto;

public class ScanRequest {

    private boolean passed;

    private String issuesFound;

    public ScanRequest() {
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
}