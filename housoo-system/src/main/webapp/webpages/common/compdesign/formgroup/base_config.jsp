<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.DesignService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" id="BaseConfigForm">
	<div class="form-group plat-form-title">
		<span class="plat-current"> 基本信息 </span>
	</div>
	<div class="form-group">
		<plattag:input name="CONTROL_ID" auth_type="write" allowblank="true" 
			placeholder="请输入表单组ID" comp_col_num="4" label_col_num="2" value="${fieldInfo.CONTROL_ID}"
			maxlength="30" label_value="表单组ID" >
		</plattag:input>
		<plattag:input name="CONTROL_STYLE" auth_type="write" allowblank="true" 
			placeholder="请输入控件STYLE" comp_col_num="4" label_col_num="2" value="${fieldInfo.CONTROL_STYLE}"
			maxlength="100" label_value="控件STYLE" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
</form>


