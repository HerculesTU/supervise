<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>

<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<title>My JSP 'uipreview.jsp' starting page</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<plattag:resources restype="css"
	loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
<plattag:resources restype="js"
	loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid"></plattag:resources>
<style type="text/css">
.jqgrid-ui-report{
	border-top: 1px solid #ccc;
	padding: 5px;
}
.jqgrid-ui-report .grid-title {
    font-size: 22px;
    text-align: center;
    width: 100%;
    color: #444;
}
.footrow-ltr td{
	border-right:1px solid #ccc !important;
}
</style>
</head>

<body>
	<script type="text/javascript">
		$(function() {
			PlatUtil.initUIComp();
		});
	</script>
	<div class="plat-directlayout" style="height:100%"
		platundragable="true" compcode="direct_layout">
		<div class="ui-layout-center" platundragable="true"
			compcode="direct_layout">
			<div class="panel-Title">
				<h5 id="SysEhcache_datatablepaneltitle">缓存配置管理</h5>
			</div>
			<div class="titlePanel">
				<div class="title-search form-horizontal" id="SysEhcache_search"
					formcontrolid="402848aa5bcd4f32015bcd60b4a40014">
					<table>
						<tr>
							<td>查询条件</td>
							<td style="padding-left: 5px;">
								<div class="table-filter">
									<input type="text" style="width: 200px; "
										onclick="PlatUtil.showOrHideSearchTable(this);"
										class="table-form-control" name="search" readonly="readonly">
									<div class="table-filter-list"
										style="width: 420px; display: none;max-height: 280px;">
										<div class="form-group">
											<plattag:input name="Q_T.EHCACHE_NAME_LIKE" auth_type="write"
												label_value="缓存名称" maxlength="100" allowblank="true"
												placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
										</div>
										<div class="table-filter-list-bottom">
											<a onclick="PlatUtil.tableSearchReset('SysEhcache_search');"
												class="btn btn-default" href="javascript:void(0);"> 重 置</a>
											<a
												onclick="PlatUtil.tableDoSearch('SysEhcache_datatable','SysEhcache_search',true);"
												class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
										</div>
									</div>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<div class="toolbar">
					<button type="button" onclick="expJqGridRecordInfo();"
						platreskey="" class="btn btn-outline btn-primary btn-sm">
						<i class="fa fa-plus"></i> 导出
					</button>

				</div>
			</div>
			<div class="jqgrid-ui-report">
				<div class="grid-title">应收账款报表</div>
			</div>
			<div class="gridPanel">
				<table id="SysEhcache_datatable"></table>
			</div>
			<div id="SysEhcache_datatable_pager"></div>
			<script type="text/javascript">
				function expJqGridRecordInfo(){
					var url = "system/WorkdayController.do?export";
					window.location.href = url; 
				}
				$(function() {
					PlatUtil
							.initJqGrid({
								tableId : "SysEhcache_datatable",
								multiselect:false,
								searchPanelId : "SysEhcache_search",
								url : "webpages/background/system/workday/list.json",
								ondblClickRow : function(rowid, iRow, iCol, e) {
									
								},
								gridComplete: completeMethod, 
								footerrow: true,
								groupHeaders:true,
								colModel : [
										{
											name : "NAME",
											label : "姓名",
											width : 100,
											align : "left",
											sortable : false
										},
										{
											name : "POSITION",
											label : "职位",
											width : 100,
											align : "left",
											sortable : false
										} ,
										{
											name : "BIRTHDAY",
											label : "生日",
											width : 100,
											align : "left",
											sortable : false
										} ,
										{
											name : "AGE",
											label : "年龄",
											width : 100,
											align : "left",
											sortable : false
										} ,
										{
											name : "SALARY",
											label : "工资",
											width : 100,
											align : "left",
											sortable : false
										} ]
							});
					jQuery("#SysEhcache_datatable").jqGrid('setGroupHeaders', {
					    useColSpanStyle: false, 
					    groupHeaders:[
					    {startColumnName: 'POSITION', numberOfColumns: 2, titleText: '<div style="text-align: center;">基本信息</div>'},
					    {startColumnName: 'AGE', numberOfColumns: 2, titleText: '<div style="text-align: center;">数字信息</div>'}
					    ]  
					  }); 
				});
				
				function completeMethod(){
					var AGESUM=$("#SysEhcache_datatable").getCol('AGE',false,'sum');  
		            var SALARYAVG=$("#SysEhcache_datatable").getCol('SALARY',false,'avg');
		            $("#SysEhcache_datatable").footerData('set', {AGE:"合计:"+AGESUM,SALARY:"平均:"+SALARYAVG}); 
				}
			</script>
		</div>
	</div>
</body>
</html>

<script type="text/javascript">
	
</script>
