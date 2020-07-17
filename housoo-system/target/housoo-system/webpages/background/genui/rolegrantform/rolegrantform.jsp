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
    <title>角色授权表单</title>
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
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   
<div class="plat-wizard">
	<ul class="steps">
		<li class="active">
		<span class="step">1</span>操作授权<span class="chevron"></span>
		</li>	
		<li class="">
		<span class="step">2</span>数据授权<span class="chevron"></span>
		</li>	
	</ul>
</div>
<div id="wizard-showdiv" style="height: calc(100% - 48px);">
		<div platundragable="true" compcode="platwizard" style="height:100%">
   		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout" id="restreelayout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <plattag:treepanel id="resTree" opertip="false" panel_title="资源树形面板">
</plattag:treepanel>
<script type="text/javascript">
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val("");
			$("#dictionary_paneltitle").text("字典信息列表");
		}else{
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val(treeNode.DICTYPE_CODE);
			$("#dictionary_paneltitle").text(treeNode.name+"-->字典信息列表");
		}
		//刷新右侧列表
		PlatUtil.tableDoSearch("dictionary_datatable","dictionary_search");
	}
}  
  $(function(){
	  var resTreecheck = {
		enable : true,
		chkStyle:"checkbox",
		chkboxType :{
			"Y" :"ps",
			"N" :"s"
		}
	  };
	  PlatUtil.initZtree({
		  treeId:"resTree",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoCompleteType:"1",
		  check: resTreecheck,
		  edit : {
				enable : false,
				showRemoveBtn : false,
				showRenameBtn : false
		  },
		  callback: {
			  onClick:function (e, treeId, treeNode, clickFlag) { 
                  var treeObj = $.fn.zTree.getZTreeObj(treeId);
                  treeObj.checkNode(treeNode, !treeNode.checked, true); 
			  },
         onAsyncSuccess : function(event, treeId, treeNode, msg) {
				  var treeObj = $.fn.zTree.getZTreeObj(treeId);
                  var sysUser = PlatUtil.getBackPlatLoginUser();
				  if (sysUser.SYSUSER_ACCOUNT!="admin") {
                var threeManagerCode = "${threeManagerCode}";
                var RESCODES = sysUser.RESCODES;
						var nodes = treeObj.transformToArray(treeObj.getNodes());
                if(threeManagerCode){
                  var threeManagerCodeArray = threeManagerCode.split(",");
                  if(PlatUtil.isContain(threeManagerCodeArray,sysUser.SYSUSER_ACCOUNT)){
                     for (var i = 0; i < nodes.length; i++) {
                       var child = nodes[i];
                       var id = child.id;
                       var RES_CODE = child.RES_CODE;
                       if(RES_CODE=="SYSTEM_MANAGER"||RES_CODE=="COMPANY_ORG"){
                         treeObj.hideNode(child);
                       }
                     }
                     return;
                  }
                }
                for (var i = 0; i < nodes.length; i++) {
							var child = nodes[i];
							var id = child.id;
							var RES_CODE = child.RES_CODE;
							if (id != "0") {
								if (RESCODES.indexOf(RES_CODE) == -1) {
									treeObj.hideNode(child);
								}
							}
				      }
                
						
					}

			  }
		  },
		  async : {
			url:"common/baseController.do?tree",
			otherParam : {
				//树形表名称
				"tableName" : "PLAT_SYSTEM_RES",
				//键和值的列名称
				"idAndNameCol" : "RES_ID,RES_NAME",
				//查询其它部门列名称
				"targetCols" : "RES_CODE,RES_PARENTID,RES_PATH",
				//最先读取的根节点的值
				"loadRootId" : "0",
				//需要回填的值
				"needCheckIds":"${selectedRecordIds}",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//根据节点名称
				"treeTitle":"系统资源树"
			}
		  }
	  });
	  
});
</script>

</div>
</div></div>
		<div platundragable="true" compcode="platwizard" style="display: none;">
   		<div class="tabs-container" platundragable="true" compcode="bootstraptab" id="datatab" style="width:100%;height:100%;">
	<ul class="nav nav-tabs">
		<li class="active" subtabid="tab1" onclick="PlatUtil.onBootstrapTabClick('datatab','tab1','');"><a data-toggle="tab" href="#tab1" aria-expanded="true">角色组授权</a></li>
		<li class="" subtabid="tab2" onclick="PlatUtil.onBootstrapTabClick('datatab','tab2','');"><a data-toggle="tab" href="#tab2" aria-expanded="false">流程类别授权</a></li>
	</ul>
	<div class="tab-content" platundragable="true" compcode="bootstraptab" style="height: calc(100% - 42px);">
		<div id="tab1" class="tab-pane active" style="height:320px;" platundragable="true" compcode="bootstraptab">
		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <plattag:treepanel id="groupTree" opertip="false" panel_title="角色组信息树">
