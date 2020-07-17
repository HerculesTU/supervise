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
      List<Map> fieldList = (List<Map>)logDetail.get("FIELD_JSONLIST");
      request.setAttribute("fieldList", fieldList);
  }
%>

<div class="eui-warpper clearfix">
    <div style="margin: 20px;">
	<table class="eui-tabData">
	    <c:forEach items="${fieldList}" var="field">
	    <tr>
			<th>${field.SECFIELD_CN}：</th>
			<c:if test="${field.SECFIELD_TYPE=='1'}">
			<td>${field.SECFIELD_VAL}</td>
			</c:if>
			<c:if test="${field.SECFIELD_TYPE=='2'}">
			<td><img src="${field.SECFIELD_VAL}" style="width: 100px; height: 100px; border-radius: 50%;"></td>
			</c:if>
		</tr>
	    </c:forEach>
	</table>
	</div>
</div>

