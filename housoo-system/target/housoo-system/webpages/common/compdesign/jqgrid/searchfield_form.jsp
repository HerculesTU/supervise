<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<div class="titlePanel">
	<div class="title-search form-horizontal" id="searchfield_search">
		<input type="hidden" name="Q_T.QUERYCONDITION_FORMCONTROLID_EQ"
			value="${formControl.FORMCONTROL_ID}">
	</div>
	<div class="toolbar">
		<button type="button" onclick="addQueryCondition();"
			class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="editQueryCondition();"
			class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;修改
		</button>
		<button type="button" onclick="deleteQueryCondition();"
			class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash-o"></i>&nbsp;删除
		</button>
	</div>
</div>
<div class="gridPanel">
	<table id="searchfield_datatable"></table>
</div>

<script type="text/javascript">

function deleteQueryCondition(){
	PlatUtil.operMulRecordForTable({
		tableId:"searchfield_datatable",
		selectColName:"QUERYCONDITION_ID",
		url:"appmodel/QueryConditionController.do?multiDel",
		callback:function(resultJson){
			$("#searchfield_datatable").trigger("reloadGrid"); 
		}
	});
}

function editQueryCondition(){
	var rowData = PlatUtil.getTableOperSingleRecord("searchfield_datatable");
	if(rowData){
		addQueryCondition(rowData.QUERYCONDITION_ID);
	}
}

function addQueryCondition(QUERYCONDITION_ID){
	var url = "appmodel/QueryConditionController.do?goForm&FORMCONTROL_ID=${formControl.FORMCONTROL_ID}";
	var title = "新增查询条件";
	if(QUERYCONDITION_ID){
		title = "编辑查询条件";
		url+=("&QUERYCONDITION_ID="+QUERYCONDITION_ID);
	}
	PlatUtil.openWindow({
	  title : title,
	  area: ["1200px","520px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
			  $("#searchfield_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
</script>