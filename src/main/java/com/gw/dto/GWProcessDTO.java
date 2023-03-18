package com.gw.dto;

public class GWProcessDTO {

    String id;
    String name;
    String description;
    String code;
    String lang;
    String owner;
    String confidential;
    String workflowId;

    public GWProcessDTO() {
    }

    public GWProcessDTO(String id, String name, String code, String lang, String owner, String confidential, String workflowId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.lang = lang;
        this.owner = owner;
        this.confidential = confidential;
        this.workflowId = workflowId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getConfidential() {
        return confidential;
    }

    public void setConfidential(String confidential) {
        this.confidential = confidential;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }
}
