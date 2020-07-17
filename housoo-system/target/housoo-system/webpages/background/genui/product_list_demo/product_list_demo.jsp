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
    <title>产品列表</title>
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
	<h5 id="product_datatablepaneltitle">产品列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="product_search" formcontrolid="4028819f71c8c2860171c8c660d2000d">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.PRODUCT_NAME_LIKE" auth_type="write" label_value="产品名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.PRODUCT_CODE_EQ" auth_type="write" label_value="产品编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('product_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('product_datatable','product_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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
	<table id="product_datatable"></table>
</div>
<div id="product_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(PRODUCT_ID){
	var url = "demo/ProductController.do?goForm&UI_DESIGNCODE=productform";
	var title = "新增产品信息信息";
	if(PRODUCT_ID){
		url+=("&PRODUCT_ID="+PRODUCT_ID);
		title = "编辑产品信息信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["1000px","400px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#product_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("product_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.PRODUCT_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"product_datatable",
		selectColName:"PRODUCT_ID",
		url:"demo/ProductController.do?multiDel",
		callback:function(resultJson){
			$("#product_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"product_datatable",
		  searchPanelId:"product_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=4028819f71c8c2860171c8c660d2000d",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "PRODUCT_ID",label:"产品id",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "PRODUCT_CODE",label:"产品编码",
		         width: 200,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "PRODUCT_NAME",label:"产品名称",
		         width: 300,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "PRODUCT_PRICE",label:"产品价格",
		         width: 100,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "PRODUCT_CREATETIME",label:"入库时间",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "PRODUCT_MANU",label:"生产日期",
		         width: 100,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "PRODUCT_DESC",label:"产品描述",
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
