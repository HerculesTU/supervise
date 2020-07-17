<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" id="ResForm">
	<div class="form-group plat-form-title">
		<span class="plat-current">资源选择</span>
	</div>
	<div class="form-group">
		<plattag:checkbox name="DESIGN_INTERNALRESES" auth_type="write"
			allowblank="false" is_horizontal="true" comp_col_num="10" 
			label_col_num="2" label_value="内部资源" value="${design.DESIGN_INTERNALRESES}"
			dyna_interface="dictionaryService.findList"
			dyna_param="{TYPE_CODE:'INTERNAL_RES',ORDER_TYPE:'ASC'}">
		</plattag:checkbox>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:winselector selectorurl="appmodel/FormControlController.do?goExternalSelector" 
	    width="450px" height="550px" maxselect="10" minselect="0" title="外部资源选择器" 
	    label_col_num="2" label_value="外部资源" allowblank="true" comp_col_num="10"
	    name="DESIGN_EXTERNALRESES" placeholder="请选择外部资源" auth_type="write"
	    value="${design.DESIGN_EXTERNALRESES}" selectedlabels="${design.DESIGN_EXTERNALRESES_LABELS}"
	    checktype="checkbox" checkcascadey="" checkcascaden="" >
	    </plattag:winselector>
	</div>
</form>