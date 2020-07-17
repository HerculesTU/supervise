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
		           <plattag:input name="COLUMN_NUM_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="false" placeholder="" value="${DATA.COLUMN_NUM}" comp_col_num="0"
						datarule="required;positiveInteger;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:input name="COL_NAME_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="false" placeholder="请输入列中文名" value="${DATA.COL_NAME}" comp_col_num="0"
						datarule="required;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:input name="FIELD_NAME_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="false" placeholder="请输入关联字段名" value="${DATA.FIELD_NAME}" comp_col_num="0"
						datarule="required;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:radio name="ALLOW_BLANK_${DATA.NAME_INDEX}" auth_type="write" 
						allowblank="true" is_horizontal="true" comp_col_num="0" attach_props="unsubmit='true'"
						value="${DATA.ALLOW_BLANK}" dyna_interface=""
						static_values="否:false,是:true" select_first="true"
						dyna_param="" 
				   ></plattag:radio>
		   </td>
		   <td>
		           <plattag:select name="VALID_RULE_${DATA.NAME_INDEX}" auth_type="write" istree="false"
						allowblank="true" placeholder="请选择验证规则" comp_col_num="0" attach_props="unsubmit='true'"
						value="${DATA.VALID_RULE}" dyna_interface="dictionaryService.findList"
						static_values="" onlyselectleaf="false"
						dyna_param="{TYPE_CODE:'javaValidRule',ORDER_TYPE:'ASC'}" style="width:90%;"
				   ></plattag:select>
		   </td>
		   <td>
		           <plattag:input name="VALID_INTER_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="true" placeholder="自定义验证接口" value="${DATA.VALID_INTER}" comp_col_num="0"
						datarule="" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:radio name="IS_HIDE_${DATA.NAME_INDEX}" auth_type="write" 
						allowblank="true" is_horizontal="true" comp_col_num="0" attach_props="unsubmit='true'"
						value="${DATA.IS_HIDE}" dyna_interface=""
						static_values="否:false,是:true" select_first="true"
						dyna_param="" 
				   ></plattag:radio>
		   </td>
		   <td>
		           <plattag:input name="HIDE_VALUE_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="true" placeholder="请输入隐藏列缺省值" value="${DATA.HIDE_VALUE}" comp_col_num="0"
						datarule="" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:input name="TRANS_INTER_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="true" placeholder="请输入值转换接口" value="${DATA.TRANS_INTER}" comp_col_num="0"
						datarule="" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
	</tr>
</c:forEach>
<script type="text/javascript">

</script>
