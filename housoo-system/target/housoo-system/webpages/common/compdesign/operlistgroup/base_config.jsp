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
    <jsp:param value="operlistgroupbase" name="DESIGN_CODE"/>
</jsp:include>
<!-- 结束引入基本信息界面 -->

<script type="text/javascript">

function saveListGroupBaseConfig(){
	var result = false;
	var formData = PlatUtil.getFormEleData("BaseConfigForm");
	var EVENT_JSON = PlatUtil.getEditTableAllRecordJson("columnTable");
	formData.EVENT_JSON = EVENT_JSON;
	result  = PlatUtil.saveUIBaseConfigAndGoAttach(formData);
	return result;
}

$(function(){
	PlatUtil.initUIComp("#BaseConfigForm");
	
});

function setColumnFormatJs(columnName){
	var expcodePath = "operlistgroup/clickexpjs.js";
	var oldConfigData = $("input[name='"+columnName+"']").val();
	if(oldConfigData&&oldConfigData!=""){
		PlatUtil.setData(columnName,oldConfigData);
	}
	var url = "appmodel/FormControlController.do?goExpCodeView&allowedit=true&expcodePath="+expcodePath;
	url+="&keyName="+columnName;
	PlatUtil.openWindow({
		title:"点击函数实现",
		area: ["800px","80%"],
		content: url,
		end:function(){
		    if(PlatUtil.isSubmitSuccess()){
		    	var configCode = PlatUtil.getData(columnName);
		    	$("input[name='"+columnName+"']").val(configCode);
		    	PlatUtil.removeData(columnName);
		    }
		}
	});
}
</script>
