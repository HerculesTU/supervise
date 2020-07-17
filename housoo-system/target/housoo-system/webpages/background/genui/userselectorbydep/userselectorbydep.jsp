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
    <title>部门分配用户弹出选择器</title>
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
   <div class="ui-layout-west" platundragable="true" compcode="direct_layout">
   <div class="tabs-container" platundragable="true" compcode="bootstraptab" id="userSelectTab" style="width:100%;height:100%;">
	<ul class="nav nav-tabs">
		<li class="active" subtabid="tab1" onclick="PlatUtil.onBootstrapTabClick('userSelectTab','tab1');"><a data-toggle="tab" href="#tab1" aria-expanded="true">按单位部门</a></li>
		<li class="" subtabid="tab2" onclick="PlatUtil.onBootstrapTabClick('userSelectTab','tab2');"><a data-toggle="tab" href="#tab2" aria-expanded="false">按角色</a></li>
	</ul>
	<div class="tab-content" platundragable="true" compcode="bootstraptab" style="height: calc(100% - 42px);">
		<div id="tab1" class="tab-pane active" style="height:100%;" platundragable="true" compcode="bootstraptab">
		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <plattag:treepanel id="departTree">
</plattag:treepanel>
<script type="text/javascript">
  $(function(){
  
      var check = {
			enable : true,
			chkStyle:"checkbox",
			chkboxType :{
				"Y" :"",
				"N" :""
			}
	  };
      var selectedRecordIds = null;
	  //获取初始化配置
	  var selectorConfig = PlatUtil.getData(PlatUtil.WIN_SELECTOR_CONFIG); 
	  if(selectorConfig&&selectorConfig.needCheckIds){
		  selectedRecordIds = selectorConfig.needCheckIds;
	  }else{
		 var selectedRecordIds = "${selectedRecordIds}";
	  } 
	  PlatUtil.initZtree({
		  treeId:"departTree",
          check: check,
          edit : {
				enable : false,
				showRemoveBtn : false,
				showRenameBtn : false
		  },
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
          autoServerUrl:"system/DepartController.do?autoDepartAndUser",
		  autoCompleteType:"1",
		  callback: {
			  onCheck:function(event, treeId, treeNode){
				  PlatUtil.selectedGridItem(treeId, treeNode,true,"selectedUserGrid");
			  },
			  onClick:function (e, treeId, treeNode, clickFlag) { 
				  PlatUtil.selectedGridItem(treeId, treeNode,false,"selectedUserGrid");
			  }
		  },
		  async : {
			url:"system/DepartController.do?departAndUsersAndAll",
			otherParam : {
				//树形表名称
				"tableName" : "PLAT_SYSTEM_DEPART",
				//键和值的列名称
				"idAndNameCol" : "DEPART_ID,DEPART_NAME",
				//查询其它部门列名称
				"targetCols" : "DEPART_PARENTID,DEPART_PATH,DEPART_TREESN",
				//最先读取的根节点的值
				"loadRootId" : "0",
				//需要回填的值
				"needCheckIds":selectedRecordIds,
				"Q_DEPART_COMPANYID_EQ":"${DEPART_COMPANYID}",
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//树形标题
				"treeTitle":"部门树"
			}
		  }
	  });
	  
});
</script>

</div>
</div></div>
		<div id="tab2" class="tab-pane " style="height:100%;" platundragable="true" compcode="bootstraptab">
		<div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-north" platundragable="true" compcode="direct_layout">
      <plattag:select name="GROUP_ID" allowblank="true" auth_type="write" value="" istree="false" onlyselectleaf="false" placeholder="请选择角色组" comp_col_num="0" style="width:100%;" dyna_interface="roleGroupService.findForSelect" dyna_param="没有参数">
   </plattag:select>
</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <plattag:treepanel id="roleusertree">
</plattag:treepanel>
<script type="text/javascript">
  $(function(){
	  $("select[name='GROUP_ID']").change(function() {
		var GROUP_ID = $(this).val();
		if(GROUP_ID){
	        var zTree = $.fn.zTree.getZTreeObj("roleusertree");
	        zTree.setting.async.otherParam["ROLE_GROUPID"] = GROUP_ID;
	        PlatUtil.refreshZtreePanel(zTree);
		}else{
			var zTree = $.fn.zTree.getZTreeObj("roleusertree");
	        zTree.setting.async.otherParam["ROLE_GROUPID"] = "";
	        PlatUtil.refreshZtreePanel(zTree);
		}
	});
	var check2 = {
			enable : true,
			chkStyle:"checkbox",
			chkboxType :{
				"Y" :"",
				"N" :""
			}
	};
	PlatUtil.initZtree({
		  treeId:"roleusertree",
          check: check2,
          edit : {
				enable : false,
				showRemoveBtn : false,
				showRenameBtn : false
		  },
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoServerUrl:"system/RoleController.do?autoRoleAndUser",
		  autoCompleteType:"1",
		  callback: {
			  onCheck:function(event, treeId, treeNode){
				  PlatUtil.selectedGridItem(treeId, treeNode,true,"selectedUserGrid");
			  },
			  onClick:function (e, treeId, treeNode, clickFlag) { 
				  PlatUtil.selectedGridItem(treeId, treeNode,false,"selectedUserGrid");
			  }
		  },
		  async : {
			url:"system/RoleController.do?roleAndUsers",
			otherParam : {
				"needCheckIds":"${selectedRecordIds}",
				"ROLE_GROUPID":""
			}
		  }
	  });
	  
});
</script>

</div>
</div></div>
	</div>
</div></div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <plattag:griditem iconfont="fa fa-user-circle-o" dyna_interface="sysUserService.findGridItemList" dragable="false" selectedrecordids="${selectedRecordIds}" itemtplpath="common/compdesign/griditem/griditem_tpl" id="selectedUserGrid" paneltitle="已选用户列表(<font color='red'>双击可取消选择</font>)" itemconf="[SYSUSER_ID,1,用户ID][COMPANY_NAME,-1,单位][DEPART_NAME,-1,部门][SYSUSER_ACCOUNT,-1,账号][SYSUSER_NAME,-1,姓名][SYSUSER_MOBILE,-1,手机号]"></plattag:griditem>
</div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="choiceWinItemForm();" id="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;提交
		</button>
		<button type="button" onclick="closeWindow();" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function choiceWinItemForm(){
	//获取初始化配置
	var selectorConfig = PlatUtil.getData(PlatUtil.WIN_SELECTOR_CONFIG);
	var maxselect = selectorConfig.maxselect;
	var minselect = selectorConfig.minselect;
	var checkRecords = PlatUtil.getSelectedGridItem("selectedUserGrid");
    var checkIds = "";
	var checkNames = "";
	if (checkRecords.length > 0) {
		for (var i = 0; i < checkRecords.length; i++) {
           if(i>0){
             checkIds+=",";
             checkNames+=",";
           }
		   checkIds += checkRecords[i].SYSUSER_ID;
           checkNames += checkRecords[i].SYSUSER_NAME;
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
function closeWindow(){
  PlatUtil.closeWindow();
}

</script>

</div>
</div>
  </body>
</html>

<script type="text/javascript">

</script>
