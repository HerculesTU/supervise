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
    <title>在线会话列表</title>
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
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="session_datatablepaneltitle">在线会话列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="session_search" formcontrolid="402848a55cecb395015cecb5bf6f0009">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="SYSUSER_ACCOUNT" auth_type="write" label_value="用户账号" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="SYSUSER_NAME" auth_type="write" label_value="用户姓名" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('session_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('session_datatable','session_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="delJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;强制退出
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="session_datatable"></table>
</div>
<div id="session_datatable_pager"></div>
<script type="text/javascript">
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"session_datatable",
		selectColName:"SESSION_ID",
		url:"security/LoginController.do?forceLogout",
		callback:function(resultJson){
			$("#session_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"session_datatable",
		  searchPanelId:"session_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55cecb395015cecb5bf6f0009",
		  nopager:true,
		  rowNum : -1,
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "SESSION_ID",label:"会话ID",
		         width: 300,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "SYSUSER_ACCOUNT",label:"用户账号",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "SYSUSER_NAME",label:"用户姓名",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "HOST",label:"IP地址",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "LASTACCESSTIME",label:"最后访问时间",
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
