<!DOCTYPE html>
<%@ page errorPage="errorpage.jsp" %>  
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>HCL ALM : Config Page</title>
<jsp:include page="header.jsp"></jsp:include>
</head>
<body>
	<div class="container">
		<div style="padding-left: 150px;">
			<nav aria-label="breadcrumb" style="width: 85%">
				<ol class="breadcrumb">
					    <li><a href="configfile">
					      <i class="fa fa-cog" aria-hidden="true"></i> Settings </a></li>
					    <li class="breadcrumb-item active" aria-current="page">Configuration for ALM Reports</li>
				</ol>
			</nav>
			<div class="col-sm-9">
			    <div>
			      ${msg}
				</div>
				<ul class="nav nav-tabs" id="navId">
				    <li id="fileConfigId1"  class="active"  onclick="chckTab('fileConfigId1')">
				       <a data-toggle="tab" href="#fileConfigId" title="More/Mail Settings">
				       <i class="fa fa-book fa-fw" aria-hidden="true"></i> Mail Config</a>
				    </li>
				    <li id="almConfigId1" onclick="chckTab('almConfigId1')"><a data-toggle="tab" href="#almConfigId" title="HP ALM Settings">
				    <i class="fa fa-book fa-fw" aria-hidden="true"></i> ALM Config</a></li>
			   </ul>
				<form:form name="form" role="form" method="POST" id="configForm"  action="saveconfiginfo" modelAttribute="infoObj">
				<form:hidden path="tabId" id="tabId" />
				<form:hidden path="cronHit" id="cronHit" />
				<input type="hidden" id="tabIds"  value="${tabId}"/>
				 <div class="tab-content">
				       <br>
                  <!-- Start Tab 3 -->
				    <div id="fileConfigId" class="tab-pane active">
				      <div>
					        <nav aria-label="breadcrumb">
								<ol class="breadcrumb">
									<li><a href="javascript:void()"><i class="fa fa-pencil fa-fw" aria-hidden="true"></i> Auto Pilot</a></li>
								</ol>
					    	</nav>
 					     <form:checkbox path="autoPilot" class="form-check-input" id="autoPilotId"/>
 					     <label class="form-check-label" for="autoPilotIdLbl">Enable Auto Pilot</label>
 					
						 <div id="isAutoPilotDiv">
	  							<small id="dd" class="form-text text-muted"><span style="color:blue">Auto Pilot will automatically Restart HCLALM in given time interval</span>(<span style="color:red"> [second, minute, hour, day, month, weekday ex  00 05 21 * * * </span>)</small>	
								<div class="input-group" >
	    					    <span class="input-group-addon">Auto Pilot&nbsp;</span>
	   						    <form:input id="autoPilotCronId" type="text" class="form-control msgc" path="autoPilotCron" placeholder="Additional Info" required="required" />
	  					    </div>
  					     </div>
					       <br/>
					       <br/>
					 </div>
					 <div>
			            <nav aria-label="breadcrumb">
								<ol class="breadcrumb">
									<li><a href="javascript:void()"><i class="fa fa-pencil fa-fw" aria-hidden="true"></i> Mail Configuration </a></li>
								</ol>
				    	</nav>
					  </div>  	
					<div class="form-group">
					   <input type="hidden" id="checkBoxId" value="${infoObj.enableMail}"/>
						<form:checkbox path="enableMail" value="${enableMail}" class="form-check-input"
							 id="emailCheckedId"/>
						<label class="form-check-label" for="emailCheckedId">Enable Email</label>
						<div id="emailCheckedTextAreaId" >
							<div class="input-group">
								<span class="input-group-addon">@</span>
								<form:input type="email" path="toWhomEmail" class="form-control" 
									id="toWhomEmailId"  placeholder="Send Email ias@hcl.com,foo@hcl.com" 
									required="required" aria-describedby="toWhomEmailIns" multiple="multiple" />
							</div>
							<small id="toWhomEmailIns" title="${toWhomEmail}" class="form-text text-muted">
						     <span class="impo">Write email id with comma separator</span> i.e <strong>ias@hcl.com, foo@hcl.com</strong></small>
						    <div class="form-row row">
								<div class="form-group col-md-4">
									<label for="host">Mail Host</label>
									<form:input path="host" 
										class="form-control" id="hostId" placeholder="Email Host"  />
								</div>	
								<div class="form-group col-md-4">
									<label for="host">Mail Port</label>
									<form:input path="port" 
										class="form-control" id="portId" placeholder="Email Port"  />
								</div>	
								<div class="form-group col-md-4">
									<label for="host">Debug Mail</label>
									<form:select path="debugMail" class="form-control" id="debugMailId">
										<form:option value="true">ON</form:option>
										<form:option value="false">OFF</form:option>
	 
								     </form:select>
								</div>		
						   </div>
						   
						   <div class="form-row row">
								<div class="form-group col-md-4">
									<label for="host">Mail Username</label>
									<form:input path="mailUserName" 
										class="form-control" id="mailUserNameId" placeholder="Mail Username" />
								</div>	
								<div class="form-group col-md-4">
									<label for="host">Mail Password</label>
									<form:input  path="mailPassword" 
										class="form-control" id="mailPasswordId" placeholder="Mail Password"  autocomplete="off" />
								</div>	
								<div class="form-group col-md-4">
									<label for="host">Mail from</label>
									<form:input path="fromMail" 
										class="form-control" id="fromMailId" placeholder="Mail From"  />
								</div>	
									
						   </div>
						   <div class="form-row row">
							<div class="form-group col-md-6">
								<label for="enableStartupEmail">Enable Startup Email</label>
								<form:select path="enableStartupEmail" class="form-control"
									id="enableStartupEmail">
									<form:option value="true">ON</form:option>
									<form:option value="false">OFF</form:option>
								</form:select>
							</div>
							<div class="form-group col-md-6">
								<label for="enableShutdownEmail">Enable Shutdown Email</label>
								<form:select path="enableShutdownEmail" class="form-control"
									id="enableShutdownEmail">
									<form:option value="true">ON</form:option>
									<form:option value="false">OFF</form:option>
	
								</form:select>
							</div>
						</div>
								
						</div>
						
					</div>
				
		           <input type="hidden" id="autoPilotTimeId" value="${infoObj.autoPilotCron}"/>
		           <input type="hidden" id="almReportsGroupId1" value="${infoObj.almConfigG1}"/>
		  	   	   <input type="hidden" id="almReportsGroupId2" value="${infoObj.almConfigG2}"/>
	   				           
	  				 <br>
					 <br>
					 <br>
				 </div>
				  <!-- End Tab 3 -->
				  
					 <!-- ALM -->
					 <div id="almConfigId" class="tab-pane">
				      <div>
					        <nav aria-label="breadcrumb">
									<ol class="breadcrumb">
										<li><a href="javascript:void()"><i class="fa fa-pencil fa-fw" aria-hidden="true"></i> ALM Reports</a></li>
									</ol>
					    	</nav>
 					     <form:checkbox path="enableAlmReport" class="form-check-input" id="enableAlmReportId"/>
 					     <label class="form-check-label" for="enabLbl">Enable ALM Report</label>
 					     <p></p>
						 <div id="isAlmDiv">
						 
						 <div class="form-row row">
								<div class="form-group col-md-6">
									<label for="limitFilesFolder">ALM Host</label>
									<form:input path="almHost" 
										class="form-control" id="almHostId" placeholder="ALM Domain" required="required" />
								</div>
								<div class="form-group col-md-6">
									<label for="responseFilePrefix">ALM Port</label>
									<form:input path="almPort" class="form-control" id="almPortId"
										placeholder="ALM Port no" required="required" />
								</div>
					      </div>
					      <div class="form-row row">
								<div class="form-group col-md-6">
									<label for="limitFilesFolder">ALM Username</label>
									<form:input path="almUserName" 
										class="form-control" id="almUserNameId" placeholder="ALM Username" required="required" />
								</div>
								<div class="form-group col-md-6">
									<label for="responseFilePrefix">ALM password</label>
									<form:input path="almPassword" class="form-control" id="almPasswordId"
										placeholder="ALM Password" required="required" />
								</div>
					      </div>
					      
					      <div class="form-row row">
								<div class="form-group col-md-6">
									<label for="limitFilesFolder">ALM Domain</label>
									<form:input path="almDomain" 
										class="form-control" id="almDomainId" placeholder="ALM Domain" required="required" />
								</div>
								<div class="form-group col-md-6">
									<label for="responseFilePrefix">ALM Project</label>
									<form:input path="almProject" class="form-control" id="almProjectId"
										placeholder="ALM Project" required="required" />
								</div>
					      </div>
						     
						    <div><p></p></div>
	  						<small id="dd" class="form-text text-muted"><span style="color:blue">ALM will automatically send Reports in given time interval</span>(<span style="color:red"> [second, minute, hour, day, month, weekday ex  00 05 21 * * * </span>)</small>	
							 <div class="input-group" >
	    					    <span class="input-group-addon">ALM Morning Reports&nbsp;</span>
	   						    <form:input id="almConfigG1Id" type="text" class="form-control msgc" path="almConfigG1" placeholder="Additional Info" required="required" />
	  					    </div>
  					     
	  							<small id="dd" class="form-text text-muted"><span style="color:blue">ALM will automatically send Reports in given time interval</span>(<span style="color:red"> [second, minute, hour, day, month, weekday ex  00 05 21 * * * </span>)</small>	
								<div class="input-group" >
	    					    <span class="input-group-addon">ALM Evening Reports&nbsp;</span>
	   						    <form:input id="almConfigG2Id" type="text" class="form-control msgc" path="almConfigG2" placeholder="Additional Info" required="required" />
	  					    </div>
  					     </div>
  					     
					       <br/>
					       <br/>
					    </div>
					       <br/>
					       <br/>
					  </div>
					</div>
					<button type="submit" id="submitButtonId" class="btn btn-primary">
					<i class="fa fa-floppy-o" aria-hidden="true"></i> Save Configuration Info</button>
					<button type="button" id="re-loadId" class="btn btn-success">
					<i class="fa fa-refresh" aria-hidden="true"></i> Reload Page</button>
					
					<br />
					<br />
					<br />
					<br />
				
				</form:form>
  			
			</div>
		</div>
	</div>

	<div>
	
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
 <jsp:include page="configfile-script.jsp"></jsp:include>
</html>
