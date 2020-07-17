<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" id="BaseConfigForm">
     <div class="form-group plat-form-title">
		<span class="plat-current"> 基本信息 </span>
	</div>
	<div class="form-group">
	    <plattag:input name="CONTAINER_HEIGHT" auth_type="write" allowblank="false"
			placeholder="请输入容器高度" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTAINER_HEIGHT}"
			maxlength="30" datarule="required;" label_value="容器高度">
		</plattag:input>
		<plattag:input name="CONTROL_ID" auth_type="write" allowblank="true"
			placeholder="请输入组件ID" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_ID}"
			maxlength="30" label_value="组件ID">
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group plat-form-title">
		<span class="plat-current">容器布局设置</span>
	</div>
	<div class="titlePanel">
		<div class="title-search form-horizontal" id="layouttable_search">
			<input type="hidden" name="FORMCONTROL_ID" value="${formControl.FORMCONTROL_ID}">
		</div>
		<div class="toolbar">
			<button type="button" onclick="addLayoutTable();"
				class="btn btn-outline btn-primary btn-sm">
				<i class="fa fa-plus"></i>&nbsp;新增
			</button>
			<button type="button"
				onclick="PlatUtil.removeEditTableRows('layoutTable');"
				class="btn btn-outline btn-danger btn-sm">
				<i class="fa fa-trash-o"></i>&nbsp;删除
			</button>
		</div>
	</div>
	<div class="gridPanel">
		<plattag:edittable dyna_interface="commonUIService.findDirectLayouts"
			tr_tplpath="common/compdesign/direct_layout/layout_tr"
			id="layoutTable" searchpanel_id="layouttable_search"
			col_style="[20%,布局类型],[40%,支持垂直滚动],[20%,布局大小],[20%,布局ID]">
		</plattag:edittable>
	</div>
</form>

<script type="text/javascript">
$(function(){
	PlatUtil.initUIComp();
});

function addLayoutTable(){
	PlatUtil.addEditTableRow({
		tableId:"layoutTable"
	});
}

function saveDirectLayoutBase(){
	var result = false;
	var layoutTableRows = PlatUtil.getEditTableAllRecord("layoutTable");
	if(layoutTableRows.length>0){
		var LAYOUT_JSON = JSON.stringify(layoutTableRows);
		var formData = PlatUtil.getFormEleData("BaseConfigForm");
		formData.LAYOUT_JSON = LAYOUT_JSON;
		result  = PlatUtil.saveUIBaseConfigAndGoAttach(formData);
	}else{
		parent.layer.alert("请至少创建一种布局!",{icon: 2,resize:false});
	}
	return result;
}
</script>

