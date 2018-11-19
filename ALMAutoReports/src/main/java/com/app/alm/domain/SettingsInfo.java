package com.app.alm.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * This Class is main configuration container.
 * @author intakhabalam.s
 *
 */
@XmlRootElement(name = "DogInfoConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsInfo {

	private MailConfig xmailConfig=new MailConfig();
    private boolean autoPilot=false;
	private String autoPilotCron="";
	private boolean enableAlmReport=false;
	private boolean enableMail = false;
	private ALMConfig xalmConfig=new ALMConfig();
	private String toWhomEmail="";
	private boolean enableStartupEmail=false;
	private boolean enableShutdownEmail=false;
	//
	
	
	public MailConfig getXmailConfig() {
		return xmailConfig;
	}
	public boolean isEnableStartupEmail() {
		return enableStartupEmail;
	}
	public void setEnableStartupEmail(boolean enableStartupEmail) {
		this.enableStartupEmail = enableStartupEmail;
	}
	public boolean isEnableShutdownEmail() {
		return enableShutdownEmail;
	}
	public void setEnableShutdownEmail(boolean enableShutdownEmail) {
		this.enableShutdownEmail = enableShutdownEmail;
	}
	public void setXmailConfig(MailConfig xmailConfig) {
		this.xmailConfig = xmailConfig;
	}
	public boolean isAutoPilot() {
		return autoPilot;
	}
	public void setAutoPilot(boolean autoPilot) {
		this.autoPilot = autoPilot;
	}
	public String getAutoPilotCron() {
		return autoPilotCron;
	}
	public void setAutoPilotCron(String autoPilotCron) {
		this.autoPilotCron = autoPilotCron;
	}
	public boolean isEnableAlmReport() {
		return enableAlmReport;
	}
	public void setEnableAlmReport(boolean enableAlmReport) {
		this.enableAlmReport = enableAlmReport;
	}
	public ALMConfig getXalmConfig() {
		return xalmConfig;
	}
	public void setXalmConfig(ALMConfig xalmConfig) {
		this.xalmConfig = xalmConfig;
	}
	public String getToWhomEmail() {
		return toWhomEmail;
	}
	public void setToWhomEmail(String toWhomEmail) {
		this.toWhomEmail = toWhomEmail;
	}
	public boolean isEnableMail() {
		return enableMail;
	}
	public void setEnableMail(boolean enableMail) {
		this.enableMail = enableMail;
	}
	
     
	
}
