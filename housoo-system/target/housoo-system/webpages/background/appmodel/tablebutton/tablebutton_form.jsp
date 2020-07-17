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
	<plattag:resources restype="css" loadres="layer,bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,plat-ui,nicevalid,codemirror">
	</plattag:resources>
	<link href="plug-in/bootstrap-star-rating-4.0.2/css/star-rating.css" 
	   rel="stylesheet" type="text/css"/>
  </head>
  
  <body class="plat-directlayout">
      <div class="ui-layout-center" style="overflow-y:auto; ">
  		     <form method="post" class="form-horizontal" action="appmodel/TableButtonController.do?saveOrUpdate" id="TableButtonForm">
  		           <input type="hidden" name="TABLEBUTTON_ID" value="${tableButton.TABLEBUTTON_ID}">
  		           <input type="hidden" name="TABLEBUTTON_FORMCONTROLID" value="${tableButton.TABLEBUTTON_FORMCONTROLID}">
  		           <div class="form-group plat-form-title">
                        <span class="plat-current">
							基本信息
						</span>
				   </div>
				   <div class="form-group">
				       <plattag:select placeholder="请选择常用按钮" istree="false" allowblank="true" 
	               	       comp_col_num="10" auth_type="write" name="COMMON_BUTTON" 
	               	       label_col_num="2" label_value="常用按钮" 
	               	       dyna_interface="genCmpTplService.findSelectButtons" dyna_param="1"
               	       ></plattag:select>
                   </div>
                   <div class="hr-line-dashed"></div>
               	   <div class="form-group">
               	       <plattag:input name="TABLEBUTTON_RESKEY" auth_type="write"
               	        allowblank="true" maxlength="30" value="${tableButton.TABLEBUTTON_RESKEY}" 
               	        datarule="onlyLetterNumberUnderLine;" 
               	       placeholder="请输入按钮KEY" label_col_num="2" label_value="按钮KEY" comp_col_num="4">
               	       </plattag:input>
               	       <plattag:input name="TABLEBUTTON_NAME" auth_type="write" allowblank="false"  
               	       maxlength="16" value="${tableButton.TABLEBUTTON_NAME}" datarule="required;"
               	       placeholder="请输入按钮名称" label_col_num="2" label_value="按钮名称" comp_col_num="4">
               	       </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                       <plattag:winselector selectorurl="appmodel/DesignController.do?goFontSelect" 
					    width="1200px" height="100%" maxselect="1" minselect="1" title="按钮图标选择器" 
					    label_col_num="2" label_value="按钮图标" allowblank="false" comp_col_num="4"
					    name="TABLEBUTTON_ICON" placeholder="请选择按钮图标" auth_type="write"
					    datarule="required;" 
					    value="${tableButton.TABLEBUTTON_ICON}" selectedlabels="${tableButton.TABLEBUTTON_ICON}">
					    </plattag:winselector>
               	       <plattag:select placeholder="请选择颜色" istree="false" value="${tableButton.TABLEBUTTON_COLOR}"
                       dyna_interface="dictionaryService.findList" label_col_num="2" 
               	        dyna_param="{TYPE_CODE:'CMP_COLOR',ORDER_TYPE:'ASC'}" label_value="按钮颜色"
                       allowblank="false" comp_col_num="4" auth_type="write" name="TABLEBUTTON_COLOR"></plattag:select>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
               	       <plattag:input name="TABLEBUTTON_BTNID" auth_type="write"
               	        allowblank="true" maxlength="60" value="${tableButton.TABLEBUTTON_BTNID}" 
               	       placeholder="请输入按钮ID" label_col_num="2" label_value="按钮ID" comp_col_num="4">
               	       </plattag:input>
               	       <plattag:radio name="TABLEBUTTON_DISABLED" auth_type="write" label_col_num="2"
               	        select_first="true" static_values="可用:1,不可用:-1" label_value="是否不可用"
               	        value="${tableButton.TABLEBUTTON_DISABLED}"
               	        allowblank="true" is_horizontal="true" comp_col_num="4"></plattag:radio>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                       <plattag:radio name="TABLEBUTTON_UPLOADED" auth_type="write" label_col_num="2"
               	        select_first="true" static_values="否:false,是:true" label_value="是否导入类按钮"
               	        value="${tableButton.TABLEBUTTON_UPLOADED}"
               	        allowblank="false" is_horizontal="true" comp_col_num="4"></plattag:radio>
               	       <plattag:input name="TABLEBUTTON_FN" auth_type="write" datarule="required;"
               	        allowblank="false" maxlength="60" value="${tableButton.TABLEBUTTON_FN}" 
               	       placeholder="请输入按钮点击事件名称" label_col_num="2" label_value="点击事件名" comp_col_num="4">
               	       </plattag:input>
                   </div>
                   <div id="impExcelDiv" 
                   <c:if test="${tableButton.TABLEBUTTON_UPLOADED==null||tableButton.TABLEBUTTON_UPLOADED=='false'}">style="display: none;"</c:if>
                   >
                   <div class="form-group">
               	       <plattag:input name="TABLEBUTTON_IMPFILETYPES" auth_type="write"
               	        allowblank="false" maxlength="60" value="${tableButton.TABLEBUTTON_IMPFILETYPES}" 
               	        datarule="required;"
               	       placeholder="请输入文件限制类型" label_col_num="2" label_value="文件限制类型" comp_col_num="4">
               	       </plattag:input>
               	       <plattag:input name="TABLEBUTTON_IMPFILESIZE" auth_type="write"
               	        allowblank="false" maxlength="20" value="${tableButton.TABLEBUTTON_IMPFILESIZE}" 
               	        datarule="required;positiveInteger;"
               	       placeholder="请输入文件限制大小,单位是字节" label_col_num="2" label_value="文件限制大小" comp_col_num="4">
               	       </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group" >
               	       <plattag:input name="TABLEBUTTON_TABLEID" auth_type="write"
               	        allowblank="false" maxlength="60" value="${tableButton.TABLEBUTTON_TABLEID}" 
               	        datarule="required;"
               	       placeholder="请输入JqGrid表格ID" label_col_num="2" label_value="JqGrid表格ID" comp_col_num="4">
               	       </plattag:input>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group" >
               	        <plattag:input name="TABLEBUTTON_IMPURL" auth_type="write"
               	        allowblank="false" maxlength="120" value="${tableButton.TABLEBUTTON_IMPURL}" 
               	        datarule="required;"
               	       placeholder="请输入导入处理URL" label_col_num="2" label_value="导入处理URL" comp_col_num="10">
               	       </plattag:input>
                   </div>
                   </div>
                   <div class="hr-line-dashed"></div>
                   <div class="form-group">
                       <plattag:textarea name="TABLEBUTTON_FNCONTENT" auth_type="write" codemirror="codemirror"
						    id="TABLEBUTTON_FNCONTENT" allowblank="true" label_col_num="2"
						    label_value="点击事件实现" value="${tableButton.TABLEBUTTON_FNCONTENT}"
							placeholder="请输入点击事件实现源码" comp_col_num="10">
						</plattag:textarea>
                   </div>
                   
              </form>
      </div>
      <div class="ui-layout-south">
		   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
                   <div class="col-sm-12 text-right">
                       <button class="btn btn-outline btn-primary btn-sm" onclick="submitTableButtonForm();" type="button" ><i class="fa fa-check"></i>提交</button>
                       <button class="btn btn-outline btn-danger btn-sm" onclick="PlatUtil.closeWindow();" type="button"><i class="fa fa-times"></i>关闭</button>
                   </div>
           </div>
	  </div>
  </body>
