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
		<plattag:input name="CONTROL_ID" auth_type="write" allowblank="false" datarule="required;"
			placeholder="请输入表单ID" comp_col_num="4" label_col_num="2" value="${fieldInfo.CONTROL_ID}"
			maxlength="30" label_value="表单ID" >
		</plattag:input>
		<plattag:input name="FORM_STYLE" auth_type="write" allowblank="true" 
			placeholder="请输入表单STYLE" comp_col_num="4" label_col_num="2" value="${fieldInfo.FORM_STYLE}"
			maxlength="60" label_value="表单STYEL" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:select istree="false" allowblank="true" dyna_param="" style="width:90%;"
	    value="${fieldInfo.COMMON_URL}" dyna_interface="commonService.findCommonUrlList"
	    placeholder="请选择通用地址"
	    label_col_num="2" label_value="通用地址" comp_col_num="10" auth_type="write" name="COMMON_URL">
	    
	    </plattag:select>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	   <plattag:input name="FORM_ACTION" auth_type="write" allowblank="true" 
			placeholder="请输入表单提交地址" comp_col_num="10" label_col_num="2" value="${fieldInfo.FORM_ACTION}"
			maxlength="200" label_value="提交地址" >
		</plattag:input>
	</div>
</form>

<script type="text/javascript">
$(function(){
	PlatUtil.initSelect2();
	$("select[name='COMMON_URL']").change(function() {
		var COMMON_URL = PlatUtil.getSelectAttValue("COMMON_URL","value");
		$("input[name='FORM_ACTION']").val(COMMON_URL);
	});
});
</script>


