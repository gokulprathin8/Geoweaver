package com.gw.jpa;

import javax.persistence.*;

@Entity
public class WorkflowDirectory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String workflowDirectoryName;

    @Column
    private String directoryPath;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getWorkflowDirectoryName() {
        return workflowDirectoryName;
    }

    public void setWorkflowDirectoryName(String workflowId) {
        this.workflowDirectoryName = workflowId;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public WorkflowDirectory() {}

    public WorkflowDirectory(String workflowDirectoryName, String directoryPath) {
        this.workflowDirectoryName = workflowDirectoryName;
        this.directoryPath = directoryPath;
    }
}
