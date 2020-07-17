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
            label_col_num="1" label_value="关联字段" style="width:100%;"
			allowblank="true" placeholder="请选择关联字段" comp_col_num="3" onlyselectleaf="true"
			value="${fieldInfo.ASSOCIAL_FIELDNAME}" dyna_interface="designService.findSelectFields"
			dyna_param="${formControl.FORMCONTROL_DESIGN_ID}" 
	    ></plattag:select>
		<plattag:input name="CONTROL_NAME" auth_type="write" allowblank="false" datarule="required;" 
			placeholder="请输入控件命名" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_NAME}"
			maxlength="60" label_value="控件命名"  >
		</plattag:input>
		<plattag:radio name="ALLOW_BLANK" value="${fieldInfo.ALLOW_BLANK}" auth_type="write" select_first="true" static_values="允许:true,不允许:false"
		  allowblank="false" is_horizontal="true" label_col_num="1" label_value="允许为空" comp_col_num="3">
		</plattag:radio>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:radio name="CONTROL_AUTH" value="${fieldInfo.CONTROL_AUTH}" auth_type="write" select_first="true"
	      dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'COMP_AUTH',ORDER_TYPE:'ASC'}"
		  allowblank="false" is_horizontal="true" label_col_num="1" label_value="权限" comp_col_num="3">
		</plattag:radio>
	    <plattag:input name="CONTROL_VALUE" auth_type="write" allowblank="true" 
			placeholder="请输入控件值" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_VALUE}"
			maxlength="60" label_value="控件值" >
		</plattag:input>
	    <plattag:input name="PLAT_COMPNAME" auth_type="write" allowblank="false" datarule="required;" 
			placeholder="请输入中文标识" comp_col_num="3" label_col_num="1" value="${fieldInfo.PLAT_COMPNAME}"
			maxlength="30" label_value="中文标识" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:radio name="DATA_SIZE" auth_type="write" select_first="true" 
	    static_values="一般大小:xs,中等大小:md" value="${fieldInfo.DATA_SIZE}" label_value="大小"
	    allowblank="false" is_horizontal="true" comp_col_num="3" label_col_num="1"></plattag:radio>
		<plattag:input name="DATA_STEP" auth_type="write" allowblank="false"  datarule="required;"
			placeholder="请输入跨度值,例如1" comp_col_num="3" label_col_num="1" value="${fieldInfo.DATA_STEP}"
			maxlength="8" label_value="跨度值"  >
		</plattag:input>
		<plattag:input name="CONTROL_LABEL" auth_type="write" allowblank="true"  
			placeholder="请输入控件标签" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_LABEL}"
			maxlength="30" label_value="控件标签" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:input name="COMP_COL_NUM" auth_type="write" allowblank="true" datarule="positiveInteger;"
			placeholder="请输入控件栅格列数" comp_col_num="3" label_col_num="1" value="${fieldInfo.COMP_COL_NUM}"
			maxlength="30" label_value="控件栅格列数" >
		</plattag:input>
		<plattag:input name="LABEL_COL_NUM" auth_type="write" allowblank="true" datarule="positiveInteger;"
			placeholder="请输入标签栅格列数" comp_col_num="3" label_col_num="1" value="${fieldInfo.LABEL_COL_NUM}"
			maxlength="30" label_value="标签栅格列数" >
		</plattag:input>
	</div>
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
		$("input[name='CONTROL_LABEL']").val(field_comments);
	});
	
});
</script>
