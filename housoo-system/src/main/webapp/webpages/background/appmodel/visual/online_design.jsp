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
    
    <title>${DESIGN_NAME}-在线设计开发</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,plat-ui,tipsy,autocomplete,layer">
	</plattag:resources>
  </head>
  
  <body>
      <div class="plat-directlayout " style="height: 100%;" layoutsize="&quot;west__size&quot;:260" >
	    <div class="ui-layout-west" >
	        <input type="hidden" id="ONLINE_DESIGNID" name="DESIGN_ID" value="${DESIGN_ID}">
	        <input type="hidden" id="designControlJson" value="${designControlJson}">
	        <plattag:treepanel panel_title="组件树" id="formControlTree">
	        </plattag:treepanel>
		</div>
  		<div class="ui-layout-center" >
  		    <iframe height="100%" id="designFormIframe" 
  		    name="designFormIframe" frameborder="0" width="100%" src="appmodel/DesignController.do?goGenDesignView&DESIGN_ID=${DESIGN_ID}&fileId=${fileId}">
  		    </iframe>
  		</div>
 	   </div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin,layer">
</plattag:resources>
<script type="text/javascript">

/**
 * 添加树形hover工具按钮
 */
function addFormCtrlTreeHoverDom(treeId, treeNode) {
	if (treeNode.parentNode && treeNode.parentNode.id != 1){
		return;
	}
	var aObj = $("#" + treeNode.tId + "_a");
	if ($("#addItemHref_" + treeNode.id).length > 0)
		return;
	if ($("#editItemHref_" + treeNode.id).length > 0)
		return;
	if ($("#delItemHref_" + treeNode.id).length > 0)
		return;
	var uibtnsrights = treeNode.uibtnsrights;
	var operateStr = "";
	if(uibtnsrights.indexOf("add")!=-1){
		operateStr += "<a id='addItemHref_" +treeNode.id+ "' style='margin:0 0 0 2px;'>创建</a>";
	}
	if(uibtnsrights.indexOf("edit")!=-1){
		operateStr += "<a id='editItemHref_" +treeNode.id+ "' style='margin:0 0 0 2px;'>编辑</a>";
	}
	if(uibtnsrights.indexOf("del")!=-1){
		operateStr += "<a id='delItemHref_" +treeNode.id+ "' style='margin:0 0 0 2px;'>删除</a>";
	}
	aObj.append(operateStr);
	$("#addItemHref_" + treeNode.id).bind("click", function() {
		PlatUtil.addDesignCmp(treeNode.compcontrolid,treeNode.id);
	});
	$("#editItemHref_" + treeNode.id).bind("click", function() {
		PlatUtil.editDesignCmp(treeNode.compcontrolid,treeNode.name);
	});
	$("#delItemHref_" + treeNode.id).bind("click", function() {
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		var nodes = treeObj.getNodesByFilter(function(){
			return true;
		},false,treeNode);
		var compcontrolids = "";
		$.each(nodes,function(index,objvalue){
			if(index>0){
				compcontrolids+=",";
			}
			compcontrolids+=objvalue.compcontrolid;
		});
		PlatUtil.delDesignCmp(treeNode.compcontrolid,compcontrolids);
	});
}

/**
 * 移除树形hover工具按钮
 */
function removeFormCtrolTreeHoverDom(treeId, treeNode) {
	$("#addItemHref_" + treeNode.id).unbind().remove();
	$("#editItemHref_" + treeNode.id).unbind().remove();
	$("#delItemHref_" + treeNode.id).unbind().remove();
}
/**
 * 容器组件
 */
var ContainerAssembly = ['formgroup','formcontainer','direct_layout','jqgrid'];
/**
 * 点击事件
 */
function formControlTreeClick(event, treeId, treeNode){
	if($.inArray(treeNode.compcode, ContainerAssembly)>-1){
		window.frames['designFormIframe'].highlightBorder(treeNode.id,treeNode.name);
	}
}

$(function(){
	PlatUtil.initUIComp();
	var designControlJson = $("#designControlJson").val();
	var zNodes = $.parseJSON(designControlJson);
    PlatUtil.initZtree({
	  treeId:"formControlTree",
	  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
	  //如果需要自定义服务端url,请传入autoServerUrl
	  autoCompleteType:"1",
	  callback: {
		  onClick: formControlTreeClick,
		  beforeDrag: function(treeId, treeNodes){
			  if(treeNodes[0].platundragable){
				  return false;
			  }else{
				  return true;
			  }
		  },
		  beforeDrop:function(treeId, treeNodes, targetNode, moveType){
			  if(moveType=="inner"){
				  var uibtnsrights = targetNode.uibtnsrights;
				  if(uibtnsrights.indexOf("add")!=-1){
					  return true;
				  }else{
					  return false;
				  }
			  }
		  },
		  onDrop: function(event, treeId, treeNodes, targetNode, moveType, isCopy){
			  if(moveType!=null){
				  if(targetNode){
					  PlatUtil.ajaxProgress({
						   url:"appmodel/FormControlController.do?updateTreeSn",
						   params:{
							   "dragTreeNodeId":treeNodes[0].compcontrolid,
							   "dragTreeNodeNewLevel":treeNodes[0].level,
							   "targetNodeId":targetNode.compcontrolid,
							   "targetPlatComId":targetNode.id,
							   "moveType":moveType,
							   "targetNodeLevel":targetNode.level,
							   "designId":$("#ONLINE_DESIGNID").val()
						   },
						   callback:function(resultJson){
								if(resultJson.success){
					    	   	    parent.layer.msg("成功完成拖拽排序!", {icon: 1});
					    	   	    PlatUtil.reloadOnlineDesign();
								}else{
									parent.layer.alert(resultJson.msg,{icon: 2,resize:false});
									$.fn.zTree.getZTreeObj(treeId).reAsyncChildNodes(null, "refresh");
								}
						   	    
						   }
					   });
				  }
			  }
			  return false;
		  }
	  },
	  view : {
		  addHoverDom : addFormCtrlTreeHoverDom,
		  selectedMulti : false,
		  removeHoverDom : removeFormCtrolTreeHoverDom
	  },
	  dataNodes:zNodes
  	});
});
</script>
