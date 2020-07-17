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
    <title>单位部门列表</title>
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
   <div class="ui-layout-west" platundragable="true" compcode="direct_layout">
   <plattag:treepanel panel_title="单位管理" id="companyTree" right_menu="[新增,fa fa-plus,showTreeInfoWin,addCompany][编辑,fa fa-pencil,editTreeInfoWin,editCompany][删除,fa fa-trash,delTreeNodeInfo,delCompany]">
</plattag:treepanel>
<script type="text/javascript">
function showTreeInfoWin(COMPANY_ID) {
	var url = "system/CompanyController.do?goForm&UI_DESIGNCODE=companyForm";
	var title = "新增单位";
	if(COMPANY_ID){
		title = "编辑单位";
		url+=("&COMPANY_ID="+COMPANY_ID);
	}else{
		PlatUtil.hideZtreeRightMenu();
		var selectTreeNode = $.fn.zTree.getZTreeObj("companyTree").getSelectedNodes()[0];
		var COMPANY_ID = selectTreeNode.id;
		url+=("&COMPANY_PARENTID="+COMPANY_ID);
	}
	PlatUtil.openWindow({
		title:title,
		area: ["1200px","400px"],
		content: url,
		end:function(){
		  if(PlatUtil.isSubmitSuccess()){
			  $.fn.zTree.getZTreeObj("companyTree").reAsyncChildNodes(null, "refresh");
		  }
		}
	});
}
function editTreeInfoWin(){
	PlatUtil.hideZtreeRightMenu();
	var selectTreeNode = $.fn.zTree.getZTreeObj("companyTree").getSelectedNodes()[0];
	var COMPANY_ID = selectTreeNode.id;
	if(COMPANY_ID=="0"){
		parent.layer.alert("根节点不能进行编辑!",{icon: 2,resize:false});
	}else{
		showTreeInfoWin(COMPANY_ID);
	}
}
function delTreeNodeInfo(){
	PlatUtil.deleteZtreeNode({
		treeId:"companyTree",
		url:"metadata/DataResController.do?invokeRes&PLAT_RESCODE=system_company_del"
	});
}
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id!="0"){
			$("#departTree_paneltitle").text(treeNode.name+"[部门管理]");
            $("input[name='DEPART_COMPANYID_EQ']").val(treeNode.id);
		}else{
            $("#departTree_paneltitle").text("部门管理");
            $("input[name='DEPART_COMPANYID_EQ']").val("0");
        }
        //刷新右侧列表
	    PlatUtil.tableDoSearch("departTree","departTree_search");
	}
}  
  $(function(){
	  PlatUtil.initZtree({
		  treeId:"companyTree",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoCompleteType:"1",
		  callback: {
			  onRightClick: PlatUtil.onZtreeRightClick,
			  onClick:onTreePanelClick,
			  onDrop: PlatUtil.onZtreeNodeDrop,
              onAsyncSuccess: function(event, treeId, treeNode, msg){
                 var treeObj = $.fn.zTree.getZTreeObj(treeId);
                 var userLoginUser = PlatUtil.getBackPlatLoginUser();
                 var node = treeObj.getNodeByParam("id",userLoginUser.SYSUSER_COMPANYID, null);
                 treeObj.selectNode(node);
                 $("#departTree_paneltitle").text(node.name+"[部门管理]");
                 $("input[name='DEPART_COMPANYID_EQ']").val(node.id);
                 PlatUtil.tableDoSearch("departTree","departTree_search");
              }
		  },
		  async : {
			url:"common/baseController.do?tree",
			otherParam : {
				//树形表名称
				"tableName" : "PLAT_SYSTEM_COMPANY",
				//键和值的列名称
				"idAndNameCol" : "COMPANY_ID,COMPANY_NAME",
				//查询其它部门列名称
				"targetCols" : "COMPANY_CODE,COMPANY_PARENTID,COMPANY_LEVEL",
				//最先读取的根节点的值
				"loadRootId" : "${sessionScope.__BACK_PLAT_USER.SYSUSER_COMPANYID}",
				//需要回填的值
				"needCheckIds":"",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//根据节点名称
				"treeTitle":"单位树"
			}
		  }
	  });
	  
});
</script>

</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="departTree_paneltitle">部门管理</h5>
</div>
<div id="departTree_search">
         <input type="hidden" name="DEPART_COMPANYID_EQ" value="${sessionScope.__BACKPLATUSER.SYSUSER_COMPANYID}">
