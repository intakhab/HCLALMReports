package com.app.alm.component;

import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.app.alm.dto.SettingsInfoDto;
import com.app.alm.service.SettingsService;

/***
 * 
 * @author intakhabalam.s@hcl.com
 * @see Component
 * @see Repository
 * @see Service
 * @see Controller
 * @see Order
 */
@Component
@Order(1)
public class DataLoaderComponent {
	private Logger logger = LogManager.getLogger("Dog-L");

	@Autowired
	private SettingsService HCLALMConfigService;
	public SettingsInfoDto configDto;

	/***
	 * @see PostConstruct
	 */
	@PostConstruct
	public void loadData() {
		try {
			this.configDto = HCLALMConfigService.convertObjToDto("1");
			logger.info("Configuration data loading from DB");
		} catch (FileNotFoundException | JAXBException e) {
			logger.error("Data Loading fail, Loading data from another source {}  " + e.getMessage());
			try {
				this.configDto = HCLALMConfigService.convertObjToDto("2");
				logger.info("Configuration data loading from Temp DB");
			} catch (FileNotFoundException | JAXBException e1) {
				logger.error("Data Loading fail  {} " + e1.getMessage());

			}

		}
	}

	/***
	 * This method will refresh the data once you save the data from UI
	 * 
	 * @param fileDogInfoDto {@link SettingsInfoDto}
	 */
	public void refreshData(SettingsInfoDto fileDogInfoDto) {
		logger.info("Refreshing data, change DB Mode");
		this.configDto = fileDogInfoDto;
	}

	
}
