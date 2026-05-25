package com.autorabit.pipeline.dto;

    public class CreateChangeRequest {

        private String title;
        private String description;
        private String repositoryName;
        private Long developerId;

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

        public Long getDeveloperId() {
            return developerId;
        }

        public void setDeveloperId(Long developerId) {
            this.developerId = developerId;
        }
    }