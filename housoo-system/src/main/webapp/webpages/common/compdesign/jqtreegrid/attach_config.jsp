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
	Map<String,Object> formControl = (Map<String,Object>)request.getAttribute("formControl"); 
    Map<String,Object> fieldInfo = (Map<String,Object>)request.getAttribute("fieldInfo");
    fieldInfo.put("FORMCONTROL_ID", formControl.get("FORMCONTROL_ID"));
    SqlFilter filter = new SqlFilter(request);
    CommonUIService commonUIService = (CommonUIService)PlatAppUtil.getBean("commonUIService");
    commonUIService.saveJqTreeTableBaseConfig(filter, fieldInfo);
%>

<div class="tabs-container" id="datatableAttachTab">
	<ul class="nav nav-tabs">
		<li class="active" ><a data-toggle="tab" href="#tab-1"
			aria-expanded="true"><i class="fa fa-bars"></i>表格列配置</a></li>
		<li class=""  ><a data-toggle="tab" href="#tab-2"
			aria-expanded="false"><i class="fa fa-search"></i>查询条件配置</a></li>
		<li class=""><a data-toggle="tab" href="#tab-3"
			aria-expanded="false"><i class="fa fa-cog"></i>操作按钮配置</a></li>
	</ul>
	<div class="tab-content">
		<div id="tab-1" class="tab-pane active">
		     <jsp:include page="/webpages/common/compdesign/jqgrid/column_form.jsp"></jsp:include>
		</div>
		<div id="tab-2" class="tab-pane" >
		     <jsp:include page="/webpages/common/compdesign/jqgrid/searchfield_form.jsp"></jsp:include>
		</div>
		<div id="tab-3" class="tab-pane" >
		     <jsp:include page="/webpages/common/compdesign/jqgrid/tablebutton_grid.jsp"></jsp:include>
		</div>
	</div>
</div>

<script type="text/javascript">

function saveJqTreeAttachConfig(){
	if(PlatUtil.validBootstrapTab("datatableAttachTab")){
		var COLUMN_JSON = PlatUtil.getEditTableAllRecordJson("columnTable");
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
			COLUMN_JSON:COLUMN_JSON,
			QUERYCONDITIONIDS:QUERYCONDITIONIDS,
			TABLEBUTTON_IDS:TABLEBUTTON_IDS
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
