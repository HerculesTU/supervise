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
    
    <title>用户组列表-在线设计开发</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,plat-ui,tipsy,autocomplete,layer,nicevalid,codemirror,webuploader,fancybox,flowdesign,touchspin,ratingstar,ueditor">
	</plattag:resources>
	<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin,layer,nicevalid,codemirror,slimscroll,webuploader,fancybox,flowdesign,touchspin,echart,ratingstar,superslide,ueditor">
	</plattag:resources>
	<style type="text/css">
	  /* .plat-designborder{position:relative;border:2px dashed #d9534f !important; background:#F6F6F6 !important;}
	  .plat-designtoolbar{position: absolute;z-index: 100000;right: 0;top: 0;}
	  .platdesigncomp{padding: 22px;} */
	  .plat-designhighlightborder{position:relative;border:2px dashed #C81522 !important;}
	  .plat-designhighlighttoolbar{position: absolute;z-index: 100000;right: 0;top: 0;}
	  .plat-designhighlightclear{ clear:both} 
	</style>
  </head>
  
  <body platcomid="0" class="platdesigncomp" platcompname="body容器" uibtnsrights="add" compcontrolid="0">
      <script type="text/javascript">
      $(function(){
    		PlatUtil.initUIComp();
      });
      </script>
      <input type="hidden" id="__BACK_PLAT_USER_JSON" value="${sessionScope.__BACK_PLAT_USER_JSON}">
      <input type="hidden" id="ONLINE_DESIGNID" value="402881e46349f61c01634a1514c00010">
      <div class="plat-directlayout platdesigncomp" style="height:100%" platcompname="东西南北中布局" platcomid="402881e46349f61c01634a1515670011" uibtnsrights="edit,del" platundragable="true" compcontrolid="402881e46349f61c01634a1515670011" compcode="direct_layout" layoutsize="&quot;west__size&quot;:280">
   <div class="ui-layout-west platdesigncomp" platcompname="西部布局" platcomid="402881e46349f61c01634a1515670011_0" uibtnsrights="add" platundragable="true" compcontrolid="402881e46349f61c01634a1515670011" compcode="direct_layout">
   <div class="platdesigncomp" platcompname="树形面板" compcode="treepanel" platcomid="402881e46349f61c01634a1516140012" uibtnsrights="edit,del" compcontrolid="402881e46349f61c01634a1516140012">
<plattag:treepanel panel_title="单位管理" id="companyTree">
</plattag:treepanel>
<script type="text/javascript">
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
 
	if(event.target.tagName=="SPAN"){
		if(treeNode.id!="0"){
			$("#usergrouplist_datatablepaneltitle").text(treeNode.name+"[角色组管理]");
            $("input[name='Q_T.USERGROUP_COMPANYID_EQ']").val(treeNode.id);
		}else{
            $("#usergrouplist_datatablepaneltitle").text("角色组管理");
            $("input[name='Q_T.USERGROUP_COMPANYID_EQ']").val("");
        }
        //刷新右侧列表
	    PlatUtil.tableDoSearch("usergrouplist_datatable","usergrouplist_search");
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
</div>
   <div class="ui-layout-center platdesigncomp" platcompname="中央布局" platcomid="402881e46349f61c01634a1515670011_1" uibtnsrights="add" platundragable="true" compcontrolid="402881e46349f61c01634a1515670011" compcode="direct_layout">
   <div class="platdesigncomp" platcompname="JqGrid表格(usergrouplist)" compcode="jqgrid" platcomid="402881e46349f61c01634a1aaa120028" uibtnsrights="edit,del" compcontrolid="402881e46349f61c01634a1aaa120028">
<div class="panel-Title">
	<h5 id="usergrouplist_datatablepaneltitle">用户组</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="usergrouplist_search" formcontrolid="402881e46349f61c01634a1aaa120028">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_T.USERGROUP_COMPANYID_EQ" value="">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.USERGROUP_NAME_LIKE" auth_type="write" label_value="用户组名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('usergrouplist_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('usergrouplist_datatable','usergrouplist_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="addJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		  </button>
		
	      <button type="button" onclick="editJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		  </button>
		
	      <button type="button" onclick="delJqGridRecordInfo();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		  </button>
		
	      <button type="button" onclick="grantUsers();" platreskey="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-users"></i>&nbsp;分配用户
		  </button>
		
	</div>
</div>

<div class="gridPanel">
	<table id="usergrouplist_datatable"></table>
</div>
<div id="usergrouplist_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(USERGROUP_ID){
   var can = true; 
	var url = "system/SysUserGroupController.do?goForm&UI_DESIGNCODE=usergroupform";
	var title = "新增用户组信息";
	if(USERGROUP_ID){
		url+=("&USERGROUP_ID="+USERGROUP_ID);
		title = "用户组信息";
	}else{
        var companyId = $("input[name='Q_T.USERGROUP_COMPANYID_EQ']").val();
        url+=("&companyId="+companyId);
        if(companyId==""){
           can = false;
         }
    }
  if(can){
    PlatUtil.openWindow({
	  title:title,
	  area: ["500px","300px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#usergrouplist_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
  }else{
    parent.layer.alert("请选择单位!",{icon: 2,resize:false});
  }
	
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("usergrouplist_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.USERGROUP_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"usergrouplist_datatable",
		selectColName:"USERGROUP_ID",
		url:"system/SysUserGroupController.do?multiDel",
		callback:function(resultJson){
			$("#usergrouplist_datatable").trigger("reloadGrid"); 
		}
	});
}
 

function grantUsers(){
    var rowData = PlatUtil.getTableOperSingleRecord("usergrouplist_datatable");
	if(rowData){
		var USERGROUP_ID = rowData.USERGROUP_ID;
		var USERGROUP_NAME = rowData.USERGROUP_NAME;
        PlatUtil.setData(PlatUtil.WIN_SELECTOR_CONFIG,{
			//0标识不控制选择的条数
			maxselect:100,
			//最少需要选择的条数
			minselect:1
			//选中时的级联方式
		});
		PlatUtil.openWindow({
			title:"分配["+USERGROUP_NAME+"]的用户",
			area: ["90%","90%"],
			content: "system/SysUserGroupController.do?goUserGrant&USERGROUP_ID="+USERGROUP_ID,
			end:function(){
              var selectedRecords = PlatUtil.getData(PlatUtil.WIN_SELECTOR_RECORDS);
              if(selectedRecords&&selectedRecords.selectSuccess){
                    var checkUserIds = selectedRecords.checkIds;
					PlatUtil.removeData(PlatUtil.WIN_SELECTOR_RECORDS);
                    PlatUtil.ajaxProgress({
                        url:"system/SysUserGroupController.do?grantUsers",
                        params : {
                           USERGROUP_ID:USERGROUP_ID,
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

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"usergrouplist_datatable",
		  searchPanelId:"usergrouplist_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402881e46349f61c01634a1aaa120028",
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "USERGROUP_ID",label:"用户组ID",
		         width: 100,align:"left",
		         hidden:true,
		         

		         
		         sortable:false
		         },
		         {name: "USERGROUP_NAME",label:"用户组名称",
		         width: 300,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "USERGROUP_CREATETIME",label:"创建时间",
		         width: 100,align:"left",
		         
		         

		         
		         sortable:false
		         },
		         {name: "USERGROUP_COMPANYID",label:"所属单位ID",
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
</div>
  </body>
</html>

<script type="text/javascript">



function highlightBorder(id,name){
	$("[platcomid]").each(function(){
		if($(this).is(".plat-designhighlightborder")){
			$(this).removeClass("plat-designhighlightborder");
			$(this).find(".plat-designhighlighttoolbar").remove();
			$(this).find(".plat-designhighlightclear").remove();
		}
	});
	$("[platcomid='"+id+"']").addClass("plat-designhighlightborder");
	var toolbarHtml = "<div class=\"plat-designhighlighttoolbar\"><b>"+name+"</b></div><div class=\"plat-designhighlightclear\"></div>";
	$("[platcomid='"+id+"']").append(toolbarHtml);
}
</script>
