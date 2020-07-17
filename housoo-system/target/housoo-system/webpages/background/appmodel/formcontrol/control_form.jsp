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
<plattag:resources restype="css"
	loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,plat-ui,nicevalid,fancybox,codemirror,touchspin">
</plattag:resources>
</head>

<body >
   <div class="plat-directlayout" style="height: 100%;" >
    <form action="" id="FormControlForm">
	    <input type="hidden" name="FORMCONTROL_ID" value="${formControl.FORMCONTROL_ID}"> 
	    <input type="hidden" name="FORMCONTROL_NAME" value="${formControl.FORMCONTROL_NAME}"> 
	    <input type="hidden" name="FORMCONTROL_PARENTID" value="${formControl.FORMCONTROL_PARENTID}"> 
	    <input type="hidden" name="FORMCONTROL_DESIGN_ID" value="${formControl.FORMCONTROL_DESIGN_ID}"> 
	    <input type="hidden" name="FORMCONTROL_COMPCODE" value="${formControl.FORMCONTROL_COMPCODE}"> 
	    <input type="hidden" name="FORMCONTROL_PARENTCOMPID" value="${formControl.FORMCONTROL_PARENTCOMPID}"> 
    </form>
	<div class="ui-layout-center" style="overflow-y:auto;" >
		<div class="plat-wizard">
			<ul class="steps">
				<li class="active"><span class="step" nextstep_fnname="choiceControl">1</span>选择组件<span class="chevron"></span></li>
				<li class=""><span class="step" id="BASE_CONFIG_SPAN" >2</span>基本配置<span class="chevron"></span></li>
				<li class=""><span class="step" id="ATTACH_CONFIG_SPAN" >3</span>附加配置<span class="chevron"></span></li>
				<li class=""><span class="step" >4</span>模版修改<span class="chevron"></span></li>
			</ul>
		</div>
		<div id="wizard-showdiv">
			<div>
			  <div class="plat-directlayout" style="height:500px;" >
				   <div class="ui-layout-west">
				       <plattag:listgroup onclickfn="reloadUiCompDatatable"
								grouptitle="组件类别列表"
								dyna_interface="dictionaryService.findUiTypeList"
								dyna_param="{TYPE_CODE:'CONTROL_TYPE',ORDER_TYPE:'ASC'}">
					   </plattag:listgroup>
				   </div>
				   <div class="ui-layout-center" style="overflow-y:auto; ">
				       <div class="col-sm-12" id="UICOMP_LIST">
				            <jsp:include page="./uicomp_list.jsp"></jsp:include>
	                    </div>
	                </div>
               </div>
			</div>
			<div style="display: none;" id="BASE_CONFIG_DIV">
			    
			</div>
			<div style="display: none;" id="ATTACH_CONFIG_DIV">
			   
			</div>
			<div style="display: none;" id="TEMPLATE_CONFIG_DIV">
			    <jsp:include page="./tplconfig_form.jsp"></jsp:include>
			</div>
			
		</div>
	</div>
	<div class="ui-layout-south">
		<div class="row"
			style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
			<div class="col-sm-12 text-right">
				<button class="btn btn-outline btn-primary btn-sm"
					id="WIZARD_PREBTN" onclick="PlatUtil.wizardPre();" type="button">
					<i class="fa fa-arrow-left"></i>上一步
				</button>
				<button class="btn btn-outline btn-primary btn-sm"
					id="WIZARD_NEXTBTN" onclick="PlatUtil.wizardNext();" type="button">
					<i class="fa fa-arrow-right"></i>下一步
				</button>
				<button class="btn btn-outline btn-primary btn-sm"
					id="WIZARD_OVERBTN" disabled="disabled"
					onclick="submitControlForm();" type="button">
					<i class="fa fa-check"></i>完成
				</button>
			</div>
		</div>
	</div>
   </div>
</body>
</html>
<plattag:resources restype="js"
	loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin,slimscroll,fancybox,nicevalid,codemirror,touchspin">
</plattag:resources>
<script type="text/javascript">
var editor = null;

function choiceControl(){
	var FORMCONTROL_COMPCODE = PlatUtil.getCheckRadioTagValue("FORMCONTROL_COMPCODE","value");
	var FORMCONTROL_NAME = PlatUtil.getCheckRadioTagValue("FORMCONTROL_COMPCODE","label");
	if(FORMCONTROL_COMPCODE){
		$("#FormControlForm input[name='FORMCONTROL_COMPCODE']").val(FORMCONTROL_COMPCODE);
		$("#FormControlForm input[name='FORMCONTROL_NAME']").val(FORMCONTROL_NAME);
		var formData = PlatUtil.getFormEleData("FormControlForm");
		var result = false;
		PlatUtil.ajaxProgress({
			url:"appmodel/FormControlController.do?saveControlInfo",
			async:"-1",
			params :formData,
			callback : function(resultJson) {
				if(resultJson.success){
					if(resultJson.JS_NAME&&resultJson.JS_NAME!=""){
						$("#BASE_CONFIG_SPAN").attr("nextstep_fnname",resultJson.JS_NAME);
					}
					if(resultJson.TPL_CODE&&resultJson.TPL_CODE!=""){
						editor.setValue(resultJson.TPL_CODE);
						editor.refresh();
					}
					$("#FormControlForm input[name='FORMCONTROL_ID']").val(resultJson.FORMCONTROL_ID);
					formData.FORMCONTROL_ID = resultJson.FORMCONTROL_ID;
					formData.VIEW_TYPE = "base";
					PlatUtil.ajaxProgress({
						url:"appmodel/FormControlController.do?getNextConfigView",
						async:"-1",
						params :formData,
						callback : function(resultHtml) {
							if(resultHtml&&resultHtml!=""){
								$("#BASE_CONFIG_DIV").html(resultHtml);
							}
							result = true;
						}
					});
				}
			}
		});
		
		return result;
	}else{
		parent.layer.alert("请选择器组件!",{icon: 7,resize:false});
		return false;
	}
}

function saveFormControlBaseConfig(){
	var result = false;
	var formData = PlatUtil.getFormEleData("BaseConfigForm");
	result  = PlatUtil.saveUIBaseConfigAndGoAttach(formData);
	return result;
}

function reloadUiCompDatatable(compTypeCode){
	PlatUtil.ajaxProgress({
		url:"appmodel/UiCompController.do?getuicompList",
		params : {
			compTypeCode:compTypeCode
		},
		callback : function(resultHtml) {
			$("#UICOMP_LIST").html(resultHtml);
		}
	});
}

function submitControlForm(){
	var formData = PlatUtil.getFormEleData("FormControlForm");
	formData.FORMCONTROL_TPLCODE = editor.getValue();
	PlatUtil.ajaxProgress({
		url:"appmodel/FormControlController.do?saveControl",
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


$(function(){
	PlatUtil.initUIComp();
	editor = CodeMirror.fromTextArea(document.getElementById("FORMCONTROL_TPLCODE"), {
		  mode: "htmlmixed",
		  theme:"blackboard",
		  autoCloseTags: true,
		  autoCloseBrackets: true,
		  styleActiveLine: true,
		  lineNumbers: true,
		  lineWrapping: true,
		  matchBrackets: true,
		  extraKeys: {
	        "F11": function(cm) {
	          cm.setOption("fullScreen", !cm.getOption("fullScreen"));
	        },
	        "Esc": function(cm) {
	          if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
	        },
	        "Alt-F": "findPersistent"
	      }
	});
	editor.setSize("auto","500px");

});
</script>
