<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<div class="titlePanel">
	<div class="title-search form-horizontal" id="tablebutton_search">
		<input type="hidden" name="Q_T.TABLEBUTTON_FORMCONTROLID_EQ"
			value="${formControl.FORMCONTROL_ID}">
	</div>
	<div class="toolbar">
		<button type="button" onclick="addTableButton();"
			class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="editTableButton();"
			class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;修改
		</button>
		<button type="button" onclick="deleteTableButton();"
			class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash-o"></i>&nbsp;删除
		</button>
	</div>
</div>
<div class="gridPanel">
	<table id="tablebutton_datatable"></table>
</div>

<script type="text/javascript">

function deleteTableButton(){
	PlatUtil.operMulRecordForTable({
		tableId:"tablebutton_datatable",
		selectColName:"TABLEBUTTON_ID",
		url:"appmodel/TableButtonController.do?multiDel",
		callback:function(resultJson){
			$("#tablebutton_datatable").trigger("reloadGrid"); 
		}
	});
}

function editTableButton(){
	var rowData = PlatUtil.getTableOperSingleRecord("tablebutton_datatable");
	if(rowData){
		addTableButton(rowData.TABLEBUTTON_ID);
	}
}

function addTableButton(TABLEBUTTON_ID){
	var url = "appmodel/TableButtonController.do?goForm&FORMCONTROL_ID=${formControl.FORMCONTROL_ID}";
	var title = "新增操作按钮";
	if(TABLEBUTTON_ID){
		title = "编辑操作按钮";
		url+=("&TABLEBUTTON_ID="+TABLEBUTTON_ID);
	}
	PlatUtil.openWindow({
	  title : title,
	  area: ["90%","90%"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
			  $("#tablebutton_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
</script>