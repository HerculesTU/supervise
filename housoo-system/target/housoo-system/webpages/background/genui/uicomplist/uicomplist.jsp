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
    <title>UI组件配置列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,slimscroll"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,slimscroll"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout" layoutsize="&quot;west__size&quot;:200">
   <div class="ui-layout-west" platundragable="true" compcode="direct_layout">
     <plattag:listgroup onclickfn="reloadUiCompDatatable" grouptitle="组件类别列表" dyna_interface="dictionaryService.findUiTypeList" dyna_param="{TYPE_CODE:'CONTROL_TYPE',ORDER_TYPE:'ASC'}"></plattag:listgroup>
<script type="text/javascript">
  function reloadUiCompDatatable(compTypeCode){
	if(compTypeCode!="0"){
		$("input[name='Q_P.COMP_TYPECODE_EQ']").val(compTypeCode);
	}else{
		$("input[name='Q_P.COMP_TYPECODE_EQ']").val("");
	}
	PlatUtil.tableDoSearch("uicomp_datatable","uicomp_search");
}
</script>
</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="uicomp_datatablepaneltitle">UI组件列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="uicomp_search">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_D.DIC_DICTYPE_CODE_EQ" value="CONTROL_TYPE">
                			  <input type="hidden" name="Q_P.COMP_TYPECODE_EQ" value="">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_P.COMP_CODE_LIKE" auth_type="write" label_value="组件编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_P.COMP_NAME_LIKE" auth_type="write" label_value="组件名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('uicomp_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('uicomp_datatable','uicomp_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<button type="button" onclick="addJqGridRecordInfo();" platreskey="addUIComp" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="editJqGridRecordInfo();" platreskey="editUIComp" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		</button>
		<button type="button" onclick="delJqGridRecordInfo();" platreskey="delUIComp" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		</button>
	</div>
</div>

<div class="gridPanel">
	<table id="uicomp_datatable"></table>
</div>
<div id="uicomp_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(COMP_ID){
	var url = "appmodel/UiCompController.do?goForm&UI_DESIGNCODE=uicompform";
	var title = "新增UI组件信息";
	if(COMP_ID){
		url+=("&COMP_ID="+COMP_ID);
		title = "编辑UI组件信息";
	}else{
      var COMP_TYPECODE = $("input[name='Q_P.COMP_TYPECODE_EQ']").val();
      url+="&COMP_TYPECODE="+COMP_TYPECODE;
    }
	PlatUtil.openWindow({
	  title:title,
	  area: ["800px","500px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#uicomp_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("uicomp_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.COMP_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"uicomp_datatable",
		selectColName:"COMP_ID",
		url:"appmodel/UiCompController.do?multiDel",
		callback:function(resultJson){
			$("#uicomp_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"uicomp_datatable",
		  searchPanelId:"uicomp_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402881e65b32eef0015b32f374cd0010",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "DIC_NAME",label:"所属类别",
		         width: 300,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "COMP_ID",label:"控件ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "COMP_CODE",label:"组件编码",
		         width: 300,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "COMP_NAME",label:"组件名称",
		         width: 400,align:"left",
		         
		         
		         
		         sortable:false
		         }
           ]
	 });
});
</script>
</div>
</div>
  </body>
</html>

<script type="text/javascript">

</script>
