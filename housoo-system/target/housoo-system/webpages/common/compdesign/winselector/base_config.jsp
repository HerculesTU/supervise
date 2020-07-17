<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.DesignService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<!-- 开始引入基本信息界面 -->
<jsp:include page='<%="/appmodel/DesignController/includeUI.do"%>' >
    <jsp:param value="winselectorconfig" name="DESIGN_CODE"/>
</jsp:include>
<!-- 结束引入基本信息界面 -->

<script type="text/javascript">

$(function(){
	PlatUtil.initUIComp("#BaseConfigForm");
	$("select[name='ASSOCIAL_FIELDNAME']").change(function() {
		var fieldName = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","field_name");
		var columnName = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","column_name");
		var field_comments = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","field_comments");
		var maxLength = PlatUtil.getSelectAttValue("ASSOCIAL_FIELDNAME","max_length");
		$("input[name='MAX_LENGTH']").val(maxLength/2-2);
		$("input[name='CONTROL_NAME']").val(columnName);
		$("input[name='CONTROL_VALUE']").val("\\${"+fieldName+"}");
		$("input[name='SELECTEDLABELS']").val("\\${"+fieldName+"_LABELS}");
		$("input[name='PLAT_COMPNAME']").val(field_comments);
		$("input[name='CONTROL_LABEL']").val(field_comments);
		$("input[name='COMP_PLACEHOLDER']").val("请输入"+field_comments);
	});
	
	
});
</script>
