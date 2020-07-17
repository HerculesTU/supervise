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
    <title>角色管理列表</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,slimscroll"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,slimscroll"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout" layoutsize="&quot;west__size&quot;:280">
   <div class="ui-layout-west" platundragable="true" compcode="direct_layout">
     <plattag:operlistgroup onclickfn="operlistclick" grouptitle="角色组列表" dyna_interface="roleGroupService.findGroupList" dyna_param="五" addclickfn="operladdclick" editclickfn="operleditclick" delclickfn="operldelclick" id="rolegrouplist"></plattag:operlistgroup>
<script type="text/javascript">
  function operlistclick(listvalue){
	//listvalue点击行的值，点击函数名称需要修改
  if(listvalue=="0"){
    $("input[name='Q_T.ROLE_GROUPID_EQ']").val("");
  }else{
    $("input[name='Q_T.ROLE_GROUPID_EQ']").val(listvalue);
  }
  PlatUtil.tableDoSearch("role_datatable","role_search");
}
  function operladdclick(GROUP_ID){
	var url = "system/RoleGroupController.do?goForm&UI_DESIGNCODE=rolegroupform";
	var title = "新增角色组信息";
	if(GROUP_ID){
		url+=("&GROUP_ID="+GROUP_ID);
		title = "编辑角色组信息";
	}
	PlatUtil.openWindow({
	  title:title,
	  area: ["800px","300px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
            PlatUtil.reloadOperGroupList("rolegrouplist",{
    		});
		  }
	  }
	});
}
  function operleditclick(listvalue){
    operladdclick(listvalue);
}
  function operldelclick(listvalue){
	//listvalue点击行的值，点击函数名称需要修改
  PlatUtil.ajaxProgress({
    url:"system/RoleGroupController.do?multiDel",
    params:{
      groupId:listvalue
    },
    callback : function(resultJson) {
      if (resultJson.success) {
        parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false});
        PlatUtil.reloadOperGroupList("rolegrouplist",{});
      } else {
        parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
      }
    }
  });
}
</script>
</div>
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
   <div class="panel-Title">
	<h5 id="role_datatablepaneltitle">角色列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="role_search">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_T.ROLE_GROUPID_EQ" value="">
                			  <input type="hidden" name="Q_P.GROUP_ID_IN" value="${sessionScope.__BACKPLATUSER.GROUPIDS}">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.ROLE_CODE_LIKE" auth_type="write" label_value="角色编码" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.ROLE_NAME_LIKE" auth_type="write" label_value="角色名称" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('role_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('role_datatable','role_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<button type="button" onclick="addJqGridRecordInfo();" platreskey="addRole" id="402881e65b707b6e015b7098fde2002f" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="editJqGridRecordInfo();" platreskey="editRole" id="402881e65b707b6e015b709927620030" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		</button>
		<button type="button" onclick="delJqGridRecordInfo();" platreskey="delRole" id="402881e65b707b6e015b7099515e0031" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		</button>
		<button type="button" onclick="grantUsers();" platreskey="grantUsers" id="402848a55b8fc1c7015b8fca28440009" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-users"></i>&nbsp;分配用户
		</button>
		<button type="button" onclick="grantRights();" platreskey="grantRights" id="402881e65b99cd52015b99eebe1a0048" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-gavel"></i>&nbsp;分配权限
		</button>
		<button type="button" onclick="saveRoleSn();" platreskey="" id="402881e6633db38c01633db9a6df0041" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-sort"></i>&nbsp;保存排序
		</button>
	</div>
</div>

<div class="gridPanel">
	<table id="role_datatable"></table>
</div>
<div id="role_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(ROLE_ID){
	var url = "system/RoleController.do?goForm&UI_DESIGNCODE=roleform";
	var title = "新增角色信息";
	if(ROLE_ID){
		url+=("&ROLE_ID="+ROLE_ID);
		title = "编辑角色信息";
	}else{
        var roleGroupId = $("input[name='Q_T.ROLE_GROUPID_EQ']").val();
        url+=("&roleGroupId="+roleGroupId);
    }
	PlatUtil.openWindow({
	  title:title,
	  area: ["800px","300px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#role_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("role_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.ROLE_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"role_datatable",
		selectColName:"ROLE_ID",
		url:"metadata/DataResController.do?invokeRes&PLAT_RESCODE=system_role_del",
		callback:function(resultJson){
			$("#role_datatable").trigger("reloadGrid"); 
		}
	});
}
function grantUsers(){
	var rowData = PlatUtil.getTableOperSingleRecord("role_datatable");
	if(rowData){
		var ROLE_ID = rowData.ROLE_ID;
		var ROLE_NAME = rowData.ROLE_NAME;
        PlatUtil.setData(PlatUtil.WIN_SELECTOR_CONFIG,{
			//0标识不控制选择的条数
			maxselect:100,
			//最少需要选择的条数
			minselect:1
			//选中时的级联方式
		});
		PlatUtil.openWindow({
			title:"分配["+ROLE_NAME+"]的用户",
			area: ["90%","90%"],
			content: "system/RoleController.do?goUserGrant&ROLE_ID="+ROLE_ID,
			end:function(){
              var selectedRecords = PlatUtil.getData(PlatUtil.WIN_SELECTOR_RECORDS);
              if(selectedRecords&&selectedRecords.selectSuccess){
                    var checkUserIds = selectedRecords.checkIds;
					PlatUtil.removeData(PlatUtil.WIN_SELECTOR_RECORDS);
                    PlatUtil.ajaxProgress({
                        url:"system/RoleController.do?grantUsers",
                        params : {
                           ROLE_ID:ROLE_ID,
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
function grantRights(){
	var rowData = PlatUtil.getTableOperSingleRecord("role_datatable");
	if(rowData){
     var loginUser = PlatUtil.getBackPlatLoginUser();
     var SYSUSER_ACCOUNT = loginUser.SYSUSER_ACCOUNT;
     if(SYSUSER_ACCOUNT!="admin"){
       var ROLE_CODE = rowData.ROLE_CODE;
       var userRoleCodeArray = loginUser.ROLECODES.split(",");
       if(PlatUtil.isContain(userRoleCodeArray,ROLE_CODE)){
         parent.layer.alert("无法对自身拥有角色进行授权!",{icon:7,resize:false});
         return;
       }
     }
	  var ROLE_NAME = rowData.ROLE_NAME;
     var ROLE_ID = rowData.ROLE_ID;
		PlatUtil.openWindow({
			title:"分配["+ROLE_NAME+"]的权限",
			area: ["80%","80%"],
			content: "system/RoleController.do?goRightGrant&tableName=PLAT_SYSTEM_ROLE&ROLE_ID="+ROLE_ID,
			end:function(){
              
			}
		});
	}
}
function saveRoleSn(){
		var rowDatas = $("#role_datatable").jqGrid("getRowData");
		if(rowDatas.length>0){
			var roleIds = "";
			$.each(rowDatas,function(index,obj){
				if(index>0){
					roleIds+=",";
				}
				roleIds+=obj.ROLE_ID;
			});
			PlatUtil.ajaxProgress({
				url:"system/RoleController.do?updateSn",
				params:{
					roleIds:roleIds
				},
				callback:function(resultJson){
					parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false});
					$("#role_datatable").trigger("reloadGrid"); 
				}
			})
		}
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"role_datatable",
		  searchPanelId:"role_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402881e65b707b6e015b7093e5da0022",
		  sortable:true,
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "ROLE_ID",label:"角色ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "ROLE_CODE",label:"角色编码",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "ROLE_NAME",label:"角色名称",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "GROUP_NAME",label:"角色组名称",
		         width: 300,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "ROLE_SN",label:"排序值",
		         width: 50,align:"left",
		         
		         
		         
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
