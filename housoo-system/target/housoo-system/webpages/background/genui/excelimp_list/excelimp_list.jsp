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
    <title>excel导入配置列表</title>
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
	<h5 id="excelimp_datatablepaneltitle">Excel导入配置列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="excelimp_search" formcontrolid="402881e65bbd9c04015bbdbc21d70008">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.EXCELIMP_CODE_LIKE" auth_type="write" label_value="配置编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.EXCELIMP_NAME_LIKE" auth_type="write" label_value="配置名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.EXCELIMP_TABLENAME_LIKE" auth_type="write" label_value="关联表名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('excelimp_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('excelimp_datatable','excelimp_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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
		
	      <button type="button" onclick="colconfig();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-wrench"></i>&nbsp;列配置
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="excelimp_datatable"></table>
</div>
<div id="excelimp_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(EXCELIMP_ID){
	var url = "appmodel/ExcelImpController.do?goForm&UI_DESIGNCODE=excelimpform";
	var title = "新增excel导入配置信息";
	if(EXCELIMP_ID){
		url+=("&EXCELIMP_ID="+EXCELIMP_ID);
		title = "编辑excel导入配置信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["70%","55%"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#excelimp_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("excelimp_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.EXCELIMP_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"excelimp_datatable",
		selectColName:"EXCELIMP_ID",
		url:"appmodel/ExcelImpController.do?multiDel",
		callback:function(resultJson){
			$("#excelimp_datatable").trigger("reloadGrid"); 
		}
	});
}
function colconfig(){
	var rowData = PlatUtil.getTableOperSingleRecord("excelimp_datatable");
	if(rowData){
        PlatUtil.openWindow({
          title:"列配置",
          area: ["100%","100%"],
          content: "appmodel/ExcelImpController.do?goColForm&UI_DESIGNCODE=colconfform&EXCELIMP_ID="+rowData.EXCELIMP_ID,
          end:function(){
          }
        });
	}
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"excelimp_datatable",
		  searchPanelId:"excelimp_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402881e65bbd9c04015bbdbc21d70008",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "EXCELIMP_ID",label:"配置ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "EXCELIMP_CODE",label:"配置编码",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EXCELIMP_NAME",label:"配置名称",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EXCELIMP_TABLENAME",label:"关联表名称",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EXCELIMP_HEADNUM",label:"表头所在行号",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "EXCELIMP_INTERFACE",label:"数据处理接口",
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
