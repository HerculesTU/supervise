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
      <div class="ui-layout-center" style="overflow-y:auto; ">
  		     <form method="post" class="form-horizontal" action="appmodel/DesignController.do?saveAssocial" id="AssocialForm">
  		           <div class="form-group plat-form-title" >
                        <span class="plat-current">
							基本信息
						</span>
				   </div>
               	   <div class="form-group">
               	      <plattag:select name="TABLE_NAME" auth_type="write" dyna_interface="dbManagerService.findAllTables"
               	          dyna_param="" 
               	          istree="false" allowblank="false" placeholder="请选择数据库表" 
               	          comp_col_num="4" label_col_num="2" label_value="数据库表" >
               	      </plattag:select>
               	      <plattag:input name="TABLE_ALIAS" allowblank="false" maxlength="30"
               	        auth_type="write" placeholder="请输入表别名" comp_col_num="4"
               	       label_col_num="2" label_value="表别名" datarule="required;onlyLetterNumberUnderLine;"
               	      >
               	      </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                      <plattag:input name="PACK_NAME" allowblank="false" maxlength="30"
               	        auth_type="write" placeholder="请输入模块包名" comp_col_num="4"
               	       label_col_num="2" label_value="模块包名" datarule="required;letters;"
               	      >
               	      </plattag:input>
               	      <plattag:input name="CLASS_NAME" allowblank="false" maxlength="30"
               	        auth_type="write" placeholder="请输入实体类名" comp_col_num="4"
               	       label_col_num="2" label_value="实体类名" datarule="required;letters;"
               	      >
               	      </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   
              </form>
      </div>
      <div class="ui-layout-south">
		   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
                   <div class="col-sm-12 text-right">
                       <button class="btn btn-outline btn-primary btn-sm" onclick="submitAssocialForm();" type="button" ><i class="fa fa-check"></i>提交</button>
                       <button class="btn btn-outline btn-danger btn-sm" onclick="PlatUtil.closeWindow();" type="button"><i class="fa fa-times"></i>关闭</button>
                   </div>
           </div>
	  </div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,jquery-layout,plat-util,nicevalid">
</plattag:resources>
<script type="text/javascript">

function submitAssocialForm(){
	$("#AssocialForm").trigger("validate"); 
	$("#AssocialForm").isValid(function(valid){
		alert(valid);
		if(valid){
			var url = $("#AssocialForm").attr("action");
			var formData = PlatUtil.getFormEleData("AssocialForm");
			PlatUtil.ajaxProgress({
				url:url,
				params : formData,
				callback : function(resultJson) {
					if (resultJson.success) {
						//parent.layer.alert(resultJson.msg,{icon: 1,resize:false});
						parent.layer.msg(resultJson.msg, {icon: 1});
						PlatUtil.setData("submitSuccess",true);
						PlatUtil.closeWindow();
					} else {
						parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
					}
				}
			});
		}
	});
}

$(function(){
	$("body").layout({ resizable:false});
	PlatUtil.initUIComp();
	$("#AssocialForm").validator({
	 	timely:1
	});  
});
</script>
