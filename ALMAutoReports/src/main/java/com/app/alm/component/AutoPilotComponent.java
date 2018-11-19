package com.app.alm.component;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.HCLALMApp;
import com.app.alm.dto.MailDto;
import com.app.alm.service.CommonMailService;
import com.app.alm.service.CommonService;

/***
 * @author intakhabalam.s@hcl.com
 * @see ApplicationContext {@link ApplicationContext} 
 * @see Component
 * @see DataLoaderComponent {@link AutoPilotComponent} 
 * @see CommonMailService {@link CommonMailService}
 * @see CommonService {@link CommonService}
 * @see Environment {@link Environment}
 */
@Component
public class AutoPilotComponent {
	private Logger logger = LogManager.getLogger("Dog-Auto-Pilot");

	@Autowired
	DataLoaderComponent dataLoader;

	@Autowired
	CommonMailService commonMailService;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	Environment env;


	@Bean
	public String autoPilotCron() {
		return dataLoader.configDto.getAutoPilotCron();
	}

	/***
	 * Auto
	 */
	@Scheduled(cron = "#{@autoPilotCron}")
	public void invokeAutoPilotRun() {
		if (validation()) {
			return;
		}
		final long startTime = System.currentTimeMillis();
		logger.info("=======================================================================");
		logger.info("Auto Pilot Starting Time [ " + LocalTime.now() + " ]");
		       //CLeanup and automail
				autoPilotMail();
				   try {
						TimeUnit.SECONDS.sleep(10);
			        } catch (InterruptedException ignored) {
			       }
				//
		try {
			ConfigurableApplicationContext ctx=(ConfigurableApplicationContext) context;
			 Thread restartThread = new Thread(() -> {
			        try {
						TimeUnit.SECONDS.sleep(10);
			        } catch (InterruptedException ignored) {
			        }
					 HCLALMApp.restart(ctx);

			    });
			    restartThread.setDaemon(false);
			    restartThread.start();
			

		} catch (Exception ex) {
			logger.error("Auto Pilot run into an error {HCLALM Exception}", ex);
		}
		final long endTime = System.currentTimeMillis();
		final double totalTimeTaken = (endTime - startTime) / (double) 1000;
		logger.info("Auto Pilot Finishing Time [ " + LocalTime.now() + " ] => Total time taken to be completed  [ "
				+ totalTimeTaken + "s ]");

	}

	
	/***
	 * Auto pilot mail
	 */
	private void autoPilotMail() {
				String subject = "Auto Pilot Starting...";
				String message = "Auto Pilot has started Maintenance Session, Please wait till startup HCLALM";
				try {

					MailDto mailDto = new MailDto(dataLoader.configDto.getFromMail(), 
							dataLoader.configDto.getToWhomEmail(), subject, message);
					commonMailService.sendEmailTemplate(mailDto);
					
				} catch (Exception e) {
					logger.error("Exception {autoPilotMail} " + e.getMessage());

				}
	}
	/**
	 * Check validation
	 * @return {@link Boolean}
	 */
	public boolean validation() {

		if (!dataLoader.configDto.isAutoPilot()) {
			logger.info("Auto Pilot is stoped... For starting reconfigure from settings {} ");
			return true;
		}
		return false;
	}

}
