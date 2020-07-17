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
    <title>系统用户列表</title>
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
   <plattag:treepanel panel_title="单位树面板" id="companyTree">
</plattag:treepanel>
<script type="text/javascript">
  //这个只是示例代码
function onTreePanelClick(event, treeId, treeNode, clickFlag) {
	if(event.target.tagName=="SPAN"){
		if(treeNode.id=="0"){
            $("input[name='Q_T.SYSUSER_COMPANYID_EQ']").val("");
            PlatUtil.reloadSelect("Q_D.DEPART_PATH_LIKE",{
                 dyna_param:""
            });
		}else{
            $("input[name='Q_T.SYSUSER_COMPANYID_EQ']").val(treeNode.id);
			PlatUtil.reloadSelect("Q_D.DEPART_PATH_LIKE",{
                 dyna_param:treeNode.id
            });
		}
		//刷新右侧列表
		PlatUtil.tableDoSearch("sysuser_datatable","sysuser_search");
	}
}  
  $(function(){
	  PlatUtil.initZtree({
		  treeId:"companyTree",
		  //自动补全匹配方式1:客户端匹配(客户端匹配则一次性加载所有数据) 2:服务端匹配(如果是服务端匹配则需要传入)
		  //如果需要自定义服务端url,请传入autoServerUrl
		  autoCompleteType:"1",
          edit : {
				enable : false,
                showRemoveBtn : false,
				showRenameBtn : false
		  },
		  callback: {
			  onClick:onTreePanelClick
		  },
		  async : {
			url:"common/baseController.do?tree",
			otherParam : {
				//树形表名称
				"tableName" : "PLAT_SYSTEM_COMPANY",
				//键和值的列名称
				"idAndNameCol" : "COMPANY_ID,COMPANY_NAME",
				//查询其它部门列名称
				"targetCols" : "COMPANY_CODE,COMPANY_PARENTID,COMPANY_TREESN",
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
	<h5 id="sysuser_datatablepaneltitle">系统用户列表</h5>
</div>
<div class="titlePanel">
	<div class="title-search form-horizontal" id="sysuser_search" formcontrolid="402848a55b56ec2a015b57273c74002b">
		<table>
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="Q_T.SYSUSER_COMPANYID_EQ" value="">
                			  <input type="hidden" name="Q_C.COMPANY_PATH_LIKE" value="${sessionScope.__BACKPLATUSER.SYSUSER_COMPANYID}">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
								<div class="form-group">
									<plattag:input name="Q_T.SYSUSER_ACCOUNT_LIKE" auth_type="write" label_value="用户账号" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
									<plattag:input name="Q_T.SYSUSER_NAME_LIKE" auth_type="write" label_value="用户姓名" maxlength="100" allowblank="true" placeholder="" label_col_num="3" comp_col_num="9"></plattag:input>
								</div>
								<div class="form-group">
								    <plattag:radio name="Q_T.SYSUSER_STATUS_EQ" auth_type="write" label_col_num="3" label_value="状态" static_values="所有:,正常:1,禁用:0" dyna_interface="" dyna_param="" select_first="true" allowblank="true" is_horizontal="true" comp_col_num="9">
								    </plattag:radio>
								</div>
								<div class="form-group">
								    <plattag:select placeholder="" istree="true" label_col_num="3" label_value="所属部门" style="width:100%;" allowblank="true" comp_col_num="9" auth_type="write" static_values="" dyna_interface="departService.findSelectTree" dyna_param="" name="Q_D.DEPART_PATH_LIKE">
								    </plattag:select>
								</div>
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('sysuser_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('sysuser_datatable','sysuser_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
	      <button type="button" onclick="addJqGridRecordInfo();" platreskey="addSysUser" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		  </button>
		
	      <button type="button" onclick="editJqGridRecordInfo();" platreskey="editSysUser" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-pencil"></i>&nbsp;编辑
		  </button>
		
	      <button type="button" onclick="delJqGridRecordInfo();" platreskey="delSysUser" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		  </button>
		
	      <button type="button" onclick="grantUserDepart();" platreskey="" id="grantUserDepart" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-code-fork"></i>&nbsp;设置分管部门
		  </button>
		
	      <button type="button" onclick="unlockUser();" platreskey="" id="unlockUser" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-unlock"></i>&nbsp;解锁用户
		  </button>
		
	       <div class="btn-group" id="OTHER_OPERDIV">
                 <button data-toggle="dropdown" class="btn btn-outline btn-info btn-sm dropdown-toggle">其它操作 <span class="caret"></span>
                 </button>
                 <ul class="dropdown-menu">
	                     <li platreskey="resetPass">
	                     <a onclick="resetPassword();" style="cursor: pointer;">重置密码</a>
	                     </li>
	                     <li platreskey="">
	                     <a onclick="saveSysUserSn();" style="cursor: pointer;">保存排序</a>
	                     </li>
	                     <li platreskey="">
	                     <a onclick="grantRights();" style="cursor: pointer;">分配权限</a>
	                     </li>
	                     <li platreskey="queryRights">
	                     <a onclick="queryJqGridRecordInfo();" style="cursor: pointer;">查看拥有权限</a>
	                     </li>
                 </ul>
            </div>
		  
		
		
		
		
	</div>
</div>

<div class="gridPanel">
	<table id="sysuser_datatable"></table>
</div>
<div id="sysuser_datatable_pager"></div>
<script type="text/javascript">
function addJqGridRecordInfo(SYSUSER_ID){
	var url = "system/SysUserController.do?goForm&UI_DESIGNCODE=sysuserForm";
	var title = "新增系统用户信息";
	if(SYSUSER_ID){
		url+=("&SYSUSER_ID="+SYSUSER_ID);
		title = "编辑系统用户信息";
	}else{
       var companyId = $("input[name='Q_T.SYSUSER_COMPANYID_EQ']").val();
       url+="&companyId="+companyId;
    }
	PlatUtil.openWindow({
	  title:title,
	  area: ["1000px","330px"],
	  content: url,
	  end:function(){
		  if(PlatUtil.isSubmitSuccess()){
              //弹出框提交成功后,需要回调的代码
			  $("#sysuser_datatable").trigger("reloadGrid"); 
		  }
	  }
	});
}
function editJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("sysuser_datatable");
	if(rowData){
		addJqGridRecordInfo(rowData.SYSUSER_ID);
	}
}
function delJqGridRecordInfo(){
	PlatUtil.operMulRecordForTable({
		tableId:"sysuser_datatable",
		selectColName:"SYSUSER_ID",
		url:"metadata/DataResController.do?invokeRes&PLAT_RESCODE=system_sysuser_del",
		callback:function(resultJson){
			$("#sysuser_datatable").trigger("reloadGrid"); 
		}
	});
}
function grantUserDepart(){
  var rowData = PlatUtil.getTableOperSingleRecord("sysuser_datatable");
    var SYSUSER_ID = rowData.SYSUSER_ID;
    	if(SYSUSER_ID=="0"){
    		parent.layer.alert("根节点禁止分配!",{icon: 2,resize:false});
    	}else{
    		rowData.isEdit = true;
		var SYSUSER_NAME = rowData.SYSUSER_NAME;
        var COMPANY_NAME = rowData.COMPANY_NAME;
        PlatUtil.setData(PlatUtil.WIN_SELECTOR_CONFIG,{
			//0标识不控制选择的条数
			maxselect:100,
			//最少需要选择的条数
			minselect:1
			//选中时的级联方式
		});
		PlatUtil.openWindow({
			title:"分配用户["+SYSUSER_NAME+"]的分管部门",
			area: ["90%","90%"],
			content: "system/UserDepartController.do?goUserDepartGrant&SYSUSER_ID="+SYSUSER_ID,
			end:function(){
              var selectedRecords = PlatUtil.getData(PlatUtil.WIN_SELECTOR_RECORDS);
              if(selectedRecords&&selectedRecords.selectSuccess){
                    var checkUserIds = selectedRecords.checkIds;
					PlatUtil.removeData(PlatUtil.WIN_SELECTOR_RECORDS);
                    PlatUtil.ajaxProgress({
                         url:"system/UserDepartController.do?grantUserDepart",
								  //url:"metadata/DataResController.do?invokeRes&PLAT_RESCODE=system_sysuser_setuserdepts",
                        params : {
                           SYSUSER_ID:SYSUSER_ID,
                           checkUserIds:checkUserIds
                        },
                        callback : function(resultJson) {
                            if (resultJson.success) {
                                parent.layer.msg(PlatUtil.SUCCESS_MSG,{icon: 1, resize: false});
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
function unlockUser(){
  PlatUtil.operMulRecordForTable({
		tableId:"sysuser_datatable",
		selectColName:"SYSUSER_ID",
    	tipMsg:"您确定要给所选用户解锁吗?",
		url:"system/SysUserController/unlockUser.do",
		callback:function(resultJson){
			$("#sysuser_datatable").trigger("reloadGrid"); 
		}
	});
}
function resetPassword(){
	PlatUtil.operMulRecordForTable({
		tableId:"sysuser_datatable",
		selectColName:"SYSUSER_ID",
     tipMsg:"您确定要重置所选用户的密码吗?",
		url:"system/SysUserController.do?resetPass",
		callback:function(resultJson){
			$("#sysuser_datatable").trigger("reloadGrid"); 
		}
	});
}
function saveSysUserSn(){
	var companyId = $("input[name='Q_T.SYSUSER_COMPANYID_EQ']").val();
	if(companyId!=""){
		var rowDatas = $("#sysuser_datatable").jqGrid("getRowData");
		if(rowDatas.length>0){
			var userIds = "";
			$.each(rowDatas,function(index,obj){
				if(index>0){
					userIds+=",";
				}
				userIds+=obj.SYSUSER_ID;
			});
			PlatUtil.ajaxProgress({
				url:"system/SysUserController.do?updateSn",
				params:{
					userIds:userIds
				},
				callback:function(resultJson){
					parent.layer.alert(PlatUtil.SUCCESS_MSG,{icon: 1,resize:false});
					$("#sysuser_datatable").trigger("reloadGrid"); 
				}
			})
		}
	}else{
		parent.layer.alert("请选择单位后,然后再进行拖拽排序!",{icon: 7,resize:false});
	}
}
function grantRights(){
	var rowData = PlatUtil.getTableOperSingleRecord("sysuser_datatable");
	if(rowData){
     var loginUser = PlatUtil.getBackPlatLoginUser();
     var SYSUSER_ACCOUNT = loginUser.SYSUSER_ACCOUNT;
     if(SYSUSER_ACCOUNT!="admin"){
       var account = rowData.SYSUSER_ACCOUNT;
       if(SYSUSER_ACCOUNT==account){
         parent.layer.alert("无法对自身进行授权!",{icon:7,resize:false});
         return;
       }
     }
	  var SYSUSER_NAME = rowData.SYSUSER_NAME;
     var ROLE_ID = rowData.SYSUSER_ID;
		PlatUtil.openWindow({
			title:"分配["+SYSUSER_NAME+"]的权限",
			area: ["80%","80%"],
			content: "system/RoleController.do?goRightGrant&tableName=PLAT_SYSTEM_SYSUSER&ROLE_ID="+ROLE_ID,
			end:function(){
              
			}
		});
	}
}
function queryJqGridRecordInfo(){
	var rowData = PlatUtil.getTableOperSingleRecord("sysuser_datatable");
	if(rowData){
        var SYSUSER_ID = rowData.SYSUSER_ID;
        var SYSUSER_NAME = rowData.SYSUSER_NAME;
        PlatUtil.openWindow({
			title:"查看["+SYSUSER_NAME+"]的权限",
			area: ["80%","80%"],
			content: "system/SysUserController.do?goQueryRight&SYSUSER_ID="+SYSUSER_ID,
			end:function(){
              
			}
		});
	}
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"sysuser_datatable",
		  searchPanelId:"sysuser_search",
		  url: "appmodel/CommonUIController.do?tabledata&FORMCONTROL_ID=402848a55b56ec2a015b57273c74002b",
		  sortable:true,
		  ondblClickRow:function(rowid,iRow,iCol,e){
				  
		  },
		  colModel: [
		         {name: "SYSUSER_ID",label:"用户ID",
		         width: 100,align:"left",
		         hidden:true,
		         
		         
		         sortable:false
		         },
		         {name: "SYSUSER_ACCOUNT",label:"用户账号",
		         width: 150,align:"left",
		         
		         
		         sortable:true,index: "O_T.SYSUSER_ACCOUNT"
		         
		         },
		         {name: "SYSUSER_NAME",label:"用户姓名",
		         width: 150,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "SYSUSER_STATUS",label:"状态",
		         width: 100,align:"left",
		         
		         formatter:function(cellvalue, options, rowObject){
	//rowObject对象代表这行的数据对象
    if(cellvalue=="0"){
       return "<span class=\"label label-danger\">禁用</span>";
    }else if(cellvalue=="1"){
       return "<span class=\"label label-primary\">正常</span>";      
    }else if(cellvalue=="2"){
       return "<span class=\"label label-warning\">锁定</span>";      
    }
    
},
		         
		         sortable:false
		         },
		         {name: "SYSUSER_MOBILE",label:"手机号",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "COMPANY_NAME",label:"单位名称",
		         width: 200,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "DEPART_NAME",label:"部门名称",
		         width: 100,align:"left",
		         
		         
		         
		         sortable:false
		         },
		         {name: "SYSUSER_SN",label:"排序值",
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
