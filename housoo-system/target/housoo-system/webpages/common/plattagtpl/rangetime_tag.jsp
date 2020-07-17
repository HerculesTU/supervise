<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatBeanUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Map<String,Object> rangetimeConfig = PlatBeanUtil.getMapFromRequest(request);
request.setAttribute("config", rangetimeConfig);
%>

<%-- 
<jsp:include page="/webpages/common/plattagtpl/rangetime_tag.jsp">
    <jsp:param name="label_col_num" value="2" />
    <jsp:param name="format" value="YYYY-MM-DD"/>
    <jsp:param name="label_value" value="时间选择器" />
    <jsp:param name="comp_col_num" value="6" />
    <jsp:param value="auth_type" name="write"/>
    <jsp:param name="istime" value="false" />
    <jsp:param name="allowblank" value="false" />
    <jsp:param name="start_name" value="BEGIN_TIME" />
    <jsp:param name="end_name" value="END_TIME" />
</jsp:include>
--%> 

<label class="col-sm-${config.label_col_num} control-label">${config.label_value}：</label>
<div class="col-sm-${config.comp_col_num}">
	<plattag:datetime istime="${config.istime}" placeholder="${config.start_placeholder}"
	   allowblank="${config.allowblank}" posttimefmt="${config.posttimefmt}"
	  style="width: 45.5%;" end_name="${config.end_name}" value="${config.start_value}"
	 comp_col_num="0" auth_type="${config.auth_type}" format="${config.format}" name="${config.start_name}">
	</plattag:datetime>
	至
	<plattag:datetime istime="${config.istime}" placeholder="${config.end_placeholder}"
	   allowblank="${config.allowblank}" posttimefmt="${config.posttimefmt}"
	  style="width: 45.5%;" start_name="${config.start_name}" value="${config.end_value}"
	 comp_col_num="0" auth_type="${config.auth_type}" format="${config.format}" name="${config.end_name}">
	</plattag:datetime>
</div>
