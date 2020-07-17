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
    <title>系统用户表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,fancybox,webuploader"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,fancybox,webuploader"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="metadata/DataResController.do?invokeRes&amp;PLAT_RESCODE=system_sysuser_addorupdate" id="SysUserForm" style="">

  <input type="hidden" name="SYSUSER_ID" value="${sysUser.SYSUSER_ID}">
<div class="form-group" compcode="formgroup">
<div class="col-sm-9" compcode="collayout" id="">
<div class="form-group" compcode="formgroup">
   <plattag:select name="SYSUSER_COMPANYID" allowblank="false" auth_type="write" value="${sysUser.SYSUSER_COMPANYID}" istree="true" onlyselectleaf="false" label_value="所在单位" placeholder="请选择用户所在单位" comp_col_num="4" label_col_num="2" dyna_interface="commonUIService.findGenTreeSelectorDatas" dyna_param="[TABLE_NAME:PLAT_SYSTEM_COMPANY],[TREE_IDANDNAMECOL:COMPANY_ID,COMPANY_NAME],[TREE_QUERYFIELDS:COMPANY_PARENTID,COMPANY_PATH],[FILTERS:COMPANY_PARENTID_EQ|0]
">
   </plattag:select>
   <plattag:select name="SYSUSER_DEPARTID" allowblank="true" auth_type="write" value="${sysUser.SYSUSER_DEPARTID}" istree="true" onlyselectleaf="false" label_value="所在部门" placeholder="请选择用户所在部门" comp_col_num="4" label_col_num="2" dyna_interface="departService.findSelectTree" dyna_param="${sysUser.SYSUSER_COMPANYID}">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:select name="SYSUSER_GROUPID" allowblank="true" auth_type="write" value="${sysUser.SYSUSER_GROUPID}" istree="false" onlyselectleaf="false" label_value="用户组" placeholder="请选择所在用户组" comp_col_num="4" label_col_num="2" dyna_interface="sysUserGroupService.findSelectList" dyna_param="${sysUser.SYSUSER_COMPANYID}">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="SYSUSER_ACCOUNT" allowblank="false" auth_type="write" value="${sysUser.SYSUSER_ACCOUNT}" datarule="required;onlyLetterNumberUnderLine;remote[common/baseController.do?checkUnique&amp;VALID_TABLENAME=PLAT_SYSTEM_SYSUSER&amp;VALID_FIELDLABLE=用户账号&amp;VALID_FIELDNAME=SYSUSER_ACCOUNT]" maxlength="30" label_value="用户账号" placeholder="请输入用户账号" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="SYSUSER_NAME" allowblank="false" auth_type="write" value="${sysUser.SYSUSER_NAME}" datarule="required;" maxlength="30" label_value="用户姓名" placeholder="请输入用户姓名" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="SYSUSER_MOBILE" allowblank="true" auth_type="write" value="${sysUser.SYSUSER_MOBILE}" datarule="mobile;" maxlength="14" label_value="手机号" placeholder="请输入手机号" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:radio name="SYSUSER_SEX" allowblank="true" auth_type="write" value="${sysUser.SYSUSER_SEX}" select_first="true" is_horizontal="true" label_value="性别" comp_col_num="4" label_col_num="2" static_values="男:1,女:-1">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:winselector name="SYSUSER_ROLEIDS" allowblank="false" auth_type="write" value="${sysUser.SYSUSER_ROLEIDS}" selectorurl="system/SysUserController.do?goRoleGrant&amp;USER_ID=${sysUser.SYSUSER_ID}" title="角色选择器" width="80%" height="70%" selectedlabels="${sysUser.SYSUSER_ROLENAMES}" datarule="required;" maxselect="100" minselect="1" label_value="选择角色" placeholder="请选择角色" comp_col_num="10" label_col_num="2">
   </plattag:winselector>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:winselector name="SYSUSER_POSIDS" allowblank="true" auth_type="write" value="${sysUser.SYSUSER_POSIDS}" selectorurl="system/PositionController.do?goSelect&amp;USER_ID=${sysUser.SYSUSER_ID}" title="岗位选择器" width="90%" height="90%" selectedlabels="${sysUser.SYSUSER_POSNAMES}" maxselect="5" minselect="1" label_value="所处岗位" placeholder="请选择所处岗位" comp_col_num="10" label_col_num="2">
   </plattag:winselector>
</div>
<div class="hr-line-dashed"></div></div><div class="col-sm-3" compcode="collayout" id="">
<div class="imageUploadDiv platSingleImgUploadDiv" compcode="singleimgupload" filesinglesizelimit="1048576" style="width:100px;height:100px;" uploadrootfolder="sysuser">
<jsp:include page="/webpages/common/plattagtpl/singleimgupload_tag.jsp">
    <jsp:param name="PLAT_COMPNAME" value="用户头像"></jsp:param>
    <jsp:param name="FORMCONTROL_ID" value="402881e65b62338e015b626ad2b700b1"></jsp:param>
    <jsp:param name="FILESIGNLE_LIMIT" value="1048576"></jsp:param>
    <jsp:param name="IMG_WIDTH" value="100"></jsp:param>
    <jsp:param name="IMG_HEIGHT" value="100"></jsp:param>
    <jsp:param name="UPLOAD_ROOTFOLDER" value="sysuser"></jsp:param>
    <jsp:param name="DEFAULT_IMGPATH" value="plug-in/platform-1.0/images/defaultuserphoto.png"></jsp:param>
    <jsp:param name="BIND_ID" value="picker"></jsp:param>
    <jsp:param name="BUS_TABLENAME" value="PLAT_SYSTEM_SYSUSER"></jsp:param>
    <jsp:param name="BUS_RECORDID" value="${sysUser.SYSUSER_ID}"></jsp:param>
    <jsp:param name="FILE_TYPEKEY" value="photo"></jsp:param>
</jsp:include>

</div></div></div>
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
	if(PlatUtil.isFormValid("#SysUserForm")){
		var url = $("#SysUserForm").attr("action");
		var formData = PlatUtil.getFormEleData("SysUserForm");
      var USER_PHOTO_JSON = PlatUtil.getSingleImgWebUploaderValue("picker");
      var USER_FILES_JSON = PlatUtil.getMultiFileWebUploadValue("userfilelist");
      formData.USER_PHOTO_JSON = USER_PHOTO_JSON;
      formData.USER_FILES_JSON = USER_FILES_JSON;
      var formfieldModifyArrayJson = PlatUtil.getFormFieldValueModifyArrayJSON();
      formData.formfieldModifyArrayJson = formfieldModifyArrayJson;
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
  if("${sysUser.SYSUSER_ID}"){
     PlatUtil.changeUICompAuth("readonly","SYSUSER_ACCOUNT");
  }
  $("select[name='SYSUSER_COMPANYID']").change(function() {
		var SYSUSER_COMPANYID = PlatUtil.getSelectAttValue("SYSUSER_COMPANYID","value");
        PlatUtil.reloadSelect("SYSUSER_DEPARTID",{
			dyna_param:SYSUSER_COMPANYID
	   });
    PlatUtil.reloadSelect("SYSUSER_GROUPID",{
			dyna_param:SYSUSER_COMPANYID
	   });
	});
});
</script>
