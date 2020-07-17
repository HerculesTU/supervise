<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<c:forEach items="${EDIT_DATAS}" var="DATA" >
   <tr id="${TABLE_ID}_${DATA.TR_INDEX}">
		<td>${DATA.TR_INDEX}</td>
		<td><input type="checkbox" class="edittable_checkbox"
			name="edittable_checkbox" value="${DATA.TR_INDEX}">
		    <input type="hidden" name="CLICK_FNTYPE_${DATA.TR_INDEX}" value="${DATA.CLICK_FNTYPE}">	
		</td>
		<td><plattag:input name="CLICK_FNTYPENAME_${DATA.TR_INDEX}" auth_type="readonly" maxlength="30"
				allowblank="false" placeholder="请输入事件类型" value="${DATA.CLICK_FNTYPENAME}" comp_col_num="0"
				datarule="required;">
			</plattag:input>
	    </td>
		<td><plattag:input name="CLICK_FNNAME_${DATA.TR_INDEX}" auth_type="write" maxlength="30"
				allowblank="false" placeholder="请输入事件名称" value="${DATA.CLICK_FNNAME}" comp_col_num="0"
				datarule="required;">
			</plattag:input>
	    </td>
		<td>
		    <input type="hidden" name="CLICK_FN_${DATA.TR_INDEX}" value="${DATA.CLICK_FN}">
		    <button type="button" onclick="setColumnFormatJs('CLICK_FN_${DATA.TR_INDEX}');"
				class="btn btn-outline btn-info btn-sm">
				<i class="fa fa-pencil"></i>&nbsp;设置事件脚本
			</button>
		</td>
	</tr>
</c:forEach>


