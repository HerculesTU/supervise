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
    <title>门户组件配置表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,ratingstar"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,ratingstar"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="appmodel/PortalRowConfController.do?saveOrUpdate" id="compconfform" style="">

  <input type="hidden" name="CONF_ID" value="${portalRowconf.CONF_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:input name="CONF_TITLE" allowblank="false" auth_type="write" value="${portalRowconf.CONF_TITLE}" datarule="required;" maxlength="14" label_value="标题" placeholder="请输入标题" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:select name="CONF_BORDERCOLOR" allowblank="false" auth_type="write" value="${portalRowconf.CONF_BORDERCOLOR}" istree="false" onlyselectleaf="false" label_value="边框颜色" placeholder="请选择边框颜色" comp_col_num="4" label_col_num="2" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'compbordercolor',ORDER_TYPE:'ASC'}">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:select name="CONF_COMPTYPECODE" allowblank="false" auth_type="write" value="${portalRowconf.CONF_COMPTYPECODE}" istree="false" onlyselectleaf="false" label_value="组件类别" placeholder="请选择组件类别" comp_col_num="4" label_col_num="2" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'COMPTYPE',ORDER_TYPE:'ASC'}">
   </plattag:select>
   <plattag:select name="CONF_COMPID" allowblank="false" auth_type="write" value="${portalRowconf.CONF_COMPID}" istree="false" onlyselectleaf="false" label_value="组件" placeholder="请选择组件" comp_col_num="4" label_col_num="2" dyna_interface="portalCompService.findByCompTypeCode" dyna_param="${portalRowconf.CONF_COMPTYPECODE}">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div>  <input type="hidden" name="THEME_ID" value="${portalRowconf.THEME_ID}">
  <input type="hidden" name="CONF_COMPURL" value="${portalRowconf.CONF_COMPURL}">
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
	if(PlatUtil.isFormValid("#compconfform")){
		var url = $("#compconfform").attr("action");
		var formData = PlatUtil.getFormEleData("compconfform");
		PlatUtil.ajaxProgress({
			url:url,
			params : formData,
			callback : function(resultJson) {
				if (resultJson.success) {
					parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
                    PlatUtil.setData("compConf",formData);
					PlatUtil.setData("submitSuccess",true);
					PlatUtil.closeWindow();
				} else {
					parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
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
  $("select[name='CONF_COMPTYPECODE']").change(function() {
		var CONF_COMPTYPECODE = PlatUtil.getSelectAttValue("CONF_COMPTYPECODE","value");
        PlatUtil.reloadSelect("CONF_COMPID",{
			dyna_param:CONF_COMPTYPECODE
	    });
	});
  $("select[name='CONF_COMPID']").change(function() {
		var COMP_URL = PlatUtil.getSelectAttValue("CONF_COMPID","COMP_URL");
        if(COMP_URL){
            $("input[name='CONF_COMPURL']").val(COMP_URL);
        }
	});
});
</script>
