package com.app;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PreDestroy;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.app.alm.common.HTMLTemplate;
import com.app.alm.component.DataLoaderComponent;
import com.app.alm.dto.MailDto;
import com.app.alm.dto.StatusInfoDto;
import com.app.alm.service.CommonMailService;
import com.app.alm.service.CommonService;
import com.app.alm.service.XMLUtilService;

/***
 * 
 * @author intakhabalam.s@hcl.com
 * @see ApplicationRunner
 * @see Component
 * @see Repository
 * @see Service
 * @see Controller
 */
@Component
public class AppCleanUP implements ApplicationRunner {

	private Logger logger = LogManager.getLogger("Dog-CUP");
	@Autowired
	Scheduler scheduler;
	@Autowired
	CommonMailService commonMailService;
	@Autowired
	Environment env;
	@Autowired
	DataLoaderComponent dataLoader;
	@Autowired
	CommonService commonService;
	@Autowired
	XMLUtilService xmlUtilService;

	/*****
	 * 
	 */

	@Override
	public void run(ApplicationArguments args) {
		
		//Init
		commonService.getServerStatus();
		commonService.STARTED_TIME=commonService.currentTime();
		checkUserStatus();
		
			if (dataLoader.configDto.isEnableStartupEmail()) {
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						String message = env.getProperty("watchdog.server.up.body");
						String subject = env.getProperty("watchdog.server.up");
						try {
							
							MailDto mailDto = new MailDto(dataLoader.configDto.getFromMail(), 
									dataLoader.configDto.getToWhomEmail(), subject, message);
							commonMailService.sendEmailTemplate(mailDto);
						} catch (Exception e) {
							logger.error("Exception {run} " + e.getMessage());
						}
					}
				}).start();
			} else {
				logger.info("Startup mail sender is off {} ");

			}
		
		
	}

	/**
	 * Cleanup/Mail shoot
	 */
	@PreDestroy
	private void onClose() {
			if (dataLoader.configDto.isEnableShutdownEmail()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						String message = env.getProperty("watchdog.server.down.body");
						String subject = env.getProperty("watchdog.server.down");
						try {
							
							MailDto mailDto = new MailDto(dataLoader.configDto.getFromMail(), 
									dataLoader.configDto.getToWhomEmail(), subject, message);
							
							commonMailService.sendEmailTemplate(mailDto);
						} catch (Exception e) {
							logger.error("Exception {onClose} " + e.getMessage());

						}
					}
				}).start();
			} else {
				logger.info("Startup mail sender is off {} ");

			}

		logger.info("Before closing called System GC.");
		commonService.deleteStatus();
		
		System.gc();

		try {
			scheduler.clear();
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.error("SchedulerException {onClose} " + e.getMessage());

		}

		logger.info("Happy to safe closing with ( CTR + C ).");
		logger.info("\nSometimes it took serveral minutes to close as it has been running since long time.");
		logger.info("Be Patience... Resources Cleanup Running \n");
		logger.info(commonService.CLOSE_BANNER);
	}

	/***
	 * 
	 */
	private void checkUserStatus() {
		logger.info("Checking server status");
		try {
			StatusInfoDto dog = (StatusInfoDto) xmlUtilService.convertXMLToObject(StatusInfoDto.class,
					Paths.get("status.db").toFile());
			if (dog != null && dog.getPort() != null) {
				if (!dog.getPort().equals(env.getProperty("server.port"))) {
					logger.info("WatchDog application Port has been changed {} ");
					commonService.writeStartFileForcely("localhost");
				}else {
					 startBanner();

				}
			}
		} catch (FileNotFoundException e) {
			logger.error("Error: checking status {} ", e.getMessage());

		} catch (JAXBException e) {
			logger.error("Error: Parsing problem {} ", e.getMessage());
		}
	}
	  void startBanner(){
			try {
				File htmlFile = Paths.get("start.html").toFile();
				if (htmlFile.exists()) {
					Desktop.getDesktop().browse(htmlFile.toURI());
				}else {
					commonService.writeStartFileForcely("localhost");
				}
			} catch (Exception e) {
			}
		}
		

	
	/***
	 * @param hostname {@link String}
	 */
	public void writeStartFileForcely(String hostname) {
		String newData = HTMLTemplate.getStartTemplate(hostname, env.getProperty("server.port"));
		File htmlFile = Paths.get("start.html").toFile();
		if (htmlFile.exists()) {
			FileUtils.deleteQuietly(htmlFile);

		}
		writeFile(newData, htmlFile.getPath());
	}
	
	/***
	 * Write file
	 * @param data {@link String}
	 * @param filePath {@link String}
	 */
	public void writeFile(String data, String filePath) {
		Path filepath = Paths.get(filePath);
		byte[] bytes = data.getBytes();
		try (OutputStream out = Files.newOutputStream(filepath)) {
			out.write(bytes);
		} catch (Exception e) {
			logger.error("Error: {Com-0015} during writing file {}  ", e);

		}
	}
}
