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
  
  <body class="plat-directlayout">
      <div class="ui-layout-center" style="overflow-y:auto; ">
  		     <form method="post" class="form-horizontal" action="appmodel/QueryConditionController.do?saveOrUpdate" id="ConditionForm">
  		           <input type="hidden" name="QUERYCONDITION_ID" value="${queryCondition.QUERYCONDITION_ID}">
  		           <input type="hidden" name="QUERYCONDITION_FORMCONTROLID" value="${queryCondition.QUERYCONDITION_FORMCONTROLID}">
  		           <input type="hidden" name="QUERYCONDITION_ISTIME" value="${queryCondition.QUERYCONDITION_ISTIME}">
  		           <div class="form-group plat-form-title">
                        <span class="plat-current">
							基本信息
						</span>
				   </div>
				   <div class="form-group">
				       <plattag:select name="QUERYCONDITION_FIELDNAME" auth_type="write" istree="true"
				            label_col_num="2" label_value="查询字段" 
							allowblank="true" placeholder="请选择查询字段" comp_col_num="8" onlyselectleaf="true"
							value="${queryCondition.QUERYCONDITION_FIELDNAME}" dyna_interface="designService.findSelectFields"
							dyna_param="${queryCondition.DESIGN_ID}" 
					   ></plattag:select>
                   </div>
                   <div class="hr-line-dashed"></div>
               	   <div class="form-group">
               	       <plattag:input name="QUERYCONDITION_LABEL" auth_type="write" allowblank="false" 
               	       maxlength="30" value="${queryCondition.QUERYCONDITION_LABEL}" datarule="required;"
               	       placeholder="请输入查询标签" label_col_num="2" label_value="查询标签" comp_col_num="4">
               	       </plattag:input>
               	       <plattag:select placeholder="请输入控件类型" istree="false" allowblank="false" 
               	       comp_col_num="4" auth_type="write" name="QUERYCONDITION_CTRLTYPE" 
               	       label_col_num="2" label_value="控件类型" value="${queryCondition.QUERYCONDITION_CTRLTYPE}"
               	       dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'QUERYCTRLTYPE',ORDER_TYPE:'ASC'}"
               	       ></plattag:select>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
               	       <plattag:select placeholder="请选择查询方案" istree="false" allowblank="false" 
               	       comp_col_num="4" auth_type="write" name="QUERYCONDITION_QUERYSTRAGE" 
               	       label_col_num="2" label_value="查询方案" value="${queryCondition.QUERYCONDITION_QUERYSTRAGE}"
               	       dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'QUERY_STRAGE',ORDER_TYPE:'ASC'}"
               	       ></plattag:select>
               	        <plattag:input name="QUERYCONDITION_CTRLNAME" auth_type="write" allowblank="false" 
               	       maxlength="30" value="${queryCondition.QUERYCONDITION_CTRLNAME}" datarule="required;"
               	       placeholder="请输入控件命名" label_col_num="2" label_value="控件命名" comp_col_num="4">
               	       </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                        <plattag:radio name="IS_TREE"  value="${queryCondition.IS_TREE}" auth_type="write" select_first="true" static_values="否:false,是:true"
						  allowblank="false" is_horizontal="true" label_col_num="2" label_value="是否树形" comp_col_num="4">
						</plattag:radio>
						<plattag:radio name="MULTI_SELECT"  value="${queryCondition.MULTI_SELECT}" auth_type="write" select_first="true" static_values="否:false,是:true"
						  allowblank="false" is_horizontal="true" label_col_num="2" label_value="支持复选" comp_col_num="4">
						</plattag:radio>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
               	       <plattag:select placeholder="请选择时间格式" istree="false" allowblank="true" 
               	       comp_col_num="4" auth_type="write" name="QUERYCONDITION_DATEFORMAT" 
               	       label_col_num="2" label_value="时间格式" value="${queryCondition.QUERYCONDITION_DATEFORMAT}"
               	       dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'DATE_FORMAT',ORDER_TYPE:'ASC'}"
               	       ></plattag:select>
               	       <plattag:select placeholder="请选择提交时间格式" istree="false" allowblank="true" 
               	       comp_col_num="4" auth_type="write" name="TIME_POSTFMT" 
               	       label_col_num="2" label_value="提交时间格式" value="${queryCondition.TIME_POSTFMT}"
               	       dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'posttimefmt',ORDER_TYPE:'ASC'}"
               	       ></plattag:select>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                      <plattag:radio name="DEFAULTVALUE_TYPE"  value="${queryCondition.DEFAULTVALUE_TYPE}" auth_type="write" select_first="true" 
                          static_values="选择类型:1,手动输入:2"
						  allowblank="true" is_horizontal="true" label_col_num="2" label_value="缺省值数据类型" comp_col_num="4">
						</plattag:radio>
               	       <plattag:select placeholder="请选择缺省值" istree="false" allowblank="true" 
               	       comp_col_num="4" auth_type="write" name="DEFAULT_SELECT" 
               	       label_col_num="2" label_value="缺省值选择" 
               	       dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'QUERY_DEFAULTVALUE',ORDER_TYPE:'ASC'}"
               	       ></plattag:select>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
               	      <plattag:input name="DEFAULT_INPUT" auth_type="write" allowblank="true" 
               	       placeholder="请输入缺省值" label_col_num="2" label_value="输入缺省值" comp_col_num="10">
               	       </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                       <plattag:radio name="SELECT_FIRST"  value="${queryCondition.SELECT_FIRST}" auth_type="write" select_first="true" static_values="是:true,否:false"
						  allowblank="false" is_horizontal="true" label_col_num="2" label_value="默认选中首个" comp_col_num="4">
					   </plattag:radio>
               	       <plattag:radio name="QUERYCONDITION_DATATYPE" value="${queryCondition.QUERYCONDITION_DATATYPE}"
               	       label_col_num="2" label_value="数据源方式" static_values="静态值:1,动态接口:2"
               	       auth_type="write" select_first="false" 
               	       allowblank="true" is_horizontal="true" comp_col_num="4"></plattag:radio>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                       <plattag:textarea name="QUERYCONDITION_STATICVALUES" auth_type="${queryCondition.QUERYCONDITION_DATATYPE=='1'?'write':'hidden'}" 
                         label_col_num="2" label_value="静态数据源" value="${queryCondition.QUERYCONDITION_STATICVALUES}"
                         allowblank="true" placeholder="请输入静态数据源例如(男:1,女:-1)" comp_col_num="10">
                       </plattag:textarea>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div id="DYNA_DIV" style="${queryCondition.QUERYCONDITION_DATATYPE=='2'?'':'display: none;'}">
                       <div class="form-group" >
                           <plattag:select placeholder="请选择动态接口" istree="false" allowblank="true" 
	               	       comp_col_num="10" auth_type="write" name="QUERYCONDITION_DYNAINTERFACE" style="width:95%;"
	               	       label_col_num="2" label_value="动态接口" value="${queryCondition.QUERYCONDITION_DYNAINTERFACE}"
	               	       dyna_interface="genCmpTplService.findSelectJavaInters" dyna_param="2"
	               	       ></plattag:select>
	                   </div>
	                   <div class="hr-line-dashed"></div>
	                   <div class="form-group" >
	                       <plattag:textarea name="QUERYCONDITION_DYNAPARAM" auth_type="write" allowblank="true" 
	                       comp_col_num="10" label_col_num="2" label_value="动态参数" placeholder="请输入动态参数"
	                        value="${queryCondition.QUERYCONDITION_DYNAPARAM}"
	                       ></plattag:textarea>
	                   </div>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div id="NUMBER_DIV" style="${queryCondition.QUERYCONDITION_CTRLTYPE=='8'?'':'display: none;'}">
                       <div class="form-group" >
                           <plattag:input name="QUERYCONDITION_STEP" auth_type="write" allowblank="false"  datarule="positiveInteger;required;"
								placeholder="请输入输入跨度值" comp_col_num="4" label_col_num="2" value="${queryCondition.QUERYCONDITION_STEP}"
								maxlength="30" label_value="跨度值"  >
							</plattag:input>
							<plattag:input name="QUERYCONDITION_DECIMALS" auth_type="write" allowblank="false"  datarule="positiveInteger;required;"
								placeholder="请输入输入小数位数" comp_col_num="4" label_col_num="2" value="${queryCondition.QUERYCONDITION_DECIMALS}"
								maxlength="30" label_value="小数位数"  >
							</plattag:input>
	                   </div>
	                   <div class="hr-line-dashed"></div>
	                   <div class="form-group" >
	                       <plattag:input name="QUERYCONDITION_MAX" auth_type="write" allowblank="true" 
								placeholder="请输入最大值" comp_col_num="4" label_col_num="2" value="${queryCondition.QUERYCONDITION_MAX}"
								maxlength="30" label_value="最大值" >
							</plattag:input>
							<plattag:input name="QUERYCONDITION_MIN" auth_type="write" allowblank="true" 
								placeholder="请输入最小值" comp_col_num="4" label_col_num="2" value="${queryCondition.QUERYCONDITION_MIN}"
								maxlength="30" label_value="最小值" >
							</plattag:input>
	                   </div>
	                   <div class="hr-line-dashed"></div>
	                   <div class="form-group" >
	                       <plattag:input name="PREFIX" auth_type="write" allowblank="true" attach_props=""
								placeholder="请输入前标签" comp_col_num="4" label_col_num="2" value="${queryCondition.PREFIX}"
								maxlength="100" label_value="前标签" >
							</plattag:input>
							<plattag:input name="POSTFIX" auth_type="write" allowblank="true" 
								placeholder="请输入后标签 " comp_col_num="4" label_col_num="2" value="${queryCondition.POSTFIX}"
								maxlength="100" label_value="后标签" >
							</plattag:input>
	                   </div>
                   </div>
                   
                   <div id="WINSELECTOR_DIV" style="${queryCondition.QUERYCONDITION_CTRLTYPE=='9'?'':'display: none;'}">
                        <div class="form-group" compcode="formgroup">
						   <plattag:input name="SELECTORURL" allowblank="false" auth_type="write" value="${queryCondition.SELECTORURL}" datarule="required;" label_value="选择器URL" placeholder="请输入选择器URL" comp_col_num="10" label_col_num="2">
						   </plattag:input>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group" compcode="formgroup">
						   <plattag:input name="WIN_TITLE" allowblank="false" auth_type="write" value="${queryCondition.WIN_TITLE}" datarule="required;" label_value="标题" placeholder="请输入弹出层标题" comp_col_num="2" label_col_num="2">
						   </plattag:input>
						   <plattag:input name="WIN_WIDTH" allowblank="false" auth_type="write" value="${queryCondition.WIN_WIDTH}" datarule="required;" label_value="宽度" placeholder="px或者%" comp_col_num="2" label_col_num="2">
						   </plattag:input>
						   <plattag:input name="WIN_HEIGHT" allowblank="false" auth_type="write" value="${queryCondition.WIN_HEIGHT}" datarule="required;" label_value="高度" placeholder="px或者%" comp_col_num="2" label_col_num="2">
						   </plattag:input>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group" compcode="formgroup">
						   <plattag:input name="MAXSELECT" allowblank="false" auth_type="write" value="${queryCondition.MAXSELECT}" datarule="required;positiveInteger;" label_value="最大可选数量" placeholder="最大可选数量,0代表不控制" comp_col_num="2" label_col_num="2">
						   </plattag:input>
						   <plattag:input name="MINSELECT" allowblank="false" auth_type="write" value="${queryCondition.MINSELECT}" datarule="required;positiveInteger;" label_value="至少选择数量" placeholder="至少选择数量" comp_col_num="2" label_col_num="2">
						   </plattag:input>
						   <plattag:radio name="IS_TREESELECTOR" allowblank="false" auth_type="write" value="${queryCondition.IS_TREESELECTOR}" select_first="true" is_horizontal="true" label_value="是否树形" comp_col_num="2" label_col_num="2" style="是否树形选择" static_values="否:false,是:true">
						   </plattag:radio>
						</div>
						<div class="hr-line-dashed"></div>
						<div class="form-group" compcode="formgroup" >
						   <plattag:checkbox name="CHECKCASCADEY" allowblank="true" auth_type="write" value="${queryCondition.CHECKCASCADEY}" is_horizontal="true" label_value="被勾选时" comp_col_num="2" label_col_num="2" static_values="关联父:p,关联子:s">
						   </plattag:checkbox>
						   <plattag:checkbox name="CHECKCASCADEN" allowblank="true" auth_type="write" value="${queryCondition.CHECKCASCADEN}" is_horizontal="true" label_value="取消勾选" comp_col_num="2" label_col_num="2" static_values="关联父:p,关联子:s">
						   </plattag:checkbox>
						   <plattag:radio name="CHECKTYPE" allowblank="true" auth_type="write" value="${queryCondition.CHECKTYPE}" select_first="true" is_horizontal="true" label_value="勾选控件" comp_col_num="2" label_col_num="2" static_values="复选框:checkbox,单选框:radio">
						   </plattag:radio>
						</div>
                   </div>
                   
              </form>
      </div>
      <div class="ui-layout-south">
		   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
                   <div class="col-sm-12 text-right">
                       <button class="btn btn-outline btn-primary btn-sm" onclick="submitConditionForm();" type="button" ><i class="fa fa-check"></i>提交</button>
                       <button class="btn btn-outline btn-danger btn-sm" onclick="PlatUtil.closeWindow();" type="button"><i class="fa fa-times"></i>关闭</button>
                   </div>
           </div>
	  </div>
  </body>
