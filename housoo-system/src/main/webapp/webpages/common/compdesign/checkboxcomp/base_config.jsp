<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.DesignService"%>
<%@ page language="java" import="org.apache.commons.lang3.StringUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
    Map<String,Object> fieldInfo = (Map<String,Object>)request.getAttribute("fieldInfo");
    if(fieldInfo==null){
        fieldInfo = new HashMap<String,Object>();
        fieldInfo.put("DATA_TYPE","1");
        request.setAttribute("fieldInfo", fieldInfo);
    }else{
        String DATA_TYPE = (String)fieldInfo.get("DATA_TYPE");
        if(StringUtils.isEmpty(DATA_TYPE)){
            fieldInfo.put("DATA_TYPE","1");
        }
    }
%>

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
			maxlength="60" label_value="控件命名"  id="" max="100" min="0" style="" attach_props="">
		</plattag:input>
		<plattag:radio name="ALLOW_BLANK"  value="${fieldInfo.ALLOW_BLANK}" auth_type="write" select_first="true" static_values="允许:true,不允许:false"
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
	     <plattag:input name="CONTROL_STYLE" auth_type="write" allowblank="true" attach_props=""
			placeholder="请输入控件STYLE" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_STYLE}"
			maxlength="100" label_value="控件STYLE" >
		</plattag:input>
	    <plattag:radio name="IS_HORIZONTAL"  value="${fieldInfo.IS_HORIZONTAL}" auth_type="write" select_first="true" static_values="是:true,否:false"
		  allowblank="false" is_horizontal="true" label_col_num="1" label_value="是否横排" comp_col_num="3">
		</plattag:radio>
		<plattag:input name="CONTROL_LABEL" auth_type="write" allowblank="true"  
			placeholder="请输入控件标签" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_LABEL}"
			maxlength="30" label_value="控件标签" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:input name="COMP_COL_NUM" auth_type="write" allowblank="true" max="12" min="0" datarule="positiveInteger;"
			placeholder="请输入控件栅格列数" comp_col_num="3" label_col_num="1" value="${fieldInfo.COMP_COL_NUM}"
			maxlength="30" label_value="控件栅格列数" >
		</plattag:input>
		<plattag:input name="LABEL_COL_NUM" auth_type="write" allowblank="true" max="12" min="0" datarule="positiveInteger;"
			placeholder="请输入标签栅格列数" comp_col_num="3" label_col_num="1" value="${fieldInfo.LABEL_COL_NUM}"
			maxlength="30" label_value="标签栅格列数" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:input name="MAXSELECT" auth_type="write" allowblank="true" datarule="positiveInteger;"
			placeholder="请输入最大可选数量" comp_col_num="3" label_col_num="1" value="${fieldInfo.MAXSELECT}"
			maxlength="30" label_value="最大可选数量" >
		</plattag:input>
		<plattag:input name="MINSELECT" auth_type="write" allowblank="true" datarule="positiveInteger;"
			placeholder="请输入至少需要选择数量" comp_col_num="3" label_col_num="1" value="${fieldInfo.MINSELECT}"
			maxlength="30" label_value="至少选择数量" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	       <plattag:radio name="DATA_TYPE" value="${fieldInfo.DATA_TYPE}"
	       label_col_num="2" label_value="数据源方式" static_values="静态值:1,动态接口:2"
	       auth_type="write" select_first="false" 
	       allowblank="true" is_horizontal="true" comp_col_num="6"></plattag:radio>
     </div>
     <div class="hr-line-dashed"></div>
     <div class="form-group">
         <plattag:textarea name="STATICVALUES" auth_type="${fieldInfo.DATA_TYPE=='1'?'write':'hidden'}" 
           label_col_num="2" label_value="静态数据源" value="${fieldInfo.STATICVALUES}"
           allowblank="true" placeholder="请输入静态数据源例如(男:1,女:-1)" comp_col_num="10">
         </plattag:textarea>
     </div>
     <div class="hr-line-dashed"></div>
     <div id="DYNA_DIV" style="${fieldInfo.DATA_TYPE=='2'?'':'display: none;'}">
         <div class="form-group" >
             <plattag:select placeholder="请选择动态接口" istree="false" allowblank="true" 
  	       comp_col_num="10" auth_type="write" name="DYNAINTERFACE" style="width:95%;"
  	       label_col_num="2" label_value="动态接口" value="${fieldInfo.DYNAINTERFACE}"
  	       dyna_interface="genCmpTplService.findSelectJavaInters" dyna_param="2"
  	       ></plattag:select>
      	 </div>
          <div class="hr-line-dashed"></div>
	      <div class="form-group" >
	         <plattag:input name="DYNAPARAM" auth_type="write" allowblank="true" 
	  	       maxlength="100" value="${fieldInfo.DYNAPARAM}" 
	  	       placeholder="请输入动态参数" label_col_num="2" label_value="动态参数" comp_col_num="10">
	  	       </plattag:input>
	      </div>
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
	
	$("input[name='DATA_TYPE']").change(function() {
		var dataType = $(this).val();
		if(dataType=="1"){
			PlatUtil.changeUICompAuth("write","STATICVALUES");
			$("#DYNA_DIV").css("display","none");
		}else if(dataType=="2"){
			PlatUtil.changeUICompAuth("hidden","STATICVALUES");
			$("#DYNA_DIV").css("display","");
		}
	});
	
	$("select[name='DYNAINTERFACE']").change(function() {
		var gencmptplCode = PlatUtil.getSelectAttValue("DYNAINTERFACE","gencmptpl_code");
		$("input[name='DYNAPARAM']").val(gencmptplCode);
	});
});
</script>
