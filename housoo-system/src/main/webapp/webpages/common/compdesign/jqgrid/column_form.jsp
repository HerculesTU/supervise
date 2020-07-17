<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<form method="post" class="form-horizontal" id="ColumnForm">
    <div class="titlePanel">
        <div class="title-search form-horizontal" id="column_search">
              <input type="hidden" name="FORMCONTROL_ID" value="${formControl.FORMCONTROL_ID}">
        </div>
        <div class="toolbar">
			<button type="button" onclick="addColumn();"
				class="btn btn-outline btn-primary btn-sm">
				<i class="fa fa-plus"></i>&nbsp;新增
			</button>
			<button type="button"
				onclick="PlatUtil.removeEditTableRows('columnTable');"
				class="btn btn-outline btn-danger btn-sm">
				<i class="fa fa-trash-o"></i>&nbsp;删除
			</button>
		</div>
    </div>
    <div class="gridPanel">
        <plattag:edittable dyna_interface="commonUIService.findDatatableColumns" 
          tr_tplpath="common/compdesign/jqgrid/columntable_tr" id="columnTable" dragable="true"
          col_style="[15%,列英文名称],[10%,列中文名称],[10%,列宽度],[10%,是否隐藏],[15%,支持排序],[10%,数据合并],[15%,排序命名],[15%,格式化脚本]" 
          searchpanel_id="column_search"
        >
        </plattag:edittable>
    </div>
</form>

<script type="text/javascript">

function addColumn(){
	PlatUtil.addEditTableRow({
		tableId:"columnTable"
	});
}

function setColumnFormatJs(columnName){
	var expcodePath = "jqgrid/formatexpjs.js";
	var oldConfigData = $("input[name='"+columnName+"']").val();
	if(oldConfigData&&oldConfigData!=""){
		PlatUtil.setData(columnName,oldConfigData);
	}
	var url = "appmodel/FormControlController.do?goExpCodeView&allowedit=true&expcodePath="+expcodePath;
	url+="&keyName="+columnName;
	PlatUtil.openWindow({
		title:"格式化函数实现",
		area: ["1200px","600px"],
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