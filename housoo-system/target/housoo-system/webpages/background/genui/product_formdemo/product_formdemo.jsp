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
    <title>产品表单</title>
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
   <form method="post" class="form-horizontal" compcode="formcontainer" action="demo/ProductController.do?saveOrUpdate" id="productform" style="">

  <input type="hidden" name="PRODUCT_ID" value="${product.PRODUCT_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:input name="PRODUCT_NAME" allowblank="false" auth_type="write" value="${product.PRODUCT_NAME}" datarule="required;" maxlength="14" label_value="产品名称" placeholder="请输入产品名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="PRODUCT_CODE" allowblank="false" auth_type="write" value="${product.PRODUCT_CODE}" datarule="required;" maxlength="14" label_value="产品编码" placeholder="请输入产品编码" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:number name="PRODUCT_PRICE" allowblank="false" auth_type="write" value="${product.PRODUCT_PRICE}" step="0.01" decimals="2" label_value="产品价格" placeholder="请输入产品价格" comp_col_num="4" label_col_num="2">
   </plattag:number>
   <plattag:datetime name="PRODUCT_CREATETIME" allowblank="false" auth_type="write" value="${product.PRODUCT_CREATETIME}" format="YYYY-MM-DD" istime="false" label_value="入库时间" placeholder="请选择入库时间" label_col_num="2" comp_col_num="4" defaultnow="-1">
   </plattag:datetime>

<script type="text/javascript">
   
</script>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:textarea name="PRODUCT_DESC" allowblank="true" auth_type="write" value="${product.PRODUCT_DESC}" maxlength="998" label_value="产品描述" placeholder="请输入产品描述" comp_col_num="10" label_col_num="2">
   </plattag:textarea>

<script type="text/javascript">

</script>
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
	if(PlatUtil.isFormValid("#productform")){
		var url = $("#productform").attr("action");
		var formData = PlatUtil.getFormEleData("productform");
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
