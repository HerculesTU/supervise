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
    <title>元数据列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,touchspin"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,touchspin"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="dbmanager_datatablepaneltitle">元数据列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="dbmanager_search" formcontrolid="402848a55b17ffd5015b18072ef2000f">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_U.TABLE_NAME_LIKE" auth_type="write" label_value="表名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_U.COMMENTS_LIKE" auth_type="write" label_value="表注释" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('dbmanager_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('dbmanager_datatable','dbmanager_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<button type="button" onclick="showDbManagerForm();" platreskey="addDbTable" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="editDbManager();" platreskey="editDbTable" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		</button>
		<button type="button" onclick="delDbManager();" platreskey="delDbTable" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		</button>
		<button type="button" onclick="cloneTable();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-clone"></i>&nbsp;克隆表
		</button>
		<button type="button" onclick="refreshColumn();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-refresh"></i>&nbsp;刷新字段缓存
		</button>
	</div>
</div>

<div class="gridPanel">
	<table id="dbmanager_datatable"></table>
</div>
<div id="dbmanager_datatable_pager"></div>
<script type="text/javascript">
function showDbManagerForm(tableName){
	var url = "appmodel/DbManagerController.do?goform&UI_DESIGNCODE=sourceform";
	var title = "新增数据库表信息";
	if(tableName){
		url+=("&tableName="+tableName);
		title = "编辑数据库表信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["100%","100%"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
			  $("#dbmanager_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editDbManager(){
	var rowData = PlatUtil.getTableOperSingleRecord("dbmanager_datatable");
	if(rowData){
		showDbManagerForm(rowData.TABLE_NAME);
	}
}
function delDbManager(){
	PlatUtil.operMulRecordForTable({
		tableId:"dbmanager_datatable",
		selectColName:"TABLE_NAME",
		url:"appmodel/DbManagerController.do?delTables",
		callback:function(resultJson){
			$("#dbmanager_datatable").trigger("reloadGrid"); 
		}
	});
}
function cloneTable(){
	var rowData = PlatUtil.getTableOperSingleRecord("dbmanager_datatable");
	if(rowData){
		var COMMENTS = rowData.COMMENTS;
        var TABLE_NAME = rowData.TABLE_NAME;
		PlatUtil.openWindow({
			title:"克隆["+COMMENTS+"]",
			area: ["800px","350px"],
			content: "appmodel/DbManagerController.do?goCloneView&TABLE_NAME="+TABLE_NAME,
			end:function(){
			  if(PlatUtil.isSubmitSuccess()){
				  $("#dbmanager_datatable").trigger("reloadGrid"); 
			  }
			}
		});
	}
}

function refreshColumn(){
  PlatUtil.ajaxProgress({
    url:"appmodel/DbManagerController.do?refreshColumns",
    async:"-1",
    params :{
      name:"zhangsan"
    },
    callback : function(resultJson) {
      if(resultJson.success){
        parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
        $("#dbmanager_datatable").trigger("reloadGrid"); 
       }
    }
  });
 
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"dbmanager_datatable",
		  searchPanelId:"dbmanager_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55b17ffd5015b18072ef2000f",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "TABLE_NAME",label:"表名称",
		         width: 300,align:"left",
		         
		         
		         sortable:true,index: "O_U.TABLE_NAME"
		         
		         },
		         {name: "COMMENTS",label:"表注释",
		         width: 300,align:"left",
		         
		         
		         
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
