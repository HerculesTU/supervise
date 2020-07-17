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
		    <input type="hidden" name="SUB_ALIASNAME_${DATA.TR_INDEX}" value="${DATA.SUB_ALIASNAME}">
		    <plattag:select name="JOIN_TYPE_${DATA.TR_INDEX}" auth_type="write" istree="false"
				allowblank="false" placeholder="请选择连接类型" comp_col_num="0"
				value="${DATA.JOIN_TYPE}" static_values="左连:left join,右连:right join,内连:inner join" style="width:90%;"
			></plattag:select>
		</td>
		<td>
		    <plattag:select name="SUB_TABLENAME_${DATA.TR_INDEX}" auth_type="write" istree="false" value="${DATA.SUB_TABLENAME}"
		    allowblank="false" placeholder="请选择子表" comp_col_num="0" style="width:90%;"
		    dyna_interface="designService.findAssoicalTables" dyna_param="${POST_PARAMS.FORMCONTROL_DESIGN_ID}"
		    >
		    </plattag:select>
		</td>
		<td>
		    <plattag:select name="SUB_ON_FIELDNAME_${DATA.TR_INDEX}" auth_type="write" istree="false" value="${DATA.SUB_ON_FIELDNAME}"
		    allowblank="false" placeholder="请选择连接字段" comp_col_num="0" style="width:90%;"
		    dyna_interface="dbManagerService.findTableColumns" dyna_param="${DATA.SUB_TABLENAME}"
		    >
		    </plattag:select>
		</td>
		<td>
		    <plattag:select name="MATCH_ON_FIELDNAME_${DATA.TR_INDEX}" auth_type="write" istree="true"
				allowblank="false" placeholder="请选择外部匹配字段" comp_col_num="0" onlyselectleaf="true"
				value="${DATA.MATCH_ON_FIELDNAME}" dyna_interface="designService.findSelectFields"
				dyna_param="${POST_PARAMS.FORMCONTROL_DESIGN_ID}" style="width:90%;"
			></plattag:select>
		</td>
		<td>
		    <plattag:select name="SUB_QUERYFIELDS_${DATA.TR_INDEX}" auth_type="write" istree="false" value="${DATA.SUB_QUERYFIELDS}"
		    allowblank="false" placeholder="请选择子表查询字段" comp_col_num="0" style="width:90%;" multiple="multiple"
		    dyna_interface="dbManagerService.findTableColumns" dyna_param="${DATA.SUB_QUERYFIELDS}"
		    >
		    </plattag:select>
		</td>
	</tr>
</c:forEach>
<script type="text/javascript">
$(function(){
	$("select[name^='SUB_TABLENAME']").change(function() {
		var tableName = $(this).val();
		if(tableName){
			var SUB_TABLENAME = $(this).attr("name");
			var tableAlias = PlatUtil.getSelectAttValue(SUB_TABLENAME,"table_alias");
			var trIndex = SUB_TABLENAME.substring(SUB_TABLENAME.lastIndexOf("_")+1,SUB_TABLENAME.length);
			var subOnFieldName = "SUB_ON_FIELDNAME_"+trIndex;
			$("input[name='SUB_ALIASNAME_"+trIndex+"']").val(subOnFieldName);
			PlatUtil.reloadSelect(subOnFieldName,{
				dyna_param:tableName
			});
			PlatUtil.reloadSelect("SUB_QUERYFIELDS_"+trIndex,{
				dyna_param:tableName
			});
		}
	});
});

</script>
