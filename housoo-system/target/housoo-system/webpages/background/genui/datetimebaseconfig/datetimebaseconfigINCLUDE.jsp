<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" compcode="formcontainer" action="" id="BaseConfigForm" style="">

<div class="form-group" compcode="formgroup">
   <plattag:select name="ASSOCIAL_FIELDNAME" allowblank="true" auth_type="write" value="${fieldInfo.ASSOCIAL_FIELDNAME}" istree="true" onlyselectleaf="true" label_value="关联字段" placeholder="请选择关联字段" comp_col_num="3" label_col_num="1" style="width:100%;" dyna_interface="designService.findSelectFields" dyna_param="${formControl.FORMCONTROL_DESIGN_ID}">
   </plattag:select>
   <plattag:input name="CONTROL_NAME" allowblank="false" auth_type="write" value="${fieldInfo.CONTROL_NAME}" datarule="required;" label_value="控件命名" placeholder="请输入控件命名" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:radio name="ALLOW_BLANK" allowblank="false" auth_type="write" value="${fieldInfo.ALLOW_BLANK}" select_first="true" is_horizontal="true" label_value="允许为空" comp_col_num="3" label_col_num="1" static_values="允许:true,不允许:false">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:radio name="CONTROL_AUTH" allowblank="false" auth_type="write" value="${fieldInfo.CONTROL_AUTH}" select_first="true" is_horizontal="true" label_value="控件权限" comp_col_num="3" label_col_num="1" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'COMP_AUTH',ORDER_TYPE:'ASC'}">
   </plattag:radio>
   <plattag:input name="CONTROL_VALUE" allowblank="true" auth_type="write" value="${fieldInfo.CONTROL_VALUE}" label_value="控件值" placeholder="请输入控件值" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="PLAT_COMPNAME" allowblank="false" auth_type="write" value="${fieldInfo.PLAT_COMPNAME}" datarule="required;" label_value="中文标识" placeholder="请输入中文标识" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="COMP_PLACEHOLDER" allowblank="true" auth_type="write" value="${fieldInfo.COMP_PLACEHOLDER}" label_value="输入提示" placeholder="请输入placeholder" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="COMP_COL_NUM" allowblank="true" auth_type="write" value="${fieldInfo.COMP_COL_NUM}" datarule="positiveInteger;" label_value="控件栅格列数" placeholder="请输入控件栅格列数" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="LABEL_COL_NUM" allowblank="true" auth_type="write" value="${fieldInfo.LABEL_COL_NUM}" label_value="标签栅格列数" placeholder="请输入标签栅格列数" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONTROL_LABEL" allowblank="true" auth_type="write" value="${fieldInfo.CONTROL_LABEL}" label_value="控件标签名" placeholder="请输入控件标签名" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="CONTROL_ID" allowblank="true" auth_type="write" value="${fieldInfo.CONTROL_ID}" label_value="控件ID" placeholder="请输入控件ID" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:radio name="ISTIME" allowblank="true" auth_type="write" value="${fieldInfo.ISTIME}" select_first="true" is_horizontal="true" label_value="精确到时间" comp_col_num="3" label_col_num="1" static_values="否:false,是:true">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:select name="DATEFORMAT" allowblank="false" auth_type="write" value="${fieldInfo.DATEFORMAT}" istree="false" onlyselectleaf="false" label_value="时间格式" placeholder="请选择时间格式" comp_col_num="3" label_col_num="1" style="width:100%;" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'DATE_FORMAT',ORDER_TYPE:'ASC'}">
   </plattag:select>
   <plattag:input name="MAX_DATE" allowblank="true" auth_type="write" value="${fieldInfo.MAX_DATE}" label_value="限制最大时间" placeholder="请输入限制最大时间" comp_col_num="3" label_col_num="1">
   </plattag:input>
   <plattag:input name="MIN_DATE" allowblank="true" auth_type="write" value="${fieldInfo.MIN_DATE}" label_value="限制最小时间" placeholder="请输入限制最小时间" comp_col_num="3" label_col_num="1">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CHOOSEFN" allowblank="true" auth_type="write" value="${fieldInfo.CHOOSEFN}" label_value="选择回调函数名" placeholder="请输入选择回调函数名" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:radio name="DEFAULTNOW" allowblank="true" auth_type="write" value="${fieldInfo.DEFAULTNOW}" select_first="true" is_horizontal="true" label_value="默认当前" comp_col_num="4" label_col_num="2" static_values="否:-1,是:1">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:textarea name="CHOOSEFNCONTENT" allowblank="true" auth_type="write" value="${fieldInfo.CHOOSEFNCONTENT}" label_value="回调函数实现" placeholder="回调函数实现" comp_col_num="10" label_col_num="2">
   </plattag:textarea>

<script type="text/javascript">

</script>
</div>
<div class="hr-line-dashed"></div></form>

<script type="text/javascript">
$(function(){
	$("select[name='ASSOCIAL_FIELDNAME']").change(function() {
		var fieldName = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","field_name");
		var columnName = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","column_name");
		var field_comments = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","field_comments");
		var maxLength = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","max_length");
		$("input[name='CONTROL_NAME']").val(columnName);
		$("input[name='CONTROL_VALUE']").val("\\${"+fieldName+"}");
		$("input[name='PLAT_COMPNAME']").val(field_comments);
		$("input[name='CONTROL_LABEL']").val(field_comments);
		$("input[name='COMP_PLACEHOLDER']").val("请选择"+field_comments);
	});
	
});
</script>
