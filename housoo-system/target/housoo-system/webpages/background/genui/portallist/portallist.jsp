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
    <title>门户主题列表</title>
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
	<h5 id="portaltheme_datatablepaneltitle">门户主题列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="portaltheme_search" formcontrolid="402848a55c854b8a015c85822f10000a">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.THEME_NAME_LIKE" auth_type="write" label_value="主题名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.CREATOR_NAME_LIKE" auth_type="write" label_value="创建人姓名" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('portaltheme_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('portaltheme_datatable','portaltheme_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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
		
	      <button type="button" onclick="designOnLine();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-bolt"></i>&nbsp;在线设计
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="portaltheme_datatable"></table>
</div>
<div id="portaltheme_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(THEME_ID){
	var url = "appmodel/PortalThemeController.do?goForm&UI_DESIGNCODE=portalthemeform";
	var title = "新增门户主题信息";
	if(THEME_ID){
		url+=("&THEME_ID="+THEME_ID);
		title = "编辑门户主题信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["500px","180px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#portaltheme_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("portaltheme_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.THEME_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"portaltheme_datatable",
		selectColName:"THEME_ID",
		url:"appmodel/PortalThemeController.do?multiDel",
		callback:function(resultJson){
			$("#portaltheme_datatable").trigger("reloadGrid"); 
		}
	});
}
function designOnLine(){
	var rowData = PlatUtil.getTableOperSingleRecord("portaltheme_datatable");
	if(rowData){
		var THEME_ID = rowData.THEME_ID;
		window.open("appmodel/PortalThemeController.do?goDesign&THEME_ID="+THEME_ID,"_blank");
	}
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"portaltheme_datatable",
		  searchPanelId:"portaltheme_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55c854b8a015c85822f10000a",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "THEME_ID",label:"主题ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "THEME_NAME",label:"主题名称",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "THEME_CREATETIME",label:"创建时间",
		         width: 150,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "IS_DEFAULT",label:"是否默认主题",
		         width: 200,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="-1"){
       return "<span class=\"label label-danger\">否</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">是</span>";      
    }
},
		         
		         sortable:false
		         },
		         {name: "CREATOR_ID",label:"主题创建人ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "CREATOR_NAME",label:"创建人姓名",
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
