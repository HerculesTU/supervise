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
    <title>字典信息表单</title>
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
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="system/DictionaryController.do?saveOrUpdate" id="DictionaryForm" style="">

  <input type="hidden" name="DIC_ID" value="${dictionary.DIC_ID}">
  <input type="hidden" name="DIC_DICTYPE_CODE" value="${dictionary.DIC_DICTYPE_CODE}">
<div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		基本信息
	</span>
</div><div class="form-group" compcode="formgroup">
   <plattag:input name="DICTYPE_NAME" allowblank="false" auth_type="readonly" value="${dictionary.DICTYPE_NAME}" datarule="required;" label_value="所属类别" placeholder="所属类别" comp_col_num="4" label_col_num="2">
   </plattag:input>
   <plattag:input name="DIC_NAME" allowblank="false" auth_type="write" value="${dictionary.DIC_NAME}" datarule="required;" maxlength="30" label_value="字典名称" placeholder="请输入字典名称" comp_col_num="4" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="DIC_VALUE" allowblank="true" auth_type="write" value="${dictionary.DIC_VALUE}" datarule="remote[system/DictionaryController.do?validDicValue, DIC_ID, DIC_DICTYPE_CODE]" maxlength="126" label_value="字典值" placeholder="请输入字典值" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group plat-form-title" compcode="formtitle" id="">
    <span class="plat-current">
		附加属性配置
	</span>
</div><div class="titlePanel">
	<div class="title-search form-horizontal" id="dicattach_search">
		<table style="display: none;">
			<tr>
				<td>查询条件</td>
				<td style="padding-left: 5px;">
					<div class="table-filter">
                			  <input type="hidden" name="DICATTACH_DICID" value="${dictionary.DIC_ID}">
						<input type="text" style="width: 200px; " onclick="PlatUtil.showOrHideSearchTable(this);" class="table-form-control" name="search" readonly="readonly">
						<div class="table-filter-list" style="width: 420px; display: none;max-height: 280px;">
							<div class="table-filter-list-bottom">
								<a onclick="PlatUtil.tableSearchReset('dicattach_search');" class="btn btn-default" href="javascript:void(0);"> 重 置</a> 
								<a onclick="PlatUtil.tableDoSearch('dicattach_datatable','dicattach_search',true);" class="btn btn-primary" href="javascript:void(0);"> 查 询</a>
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
	<plattag:edittable dyna_interface="dicAttachService.findEditTableDatas" tr_tplpath="background/genui/dictionaryform_sub/dicattach" id="dicattach" searchpanel_id="dicattach_search" col_style="[20%,附加属性名],[20%,附加属性KEY],[60%,附加属性值]">
	</plattag:edittable>
</div>

<script type="text/javascript">
function addEditTableRow(){
	PlatUtil.addEditTableRow({
		tableId:"dicattach"
	});
}
function delEditTableRow(){
  PlatUtil.removeEditTableRows('dicattach');
}

</script>
</form></div>
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
	if(PlatUtil.isFormValid("#DictionaryForm")){
		var url = $("#DictionaryForm").attr("action");
        var ATTACH_JSON = PlatUtil.getEditTableAllRecordJson("dicattach");
		var formData = PlatUtil.getFormEleData("DictionaryForm");
        formData.ATTACH_JSON = ATTACH_JSON;
		PlatUtil.ajaxProgress({
			url:url,
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

</script>
