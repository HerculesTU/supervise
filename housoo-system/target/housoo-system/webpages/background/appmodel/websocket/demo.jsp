<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String webSocketPath = PlatAppUtil.getWebSocketServerUrl(request);
request.setAttribute("webSocketPath", webSocketPath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP '1.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<plattag:resources restype="css"
		loadres="bootstrap-checkbox,jquery-ui,plat-ui,layer,nicevalid">
</plattag:resources>
<plattag:resources restype="js" loadres="jquery-ui,plat-util,layer,nicevalid">
</plattag:resources>

</head>

<body>
	<button onclick="send()">推送消息</button>
 </body>
   
  <script type="text/javascript">
      var uuid = PlatUtil.getUUID();
      var websocket = PlatUtil.initWebSocket({
    	  //指定websocket服务器
    	  url:"${webSocketPath}",
    	  //指定客户端ID
    	  clientId:uuid,
    	  onmessage:function(msgContent){
    		  alert("客户端接收到消息:"+msgContent);
    	  },
    	  onclose:function(){
    		  //alert("调用了关闭方法...");
    	  }
      });
      
      //发送消息
      function send(){
          websocket.send("{msgContent:'测试内容',invokeJavaInter:'leaveInfoService.sendWebSocketMsg'}");
      }
      
  </script>
</html>
