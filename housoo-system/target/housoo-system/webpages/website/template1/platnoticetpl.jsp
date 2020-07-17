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
    
    <title>${ARTICLE_TITLE}</title>
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
                            <button class="btn btn-white btn-xs" type="button">${ARTICLE_PUBTIME}</button>
                        </div>
                        <div class="text-center article-title">
                            <h1>
                                    ${ARTICLE_TITLE}
                                </h1>
                        </div>
                        ${ARTICLE_CONTENT!''}
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
