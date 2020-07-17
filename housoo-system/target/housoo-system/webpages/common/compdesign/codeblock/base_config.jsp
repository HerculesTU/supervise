<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatFileUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.DesignService"%>
<%@ page language="java" import="org.apache.commons.lang3.StringUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
  
%>

<!-- 开始引入基本信息界面 -->
<jsp:include page='<%="/appmodel/DesignController/includeUI.do"%>' >
    <jsp:param value="codeblockbaseconfig" name="DESIGN_CODE"/>
</jsp:include>
<!-- 结束引入基本信息界面 -->

<script type="text/javascript">

function saveCodeBlockBaseConfig(){
	var result = false;
	var formData = PlatUtil.getFormEleData("BaseConfigForm");
	var CODE_TEXT = PlatUtil.PLAT_CODEMIRROREDITOR.getValue();
	formData.CODE_TEXT = CODE_TEXT;
	result  = PlatUtil.saveUIBaseConfigAndGoAttach(formData);
	return result;
}

$(function(){
	PlatUtil.initUIComp("#BaseConfigForm");
	
});
</script>
