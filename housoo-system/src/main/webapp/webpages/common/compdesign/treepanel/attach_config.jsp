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
    <jsp:param value="treepanelattachconfig" name="DESIGN_CODE"/>
</jsp:include>
<!-- 结束引入基本信息界面 -->

<script type="text/javascript">

function saveTreeAttachConfig(){
	var result = false;
	var tablebuttons = $("#tablebutton_datatable").jqGrid("getRowData");
	var TABLEBUTTON_IDS = "";
	if(tablebuttons.length>0){
		$.each(tablebuttons,function(index,obj){
			if(index>0){
				TABLEBUTTON_IDS+=",";
			}
			TABLEBUTTON_IDS+=obj.TABLEBUTTON_ID;
		});
	}
	var formData = {
		TABLEBUTTON_IDS:TABLEBUTTON_IDS
	};
	result = PlatUtil.saveUIAttachConfig(formData);
	return result;
}

$(function(){
	PlatUtil.initUIComp("#treepanelattachtab");
	PlatUtil.initJqGrid({
		  tableId:"searchfield_datatable",
		  searchPanelId:"searchfield_search",
		  url: "appmodel/QueryConditionController.do?datagrid",
		  nopager:true,
		  sortable:true,
		  height:300,
		  colModel: [
          {  name: "QUERYCONDITION_ID",label:"",hidden:true},
          {  name: "QUERYCONDITION_CTRLNAME",label:"控件命名",width: 300, align:"left",sortable:false},
          {  name: "QUERYCONDITION_LABEL",label:"控件标签",width: 200, align:"left",sortable:false},
          {  name: "CTRLTYPE",label:"控件类型",width:200, align:"left",sortable:false},
          {  name: "QUERYSTRAGE",label:"查询方案",width:200, align:"left",sortable:false}
       ],
       gridComplete:function(){
      	 $("#searchfield_datatable").setGridWidth(($(".ui-layout-south").width())-10);
       }
	});
	
});
</script>
