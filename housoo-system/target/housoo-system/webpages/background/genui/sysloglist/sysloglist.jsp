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
    <title>系统日志列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,codemirror"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,codemirror"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="tabs-container" platundragable="true" compcode="bootstraptab" id="syslogtab" style="width:100%;height:100%;">
	<ul class="nav nav-tabs">
		<li class="active" subtabid="tab1" onclick="PlatUtil.onBootstrapTabClick('syslogtab','tab1','');"><a data-toggle="tab" href="#tab1" aria-expanded="true">用户操作日志</a></li>
		<li class="" subtabid="tab2" onclick="PlatUtil.onBootstrapTabClick('syslogtab','tab2','');"><a data-toggle="tab" href="#tab2" aria-expanded="false">实时控制台日志</a></li>
	</ul>
	<div class="tab-content" platundragable="true" compcode="bootstraptab" style="height: calc(100% - 42px);">
		<div id="tab1" class="tab-pane active" style="height:100%;" platundragable="true" compcode="bootstraptab">
		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="titlePanel">
	<div class="title-search form-horizontal" id="syslog_search">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
								    <plattag:select placeholder="" istree="false" label_col_num="3" label_value="操作类型" style="width:100%;" allowblank="true" comp_col_num="9" auth_type="write" static_values="" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'OPER_TYPE',ORDER_TYPE:'ASC'}" name="Q_T.OPER_TYPE_EQ">
								    </plattag:select>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.OPER_USERACCOUNT_LIKE" auth_type="write" label_value="操作人账号" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.OPER_USERNAME_LIKE" auth_type="write" label_value="操作人姓名" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
								    <jsp:include page="/webpages/common/plattagtpl/rangetime_tag.jsp">
									    <jsp:param name="label_col_num" value="3"></jsp:param>
									    <jsp:param name="format" value="YYYY-MM-DD"></jsp:param>
									    <jsp:param name="label_value" value="操作日期"></jsp:param>
									    <jsp:param name="comp_col_num" value="9"></jsp:param>
									    <jsp:param value="auth_type" name="write"></jsp:param>
									    <jsp:param name="istime" value="false"></jsp:param>
									    <jsp:param name="allowblank" value="false"></jsp:param>
									    <jsp:param name="start_name" value="Q_T.OPER_TIME_GE"></jsp:param>
									    <jsp:param name="end_name" value="Q_T.OPER_TIME_LE"></jsp:param>
									</jsp:include>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.OPER_MODULENAME_LIKE" auth_type="write" label_value="操作模块" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('syslog_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('syslog_datatable','syslog_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<button type="button" onclick="queryDetail();" id="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-eye"></i>&nbsp;查看日志详情
		</button>
	</div>
</div>

<div class="gridPanel">
	<table id="syslog_datatable"></table>
</div>
<div id="syslog_datatable_pager"></div>
<script type="text/javascript">
function queryDetail(){
   PlatUtil.showSysLogDetail("syslog_datatable");
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"syslog_datatable",
		  searchPanelId:"syslog_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55b7b0381015b7b32978f002e",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "LOG_ID",label:"日志ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "OPER_MODULENAME",label:"操作模块",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "OPER_TYPE",label:"操作类型",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "OPER_USERACCOUNT",label:"操作人账号",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "OPER_USERNAME",label:"操作人姓名",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "OPER_TIME",label:"操作时间",
		         width: 150,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "BROWSER",label:"浏览器",
		         width: 150,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "IP_ADDRESS",label:"访问IP地址",
		         width: 150,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "LOG_CONTENT",label:"日志内容",
		         width: 300,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "DETAILABLE",label:"是否支持明细",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         }
           ]
	 });
});
</script>
</div>
</div></div>
		<div id="tab2" class="tab-pane " style="height:100%;" platundragable="true" compcode="bootstraptab">
		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-north" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="stopRefresh();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-ban"></i>&nbsp;停止刷新
		</button>
		<button type="button" onclick="startRefresh();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;重新刷新
		</button>
     </div>
</div>

<script type="text/javascript">
function stopRefresh(){
  alert("停止刷新成功!");
  window.clearInterval(refreshLogInterval); 
}
function startRefresh(){
  alert("重新刷新成功!");
  refreshLogInterval = window.setInterval("refreshLog()",2000); 
}

</script>

</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="" id="LogForm" style="">

<div class="form-group" compcode="formgroup">
   <plattag:textarea name="LOG_CONTENT" allowblank="true" auth_type="write" value="" codemirror="codemirror" comp_col_num="12" id="LOG_CONTENT" mirrorwidth="auto" mirrorheight="400px">
   </plattag:textarea>

<script type="text/javascript">

</script>
</div>
<div class="hr-line-dashed"></div></form></div>
</div></div>
	</div>
</div>

<script type="text/javascript">

function mytabclick(subtabId){
	alert(subtabId);
}
</script>
  </body>
</html>

<script type="text/javascript">
var startRowNum  = 0;
var refreshLogInterval = null;

function refreshLog(){
	var code = PlatUtil.PLAT_CODEMIRROREDITOR.getValue();
	$.ajax({
        type: "POST",
        url: "system/SysLogController.do?getLogContent&startRowNum="+startRowNum,
        cache: false,
        success: function (responseJsonObj,status) {
     	   var endRowNum = responseJsonObj.endRowNum;
     	   if(endRowNum!=startRowNum){
     		   startRowNum = endRowNum;
     		   var logContent = responseJsonObj.logContent;
     		   code+=logContent;
     		   PlatUtil.PLAT_CODEMIRROREDITOR.setValue(code);
               PlatUtil.PLAT_CODEMIRROREDITOR.scrollIntoView(endRowNum);
     	   }
        }
    });
}

$(function(){
	 refreshLogInterval = window.setInterval("refreshLog()",2000); 
});
</script>
