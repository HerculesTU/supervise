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
    <title>角色选择器</title>
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
   <plattag:treepanel id="grouproletree" opertip="false" panel_title="角色信息树">
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
		  treeId:"grouproletree",
          check: check,
          edit : {
				enable : false,
				showRemoveBtn : false,
				showRenameBtn : false
		  },
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
          autoServerUrl:"system/RoleGroupController.do?autoGroupAndRole",
		  autoCompleteType:"1",
		  callback: {
			  onCheck:function(event, treeId, treeNode){
				  PlatUtil.selectedGridItem(treeId, treeNode,true,"selectedRoleGrid");
			  },
			  onClick:function (e, treeId, treeNode, clickFlag) { 
				  PlatUtil.selectedGridItem(treeId, treeNode,false,"selectedRoleGrid");
			  }
		  },
		  async : {
			url:"system/RoleGroupController.do?groupAndRoles",
			otherParam : {
				//需要回填的值
				"needCheckIds":selectedRecordIds,
				//是否显示树形标题
				"isShowTreeTitle" : "true",
				//根据节点名称
				"treeTitle":"角色信息树"
			}
		  }
	  });
	  
});
</script>

</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <plattag:griditem iconfont="fa fa-user-secret" dyna_interface="roleService.findGridItemList" dragable="false" selectedrecordids="${selectedRecordIds}" itemtplpath="common/compdesign/griditem/griditem_tpl" id="selectedRoleGrid" paneltitle="已选角色列表(<font color='red'>双击可取消选择</font>)" itemconf="[ROLE_ID,1,角色ID][ROLE_NAME,-1,角色名称][ROLE_CODE,-1,角色编码]"></plattag:griditem>
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
	var checkRecords = PlatUtil.getSelectedGridItem("selectedRoleGrid");
    var checkIds = "";
	var checkNames = "";
	if (checkRecords.length > 0) {
		for (var i = 0; i < checkRecords.length; i++) {
           if(i>0){
             checkIds+=",";
             checkNames+=",";
           }
		   checkIds += checkRecords[i].ROLE_ID;
           checkNames += checkRecords[i].ROLE_NAME;
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
