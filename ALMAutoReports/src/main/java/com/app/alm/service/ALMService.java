
package com.app.alm.service;

import java.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.alm.component.ALMComponent;
import com.app.alm.component.DataLoaderComponent;
import com.app.alm.config.ALMUtils;
import com.app.alm.domain.Entities;
import com.app.alm.dto.ALMConfigDto;
/***
 * 
 * @author intakhabalam.s@hcl.com
 * @see Service
 * @See Client
 * @See {@link ALMComponent}
 * @See {@link ALMConfigDto}
 * @See {@link ALMReports}
 *
 */
@Service
public class ALMService {
	
		private Logger logger = LogManager.getLogger("ALM-Service");

	    
	    private static final String isAuthenticatedPath = "authentication-point/authenticate";
	    private static final String qcSiteSession = "rest/site-session";
	    private static final String logoutPath = "authentication-point/logout";
	    private static String lswoocookie;
	    private static String qcsessioncookie;

	    public static Client client;
	    public static WebTarget target;
	    public static Invocation.Builder invocationBuilder;
	    public static Response res;
	    
	    @Autowired
	    private ALMReports almReports;
	    
		@Autowired
		private DataLoaderComponent dataLoader;

		
		/***
		 * @return {@link String}
		 */
		private String getALMURL() {
			String urlStr="http://"+dataLoader.configDto.getAlmHost()+":"+dataLoader.configDto.getAlmPort()+"/qcbin";
			return urlStr;
		}


        /***
         * 
         * @return {@link String}
         */
	    private String getEncodedAuthString() {
	        String auth = dataLoader.configDto.getAlmUserName() + ":" + dataLoader.configDto.getAlmPassword();
	        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
	        String authHeader = "Basic " + new String(encodedAuth);
	        return authHeader;
	    }
	    
	    
	    /**
	     * @throws Exception *
	     * 
	     */
	    public void  generateALMReport() throws Exception {
	    	
	    	  client = ClientBuilder.newBuilder().build();
	          /* Get LWSSO Cookie */
	          target = client.target(getALMURL()).path(
	                  isAuthenticatedPath);
	          invocationBuilder = target.request(new String[] { "application/xml" });
	          invocationBuilder.header("Authorization", getEncodedAuthString());
	          res = invocationBuilder.get();
	          lswoocookie = res.getCookies().get("LWSSO_COOKIE_KEY").getValue();

	          /* Get QCSession Cookie */
	          target = client.target(getALMURL()).path(qcSiteSession);
	          invocationBuilder = target
	                  .request();
	          invocationBuilder.cookie("LWSSO_COOKIE_KEY", lswoocookie);
	          res = invocationBuilder.post(null);
	          qcsessioncookie = res.getCookies().get("QCSession").getValue();

	          /* Get the first defect */
	          String midPoint = "rest/domains/" + dataLoader.configDto.getAlmDomain() + "/projects/" + dataLoader.configDto.getAlmProject();
	          target = client.target(getALMURL()).path(midPoint).path("defects").queryParam("page-size", dataLoader.configDto.getAlmPageSize());
	          invocationBuilder = target
	                  .request(new String[] { "application/xml" });
	          invocationBuilder.cookie("LWSSO_COOKIE_KEY", lswoocookie);
	          invocationBuilder.cookie("QCSession", qcsessioncookie);
	          res = invocationBuilder.get();

	          logger.info("Cheking Response-->"+res.toString());
	          
	          String responseAsString = res.readEntity(String.class);

	       /* System.out.println( responseAsString);
	  		  String postedEntityReturnedXml = res.toString();*/
	          
	  	     Entities entities =  ALMUtils.marshal(Entities.class,
	  				responseAsString);
	  		 //Calling Reports for creation
	  	     almReports.createReport(entities);
	          /* Logout */
	          target = client.target(getALMURL()).path(logoutPath);
	          invocationBuilder = target
	                  .request();
	          invocationBuilder.cookie("LWSSO_COOKIE_KEY", lswoocookie);
	          invocationBuilder.cookie("QCSession", qcsessioncookie);
	          res = invocationBuilder.post(null);
	    }
}
