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
    <title>部门信息表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,touchspin"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,touchspin"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="metadata/DataResController.do?invokeRes&amp;PLAT_RESCODE=system_dep01" id="DepartForm" style="">

  <input type="hidden" name="DEPART_ID" value="${depart.DEPART_ID}">
  <input type="hidden" name="DEPART_PARENTID" value="${depart.DEPART_PARENTID}">
  <input type="hidden" name="DEPART_COMPANYID" value="${depart.DEPART_COMPANYID}">
<div class="form-group" compcode="formgroup">
   <plattag:input name="DEPART_COMPANYNAME" allowblank="false" auth_type="readonly" value="${depart.DEPART_COMPANYNAME}" datarule="required;" label_value="所属单位" placeholder="所属单位" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="DEPART_PARENTNAME" allowblank="false" auth_type="readonly" value="${depart.DEPART_PARENTNAME}" datarule="required;" label_value="上级部门" placeholder="请输入上级部门" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="DEPART_NAME" allowblank="false" auth_type="write" value="${depart.DEPART_NAME}" datarule="required;" maxlength="30" label_value="部门名称" placeholder="请输入部门名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="DEPART_CODE" allowblank="true" auth_type="write" value="${depart.DEPART_CODE}" datarule="onlyLetterNumberUnderLine;remote[system/DepartController.do?checkDepcode&amp;DEPART_COMPANYID=${depart.DEPART_COMPANYID}]" maxlength="30" label_value="部门编码" placeholder="请输入部门编码" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:number name="DEPART_TREESN" allowblank="false" auth_type="write" value="${depart.DEPART_TREESN}" step="1" decimals="0" label_value="部门排序" placeholder="请输入同级排序" comp_col_num="4" label_col_num="2" max="1000000" min="1">
   </plattag:number>
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
	if(PlatUtil.isFormValid("#DepartForm")){
		var url = $("#DepartForm").attr("action");
		var formData = PlatUtil.getFormEleData("DepartForm");
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
             if(resultJson.msg){
               parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
             }else{
               parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
             }
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
    if("${depart.DEPART_CODE}"){
       PlatUtil.changeUICompAuth("readonly","DEPART_CODE");
     
    }
  	 if(!"${depart.DEPART_ID}"){
      PlatUtil.changeUICompAuth("hidden","DEPART_TREESN");   
     }
});
</script>
