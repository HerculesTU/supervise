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
    <title>字典管理列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,tipsy,autocomplete,pinyin,nicevalid"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,tipsy,autocomplete,pinyin,nicevalid"></plattag:resources>
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
	<h5 id="dictionary_datatablepaneltitle">字典信息列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="dictionary_search">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.DIC_NAME_LIKE" auth_type="write" label_value="字典名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.DIC_VALUE_LIKE" auth_type="write" label_value="字典值" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.DIC_DICTYPE_CODE_EQ" auth_type="write" label_value="类别编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('dictionary_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('dictionary_datatable','dictionary_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<button type="button" onclick="addDictionary();" platreskey="addDictionary" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="editDictionary();" platreskey="editDictionary" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		</button>
		<button type="button" onclick="delDictionary();" platreskey="delDictionary" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		</button>
		<button type="button" onclick="saveDictionarySn();" platreskey="saveDictionarySn" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-sort"></i>&nbsp;保存排序
		</button>
	</div>
</div>

<div class="gridPanel">
	<table id="dictionary_datatable"></table>
</div>
<div id="dictionary_datatable_pager"></div>
<script type="text/javascript">
function addDictionary(DIC_ID){
    var url = "system/DictionaryController.do?goform&UI_DESIGINCODE=dictionaryform";
    var title = "新增字典信息";
    if(DIC_ID){
        title = "编辑字典信息";
	    url+="&DIC_ID="+DIC_ID;
    }else{
        var selectDicType = $.fn.zTree.getZTreeObj("dicTypeTree").getSelectedNodes()[0];
        if(selectDicType){
           var dicTypeId = selectDicType.id;
           if(dicTypeId=="0"){
                parent.layer.alert("请先选择左侧字典类别!",{icon: 2,resize:false});
                return;
           }else{
                url+="&dicTypeId="+dicTypeId;
           }
        }else{
           parent.layer.alert("请先选择左侧字典类别!",{icon: 2,resize:false});
           return;
        }
    }
    PlatUtil.openWindow({
      title:title,
      area: ["800px","500px"],
      content: url,
      end:function(){
        if(PlatUtil.isSubmitSuccess()){
          $("#dictionary_datatable").trigger("reloadGrid"); 
        }
      }
    });
	
}
function editDictionary(){
	var rowData = PlatUtil.getTableOperSingleRecord("dictionary_datatable");
	if(rowData){
		var DIC_ID = rowData.DIC_ID;
		addDictionary(DIC_ID);
	}
}
function delDictionary(){
	PlatUtil.operMulRecordForTable({
		tableId:"dictionary_datatable",
		selectColName:"DIC_ID",
		url:"system/DictionaryController.do?multiDel",
		callback:function(resultJson){
			$("#dictionary_datatable").trigger("reloadGrid"); 
		}
	});
}
function saveDictionarySn(){
	var dicTypeCode = $("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val();
	if(dicTypeCode!=""){
		var rowDatas = $("#dictionary_datatable").jqGrid("getRowData");
		if(rowDatas.length>0){
			var dicIds = "";
			$.each(rowDatas,function(index,obj){
				if(index>0){
					dicIds+=",";
				}
				dicIds+=obj.DIC_ID;
			});
			PlatUtil.ajaxProgress({
				url:"system/DictionaryController.do?updateSn",
				params:{
					dicIds:dicIds
				},
				callback:function(resultJson){
					parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false});
					$("#dictionary_datatable").trigger("reloadGrid"); 
				}
			})
		}
	}else{
		parent.layer.alert("请选择字典类别后,然后再进行拖拽排序!",{icon: 7,resize:false});
	}
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"dictionary_datatable",
		  searchPanelId:"dictionary_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402881e65b31bd7f015b31be01160001",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "DIC_ID",label:"字典ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "DIC_NAME",label:"字典名称",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "DIC_VALUE",label:"字典值",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "DIC_SN",label:"字典排序",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "DICTYPE_NAME",label:"类别名称",
		         width: 300,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "DIC_DICTYPE_CODE",label:"类别编码",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         }
           ]
	 });
});
</script>
</div>
   <div class="ui-layout-west" platundragable="true" compcode="direct_layout">
   <plattag:treepanel panel_title="字典类别管理" id="dicTypeTree" right_menu="[新增,fa fa-plus,showTreeInfoWin,addDicType][编辑,fa fa-pencil,editTreeInfoWin,editDicType][删除,fa fa-trash,delTreeNodeInfo,delDicType]">
</plattag:treepanel>
<script type="text/javascript">
function showTreeInfoWin(DICTYPE_ID) {
	var url = "system/DicTypeController.do?goform&UI_DESIGNCODE=dictypeform";
	var title = "新增字典类别";
	if(DICTYPE_ID){
		title = "编辑字典类别";
		url+=("&DICTYPE_ID="+DICTYPE_ID);
	}else{
		PlatUtil.hideZtreeRightMenu();
		var selectTreeNode = $.fn.zTree.getZTreeObj("dicTypeTree").getSelectedNodes()[0];
		var DICTYPE_ID = selectTreeNode.id;
		url+=("&parentId="+DICTYPE_ID);
	}
	PlatUtil.openWindow({
		title:title,
		area: ["800px","280px"],
		content: url,
		end:function(){
		  if(PlatUtil.isSubmitSuccess()){
			  $.fn.zTree.getZTreeObj("dicTypeTree").reAsyncChildNodes(null, "refresh");
		  }
		}
	});
}
function editTreeInfoWin(){
	PlatUtil.hideZtreeRightMenu();
	var selectTreeNode = $.fn.zTree.getZTreeObj("dicTypeTree").getSelectedNodes()[0];
	var DICTYPE_ID = selectTreeNode.id;
	if(DICTYPE_ID=="0"){
		parent.layer.alert("根节点不能进行编辑!",{icon: 2,resize:false});
	}else{
		showTreeInfoWin(DICTYPE_ID);
	}
}
function delTreeNodeInfo(){
	PlatUtil.deleteZtreeNode({
		treeId:"dicTypeTree",
		url:"system/DicTypeController.do?delRecord"
	});
}
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val("");
			$("#dictionary_paneltitle").text("字典信息列表");
		}else{
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val(treeNode.DICTYPE_CODE);
			$("#dictionary_paneltitle").text(treeNode.name+"-->字典信息列表");
            $("#dictionary_datatable").jqGrid("sortableRows");
		}
		//刷新右侧列表
		PlatUtil.tableDoSearch("dictionary_datatable","dictionary_search");
	}
}  
  $(function(){
	  PlatUtil.initZtree({
		  treeId:"dicTypeTree",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoCompleteType:"1",
		  callback: {
			  onRightClick: PlatUtil.onZtreeRightClick,
			  onClick:onTreePanelClick,
			  onDrop: PlatUtil.onZtreeNodeDrop
		  },
		  async : {
			url:"system/DicTypeController.do?tree",
			otherParam : {
				//树形表名称
				"tableName" : "PLAT_SYSTEM_DICTYPE",
				//键和值的列名称
				"idAndNameCol" : "DICTYPE_ID,DICTYPE_NAME",
				//查询其它部门列名称
				"targetCols" : "DICTYPE_CODE,DICTYPE_PARENTID,DICTYPE_PATH",
				//最先读取的根节点的值
				"loadRootId" : "0",
				//需要回填的值
				"needCheckIds":"",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//根据节点名称
				"treeTitle":"字典类别树"
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

</script>
