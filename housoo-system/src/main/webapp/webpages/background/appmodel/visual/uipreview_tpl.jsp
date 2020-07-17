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
    <title>${DESIGN_NAME}</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="${DESIGN_INTERNALRESES}"></plattag:resources>
    <#if externalcss?? && (externalcss?size > 0) >
    <#list externalcss as rescss>
    <link rel="stylesheet" type="text/css" href="${rescss}">
    </#list>
    </#if>
    <plattag:resources restype="js" loadres="${DESIGN_INTERNALRESES}"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    ${genHtmlCode!''}
  </body>
</html>

<#if externaljs?? && (externaljs?size > 0) >
<#list externaljs as resjs>
<script type="text/javascript" src="${resjs}"></script>
</#list>
</#if>
<script type="text/javascript">
${DESIGN_JSENHANCE!''}
</script>
