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
    <title>门户行表单</title>
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
   <form method="post" class="form-horizontal" compcode="formcontainer" action="appmodel/PortalRowController.do?saveOrUpdate" id="portalrowform" style="">

  <input type="hidden" name="ROW_ID" value="${portalRow.ROW_ID}">
  <input type="hidden" name="ROW_THEMEID" value="${portalRow.ROW_THEMEID}">
<div class="form-group" compcode="formgroup">
   <plattag:radio name="ROW_LAYOUT" allowblank="false" auth_type="write" value="${portalRow.ROW_LAYOUT}" select_first="true" is_horizontal="true" label_value="布局方式" comp_col_num="10" label_col_num="2" static_values="均分四列:1,一比三:2,三比一:3,均分三列:4,均分两列:5,一列:6">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:number name="ROW_HEIGHT" allowblank="false" auth_type="write" value="${portalRow.ROW_HEIGHT}" step="10" decimals="0" postfix="PX" label_value="行高度" placeholder="请输入行高度" comp_col_num="4" label_col_num="2" max="1000" min="10">
   </plattag:number>
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
	if(PlatUtil.isFormValid("#portalrowform")){
		var url = $("#portalrowform").attr("action");
		var formData = PlatUtil.getFormEleData("portalrowform");
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
