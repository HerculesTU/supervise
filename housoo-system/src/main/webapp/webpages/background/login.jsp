<%@page import="com.housoo.platform.core.service.GlobalConfigService" %>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil" pageEncoding="UTF-8" %>
<%@ page language="java" import="java.util.Map" %>
<%@ page language="java" %>
<%@ page language="java" %>
<%@ page language="java" %>
<%@ page language="java" %>
<%@ page language="java" %>
<%@ page language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    if (PlatAppUtil.getRequest() != null) {
        Map<String, Object> sysUser = PlatAppUtil
                .getBackPlatLoginUser();
        if (sysUser != null) {
            response.sendRedirect(basePath
                    + "framework/ViewController.do?main");
        }
    }
    GlobalConfigService globalConfigService = (GlobalConfigService)PlatAppUtil.getBean("globalConfigService");
    Map<String,Object> globalConfig = globalConfigService.getFirstConfigMap();
    String backloginValidCode = (String) globalConfig.get("CONFIG_BACKVALIDCODE");
	request.setAttribute("backloginValidCode", backloginValidCode);
	int pwdErrorNum = Integer.parseInt(globalConfig.get("CONFIG_PWDERRORNUM").toString());
%>


<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>欢迎登录${sessionScope.globalProjectName}</title>
    <meta name="keywords" content="java 工作流开源，java工作流引擎 开源，快速开发平台,可视化流程，JBPM流程引擎，自定义表单，即时通讯，软件傻瓜开发平台，JAVA快速开发" />
    <meta name="description" content="中网软件快速开发平台，它本着灵活、快捷开发、高性能、高协作性、高稳定性、高可用性、人性化的操作体验为设计宗旨历经2年研发成功适用于搭建 OA、ERP、CRM、HR、HIS 等所有的企业信息管理系统。是全国性价比最高的零编程软件快速开发平台。" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="icon" href="webpages/website/official/images/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="webpages/website/official/images/favicon.ico" type="image/x-icon" />
	<plattag:resources restype="css"
		loadres="bootstrap-checkbox,jquery-ui,plat-ui,layer,nicevalid">
	</plattag:resources>
	<plattag:resources restype="js" loadres="jquery-ui,plat-util,layer,nicevalid,cryptojs">
	</plattag:resources>
	<!-- CSS -->
	<link rel="stylesheet" type="text/css" href="webpages/background/login/css/aos.css">
	<link rel="stylesheet" type="text/css" href="webpages/background/login/css/eui.css">
	<!-- JS -->
    <script type="text/javascript" src="webpages/background/login/js/aos.js"></script>
	<script type="text/javascript">
	  //获取浏览器地址
	  var href = window.top.location.href;
	  if(href.indexOf("login.jsp")==-1&&
			  href.indexOf("backLogoff.do")==-1&&href.indexOf("goBackLogin.do")==-1){
		  window.top.location.href = __ctxPath+"/webpages/background/login.jsp";
	  }
	</script>

  </head>
  
  <body>
  	<form action="security/LoginController/backLogin.do?service=${service}" id="loginForm" method="post">
	<div class="eui-logMain" data-aos="flip-up">
        <p class="eui-logName"><input type="text" name="USERNAME" maxlength="30" placeholder="请输入用户名" autofocus></p>
		<p class="eui-logPsd"><input type="password" name="PASSWORD" maxlength="30" placeholder="请输入密码"/></p>
		<c:if test="${backloginValidCode=='1'}">
		<p class="eui-logYzm"><input type="text" name="jcaptcha"  maxlength="6" placeholder="请输入验证码" style="width: 154px;">
		<img title="点击图片换一张" onclick="changeRandPic();" class="rfloat" style="cursor: pointer;" id="randpic" src="<%=basePath%>jcaptcha/ImageCaptchaController/image.do">
		</p>
		</c:if>
		<a href="javascript:void(0);" onclick="submitLoginForm();">登录</a>
	</div>
	</form>
  </body>
</html>
<script type="text/javascript">
    var pwdErrorNum = 0;
function submitLoginForm(){

	var USERNAME = $("input[name='USERNAME']").val();
	if(USERNAME==null||USERNAME==""){
		layer.alert("请输入用户名",{icon: 2,resize:false});
		return; 
	}
	var PASSWORD = $("input[name='PASSWORD']").val();
	if(PASSWORD==null||PASSWORD==""){
		layer.alert("请输入密码",{icon: 2,resize:false});
		return; 
	}
	if("${backloginValidCode}"=="true"){
		var jcaptcha = $("input[name='jcaptcha']").val();
		if(jcaptcha==null||jcaptcha==""){
			layer.alert("请输入验证码",{icon: 2,resize:false});
			return; 
		}
	}
	var url = $("#loginForm").attr("action");
	var formData = PlatUtil.getFormEleData("loginForm");
	//formData.PASSWORD = PlatUtil.getSha256Encode(PASSWORD);
    formData.PASSWORD = PlatUtil.getMD5Encode(PASSWORD);
    formData.loginType = "2";
	PlatUtil.ajaxProgress({
		url:url,
		params : formData,
		callback : function(resultJson) {
		    console.log(resultJson);
			if (resultJson.success) {
				if(resultJson.redirecturl){
					window.top.location.href = resultJson.redirecturl;
				}else{
					window.top.location.href = __ctxPath+"/framework/ViewController.do?main";
				}
			} else if (resultJson.success == undefined){
				changeRandPic();
                layer.alert("当前用户不存在！", {icon: 2, resize: false});
				//layer.alert(resultJson.msg,{icon: 2,resize:false});
			} else if (!resultJson.success){
                changeRandPic();
                layer.alert(resultJson.msg, {icon: 2, resize: false});
                pwdErrorNum ++;
                console.log(pwdErrorNum);
                //判断密码错误次数
                if(pwdErrorNum == <%=pwdErrorNum%>){
                    //调用锁定用户的接口
					console.log("判断密码错误次数"+pwdErrorNum);
                    PlatUtil.ajaxProgress({
                        url: "system/SysUserController/lockUser.do",
                        params: formData,
                        callback: function (lockUser) {
                            if(lockUser.success){
                                console.log(lockUser.msg);
                                layer.alert(lockUser.msg, {icon: 2, resize: false});
                            }
                        }
                    });
                }
            }
		}
	});
}


$(function () {
	// 动画
	AOS.init({
	  offset: 0,
	  duration: 1000,
	});

});

$("body").keydown(function(event) {
    if (event.keyCode == "13") {//keyCode=13是回车键
    	submitLoginForm();
    }
});  

function changeRandPic(){
	$("#randpic").attr({
          "src": "<%=basePath%>jcaptcha/ImageCaptchaController/image.do?"+Math.random()
     });
}

</script>
