package com.app;

import java.awt.Desktop;
import java.io.File;
import java.nio.file.Paths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.app.alm.common.HTMLTemplate;
/***
 * 
 * @author intakhabalam.s@hcl.com
 * Main Application Entry point
 * @see java.lang.Object
 *
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.app")
@ControllerAdvice("com.app.filedog.controller")
public class HCLALMApp {

	private static String[] args;
	
	
	/***
	 * 
	 * @param args as string array
	 * @see SpringApplication 
	   @see ApplicationPidFileWriter
	   @see ConfigurableApplicationContext
	 */
	public static void main(String[] args) {

		HTMLTemplate.startBanner();
		HCLALMApp.args = args;
		ConfigurableApplicationContext application =SpringApplication.run(HCLALMApp.class, args);
		application.addApplicationListener(new ApplicationPidFileWriter());
	}
	
	/***
	 * Re-Starting 
	 * the application
	 * @param ctx as ConfigurableApplicationContext
	 * @see SpringApplication
	 */
	public static void restart(ConfigurableApplicationContext ctx) {
	    // close previous context
		ctx.close();
	    // and build new one
	    SpringApplication.run(HCLALMApp.class, args);

	}
	
	public static void startBanner(){
		try {
			File htmlFile = Paths.get("start.html").toFile();
			if (htmlFile.exists()) {
				Desktop.getDesktop().browse(htmlFile.toURI());
				
			}
		} catch (Exception e) {
		}
	}
	
}

