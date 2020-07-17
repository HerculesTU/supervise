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
    <title>缓存配置列表</title>
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
	<h5 id="SysEhcache_datatablepaneltitle">缓存配置管理</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="SysEhcache_search" formcontrolid="402848aa5bcd4f32015bcd60b4a40014">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.EHCACHE_NAME_LIKE" auth_type="write" label_value="缓存名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('SysEhcache_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('SysEhcache_datatable','SysEhcache_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="addJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		  </button>
		
	      <button type="button" onclick="editJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		  </button>
		
	      <button type="button" onclick="delJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		  </button>
		
	      <button type="button" onclick="enableJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-success btn-sm">
			<i class="fa fa-check"></i>&nbsp;启用
		  </button>
		
	      <button type="button" onclick="disableJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-minus-circle"></i>&nbsp;禁用
		  </button>
		
	      <button type="button" onclick="manualGridRecordInfo();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;手动刷新缓存
		  </button>
		
	      <button type="button" onclick="manualAllEhcache();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;清除所有缓存
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="SysEhcache_datatable"></table>
</div>
<div id="SysEhcache_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(EHCACHE_ID){
	var url = "system/SysEhcacheController.do?goForm&UI_DESIGNCODE=sysehcache";
	var title = "新增缓存配置信息";
	if(EHCACHE_ID){
		url+=("&EHCACHE_ID="+EHCACHE_ID);
		title = "编辑缓存配置信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["1000px","80%"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#SysEhcache_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("SysEhcache_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.EHCACHE_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"SysEhcache_datatable",
		selectColName:"EHCACHE_ID",
		url:"system/SysEhcacheController.do?multiDel",
		callback:function(resultJson){
			$("#SysEhcache_datatable").trigger("reloadGrid"); 
		}
	});
}
function enableJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
       tipMsg:"您确定要启用所选记录吗?",
       tableId:"SysEhcache_datatable",
		selectColName:"EHCACHE_ID",
		url:"system/SysEhcacheController.do?enable",
		callback:function(resultJson){
			$("#SysEhcache_datatable").trigger("reloadGrid"); 
		}
	});
}
function disableJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
       tipMsg:"您确定要禁用所选记录吗?",
       tableId:"SysEhcache_datatable",
		selectColName:"EHCACHE_ID",
		url:"system/SysEhcacheController.do?disable",
		callback:function(resultJson){
			$("#SysEhcache_datatable").trigger("reloadGrid"); 
		}
	});
}
function manualGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"SysEhcache_datatable",
		selectColName:"EHCACHE_ID",
		url:"system/SysEhcacheController.do?manualReloadEhcache",
		callback:function(resultJson){
			$("#SysEhcache_datatable").trigger("reloadGrid"); 
		}
	});
}
function manualAllEhcache(){
  parent.layer.confirm("您确定要清除所有缓存吗", {
				icon: 7,
			    resize :false
			}, function(){
	PlatUtil.ajaxProgress({
					url:"system/SysEhcacheController.do?manualAllEhcache",
					params:{},
					callback : function(resultJson) {
						if (resultJson.success) {
							parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false});
							$("#SysEhcache_datatable").trigger("reloadGrid"); 
						} else {
							parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
						}
					}
				});
  }, function(){
				
			});	
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"SysEhcache_datatable",
		  searchPanelId:"SysEhcache_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848aa5bcd4f32015bcd60b4a40014",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "EHCACHE_ID",label:"主键ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "EHCACHE_NAME",label:"缓存名称",
		         width: 800,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EHCACHE_STATUE",label:"缓存状态",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="0"){
       return "<span class=\"label label-danger\">禁用</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">启用</span>";      
    }
},
		         
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
