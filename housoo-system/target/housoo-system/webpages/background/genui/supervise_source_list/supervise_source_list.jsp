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
    <title>督办来源列表</title>
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
   <div class="titlePanel">
	<div class="title-search form-horizontal" id="supervisesourcetable_search" formcontrolid="4028819e717185870171718db1cc0011">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_E.SOURCE_NAME_LIKE" auth_type="write" label_value="督办来源" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('supervisesourcetable_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('supervisesourcetable_datatable','supervisesourcetable_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="addJqGridRecordInfo();" platreskey="" id="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		  </button>
		
	      <button type="button" onclick="editJqGridRecordInfo();" platreskey="" id="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		  </button>
		
	      <button type="button" onclick="delJqGridRecordInfo();" platreskey="" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="supervisesourcetable_datatable"></table>
</div>
<div id="supervisesourcetable_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(RECORD_ID){
	var url = "supervise/SuperviseSourceController.do?goForm&UI_DESIGNCODE=supervise_source_form";
	var title = "新增督办来源信息";
	if(RECORD_ID){
		url+=("&RECORD_ID="+RECORD_ID);
		title = "编辑督办来源信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["800px","400px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#supervisesourcetable_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("supervisesourcetable_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.RECORD_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"supervisesourcetable_datatable",
		selectColName:"RECORD_ID",
		url:"supervise/SuperviseSourceController.do?multiDel",
		callback:function(resultJson){
			$("#supervisesourcetable_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"supervisesourcetable_datatable",
		  searchPanelId:"supervisesourcetable_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=4028819e717185870171718db1cc0011",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "RECORD_ID",label:"主键",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "SOURCE_NAME",label:"督办来源",
		         width: 150,align:"left",
		         
		         

		         sortable:true,index: "O_E.SOURCE_NAME"
		         
		         },
		         {name: "FILE_NAME",label:"文件名",
		         width: 150,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "CREATE_BY",label:"创建者",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "SYSUSER_NAME",label:"用户姓名",
		         width: 100,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "CREATE_TIME",label:"创建时间",
		         width: 180,align:"left",
		         
		         

		         sortable:true,index: "O_E.CREATE_TIME"
		         
		         },
		         {name: "UPDATE_BY",label:"更新者",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "UPDATE_TIME",label:"更新时间",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "REMARKS",label:"备注",
		         width: 150,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "DEL_FLAG",label:"删除标记",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
  if(cellvalue=="2"){
       return "<span class=\"label label-danger\">禁用</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">正常</span>";      
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
