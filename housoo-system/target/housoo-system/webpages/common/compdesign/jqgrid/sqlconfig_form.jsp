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
	loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,plat-ui,nicevalid">
</plattag:resources>
</head>

<body>
	<div class="plat-directlayout" style="height: 100%;">
		<div class="ui-layout-center" style="overflow-y:auto; ">
			<form method="post" class="form-horizontal"
				action="system/dictionaryController.do?saveOrUpdate"
				id="SqlConfigForm">
				<input type="hidden" name="MAIN_ALIASNAME" value="${sqlConfig.MAIN_ALIASNAME}">
				<input type="hidden" name="FORMCONTROL_DESIGN_ID" value="${sqlConfig.FORMCONTROL_DESIGN_ID}">
				<div class="form-group plat-form-title">
					<span class="plat-current">查询主表选择</span>
				</div>
				<div class="form-group">
				    <plattag:select name="MAIN_TABLENAME" auth_type="write" istree="false" value="${sqlConfig.MAIN_TABLENAME}"
				    allowblank="false" placeholder="请选择主表" label_col_num="2" comp_col_num="4"
				    label_value="查询主表" dyna_interface="designService.findAssoicalTables" dyna_param="${sqlConfig.FORMCONTROL_DESIGN_ID}"
				    >
				    </plattag:select>
				    <plattag:select name="MAIN_QUERYFIELDS" auth_type="write" istree="false" value="${sqlConfig.MAIN_QUERYFIELDS}"
				    allowblank="false" placeholder="请选择查询字段" label_col_num="2" comp_col_num="4" multiple="multiple"
				    label_value="查询字段" dyna_interface="dbManagerService.findTableColumns" dyna_param="${sqlConfig.MAIN_TABLENAME}"
				    >
				    </plattag:select>
				</div>
				<div class="hr-line-dashed"></div>
				<div class="form-group plat-form-title">
					<span class="plat-current">连接子表选择</span>
				</div>
				<div class="titlePanel">
					<div class="title-search form-horizontal" id="jointable_search">
						<input type="hidden" name="DESIGN_ID" value="${sqlConfig.FORMCONTROL_DESIGN_ID}">
					</div>
					<div class="toolbar">
						<button type="button" onclick="addJoinTable();"
							class="btn btn-outline btn-primary btn-sm">
							<i class="fa fa-plus"></i>&nbsp;新增
						</button>
						<button type="button"
							onclick="PlatUtil.removeEditTableRows('joinTable');"
							class="btn btn-outline btn-danger btn-sm">
							<i class="fa fa-trash-o"></i>&nbsp;删除
						</button>
					</div>
				</div>
				<div class="gridPanel">
					<plattag:edittable dyna_interface="formcontrolService.findGridSqlConfigSubTables"
						tr_tplpath="common/compdesign/jqgrid/jointable_tr"
						id="joinTable" searchpanel_id="jointable_search"
						col_style="[5%,连接类型],[20%,子表选择],[20%,连接字段],[25%,外部连接字段],[30%,查询字段]">
					</plattag:edittable>
				</div>
			</form>
		</div>
		<div class="ui-layout-south">
			<div class="row"
				style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
				<div class="col-sm-12 text-right">
					<button class="btn btn-outline btn-primary btn-sm"
						onclick="submitSqlConfigForm();" type="button">
						<i class="fa fa-check"></i>提交
					</button>
					<button class="btn btn-outline btn-danger btn-sm"
						onclick="PlatUtil.closeWindow();" type="button">
						<i class="fa fa-times"></i>关闭
					</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
<plattag:resources restype="js"
	loadres="jquery-ui,jqgrid,jedate,select2,jquery-layout,plat-util,nicevalid">
</plattag:resources>
<script type="text/javascript">

function addJoinTable(){
	var FORMCONTROL_DESIGN_ID = $("input[name='FORMCONTROL_DESIGN_ID']").val();
	PlatUtil.addEditTableRow({
		tableId:"joinTable",
		FORMCONTROL_DESIGN_ID:FORMCONTROL_DESIGN_ID
	});
}

function submitSqlConfigForm(){
	$("#SqlConfigForm").trigger("validate"); 
	$("#SqlConfigForm").isValid(function(valid){
		if(valid){
			var url = $("#SqlConfigForm").attr("action");
			var formData = PlatUtil.getFormEleData("SqlConfigForm");
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
	PlatUtil.initUIComp();
	//table_alias
	$("select[name='MAIN_TABLENAME']").change(function() {
		var MAIN_TABLENAME = $(this).val();
		var tableAlias = PlatUtil.getSelectAttValue("MAIN_TABLENAME","table_alias");
		$("input[name='MAIN_ALIASNAME']").val(tableAlias);
		PlatUtil.reloadSelect("MAIN_QUERYFIELDS",{
			dyna_param:MAIN_TABLENAME
		});
	});
});

</script>
