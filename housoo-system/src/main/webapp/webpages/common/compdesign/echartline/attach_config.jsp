<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.DesignService"%>
<%@ page language="java" import="com.housoo.platform.core.service.CommonUIService"%>
<%@ page language="java" import="com.housoo.platform.core.model.SqlFilter"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
%>


<div class="tabs-container" id="datatableAttachTab">
	<ul class="nav nav-tabs">
		<li class="active" ><a data-toggle="tab" href="#tab-1"
			aria-expanded="true"><i class="fa fa-bars"></i>查询条件配置</a></li>
	</ul>
	<div class="tab-content">
		<div id="tab-1" class="tab-pane active">
		    <jsp:include page="/webpages/common/compdesign/jqgrid/searchfield_form.jsp"></jsp:include>
		</div>
	</div>
</div>

<script type="text/javascript">

function saveEchartAttachConfig(){
	if(PlatUtil.validBootstrapTab("datatableAttachTab")){
		var searchFields = $("#searchfield_datatable").jqGrid("getRowData");
		var QUERYCONDITIONIDS = "";
		if(searchFields.length>0){
			$.each(searchFields,function(index,obj){
				if(index>0){
					QUERYCONDITIONIDS+=",";
				}
				QUERYCONDITIONIDS+=obj.QUERYCONDITION_ID;
			});
		}
		var formData = {
			QUERYCONDITIONIDS:QUERYCONDITIONIDS
		};
		result = PlatUtil.saveUIAttachConfig(formData);
	}
	return result;
}

$(function(){
	PlatUtil.initUIComp("#datatableAttachTab");
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
