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
    <title>通用组件列表</title>
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
    <div class="plat-directlayout" style="height:100%" compcode="direct_layout">
   <div class="ui-layout-center" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="gencmptpl_datatablepaneltitle">通用组件模版列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="gencmptpl_search">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_D.DIC_DICTYPE_CODE_EQ" value="GEN_TPL_TYPE">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.GENCMPTPL_DESC_LIKE" auth_type="write" label_value="模版说明" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
								    <plattag:select placeholder="" istree="false" label_col_num="3" label_value="模版类型" style="width:100%;" allowblank="true" comp_col_num="9" auth_type="write" static_values="" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'GEN_TPL_TYPE',ORDER_TYPE:'ASC'}" name="Q_T.GENCMPTPL_TYPE_EQ">
								    </plattag:select>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('gencmptpl_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('gencmptpl_datatable','gencmptpl_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<button type="button" onclick="addJqGridRecordInfo();" platreskey="addGenCmpTpl" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="editJqGridRecordInfo();" platreskey="editGenCmpTpl" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		</button>
		<button type="button" onclick="delJqGridRecordInfo();" platreskey="delGenCmpTpl" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		</button>
	</div>
</div>

<div class="gridPanel">
	<table id="gencmptpl_datatable"></table>
</div>
<div id="gencmptpl_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(GENCMPTPL_ID){
	var url = "appmodel/GenCmpTplController.do?goForm&UI_DESIGNCODE=gencmpform";
	var title = "新增组件模版信息";
	if(GENCMPTPL_ID){
		url+=("&GENCMPTPL_ID="+GENCMPTPL_ID);
		title = "编辑组件模版信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["1200px","550px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#gencmptpl_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("gencmptpl_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.GENCMPTPL_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"gencmptpl_datatable",
		selectColName:"GENCMPTPL_ID",
		url:"appmodel/GenCmpTplController.do?multiDel",
		callback:function(resultJson){
			$("#gencmptpl_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"gencmptpl_datatable",
		  searchPanelId:"gencmptpl_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55aeae359015aeae3ee5b0005",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },		
		  colModel: [
		         {name: "GENCMPTPL_ID",label:"模版ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         sortable:false
		         },
		         {name: "GENCMPTPL_DESC",label:"模版说明",
		         width: 300,align:"left",
		         
		         
		         sortable:false
		         },
		         {name: "GENCMPTPL_TYPE",label:"模版类型",
		         width: 200,align:"left",
		         
		         
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
