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
    <title>可视化编程列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,tipsy,autocomplete,pinyin,nicevalid,webuploader"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,tipsy,autocomplete,pinyin,nicevalid,webuploader"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout" layoutsize="&quot;west__size&quot;:280">
   <div class="ui-layout-west" platundragable="true" compcode="direct_layout">
   <plattag:treepanel id="moduleTree" panel_title="系统模块管理" right_menu="[新增,fa fa-plus,showModuleWin][编辑,fa fa-pencil,editModuleWin][删除,fa fa-trash,delModule]">
</plattag:treepanel>
<script type="text/javascript">
function showModuleWin(editModuleId) {
	var url = "appmodel/ModuleController.do?goform";
	var title = "新增系统模块";
	if(editModuleId){
		title = "编辑系统模块";
		url+=("&MODULE_ID="+editModuleId);
	}else{
		PlatUtil.hideZtreeRightMenu();
		var selectModule = $.fn.zTree.getZTreeObj("moduleTree").getSelectedNodes()[0];
		var PARENT_ID = selectModule.id;
		url+=("&PARENT_ID="+PARENT_ID);
	}
	PlatUtil.openWindow({
		title:title,
		area: ["800px","300px"],
		content: url,
		end:function(){
		  if(PlatUtil.isSubmitSuccess()){
			  $.fn.zTree.getZTreeObj("moduleTree").reAsyncChildNodes(null, "refresh");
		  }
		}
	});
}
function editModuleWin(){
	PlatUtil.hideZtreeRightMenu();
	var selectModule = $.fn.zTree.getZTreeObj("moduleTree").getSelectedNodes()[0];
	var MODULE_ID = selectModule.id;
	if(MODULE_ID=="0"){
		parent.layer.alert("根节点不能进行编辑!",{icon: 2,resize:false});
	}else{
		showModuleWin(MODULE_ID);
	}
}
function delModule(){
	PlatUtil.deleteZtreeNode({
		treeId:"moduleTree",
		url:"appmodel/ModuleController.do?delRecord"
	});
}
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
			$("input[name='Q_M.MODULE_PATH_LIKE']").val("");
			$("#design_datatablepaneltitle").text("可视化设计列表");
		}else{
			$("input[name='Q_M.MODULE_PATH_LIKE']").val(treeNode.id);
			$("#design_datatablepaneltitle").text(treeNode.name+"-->可视化设计列表");
		}
		PlatUtil.tableDoSearch("design_datatable","design_search");
	}
}  
  $(function(){
	  PlatUtil.initZtree({
		  treeId:"moduleTree",
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
				"tableName" : "PLAT_APPMODEL_MODULE",
				//键和值的列名称
				"idAndNameCol" : "MODULE_ID,MODULE_NAME",
				//查询其它部门列名称
				"targetCols" : "MODULE_CODE,MODULE_PARENTID,MODULE_PATH",
				//最先读取的根节点的值
				"loadRootId" : "0",
				//需要回填的值
				"needCheckIds":"",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//根据节点名称
				"treeTitle":"系统模块树"
			}
		  }
	  });
	  
});
</script>

</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="design_datatablepaneltitle">可视化设计列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="design_search" formcontrolid="402848a55c801bb5015c80200f560012">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_M.MODULE_PATH_LIKE" value="">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.DESIGN_NAME_LIKE" auth_type="write" label_value="设计名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.DESIGN_CODE_LIKE" auth_type="write" label_value="设计编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('design_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('design_datatable','design_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="addDesign();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		  </button>
		
	      <button type="button" onclick="editDesign();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		  </button>
		
	      <button type="button" onclick="onlineDesign();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-bolt"></i>&nbsp;在线设计
		  </button>
		
          <span id="importjson" impfiletypes="json" platreskey="" impfilesize="10,485,760" imptableid="design_datatable" impurl="appmodel/DesignController.do?impConfig" class="btn btn-outline btn-info btn-sm platImpUploadFile"><i class="fa fa-upload"></i>&nbsp;导入配置</span>
		
	       <div class="btn-group" id="OTHER_OPERDIV">
                 <button data-toggle="dropdown" class="btn btn-outline btn-info btn-sm dropdown-toggle">其它操作 <span class="caret"></span>
                 </button>
                 <ul class="dropdown-menu">
	                     <li platreskey="">
	                     <a onclick="exportConfig();" style="cursor: pointer;">合并导出</a>
	                     </li>
	                     <li platreskey="">
	                     <a onclick="exportSingleConfig();" style="cursor: pointer;">拆分导出</a>
	                     </li>
	                     <li platreskey="">
	                     <a onclick="queryDesignPath();" style="cursor: pointer;">查看源码</a>
	                     </li>
	                     <li platreskey="">
	                     <a onclick="delDesignCode();" style="cursor: pointer;">清除代码</a>
	                     </li>
	                     <li platreskey="">
	                     <a onclick="updateDesignTpl();" style="cursor: pointer;">更新模版</a>
	                     </li>
	                     <li platreskey="">
	                     <a onclick="cloneDesign();" style="cursor: pointer;">克隆设计</a>
	                     </li>
	                     <li platreskey="">
	                     <a onclick="delDesign();" style="cursor: pointer;">删除</a>
	                     </li>
                 </ul>
            </div>
		  
		
		
		
		
		
		
		
	</div>
