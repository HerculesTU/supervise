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
    <title>系统资源列表</title>
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
   <div id="resTree_search">
</div>

<div class="gridPanel">
	<table id="resTree"></table>
</div>
<div id="jqtreeContextmenu">
      <ul class="dropdown-menu" role="menu">
          <li platreskey="addRes"><a fnname="addJqTreeNode"><i class="fa fa-plus"></i>&nbsp;新增</a></li>
          <li platreskey="editRes"><a fnname="editJqTreeNode"><i class="fa fa-pencil"></i>&nbsp;编辑</a></li>
          <li platreskey="delRes"><a fnname="delJqTreeNode"><i class="fa fa-remove"></i>&nbsp;删除</a></li>
      </ul>
 </div>
<script type="text/javascript">
function addJqTreeNode(rowData){
    	var RES_ID=rowData.RES_ID;
    	var isEdit = rowData.isEdit;
    	var title = "新增系统资源";
    	var url = "system/ResController.do?goForm&UI_DESIGNCODE=systemresform";
    	if(isEdit){
    		title = "编辑系统资源";
    		url+="&RES_ID="+RES_ID;
    	}else{
    		url+="&RES_PARENTID="+RES_ID;
    	}
    	PlatUtil.openWindow({
    		title:title,
    		area: ["1200px","500px"],
    		content: url,
    		end:function(){
    		  if(PlatUtil.isSubmitSuccess()){
    			  $("#resTree").trigger("reloadGrid"); 
    		  }
    		}
    	});
 }
function editJqTreeNode(rowData){
    	var RES_ID = rowData.RES_ID;
    	if(RES_ID=="0"){
    		parent.layer.alert("根节点禁止编辑!",{icon: 2,resize:false});
    	}else{
    		rowData.isEdit = true;
    		addJqTreeNode(rowData);
    	}
 }
function delJqTreeNode(rowData){
    	PlatUtil.deleteJqTreeGridNode({
    		tableId:"resTree",
    		treeNodeId:rowData.RES_ID,
    		url:"system/ResController.do?multiDel"
    	});
}

$(function(){
	PlatUtil.initJqTreeGrid({
		  tableId:"resTree",
		  searchPanelId:"resTree_search",
		  url: "appmodel/CommonUIController.do?jqtreetabledata&FORMCONTROL_ID=402881e65b4ceaae015b4ceaae740000",
		  ExpandColumn: "RES_CODE",
		  colModel: [
		         {name: "RES_ID",label:"资源ID",
		         width: 100,align:"left",
		         hidden:true,
		         key:true,
		         
		         sortable:false
		         },
		         {name: "RES_NAME",label:"资源名称",
		         width: 300,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "RES_CODE",label:"资源编码",
		         width: 150,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "RES_PARENTID",label:"父资源ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "RES_TREESN",label:"同级资源排序",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "RES_TYPE",label:"资源类型",
		         width: 150,align:"left",
		         
		         
		         formatter:function(cellvalue, options, rowObject){
	if(rowObject.RES_TYPE=="1"){
      return "菜单";
    }else if(rowObject.RES_TYPE=="2"){
      return "操作权限";
    }else if(rowObject.RES_TYPE=="3"){
      return "应用系统";     
    }else{
      return "";
    }
},
		         sortable:false
		         },
		         {name: "RES_MENUURL",label:"资源URL",
		         width: 400,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "RES_MENUICON",label:"资源图标",
		         width: 150,align:"left",
		         
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
  if(cellvalue){
       return "<i class=\""+cellvalue+"\"></i>"+cellvalue;
   }else{
       return "";
   }
},
		         sortable:false
		         },
		         {name: "RES_STATUS",label:"资源状态",
		         width: 100,align:"left",
		         
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
  	if(cellvalue == "1"){
    	return "<span class=\"label label-primary\">启用</span>";		
    }else if(cellvalue == "2"){
    	return "<span class=\"label label-danger\">禁用</span>";
    }else{
        return "";
    }
     
},
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