</html>
<plattag:resources restype="js" loadres="layer,jquery-ui,jqgrid,jedate,select2,jquery-layout,plat-util,nicevalid,codemirror">
</plattag:resources>
<script src="plug-in/bootstrap-star-rating-4.0.2/js/star-rating.js" 
type="text/javascript"></script>
<script src="plug-in/bootstrap-star-rating-4.0.2/js/locales/zh.js" 
type="text/javascript"></script>
<script type="text/javascript">
function submitTableButtonForm(){
	if(PlatUtil.isFormValid("#TableButtonForm")){
		var TABLEBUTTON_UPLOADED = PlatUtil.getCheckRadioTagValue("TABLEBUTTON_UPLOADED","VALUE");
		if(TABLEBUTTON_UPLOADED=="true"){
			var TABLEBUTTON_BTNID = $("input[name='TABLEBUTTON_BTNID']").val();
			if(!TABLEBUTTON_BTNID){
				alert("导入类按钮,按钮ID不能为空!");
				return;
			}
		}
		var url = $("#TableButtonForm").attr("action");
		var formData = PlatUtil.getFormEleData("TableButtonForm");
		formData.TABLEBUTTON_FNCONTENT = PlatUtil.PLAT_CODEMIRROREDITOR.getValue();
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
	$("input[name='TABLEBUTTON_UPLOADED']").change(function() {
		var isUploaded = $(this).val();
		if(isUploaded=='true'){
			$("#impExcelDiv").attr("style","");
		}else{
			$("#impExcelDiv").attr("style","display:none");
		}
	});
	$("select[name='COMMON_BUTTON']").change(function() {
		var gencmptpl_btnicon = PlatUtil.getSelectAttValue("COMMON_BUTTON","gencmptpl_btnicon");
		var gencmptpl_btncolor = PlatUtil.getSelectAttValue("COMMON_BUTTON","gencmptpl_btncolor");
		var gencmptpl_btnname = PlatUtil.getSelectAttValue("COMMON_BUTTON","gencmptpl_btnname");
		$("input[name='TABLEBUTTON_NAME']").val(gencmptpl_btnname);
		$("input[name='TABLEBUTTON_ICON']").val(gencmptpl_btnicon);
		$("input[name='TABLEBUTTON_ICON_LABELS']").val(gencmptpl_btnicon);
		PlatUtil.changeSelect2Val("select[name='TABLEBUTTON_COLOR']",gencmptpl_btncolor);
		var GENCMPTPL_ID = $(this).val();
		var TABLEBUTTON_FORMCONTROLID = $("input[name='TABLEBUTTON_FORMCONTROLID']").val();
		PlatUtil.ajaxProgress({
			url:"appmodel/CommonUIController.do?getJqgridBtnCode",
			params :{
				FORMCONTROL_ID:TABLEBUTTON_FORMCONTROLID,
				GENCMPTPL_ID:GENCMPTPL_ID
			},
			callback : function(resultJson) {
				if(resultJson.success){
					PlatUtil.PLAT_CODEMIRROREDITOR.setValue(resultJson.tplCode)
				}
			}
		});
		
	});
});
</script>
