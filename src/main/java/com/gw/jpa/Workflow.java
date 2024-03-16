package com.gw.jpa;

import org.hibernate.annotations.Type;

import javax.persistence.Lob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Workflow {
	
	@Id
	private String id;
	
	private String name, description, owner;

	private String confidential;
	
	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column
	private String edges;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column
	private String nodes;
	
	
	public String getConfidential() {
		return this.confidential;
	}

	public void setConfidential(String confidential) {
		this.confidential = confidential;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public String getEdges() {
		return edges;
	}

	public void setEdges(String edges) {
		this.edges = edges;
	}
	
	
	
}
