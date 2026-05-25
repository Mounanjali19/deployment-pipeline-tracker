package com.autorabit.pipeline.dto;

public class DeploymentRequest {

    private String environment;

    public DeploymentRequest() {
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}