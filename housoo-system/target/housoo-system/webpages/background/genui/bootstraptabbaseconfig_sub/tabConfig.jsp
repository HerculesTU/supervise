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
          	    <input type="hidden" name="SUBTAB_CLICKCONTENT_${DATA.NAME_INDEX}" unsubmit="true" value="${DATA.SUBTAB_CLICKCONTENT}">
		</td>
		   <td>
		           <plattag:input name="SUBTAB_ID_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="false" placeholder="请输入页签ID" value="${DATA.SUBTAB_ID}" comp_col_num="0"
						datarule="required;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:input name="SUBTAB_NAME_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="false" placeholder="请输入页签名称" value="${DATA.SUBTAB_NAME}" comp_col_num="0"
						datarule="required;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:radio name="SUB_RESIZEHEIGHT_${DATA.NAME_INDEX}" auth_type="write" 
						allowblank="true" is_horizontal="true" comp_col_num="0" attach_props="unsubmit='true'"
						value="${DATA.SUB_RESIZEHEIGHT}" dyna_interface=""
						static_values="是:1,否:-1" select_first="true"
						dyna_param="" 
				   ></plattag:radio>
		   </td>
		   <td>
		           <plattag:input name="SUBTAB_CLICKFN_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="true" placeholder="页签点击事件名称" value="${DATA.SUBTAB_CLICKFN}" comp_col_num="0"
						datarule="" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <button type="button" onclick="clickButtonFn('SUBTAB_CLICKCONTENT_${DATA.NAME_INDEX}');"
						class="btn btn-outline btn-info btn-sm">
						页签点击函数实现
				    </button>
		   </td>
	</tr>
</c:forEach>
<script type="text/javascript">
  
</script>
