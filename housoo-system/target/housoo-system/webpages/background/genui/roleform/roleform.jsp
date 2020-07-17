<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>角色表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="metadata/DataResController.do?invokeRes&amp;PLAT_RESCODE=system_role_addupdate" id="RoleForm" style="">

  <input type="hidden" name="ROLE_ID" value="${role.ROLE_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:select name="ROLE_GROUPID" allowblank="false" auth_type="write" value="${role.ROLE_GROUPID}" istree="false" onlyselectleaf="false" label_value="所属角色组" placeholder="请选择所属角色组" comp_col_num="4" label_col_num="2" dyna_interface="roleGroupService.findForSelect" dyna_param="没有参数">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="ROLE_CODE" allowblank="false" auth_type="write" value="${role.ROLE_CODE}" datarule="required;onlyLetterNumberUnderLine;remote[common/baseController.do?checkUnique&amp;VALID_TABLENAME=PLAT_SYSTEM_ROLE&amp;VALID_FIELDLABLE=角色编码&amp;VALID_FIELDNAME=ROLE_CODE]" maxlength="30" label_value="角色编码" placeholder="请输入角色编码" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="ROLE_NAME" allowblank="false" auth_type="write" value="${role.ROLE_NAME}" datarule="required;" maxlength="30" label_value="角色名称" placeholder="请输入角色名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:textarea name="ROLE_DESC" allowblank="true" auth_type="write" value="${role.ROLE_DESC}" maxlength="254" label_value="角色描述" placeholder="请输入角色描述" comp_col_num="10" label_col_num="2">
   </plattag:textarea>

<script type="text/javascript">

</script>
</div>
<div class="hr-line-dashed"></div></form></div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" id="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;提交
		</button>
		<button type="button" onclick="closeWindow();" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
	if(PlatUtil.isFormValid("#RoleForm")){
		var url = $("#RoleForm").attr("action");
		var formData = PlatUtil.getFormEleData("RoleForm");
     var formfieldModifyArrayJson = PlatUtil.getFormFieldValueModifyArrayJSON();
     formData.formfieldModifyArrayJson = formfieldModifyArrayJson;
		PlatUtil.ajaxProgress({
			url:url,
			params : formData,
			callback : function(resultJson) {
				if (resultJson.success) {
					parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
					PlatUtil.setData("submitSuccess",true);
					PlatUtil.closeWindow();
				} else {
					parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
				}
			}
		});
	}
}
function closeWindow(){
  PlatUtil.closeWindow();
}

</script>

</div>
</div>
  </body>
</html>

<script type="text/javascript">
$(function(){
  if("${role.ROLE_CODE}"){
    PlatUtil.changeUICompAuth("readonly","ROLE_CODE");
  }
});
</script>
