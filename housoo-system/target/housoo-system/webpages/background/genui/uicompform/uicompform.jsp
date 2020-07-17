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
    <title>UI组件表单</title>
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
   <form method="post" class="form-horizontal" compcode="formcontainer" action="appmodel/UiCompController.do?saveOrUpdate" id="UiCompForm" style="">

  <input type="hidden" name="COMP_ID" value="${uicomp.COMP_ID}">
<div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		基本信息
	</span>
</div><div class="form-group" compcode="formgroup">
   <plattag:select name="COMP_TYPECODE" allowblank="false" auth_type="write" value="${uicomp.COMP_TYPECODE}" istree="false" onlyselectleaf="false" label_value="组件类别" placeholder="请选择所属类别" comp_col_num="4" label_col_num="2" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'CONTROL_TYPE',ORDER_TYPE:'ASC'}">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="COMP_CODE" allowblank="false" auth_type="write" value="${uicomp.COMP_CODE}" datarule="required;onlyLetterNumberUnderLine;remote[common/baseController.do?checkUnique&amp;VALID_TABLENAME=PLAT_APPMODEL_UICOMP&amp;VALID_FIELDLABLE=组件编码&amp;VALID_FIELDNAME=COMP_CODE]" maxlength="30" label_value="组件编码" placeholder="请输入控件编码" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="COMP_NAME" allowblank="false" auth_type="write" value="${uicomp.COMP_NAME}" datarule="required;" maxlength="30" label_value="组件名称" placeholder="请输入控件名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="COMP_JAVAINTERFACE" allowblank="false" auth_type="write" value="${uicomp.COMP_JAVAINTERFACE}" datarule="required;" maxlength="60" label_value="模版处理接口" placeholder="请输入模版处理接口" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="COMP_BASEJS" allowblank="true" auth_type="write" value="${uicomp.COMP_BASEJS}" datarule="onlyLetterNumberUnderLine;" maxlength="30" label_value="基本配置函数名称" placeholder="请输入基本配置函数名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="COMP_ATTACHJS" allowblank="true" auth_type="write" value="${uicomp.COMP_ATTACHJS}" datarule="onlyLetterNumberUnderLine;" maxlength="30" label_value="附加配置函数名称" placeholder="请输入附加配置函数名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		其它信息
	</span>
</div><div class="form-group" compcode="formgroup">
   <label class="col-sm-2 control-label">组件预览:</label>
                       <div class="col-sm-10">
                          <img style="width:329px;height: 205px;" onerror="this.src='webpages/common/images/404_nophoto.png'" src="webpages/common/compdesign/${uicomp.COMP_CODE}/preview.png">
                       </div>
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
	if(PlatUtil.isFormValid("#UiCompForm")){
		var url = $("#UiCompForm").attr("action");
		var formData = PlatUtil.getFormEleData("UiCompForm");
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
    if("${uicomp.COMP_ID}"){
       PlatUtil.changeUICompAuth("readonly","COMP_CODE");
    }
});
</script>
