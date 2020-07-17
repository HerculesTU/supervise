<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="mytasklist_datatablepaneltitle">我的待办列表</h5>
</div>
<div class="titlePanel" style="display: none;">
	<div class="title-search form-horizontal" id="mytasklist_search" formcontrolid="402848a55c9a117e015c9a194c5a000b">
		<table style="display: none;">
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_T.TASK_HANDLERIDS_LIKE" value="${sessionScope.__BACKPLATUSER.SYSUSER_ID}">
                			  <input type="hidden" name="limit" value="5">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('mytasklist_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('mytasklist_datatable','mytasklist_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>

<div class="gridPanel">
	<table id="mytasklist_datatable"></table>
</div>
<div id="mytasklist_datatable_pager"></div>
<script type="text/javascript">

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"mytasklist_datatable",
		  searchPanelId:"mytasklist_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55c9a117e015c9a194c5a000b",
		  nopager:true,
		  rowNum : -1,
		  height:300,
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "TASK_ID",label:"任务ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "TASK_STATUS",label:"任务状态(-2:等待 -1:挂起 1:正在办理 2:已办理 3:退回 4:转发 5:结束流程 6:审核不通过 )",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "EXECUTION_ID",label:"申请流水号",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EXECUTION_SUBJECT",label:"申请标题",
		         width: 400,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
  var EXECUTION_SUBJECT = rowObject.EXECUTION_SUBJECT;
  var EXECUTION_ID = rowObject.EXECUTION_ID;
  var TASK_STATUS = rowObject.TASK_STATUS;
  var TASK_ID = rowObject.TASK_ID;
  var jbpmIsQuery = "false";
  if(TASK_STATUS=="-1"){
     jbpmIsQuery = "true";
  }
  var href = "<a href=\"javascript:void(0)\"";
  href+=" onclick=\"PlatUtil.showExecutionWindow('"+EXECUTION_ID+"','"+EXECUTION_SUBJECT+"','"+jbpmIsQuery+"','"+TASK_ID+"',reloadMyTask);\" ";
  href += " ><span style='text-decoration:underline;color:green;'>"+EXECUTION_SUBJECT+"</span></a>";
  return href;
},
		         
		         sortable:false
		         },
		         {name: "EXECUTION_CREATETIME",label:"申请时间",
		         width: 150,align:"left",
		         
		         
		         
		         sortable:false
		         }
           ]
	 });
});
</script>
</div>
</div>

<script type="text/javascript">
function reloadMyTask(){
  $("#mytasklist_datatable").trigger("reloadGrid"); 
}
</script>
