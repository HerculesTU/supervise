<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.SysLogService"%>
<%@ page language="java" import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java" import="com.alibaba.fastjson.JSON"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
String LOG_ID = request.getParameter("LOG_ID");
  if(StringUtils.isNotEmpty(LOG_ID)){
      SysLogService sysLogService = (SysLogService)PlatAppUtil.getBean("sysLogService");
      Map<String,Object> sysLog = sysLogService.getRecord("PLAT_SYSTEM_SYSLOG"
            ,new String[]{"LOG_ID"},new Object[]{LOG_ID});
      String LOG_DETAIL = (String)sysLog.get("LOG_DETAIL");
      Map<String,Object> logDetail = JSON.parseObject(LOG_DETAIL,Map.class);
      String DEL_COLNAMES = (String)logDetail.get("DEL_COLNAMES");
      request.setAttribute("DEL_COLNAMES", DEL_COLNAMES.split(","));
      request.setAttribute("colJsonList", logDetail.get("DEL_COLJSON"));
  }
%>

<div class="gridPanel">
	<table class="table table-bordered table-hover platedittable">
		<thead>
			<tr class="active">
			    <c:forEach items="${DEL_COLNAMES}" var="colname">
			       <th >${colname}</th>
			    </c:forEach>
			</tr>
			<c:forEach items="${colJsonList}" var="colJson">
			<tr>
			   <c:forEach items="${colJson}" var="data">
			      <td>${data}</td>
			   </c:forEach>
			</tr>
			</c:forEach>
		</thead>
	</table>
</div>

