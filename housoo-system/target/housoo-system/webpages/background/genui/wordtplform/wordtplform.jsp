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
    <title>WORD模版表单</title>
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
   <form method="post" class="form-horizontal" compcode="formcontainer" action="appmodel/WordTplController.do?saveOrUpdate" id="wordtplform" style="">

  <input type="hidden" name="TPL_ID" value="${wordTpl.TPL_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:input name="TPL_CODE" allowblank="false" auth_type="write" value="${wordTpl.TPL_CODE}" datarule="required;onlyLetterNumberUnderLine;remote[common/baseController.do?checkUnique&amp;VALID_TABLENAME=PLAT_APPMODEL_WORDTPL&amp;VALID_FIELDLABLE=模版编码&amp;VALID_FIELDNAME=TPL_CODE]" maxlength="30" label_value="模版编码" placeholder="请输入模版编码" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="TPL_NAME" allowblank="false" auth_type="write" value="${wordTpl.TPL_NAME}" datarule="required;" maxlength="62" label_value="模版名称" placeholder="请输入模版名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="TPL_INTER" allowblank="false" auth_type="write" value="${wordTpl.TPL_INTER}" datarule="required;" maxlength="120" label_value="数据回填接口" placeholder="请输入数据回填接口" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
<label class="col-sm-2 control-label">
  <font class="redDot">*</font>
  模版内容：
</label>
<div class="col-sm-10">
   <input type="hidden" name="TPL_CONTENT" id="TPL_CONTENT" value="${wordTpl.TPL_CONTENT}" isewebeditor="true"> 
   <iframe id="TPL_CONTENT_EWEB" src="plug-in/ewebeditor-10.8/ewebeditor.htm?id=TPL_CONTENT&amp;style=mini500&amp;skin=flat3" frameborder="0" scrolling="no" width="100%" height="450"></iframe>
</div>
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
	if(PlatUtil.isFormValid("#wordtplform")){
		var url = $("#wordtplform").attr("action");
		var formData = PlatUtil.getFormEleData("wordtplform");
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
  if("${wordTpl.TPL_ID}"){
     PlatUtil.changeUICompAuth("readonly","TPL_CODE");
  }
});
</script>
