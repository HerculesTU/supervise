<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.SecAuditService"%>
<%@ page language="java" import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java" import="com.alibaba.fastjson.JSON"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
  String SECAUDIT_ID = request.getParameter("SECAUDIT_ID");
  if(StringUtils.isNotEmpty(SECAUDIT_ID)){
      SecAuditService secAuditService = (SecAuditService)PlatAppUtil.getBean("secAuditService");
      Map<String,Object> secAudit = secAuditService.getRecord("PLAT_SYSTEM_SECAUDIT"
            ,new String[]{"SECAUDIT_ID"},new Object[]{SECAUDIT_ID});
      String DEL_COLNAMES = (String)secAudit.get("DEL_COLNAMES");
      String DEL_COLJSON= (String)secAudit.get("DEL_COLJSON");
      List<List> colJsonList = JSON.parseArray(DEL_COLJSON, List.class);
      request.setAttribute("DEL_COLNAMES", DEL_COLNAMES.split(","));
      request.setAttribute("colJsonList", colJsonList);
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

