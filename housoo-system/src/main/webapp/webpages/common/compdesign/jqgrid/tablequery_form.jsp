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
	<plattag:resources restype="css" loadres="layer,bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,plat-ui,nicevalid">
	</plattag:resources>
  </head>
  
  <body>
      <div class="ui-layout-center" style="overflow-y:auto; ">
  		     <form method="post" class="form-horizontal" action="tableQueryController.do?saveOrUpdate" id="TableQueryForm">
  		           <input type="hidden" name="CONDITION_ID" value="${queryCondition.CONDITION_ID}">
  		           <input type="hidden" name="THROUGH_ID" value="${queryCondition.THROUGH_ID}">
  		           <div class="form-group plat-form-title">
                        <span class="plat-current">
							基本信息
						</span>
				   </div>
				   <div class="form-group">
				       <plattag:select name="QUERY_FIELDNAME" auth_type="write" istree="true"
				            label_col_num="2" label_value="查询字段" style="width:80%;"
							allowblank="true" placeholder="请选择查询字段" comp_col_num="8" onlyselectleaf="true"
							value="${queryCondition.QUERY_FIELDNAME}" dyna_interface="throughService.findSelectFields"
							dyna_param="${queryCondition.THROUGH_ID}" 
					   ></plattag:select>
                   </div>
                   <div class="hr-line-dashed"></div>
               	   <div class="form-group">
               	       <plattag:input name="QUERY_LABEL" auth_type="write" allowblank="false" 
               	       maxlength="30" value="${queryCondition.QUERY_LABEL}" datarule="required;"
               	       placeholder="请输入查询标签" label_col_num="2" label_value="查询标签" comp_col_num="4">
               	       </plattag:input>
               	       <plattag:select placeholder="请输入控件类型" istree="false" allowblank="false" 
               	       comp_col_num="4" auth_type="write" name="CONTROL_TYPE" 
               	       label_col_num="2" label_value="控件类型" value="${queryCondition.CONTROL_TYPE}"
               	       dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'QUERYCTRLTYPE',ORDER_TYPE:'ASC'}"
               	       ></plattag:select>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
               	       <plattag:select placeholder="请选择查询方案" istree="false" allowblank="true" 
               	       comp_col_num="4" auth_type="write" name="QUERY_STRAGE" 
               	       label_col_num="2" label_value="查询方案" value="${queryCondition.QUERY_STRAGE}"
               	       dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'QueryStrage',ORDER_TYPE:'ASC'}"
               	       ></plattag:select>
               	        <plattag:input name="CONTROL_NAME" auth_type="write" allowblank="false" 
               	       maxlength="30" value="${queryCondition.CONTROL_NAME}" datarule="required;"
               	       placeholder="请输入控件命名" label_col_num="2" label_value="控件命名" comp_col_num="4">
               	       </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
               	       <plattag:input name="CONTROL_VALUE" auth_type="write" allowblank="true" 
               	       maxlength="30" value="${queryCondition.CONTROL_VALUE}" 
               	       placeholder="请输入缺省值" label_col_num="2" label_value="控件缺省值" comp_col_num="4">
               	       </plattag:input>
               	       <plattag:select placeholder="请选择时间格式" istree="false" allowblank="true" 
               	       comp_col_num="4" auth_type="write" name="DATE_FORMAT" 
               	       label_col_num="2" label_value="时间格式" value="${queryCondition.DATE_FORMAT}"
               	       dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'DateFormat',ORDER_TYPE:'ASC'}"
               	       ></plattag:select>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
               	       <plattag:radio name="DATA_TYPE" value="${queryCondition.DATA_TYPE}"
               	       label_col_num="2" label_value="数据源方式" static_values="静态值:1,动态接口:2,JSONURL:3"
               	       auth_type="write" select_first="false" 
               	       allowblank="false" is_horizontal="true" comp_col_num="6"></plattag:radio>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                       <plattag:textarea name="STATIC_VALUES" auth_type="${queryCondition.DATA_TYPE=='1'?'write':'hidden'}" 
                         label_col_num="2" label_value="静态数据源" value="${queryCondition.STATIC_VALUES}"
                         allowblank="true" placeholder="请输入静态数据源例如(男:1,女:-1)" comp_col_num="10">
                       </plattag:textarea>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group" id="DYNA_DIV" style="${queryCondition.DATA_TYPE=='2'?'':'display: none;'}">
                       <plattag:input name="DYNA_INTERFACE" auth_type="write" allowblank="true" 
               	       maxlength="100" value="${queryCondition.DYNA_INTERFACE}" 
               	       placeholder="请输入动态接口" label_col_num="2" label_value="动态接口" comp_col_num="4">
               	       </plattag:input>
               	       <plattag:input name="DYNA_PARAM" auth_type="write" allowblank="true" 
               	       maxlength="100" value="${queryCondition.DYNA_PARAM}" 
               	       placeholder="请输入动态参数" label_col_num="2" label_value="动态参数" comp_col_num="4">
               	       </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                       <plattag:textarea name="JSON_URL" auth_type="${queryCondition.DATA_TYPE=='3'?'write':'hidden'}" 
                         label_col_num="2" label_value="JSON URL地址" value="${queryCondition.JSON_URL}"
                         allowblank="true" placeholder="http://" comp_col_num="10">
                       </plattag:textarea>
                   </div>
              </form>
      </div>
      <div class="ui-layout-south">
		   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
                   <div class="col-sm-12 text-right">
                       <button class="btn btn-outline btn-primary btn-sm" onclick="submitTableQueryForm();" type="button" ><i class="fa fa-check"></i>提交</button>
                   </div>
           </div>
	  </div>
  </body>
