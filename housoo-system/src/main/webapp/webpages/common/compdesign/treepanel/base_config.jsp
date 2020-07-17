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
   Map<String,Object> fieldInfo = (Map<String,Object>)request.getAttribute("fieldInfo");
   if(fieldInfo!=null){
       String CONTROL_ID = (String)fieldInfo.get("CONTROL_ID");
       if(StringUtils.isEmpty(CONTROL_ID)){
           fieldInfo.put("TREE_CLICKFNNAME", "onTreePanelClick");
           String appPath = PlatAppUtil.getAppAbsolutePath();
           StringBuffer filePath = new StringBuffer(appPath);
           filePath.append("webpages/common/compdesign/treepanel/ontreeclickexpjs.js");
           String expCode = PlatFileUtil.readFileString(filePath.toString());
           fieldInfo.put("TREE_CLICKFN",expCode);
       }
   }
%>

<!-- 开始引入基本信息界面 -->
<jsp:include page='<%="/appmodel/DesignController/includeUI.do"%>' >
    <jsp:param value="treepanelbase" name="DESIGN_CODE"/>
</jsp:include>
<!-- 结束引入基本信息界面 -->

<script type="text/javascript">

function saveTreeBaseConfig(){
	var result = false;
	var formData = PlatUtil.getFormEleData("BaseConfigForm");
	var TREE_CLICKFN = PlatUtil.PLAT_CODEMIRROREDITOR.getValue();
	formData.TREE_CLICKFN = TREE_CLICKFN;
	result  = PlatUtil.saveUIBaseConfigAndGoAttach(formData);
	return result;
}

$(function(){
	PlatUtil.initUIComp("#BaseConfigForm");
	$("select[name='TREE_TABLENAME']").change(function() {
		var tableName = $(this).val();
		PlatUtil.reloadSelect("ID_ANDNAME",{
			dyna_param:tableName
	    });
		PlatUtil.reloadSelect("TARGET_COLS",{
			dyna_param:tableName
	    });
	});
	
});
</script>
