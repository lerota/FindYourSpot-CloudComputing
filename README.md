# FindYourSpot-CloudComputing

A Website to help students to locate a spot in Columbia libraries more efficiently.

To use the website, just go to http://cloudfinal11-kvckkytqnf.elasticbeanstalk.com and here you go.

To host your own website, first pull the codes and then deploy the whole application on AWS Elastic Beanstalk in Eclipse. 
To run locally, you need to change the WebSocket client IP in allquestions2.jsp and upload.jsp.

Front End: 

    index.html - front page
    register.html/signin.html/checkin.html - register/sign in/check in page
    libinfo.jsp - main page
    allquestions2.jsp - classic Q&A main page
    unansweredquestions.jsp/useranswer.jsp/userquestion.jsp - classic Q&A sub page
    upload.jsp - "Snapchat" style Q&A page
    
Back End:

    regiServer.java/authServer.java/testServer.java/signoutServer.java/
    iniServer.java/initCheckCount.java/checkinServer.java/
    CheckInServlet.java/checkoutServer.java/checkOutServlet.java          - Check-in/out part
    
    middleServlet.java/unansweredquestions.java/addAnswerServlet.java/
    allQuestionServlet.java/answer.java/myquestions.java/CommentToDB.java/
    DBcontrol.java/getanswer.java/Notification.java/Rds.java/Rds2.java/
    SendToSNS.java/Server.java/SNSHelper.java/SNSMessage.java/
    Startup.java/WorkerPool.java                                          - classic Q&A part 
    
    uploadServlet.java/NotificationServer.java/TextToSNS.java/
    ServerDispatch.java/SNSHelper.java/SNSMessage.java/
    StartupInit.java/ThreadPool.java/                                     - "Snapchat" style Q&A part