</html>
<plattag:resources restype="js" loadres="layer,jquery-ui,jqgrid,jedate,select2,jquery-layout,plat-util,nicevalid">
</plattag:resources>
<script type="text/javascript">

function submitTableQueryForm(){
	$("#TableQueryForm").trigger("validate"); 
	$("#TableQueryForm").isValid(function(valid){
		if(valid){
			var url = $("#TableQueryForm").attr("action");
			var formData = PlatUtil.getFormEleData("TableQueryForm");
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
	$("body").layout({ resizable:false});
	//初始化UI控件
	PlatUtil.initUIComp();
	$("select[name='QUERY_FIELDNAME']").change(function() {
		var selectValue = PlatUtil.getSelectAttValue("QUERY_FIELDNAME","value");
		var field_comments = PlatUtil.getSelectAttValue("QUERY_FIELDNAME","field_comments");
		$("input[name='QUERY_LABEL']").val(field_comments);
	});
	$("select[name='QUERY_STRAGE']").change(function() {
		var selectValue = PlatUtil.getSelectAttValue("QUERY_STRAGE","value");
		var QUERY_FIELDNAME = PlatUtil.getSelectAttValue("QUERY_FIELDNAME","value");
		var CONTROL_TYPE = PlatUtil.getSelectAttValue("CONTROL_TYPE","value");
		var controlName = "Q_"+QUERY_FIELDNAME;
		if(CONTROL_TYPE!="6"){
			controlName+="_"+selectValue;
		}
		$("input[name='CONTROL_NAME']").val(controlName);
	});
	$("input[name='DATA_TYPE']").change(function() {
		var dataType = $(this).val();
		if(dataType=="1"){
			PlatUtil.changeUICompAuth("write","STATIC_VALUES");
			$("#DYNA_DIV").css("display","none");
			PlatUtil.changeUICompAuth("hidden","JSON_URL");
		}else if(dataType=="2"){
			PlatUtil.changeUICompAuth("hidden","STATIC_VALUES");
			$("#DYNA_DIV").css("display","");
			PlatUtil.changeUICompAuth("hidden","JSON_URL");
		}else if(dataType=="3"){
			PlatUtil.changeUICompAuth("hidden","STATIC_VALUES");
			$("#DYNA_DIV").css("display","none");
			PlatUtil.changeUICompAuth("write","JSON_URL");
		}
	});
	$("#TableQueryForm").validator({
	 	timely:1
	});  
});
</script>
