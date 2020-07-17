<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<c:forEach items="${EDIT_DATAS}" var="DATA" >
   <tr id="${TABLE_ID}_${DATA.NAME_INDEX}">
		<td>${DATA.TR_INDEX}</td>
		<td><input type="checkbox" class="edittable_checkbox" unsubmit="true"
			name="edittable_checkbox" value="${DATA.NAME_INDEX}">
		</td>
		   <td>
		           <plattag:input name="DICATTACH_NAME_${DATA.NAME_INDEX}"  maxlength="60"
		                auth_type="write" 
						allowblank="false" placeholder="请输入附加属性名" value="${DATA.DICATTACH_NAME}" comp_col_num="0"
						datarule="required;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:input name="DICATTACH_KEY_${DATA.NAME_INDEX}"  maxlength="30"
		                auth_type="write" 
						allowblank="false" placeholder="请输入附加属性KEY" value="${DATA.DICATTACH_KEY}" comp_col_num="0"
						datarule="required;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:input name="DICATTACH_VALUE_${DATA.NAME_INDEX}"  maxlength="256"
		                auth_type="write" 
						allowblank="false" placeholder="请输入附加属性值" value="${DATA.DICATTACH_VALUE}" comp_col_num="0"
						datarule="required;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
	</tr>
</c:forEach>
<script type="text/javascript">

</script>
