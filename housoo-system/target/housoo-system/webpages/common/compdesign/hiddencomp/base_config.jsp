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
		<plattag:select name="ASSOCIAL_FIELDNAME" auth_type="write" istree="true"
            label_col_num="2" label_value="关联字段" style="width:100%;"
			allowblank="true" placeholder="请选择关联字段" comp_col_num="4" onlyselectleaf="true"
			value="${fieldInfo.ASSOCIAL_FIELDNAME}" dyna_interface="designService.findSelectFields"
			dyna_param="${formControl.FORMCONTROL_DESIGN_ID}" 
	    ></plattag:select>
		<plattag:input name="CONTROL_NAME" auth_type="write" allowblank="false" datarule="required;" 
			placeholder="请输入控件命名" comp_col_num="4" label_col_num="2" value="${fieldInfo.CONTROL_NAME}"
			maxlength="60" label_value="控件命名" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
		<plattag:input name="CONTROL_VALUE" auth_type="write" allowblank="true" 
			placeholder="请输入控件值" comp_col_num="4" label_col_num="2" value="${fieldInfo.CONTROL_VALUE}"
			maxlength="60" label_value="控件值" >
		</plattag:input>
		<plattag:input name="PLAT_COMPNAME" auth_type="write" allowblank="false" datarule="required;" 
			placeholder="请输入中文标识" comp_col_num="4" label_col_num="2" value="${fieldInfo.PLAT_COMPNAME}"
			maxlength="30" label_value="中文标识" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
		<plattag:input name="CONTROL_ID" auth_type="write" allowblank="true" 
			placeholder="请输入控件ID" comp_col_num="4" label_col_num="2" value="${fieldInfo.CONTROL_ID}"
			maxlength="30" label_value="控件ID" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
</form>

<script type="text/javascript">

$(function(){
	PlatUtil.initUIComp("#BaseConfigForm");
	$("select[name='ASSOCIAL_FIELDNAME']").change(function() {
		var fieldName = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","field_name");
		var columnName = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","column_name");
		var field_comments = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","field_comments");
		$("input[name='CONTROL_NAME']").val(columnName);
		$("input[name='CONTROL_VALUE']").val("\\${"+fieldName+"}");
		$("input[name='PLAT_COMPNAME']").val(field_comments);
	});
});
</script>
