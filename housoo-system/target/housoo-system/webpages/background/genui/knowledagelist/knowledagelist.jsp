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
    <title>技术知识库列表</title>
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
	<h5 id="techknowledge_datatablepaneltitle">技术知识列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="techknowledge_search" formcontrolid="402848a55cf17c59015cf197445e0026">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.KNOWLEDGE_TITLE_LIKE" auth_type="write" label_value="知识标题" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
								    <plattag:select placeholder="" istree="false" label_col_num="3" label_value="技术类别" style="width:100%;" allowblank="true" comp_col_num="9" auth_type="write" static_values="" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'techtype',ORDER_TYPE:'ASC'}" name="Q_T.KNOWLEDGE_TYPE_EQ">
								    </plattag:select>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('techknowledge_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('techknowledge_datatable','techknowledge_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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
		
	      <button type="button" onclick="queryDetail();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;查看
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="techknowledge_datatable"></table>
</div>
<div id="techknowledge_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(KNOWLEDGE_ID){
	var url = "appmodel/KnowledgeController.do?goForm&UI_DESIGNCODE=knowledgeform";
	var title = "新增技术知识信息";
	if(KNOWLEDGE_ID){
		url+=("&KNOWLEDGE_ID="+KNOWLEDGE_ID);
		title = "编辑技术知识信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["90%","90%"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#techknowledge_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("techknowledge_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.KNOWLEDGE_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"techknowledge_datatable",
		selectColName:"KNOWLEDGE_ID",
		url:"appmodel/KnowledgeController.do?multiDel",
		callback:function(resultJson){
			$("#techknowledge_datatable").trigger("reloadGrid"); 
		}
	});
}
function queryDetail(){
	var rowData = PlatUtil.getTableOperSingleRecord("techknowledge_datatable");
	if(rowData){
        var KNOWLEDGE_ID = rowData.KNOWLEDGE_ID;
		var url = "appmodel/KnowledgeController.do?goDetail&KNOWLEDGE_ID="+KNOWLEDGE_ID;
		window.open(url,"_blank");
	}
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"techknowledge_datatable",
		  searchPanelId:"techknowledge_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55cf17c59015cf197445e0026",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "KNOWLEDGE_ID",label:"知识ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "DIC_NAME",label:"技术类别",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "KNOWLEDGE_TITLE",label:"知识标题",
		         width: 400,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "KNOWLEDGE_CREATETIME",label:"创建时间",
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
