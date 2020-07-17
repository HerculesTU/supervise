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
		<td><plattag:input name="FIELD_NAME_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="false" placeholder="请输入" value="${DATA.FIELD_NAME}" comp_col_num="0"
				datarule="required;">
			</plattag:input>
	    </td>
		<td><plattag:input name="FIELD_COMMENT_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="false" placeholder="请输入列中文名称" value="${DATA.FIELD_COMMENT}" comp_col_num="0"
				datarule="required;">
			</plattag:input>
	    </td>
	    <td><plattag:input name="FIELD_WIDTH_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="false" placeholder="请输入列宽度" value="${DATA.FIELD_WIDTH}" comp_col_num="0"
				datarule="required;">
			</plattag:input>
	    </td>
		<td>
			<plattag:radio name="FIELD_ISHIDE_${DATA.TR_INDEX}" static_values="否:-1,是:1"
			value="${DATA.FIELD_ISHIDE}" 
			auth_type="write" select_first="true" allowblank="false" is_horizontal="true" comp_col_num="0">
			</plattag:radio>
		</td>
		<td>
			<plattag:radio name="FIELD_SORT_${DATA.TR_INDEX}" static_values="不支持:-1,支持:1"
			value="${DATA.FIELD_SORT}" 
			auth_type="write" select_first="true" allowblank="false" is_horizontal="true" comp_col_num="0">
			</plattag:radio>
		</td>
		<td>
			<plattag:radio name="FIELD_DATAJOIN_${DATA.TR_INDEX}" static_values="否:-1,是:1"
			value="${DATA.FIELD_DATAJOIN}" 
			auth_type="write" select_first="true" allowblank="false" is_horizontal="true" comp_col_num="0">
			</plattag:radio>
		</td>
		<td><plattag:input name="FIELD_ORDERNAME_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="true" placeholder="请输入排序命名" value="${DATA.FIELD_ORDERNAME}" comp_col_num="0">
			</plattag:input>
	    </td>
		<td>
		    <input type="hidden" name="FORMAT_FN_${DATA.TR_INDEX}" value="${DATA.FORMAT_FN}">
		    <button type="button" onclick="setColumnFormatJs('FORMAT_FN_${DATA.TR_INDEX}');"
				class="btn btn-outline btn-info btn-sm">
				<i class="fa fa-pencil"></i>&nbsp;设置格式化脚本
			</button>
		</td>
	</tr>
</c:forEach>


