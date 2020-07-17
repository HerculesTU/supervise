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
    <title>门户组件表单</title>
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
   <form method="post" class="form-horizontal" compcode="formcontainer" action="appmodel/PortalCompController.do?saveOrUpdate" id="portalcompform" style="">

  <input type="hidden" name="COMP_ID" value="${portalComp.COMP_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:select name="COMP_TYPECODE" allowblank="false" auth_type="write" value="${portalComp.COMP_TYPECODE}" istree="false" onlyselectleaf="false" label_value="组件类别" placeholder="请选择组件类别" comp_col_num="4" label_col_num="2" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'COMPTYPE',ORDER_TYPE:'ASC'}">
   </plattag:select>
   <plattag:input name="COMP_NAME" allowblank="false" auth_type="write" value="${portalComp.COMP_NAME}" datarule="required;" maxlength="30" label_value="组件名称" placeholder="请输入组件名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="COMP_URL" allowblank="false" auth_type="write" value="${portalComp.COMP_URL}" datarule="required;" maxlength="200" label_value="组件URL" placeholder="请输入组件URL" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:select name="COMP_MORERESID" allowblank="true" auth_type="write" value="${portalComp.COMP_MORERESID}" istree="false" onlyselectleaf="false" label_value="更多指向菜单" placeholder="请选择查看更多菜单" comp_col_num="4" label_col_num="2" dyna_interface="resService.findUrlList" dyna_param="无">
   </plattag:select>
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
	if(PlatUtil.isFormValid("#portalcompform")){
		var url = $("#portalcompform").attr("action");
		var formData = PlatUtil.getFormEleData("portalcompform");
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

</script>
