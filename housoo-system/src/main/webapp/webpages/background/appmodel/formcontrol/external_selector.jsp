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
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,plat-ui,tipsy,autocomplete">
	</plattag:resources>
  </head>
  
  <body>
    <div class="plat-directlayout" style="height: 100%;width: 100%;">
      <div class="ui-layout-center" style="overflow-y:auto; ">
  		   <plattag:treepanel id="externalResTree"  >
	        </plattag:treepanel>
      </div>
      <div class="ui-layout-south">
		   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;">
                   <div class="col-sm-12 text-right">
                       <button class="btn btn-outline btn-primary btn-sm" onclick="choiceExternalForm();" type="button" ><i class="fa fa-check"></i>提交</button>
                       <button class="btn btn-outline btn-danger btn-sm" onclick="PlatUtil.closeWindow();" type="button"><i class="fa fa-times"></i>关闭</button>
                   </div>
           </div>
	  </div>
	 </div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin">
</plattag:resources>
<script type="text/javascript">

function choiceExternalForm(){
	var treeObj = $.fn.zTree.getZTreeObj("externalResTree");
	//获取初始化配置
	var selectorConfig = PlatUtil.getData(PlatUtil.WIN_SELECTOR_CONFIG);
	var maxselect = selectorConfig.maxselect;
	var minselect = selectorConfig.minselect;
	var nodes = treeObj.getCheckedNodes(true);
	var checkRecords = [];
	var checkIds = "";
	var checkNames = "";
	if (nodes != null && nodes.length > 0) {
		for (var i = 0; i < nodes.length; i++) {
			if (nodes[i].id != "0") {
				checkIds += nodes[i].id + ",";
				checkNames += nodes[i].name + ",";
				checkRecords.push(nodes[i].id);
			}
		}
		if (checkRecords.length >= 1) {
			checkIds = checkIds.substring(0, checkIds.length - 1);
			checkNames = checkNames.substring(0, checkNames.length - 1);
		}
	} 
	if(maxselect!=0){
		if(checkRecords.length>maxselect){
			parent.layer.msg("最多只能选择"+maxselect+"条记录!", {icon: 2});
			return;
		}
	}
	if(checkRecords.length<minselect){
		parent.layer.msg("至少需要选择"+minselect+"条记录!", {icon: 2});
		return;
	}
	PlatUtil.removeData(PlatUtil.WIN_SELECTOR_CONFIG);
	PlatUtil.setData(PlatUtil.WIN_SELECTOR_RECORDS,{
		selectSuccess:true,
		checkIds:checkIds,
		checkNames:checkNames
	});
	PlatUtil.closeWindow();
}

$(function(){
	PlatUtil.initUIComp();
	//获取初始化配置
	var selectorConfig = PlatUtil.getData(PlatUtil.WIN_SELECTOR_CONFIG);
	var checkTypeObj = {
		"Y" : selectorConfig.checkCascadeY,
		"N" : selectorConfig.checkCascadeN
	};
	var check = {
		enable : true,
		chkboxType : checkTypeObj
	};
	if (selectorConfig.checktype=="radio") {
		check.chkStyle = "radio";
		check.radioType = "all";
	}
	var needCheckIds = selectorConfig.needCheckIds;
	PlatUtil.initZtree({
	  treeId:"externalResTree",
	  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
	  //如果需要自定义服务端url,请传入autoServerUrl
	  autoCompleteType:"1",
	  check: check,
	  edit : {
		enable : false,
		showRemoveBtn : false,
		showRenameBtn : false
	  },
	  callback: {
		onClick:function (e, treeId, treeNode, clickFlag) { 
			var treeObj = $.fn.zTree.getZTreeObj(treeId);
			treeObj.checkNode(treeNode, !treeNode.checked, true); 
		}
	  },
	  async : {
		url:"appmodel/FormControlController.do?externalResTree",
		otherParam : {
			//需要回填的值
			"needCheckIds":needCheckIds,
			//是否显示树形标题
			"isShowTreeTitle" : "true",
			//树形标题
			"treeTitle":"外部资源树"
		}
	  }
  });
});
</script>
