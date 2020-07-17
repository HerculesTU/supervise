<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<c:forEach items="${EDIT_DATAS}" var="DATA" >
   <tr id="${TABLE_ID}_${DATA.TR_INDEX}">
		<td>${DATA.TR_INDEX}</td>
		<td><input type="checkbox" class="edittable_checkbox"
			name="edittable_checkbox" value="${DATA.TR_INDEX}"></td>
		<td>
		    <input type="hidden" name="TABLE_COMMNETS_${DATA.TR_INDEX}" value="${DATA.TABLE_COMMNETS}">
		    <plattag:select name="TABLE_NAME_${DATA.TR_INDEX}" auth_type="write" istree="false"
				allowblank="false" placeholder="请输入表名称" comp_col_num="0" 
				value="${DATA.TABLE_NAME}" dyna_interface="dbManagerService.findAllTables"
				dyna_param="" style="width:90%;"
			></plattag:select>
		</td>
		<td>
		    <plattag:input name="PACK_NAME_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="false" placeholder="请输入模块包名" value="${DATA.PACK_NAME}" comp_col_num="0"
				datarule="required;">
			</plattag:input>
		</td>
		<td>
		    <plattag:input name="CLASS_NAME_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="false" placeholder="请输入实体类名" value="${DATA.CLASS_NAME}" comp_col_num="0"
				datarule="required;">
			</plattag:input>
		</td>
		<td>
		    <plattag:input name="CN_NAME_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="false" placeholder="请输入中文名" value="${DATA.CN_NAME}" comp_col_num="0"
				datarule="required;">
			</plattag:input>
		</td>
		<td>
		    <plattag:input name="TABLE_ALIAS_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="false" placeholder="请输入表别名" value="${DATA.TABLE_ALIAS}" comp_col_num="0"
				datarule="required;">
			</plattag:input>
		</td>
		<td>
			<plattag:radio name="GEN_CODE_${DATA.TR_INDEX}" auth_type="write" select_first="true" 
			value="${DATA.GEN_CODE}"
			allowblank="false" is_horizontal="true" comp_col_num="0" static_values="是:1,否:-1"></plattag:radio>
		</td>
	</tr>
</c:forEach>
<script type="text/javascript">
$(function(){
	$("select[name^='TABLE_NAME']").change(function() {
		var tableName = $(this).val();
		if(tableName){
			var selectName = $(this).attr("name");
			var comments = PlatUtil.getSelectAttValue(selectName,"label");
			var trIndex = selectName.substring(selectName.lastIndexOf("_")+1,selectName.length);
			var className = tableName.substring(tableName.lastIndexOf("_")+1,tableName.length);
			var aliasName = tableName.substring(tableName.length-1,tableName.length);
			var packageName = tableName.substring(tableName.indexOf("_")+1,tableName.lastIndexOf("_"));
			$("input[name='PACK_NAME_"+trIndex+"']").val(packageName.toLowerCase());
			$("input[name='CLASS_NAME_"+trIndex+"']").val(PlatUtil.firstLetterUpperCase(className,true));
			$("input[name='TABLE_ALIAS_"+trIndex+"']").val(aliasName);
			$("input[name='TABLE_COMMNETS_"+trIndex+"']").val(comments);
		}
	});
});

</script>
