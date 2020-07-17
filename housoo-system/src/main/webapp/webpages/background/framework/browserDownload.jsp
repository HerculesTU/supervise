<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>浏览器下载</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-submenu,plat-main"></plattag:resources>
	 <style>
    .eui-foriebg{position:absolute; width:100%; height:100%; background:#2a3c54;}
	.eui-forie{position:absolute; top:50%; left:50%; width:760px; height:225px; margin:-112px 0 0 -380px; background:url(webpages/background/framework/images/forie.png) no-repeat;}
	.eui-forie ul{padding:140px 0 0 350px;}
	.eui-forie ul li{float:left; width:72px; margin-right:30px; text-align:center;}
	.eui-forie ul li a{display:block; width:72px; color:#fff;}
	.eui-forie ul li a:hover{text-decoration:underline;}
	.eui-forie ul li p{margin:6px 0 0 0; line-height:20px;}
    </style>
  </head>
  
<body class="eui-foriebg">
    <div class="eui-forie">
    	<ul>
        	<li><a href="plug-in/Browser/Chrome.exe" target="_blank" title="请点击下载"><img src="webpages/background/framework/images/l-icon.png"><p>谷歌浏览器</p></a></li>
            <li><a href="plug-in/Browser/IE11.exe" target="_blank" title="请点击下载"><img src="webpages/background/framework/images/l-icon1.png"><p>IE浏览器</p></a></li>
            <li><a href="plug-in/Browser/Firefox.exe" target="_blank" title="请点击下载"><img src="webpages/background/framework/images/l-icon2.png"><p>火狐浏览器</p></a></li>
            <li><a href="plug-in/Browser/360.exe" target="_blank" title="请点击下载"><img src="webpages/background/framework/images/l-icon1.png"><p>360浏览器</p></a></li>
        </ul>
    </div>
</body>
</html>
<plattag:resources restype="js" loadres="bootstrap-submenu,jquery-cookie,plat-util,plat-tab,layer">
</plattag:resources>
