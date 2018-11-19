package com.app.alm.dto;

public class ALMConfigDto {
	public String host = "cbigdc-isapk901.cbi.net";
	public String port = "8080";
	public String username = "jchandran";
	public String password = "jchandran";
	public String domain = "WINE_OPERATIONS";
	public String project = "WMS_TMS_1";
	public String pageSize = "1000";
	private String almConfigG1="";
	private String almConfigG2="";
	

	public String getAlmConfigG1() {
		return almConfigG1;
	}

	public void setAlmConfigG1(String almConfigG1) {
		this.almConfigG1 = almConfigG1;
	}

	public String getAlmConfigG2() {
		return almConfigG2;
	}

	public void setAlmConfigG2(String almConfigG2) {
		this.almConfigG2 = almConfigG2;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

}
