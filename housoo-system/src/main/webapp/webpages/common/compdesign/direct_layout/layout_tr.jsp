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
			<plattag:select placeholder="请选择布局类型" value="${DATA.LAYOUT_TYPE}" 
			static_values="中部:center,东部:east,西部:west,南部:south,北部:north"
			istree="false" allowblank="false" comp_col_num="0" auth_type="write" name="LAYOUT_TYPE_${DATA.TR_INDEX}">
			</plattag:select>
	    </td>
		<td>
		    <plattag:radio name="SUPPORT_YSCROLL_${DATA.TR_INDEX}" static_values="不支持:-1,支持:1"
			value="${DATA.SUPPORT_YSCROLL}" 
			auth_type="write" select_first="true" allowblank="false" is_horizontal="true" comp_col_num="0">
			</plattag:radio>
	    </td>
	    <td><plattag:input name="LAYOUT_SIZE_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="true" placeholder="请输入布局大小" value="${DATA.LAYOUT_SIZE}" comp_col_num="0"
				datarule="positiveInteger;">
			</plattag:input>
	    </td>
		<td>
			<plattag:input name="LAYOUT_ID_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="true" placeholder="请输入布局ID" value="${DATA.LAYOUT_ID}" comp_col_num="0">
			</plattag:input>
		</td>
	</tr>
</c:forEach>


