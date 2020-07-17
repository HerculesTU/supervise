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
    <title>系统资源表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,touchspin"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,touchspin"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <div class="tabs-container" platundragable="true" compcode="bootstraptab" id="ResTab">
	<ul class="nav nav-tabs">
		<li class="active"><a data-toggle="tab" href="#tab1" aria-expanded="true">基本配置</a></li>
		<li class=""><a data-toggle="tab" href="#tab2" aria-expanded="false">权限URL配置</a></li>
	</ul>
	<div class="tab-content" platundragable="true" compcode="bootstraptab">
		<div id="tab1" class="tab-pane active" platundragable="true" compcode="bootstraptab">
		<form method="post" class="form-horizontal" compcode="formcontainer" action="" id="ResBaseForm" style="">

  <input type="hidden" name="RES_ID" value="${res.RES_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:select name="RES_PARENTID" allowblank="false" auth_type="write" value="${res.RES_PARENTID}" istree="true" onlyselectleaf="false" label_value="上级资源" placeholder="请选择父资源ID" comp_col_num="4" label_col_num="2" dyna_interface="resService.findTreeSelectRes" dyna_param="无参数">
   </plattag:select>
   <plattag:number name="RES_TREESN" allowblank="false" auth_type="write" value="${res.RES_TREESN}" step="1" decimals="0" label_value="资源排序" placeholder="请输入同级排序" comp_col_num="4" label_col_num="2" max="1000000" min="1">
   </plattag:number>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="RES_CODE" allowblank="false" auth_type="write" value="${res.RES_CODE}" datarule="required;onlyLetterNumberUnderLine;remote[common/baseController.do?checkUnique&amp;VALID_TABLENAME=PLAT_SYSTEM_RES&amp;VALID_FIELDLABLE=资源编码&amp;VALID_FIELDNAME=RES_CODE]" maxlength="30" label_value="资源编码" placeholder="请输入资源编码" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="RES_NAME" allowblank="false" auth_type="write" value="${res.RES_NAME}" datarule="required;" maxlength="30" label_value="资源名称" placeholder="请输入资源名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:radio name="RES_TYPE" allowblank="false" auth_type="write" value="${res.RES_TYPE}" select_first="true" is_horizontal="true" label_value="资源类型" comp_col_num="4" label_col_num="2" static_values="菜单:1,操作权限:2,应用系统:3">
   </plattag:radio>
   <plattag:winselector name="RES_MENUICON" allowblank="true" auth_type="write" value="${res.RES_MENUICON}" selectorurl="appmodel/DesignController.do?goFontSelect" title="按钮图标选择器" width="1200px" height="100%" selectedlabels="${res.RES_MENUICON}" maxselect="1" minselect="1" label_value="菜单图标" placeholder="请输入菜单图标" comp_col_num="4" label_col_num="2">
   </plattag:winselector>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="RES_MENUURL" allowblank="true" auth_type="write" value="${res.RES_MENUURL}" maxlength="200" label_value="菜单访问地址" placeholder="请输入菜单访问地址" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div>   <plattag:radio name="RES_STATUS" allowblank="true" auth_type="write" value="${res.RES_STATUS}" select_first="true" is_horizontal="true" label_value="资源状态" comp_col_num="4" label_col_num="2" static_values="启用:1,禁用:2">
   </plattag:radio>
</form></div>
		<div id="tab2" class="tab-pane " platundragable="true" compcode="bootstraptab">
		<form method="post" class="form-horizontal" compcode="formcontainer" action="" id="ResUrlForm" style="">

<div class="titlePanel">
	<div class="title-search form-horizontal" id="ResUrlTable_search">
		<table style="display: none;">
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="RES_ID" value="${res.RES_ID}">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('ResUrlTable_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('ResUrlTable_datatable','ResUrlTable_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="toolbar">
		<button type="button" onclick="addEditTableRow();" id="" class="btn btn-outline btn-info btn-sm">
			<i class="fa fa-plus"></i>&nbsp;新增
		</button>
		<button type="button" onclick="delEditTableRow();" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-trash"></i>&nbsp;删除
		</button>
	</div>
</div>

<div class="gridPanel">
	<plattag:edittable dyna_interface="resService.findResUrlByFilter" tr_tplpath="background/genui/systemresform_sub/ResUrlTable" id="ResUrlTable" searchpanel_id="ResUrlTable_search" col_style="[90%,访问控制URL]">
	</plattag:edittable>
</div>

<script type="text/javascript">
function addEditTableRow(){
	PlatUtil.addEditTableRow({
		tableId:"ResUrlTable"
	});
}
function delEditTableRow(){
  PlatUtil.removeEditTableRows('ResUrlTable');
}

</script>
</form></div>
	</div>
</div></div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" id="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;提交
		</button>
		<button type="button" onclick="closeWindow();" id="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
    if(PlatUtil.validBootstrapTab("ResTab")){
        //获取父亲资源的值
        var RES_PARENTID = PlatUtil.getSelectAttValue("RES_PARENTID","value");
        if("${res.RES_ID}"==RES_PARENTID){
            parent.layer.alert("禁止将自身资源选取为上级资源",{icon: 2,resize:false});
            return;
        }
        var formData = PlatUtil.getFormEleData("ResBaseForm");
		var RES_OPERURLJSON = PlatUtil.getEditTableAllRecordJson("ResUrlTable");
        if(RES_OPERURLJSON){
           formData.RES_OPERURLJSON = RES_OPERURLJSON;
        }else{
           formData.RES_OPERURLJSON = "";
        }
        PlatUtil.ajaxProgress({
			url:"system/ResController.do?saveOrUpdate",
			params : formData,
			callback : function(resultJson) {
				if (resultJson.success) {
					parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
					PlatUtil.setData("submitSuccess",true);
					PlatUtil.closeWindow();
				} else {
					parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
				}
			}
		});
    }
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
$(function(){
  if(!"${res.RES_ID}"){
    PlatUtil.changeUICompAuth("hidden","RES_TREESN");
  }else{
    PlatUtil.changeUICompAuth("readonly","RES_CODE");
  }
});
</script>