</div>

<div class="gridPanel">
	<table id="design_datatable"></table>
</div>
<div id="design_datatable_pager"></div>
<script type="text/javascript">
function addDesign(){
	var selectModule = $.fn.zTree.getZTreeObj("moduleTree").getSelectedNodes()[0];
	if(selectModule){
		var MODULE_ID = selectModule.id;
		if(MODULE_ID=="0"){
			parent.layer.alert("请先选择左侧系统模块!",{icon: 2,resize:false});
		}else{
			showDesignWindow("新增设计信息","appmodel/DesignController.do?goform&MODULE_ID="+MODULE_ID);
		}
	}else{
		parent.layer.alert("请先选择左侧系统模块!",{icon: 2,resize:false});
	}
}
function editDesign(){
	var rowData = PlatUtil.getTableOperSingleRecord("design_datatable");
	if(rowData){
		var DESIGN_ID = rowData.DESIGN_ID;
		showDesignWindow("编辑设计信息","appmodel/DesignController.do?goform&DESIGN_ID="+DESIGN_ID);
	}
}
function onlineDesign(){
	var rowData = PlatUtil.getTableOperSingleRecord("design_datatable");
	if(rowData){
		var DESIGN_ID = rowData.DESIGN_ID;
		window.open("appmodel/DesignController.do?goDesign&DESIGN_ID="+DESIGN_ID,"_blank");
	}
}

function exportConfig(){
    var selectDatas = PlatUtil.getTableOperMulRecord("design_datatable");
	if(selectDatas){
       var selectDesignIds = "";
       $.each(selectDatas,function(index,obj){
        if(index>0){
          selectDesignIds+=",";
        }
        selectDesignIds+= obj.DESIGN_ID;
       });
       var url = __ctxPath+"/appmodel/DesignController/exportConfig.do?designIds="+selectDesignIds;
		window.location.href=url;
    }
}

function exportSingleConfig(){
    var selectDatas = PlatUtil.getTableOperMulRecord("design_datatable");
	if(selectDatas){
       var selectDesignIds = "";
       $.each(selectDatas,function(index,obj){
        if(index>0){
          selectDesignIds+=",";
        }
        selectDesignIds+= obj.DESIGN_ID;
       });
       var url = __ctxPath+"/appmodel/DesignController/exportSignle.do?designIds="+selectDesignIds;
		window.location.href=url;
    }
}

function queryDesignPath(){
	var rowData = PlatUtil.getTableOperSingleRecord("design_datatable");
	if(rowData){
		var DESIGN_CODE = rowData.DESIGN_CODE;
		var url = "appmodel/DesignController.do?goQueryCode&DESIGN_CODE="+DESIGN_CODE;
		window.open(url,"_blank");
	}
}
function delDesignCode(){
	PlatUtil.operMulRecordForTable({
		tableId:"design_datatable",
		selectColName:"DESIGN_ID",
		url:"appmodel/DesignController.do?delDesignCode",
		callback:function(resultJson){
			
		}
	});
}
function updateDesignTpl(){
	PlatUtil.operMulRecordForTable({
		tableId:"design_datatable",
		selectColName:"DESIGN_ID",
		tipMsg:"注意!该操作将会导致所设计UI控件的模版更新为最新版本,您确定要进行?",
		url:"appmodel/DesignController.do?updateControlTpl",
		callback:function(resultJson){
			$("#design_datatable").trigger("reloadGrid"); 
		}
	});
}
function cloneDesign(){
	var rowData = PlatUtil.getTableOperSingleRecord("design_datatable");
	if(rowData){
		var DESIGN_ID = rowData.DESIGN_ID;
		var DESIGN_NAME = rowData.DESIGN_NAME;
		PlatUtil.openWindow({
			title:"克隆["+DESIGN_NAME+"]",
			area: ["800px","350px"],
			content: "appmodel/DesignController.do?goCloneView&DESIGN_ID="+DESIGN_ID,
			end:function(){
			  if(PlatUtil.isSubmitSuccess()){
				  $("#design_datatable").trigger("reloadGrid"); 
			  }
			}
		});
	}
}
function delDesign(){
	PlatUtil.operMulRecordForTable({
		tableId:"design_datatable",
		selectColName:"DESIGN_ID",
		url:"appmodel/DesignController.do?multiDel",
		callback:function(resultJson){
			$("#design_datatable").trigger("reloadGrid"); 
		}
	});
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"design_datatable",
		  searchPanelId:"design_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55c801bb5015c80200f560012",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "DESIGN_ID",label:"设计ID",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "DESIGN_CODE",label:"设计编码",
		         width: 200,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "DESIGN_NAME",label:"设计名称",
		         width: 400,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "MODULE_NAME",label:"模块名称",
		         width: 200,align:"left",
		         
		         

		         
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
function showDesignWindow(title,url){
	PlatUtil.openWindow({
		title:title,
		area: ["1200px","550px"],
		content: url,
		end:function(){
		  if(PlatUtil.isSubmitSuccess()){
			  $("#design_datatable").trigger("reloadGrid"); 
		  }
		}
	});
}
</script>
