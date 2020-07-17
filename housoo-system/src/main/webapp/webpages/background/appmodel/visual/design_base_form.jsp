<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal"
	action="appmodel/DesignController.do?saveBaseInfo" id="DesignForm">
	<input type="hidden" name="DESIGN_ID" value="${design.DESIGN_ID}">
	<input type="hidden" id="designToken" name="designToken">
	<div class="form-group plat-form-title">
		<span class="plat-current"> 基本信息 </span>
	</div>
	<div class="form-group">
	    <plattag:select istree="true" allowblank="false" comp_col_num="4" auth_type="write" 
	     value="${design.DESIGN_MODULEID}" label_value="所在模块" label_col_num="2"
	     dyna_interface="commonUIService.findGenTreeSelectorDatas" 
	     dyna_param="[TABLE_NAME:PLAT_APPMODEL_MODULE],[TREE_IDANDNAMECOL:MODULE_ID,MODULE_NAME],[TREE_QUERYFIELDS:MODULE_PARENTID,MODULE_PATH],[FILTERS:MODULE_PARENTID_EQ|0]"
	    name="DESIGN_MODULEID"></plattag:select>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
		<plattag:input name="DESIGN_CODE"
			auth_type="write"
			allowblank="false" placeholder="请输入设计编码" comp_col_num="4"
			value="${design.DESIGN_CODE}" label_col_num="2" label_value="设计编码"
			datarule="required;onlyLetterNumberUnderLine;remote[common/baseController.do?checkUnique&VALID_TABLENAME=PLAT_APPMODEL_DESIGN&VALID_FIELDLABLE=设计编码&VALID_FIELDNAME=DESIGN_CODE&RECORD_ID=${design.DESIGN_ID}]"></plattag:input>
		<plattag:input name="DESIGN_NAME" auth_type="write" allowblank="false"
			placeholder="请输入设计名称" comp_col_num="4" label_col_num="2"
			maxlength="30" datarule="required;" label_value="设计名称"
			value="${design.DESIGN_NAME}">
		</plattag:input>
	</div>

	<div class="form-group plat-form-title">
		<span class="plat-current">涉及表信息 </span>
	</div>
	<div class="titlePanel">
		<div class="title-search form-horizontal" id="associal_search">
			<input type="hidden" name="DESIGN_ID" value="${design.DESIGN_ID}">
		</div>
		<div class="toolbar">
			<button type="button" onclick="addAssocialTable();"
				class="btn btn-outline btn-primary btn-sm">
				<i class="fa fa-plus"></i>&nbsp;新增
			</button>
			<button type="button"
				onclick="PlatUtil.removeEditTableRows('associalTable');"
				class="btn btn-outline btn-danger btn-sm">
				<i class="fa fa-trash-o"></i>&nbsp;删除
			</button>
		</div>
	</div>
	<div class="gridPanel">
		<plattag:edittable dyna_interface="designService.findAssocialTables"
			tr_tplpath="background/appmodel/visual/associaltable_tr" 
			id="associalTable" searchpanel_id="associal_search" 
			col_style="[30%,表名],[10%,模块包名],[20%,实体类名],[15%,中文名],[5%,表别名],[10%,生成代码]">
		</plattag:edittable>
	</div>
</form>