</html>
<plattag:resources restype="js" loadres="layer,jquery-ui,jqgrid,jedate,select2,jquery-layout,plat-util,nicevalid">
</plattag:resources>
<script type="text/javascript">

function submitConditionForm(){
	if(PlatUtil.isFormValid("#ConditionForm")){
		var url = $("#ConditionForm").attr("action");
		var DEFAULTVALUE_TYPE = PlatUtil.getCheckRadioTagValue("DEFAULTVALUE_TYPE","value");
		var QUERYCONDITION_CONTROLVALUE = null;
		if(DEFAULTVALUE_TYPE=="1"){
			QUERYCONDITION_CONTROLVALUE = $("select[name='DEFAULT_SELECT']").val();
		}else if(DEFAULTVALUE_TYPE=="2"){
			QUERYCONDITION_CONTROLVALUE = $("input[name='DEFAULT_INPUT']").val();
		}
		var formData = PlatUtil.getFormEleData("ConditionForm");
		formData.QUERYCONDITION_CONTROLVALUE = QUERYCONDITION_CONTROLVALUE;
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
}

$(function(){
	//初始化UI控件
	PlatUtil.initUIComp();
	var DEFAULTVALUE_TYPE = PlatUtil.getCheckRadioTagValue("DEFAULTVALUE_TYPE","value");
	var QUERYCONDITION_CONTROLVALUE = "${queryCondition.QUERYCONDITION_CONTROLVALUE}";
	if(DEFAULTVALUE_TYPE=="1"){
		PlatUtil.changeSelect2Val("select[name='DEFAULT_SELECT']",QUERYCONDITION_CONTROLVALUE);
		PlatUtil.changeUICompAuth("hidden","DEFAULT_INPUT");
	}else if(DEFAULTVALUE_TYPE=="2"){
		$("input[name='DEFAULT_INPUT']").val(QUERYCONDITION_CONTROLVALUE);
		PlatUtil.changeUICompAuth("hidden","DEFAULT_SELECT");
	}
	$("select[name='QUERYCONDITION_FIELDNAME']").change(function() {
		var selectValue = PlatUtil.getSelectAttValue("QUERYCONDITION_FIELDNAME","value");
		var field_comments = PlatUtil.getSelectAttValue("QUERYCONDITION_FIELDNAME","field_comments");
		$("input[name='QUERYCONDITION_LABEL']").val(field_comments);
	});
	$("select[name='QUERYCONDITION_QUERYSTRAGE']").change(function() {
		var selectValue = PlatUtil.getSelectAttValue("QUERYCONDITION_QUERYSTRAGE","value");
		var QUERYCONDITION_FIELDNAME = PlatUtil.getSelectAttValue("QUERYCONDITION_FIELDNAME","value");
		var QUERYCONDITION_CTRLTYPE = PlatUtil.getSelectAttValue("QUERYCONDITION_CTRLTYPE","value");
		var controlName = "Q_"+QUERYCONDITION_FIELDNAME;
		if(QUERYCONDITION_CTRLTYPE!="7"){
			controlName+="_"+selectValue;
		}
		$("input[name='QUERYCONDITION_CTRLNAME']").val(controlName);
	});
	$("select[name='QUERYCONDITION_CTRLTYPE']").change(function(){
		var QUERYCONDITION_CTRLTYPE = PlatUtil.getSelectAttValue("QUERYCONDITION_CTRLTYPE","value");
		if(QUERYCONDITION_CTRLTYPE=="8"){
			$("#NUMBER_DIV").attr("style","");
		}else{
			$("#NUMBER_DIV").attr("style","display:none;");
		}
		if(QUERYCONDITION_CTRLTYPE=="9"){
			$("#WINSELECTOR_DIV").attr("style","");
		}else{
			$("#WINSELECTOR_DIV").attr("style","display:none;");
		}
	});
	
	$("input[name='QUERYCONDITION_DATATYPE']").change(function() {
		var dataType = $(this).val();
		if(dataType=="1"){
			PlatUtil.changeUICompAuth("write","QUERYCONDITION_STATICVALUES");
			$("#DYNA_DIV").css("display","none");
		}else if(dataType=="2"){
			PlatUtil.changeUICompAuth("hidden","QUERYCONDITION_STATICVALUES");
			$("#DYNA_DIV").css("display","");
		}
	});
	
	$("select[name='QUERYCONDITION_DYNAINTERFACE']").change(function() {
		var gencmptplCode = PlatUtil.getSelectAttValue("QUERYCONDITION_DYNAINTERFACE","gencmptpl_code");
		$("textarea[name='QUERYCONDITION_DYNAPARAM']").val(gencmptplCode);
	});
	
	$("select[name='QUERYCONDITION_DATEFORMAT']").change(function() {
		var formatValue = $(this).val();
		if(formatValue.indexOf("hh")!=-1){
			$("input[name='QUERYCONDITION_ISTIME']").val("1");
		}else{
			$("input[name='QUERYCONDITION_ISTIME']").val("-1");
		}
	});
	
	$("input[name='DEFAULTVALUE_TYPE']").change(function() {
		var type = $(this).val();
		if(type=="1"){
			PlatUtil.changeUICompAuth("write","DEFAULT_SELECT");
			PlatUtil.changeUICompAuth("hidden","DEFAULT_INPUT");
		}else if(type=="2"){
			PlatUtil.changeUICompAuth("hidden","DEFAULT_SELECT");
			PlatUtil.changeUICompAuth("write","DEFAULT_INPUT");
		}
	});
});
</script>