</div>

<div class="gridPanel">
	<table id="departTree"></table>
</div>
<div id="jqtreeContextmenu">
      <ul class="dropdown-menu" role="menu">
          <li platreskey="addDepart"><a fnname="addJqTreeNode"><i class="fa fa-plus"></i>&nbsp;新增</a></li>
          <li platreskey="editDepart"><a fnname="editJqTreeNode"><i class="fa fa-pencil"></i>&nbsp;编辑</a></li>
          <li platreskey="delDepart"><a fnname="delJqTreeNode"><i class="fa fa-remove"></i>&nbsp;删除</a></li>
          <li platreskey=""><a fnname="grantUsers"><i class="fa fa-users"></i>&nbsp;分配用户</a></li>
          <li platreskey=""><a fnname="setDirector"><i class="fa fa-user"></i>&nbsp;设置部门主任</a></li>
          <li platreskey=""><a fnname="setTaker"><i class="fa fa-user-o"></i>&nbsp;设置部门承办人</a></li>
      </ul>
 </div>
<script type="text/javascript">
function addJqTreeNode(rowData){
        var DEPART_COMPANYID = $("input[name='DEPART_COMPANYID_EQ']").val();
        if(DEPART_COMPANYID=="0"){
           parent.layer.alert("请选择左侧具体单位,然后进行创建部门!",{icon: 2,resize:false});
           return;
        }
    	var DEPART_ID=rowData.DEPART_ID;
    	var isEdit = rowData.isEdit;
    	var title = "新增部门";
    	var url = "system/DepartController.do?goForm&UI_DESIGNCODE=depform&DEPART_COMPANYID="+DEPART_COMPANYID;
    	if(isEdit){
    		title = "编辑部门";
    		url+="&DEPART_ID="+DEPART_ID;
    	}else{
    		url+="&DEPART_PARENTID="+DEPART_ID;
    	}
    	PlatUtil.openWindow({
    		title:title,
    		area: ["1000px","220px"],
    		content: url,
    		end:function(){
    		  if(PlatUtil.isSubmitSuccess()){
    			  $("#departTree").trigger("reloadGrid"); 
    		  }
    		}
    	});
 }
function editJqTreeNode(rowData){
    	var DEPART_ID = rowData.DEPART_ID;
    	if(DEPART_ID=="0"){
    		parent.layer.alert("根节点禁止编辑!",{icon: 2,resize:false});
    	}else{
    		rowData.isEdit = true;
    		addJqTreeNode(rowData);
    	}
 }
function delJqTreeNode(rowData){
    	PlatUtil.deleteJqTreeGridNode({
    		tableId:"departTree",
    		treeNodeId:rowData.DEPART_ID,
    		url:"metadata/DataResController.do?invokeRes&PLAT_RESCODE=system_dep02"
    	});
}
 

