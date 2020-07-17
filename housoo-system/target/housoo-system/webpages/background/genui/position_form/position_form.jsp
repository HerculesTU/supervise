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
    <title>岗位表单</title>
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
   <form method="post" class="form-horizontal" compcode="formcontainer" action="metadata/DataResController.do?invokeRes&amp;PLAT_RESCODE=system_pos_save" id="baseform" style="">

  <input type="hidden" name="POSITION_ID" value="${position.POSITION_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:select name="POSITION_CID" allowblank="false" auth_type="write" value="${position.POSITION_CID}" istree="true" onlyselectleaf="false" label_value="所属单位" placeholder="请选择单位" comp_col_num="4" label_col_num="2" dyna_interface="commonUIService.findGenTreeSelectorDatas" dyna_param="[TABLE_NAME:PLAT_SYSTEM_COMPANY],[TREE_IDANDNAMECOL:COMPANY_ID,COMPANY_NAME],[TREE_QUERYFIELDS:COMPANY_PARENTID,COMPANY_PATH],[FILTERS:COMPANY_PARENTID_EQ|0]">
   </plattag:select>
   <plattag:input name="POSITION_NAME" allowblank="false" auth_type="write" value="${position.POSITION_NAME}" datarule="required;" maxlength="30" label_value="岗位名称" placeholder="请输入岗位名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:select name="POSITION_LEVEL" allowblank="false" auth_type="write" value="${position.POSITION_LEVEL}" istree="false" onlyselectleaf="false" label_value="岗位等级" placeholder="请选择岗位等级" comp_col_num="4" label_col_num="2" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'positionlevel',ORDER_TYPE:'ASC'}">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:textarea name="POSITION_DESC" allowblank="true" auth_type="write" value="${position.POSITION_DESC}" maxlength="254" label_value="岗位描述" placeholder="请输入岗位描述" comp_col_num="10" label_col_num="2">
   </plattag:textarea>

<script type="text/javascript">

</script>
</div>
<div class="hr-line-dashed"></div></form></div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;提交
		</button>
		<button type="button" onclick="closeWindow();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
	if(PlatUtil.isFormValid("#baseform")){
		var url = $("#baseform").attr("action");
		var formData = PlatUtil.getFormEleData("baseform");
		PlatUtil.ajaxProgress({
			url:url,
			params : formData,
			callback : function(resultJson) {
				if (resultJson.success) {
             if(resultJson.msg){
               parent.layer.msg(resultJson.msg, {icon: 1});
             }else{
               parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
             }
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

</script>
