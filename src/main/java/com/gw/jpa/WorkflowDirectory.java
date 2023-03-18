package com.gw.jpa;

import javax.persistence.*;

@Entity
public class WorkflowDirectory {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String sourcePath;

    @Column
    private String gwWorkspacePath;

    public WorkflowDirectory() {
    }

    public WorkflowDirectory(Long id, String sourcePath, String gwWorkspacePath) {
        this.id = id;
        this.sourcePath = sourcePath;
        this.gwWorkspacePath = gwWorkspacePath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getGwWorkspacePath() {
        return gwWorkspacePath;
    }

    public void setGwWorkspacePath(String gwWorkspacePath) {
        this.gwWorkspacePath = gwWorkspacePath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
