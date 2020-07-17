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
    <title>WORD模版列表</title>
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
	<h5 id="wordtpl_datatablepaneltitle">WORD模版列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="wordtpl_search" formcontrolid="402848a55c5c6997015c5c777db7000a">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.TPL_NAME_LIKE" auth_type="write" label_value="模版名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.TPL_CODE_LIKE" auth_type="write" label_value="模版编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('wordtpl_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('wordtpl_datatable','wordtpl_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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
		
	      <button type="button" onclick="editJqGridRecordInfo2();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑代码
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="wordtpl_datatable"></table>
</div>
<div id="wordtpl_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(TPL_ID){
	var url = "appmodel/WordTplController.do?goForm&UI_DESIGNCODE=wordtplform";
	var title = "新增WORD模版信息";
	if(TPL_ID){
		url+=("&TPL_ID="+TPL_ID);
		title = "编辑WORD模版信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["90%","90%"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#wordtpl_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("wordtpl_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.TPL_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"wordtpl_datatable",
		selectColName:"TPL_ID",
		url:"appmodel/WordTplController.do?multiDel",
		callback:function(resultJson){
			$("#wordtpl_datatable").trigger("reloadGrid"); 
		}
	});
}
function editJqGridRecordInfo2(){
	var rowData = PlatUtil.getTableOperSingleRecord("wordtpl_datatable");
	if(rowData){
        var TPL_ID = rowData.TPL_ID;
        PlatUtil.openWindow({
          title:"代码编辑",
          area: ["90%","90%"],
          content: "appmodel/WordTplController.do?goEditCode&TPL_ID="+TPL_ID,
          end:function(){
              if(PlatUtil.isSubmitSuccess()){
                  //弹出框提交成功后,需要回调的代码
                  $("#wordtpl_datatable").trigger("reloadGrid"); 
              }
          }
        });
	}
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"wordtpl_datatable",
		  searchPanelId:"wordtpl_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55c5c6997015c5c777db7000a",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "TPL_ID",label:"模版ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "TPL_CODE",label:"模版编码",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "TPL_NAME",label:"模版名称",
		         width: 400,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "TPL_INTER",label:"数据回填接口",
		         width: 300,align:"left",
		         
		         
		         
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
