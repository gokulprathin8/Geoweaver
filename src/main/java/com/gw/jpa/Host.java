package com.gw.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Host {

	@Id
//	@GeneratedValue(generator="system-uuid")
//	@GenericGenerator(name="system-uuid", strategy = "uuid")
	String id;
	
	String name, ip, port, username, owner, type, url;

	String confidential;

	public String getConfidential() {
		return this.confidential;
	}

	public void setConfidential(String confidential) {
		this.confidential = confidential;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
