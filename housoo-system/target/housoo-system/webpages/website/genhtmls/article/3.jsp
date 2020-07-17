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
    
    <title>广西苗族民众吹芦笙跳踩堂舞祭祖庆祝苗年</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="layer,bootstrap-checkbox,jquery-ui,select2,plat-ui">
	</plattag:resources>
  </head>
  
  <body class="gray-bg">
    <div class="wrapper wrapper-content  animated fadeInRight article">
        <div class="row">
            <div class="col-lg-10 col-lg-offset-1">
                <div class="ibox">
                    <div class="ibox-content">
                        <div class="pull-right">
                            <button class="btn btn-white btn-xs" type="button">2018-01-15 09:55:13</button>
                        </div>
                        <div class="text-center article-title">
                            <h1>
                                    广西苗族民众吹芦笙跳踩堂舞祭祖庆祝苗年
                                </h1>
                        </div>
                        <p><span style="color: rgb(56, 56, 56); font-family: simsun; text-indent: 32px; background-color: rgb(255, 255, 255);">1月14日，广西柳州市融水苗族自治县苗族民众穿着节日的盛装，吹起芦笙，跳起踩堂舞，共同祭祀祖先庆祝苗年。农历十一月二十八日是当地苗族的传统节日苗年，在当天，当地民众通过吹芦笙，跳踩堂，唱苗歌等形式来祭祀祖先，祈求来年风调雨顺，五谷丰登，百业兴旺。（完）</span></p><p><span style="color: rgb(56, 56, 56); font-family: simsun; text-indent: 32px; background-color: rgb(255, 255, 255);"><img src="http://localhost/szfoa/attachfiles/uploadUE/2018-01-15/4028d08160f7703f0160f7882d0e002e.jpg" alt="98553cb8-6c5e-4a8e-8454-7db2efcaa10a.jpg"/></span></p>
                        <hr>


                    </div>
                </div>
            </div>
        </div>

    </div>
    </body>
</html>
<plattag:resources restype="js" loadres="layer,jquery-ui,select2,jquery-layout,plat-util">
</plattag:resources>
<script type="text/javascript">

$(function(){
	//初始化UI控件
	
});
</script>
