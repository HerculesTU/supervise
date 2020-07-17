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
    
    <title>My JSP 'dbmanager_view.jsp' starting page</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,plat-ui,nicevalid">
	</plattag:resources>
  </head>
  
  <body>
   <div class="plat-directlayout" style="height: 100%;width: 100%;">
      <div class="ui-layout-center" style="overflow-y:auto; ">
  		     <form method="post" class="form-horizontal" action="appmodel/ModuleController.do?saveOrUpdate" id="ModuleForm">
  		           <input type="hidden" name="MODULE_ID" value="${module.MODULE_ID}">
  		           <input type="hidden" name="MODULE_PARENTID" value="${module.MODULE_PARENTID}">
  		           <div class="form-group plat-form-title">
                        <span class="plat-current">
							基本信息
						</span>
				   </div>
				   <div class="form-group">
				      <plattag:input name="PARENT_NAME" auth_type="readonly" allowblank="false" 
				       placeholder="" comp_col_num="4" value="${module.PARENT_NAME}"
				       label_col_num="2" label_value="上级模块"
				       ></plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
               	   <div class="form-group">
               	      <plattag:input name="MODULE_CODE" auth_type="${module.MODULE_ID!=null?'readonly':'write'}" 
               	       allowblank="false" maxlength="30"
				       placeholder="请输入模块编码" comp_col_num="4" value="${module.MODULE_CODE}"
				       label_col_num="2" label_value="模块编码" 
				       datarule="${module.MODULE_ID==null?'required;onlyLetterNumberUnderLine;remote[common/baseController.do?checkUnique&VALID_TABLENAME=PLAT_APPMODEL_MODULE&VALID_FIELDLABLE=模块编码&VALID_FIELDNAME=MODULE_CODE]':''}"
				       ></plattag:input>
				       <plattag:input name="MODULE_NAME" auth_type="write" allowblank="false" 
				       placeholder="请输入模块名称" comp_col_num="4" value="${module.MODULE_NAME}"
				       label_col_num="2" label_value="模块名称" maxlength="30" datarule="required;"
				       ></plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
              </form>
      </div>
      <div class="ui-layout-south">
		   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
                   <div class="col-sm-12 text-right">
                       <button class="btn btn-outline btn-primary btn-sm" onclick="submitModuleForm();" type="button" ><i class="fa fa-check"></i>提交</button>
                       <button class="btn btn-outline btn-danger btn-sm" onclick="PlatUtil.closeWindow();" type="button"><i class="fa fa-times"></i>关闭</button>
                   </div>
           </div>
	  </div>
	 </div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,jquery-layout,plat-util,nicevalid">
</plattag:resources>
<script type="text/javascript">

function submitModuleForm(){
	$("#ModuleForm").trigger("validate"); 
	$("#ModuleForm").isValid(function(valid){
		if(valid){
			var url = $("#ModuleForm").attr("action");
			var formData = PlatUtil.getFormEleData("ModuleForm");
			PlatUtil.ajaxProgress({
				url:url,
				params : formData,
				callback : function(resultJson) {
					if (resultJson.success) {
						//parent.layer.alert(resultJson.msg,{icon: 1,resize:false});
						parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
						PlatUtil.setData("submitSuccess",true);
						PlatUtil.closeWindow();
					} else {
						parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
					}
				}
			});
		}
	});
}

$(function(){
	//初始化UI控件
	PlatUtil.initUIComp();
});
</script>
