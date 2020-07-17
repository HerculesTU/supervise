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
    <title>元数据表单</title>
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
   <form method="post" class="form-horizontal" compcode="formcontainer" action="appmodel/DbManagerController.do?saveOrUpdateTable" id="DbManagerForm" style="">

  <input type="hidden" name="isEdit" value="${isEdit}">
  <input type="hidden" name="oldTableName" value="${tableInfo.tableName}">
<div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		基本信息
	</span>
</div><div class="form-group" compcode="formgroup">
   <plattag:input name="tableName" allowblank="false" auth_type="write" value="${tableInfo.tableName}" datarule="required;remote[appmodel/DbManagerController.do?checkTableName, isEdit]" maxlength="30" label_value="表名" placeholder="请输入表名" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="tableComments" allowblank="false" auth_type="write" value="${tableInfo.tableComments}" datarule="required;" maxlength="100" label_value="表注释" placeholder="请输入表注释" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		字段信息
	</span>
</div><div class="titlePanel">
	<div class="title-search form-horizontal" id="columnTable_search">
		<table style="display: none;">
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="TABLE_NAME" value="${tableInfo.tableName}">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('columnTable_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('columnTable_datatable','columnTable_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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
	<plattag:edittable dyna_interface="dbManagerService.findTableColumnByFilter" tr_tplpath="background/genui/sourceform_sub/columnTable" id="columnTable" searchpanel_id="columnTable_search" col_style="[15%,列名称],[15%,列注释],[10%,列类型],[7%,数据长度],[13%,允许为空],[10%,默认值],[10%,是否主键],[10%,是否唯一],[10%,小数点数]">
	</plattag:edittable>
</div>

<script type="text/javascript">
function addEditTableRow(){
	PlatUtil.addEditTableRow({
		tableId:"columnTable",
        isNewColumn:"1"
	});
}
function delEditTableRow(){
  PlatUtil.removeEditTableRows('columnTable');
}

$(function(){
	
});
</script>
</form></div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" id="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;提交
		</button>
		<button type="button" onclick="closeWindow();" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
	if(PlatUtil.isFormValid("#DbManagerForm")){
		var url = $("#DbManagerForm").attr("action");
        var columnTableRows = PlatUtil.getEditTableAllRecord("columnTable");
		var formData = PlatUtil.getFormEleData("DbManagerForm");
        var tableColumnJson = JSON.stringify(columnTableRows);
		formData.tableColumnJson = tableColumnJson;
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
