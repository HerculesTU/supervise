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
    <title>门户组件列表</title>
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
	<h5 id="portalcomp_datatablepaneltitle">门户组件列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="portalcomp_search" formcontrolid="402848a55c9b0564015c9b0d3ce90009">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.COMP_NAME_LIKE" auth_type="write" label_value="组件名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
								    <plattag:select placeholder="" istree="false" label_col_num="3" label_value="组件类别" style="width:100%;" allowblank="true" comp_col_num="9" auth_type="write" static_values="" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'COMPTYPE',ORDER_TYPE:'ASC'}" name="Q_T.COMP_TYPECODE_EQ">
								    </plattag:select>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('portalcomp_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('portalcomp_datatable','portalcomp_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="addJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		  </button>
		
	      <button type="button" onclick="editJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		  </button>
		
	      <button type="button" onclick="delJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="portalcomp_datatable"></table>
</div>
<div id="portalcomp_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(COMP_ID){
	var url = "appmodel/PortalCompController.do?goForm&UI_DESIGNCODE=portalcompform";
	var title = "新增门户组件信息";
	if(COMP_ID){
		url+=("&COMP_ID="+COMP_ID);
		title = "编辑门户组件信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["1000px","300px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#portalcomp_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("portalcomp_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.COMP_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"portalcomp_datatable",
		selectColName:"COMP_ID",
		url:"appmodel/PortalCompController.do?multiDel",
		callback:function(resultJson){
			$("#portalcomp_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"portalcomp_datatable",
		  searchPanelId:"portalcomp_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55c9b0564015c9b0d3ce90009",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "COMP_ID",label:"组件ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "DIC_NAME",label:"组件类别",
		         width: 250,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "COMP_NAME",label:"组件名称",
		         width: 300,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "COMP_URL",label:"访问URL",
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
