package com.app.alm.service;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.app.alm.domain.ALMConfig;
import com.app.alm.domain.MailConfig;
import com.app.alm.domain.SettingsInfo;
import com.app.alm.dto.SettingsInfoDto;
import com.app.alm.dto.UserDto;
/***
 * @author intakhabalam.s@hcl.com
   Database storage business logic class for HCLALM  
 * @see Service
 * @see XMLUtilService {@link XMLUtilService}
 * @see CommonService {@link CommonService}
 * @see Environment {@link Environment}
 *
 */
@Service
public class SettingsService {

	@Autowired
	private XMLUtilService xmlUtilService;
	@Autowired
	private CommonService commonService;
	@Autowired
	Environment env;
	
	/**
	 * @param settingDto {@link SettingsInfoDto}
	 * @return link {@link SettingsInfo}
	 * @throws FileNotFoundException {@link FileNotFoundException}
	 * @throws JAXBException {@link JAXBException}
	 */
	public SettingsInfo saveSettingsInfo(SettingsInfoDto settingDto) throws
	FileNotFoundException, JAXBException {

		SettingsInfo config = convertDtoToObj(settingDto);
		String fileName = env.getProperty("db.location");
		commonService.backupConfigFile(fileName);

		return (SettingsInfo) xmlUtilService.convertObjectToXML(config, env.getProperty("db.location"));
	}

	
	
	
	/***
	 * This method will convert dto to obj
	 * @param settingInfoDto {@link SettingsInfoDto}
	 * @return {@link SettingsInfo}
	 */
	public SettingsInfo convertDtoToObj(SettingsInfoDto settingInfoDto) {
		
		SettingsInfo settingInfo = new SettingsInfo();
		//
		settingInfo.setEnableMail(settingInfoDto.isEnableMail());
		settingInfo.setToWhomEmail(settingInfoDto.getToWhomEmail());

		MailConfig mc=new MailConfig();
		mc.setHost(settingInfoDto.getHost());
		mc.setPort(settingInfoDto.getPort());
		mc.setUsername(settingInfoDto.getMailUserName());
		mc.setPassword(settingInfoDto.getMailPassword());
		mc.setDebugMail(settingInfoDto.isDebugMail());
		settingInfo.setXmailConfig(mc);
       //
		settingInfo.setAutoPilot(settingInfoDto.isAutoPilot());
		settingInfo.setAutoPilotCron(settingInfoDto.getAutoPilotCron());
		//
		settingInfo.setEnableAlmReport(settingInfoDto.isEnableAlmReport());
		ALMConfig almConfig=new ALMConfig();
		almConfig.setAlmConfigG1(settingInfoDto.getAlmConfigG1());
		almConfig.setAlmConfigG2(settingInfoDto.getAlmConfigG2());
		almConfig.setAlmDomain(settingInfoDto.getAlmDomain());
		almConfig.setAlmProject(settingInfoDto.getAlmProject());

		almConfig.setAlmHost(settingInfoDto.getAlmHost());
		almConfig.setAlmPort(settingInfoDto.getAlmPort());
		almConfig.setAlmUserName(settingInfoDto.getAlmUserName());
		almConfig.setAlmPassword(settingInfoDto.getAlmPassword());
		almConfig.setAlmPageSize(settingInfoDto.getAlmPageSize());
		settingInfo.setXalmConfig(almConfig);
		return settingInfo;
	}
	
	
	/***
	 * This method will convert Obj to Dto
	 * @param location {@link String}
	 * @return {@link SettingsInfoDto}
	 * @throws FileNotFoundException  {@link FileNotFoundException}
	 * @throws JAXBException  {@link JAXBException}
	 */
	public SettingsInfoDto convertObjToDto(String location) throws FileNotFoundException, JAXBException {
			String dbLocation;
			if("1".equals(location)) {
				dbLocation=env.getProperty("db.location");
			}else {
				dbLocation=env.getProperty("backup.dir")+"/"+"config.db";
			}
			
			SettingsInfo  wdInfo=xmlUtilService.convertXMLToObject(SettingsInfo.class, Paths.get(dbLocation).toFile());
			if(wdInfo==null) {
				return new SettingsInfoDto();
			}
		
		    SettingsInfoDto sInfoDto=new SettingsInfoDto();
			sInfoDto.setEnableMail(wdInfo.isEnableMail());
			sInfoDto.setToWhomEmail(wdInfo.getToWhomEmail());

			
			//if(wdInfo.getXmailConfig()!=null) { //Revised logic for error prevention
     		//Mail
				sInfoDto.setHost(wdInfo.getXmailConfig().getHost().isEmpty() ? sInfoDto.getHost()
						: wdInfo.getXmailConfig().getHost());
				sInfoDto.setPort(
						wdInfo.getXmailConfig().getPort() == 0 ? sInfoDto.getPort() : wdInfo.getXmailConfig().getPort());
				sInfoDto.setMailUserName(wdInfo.getXmailConfig().getUsername().isEmpty() ? sInfoDto.getMailUserName()
						: wdInfo.getXmailConfig().getUsername());
				sInfoDto.setMailPassword(wdInfo.getXmailConfig().getPassword().isEmpty() ? sInfoDto.getMailPassword()
						: wdInfo.getXmailConfig().getPassword());
				sInfoDto.setDebugMail(wdInfo.getXmailConfig().isDebugMail());
	    	//}
			//Pilot
			sInfoDto.setAutoPilot(wdInfo.isAutoPilot());
	        sInfoDto.setAutoPilotCron(wdInfo.getAutoPilotCron().isEmpty()?sInfoDto.getAutoPilotCron():wdInfo.getAutoPilotCron());
	        //ALM
		     sInfoDto.setEnableAlmReport(wdInfo.isEnableAlmReport());//
		        sInfoDto.setAlmConfigG1(wdInfo.getXalmConfig().getAlmConfigG1().isEmpty()
		        		?sInfoDto.getAlmConfigG1():wdInfo.getXalmConfig().getAlmConfigG1());
		        sInfoDto.setAlmConfigG2(wdInfo.getXalmConfig().getAlmConfigG2().isEmpty()
		        		?sInfoDto.getAlmConfigG2():wdInfo.getXalmConfig().getAlmConfigG2());
		        
		        sInfoDto.setAlmDomain(wdInfo.getXalmConfig().getAlmDomain().isEmpty()
		        		?sInfoDto.getAlmDomain():wdInfo.getXalmConfig().getAlmDomain());
		        
		        sInfoDto.setAlmProject(wdInfo.getXalmConfig().getAlmProject().isEmpty()
		        		?sInfoDto.getAlmProject():wdInfo.getXalmConfig().getAlmProject());
		        
		        sInfoDto.setAlmHost(wdInfo.getXalmConfig().getAlmHost().isEmpty()
		        		?sInfoDto.getAlmHost():wdInfo.getXalmConfig().getAlmHost());
		        sInfoDto.setAlmPort(wdInfo.getXalmConfig().getAlmPort().isEmpty()
		        		?sInfoDto.getAlmPort():wdInfo.getXalmConfig().getAlmPort());
		        sInfoDto.setAlmUserName(wdInfo.getXalmConfig().getAlmUserName().isEmpty()
		        		?sInfoDto.getAlmUserName():wdInfo.getXalmConfig().getAlmUserName());
		        sInfoDto.setAlmPassword(wdInfo.getXalmConfig().getAlmPassword().isEmpty()
		        		?sInfoDto.getAlmPassword():wdInfo.getXalmConfig().getAlmPassword());
		        sInfoDto.setAlmPageSize(wdInfo.getXalmConfig().getAlmPageSize().isEmpty()
		        		?sInfoDto.getAlmPageSize():wdInfo.getXalmConfig().getAlmPageSize());

		return sInfoDto;
	}



	/**
	 * This method will save user
	 * @param userDto {@link UserDto} 
	 * @return {@link UserDto}
	 * @throws FileNotFoundException  {@link FileNotFoundException}
	 * @throws JAXBException  {@link JAXBException}
	 */
	public UserDto saveUserInfo(UserDto userDto) throws FileNotFoundException, JAXBException {
		userDto.setCreateDate(commonService.currentTime());
		commonService.backupUserFile();
		commonService.createRegisterUsers(userDto);
		return userDto ;
	}
	
}

