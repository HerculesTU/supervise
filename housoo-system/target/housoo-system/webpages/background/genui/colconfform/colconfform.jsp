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
    <title>列配置表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="appmodel/ExcelImpController.do?saveColConf" id="colconfform" style="">

  <input type="hidden" name="EXCELIMP_ID" value="${EXCELIMP_ID}">
<div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		列配置信息
	</span>
</div><div class="titlePanel">
	<div class="title-search form-horizontal" id="coltable_search">
		<table style="display: none;">
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="EXCELIMP_ID" value="${EXCELIMP_ID}">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('coltable_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('coltable_datatable','coltable_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<button type="button" onclick="addEditTableRow();" id="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="delEditTableRow();" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		</button>
	</div>
</div>

<div class="gridPanel">
	<plattag:edittable dyna_interface="excelImpService.findColConfList" tr_tplpath="background/genui/colconfform_sub/coltable" id="coltable" searchpanel_id="coltable_search" col_style="[10%,对应列序号],[10%,列中文名],[10%,关联字段名],[10%,允许空值],[10%,验证规则],[15%,验证接口],[10%,是否隐藏],[10%,隐藏列缺省值],[15%,值转换接口]">
	</plattag:edittable>
</div>

<script type="text/javascript">
function addEditTableRow(){
	PlatUtil.addEditTableRow({
		tableId:"coltable"
	});
}
function delEditTableRow(){
  PlatUtil.removeEditTableRows('coltable');
}

</script>
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
	if(PlatUtil.isFormValid("#colconfform")){
		var url = $("#colconfform").attr("action");
		var formData = PlatUtil.getFormEleData("colconfform");
        var EXCELIMP_COLUMNJSON = PlatUtil.getEditTableAllRecordJson("coltable");
        formData.EXCELIMP_COLUMNJSON = EXCELIMP_COLUMNJSON;
		PlatUtil.ajaxProgress({
			url:url,
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
function closeWindow(){
  PlatUtil.closeWindow();
}

</script>

</div>
</div>
  </body>
</html>

<script type="text/javascript">

</script>
