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
  	while(rs.next()){
  		String[] strArr = {rs.getString(1),rs.getString(2)};
  		if(!libCountMap.containsKey(strArr[0])){
  			libCountMap.put(strArr[0], Integer.parseInt(strArr[1]));
  		}
  		else{
  			libCountMap.put(strArr[0], libCountMap.get(strArr[0])+Integer.parseInt(strArr[1]));
  		}
  	}
  	int butlerCount = 0,nwcCount = 0,averyCount =0,starrCount = 0,lehmanCount =0,urisCount =0;
  	
  	for(Entry<String, Integer> count: libCountMap.entrySet()){
  		if(count.getKey().contains("butler")){
  			butlerCount =butlerCount+count.getValue();
  		}
  		if(count.getKey().contains("avery")){
  			averyCount =averyCount+count.getValue();
  		}
  		if(count.getKey().contains("nwc")){
  			nwcCount =nwcCount+count.getValue();
  		}
  		if(count.getKey().contains("lehman")){
  			lehmanCount =lehmanCount+count.getValue();
  		}
  		if(count.getKey().contains("starr")){
  			starrCount =starrCount+count.getValue();
  		}
  		if(count.getKey().contains("uris")){
  			urisCount =urisCount+count.getValue();
  		}
  	}
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

<!-- Bootstrap Core CSS -->


