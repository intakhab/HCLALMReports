package com.app.alm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
/***
 * 
 * @author intakhabalam.s@hcl.com
 * @see Configuration
 * @see PropertySource
 */
@Configuration
@PropertySource("classpath:application.properties")
public class PropertiesConfig {
	
	@Value("${initial.polling.delay}")
	public String initialPollingDelay;
	
	public String ipAddress="";
	
	//public String versionId="16082018-V-0.7";
	//public String versionId="04092018-V-1.1";//Login Major change
	//public String versionId="07092018-V-1.2";//SO Revamp
	//public String versionId="11092018-V-1.3"; //SO
	//public String versionId="14092018-V-1.4"; //FBPay
	//public String versionId="27092018-V-2.1";// Cron/Mail/AutoStart changes
	//public String versionId="10102018-V-2.2"; //Doc added/Documentation
	//public String versionId="01112018-V-2.3"; //BLK Implemented
	public String versionId="017112018-V-2.4"; //ALM Implemented
		
}