function grantUsers(rowData){
    var DEPART_ID = rowData.DEPART_ID;
    	if(DEPART_ID=="0"){
    		parent.layer.alert("根节点禁止分配!",{icon: 2,resize:false});
    	}else{
    		rowData.isEdit = true;
		var DEPART_NAME = rowData.DEPART_NAME;
        PlatUtil.setData(PlatUtil.WIN_SELECTOR_CONFIG,{
			//0标识不控制选择的条数
			maxselect:100,
			//最少需要选择的条数
			minselect:1
			//选中时的级联方式
		});
		PlatUtil.openWindow({
			title:"分配["+DEPART_NAME+"]的用户",
			area: ["90%","90%"],
			content: "system/DepartController.do?goUserGrant&DEPART_ID="+DEPART_ID,
			end:function(){
              var selectedRecords = PlatUtil.getData(PlatUtil.WIN_SELECTOR_RECORDS);
              if(selectedRecords&&selectedRecords.selectSuccess){
                    var checkUserIds = selectedRecords.checkIds;
					PlatUtil.removeData(PlatUtil.WIN_SELECTOR_RECORDS);
                    PlatUtil.ajaxProgress({
                        url:"system/DepartController.do?grantUsers",
                        params : {
                           DEPART_ID:DEPART_ID,
                           checkUserIds:checkUserIds
                        },
                        callback : function(resultJson) {
                            if (resultJson.success) {
                                parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
                            } else {
                                parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
                            }
                        }
                    });
                    
			  }
			}
		});
    }
}
function setDirector(rowData){
    var DEPART_ID = rowData.DEPART_ID;
  var DEPART_PARENTID = rowData.DEPART_PARENTID;
	if(DEPART_ID=="0"){
		parent.layer.alert("根节点禁止分配!",{icon: 2,resize:false});
	}else{
		rowData.isEdit = true;
		var DEPART_NAME = rowData.DEPART_NAME;
		PlatUtil.setData(PlatUtil.WIN_SELECTOR_CONFIG,{
			//最多可选择的条数
			maxselect:1,
			//最少需要选择的条数
			minselect:1
		});
		PlatUtil.openWindow({
			title:"分配["+DEPART_NAME+"]的室主任",
			area: ["90%","90%"],
			content: "system/DepartController.do?goDepartDirectorGrant&DEPART_ID="+DEPART_ID+"&DEPART_PARENTID="+DEPART_PARENTID,
			end:function(){
			  var selectedRecords = PlatUtil.getData(PlatUtil.WIN_SELECTOR_RECORDS);
			  if(selectedRecords&&selectedRecords.selectSuccess){
					var checkUserIds = selectedRecords.checkIds;
                	var checkDeptNames = selectedRecords.checkNames;
					PlatUtil.removeData(PlatUtil.WIN_SELECTOR_RECORDS);
					PlatUtil.ajaxProgress({
						url:"system/DepartController.do?grantDepartDirector",
						params : {
						   DEPART_ID:DEPART_ID,
						   checkUserIds:checkUserIds,
                           checkDeptNames:checkDeptNames
						},
						callback : function(resultJson) {
							if (resultJson.success) {
								parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
							} else {
								parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
							}
						}
					});
					
			  }
			}
		});
	}
}
function setTaker(rowData){
    var DEPART_ID = rowData.DEPART_ID;
  var DEPART_PARENTID = rowData.DEPART_PARENTID;
	if(DEPART_ID=="0"){
		parent.layer.alert("根节点禁止分配!",{icon: 2,resize:false});
	}else{
		rowData.isEdit = true;
		var DEPART_NAME = rowData.DEPART_NAME;
		PlatUtil.setData(PlatUtil.WIN_SELECTOR_CONFIG,{
			//最多可选择的条数
			maxselect:1,
			//最少需要选择的条数
			minselect:1
		});
		PlatUtil.openWindow({
			title:"分配["+DEPART_NAME+"]的承办人",
			area: ["90%","90%"],
			content: "system/DepartController.do?goDepartTakerGrant&DEPART_ID="+DEPART_ID+"&DEPART_PARENTID="+DEPART_PARENTID,
			end:function(){
			  var selectedRecords = PlatUtil.getData(PlatUtil.WIN_SELECTOR_RECORDS);
			  if(selectedRecords&&selectedRecords.selectSuccess){
					var checkUserIds = selectedRecords.checkIds;
                	var checkDeptNames = selectedRecords.checkNames;
					PlatUtil.removeData(PlatUtil.WIN_SELECTOR_RECORDS);
					PlatUtil.ajaxProgress({
						url:"system/DepartController.do?grantDepartTaker",
						params : {
						   DEPART_ID:DEPART_ID,
						   checkUserIds:checkUserIds,
                           checkDeptNames:checkDeptNames
						},
						callback : function(resultJson) {
							if (resultJson.success) {
								parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
							} else {
								parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
							}
						}
					});
					
			  }
			}
		});
	}
}

$(function(){
	PlatUtil.initJqTreeGrid({
		  tableId:"departTree",
		  searchPanelId:"departTree_search",
		  url: "appmodel/CommonUIController.do?jqtreetabledata&FORMCONTROL_ID=402881e65b505ed4015b5070fb3f0021",
		  ExpandColumn: "DEPART_CODE",
		  colModel: [
		         {name: "DEPART_ID",label:"部门ID",
		         width: 100,align:"left",
		         hidden:true,
		         key:true,
		         
		         sortable:false
		         },
		         {name: "DEPART_NAME",label:"部门名称",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "DEPART_CODE",label:"部门编码",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "DEPART_PARENTID",label:"部门类别ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "DEPART_LEVEL",label:"树形层级",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "DEPART_TREESN",label:"同级部门排序",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "DEPART_NAME",label:"隐藏的单位名称",
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
