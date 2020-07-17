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
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,plat-ui,nicevalid,codemirror,touchspin">
	</plattag:resources>
  </head>
  
  <body>
   <div class="plat-directlayout" style="height: 100%;">
      <div class="ui-layout-center" style="overflow-y:auto; ">
        <div class="plat-wizard">
			<ul class="steps">
				<li class="active"><span class="step" nextstep_fnname="saveBaseForm">1</span>基本配置<span class="chevron"></span></li>
				<li class=""><span class="step" nextstep_fnname="saveResForm" >2</span>资源配置<span class="chevron"></span></li>
				<li class=""><span class="step" >3</span>JS增强<span class="chevron"></span></li>
			</ul>
		</div>
		<div id="wizard-showdiv">
		   <div>
		     <jsp:include page="./design_base_form.jsp"></jsp:include>
		   </div>
		   <div style="display: none;">
		     <jsp:include page="./design_res_form.jsp"></jsp:include>
		   </div>
		   <div style="display: none;">
		      <form method="post" class="form-horizontal" >
					<div class="form-group">
						<plattag:textarea name="DESIGN_JSENHANCE" auth_type="write"
						    mirrorheight="400px" mirrorwidth="auto" codemirror="codemirror"
							label_col_num="2" label_value="JS增强" id="DESIGN_JSENHANCE"
							value="${design.DESIGN_JSENHANCE}" allowblank="true"
							placeholder="请输入模版源代码" comp_col_num="10">
						</plattag:textarea>
					</div>
			   </form>
		      
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
					onclick="submitDesignForm();" type="button">
					<i class="fa fa-check"></i>完成
				</button>
			</div>
		</div>
	  </div>
	</div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,jquery-layout,plat-util,nicevalid,codemirror,touchspin">
</plattag:resources>
<script type="text/javascript">

function addAssocialTable(){
	PlatUtil.addEditTableRow({
		tableId:"associalTable"
	});
}

function saveBaseForm(){
	var result = false;
	if(PlatUtil.isFormValid("#DesignForm")){
		var associalRows = PlatUtil.getEditTableAllRecord("associalTable");
		var url = $("#DesignForm").attr("action");
		var formData = PlatUtil.getFormEleData("DesignForm");
		if(associalRows.length>0){
			var aliasNames = [];
			var isExistSameAlias = false;
			$.each(associalRows,function(index,obj){
				if(aliasNames.indexOf(obj.TABLE_ALIAS)==-1){
					aliasNames.push(obj.TABLE_ALIAS);
				}else{
					isExistSameAlias = true;
					return false;
				}
			});
			if(isExistSameAlias){
				parent.layer.alert("不能存在相同别名的表,请修改!",{icon: 2,resize:false});
				return;
			}
			var ASSOICAL_JSON = JSON.stringify(associalRows);
			formData.ASSOICAL_JSON = ASSOICAL_JSON;
		}
		PlatUtil.ajaxProgress({
			url:url,
			async:"-1",
			params : formData,
			callback : function(resultJson) {
				if (resultJson.success) {
					$("#designToken").val(resultJson.designToken);
					result = true;
				} else {
					parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
					result = false;
				}
			}
		});
	}
	return result;
}

function saveResForm(){
	var result = false;
	if(PlatUtil.isFormValid("#ResForm")){
		PlatUtil.PLAT_CODEMIRROREDITOR.refresh();
		result = true;
	}
	return result;
}

function submitDesignForm(){
	if(PlatUtil.isFormValid("#ResForm")){
		var formData = PlatUtil.getFormEleData("ResForm");
		var DESIGN_JSENHANCE = PlatUtil.PLAT_CODEMIRROREDITOR.getValue();
		var designToken = $("#designToken").val();
		formData.DESIGN_JSENHANCE = DESIGN_JSENHANCE;
		formData.designToken = designToken;
		PlatUtil.ajaxProgress({
			url:"appmodel/DesignController.do?saveCompInfo",
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

$(function(){
	//初始化UI控件
	PlatUtil.initUIComp();
});
</script>
