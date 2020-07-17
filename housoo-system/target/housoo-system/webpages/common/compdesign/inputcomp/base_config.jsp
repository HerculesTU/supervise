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
	    <plattag:select placeholder="请选择验证规则" istree="false" style="width:100%;"
	    dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'VALID_RULE',ORDER_TYPE:'ASC'}"
	    allowblank="true" comp_col_num="3" label_col_num="1" label_value="验证规则" auth_type="write"
	     name="DATA_RULE" multiple="multiple" value="${fieldInfo.DATA_RULE}" >
	    </plattag:select>
		<plattag:input name="MAX_LENGTH" auth_type="write" allowblank="true"  datarule="positiveInteger;"
			placeholder="请输入输入最大长度" comp_col_num="3" label_col_num="1" value="${fieldInfo.MAX_LENGTH}"
			maxlength="30" label_value="输入最大长度"  >
		</plattag:input>
		<plattag:input name="CONTROL_LABEL" auth_type="write" allowblank="true"  
			placeholder="请输入控件标签" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_LABEL}"
			maxlength="30" label_value="控件标签" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:input name="COMP_PLACEHOLDER" auth_type="write" allowblank="false" datarule="required;"
			placeholder="请输入提示" comp_col_num="3" label_col_num="1" value="${fieldInfo.COMP_PLACEHOLDER}"
			maxlength="30" label_value="输入提示" >
		</plattag:input>
	    <plattag:input name="COMP_COL_NUM" auth_type="write" allowblank="true" datarule="positiveInteger;"
			placeholder="请输入控件栅格列数" comp_col_num="3" label_col_num="1" value="${fieldInfo.COMP_COL_NUM}"
			maxlength="30" label_value="控件栅格列数" >
		</plattag:input>
		<plattag:input name="LABEL_COL_NUM" auth_type="write" allowblank="true" datarule="positiveInteger;"
			placeholder="请输入标签栅格列数" comp_col_num="3" label_col_num="1" value="${fieldInfo.LABEL_COL_NUM}"
			maxlength="30" label_value="标签栅格列数" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group plat-form-title">
		<span class="plat-current">附加信息</span>
	</div>
	<div class="form-group">
	    <plattag:input name="CONTROL_STYLE" auth_type="write" allowblank="true" attach_props=""
			placeholder="请输入控件STYLE" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_STYLE}"
			maxlength="100" label_value="控件STYLE" >
		</plattag:input>
		<plattag:input name="CONTROL_ATTACH" auth_type="write" allowblank="true" 
			placeholder="附加属性例子draggable='true' autofocus='autofocus' " comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_ATTACH}"
			maxlength="100" label_value="附加属性" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:input name="COMP_MAX_VALUE" auth_type="write" allowblank="true" 
			placeholder="请输入最大值" comp_col_num="3" label_col_num="1" value="${fieldInfo.COMP_MAX_VALUE}"
			maxlength="30" label_value="最大值" >
		</plattag:input>
		<plattag:input name="COMP_MIN_VALUE" auth_type="write" allowblank="true" 
			placeholder="请输入最小值" comp_col_num="3" label_col_num="1" value="${fieldInfo.COMP_MIN_VALUE}"
			maxlength="30" label_value="最小值" >
		</plattag:input>
		<plattag:radio name="AJAX_ABLE" value="${fieldInfo.AJAX_ABLE}" auth_type="write" select_first="true" static_values="否:-1,是:1"
		  allowblank="true" is_horizontal="true" label_col_num="1" label_value="AJAX验证" comp_col_num="3">
		</plattag:radio>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:input name="AJAX_URL" auth_type="write" allowblank="true" 
			placeholder="请输入AJAX验证URL" comp_col_num="10" label_col_num="2" value="${fieldInfo.AJAX_URL}"
			maxlength="200" label_value="AJAX验证URL" >
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
		var maxLength = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","max_length");
		$("input[name='MAX_LENGTH']").val(maxLength/2-2);
		$("input[name='CONTROL_NAME']").val(columnName);
		$("input[name='CONTROL_VALUE']").val("\\${"+fieldName+"}");
		$("input[name='PLAT_COMPNAME']").val(field_comments);
		$("input[name='CONTROL_LABEL']").val(field_comments);
		$("input[name='COMP_PLACEHOLDER']").val("请输入"+field_comments);
	});
	
	$("input[name='AJAX_ABLE']").change(function() {
		var ajaxAble = $(this).val();
		if(ajaxAble=="1"){
			var AJAX_URL = $("input[name='AJAX_URL']").val();
			if(!AJAX_URL){
				var CONTROL_LABEL = $("input[name='CONTROL_LABEL']").val();
				var CONTROL_NAME = $("input[name='CONTROL_NAME']").val();
				var ajaxUrl = "remote[common/baseController.do?checkUnique&VALID_TABLENAME=";
				var table_name = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","table_name");
				ajaxUrl+=table_name+"&VALID_FIELDLABLE="+CONTROL_LABEL;
				ajaxUrl+="&VALID_FIELDNAME="+CONTROL_NAME+"&RECORD_ID="+"]";
				$("input[name='AJAX_URL']").val(ajaxUrl);
			}
			
		}
		
	});
});
</script>