<!-- Custom CSS -->
 <link href="css/dropzone.min.css" rel="stylesheet"> 
    <link href="css/basic.min.css" rel="stylesheet"> 
    <link rel="stylesheet" href="./css/normalize.css">    
    <link rel="stylesheet" href="./css/grid.css">  
    <link rel="stylesheet" href="./css/animate.css">
    <link rel="stylesheet" href="./css/component.css">  
    <link href="css/round-about.css" rel="stylesheet">
    <link href="css/bootstrap.min.css" rel="stylesheet">

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
		</div>
	</nav>

	<!-- Page Content -->
	<div class="container">
	<!-- Quick Check In Row -->
		<div class="row">
			<div class="col-lg-12" style = "text-align: center">
				<h1 class="page-header">
					Quick Check In <br>
				</h1>
				<form action="checkInServlet" method ="GET">
				<select name="rooms">
  				<option value="butler2">Butler 2nd Floor</option>
  				<option value="butler3">Butler 3rd Floor</option>
  				<option value="butler4">Butler 4th Floor</option>
  				<option value="butler5">Butler 5th Floor</option>
  				<option value="butler6">Butler 6th Floor</option> 				
  				<option value="nwc4">NWC 4th Floor</option>
  				<option value="nwc4">NWC 5th Floor</option>
  				<option value="uris">Uris</option>
  				<option value="avery1">Avery 1st Floor</option>
  				<option value="avery2">Avery 2nd Floor</option>
  				<option value="avery3">Avery 3rd Floor</option>
  				<option value="starr">Starr</option>
				<option value="lehman2">Lehman 2nd Floor</option>
				<option value="lehman3">Lehman 3rd Floor</option>
				</select>
				<br>
				<input class="btn btn-lg btn-primary" type="submit" value="Check In">
				</form>
			</div>
		</div>
		
		<!-- Library Row -->
		<div class="row">
			<div class="col-lg-12">
				<h2 class="page-header">Libraries</h2>
			</div>
			<div class="col-lg-4 col-sm-6 text-center">
				<img class="img-circle img-responsive img-center"
					src="img/ButlerLib.jpg" alt="" style="width:300px;height:200px">
				<h3>
					<a class="page-scroll" href=#butler>Butler Library<span class="badge"><%=butlerCount %></span></a><br><!-- <small>Job Title</small> -->
				</h3>
			</div>
			<div class="col-lg-4 col-sm-6 text-center">
				<img class="img-circle img-responsive img-center"
					src="img/UrisLib.jpg" alt="" style="width:300px;height:200px">
				<h3>
					<a class="page-scroll" href=#uris>Uris/Waston Library<span class="badge"><%=urisCount %></span></a><br><!-- <small>Job Title</small> -->
				</h3>
			</div>
			<div class="col-lg-4 col-sm-6 text-center">
				<img class="img-circle img-responsive img-center"
					src="img/averyLib.jpg" alt="" style="width:300px;height:200px">
				<h3>
					<a class="page-scroll" href=#avery>Avery Library<span class="badge"><%=averyCount %></span></a><br> <!-- <small>Job Title</small> -->
				</h3>
			</div>
			<div class="col-lg-4 col-sm-6 text-center">
				<img class="img-circle img-responsive img-center"
					src="img/NWCLib.jpg" alt="" style="width:300px;height:200px">
				<h3>
					<a class="page-scroll" href=#nwc>Science & Engineer Library<span class="badge"><%=nwcCount %></span></a><br><!-- <small>Job Title</small> -->
				</h3>
			</div>
			<div class="col-lg-4 col-sm-6 text-center">
				<img class="img-circle img-responsive img-center"
					src="img/StarrLib.jpg" alt="" style="width:300px;height:200px">
				<h3>
					<a class="page-scroll" href=#starr>Starr Library<span class="badge"><%=starrCount %></span></a><br><!-- <small>Job Title</small> -->
				</h3>
			</div>
			<div class="col-lg-4 col-sm-6 text-center">
				<img class="img-circle img-responsive img-center"
					src="img/LehmanLib.jpg" alt="" style="width:300px;height:200px">
				<h3>
					<a class="page-scroll" href=#lehman>Lehman Library<span class="badge"><%=lehmanCount %></span></a><br><!-- <small>Job Title</small> -->
				</h3>
			</div>
		</div>

		<hr>

		<!-- Q & A Row -->
		<div class="row">
			<div class="col-lg-12">
				<h1 class="page-header">
					Ask It! <br><small><small>Still couldn't decide where to study? Ask
						it! get real-time responses from all libraries!</small></small>
				</h1>
				<p><a href ="allQuestionServlet"><button class="btn btn-lg btn-primary">Go Ask!</button></a></p>
			</div>
		</div>
		
		<!-- Butler Section -->
		<div id="butler" class="a-section"><br><br></div>
   	    <div class="a-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Butler Library</h1>
                    <div class="col-md-5">
                    <img src="img/ButlerLib.jpg" style ="width: 380px;height: 180px; border-radius: 20px; object-fit: none; object-position: center;" alt="">
          		 	</div>
	          		<div class="col-md-7">
	          		<ul class="list-group">
  						<li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("butler2")%></span>
					    Butler 2nd Floor :
					  </li>
					  <li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("butler3")%></span>
					    Butler 3rd Floor :
					  </li>
					  <li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("butler4")%></span>
					    Butler 4th Floor :
					  </li>
					  <li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("butler5")%></span>
					    Butler 5th Floor :
					  </li>
					  <li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("butler6")%></span>
					    Butler 6th Floor :
					  </li>
					 </ul>
	                </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Avery Section -->
    <div id="avery" class="a-section"><br><br></div>
    <div class="b-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Avery Library</h1>
                    <div class="col-md-5">
                    <img src="img/averyLib.jpg" style ="width: 380px;height: 180px; border-radius: 20px; object-fit: none; object-position: center;" alt="">
          		 	</div>
	          		<div class="col-md-7">
	          		<ul class="list-group">
  						<li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("avery1")%></span>
					    Avery 1st Floor :
					  </li>
					  <li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("avery2")%></span>
					    Avery 2nd Floor :
					  </li>
					  <li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("avery3")%></span>
					    Avery 3rd Floor :
					  </li>
					</ul>
	                </div>
                </div>
            </div>
        </div>
    </div>

    <!-- NWC section-->
    <div id="nwc"><br><br></div>
    <div class="a-dsection">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Science & Engineer Library</h1>
                    <div class="col-md-5">
                    <img src="img/NWCLib.jpg" style ="width: 380px;height: 180px; border-radius: 20px; object-fit: none; object-position: center;" alt="">
          		 	</div>
	          		<div class="col-md-7">
	          		<ul class="list-group">
  						<li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("nwc4")%></span>
					    NWC 4th Floor :
					  </li>
					  <li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("nwc5")%></span>
					    NWC 5th Floor :
					  </li>
					</ul>
	                </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Uris section -->
    <div id="uris"><br><br></div>
    <div class="b-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Uris/Waston Library</h1>
                    <div class="col-md-5">
                    <img src="img/UrisLib.jpg" style ="width: 380px;height: 180px; border-radius: 20px; object-fit: none; object-position: center;" alt="">
          		 	</div>
	          		<div class="col-md-7">
	          		<ul class="list-group">
  						<li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("uris")%></span>
					    Uris/Waston Library :
					  </li>
					  
					</ul>
	                </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Starr -->
    <div id="starr"><br><br></div>
    <div class="a-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Starr Library</h1>
                    <div class="col-md-5">
                    <img src="img/StarrLib.jpg" style ="width: 380px;height: 180px; border-radius: 20px; object-fit: none; object-position: left;" alt="">
          		 	</div>
	          		<div class="col-md-7">
	          		<ul class="list-group">
  						<li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("starr")%></span>
					    Starr Library:
					  </li>
					</ul>
	                </div>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Lehman Section -->
    <div id="lehman"><br><br></div>
    <div class="b-section">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Lehman Library</h1>
                    <div class="col-md-5">
                    <img src="img/LehmanLib.jpg" style ="width: 380px;height: 180px; border-radius: 20px; object-fit: none; object-position: center;" alt="">
          		 	</div>
	          		<div class="col-md-7">
	          		<ul class="list-group">
  						<li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("lehman2")%></span>
					    Lehman 2nd Floor :
					  </li>
					  <li class="list-group-item">
					    <span class="badge"><%=libCountMap.get("lehman3")%></span>
					    Lehman 3rd Floor :
					  </li>
					 
					</ul>
	                </div>
                </div>
            </div>
        </div>
    </div>

		<!-- Footer -->
		<footer>
			<div class="row">
				<div class="col-lg-12">
					<p style="text-align:center">Copyright &copy; Find Your Spot@Columbia 2015</p>
				</div>
			</div>
		</footer>

	</div>

	<!-- jQuery -->
	<script src="js/jquery.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>

    <!-- Scrolling Nav JavaScript -->
    <script src="js/jquery.easing.min.js"></script>
    <script src="js/scrolling-nav.js"></script>
</body>

</html>
