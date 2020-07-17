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
    <title>督查督办表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,nicevalid,webuploader,flowdesign,touchspin"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,nicevalid,webuploader,flowdesign,touchspin"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-north" platundragable="true" compcode="direct_layout">
   </div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="tabs-container" platundragable="true" compcode="bootstraptab" id="dcdbTab" style="width:100%;height:100%;">
	<ul class="nav nav-tabs">
		<li class="active" subtabid="tab1" onclick="PlatUtil.onBootstrapTabClick('dcdbTab','tab1','','1');"><a data-toggle="tab" href="#tab1" aria-expanded="true">督察督办表单</a></li>
	</ul>
	<div class="tab-content" platundragable="true" compcode="bootstraptab" style="height: calc(100% - 42px);">
		<div id="tab1" class="tab-pane active" style="height:100%;" platundragable="true" compcode="bootstraptab">
		<form method="post" class="form-horizontal" compcode="formcontainer" action="supervise/SuperviseController.do?saveOrUpdate" id="superform" style="">

<div class="form-group" compcode="formgroup">
  <input type="hidden" name="SUPERVISE_SOURCE" value="${supervise.SUPERVISE_SOURCE}">
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
  <input type="hidden" name="RECORD_ID" value="${supervise.RECORD_ID}">
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:select name="SUPERVISE_CLAZZ_ID" allowblank="false" auth_type="write" value="${supervise.SUPERVISE_CLAZZ_ID}" istree="false" onlyselectleaf="false" label_value="督办类型" placeholder="请选择督办类型" comp_col_num="4" label_col_num="2" dyna_interface="superviseClazzService.getAllSuperviseClazz" dyna_param="无">
   </plattag:select>
   <plattag:select name="DEPART_ID" allowblank="false" auth_type="write" value="${task.DEPART_ID}" istree="true" onlyselectleaf="false" label_value="承办部门" placeholder="请选择承办部门" comp_col_num="4" label_col_num="2" dyna_interface="commonUIService.findCompanyAndDeptSelectTree" dyna_param="[TABLE_NAME:PLAT_SYSTEM_COMPANY],[TREE_IDANDNAMECOL:COMPANY_ID,COMPANY_NAME],[TREE_QUERYFIELDS:COMPANY_PARENTID,COMPANY_PATH],[FILTERS:COMPANY_PARENTID_EQ|0]">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:select name="SUPERVISE_ITEM_ID" allowblank="false" auth_type="write" value="${supervise.SUPERVISE_ITEM_ID}" istree="false" onlyselectleaf="false" label_value="督办事项" placeholder="请选择督办事项" comp_col_num="4" label_col_num="2" dyna_interface="superviseItemService.getAllSuperviseItem" dyna_param="无">
   </plattag:select>
   <plattag:radio name="HANDLE_TYPE" allowblank="false" auth_type="write" value="${supervise.HANDLE_TYPE}" select_first="true" is_horizontal="true" label_value="办理类型" comp_col_num="4" label_col_num="2" static_values="办件:1,阅件:2">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:select name="SUPERVISE_SOURCE_ID" allowblank="false" auth_type="write" value="${supervise.SUPERVISE_SOURCE_ID}" istree="false" onlyselectleaf="false" label_value="督办来源" placeholder="请选择督办来源" comp_col_num="4" label_col_num="2" dyna_interface="superviseSourceService.findForSelect" dyna_param="无">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:textarea name="TITLE" allowblank="false" auth_type="write" value="${supervise.TITLE}" maxlength="30" label_value="主题" placeholder="请输入主题" comp_col_num="10" label_col_num="2">
   </plattag:textarea>

<script type="text/javascript">

</script>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="KEYWORDS" allowblank="false" auth_type="write" value="${supervise.KEYWORDS}" datarule="required;" maxlength="30" label_value="关键字" placeholder="请输入关键字" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:number name="HANDLE_LIMIT" allowblank="false" auth_type="write" value="${supervise.HANDLE_LIMIT}" step="1" label_value="办理时限" placeholder="请输入办理时限" comp_col_num="4" label_col_num="2">
   </plattag:number>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:textarea name="SUPERVISE_CONTENT" allowblank="false" auth_type="write" value="${supervise.SUPERVISE_CONTENT}" maxlength="510" label_value="督办内容" placeholder="请输入督办内容" comp_col_num="10" label_col_num="2">
   </plattag:textarea>

<script type="text/javascript">

</script>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
<jsp:include page="/webpages/common/plattagtpl/multifileupload_tag.jsp">
    <jsp:param name="FILESIGNLE_LIMIT" value="1048576"></jsp:param>
    <jsp:param name="ALLOW_FILEEXTS" value="doc,docx,pdf,xls,xlsx"></jsp:param>
    <jsp:param name="UPLOAD_ROOTFOLDER" value="superviseFile"></jsp:param>
    <jsp:param name="BIND_ID" value="filepicker"></jsp:param>
    <jsp:param name="BUS_TABLENAME" value="tb_supervise"></jsp:param>
    <jsp:param name="BUS_RECORDID" value="${supervise.RECORD_ID}"></jsp:param>
    <jsp:param name="FILE_TYPEKEY" value=""></jsp:param>
    <jsp:param name="FILE_RIGHTS" value=""></jsp:param>
    <jsp:param name="FILE_UPSERVER" value="1"></jsp:param>
    <jsp:param name="FILE_UPURL" value=""></jsp:param>
    <jsp:param name="IS_INIT" value="1"></jsp:param>
</jsp:include>
</div>
<div class="hr-line-dashed"></div></form></div>
	</div>
</div>

<script type="text/javascript">

</script></div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="saveDraftInfo();" platreskey="" id="" class="btn btn-outline btn-warning btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;草稿
		</button>
		<button type="button" onclick="submitBusForm();" platreskey="" id="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;提交
		</button>
		<button type="button" onclick="closeWindow();" platreskey="" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function saveDraftInfo(){
  var formData = PlatUtil.getFormEleData("superform");
	PlatUtil.ajaxProgress({
      params : formData,
		url:'/supervise/SuperviseController.do?saveDraftInfo',
		callback: function (data) {
                    if(data.success){
                        parent.layer.msg("存储草稿成功！");
                    }
                }
	});
}
function submitBusForm(){
	if(PlatUtil.isFormValid("#superform")){
		var url = $("#superform").attr("action");
		var formData = PlatUtil.getFormEleData("superform");
      var fileJson = PlatUtil.getMultiFileWebUploadValue("filepicker");
      formData.fileJson = fileJson;
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
