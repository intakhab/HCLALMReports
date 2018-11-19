package com.app.alm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.app.alm.common.EmailTemplate;
import com.app.alm.component.DataLoaderComponent;
import com.app.alm.config.PropertiesConfig;
import com.app.alm.dto.MailDto;
/***
 * Common Mail Services for HCLALM  
 * @author intakhabalam.s@hcl.com
 * Provide the supports of mail
 * @see Service 
 * @see Environment
 * @see PropertiesConfig {@link PropertiesConfig}
 * @see EmailService {@link EmailService}
 */
@Service
public class CommonMailService{

	
	private final Logger logger = LogManager.getLogger("Dog-CM");
	@Autowired
	Environment env;
	@Autowired
	PropertiesConfig propertiesConfig;
	@Autowired
	DataLoaderComponent dataLoader;
	
	@Autowired
	EmailService emailService;
	/**
	 * @param mailDto {@link MailDto}
	 * @throws Exception {@link Exception}
	 */
	public void sendEmailTemplate(MailDto mailDto) throws Exception {
		logger.info("Email sending.......");
		EmailTemplate template = new EmailTemplate("email-template.html");

		Map<String, String> replacements = new HashMap<String, String>();
		replacements.put("body", mailDto.getMessage());
		String tdy=String.valueOf(new Date())+"-"+propertiesConfig.ipAddress;
		replacements.put("today", tdy);
		String message = template.getTemplate(replacements);
		mailDto.setMessage(message);
		mailDto.setHtml(true);
		emailService.send(mailDto);
		logger.info("Email sent.......");

	}
	
	
  }
