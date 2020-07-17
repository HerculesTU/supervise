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
    <title>邮件发送日志列表</title>
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
	<h5 id="baselist_datatablepaneltitle">邮件发送日志列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="baselist_search" formcontrolid="402882a16072da7b016072e334520015">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.EMAILLOG_TITLE_LIKE" auth_type="write" label_value="发送标题" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.EMAILLOG_REMAILS_LIKE" auth_type="write" label_value="接收方邮箱" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
								    <jsp:include page="/webpages/common/plattagtpl/rangetime_tag.jsp">
									    <jsp:param name="label_col_num" value="3"></jsp:param>
									    <jsp:param name="format" value="YYYY-MM-DD"></jsp:param>
									    <jsp:param name="label_value" value="发送时间"></jsp:param>
									    <jsp:param name="comp_col_num" value="9"></jsp:param>
									    <jsp:param value="auth_type" name="write"></jsp:param>
									    <jsp:param name="posttimefmt" value="1"></jsp:param>
									    <jsp:param name="istime" value="false"></jsp:param>
									    <jsp:param name="allowblank" value="false"></jsp:param>
									    <jsp:param name="start_name" value="Q_T.EMAILLOG_TIME_GE"></jsp:param>
									    <jsp:param name="end_name" value="Q_T.EMAILLOG_TIME_LE"></jsp:param>
									</jsp:include>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('baselist_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('baselist_datatable','baselist_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="delJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="baselist_datatable"></table>
</div>
<div id="baselist_datatable_pager"></div>
<script type="text/javascript">
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"baselist_datatable",
		selectColName:"EMAILLOG_ID",
		url:"appmodel/EmailLogController.do?multiDel",
		callback:function(resultJson){
			$("#baselist_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"baselist_datatable",
		  searchPanelId:"baselist_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402882a16072da7b016072e334520015",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "EMAILLOG_ID",label:"日志ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "EMAILLOG_SENDER",label:"发送方邮箱",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EMAILLOG_TITLE",label:"发送标题",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EMAILLOG_CONTENT",label:"发送内容",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EMAILLOG_TIME",label:"发送时间",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EMAILLOG_REMAILS",label:"接收方邮箱",
		         width: 100,align:"left",
		         
		         
		         
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
