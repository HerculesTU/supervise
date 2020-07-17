<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.DesignService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
  
%>

<!-- 开始引入基本信息界面 -->
<jsp:include page='<%="/appmodel/DesignController/includeUI.do"%>' >
    <jsp:param value="griditembaseconfig" name="DESIGN_CODE"/>
</jsp:include>
<!-- 结束引入基本信息界面 -->

<script type="text/javascript">
function saveGridItemBaseConfig(){
	var result = false;
	var formData = PlatUtil.getFormEleData("BaseConfigForm");
	var COLUMN_JSON = PlatUtil.getEditTableAllRecordJson("griditemconf");
	formData.COLUMN_JSON = COLUMN_JSON;
	result  = PlatUtil.saveUIBaseConfigAndGoAttach(formData);
	return result;
}

$(function(){
	PlatUtil.initUIComp("#BaseConfigForm");
});
</script>

