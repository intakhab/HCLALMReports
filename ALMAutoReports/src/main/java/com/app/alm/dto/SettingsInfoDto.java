package com.app.alm.dto;

/***
 * @author intakhabalam.s@hcl.com
 */
public class SettingsInfoDto {

	
	private boolean enableMail = false;
	private String toWhomEmail = "";
	private String tabId = "fileConfigId1";
	private boolean cronHit = false;
	private String host = "mail.cbrands.com";
	private int port = 25;
	private String mailUserName = "noreply@cbrands.com";
	private String mailPassword = "";
	private String fromMail = "noreply@cbrands.com";
	private boolean debugMail = false;
	private boolean autoPilot = false;
	private String autoPilotCron = "00 00 05 * * *";// Five O'clock Morning default
    private boolean enableAlmReport=false;

	private String almHost = "cbigdc-isapk901.cbi.net";
	private String almPort = "8080";
	private String almUserName = "jchandran";
	private String almPassword = "jchandran";
	private String almDomain = "WINE_OPERATIONS";
	private String almProject = "WMS_TMS_1";
	private String almPageSize = "1000";
	private String almConfigG1 = "00 00 09 * * *"; // 9 O'clcok
	private String almConfigG2 = "00 00 18 * * *"; // 18 O'clcok
	private boolean enableStartupEmail=false;
	private boolean enableShutdownEmail=false;
	
	
	
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
	public boolean isEnableMail() {
		return enableMail;
	}
	public void setEnableMail(boolean enableMail) {
		this.enableMail = enableMail;
	}
	public String getToWhomEmail() {
		return toWhomEmail;
	}
	public void setToWhomEmail(String toWhomEmail) {
		this.toWhomEmail = toWhomEmail;
	}
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public boolean isCronHit() {
		return cronHit;
	}
	public void setCronHit(boolean cronHit) {
		this.cronHit = cronHit;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getMailUserName() {
		return mailUserName;
	}
	public void setMailUserName(String mailUserName) {
		this.mailUserName = mailUserName;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
	public String getFromMail() {
		return fromMail;
	}
	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}
	public boolean isDebugMail() {
		return debugMail;
	}
	public void setDebugMail(boolean debugMail) {
		this.debugMail = debugMail;
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
