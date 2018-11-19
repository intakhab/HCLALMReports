package com.app.alm.controller;

import java.io.FileNotFoundException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.alm.component.DataLoaderComponent;
import com.app.alm.config.PropertiesConfig;
import com.app.alm.dto.SettingsInfoDto;
import com.app.alm.service.AlertService;
import com.app.alm.service.CommonService;
import com.app.alm.service.SettingsService;
import com.app.alm.service.XMLUtilService;


/***
 * @author intakhabalam.s@hcl.com
 * @see Controller {@link Controller}
 * @see CrossOrigin {@link CrossOrigin}
 * @see AlertService {@link AlertService}
 * @see Environment {@link Environment}
 * @see XMLUtilService {@link XMLUtilService}
 * @see SettingsService {@link SettingsService}
 * @see CommonService {@link CommonService}
 * @see PropertiesConfig {@link PropertiesConfig}
 * @see RequestMapping  {@link RequestMapping}
 * @see ResponseBody {@link ResponseBody}
 */
@Controller
@CrossOrigin
public class ALMController {

	@Autowired
	PropertiesConfig propertiesConfig;
	@Autowired
	CommonService commonService;

	@Autowired
	SettingsService dogConfigService;

	@Autowired
	AlertService alertService;

	@Autowired
	DataLoaderComponent dataLoader;

	@Autowired
	Environment env;

	@Autowired
	XMLUtilService xmlUtilService;

	
	/**
	 * @return {@link String}
	 */
	@RequestMapping("/run")
    @ResponseBody
	public String startedApps() {
		return "OK";
	}
	
	/***
	 * @param model {@link Map}
	 * @return {@link String}
	 */
	@RequestMapping("/")
	public String defaultPage(Map<String, Object> model) {
		model.put("statusList", commonService.getServerStatus());
		return "status";
	}


	/***
	 * Status page
	 * @param model {@link Model}
	 * @return {@link String}
	 */
	@RequestMapping("/status")
	public String statusPage(Map<String, Object> model) {
		model.put("statusList", commonService.getServerStatus());
		return "status";
	}


	/***
	 * This will load all configuration data from DB
	 * @param model {@link Model}
	 * @return {@link String}
	 */

	@RequestMapping("/configfile")
	public String fileConfig(ModelMap model) {
		SettingsInfoDto infoObj;
		try {
			infoObj = dogConfigService.convertObjToDto("1");
			model.addAttribute("infoObj", infoObj);

		} catch (FileNotFoundException | JAXBException e) {
			try {
				infoObj = dogConfigService.convertObjToDto("2");
				model.addAttribute("infoObj", infoObj);

			} catch (Exception fj) {
				model.addAttribute("msg", e.getMessage());
				return "redirect:/errorpage?msg=Settings Configuration file loading error " + fj.getMessage();

			}
		}

		return "configfile";
	}
	
	
	
	
	/***
	 * This method will restore 
	 * the configuration file when db would crashed.
	 * @param model {@link Model}
	 * @return {@link String}
	 */
	@RequestMapping("/restoreconfig")
	public String restoreConfig(ModelMap model) {
		SettingsInfoDto infoObj;
		try {
			commonService.reloaConfigBackup();
			infoObj = dogConfigService.convertObjToDto("1");
			model.addAttribute("infoObj", infoObj);
			model.addAttribute("msg", alertService.sucess("Settings Configuration load successfully"));

		} catch (Exception e) {
			return "redirect:/errorpage?msg=Configuration file loading error " + e.getMessage();

		}
		return "configfile";
	}

	/***
	 * This method will save config information
	 * @param infoObj  {@link SettingsInfoDto} 
	 * @param result  {@link BindingResult} 
	 * @param model {@link Model} 
	 * @return {@link String}
	 */
	@RequestMapping("/saveconfiginfo")
	public String saveConfigInfo(@Valid @ModelAttribute("infoObj") SettingsInfoDto infoObj, BindingResult result,
			ModelMap model) {
		try {
			if (result.hasErrors()) {
				return "redirect:/errorpage?msg=Data entered is not valid";
			}
			String tabId = infoObj.getTabId();
			boolean cronHit=infoObj.isCronHit();
			dogConfigService.saveSettingsInfo(infoObj);
			model.addAttribute("msg", alertService.sucess("Settings Configuration saved successfully"));
			infoObj = dogConfigService.convertObjToDto("1");
			infoObj.setCronHit(cronHit);
			model.addAttribute("infoObj", infoObj);
			model.addAttribute("tabId", tabId);
			model.addAttribute("cronHit",cronHit);

			dataLoader.refreshData(infoObj);

		} catch (Exception e) {
			model.addAttribute("msg", e.getMessage());
			return "redirect:/errorpage?msg=Saving Settings Configuration error " + e.getMessage();
		}
		return "configfile";
	}
	
	
	/***
	 * CronValidator
	 * @param req link {@link HttpServletRequest}
	 * @return {@link Boolean}
	 */
	@RequestMapping("/cronvalidator")
    @ResponseBody
	public boolean cronValidator(HttpServletRequest req) {
		String cronStr=req.getParameter("param");
		if(!CronSequenceGenerator.isValidExpression(cronStr)) {
			return false;
		}
		return true;
	}

}