</plattag:treepanel>
<script type="text/javascript">
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val("");
			$("#dictionary_paneltitle").text("字典信息列表");
		}else{
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val(treeNode.DICTYPE_CODE);
			$("#dictionary_paneltitle").text(treeNode.name+"-->字典信息列表");
		}
		//刷新右侧列表
		PlatUtil.tableDoSearch("dictionary_datatable","dictionary_search");
	}
}  
  $(function(){
	  var groupTreecheck = {
		enable : true,
		chkStyle:"checkbox",
		chkboxType :{
			"Y" :"",
			"N" :""
		}
	  };
	  PlatUtil.initZtree({
		  treeId:"groupTree",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoCompleteType:"1",
          autoServerUrl:"system/RoleGroupController.do?autoGroup",
		  check: groupTreecheck,
		  edit : {
				enable : false,
				showRemoveBtn : false,
				showRenameBtn : false
		  },
		  callback: {
             onClick:function (e, treeId, treeNode, clickFlag) { 
                  var treeObj = $.fn.zTree.getZTreeObj(treeId);
                  treeObj.checkNode(treeNode, !treeNode.checked, true); 
			  },
              onAsyncSuccess : function(event, treeId, treeNode, msg) {
				  var treeObj = $.fn.zTree.getZTreeObj(treeId);
                  var sysUser = PlatUtil.getBackPlatLoginUser();
				  if (!sysUser.IS_ADMIN) {
                        var GROUPIDS = sysUser.GROUPIDS;
						var nodes = treeObj
								.transformToArray(treeObj.getNodes());
						for (var i = 0; i < nodes.length; i++) {
							var child = nodes[i];
							var id = child.id;
							if (id != "0") {
								if (GROUPIDS.indexOf(id) == -1) {
									treeObj.hideNode(child);
								}
							}
						}
					}

			  }
		  },
		  async : {
			url:"system/RoleGroupController.do?groupTreeData",
			otherParam : {
				//最先读取的根节点的值
				"loadRootId" : "0",
				//需要回填的值
				"needCheckIds":"${selectedRecordIds}",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//根据节点名称
				"treeTitle":"角色组信息树"
			}
		  }
	  });
	  
});
</script>

</div>
</div></div>
		<div id="tab2" class="tab-pane " style="height:320px;" platundragable="true" compcode="bootstraptab">
		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <plattag:treepanel id="flowtypedeftree" opertip="false" panel_title="流程类别定义树">
</plattag:treepanel>
<script type="text/javascript">
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val("");
			$("#dictionary_paneltitle").text("字典信息列表");
		}else{
			$("input[name='Q_T.DIC_DICTYPE_CODE_EQ']").val(treeNode.DICTYPE_CODE);
			$("#dictionary_paneltitle").text(treeNode.name+"-->字典信息列表");
		}
		//刷新右侧列表
		PlatUtil.tableDoSearch("dictionary_datatable","dictionary_search");
	}
}  
  $(function(){
	  var flowtypedeftreecheck = {
		enable : true,
		chkStyle:"checkbox",
		chkboxType :{
			"Y" :"ps",
			"N" :"s"
		}
	  };
	  PlatUtil.initZtree({
		  treeId:"flowtypedeftree",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
          autoServerUrl:"workflow/FlowTypeController.do?autoTypeAndDef",
		  autoCompleteType:"1",
		  check: flowtypedeftreecheck,
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
			url:"workflow/FlowTypeController.do?typeAndDefs",
			otherParam : {
				//最先读取的根节点的值
				"loadRootId" : "0",
				//需要回填的值
				"needCheckIds":"${selectedRecordIds}",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//根据节点名称
				"treeTitle":"流程类别定义树"
			}
		  }
	  });
	  
});
</script>

                                                                                             </div>
</div></div>
	</div>
</div>

<script type="text/javascript">


</script></div>
</div>
<script type="text/javascript">


</script>
  <input type="hidden" name="ROLE_ID" value="${ROLE_ID}" id="ROLE_ID">
</div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="wizardPre();" platreskey="" id="WIZARD_PREBTN" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-arrow-left"></i>&nbsp;上一步
		</button>
		<button type="button" onclick="wizardNext();" platreskey="" id="WIZARD_NEXTBTN" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-arrow-right"></i>&nbsp;下一步
		</button>
		<button type="button" onclick="submitRoleGrantForm();" platreskey="" id="WIZARD_OVERBTN" disabled="disabled" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-check"></i>&nbsp;完成
		</button>
     </div>
</div>

<script type="text/javascript">
function wizardPre(){
  PlatUtil.wizardPre();
}
function wizardNext(){
  PlatUtil.wizardNext();
}
function submitRoleGrantForm(){
  var resCheckRecord =  PlatUtil.getTreeCheckedValue("resTree");
  var ROLE_ID = $("#ROLE_ID").val();
  var resIds = resCheckRecord.checkIds;
  var groupCheckRecord = PlatUtil.getTreeCheckedValue("groupTree");
  var groupIds = groupCheckRecord.checkIds;
  var typedefCheckRecord = PlatUtil.getTreeCheckedValue("flowtypedeftree");
  var typedefIds = typedefCheckRecord.checkIds;
  PlatUtil.ajaxProgress({
    url:"metadata/DataResController.do?invokeRes&PLAT_RESCODE=system_role_grantright",
    params : {
      ROLE_ID:ROLE_ID,
      tableName:"${tableName}",
      resIds:resIds,
      groupIds:groupIds,
      typedefIds:typedefIds
    },
    callback : function(resultJson) {
      if (resultJson.success) {
        parent.layer.msg("授权成功,重新登录系统才可生效!", {icon: 1});
        PlatUtil.setData("submitSuccess",true);
        PlatUtil.closeWindow();
      } else {
        parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
      }
    }
  });
}

</script>

</div>
</div>
  </body>
</html>

<script type="text/javascript">
$(function(){
   if("${queryRight}"){
     $("#WIZARD_OVERBTN").attr("style","display:none;");
   }
   
});
</script>
