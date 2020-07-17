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
    <title>全局配置表单</title>
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
   <div class="ui-layout-north" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;保存
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
	if(PlatUtil.isFormValid("#globalform")){
		var url = $("#globalform").attr("action");
		var formData = PlatUtil.getFormEleData("globalform");
		PlatUtil.ajaxProgress({
			url:url,
			params : formData,
			callback : function(resultJson) {
				if (resultJson.success) {
             if(resultJson.msg){
               parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
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
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="common/baseController.do?saveOrUpdateSingle&amp;tableName=PLAT_SYSTEM_GLOBALCONFIG&amp;busDesc=全局配置" id="globalform" style="">

<div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		全局配置信息
	</span>
</div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONFIG_PROJECTNAME" allowblank="false" auth_type="write" value="${globalConfig.CONFIG_PROJECTNAME}" datarule="required;" maxlength="62" label_value="系统名称" placeholder="请输入系统名称" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONFIG_LOGOURL" allowblank="false" auth_type="write" value="${globalConfig.CONFIG_LOGOURL}" datarule="required;" maxlength="254" label_value="LOGO图片地址" placeholder="请输入LOGO图片地址" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONFIG_LOGINURL" allowblank="false" auth_type="write" value="${globalConfig.CONFIG_LOGINURL}" datarule="required;" maxlength="510" label_value="后台登录界面地址" placeholder="请输入登录界面地址" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONFIG_FAVICONURL" allowblank="true" auth_type="write" value="${globalConfig.CONFIG_FAVICONURL}" maxlength="500" label_value="系统favicon路径" placeholder="请输入系统favicon路径" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div>  <input type="hidden" name="CONFIG_ID" value="${globalConfig.CONFIG_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:radio name="CONFIG_CHATABLE" allowblank="false" auth_type="write" value="${globalConfig.CONFIG_CHATABLE}" select_first="true" is_horizontal="true" label_value="开启即时通讯" comp_col_num="4" label_col_num="2" static_values="开启:1,停用:-1">
   </plattag:radio>
   <plattag:radio name="CONFIG_BACKVALIDCODE" allowblank="false" auth_type="write" value="${globalConfig.CONFIG_BACKVALIDCODE}" select_first="false" is_horizontal="true" label_value="后台登录验证码" comp_col_num="4" label_col_num="2" static_values="启用:1,停用:-1">
   </plattag:radio>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:radio name="CONFIG_ALOWSAMELOGIN" allowblank="false" auth_type="write" value="${globalConfig.CONFIG_ALOWSAMELOGIN}" select_first="true" is_horizontal="true" label_value="相同账号重复登录" comp_col_num="4" label_col_num="2" static_values="不允许:-1,允许:1">
   </plattag:radio>
   <plattag:number name="CONFIG_PWDERRORNUM" allowblank="false" auth_type="write" value="${globalConfig.CONFIG_PWDERRORNUM}" step="1" decimals="0" label_value="密码输入错误次数" placeholder="请输入密码输入错误次数" comp_col_num="4" label_col_num="2" min="1">
   </plattag:number>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:radio name="CONFIG_FIRST_LOGIN_MODIFY_PWD" allowblank="true" auth_type="write" value="${globalConfig.CONFIG_FIRST_LOGIN_MODIFY_PWD}" select_first="true" is_horizontal="true" label_value="首次登录修改密码" comp_col_num="4" label_col_num="2" static_values="是:1,否:2">
   </plattag:radio>
   <plattag:number name="CONFIG_PWDOVERTIME" allowblank="true" auth_type="write" value="${globalConfig.CONFIG_PWDOVERTIME}" step="1" label_value="用户密码有效期（月）" placeholder="请输入用户密码有效期（月份，默认值0，表示永久有效）" comp_col_num="4" label_col_num="2" min="0">
   </plattag:number>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="CONFIG_DEFAULT_PWD" allowblank="false" auth_type="write" value="${globalConfig.CONFIG_DEFAULT_PWD}" datarule="required;onlyLetterNumberUnderLine;" maxlength="16" label_value="用户默认密码" placeholder="请输入用户默认密码" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div></form></div>
</div>
  </body>
</html>

<script type="text/javascript">
var backValidCode = ${globalConfig.CONFIG_BACKVALIDCODE};
 $('input[name=CONFIG_BACKVALIDCODE]').change(function() {
        if ("1"===this.value) {
           PlatUtil.openWindow({
	           title : "请选择验证码类型",
	           area: ["500px","300px"],
	           content: "system/GlobalConfigController.do?goForm&UI_DESIGNCODE=config_verify_code_type",
	           end:function(){
		 
	            }
	        });
        }
    });
</script>
