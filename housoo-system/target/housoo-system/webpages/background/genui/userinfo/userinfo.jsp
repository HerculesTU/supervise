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
    <title>用户个人信息</title>
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
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="tabs-container" platundragable="true" compcode="bootstraptab" id="userinfotab" style="width:100%;height:100%;">
	<ul class="nav nav-tabs">
		<li class="active" subtabid="tab1" onclick="PlatUtil.onBootstrapTabClick('userinfotab','tab1','','1');"><a data-toggle="tab" href="#tab1" aria-expanded="true">基本信息</a></li>
		<li class="" subtabid="tab2" onclick="PlatUtil.onBootstrapTabClick('userinfotab','tab2','','-1');"><a data-toggle="tab" href="#tab2" aria-expanded="false">密码修改</a></li>
		<li class="" subtabid="tab3" onclick="PlatUtil.onBootstrapTabClick('userinfotab','tab3','','-1');"><a data-toggle="tab" href="#tab3" aria-expanded="false">主题设置</a></li>
	</ul>
	<div class="tab-content" platundragable="true" compcode="bootstraptab" style="height: calc(100% - 42px);">
		<div id="tab1" class="tab-pane active" style="height:100%;" platundragable="true" compcode="bootstraptab">
		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-north" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;保存
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
	if(PlatUtil.isFormValid("#userinfoform")){
		var url = $("#userinfoform").attr("action");
		var formData = PlatUtil.getFormEleData("userinfoform");
        var USER_PHOTO_JSON = PlatUtil.getSingleImgWebUploaderValue("picker");
        formData.USER_PHOTO_JSON = USER_PHOTO_JSON;
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

</script>

</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="system/SysUserController.do?updateUserInfo" id="userinfoform" style="">

  <input type="hidden" name="SYSUSER_ID" value="${sysUser.SYSUSER_ID}">
<div class="form-group" compcode="formgroup">
<div class="col-sm-9" compcode="collayout" id="">
<div class="form-group" compcode="formgroup">
   <plattag:input name="COMPANY_NAME" allowblank="false" auth_type="readonly" value="${sysUser.COMPANY_NAME}" datarule="required;" label_value="所在单位" placeholder="请输入所在单位" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="DEPART_NAME" allowblank="true" auth_type="readonly" value="${sysUser.DEPART_NAME}" label_value="所在部门" placeholder="请输入所在部门" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="SYSUSER_ACCOUNT" allowblank="false" auth_type="readonly" value="${sysUser.SYSUSER_ACCOUNT}" datarule="required;" maxlength="30" label_value="用户账号" placeholder="请输入用户账号" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="SYSUSER_NAME" allowblank="false" auth_type="readonly" value="${sysUser.SYSUSER_NAME}" datarule="required;" maxlength="30" label_value="用户姓名" placeholder="请输入用户姓名" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="SYSUSER_MOBILE" allowblank="false" auth_type="write" value="${sysUser.SYSUSER_MOBILE}" datarule="required;mobile;" maxlength="20" label_value="手机号" placeholder="请输入用户的手机号" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:radio name="SYSUSER_SEX" allowblank="true" auth_type="write" value="${sysUser.SYSUSER_SEX}" select_first="true" is_horizontal="true" label_value="性别" comp_col_num="4" label_col_num="2" static_values="男:1,女:-1">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="USERGROUP_NAME" allowblank="true" auth_type="readonly" value="${sysUser.USERGROUP_NAME}" maxlength="14" label_value="所在用户组" placeholder="请输入所在用户组" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="SYSUSER_POSNAMES" allowblank="true" auth_type="readonly" value="${sysUser.SYSUSER_POSNAMES}" maxlength="30" label_value="所在岗位" placeholder="所在岗位" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div></div><div class="col-sm-3" compcode="collayout" id="">
<div class="imageUploadDiv platSingleImgUploadDiv" compcode="singleimgupload" filesinglesizelimit="5242880" style="width:100px;height:100px;" uploadrootfolder="sysuser">
<jsp:include page="/webpages/common/plattagtpl/singleimgupload_tag.jsp">
    <jsp:param name="FILESIGNLE_LIMIT" value="5242880"></jsp:param>
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
</div></div>
		<div id="tab2" class="tab-pane " style="height:100%;" platundragable="true" compcode="bootstraptab">
		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-north" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="updatePassword();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;保存
		</button>
     </div>
</div>

<script type="text/javascript">
function updatePassword(){
	if(PlatUtil.isFormValid("#passform")){
		var url = $("#passform").attr("action");
		var formData = PlatUtil.getFormEleData("passform");
		PlatUtil.ajaxProgress({
			url:url,
			params : formData,
			callback : function(resultJson) {
				if (resultJson.success) {
					parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
					PlatUtil.setData("submitSuccess",true);
					PlatUtil.closeWindow();
				} else {
					parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
				}
			}
		});
	}
}

</script>

</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="system/SysUserController.do?updatePass" id="passform" style="">

<div class="form-group" compcode="formgroup">
   <input type="password" style="display: none;">
   <plattag:input name="OLD_PASSWORD" allowblank="false" auth_type="write" value="" ispass="true" datarule="required;onlyLetterNumberUnderLine;" maxlength="10" label_value="原密码" placeholder="请输入原密码" comp_col_num="6" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="NEW_PASSWORD" allowblank="false" auth_type="write" value="" ispass="true" datarule="required;onlyLetterNumberUnderLine;" maxlength="10" label_value="新密码" placeholder="请输入新密码" comp_col_num="6" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONFIRM_PASSWORD" allowblank="false" auth_type="write" value="" ispass="true" datarule="required;" maxlength="10" label_value="确认新密码" placeholder="请再次输入密码" comp_col_num="6" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div></form></div>
</div></div>
		<div id="tab3" class="tab-pane " style="height:100%;" platundragable="true" compcode="bootstraptab">
		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-north" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="saveThemeConfig();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;保存
		</button>
     </div>
</div>

<script type="text/javascript">
function saveThemeConfig(){
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

</script>

</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="system/SysUserController.do?updateTheme" id="baseform" style="">

  <input type="hidden" name="SYSUSER_ID" value="${sysUser.SYSUSER_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:radio name="SYSUSER_MENUTYPE" allowblank="false" auth_type="write" value="${sysUser.SYSUSER_MENUTYPE}" select_first="true" is_horizontal="true" label_value="菜单风格" comp_col_num="10" label_col_num="2" static_values="门户网站风格:1,传统经典风格:2">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:radio name="SYSUSER_THEMECOLOR" allowblank="false" auth_type="write" value="${sysUser.SYSUSER_THEMECOLOR}" select_first="true" is_horizontal="true" label_value="主题颜色" comp_col_num="10" label_col_num="2" static_values="蓝色:blue,深色:dark,红色:red,绿色:green,棕色:brown,紫色:purple,粉红色:pink">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div></form></div>
</div></div>
	</div>
</div>

<script type="text/javascript">



</script></div>
</div>
  </body>
</html>

<script type="text/javascript">

</script>
