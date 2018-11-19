package com.app.alm.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ALMConfig")
public class ALMConfig {

	private String almHost = "";
	private String almPort = "";
	private String almUserName = "";
	private String almPassword = "";
	private String almDomain = "";
	private String almProject = "";
	private String almPageSize = "";
	private String almConfigG1 = "";
	private String almConfigG2 = "";

	public String getAlmHost() {
		return almHost;
	}

	public void setAlmHost(String almHost) {
		this.almHost = almHost;
	}

	public String getAlmPort() {
		return almPort;
	}

	public void setAlmPort(String almPort) {
		this.almPort = almPort;
	}

	public String getAlmUserName() {
		return almUserName;
	}

	public void setAlmUserName(String almUserName) {
		this.almUserName = almUserName;
	}

	public String getAlmPassword() {
		return almPassword;
	}

	public void setAlmPassword(String almPassword) {
		this.almPassword = almPassword;
	}

	public String getAlmDomain() {
		return almDomain;
	}

	public void setAlmDomain(String almDomain) {
		this.almDomain = almDomain;
	}

	public String getAlmProject() {
		return almProject;
	}

	public void setAlmProject(String almProject) {
		this.almProject = almProject;
	}

	public String getAlmPageSize() {
		return almPageSize;
	}

	public void setAlmPageSize(String almPageSize) {
		this.almPageSize = almPageSize;
	}

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


}
