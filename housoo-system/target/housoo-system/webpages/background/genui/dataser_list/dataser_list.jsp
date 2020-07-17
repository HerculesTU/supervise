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
    <title>服务注册列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,tipsy,autocomplete,pinyin,nicevalid,slimscroll"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,tipsy,autocomplete,pinyin,nicevalid,slimscroll"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout" layoutsize="&quot;west__size&quot;:280">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="baselist_datatablepaneltitle">注册服务列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="baselist_search" formcontrolid="297ecf12633e955a01633ef1271d0075">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="CATALOG_ID" value="">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.DATASER_CODE_LIKE" auth_type="write" label_value="服务编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.DATASER_NAME_LIKE" auth_type="write" label_value="服务名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_R.DATARES_NAME_LIKE" auth_type="write" label_value="绑定资源名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('baselist_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('baselist_datatable','baselist_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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
	<table id="baselist_datatable"></table>
</div>
<div id="baselist_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(DATASER_ID){
	var url = "metadata/DataSerController.do?goForm&UI_DESIGNCODE=dataser_form";
	var title = "新增数据服务信息";
	if(DATASER_ID){
		url+=("&DATASER_ID="+DATASER_ID);
		title = "编辑数据服务信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["1000px","350px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#baselist_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("baselist_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.DATASER_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"baselist_datatable",
		selectColName:"DATASER_ID",
		url:"metadata/DataSerController.do?multiDel",
		callback:function(resultJson){
			$("#baselist_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"baselist_datatable",
		  searchPanelId:"baselist_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=297ecf12633e955a01633ef1271d0075",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "DATASER_ID",label:"资源ID",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "DATARES_NAME",label:"绑定资源",
		         width: 200,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "DATASER_CODE",label:"服务编码",
		         width: 150,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "DATASER_NAME",label:"服务名称",
		         width: 200,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
  var DATASER_ID = rowObject.DATASER_ID;
  var DATASER_NAME = rowObject.DATASER_NAME;
  var href = "<a href=\"javascript:void(0)\"";
  href+=" onclick=\"showReqserDetail('"+DATASER_ID+"');\" ";
  href += " ><span style='text-decoration:underline;color:green;'>"+DATASER_NAME+"</span></a>";
  return href;
},

		         
		         sortable:false
		         },
		         {name: "DATASER_STATUS",label:"服务状态",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="-1"){
       return "<span class=\"label label-danger\">禁用</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">启用</span>";      
    }
    
},

		         
		         sortable:false
		         },
		         {name: "DATASER_CACHEABLE",label:"启用缓存",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="-1"){
       return "<span class=\"label label-danger\">不启用</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">启用</span>";      
    }
    
},

		         
		         sortable:false
		         },
		         {name: "DATASER_RELOG",label:"记录日志",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="-1"){
       return "<span class=\"label label-danger\">不记录</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">记录</span>";      
    }
    
},

		         
		         sortable:false
		         }
           ]
	 });
});
</script>
</div>
   <div class="ui-layout-west" platundragable="true" compcode="direct_layout">
   <plattag:treepanel id="basetree" panel_title="数据目录树">
</plattag:treepanel>
<script type="text/javascript">
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
			$("input[name='CATALOG_ID']").val("");
			$("#baselist_datatablepaneltitle").text("注册服务列表");
		}else{
			$("input[name='CATALOG_ID']").val(treeNode.id);
			$("#baselist_datatablepaneltitle").text(treeNode.name+"-->注册服务列表");
		}
		//刷新右侧列表
		PlatUtil.tableDoSearch("baselist_datatable","baselist_search");
	}
}  
  $(function(){
	  PlatUtil.initZtree({
		  treeId:"basetree",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoCompleteType:"1",
		  callback: {
			  onRightClick: PlatUtil.onZtreeRightClick,
			  onClick:onTreePanelClick,
			  onDrop: PlatUtil.onZtreeNodeDrop
		  },
		  async : {
			url:"common/baseController.do?tree",
			otherParam : {
				//树形表名称
				"tableName" : "PLAT_METADATA_CATALOG",
				//键和值的列名称
				"idAndNameCol" : "CATALOG_ID,CATALOG_NAME",
				//查询其它部门列名称
				"targetCols" : "CATALOG_PARENTID,CATALOG_LEVEL,CATALOG_PATH",
				//最先读取的根节点的值
				"loadRootId" : "0",
				//需要回填的值
				"needCheckIds":"",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//根据节点名称
				"treeTitle":"全部目录"
			}
		  }
	  });
	  
});
</script>

</div>
</div>
  </body>
</html>

<script type="text/javascript">
function showReqserDetail(DATASER_ID){
	var url = "metadata/DataSerController.do?goDetail&DATASER_ID="+DATASER_ID;
	PlatUtil.openWindow({
		title:"服务文档明细",
		area: ["100%","100%"],
		content: url,
		end:function(){
			
		}
	});
}
</script>
