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
  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">

<head>

<!-- <meta charset="utf-8"> -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<style>
/* 	  body {background-image: url("img/library-op.jpg");} */
.navbar {
	/* background-image: url("img/library-10.jpg"); */
}
</style>
<title>Libraries -Find a Spot</title>

	<!-- jQuery -->
    <script src="js/jquery.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/clean-blog.min.js"></script>
    
    <!-- Scrolling Nav JavaScript -->
    <script src="js/jquery.easing.min.js"></script>
    <script src="js/scrolling-nav.js"></script>
    
    <script> 
  $(document).ready(function() {
    
    var reset = false;
    var chatClient = new WebSocket("ws://54.165.227.167:8080/aws");
    //var chatClient = new WebSocket("ws://localhost:8080/cloudFinal11/aws");
    var Button = "All";
    var Id = "";
    var Mid = alphanumeric_unique();
    var Text = "";
    var Timestamp = "";
    var Aid = "";
    
    var qId = "";
        var qtext = "";
        var quserId = "";
        var qtime = "";

    chatClient.onopen = function(){
      console.log("test");
      setInterval(function(){
        var obj = {"Button":Button, "Text":Text, "Id":Id, "Timestamp":Timestamp, "Qid":Mid, "Aid":Aid};
        var JSONstring = JSON.stringify(obj);
        if (reset){
        chatClient.send(JSONstring);
        reset = false;
        }
        if (!reset){
          var obj = {"Button":"All"};
          var JSONstring = JSON.stringify(obj);
          chatClient.send(JSONstring);
          reset = false;
        } 
      },3000);
    }
    
    chatClient.onmessage = function(evt){
      $("div").remove(".dropdown");
      var rawString = evt.data;
        console.log(rawString);

      var rawJSON = JSON.parse(rawString);
      for (var first in rawJSON) {
            var flag = rawJSON[first].flag;
            break;        
        } 

      if(flag == "all"){
        for (var mes in rawJSON) {
              qId = rawJSON[mes].qId;
              qtext = rawJSON[mes].qtext;
              quserId = rawJSON[mes].quserId;
              qtime = rawJSON[mes].qtime;
              aId = rawJSON[mes].aId;
              atext = rawJSON[mes].atext;
              auserId = rawJSON[mes].auserId;
              atime = rawJSON[mes].atime;
            $("button#qtnSubmit").after("<div class='dropdown' id='div"+qId+"'><div class='container-fluid'><button class='btn btn-3 btn-3e col-xs-12' type='button' id='"+qId+"' data-toggle='dropdown'>" + quserId + " asked: " + qtext + " at " + qtime + "<span class='caret'></span></button><ul class='dropdown-menu col-xs-12' aria-labelledby='dropdownMenu1'><li><a style='text-align:center'>" + auserId + " answered: " + atext + " at " + atime + "</a></li></ul></div></div>");    
            console.log(qId);
            console.log(qtext);
            console.log(quserId);
            console.log(qtime);
        } 
        } 
    }
    
    $("#qtnSubmit").click(function() {
      Text = $("#question").val();
      Id = '<%= session.getAttribute( "email" ) %>';
        Timestamp = new Date().toISOString();
      Button = "Question";  
      Mid = alphanumeric_unique();
      reset = true;
    });   
    
  });

  function alphanumeric_unique() {
      return Math.random().toString(36).split('').filter( function(value, index, self) { 
          return self.indexOf(value) === index;
      }).join('').substr(2,8);
  }
  
  </script>
     
    
	<!-- Custom CSS -->
	<link href="css/dropzone.min.css" rel="stylesheet"> 
    <link href="css/basic.min.css" rel="stylesheet"> 
    <link rel="stylesheet" href="./css/normalize.css">    
    <link rel="stylesheet" href="./css/grid.css">  
    <link rel="stylesheet" href="./css/animate.css">
    <link rel="stylesheet" href="./css/component.css">  
    <link href="css/round-about.css" rel="stylesheet">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->


</head>

<body>

	<!-- Navigation -->

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
			<!-- /.navbar-collapse -->
		</div>
		<!-- /.container -->
	</nav>
 <nav class="navbar navbar-default" id="navbarhome">
      <!-- /.container -->
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
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse">
            <ul class="nav nav-justified col-md-4">
              <li><a href="myquestions">Your Questions</a>
              </li>
                  <li><a href="getanswer">Your Answers</a>
                  </li>
                    <li><a href="unansweredquestions">Unanswered Questions</a>
                    </li>
            </ul>
        </div>  
      </div> 
  </nav> 
      

    <script>
  <% 
       String points = session.getAttribute("points").toString(); 
  %>
    var rawJSONpoints = <%= points %>;  
    var points = rawJSONpoints.points;
    console.log(points);
    $("nav#navbar1").after("<div class='col-md-12 text-center' id='div1'><span class='label label-danger'>Your Points: "+ points +" </span></div>");    

    if(parseInt(points)<=0){
      $("div#div1").after("<section><div class='row'><div class='.col-xs-6'><input type='text' placeholder='Question:' id='question'/></div><div class='.col-xs-6'><button id='qtnSubmit' class='btn btn-3 btn-3e disabled'>Ask</button></div></div></section>");
      $("button#qtnSubmit").after("<section><div class='row'><div class='.col-xs-6'><small>No enough points? Go here to ask and get real-time answer!</small></div><div class='.col-xs-6'><p><a href ='upload.jsp'>Go Ask!</a></p></div></div></section>");

    }else{
      $("div#div1").after("<section><div class='row'><div class='.col-xs-6'><input type='text' placeholder='Question:' id='question'/></div><div class='.col-xs-6'><button id='qtnSubmit' class='btn btn-3 btn-3e'>Ask</button></div></div></section>");
    }
  </script> 


		<!-- Footer -->
		<footer>
			<div class="row">
				<div class="col-lg-12">
					<p style="text-align:center" class="copyright text-muted">Copyright &copy; Find Your Spot@Columbia 2015</p>
				</div>
			</div>
		</footer>
	
</body>

</html>
