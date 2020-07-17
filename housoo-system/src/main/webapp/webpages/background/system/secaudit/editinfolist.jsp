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
      String FIELD_JSONLIST = (String)secAudit.get("FIELD_JSONLIST");
      List<Map> fieldList = JSON.parseArray(FIELD_JSONLIST, Map.class);
      request.setAttribute("fieldList", fieldList);
  }
%>

<div class="eui-warpper clearfix">
	<div class="eui-warpL">
		<div class="eui-warpTt">修改前数据</div>
		<div style="margin: 20px;">
			<table class="eui-tabData">
			    <c:forEach items="${fieldList}" var="field">
				    <tr>
						<th>${field.SECFIELD_CN}：</th>
						<c:if test="${field.SECFIELD_TYPE=='1'}">
						<td class="${field.SECFIELD_ISUPADE=='1'?'revamp':''}" >${field.SECFIELD_VAL}</td>
						</c:if>
						<c:if test="${field.SECFIELD_TYPE=='2'}">
						<td><img src="${field.SECFIELD_VAL}" style="width: 100px; height: 100px; border-radius: 50%;"></td>
						</c:if>
					</tr>
	   			 </c:forEach>
			</table>
		</div>
	</div>
	
	<div class="eui-warpL">
		<div class="eui-warpTt">修改后数据</div>
		<div style="margin: 20px;">
			<table class="eui-tabData">
			    <c:forEach items="${fieldList}" var="field">
				    <tr>
						<th>${field.SECFIELD_CN}：</th>
						<c:if test="${field.SECFIELD_TYPE=='1'}">
						<td class="${field.SECFIELD_ISUPADE=='1'?'revamp':''}" >${field.SECFIELD_UPVAL}</td>
						</c:if>
						<c:if test="${field.SECFIELD_TYPE=='2'}">
						<td><img src="${field.SECFIELD_UPVAL}" style="width: 100px; height: 100px; border-radius: 50%;"></td>
						</c:if>
					</tr>
	   			 </c:forEach>
			</table>
		</div>
	</div>
	
</div>

