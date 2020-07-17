<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.DesignService"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
    Map<String,Object> formControl = (Map<String,Object>)request.getAttribute("formControl"); 
    DesignService designService = (DesignService)PlatAppUtil.getBean(DesignService.class);
    List<Map> tables = designService.findAssoicalTables(formControl.get("FORMCONTROL_DESIGN_ID").toString());
    StringBuffer assoicalTables = new StringBuffer("");
    StringBuffer tableNames = new StringBuffer("");
    if(tables!=null){
        for(int i=0;i<tables.size();i++){
    Map<String,Object> table = tables.get(i);
    if(i>0){
        assoicalTables.append(",");
        tableNames.append(",");
    }
    tableNames.append(table.get("VALUE"));
    assoicalTables.append("【").append(table.get("VALUE")).append(",别名:");
    assoicalTables.append(table.get("ALIAS")).append("】");
        }
    }
    request.setAttribute("ASSOCIAL_TABLENAMES", tableNames.toString());
    request.setAttribute("assoicalTables", assoicalTables.toString());
%>

<form method="post" class="form-horizontal" id="BaseConfigForm">
	<plattag:alertdivtag color="info" content="配置的SQL的时候表必须使用别名,本设计的涉及的表有${assoicalTables}"
	 title="注意事项!">
	</plattag:alertdivtag>
	<div class="form-group plat-form-title">
		<span class="plat-current"> 基本信息 </span>
	</div>
	<div class="form-group">
	    <input type="hidden" name="ASSOCIAL_TABLENAMES" value="${ASSOCIAL_TABLENAMES}">
		<plattag:input name="CONTROL_ID" auth_type="write" allowblank="false" datarule="required;"
			placeholder="请输入表格ID" comp_col_num="3" label_col_num="1" value="${fieldInfo.CONTROL_ID}"
			maxlength="30" label_value="表格ID" >
		</plattag:input>
		<plattag:radio name="IS_PAGE" auth_type="write" 
		select_first="true" allowblank="false" is_horizontal="true" comp_col_num="3"
		label_col_num="1" label_value="是否分页" static_values="分页:1,不分页:-1" value="${fieldInfo.IS_PAGE}"
		></plattag:radio>
		<plattag:radio name="DATA_TYPE" auth_type="write" 
		select_first="true" allowblank="false" is_horizontal="true" comp_col_num="3"
		label_col_num="1" label_value="数据源" static_values="SQL语句:1,JAVA接口:2" value="${fieldInfo.DATA_TYPE}"
		></plattag:radio>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
		<plattag:radio name="ALLOW_DRAG" auth_type="write" 
		select_first="true" allowblank="false" is_horizontal="true" comp_col_num="3"
		label_col_num="1" label_value="拖拽排序" static_values="不支持:-1,支持:1" value="${fieldInfo.ALLOW_DRAG}"
		></plattag:radio>
		<plattag:input name="TABLE_TITLE" auth_type="write" allowblank="true"
			placeholder="请输入表格标题" comp_col_num="3" label_col_num="1" value="${fieldInfo.TABLE_TITLE}"
			maxlength="30" label_value="表格标题" >
		</plattag:input>
		<plattag:radio name="IS_INITCOL" auth_type="write" select_first="true"
	     allowblank="false" is_horizontal="true" comp_col_num="3" 
	     label_col_num="1" label_value="初始化列" static_values="否:-1,是:1"
	     ></plattag:radio>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
		<plattag:input name="TABLE_HEIGHT" auth_type="write" allowblank="true" datarule="positiveInteger;"
			placeholder="请输入表格高度" comp_col_num="3" label_col_num="1" value="${fieldInfo.TABLE_HEIGHT}"
			maxlength="30" label_value="表格高度" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	     <plattag:textarea name="SQL_CONTENT" auth_type="${(fieldInfo.DATA_TYPE==null||fieldInfo.DATA_TYPE=='1')?'write':'hidden'}" 
	    allowblank="${(fieldInfo.DATA_TYPE==null||fieldInfo.DATA_TYPE=='1')?'false':'true'}" style="height:100px;"
	    placeholder="请输入SQL语句" comp_col_num="11" label_col_num="1" 
	    maxlength="2000" label_value="SQL语句" value="${fieldInfo.SQL_CONTENT}"
	    ></plattag:textarea>
	    <plattag:input name="JAVA_INTERCODE" auth_type="${fieldInfo.DATA_TYPE=='2'?'write':'hidden'}"
	        allowblank="${fieldInfo.DATA_TYPE=='2'?'false':'true'}" datarule="${fieldInfo.DATA_TYPE=='2'?'required;':''}"
			placeholder="请输入JAVA接口代码,例如:uiCompService.findBySqlFilter" comp_col_num="11" label_col_num="1" value="${fieldInfo.JAVA_INTERCODE}"
			maxlength="100" label_value="JAVA接口" >
		</plattag:input>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:textarea name="JSDBCLICK_FN" auth_type="write" 
	    allowblank="true" buttonconfigs="[查看例子,queryDbClickFnExp]"
	    placeholder="请输入双击行函数" comp_col_num="11" label_col_num="1"
	    maxlength="500" label_value="双击函数实现" value="${fieldInfo.JSDBCLICK_FN}"
	    ></plattag:textarea>
	</div>
	<div class="hr-line-dashed"></div>
	<div class="form-group">
	    <plattag:textarea name="GRIDCOMPLETE_FN" auth_type="write" 
	    allowblank="true" 
	    placeholder="请输入加载完成回调实现" comp_col_num="11" label_col_num="1"
	    maxlength="500" label_value="加载完回调函数实现" value="${fieldInfo.GRIDCOMPLETE_FN}"
	    ></plattag:textarea>
	</div>
</form>

<script type="text/javascript">

function queryDbClickFnExp(){
	var expcodePath = "jqgrid/querydbclickexp.js";
	PlatUtil.openWindow({
		title:"双击功能实现例子",
		area: ["600px","400px"],
		content: "appmodel/FormControlController.do?goExpCodeView&expcodePath="+expcodePath,
		end:function(){
		}
	});
}

function showSqlConfigWin(){
	var FORMCONTROL_DESIGN_ID = $("#FormControlForm input[name='FORMCONTROL_DESIGN_ID']").val();
	PlatUtil.openWindow({
		title:"SQL语句构建器",
		area: ["90%","600px"],
		content: "appmodel/UiCompController.do?goSqlConfigForm&FORMCONTROL_DESIGN_ID="+FORMCONTROL_DESIGN_ID,
		end:function(){
		  if(PlatUtil.isSubmitSuccess()){
			  
		  }
		}
	});
}

$(function(){
	$("input[name='DATA_TYPE']").change(function() {
		var DATA_TYPE = PlatUtil.getCheckRadioTagValue("DATA_TYPE","value");
		if(DATA_TYPE=="1"){
			PlatUtil.changeUICompAuth("write","SQL_CONTENT","-1");
			PlatUtil.changeUICompAuth("hidden","JAVA_INTERCODE");
		}else if(DATA_TYPE=="2"){
			PlatUtil.changeUICompAuth("hidden","SQL_CONTENT");
			PlatUtil.changeUICompAuth("write","JAVA_INTERCODE","-1");
		}
	});
	
});
</script>

