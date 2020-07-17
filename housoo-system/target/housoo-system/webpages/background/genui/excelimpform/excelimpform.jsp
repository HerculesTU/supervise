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
    <title>Excel配置表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,webuploader"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,webuploader"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="appmodel/ExcelImpController.do?saveOrUpdate" id="excelimpform" style="">

  <input type="hidden" name="EXCELIMP_ID" value="${excelImp.EXCELIMP_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:input name="EXCELIMP_CODE" allowblank="false" auth_type="write" value="${excelImp.EXCELIMP_CODE}" datarule="required;onlyLetterNumberUnderLine;remote[common/baseController.do?checkUnique&amp;VALID_TABLENAME=PLAT_APPMODEL_EXCELIMP&amp;VALID_FIELDLABLE=配置编码&amp;VALID_FIELDNAME=EXCELIMP_CODE]" maxlength="30" label_value="配置编码" placeholder="请输入配置编码" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="EXCELIMP_NAME" allowblank="false" auth_type="write" value="${excelImp.EXCELIMP_NAME}" datarule="required;" maxlength="30" label_value="配置名称" placeholder="请输入配置名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="EXCELIMP_TABLENAME" allowblank="false" auth_type="write" value="${excelImp.EXCELIMP_TABLENAME}" datarule="required;onlyLetterNumberUnderLine;" maxlength="30" label_value="关联表名称" placeholder="请输入关联表名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="EXCELIMP_HEADNUM" allowblank="false" auth_type="write" value="${excelImp.EXCELIMP_HEADNUM}" datarule="required;positiveInteger;" maxlength="4" label_value="表头所在行号" placeholder="请输入表头所在行号" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="EXCELIMP_INTERFACE" allowblank="false" auth_type="write" value="${excelImp.EXCELIMP_INTERFACE}" datarule="required;" maxlength="60" label_value="数据处理接口" placeholder="请输入数据处理接口" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		模版上传
	</span>
</div><jsp:include page="/webpages/common/plattagtpl/multifileupload_tag.jsp">
    <jsp:param name="FILESIGNLE_LIMIT" value="10485760"></jsp:param>
    <jsp:param name="ALLOW_FILEEXTS" value="xls,xlsx"></jsp:param>
    <jsp:param name="UPLOAD_ROOTFOLDER" value="excelimptpl"></jsp:param>
    <jsp:param name="BIND_ID" value="excelImpTpl"></jsp:param>
    <jsp:param name="BUS_TABLENAME" value="PLAT_APPMODEL_EXCELIMP"></jsp:param>
    <jsp:param name="BUS_RECORDID" value="${excelImp.EXCELIMP_ID}"></jsp:param>
    <jsp:param name="FILE_TYPEKEY" value=""></jsp:param>
    <jsp:param name="FILE_RIGHTS" value=""></jsp:param>
    <jsp:param name="FILE_UPSERVER" value="1"></jsp:param>
    <jsp:param name="FILE_UPURL" value=""></jsp:param>
</jsp:include>
</form></div>
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
	if(PlatUtil.isFormValid("#excelimpform")){
		var url = $("#excelimpform").attr("action");
		var formData = PlatUtil.getFormEleData("excelimpform");
        var TPL_FILES_JSON = PlatUtil.getMultiFileWebUploadValue("excelImpTpl");
        formData.TPL_FILES_JSON = TPL_FILES_JSON;
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
  if("${excelImp.EXCELIMP_ID}"){
     PlatUtil.changeUICompAuth("readonly","EXCELIMP_CODE");
  }
  
});
</script>
