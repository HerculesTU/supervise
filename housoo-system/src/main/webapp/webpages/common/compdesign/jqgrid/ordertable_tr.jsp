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
			<plattag:select name="ORDER_FIELD_${DATA.TR_INDEX}" auth_type="write" istree="true"
				allowblank="false" placeholder="请选择排序字段" comp_col_num="0" onlyselectleaf="true"
				value="${DATA.ORDER_FIELD}" dyna_interface="designService.findSelectFields"
				dyna_param="${POST_PARAMS.DESIGN_ID}" style="width:90%;"
		    ></plattag:select>
	    </td>
		<td>
		    <plattag:radio name="ORDER_TYPE_${DATA.TR_INDEX}" auth_type="write" static_values="降序:DESC,升序:ASC"
		     select_first="true" allowblank="false" is_horizontal="true" value="${DATA.ORDER_TYPE}" comp_col_num="0">
		     </plattag:radio>
	    </td>
	</tr>
</c:forEach>


