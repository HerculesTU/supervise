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
    <title>全局URL列表</title>
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
	<h5 id="globalurl_datatablepaneltitle">全局URL列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="globalurl_search">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
								    <plattag:radio name="Q_T.URL_FILTERTYPE_EQ" auth_type="write" label_col_num="3" label_value="过滤类型" static_values="所有类型:,匿名类型:1,会话类型:2" dyna_interface="" dyna_param="" select_first="true" allowblank="true" is_horizontal="true" comp_col_num="9">
								    </plattag:radio>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.URL_ADDRESS_LIKE" auth_type="write" label_value="访问地址" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('globalurl_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('globalurl_datatable','globalurl_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<button type="button" onclick="addJqGridRecordInfo();" platreskey="addGlobalUrl" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="editJqGridRecordInfo();" platreskey="editGlobalUrl" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		</button>
		<button type="button" onclick="delJqGridRecordInfo();" platreskey="delGlobalUrl" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		</button>
	</div>
</div>

<div class="gridPanel">
	<table id="globalurl_datatable"></table>
</div>
<div id="globalurl_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(URL_ID){
	var url = "system/GlobalUrlController.do?goForm&UI_DESIGNCODE=globalform";
	var title = "新增全局URL信息";
	if(URL_ID){
		url+=("&URL_ID="+URL_ID);
		title = "编辑全局URL信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["800px","300px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#globalurl_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("globalurl_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.URL_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"globalurl_datatable",
		selectColName:"URL_ID",
		url:"system/GlobalUrlController.do?multiDel",
		callback:function(resultJson){
			$("#globalurl_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"globalurl_datatable",
		  searchPanelId:"globalurl_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402881e65b9866c7015b986997eb0008",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "URL_ID",label:"URL的ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "URL_ADDRESS",label:"访问地址",
		         width: 1000,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "URL_FILTERTYPE",label:"过滤类型",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="1"){
       return "<span class=\"label label-warning\">匿名类型</span>";
    }else if(cellvalue=="2"){
       return "<span class=\"label label-primary\">会话类型</span>";      
    }
    
},
		         
		         sortable:false
		         },
		         {name: "URL_CREATETIME",label:"URL的入库时间",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
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
