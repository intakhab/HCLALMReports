package com.app.alm.component;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.alm.service.ALMService;
import com.app.alm.service.CommonMailService;

/***
 * @author intakhabalam.s@hcl.com
 * @see ApplicationContext {@link ApplicationContext}
 * @see Component
 * @see CommonMailService {@link CommonMailService}
 * @see Environment {@link Environment}
 */
@Component
public class ALMComponent {
	private Logger logger = LogManager.getLogger("ALM-Reports");

	@Autowired
	DataLoaderComponent dataLoader;

	@Autowired
	ALMService almService;

	
	@Bean
	public String autoALMCron1() {
		return dataLoader.configDto.getAlmConfigG1();
	}

	@Bean
	public String autoALMCron2() {
		return dataLoader.configDto.getAlmConfigG2();
	}

	/***
	 * Auto
	 */
	@Scheduled(cron = "#{@autoALMCron1}")
	public void invokeAutoPilotRun() {
		if (validation()) {
			return;
		}

		final long startTime = System.currentTimeMillis();
		logger.info("=======================================================================");
		logger.info("ALM Reporting-1 Starting Time [ " + LocalTime.now() + " ]");
		// CLeanup and automail
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException ignored) {
			}
			try {
				almService.generateALMReport();
				
			} catch (Exception e) {
				logger.error("Error at invokeALMRun2 {} ", e.getMessage());
			}
			//

		final long endTime = System.currentTimeMillis();
		final double totalTimeTaken = (endTime - startTime) / (double) 1000;
		logger.info("ALM Reporting-1 Finishing Time [ " + LocalTime.now() + " ] => Total time taken to be completed  [ "
				+ totalTimeTaken + "s ]");

	}

	@Scheduled(cron = "#{@autoALMCron2}")
	public void invokeALMRun1() {
		if (validation()) {
			return;
		}

		final long startTime = System.currentTimeMillis();
		logger.info("=======================================================================");
		logger.info("ALM Reporting-2 Starting Time [ " + LocalTime.now() + " ]");
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException ignored) {
			}
			try {
				almService.generateALMReport();
			} catch (Exception e) {
				logger.error("Error at invokeALMRun2 {} ", e.getMessage());
			}
		//
		final long endTime = System.currentTimeMillis();
		final double totalTimeTaken = (endTime - startTime) / (double) 1000;
		logger.info("ALM Reporting-2 Finishing Time [ " + LocalTime.now() + " ] => Total time taken to be completed  [ "
				+ totalTimeTaken + "s ]");

	}
	
	
	/***
	 * Auto pilot mail
	 */

	/**
	 * Check validation
	 * @return {@link Boolean}
	 */
	public boolean validation() {

		if (!dataLoader.configDto.isEnableAlmReport()) {
			logger.info("ALM Reports is stoped... For starting reconfigure from settings {} ");
			return true;
		}
		return false;
	}

}
