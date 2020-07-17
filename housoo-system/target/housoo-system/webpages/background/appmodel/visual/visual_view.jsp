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
    
    <title>My JSP 'dbmanager_view.jsp' starting page</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,plat-ui,tipsy,autocomplete,webuploader">
	</plattag:resources>
  </head>
  
  <body>
	    <div class="ui-layout-west" >
	        <plattag:treepanel panel_title="系统模块管理" id="moduleTree" 
             right_menu="[新增,fa-plus,showModuleWin][修改,fa-pencil,editModuleWin][删除,fa-trash-o,delModule]"	        
	        >
	        </plattag:treepanel>
		</div>
  		<div class="ui-layout-center" >
                    <div class="panel-Title">
                        <h5 id="design_datatablepaneltitle">可视化设计列表 </h5>
                    </div>
                    <div class="titlePanel">
                   	<div class="title-search form-horizontal" id="design_datatable_search">
                   		<table>
                   			<tr>
	                    		<td>查询条件</td>
	                    		<td style="padding-left: 5px;">
	                    			<div class="table-filter">
	                    			    <input type="hidden" name="Q_T.DESIGN_MODULEID_EQ" >
                                		<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);"
                                              class="table-form-control" name="search" readonly="readonly"/>
		                                <div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
		                                    <div class="form-group">
			                                    <plattag:input name="Q_T.DESIGN_NAME_LIKE" auth_type="write" 
			                                    label_value="设计名称" maxlength="100"
			                                    allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
		                                    </div>
		                                    <div class="form-group">
			                                    <plattag:input name="Q_T.DESIGN_CODE_LIKE" auth_type="write" 
			                                    label_value="设计编码" maxlength="100"
			                                    allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
		                                    </div>
											<div class="table-filter-list-bottom">
												<a onclick="PlatUtil.tableSearchReset('design_datatable_search');" class="btn btn-default" href="javascript:void(0);"> 重  置</a>
												<a onclick="PlatUtil.tableDoSearch('design_datatable','design_datatable_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查  询</a>
											</div>
				                        </div>
			                     	</div>
			                     </td>
		                     </tr>
		                 </table>
                   	</div>
					<div class="toolbar">
					<button type="button" onclick="addDesign();" class="btn btn-outline btn-primary btn-sm" ><i class="fa fa-plus"></i>&nbsp;新增</button>
					<button type="button" onclick="editDesign();" class="btn btn-outline btn-info btn-sm"><i class="fa fa-pencil"></i>&nbsp;修改</button>
					<button type="button" onclick="delDesign();" class="btn btn-outline btn-danger btn-sm"><i class="fa fa-trash-o"></i>&nbsp;删除</button>
					<button type="button" onclick="onlineDesign();" class="btn btn-outline btn-primary btn-sm"><i class="fa fa-bolt"></i>&nbsp;在线设计</button>
					<button type="button" onclick="queryDesignPath();" class="btn btn-outline btn-primary btn-sm"><i class="fa fa-search"></i>&nbsp;查看源码</button>
					<div class="btn-group">
                         <button data-toggle="dropdown" class="btn btn-outline btn-info btn-sm dropdown-toggle">其它操作 <span class="caret"></span>
                         </button>
                         <ul class="dropdown-menu">
                             <li>
                             <a onclick="updateDesignTpl();" style="cursor: pointer;">更新模版</a>
                             </li>
                             <li>
                             <a onclick="delDesignCode();" style="cursor: pointer;">清除代码</a>
                             </li>
                         </ul>
                     </div>
					</div>
                    </div>
                    <div class="gridPanel">
                    	<table id="design_datatable"></table>
                    </div>
					<div id="design_datatable_pager"></div>
  		</div>
   
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin,webuploader">
</plattag:resources>
<script type="text/javascript">

function delModule(){
	PlatUtil.deleteZtreeNode({
		treeId:"moduleTree",
		url:"appmodel/ModuleController.do?delRecord"
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

/**
 * 点击树形节点触发的事件
 */
function onModuleTreeClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
			$("input[name='Q_T.DESIGN_MODULEID_EQ']").val("");
			$("#design_paneltitle").text("可视化设计列表");
		}else{
			$("input[name='Q_T.DESIGN_MODULEID_EQ']").val(treeNode.id);
			$("#design_paneltitle").text(treeNode.name+"-->可视化设计列表");
		}
		PlatUtil.tableDoSearch("design_datatable","design_datatable_search");
	}
}

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

function queryDesignPath(){
	var rowData = PlatUtil.getTableOperSingleRecord("design_datatable");
	if(rowData){
		var DESIGN_CODE = rowData.DESIGN_CODE;
		var url = "appmodel/DesignController.do?goQueryCode&DESIGN_CODE="+DESIGN_CODE;
		window.open(url,"_blank");
	}
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

/**
 * 新建系统模块
 */
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

/**
 * 清除生成的代码
 */
function delDesignCode(){
	PlatUtil.operMulRecordForTable({
		tableId:"design_datatable",
		selectColName:"DESIGN_ID",
		url:"appmodel/DesignController.do?delDesignCode",
		callback:function(resultJson){
			
		}
	});
}

$(function(){
	  $("body").layout({resizable:true, west__size:280,west__closable:false});
	  PlatUtil.initUIComp();
	  PlatUtil.initZtree({
		  treeId:"moduleTree",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoCompleteType:"1",
		  edit : {
				enable : false
		  },
		  callback: {
			  onRightClick: PlatUtil.onZtreeRightClick,
			  onClick:onModuleTreeClick
		  },
		  async : {
			url:"common/baseController.do?tree",
			otherParam : {
				//树形表名称
				"tableName" : "PLAT_APPMODEL_MODULE",
				//键和值的列名称
				"idAndNameCol" : "MODULE_ID,MODULE_NAME",
				//查询其它部门列名称
				"targetCols" : "MODULE_CODE,MODULE_PATH,MODULE_PARENTID",
				//最先读取的根节点的值
				"loadRootId" : "0",
				//需要回填的值
				"needCheckIds":"",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//树形标题
				"treeTitle":"系统模块树"
			}
		  }
	  });
	  PlatUtil.initJqGrid({
		  tableId:"design_datatable",
		  searchPanelId:"design_datatable_search",
		  url: "appmodel/DesignController.do?datagrid",
		  colModel: [
               {  name: "DESIGN_ID",hidden:true},
               {  name: "DESIGN_CODE",label:"设计编码",width: 200, align:"left",sortable:false},
               {  name: "DESIGN_NAME",label:"设计名称",width: 400, align:"left",sortable:false},
               {  name: "MODULE_NAME",label:"所属模块名称",width: 200, align:"left",sortable:false}
           ]
	  });
	  
});
</script>
