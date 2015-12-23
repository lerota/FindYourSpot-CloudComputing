<%@ page language="java" import="java.io.*,java.util.*,cloudFinal11.*,java.sql.*,java.util.Map.Entry,java.util.HashMap.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%
  String usr = (String) session.getAttribute("usrName");
  String checkRoom = (String) session.getAttribute("checkRoom");
 
  if(usr==null){
	  request.getRequestDispatcher("index.html").forward(request,response);
  }  %>    
  <%
  String sql = "Select * from checkcount;";
  ResultSet rs = Rds.stmt.executeQuery(sql);
  HashMap<String,Integer> libCountMap = new HashMap<String,Integer>();
  HashMap<String,String> roomMap = new HashMap<String,String>();
  roomMap.put("butler2", "Butler 2nd Floor");
  roomMap.put("butler3", "Butler 3rd Floor");
  roomMap.put("butler4", "Butler 4th Floor");
  roomMap.put("butler5", "Butler 5th Floor");
  roomMap.put("butler6", "Butler 6th Floor");
  
  roomMap.put("avery1", "Avery 1st Floor");
  roomMap.put("avery2", "Avery 2nd Floor");
  roomMap.put("avery3", "Avery 1st Floor");
  
  roomMap.put("nwc4", "NWC 4th Floor");
  roomMap.put("nwc5", "NWC 5th Floor");
  
  roomMap.put("uris", "Uris/Waston Library");
  
  roomMap.put("starr", "Starr Library");
  
  roomMap.put("lehman2", "Lehman 2nd Floor");
  roomMap.put("lehman3", "Lehman 3rd Floor");
  String cr = roomMap.get(checkRoom);
  %>
  
<!DOCTYPE html>
<html>

  <head>
    <meta charset="utf-8">
    <title>Find Your Spot</title>
    <style>
      html, body {
        height: 100%;  
        margin: 0px;
        padding: 0px
      }
      section {
    	width: 100%;
	  } 
    </style>
    <!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/clean-blog.min.js"></script>
    
    <!-- Scrolling Nav JavaScript -->
    <script src="js/jquery.easing.min.js"></script>
    <script src="js/scrolling-nav.js"></script>
    <script src="js/dropzone.js"></script>
 	<script src="//cdnjs.cloudflare.com/ajax/libs/moment.js/2.6.0/moment.min.js"></script>
	<script src="./js/livestamp.min.js"></script>
     
    
	<!-- Custom CSS -->
	<link href="css/dropzone.min.css" rel="stylesheet"> 
    <link href="css/basic.min.css" rel="stylesheet"> 
    <link rel="stylesheet" href="./css/normalize.css">    
    <link rel="stylesheet" href="./css/grid.css">  
    <link rel="stylesheet" href="./css/animate.css">
    <link rel="stylesheet" href="./css/component.css">  
    <link href="css/round-about.css" rel="stylesheet">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    

    <!-- Custom Fonts -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link href='http://fonts.googleapis.com/css?family=Lora:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
	
  </head>

  <body>

    <!-- Page Header -->
    <!-- Set your background image for this header on the line below. -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">

		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<h1 style="color: white;/* font-family: Palatino Linotype */">Find a Spot</h1>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<p style="margin-top: 10px; color: white; text-align:right">
				<%if(checkRoom == null){%>
					Hi! <%=usr%>
				<%}
				else{%>		
					Hi! <%=usr%>, you are at: <%=cr %> now! Wrong?  <a style="color: white;" href="checkOutServlet?checkout=yes"><ins>Check Out</ins></a>
				<%}%>
					<br><a href="signoutServer" style=";"><button style="padding: 3px 6px; margin-top:2px" class='btn btn-3 btn-3e'><b>SIGN OUT</b></button></a><!-- <ins><b>Log Out</b></ins></a> -->
				</p>
			</div>
		</div>
	</nav>
     
    <!-- Main Content -->
    <nav class="navbar navbar-default" id="navbarhome">
  		<div class="container-fluid">
  		
  			<!-- Collect the nav links, forms, and other content for toggling -->
  			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse">
      			<ul class="nav nav-justified col-md-12">
        			<li><a href="libinfo.jsp">
        			    <span class="glyphicon glyphicon-home" aria-hidden="true"></span>
        			    </a>
        			</li>
                </ul>
   			</div>  
  		</div> 
	</nav>
	
	<nav class="navbar navbar-default" id="navbar1">
  		<div class="container-fluid">
  		
  			<!-- Collect the nav links, forms, and other content for toggling -->
  			<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      			
      			<ul class="nav nav-justified col-md-4">
                	<li>
                	<a href="middleServlet"><span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>Go Back to Ask Questions</a></li>
                </ul>
   			</div>  
  		</div> 
	</nav>
	
	<script>
	<% 
    	 String data = request.getAttribute("data").toString(); 
	%>
	var rawJSON = <%= data %>;	
	for (var mes in rawJSON) {
	    qId = rawJSON[mes].qId;
	    qtext = rawJSON[mes].qtext;
	    quserId = rawJSON[mes].quserId;
	    qtime = rawJSON[mes].qtime;	
	    aId = rawJSON[mes].aId;
	    atext = rawJSON[mes].atext;
	    auserId = rawJSON[mes].auserId;
	    atime = rawJSON[mes].atime;	
		console.log(qId);
		$("nav#navbar1").after("<div class='dropdown'><div class='container-fluid'><button class='btn btn-3 btn-3e col-xs-12' type='button' id='"+qId+"' data-toggle='dropdown'>" + quserId + " asked: " + qtext + " at " + qtime + "<span class='caret'></span></button><ul class='dropdown-menu col-xs-12' aria-labelledby='dropdownMenu1'><li><a style='text-align:center'>" + auserId + " answered: " + atext + " at " + atime + "</a></li></ul></div></div>");		
	}	
	</script>


    <!-- Footer -->
    <footer>
        <div class="container">
            <div class="row">
                <div class="col-lg-8 col-lg-offset-2 col-md-10 col-md-offset-1">
                    <ul class="list-inline text-center"> </ul>
                    <p class="copyright text-muted" style="text-align:center">Copyright &copy; Find Your Spot @ Columbia 2015</p>
                </div>
            </div>
        </div>
    </footer>

</body>

</html>