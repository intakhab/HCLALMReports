package com.app.alm.service;


import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.alm.component.DataLoaderComponent;
import com.app.alm.domain.Entities;
import com.app.alm.domain.Entity;
import com.app.alm.domain.Entity.Fields.Field;
import com.app.alm.dto.ALMReportsDto;
import com.app.alm.dto.MailDto;
/***
 * 
 * @author Intakhabalam.s@hcl.com
 * @see RestALM
 *
 */
@Service
public class ALMReports {
	  private Logger logger = LogManager.getLogger("ALM-Reports");

		private static final String STATUS = "status";
		private static final String NEW = "[New]";
		private static final String OPEN = "[Open]";
		private static final String IN_PROGRESS="[In Progress]";
		private static final String FIXED = "[Fixed]";
		private static final String RE_OPEN = "[Reopen]";
		private static final String READY_TEST = "[Ready to re-Test]";
		private static final String WMS = "WMS";
		private static final String TMS = "TMS";
		private static final String SOA = "SOA";
		private static final String JDE = "JDE";
		private static final String EDI = "EDI";
		@Autowired
		private DataLoaderComponent dataLoader;
		
		@Autowired
		private EmailService emailService;

		/**
		 * 
		 * @param entities
		 * @See {@link Entities}
		 */
		public  void createReport(Entities entities)  throws Exception{
			
			logger.info("Starting report creating from HP ALM... ");
			 int count=1;
	         int repoSize=entities.getEntity().length;
	         Map<Integer,List<ALMReportsDto>> repoMap=new HashMap<>();
	          
	         logger.info("Total Reports Size : "+repoSize);
	            
			for (Entity entity : entities.getEntity()) {
				List<Field> fields = entity.getFields().getField();

				List<ALMReportsDto> reportList=new ArrayList<>();
				for (Field field : fields) {
						//System.out.println(field.getName() + " : " + field.getValue());
					  switch(field.getName().toString()) {
					       case "id" :
							reportList.add(new ALMReportsDto(count,field.getName(),field.getValue().toString()));

				        	break;	
					        case "status":	
								reportList.add(new ALMReportsDto(count,field.getName(),field.getValue().toString()));

					        	break;
					        case "severity":					
								reportList.add(new ALMReportsDto(count,field.getName(),field.getValue().toString()));
					        	break;	
					        	
					        case "description":					
					        	String desc=field.getValue().toString().replaceAll("<html><body>", "")
					        	.replaceAll("</html></body>", "").replaceAll("<html>","").replaceAll("</html>","").replaceAll("<body>", "").replaceAll("</body>","");
								reportList.add(new ALMReportsDto(count,field.getName(),desc));
					        	break;	
					       /* case "priority":					
								reportList.add(new ReportsEntity(count,field.getName(),field.getValue().toString()));
					        	break;*/
					        case "owner":					
								reportList.add(new ALMReportsDto(count,field.getName(),field.getValue().toString()));
					        	break;
					        	
					        case "user-04":					
								reportList.add(new ALMReportsDto(count,field.getName(),field.getValue().toString()));
					        	break;	
					      /*  case "user-02":					
								reportList.add(new ReportsEntity(count,field.getName(),field.getValue().toString()));
					        	break;	*/
					        case "user-01":					
								reportList.add(new ALMReportsDto(count,field.getName(),field.getValue().toString()));
					        	break;		
					  }
				}
				
				repoMap.put(count,reportList);
				count++;
			}
			/////////////////////////////////////
			int totalCount=0;
	        List<Integer> notCloseList=new ArrayList<>();
	     	Set<String> statusList=new HashSet<>();
			for(Map.Entry<Integer,List<ALMReportsDto>> entry:repoMap.entrySet()) {
				
				List<ALMReportsDto> repList=entry.getValue();
				for(ALMReportsDto re:repList) {
					if(re.getEntityName().equals(STATUS) 
							&& !re.getEntityValue().equals("[Closed]") 
							&& !re.getEntityValue().equals("[Rejected]")
							&& !re.getEntityValue().equals(READY_TEST)
							) {
						
						notCloseList.add(re.getId());
						statusList.add(re.getEntityValue());
						totalCount++;
					}
				}
				
			}
		   /**
		   	for(String s:statusList) {
				System.out.println(s);
			}
			*/
			 StringBuilder sbTable=new StringBuilder("<br/><br/><table border='1' class='tt'>");
	         sbTable.append("<tr bgcolor='#606d9a'><td width='60%'>#Description</td><td>#Status</td><td>#Owner</td><td>#Component</td><td>#Problem Type</td><td>#ID</td><td>#Severity</td></tr>");
	     
			for(Integer in:notCloseList) {
				List<ALMReportsDto> finalRepor=repoMap.get(in);
				sbTable.append("<tr>");
				for(ALMReportsDto re:finalRepor) {
					if(re.getEntityValue().equals("[Critical]") && re.getEntityName().equals("severity")) {
					       sbTable.append("<td bgcolor='#e50000'>").append(re.getEntityValue().replace("[","").replace("]", "")).append("</td>");
					}
					else if(re.getEntityValue().equals("[Major]")  && re.getEntityName().equals("severity")) {
						  sbTable.append("<td bgcolor='#f97306'>").append(re.getEntityValue().replace("[","").replace("]", "")).append("</td>");
					}else if(re.getEntityValue().equals("[Medium]")  && re.getEntityName().equals("severity")) {
						  sbTable.append("<td bgcolor='#ffb07c'>").append(re.getEntityValue().replace("[","").replace("]", "")).append("</td>");
					}else {
						  sbTable.append("<td>").append(re.getEntityValue().replace("[","").replace("]", "")).append("</td>");
					}
				}
				sbTable.append("</tr>");
			}
			sbTable.append("</table>");
			// //
	        int wmsCount=0;List<Integer> wmsList=new ArrayList<>(0);
	        int tmsCount=0;List<Integer> tmsList=new ArrayList<>(0);
	        int jdeCount=0;List<Integer> jdeList=new ArrayList<>(0);
	        int ediCount=0;List<Integer> ediList=new ArrayList<>(0);
	        int soaCount=0;List<Integer> soaList=new ArrayList<>(0);
			
			for (Integer in : notCloseList) {
				List<ALMReportsDto> finalRepor = repoMap.get(in);
				for (ALMReportsDto re : finalRepor) {
					// System.out.println(re.getEntityName() +"----"+re.getEntityValue());
					if (re.getEntityName().equals("user-04")) {
	
						String[] s = re.getEntityValue().split(",");
						for (int i = 0; i < s.length; i++) {
							String val = s[i].replaceAll("\\[", "").replaceAll("\\]", "").trim();
							if (val.equals(WMS)) {
								wmsCount++;
								wmsList.add(re.getId());
							}
							if (val.equals(TMS)) {
								tmsCount++;
								tmsList.add(re.getId());
	
							}
							if (val.equals(SOA)) {
								soaCount++;
								soaList.add(re.getId());
	
							}
							if (val.equals(JDE)) {
								jdeCount++;
								jdeList.add(re.getId());
	
							}
							if (val.equals(EDI)) {
								ediCount++;
								ediList.add(re.getId());
	
							}
	
						}
	
					}
	
				}
	
			}
			
	         logger.info("Total open count-"+totalCount);
	         logger.info("Total soa-"+soaCount);
	         logger.info("Total tms-"+tmsCount);
	         logger.info("Total wms-"+wmsCount);
	         logger.info("Total jde-"+jdeCount);
	         logger.info("Total edi-"+ediCount);

			
			 int wms_inprogressCount=0;
			 int wms_fixedCount=0;
			 int wms_openCount=0;
			 int wms_newCount=0;
			 int wms_reopenCount=0;
			// int wms_readyTestCount=0;
			 
			       
			 int soa_inprogressCount=0;
			 int soa_fixedCount=0;
			 int soa_openCount=0;
			 int soa_newCount=0;
			 int soa_reopenCount=0;
			 //int soa_readyTestCount=0;
			 
			 int tms_inprogressCount=0;
			 int tms_fixedCount=0;
			 int tms_openCount=0;
			 int tms_newCount=0;
			 int tms_reopenCount=0;
			 //int tms_readyTestCount=0;
			 
			 int jde_inprogressCount=0;
			 int jde_fixedCount=0;
			 int jde_openCount=0;
			 int jde_newCount=0;
			 int jde_reopenCount=0;
			 //int jde_readyTestCount=0;
			 
			 int edi_inprogressCount=0;
			 int edi_fixedCount=0;
			 int edi_openCount=0;
			 int edi_newCount=0;
			 int edi_reopenCount=0;
			 //int edi_readyTestCount=0;
			 
			 
			for(Integer in:wmsList) {
				List<ALMReportsDto> finalRepor=repoMap.get(in);
				for(ALMReportsDto re:finalRepor) {
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(NEW)) {
						wms_newCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(OPEN)) {
						wms_openCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(IN_PROGRESS)) {
						wms_inprogressCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(FIXED)) {
						wms_fixedCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(RE_OPEN)) {
						wms_reopenCount++;
					}
					/*if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(READY_TEST)) {
						wms_readyTestCount++;
					}*/
					
				}
			}
			for(Integer in:soaList) {
				List<ALMReportsDto> finalRepor=repoMap.get(in);
				for(ALMReportsDto re:finalRepor) {
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(NEW)) {
						soa_newCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(OPEN)) {
						soa_openCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(IN_PROGRESS)) {
						soa_inprogressCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(FIXED)) {
						soa_fixedCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(RE_OPEN)) {
						soa_reopenCount++;
					}
					/*if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(READY_TEST)) {
						soa_readyTestCount++;
					}*/
				}
			}
			for(Integer in:tmsList) {
				List<ALMReportsDto> finalRepor=repoMap.get(in);
				for(ALMReportsDto re:finalRepor) {
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(NEW)) {
						tms_newCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(OPEN)) {
						tms_openCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(IN_PROGRESS)) {
						tms_inprogressCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(FIXED)) {
						tms_fixedCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(RE_OPEN)) {
						tms_reopenCount++;
					}
					/*if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(READY_TEST)) {
						tms_readyTestCount++;
					}*/
				}
			}
			for(Integer in:jdeList) {
				List<ALMReportsDto> finalRepor=repoMap.get(in);
				for(ALMReportsDto re:finalRepor) {
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(NEW)) {
						jde_newCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(OPEN)) {
						jde_openCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(IN_PROGRESS)) {
						jde_inprogressCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(FIXED)) {
						jde_fixedCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(RE_OPEN)) {
						jde_reopenCount++;
					}
					/*if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(READY_TEST)) {
						jde_readyTestCount++;
					}*/
				}
			}
			for(Integer in:ediList) {
				List<ALMReportsDto> finalRepor=repoMap.get(in);
				for(ALMReportsDto re:finalRepor) {
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(NEW)) {
						edi_newCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(OPEN)) {
						edi_openCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(IN_PROGRESS)) {
						edi_inprogressCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(FIXED)) {
						edi_fixedCount++;
					}
					if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(RE_OPEN)) {
						edi_reopenCount++;
					}
					/*if(re.getEntityName().equals(STATUS) && re.getEntityValue().equals(READY_TEST)) {
						edi_readyTestCount++;
					}*/
				}
			}
			int totalAllNewCount = soa_newCount + tms_newCount + edi_newCount + wms_newCount + jde_newCount;
			int totalAllOpenCount = soa_openCount + tms_openCount + edi_openCount + wms_openCount + jde_openCount;
			int totalAllInProgressCount = soa_inprogressCount + tms_inprogressCount + edi_inprogressCount
					+ wms_inprogressCount + jde_inprogressCount;
			int totalAllFixedCount = soa_fixedCount + tms_fixedCount + edi_fixedCount + wms_fixedCount + jde_fixedCount;
			int totalAllReopenCount = soa_reopenCount + tms_reopenCount + edi_reopenCount + wms_reopenCount
					+ jde_reopenCount;
			/*int totalAllReadyTestCount = soa_readyTestCount + tms_readyTestCount + edi_readyTestCount + wms_readyTestCount
					+ jde_readyTestCount;*/
			int totalAllCount = soaCount + ediCount + tmsCount + wmsCount + jdeCount;

			StringBuilder sb=new StringBuilder("<table style='float:left;'><tr><td bgcolor=\"#f2f2f2\" style='text-align:center;font-size:15px;font-face:bold;'>Open Defects Summary</td></tr></table>\r\n" + 
					"<br/><br/>"+
					"<table border='1' style='float:left;'>\r\n" + 
					"    <tr>\r\n" + 
					"    <td bgcolor='#f2f2f2'> #Track</td>\r\n" + 
					"	 <td bgcolor='#f2f2f2'>New </td>\r\n" + 
					"	 <td bgcolor='#f2f2f2'>Open </td>\r\n" + 
					"	 <td bgcolor='#f2f2f2'>In Progress</td>\r\n" + 
					"	 <td bgcolor='#f2f2f2'>Fixed </td>\r\n" + 
					"	 <td bgcolor='#f2f2f2'>Reopen </td>\r\n" + 
					/*"	 <td bgcolor='#f2f2f2'>Ready to ReTest </td>\r\n" + */
					"	 <td bgcolor='#f2f2f2'>Total</td>\r\n" + 
					"	</tr>\r\n" + 
					"	 <tr>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>SOA</td>\r\n" + 
					"		 <td bgcolor='#8e82fe'>"+soa_newCount+"</td>\r\n" + 
					"		 <td bgcolor='#f97306'>"+soa_openCount+"</td>\r\n" + 
					"		 <td bgcolor='#ceb301'>"+soa_inprogressCount+"</td>\r\n" + 
					"		 <td bgcolor='#15b01a'>"+soa_fixedCount+"</td>\r\n" + 
					"		 <td bgcolor='#cb416b'>"+soa_reopenCount+"</td>\r\n" + 
					/*"		 <td bgcolor='#9fd0fc'>"+soa_readyTestCount+"</td>\r\n" + */
					"		 <td bgcolor='#f2f2f2'>"+soaCount+"</td>\r\n" + 
					"	</tr>\r\n" + 
					"	 <tr>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>JDE</td>\r\n" + 
					"		 <td bgcolor='#8e82fe'>"+jde_newCount+"</td>\r\n" + 
					"		 <td bgcolor='#f97306'>"+jde_openCount+"</td>\r\n" + 
					"		 <td bgcolor='#ceb301'>"+jde_inprogressCount+"</td>\r\n" + 
					"		 <td bgcolor='#15b01a'>"+jde_fixedCount+"</td>\r\n" + 
					"		 <td bgcolor='#cb416b'>"+jde_reopenCount+"</td>\r\n" + 
					/*"		 <td bgcolor='#9fd0fc'>"+jde_readyTestCount+"</td>\r\n" + */
					"		 <td bgcolor='#f2f2f2'>"+jdeCount+"</td>\r\n" + 
					"	</tr>\r\n" + 
					"	<tr>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>TMS</td>\r\n" + 
					"		 <td bgcolor='#8e82fe'>"+tms_newCount+"</td>\r\n" + 
					"		 <td bgcolor='#f97306'>"+tms_openCount+"</td>\r\n" + 
					"		 <td bgcolor='#ceb301'>"+tms_inprogressCount+"</td>\r\n" + 
					"		 <td bgcolor='#15b01a'>"+tms_fixedCount+"</td>\r\n" + 
					"		 <td bgcolor='#cb416b'>"+tms_reopenCount+"</td>\r\n" + 
					/*"		 <td bgcolor='#9fd0fc'>"+tms_readyTestCount+"</td>\r\n" + */
					"		 <td bgcolor='#f2f2f2'>"+tmsCount+"</td>\r\n" + 
					"	</tr>\r\n" + 
					"	<tr>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>WMS</td>\r\n" + 
					"		 <td bgcolor='#8e82fe'>"+wms_newCount+"</td>\r\n" + 
					"		 <td bgcolor='#f97306'>"+wms_openCount+"</td>\r\n" + 
					"		 <td bgcolor='#ceb301'>"+wms_inprogressCount+"</td>\r\n" + 
					"		 <td bgcolor='#15b01a'>"+wms_fixedCount+"</td>\r\n" + 
					"		 <td bgcolor='#cb416b'>"+wms_reopenCount+"</td>\r\n" + 
					/*"		 <td bgcolor='#9fd0fc'>"+wms_readyTestCount+"</td>\r\n" + */
					"		 <td bgcolor='#f2f2f2'>"+wmsCount+"</td>\r\n" + 
					"	</tr>\r\n" + 
					"	 <tr>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>EDI</td>\r\n" + 
					"		 <td bgcolor='#8e82fe'>"+edi_newCount+"</td>\r\n" + 
					"		 <td bgcolor='#f97306'>"+edi_openCount+"</td>\r\n" + 
					"		 <td bgcolor='#ceb301'>"+edi_inprogressCount+"</td>\r\n" + 
					"		 <td bgcolor='#15b01a'>"+edi_fixedCount+"</td>\r\n" + 
					"		 <td bgcolor='#cb416b'>"+edi_reopenCount+"</td>\r\n" + 
					/*"		 <td bgcolor='#9fd0fc'>"+edi_readyTestCount+"</td>\r\n" + */
					"		 <td bgcolor='#f2f2f2'>"+ediCount+"</td>\r\n" + 
					"	</tr>\r\n" + 
					"	 <tr>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>Total</td>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>"+totalAllNewCount+"</td>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>"+totalAllOpenCount+"</td>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>"+totalAllInProgressCount+"</td>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>"+totalAllFixedCount+"</td>\r\n" + 
					"		 <td bgcolor='#f2f2f2'>"+totalAllReopenCount+"</td>\r\n" + 
					/*"		 <td bgcolor='#f2f2f2'>"+totalAllReadyTestCount+"</td>\r\n" + */
					"		 <td bgcolor='#f2f2f2'>"+totalAllCount+"</td>\r\n" + 
					"	</tr>\r\n" + 
					"</table>\r\n" +
					"\r\n" + 
					"<div id=\"piechart\" style=\"float:left;\"></div>\r\n" + 
					"<div id=\"piechart2\" style=\"float:left;\"></div>\r\n<br/>"+
					sbTable.toString()+"\r\r\n"+
					"<script type=\"text/javascript\">\r\n" + 
					"// Load google charts\r\n" + 
					"google.charts.load('current', {'packages':['corechart']});\r\n" + 
					"google.charts.setOnLoadCallback(drawChart);\r\n" + 
					"\r\n" + 
					"// Draw the chart and set the chart values\r\n" + 
					"function drawChart() {\r\n" + 
					"  var data = google.visualization.arrayToDataTable([\r\n" + 
					"  ['Task', 'Status'],\r\n" + 
					"  ['New', "+totalAllNewCount+"],\r\n" + 
					"  ['Open', "+totalAllOpenCount+"],\r\n" + 
					"  ['In Progress', "+totalAllInProgressCount+"],\r\n" + 
					"  ['Fixed', "+totalAllFixedCount+"],\r\n" + 
					"  ['Reopen', "+totalAllReopenCount+"]\r\n" + 	
					/*"  ['Ready to ReTest', "+totalAllReadyTestCount+"]\r\n" +*/
					"]);\r\n" + 
					"\r\n" + 
					"  // Optional; add a title and set the width and height of the chart\r\n" + 
					"  var options = {'title':'Open Defects by  Status', 'width':400, 'height':300};\r\n" + 
					"\r\n" + 
					"  // Display the chart inside the <div> element with id=\"piechart\"\r\n" + 
					"  var chart = new google.visualization.PieChart(document.getElementById('piechart'));\r\n" + 
					"  chart.draw(data, options);\r\n" + 
					"}\r\n" + 
					"\r\n" + 
					"google.charts.load('current', {'packages':['corechart']});\r\n" + 
					"google.charts.setOnLoadCallback(drawChart1);\r\n" + 
					"\r\n" + 
					"function drawChart1() {\r\n" + 
					"  var data1 = google.visualization.arrayToDataTable([\r\n" + 
					"  ['Task', 'Status'],\r\n" + 
					"  ['SOA', "+soaCount+"],\r\n" + 
					"  ['JDE', "+jdeCount+"],\r\n" + 
					"  ['TMS', "+tmsCount+"],\r\n" + 
					"  ['WMS', "+wmsCount+"],\r\n" + 
					"  ['EDI', "+ediCount+"]\r\n" + 
					"]);\r\n" + 
					"\r\n" + 
					"  // Optional; add a title and set the width and height of the chart\r\n" + 
					"  var options1 = {'title':'Open Defects by2 Status', 'width':400, 'height':300};\r\n" + 
					"\r\n" + 
					"  // Display the chart inside the <div> element with id=\"piechart\"\r\n" + 
					"  var chart1 = new google.visualization.PieChart(document.getElementById('piechart2'));\r\n" + 
					"  chart1.draw(data1, options1);\r\n" + 
					"}\r\n" + 
					"</script>");
			
			      // writeFile(sb.toString(),"C:/@workspace/reports/repo.html");
			       
			       String subj="ALM Reports-Open Defects Summary";
			       MailDto mailDto = new MailDto(dataLoader.configDto.getFromMail(), 
							dataLoader.configDto.getToWhomEmail(), subj, sb.toString());
			       logger.info("ALM sending email.........."+dataLoader.configDto.getToWhomEmail());
					mailDto.setHtml(true);
					emailService.send(mailDto);
                   logger.info("ALM reports successfully created..........");
		}



		/***
		 * Write file
		 * @param data {@link String}
		 * @param filePath {@link String}
		 */
		public static void writeFile(String data, String filePath) {
			Path filepath = Paths.get(filePath);
			byte[] bytes = data.getBytes();
			try (OutputStream out = Files.newOutputStream(filepath)) {
				out.write(bytes);
			} catch (Exception e) {
	               e.fillInStackTrace();
			}
		}

}

