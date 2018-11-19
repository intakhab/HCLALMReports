<meta http-equiv="refresh" content="${user.sessionTime}; url=logout">
<link rel="shortcut icon" href="/images/alm.ico">
<link rel="stylesheet" href="/css/main.css">
<link rel="stylesheet" href="/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/jquery-ui.css">
<link rel="stylesheet" href="/font-awesome/css/font-awesome.css">
<script type="text/javascript" src="/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/js/Popper.js"></script>
<script type="text/javascript" src="/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<style type="text/css">
.focus {
	border: 2px solid #AA88FF;
	background-color: #FFEEAA;
}
</style>
<nav class="navbar navbar-inverse navbar-fixed-top">
<!-- navbar-fixed-top -->
<div>
 <!-- class= navbar-header -->
			<a class="navbar-brand" href="javascript:void(0)">
				 <strong>
				  <span style="color:blue;">HCL</span>
				 <span style="color:red;">A</span>
				 <span style="color:cyan;">L</span>
				 <span style="color:yellow;">M</span>
				 </strong>
				 
			</a>
		</div>
	<div>
		<div id="navbar" class="collapse navbar-collapse mfont"  role ="navigation">
			<ul class="nav navbar-nav">
				<li id="tab1"  class="active"><a href="status"  title="Check Running and Others Status">
				<i class="fa fa-home" aria-hidden="true"></i> Status</a></li>
				<li id="tab2"><a href="configfile" title="Setting configuation for application run">
				<i class="fa fa-cog" aria-hidden="true"></i> Settings </a></li>
			</ul>
			<!-- UI -->
			<ul class="nav navbar-nav navbar-right" style="padding-right: 30px;">
					<li><a href="#" title="Username"><i class="fa fa-user-o" aria-hidden="true" style="color: yellow"> </i>
					  &nbsp;Hello: ${user.username}</a></li>
					<li class="dropdown" id="tab10">
				        <a class="dropdown-toggle" data-toggle="dropdown" href="javascript:void(0)" title="Logout & User registration">
				        <span class="fa fa-address-book"></span></a>
				        <ul class="dropdown-menu mfont" id="tab10" >
				          <li><a href="logout"  title="Logout"><i class="fa fa-sign-out" aria-hidden="true" style="color: red;font-size:15px;"></i>&nbsp; Logout&nbsp;&nbsp;</a></li>
				          <li><a href="reguser" title="Registration of user"><i class="fa fa-user-plus" aria-hidden="true"></i>&nbsp;&nbsp; Add User</a></li>
				          <li><a href="showusers" title="All users"><i class="fa fa-users" aria-hidden="true"></i>&nbsp;&nbsp; Show User</a></li>
				          <li><a href="javascript:void(0)" onclick="restoreUsers()" title="Restore users"><i class="fa fa-window-restore" aria-hidden="true"></i>&nbsp;&nbsp; Restore User</a></li>
				        </ul>
			      </li>
			</ul>
		</div>
	</div>
</nav>
<div style="padding-top: 80px;">
	<jsp:include page="model.jsp"></jsp:include>
</div>
