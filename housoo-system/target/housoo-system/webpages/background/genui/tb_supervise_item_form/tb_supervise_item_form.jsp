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
    <title>督办事项分类表单</title>
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
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="supervise/SuperviseItemController.do?saveOrUpdate" id="superviseItem" style="">

  <input type="hidden" name="RECORD_ID" value="${superviseItem.RECORD_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:input name="ITEM_NAME" allowblank="false" auth_type="write" value="${superviseItem.ITEM_NAME}" datarule="required;" maxlength="60" label_value="督办事项" placeholder="请输入督办事项" comp_col_num="6" label_col_num="3">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="REMARKS" allowblank="true" auth_type="write" value="${superviseItem.REMARKS}" maxlength="248" label_value="备注信息" placeholder="请输入备注信息" comp_col_num="6" label_col_num="3">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:radio name="DEL_FLAG" allowblank="true" auth_type="write" value="${superviseItem.DEL_FLAG}" select_first="true" is_horizontal="true" label_value="删除标记" comp_col_num="6" label_col_num="3" static_values="正常:1,删除:2">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div></form></div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" platreskey="" id="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;提交
		</button>
		<button type="button" onclick="closeWindow();" platreskey="" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
	if(PlatUtil.isFormValid("#superviseItem")){
		var url = $("#superviseItem").attr("action");
		var formData = PlatUtil.getFormEleData("superviseItem");
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
