<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.SysConstants"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatBeanUtil"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatPropUtil"%>
<%@ page language="java" import="org.apache.commons.lang3.StringUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
    Map<String, Object> selecttableconfig = PlatBeanUtil
            .getMapFromRequest(request);
    String showcolnames= request.getParameter("showcolnames");
    String selectedRecordIds = request.getParameter("selectedRecordIds");
    List<String> showcolList = Arrays.asList(showcolnames.split(","));
    request.setAttribute("showcolList", showcolList);
    request.setAttribute("selecttableconfig", selecttableconfig);
    request.setAttribute("selectedRecordIds", selectedRecordIds);
%>

<div class="panel-Title">
	<h5>已选记录列表(<span id="${selecttableconfig.tableid}_count" style="color: red;">3</span>条)
	<button type="button" style="position: absolute;right: 5px;"
	class="btn btn-outline btn-danger btn-xs" onclick="removeSelectedTableRecord('${selecttableconfig.tableid}');" ><i class="fa fa-remove"></i>
	&nbsp;移除所选</button>
	</h5>
</div>
<div style="position:absolute; top:32px; left:0px; right:0px; bottom:0px;">
<table class="table table-hover" id="${selecttableconfig.tableid}" 
    checkvaluecol="${selecttableconfig.checkvaluecol}" checklabelcol="${selecttableconfig.checklabelcol}"
    showcolcodes="${selecttableconfig.showcolcodes}" dynainterface="${selecttableconfig.dynainterface }" >
      <tbody id="${selecttableconfig.tableid}_tbody">
          <tr class="active">
             <td><input type="checkbox" name="${selecttableconfig.tableid}_ALLCHECKBOX"  /></td>
             <c:forEach items="${showcolList}" var="showcol">
             <td>${showcol}</td>
             </c:forEach>
          </tr>
      </tbody>
  </table>
</div>
<script type="text/javascript">
function removeSelectedTableRecord(tableid){
	$("#"+tableid+" input[value]:checkbox:checked").each(function(){ 
	    var checkValue = $(this).val();
	    $("#"+checkValue).remove();
	});
	var selectedCount = $("#"+tableid+" input[value]:checkbox").length;
	$("#"+tableid+"_count").text(selectedCount);
}

$(function(){
	//获取初始化配置
	var selectedRecordIds = "${selectedRecordIds}";
    var selectorConfig = PlatUtil.getData(PlatUtil.WIN_SELECTOR_CONFIG); 
	 if(selectorConfig&&selectorConfig.needCheckIds){
		  selectedRecordIds = selectorConfig.needCheckIds;
	 }
	 PlatUtil.loadSelectedTable({
		tableid:"${selecttableconfig.tableid}",
		selectedRecordIds:selectedRecordIds
	});
	var tableid = "${selecttableconfig.tableid}";
	$("input[name='"+tableid+"_ALLCHECKBOX']").change(function() {
		var isChecked = $(this).is(":checked");
		if(isChecked){
			$("#"+tableid).find("[type='checkbox'][value]").prop("checked",true);
		}else{
			$("#"+tableid).find("[type='checkbox'][value]").prop("checked",false);
		}
	});
});
</script>
