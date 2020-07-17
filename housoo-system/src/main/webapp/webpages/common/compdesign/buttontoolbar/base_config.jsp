<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.model.SqlFilter"%>
<%@ page language="java" import="com.housoo.platform.core.service.DesignService"%>
<%@ page language="java" import="com.housoo.platform.core.service.CommonUIService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%

%>
<form method="post" class="form-horizontal" id="BaseConfigForm">
<jsp:include page="/webpages/common/compdesign/jqgrid/tablebutton_grid.jsp"></jsp:include>
</form>

<script type="text/javascript">

function saveButtonToolbarConfig(){
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
	}
	result  = PlatUtil.saveUIBaseConfigAndGoAttach(formData);
	return result;
}

$(function(){
	PlatUtil.initJqGrid({
		  tableId:"tablebutton_datatable",
		  searchPanelId:"tablebutton_search",
		  url: "appmodel/TableButtonController.do?datagrid",
		  nopager:true,
		  sortable:true,
		  height:300,
		  colModel: [
	          {  name: "TABLEBUTTON_ID",label:"",hidden:true},
	          {  name: "TABLEBUTTON_RESKEY",label:"按钮KEY",width:200, align:"left",sortable:false},
	          {  name: "TABLEBUTTON_NAME",label:"按钮名称",width: 200, align:"left",sortable:false},
	          {  name: "TABLEBUTTON_ICON",label:"按钮图标",width: 100, align:"left",sortable:false
	        	,formatter:function(cellvalue, options, rowObject){
	        		return "<i class=\""+cellvalue+"\"></i>"+cellvalue;
	        	}  
	          },
	          {  name: "TABLEBUTTON_FN",label:"按钮点击函数名",width:200, align:"left",sortable:false}
	      ],
	      gridComplete:function(){
	      	 $("#tablebutton_datatable").setGridWidth(($(".ui-layout-south").width())-10);
	      }
	});
});
</script>
