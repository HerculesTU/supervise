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
          	    <input type="hidden" name="oldColumnName_${DATA.NAME_INDEX}" unsubmit="true" value="${DATA.oldColumnName}">
          	    <input type="hidden" name="isNewColumn_${DATA.NAME_INDEX}" unsubmit="true" value="${DATA.isNewColumn}">
		</td>
		   <td>
		           <plattag:input name="columnName_${DATA.NAME_INDEX}"  maxlength="30"
		                auth_type="write" 
						allowblank="false" placeholder="请输入列名称" value="${DATA.columnName}" comp_col_num="0"
						datarule="required;onlyLetterNumberUnderLine;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:input name="columnComments_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="false" placeholder="请输入列注释" value="${DATA.columnComments}" comp_col_num="0"
						datarule="required;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:select name="columnType_${DATA.NAME_INDEX}" auth_type="write" istree="false"
						allowblank="false" placeholder="请选择列类型" comp_col_num="0" attach_props="unsubmit='true'"
						value="${DATA.columnType}" dyna_interface="dictionaryService.findList"
						static_values="" onlyselectleaf="false"
						dyna_param="{TYPE_CODE:'COLUMN_TYPE',ORDER_TYPE:'ASC'}" style="width:90%;"
				   ></plattag:select>
		   </td>
		   <td>
		           <plattag:input name="dataLength_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="true" placeholder="请输入数据长度" value="${DATA.dataLength}" comp_col_num="0"
						datarule="positiveInteger;" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:radio name="isNullable_${DATA.NAME_INDEX}" auth_type="write" 
						allowblank="true" is_horizontal="true" comp_col_num="0" attach_props="unsubmit='true'"
						value="${DATA.isNullable}" dyna_interface=""
						static_values="允许:1,不允许:-1" select_first="true"
						dyna_param="" 
				   ></plattag:radio>
		   </td>
		   <td>
		           <plattag:input name="defaultValue_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="true" placeholder="请输入默认值" value="${DATA.defaultValue}" comp_col_num="0"
						datarule="" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
		   <td>
		           <plattag:radio name="isPrimaryKey_${DATA.NAME_INDEX}" auth_type="write" 
						allowblank="true" is_horizontal="true" comp_col_num="0" attach_props="unsubmit='true'"
						value="${DATA.isPrimaryKey}" dyna_interface=""
						static_values="否:-1,是:1" select_first="true"
						dyna_param="" 
				   ></plattag:radio>
		   </td>
		   <td>
		           <plattag:radio name="isUnique_${DATA.NAME_INDEX}" auth_type="write" 
						allowblank="true" is_horizontal="true" comp_col_num="0" attach_props="unsubmit='true'"
						value="${DATA.isUnique}" dyna_interface=""
						static_values="否:-1,是:1" select_first="true"
						dyna_param="" 
				   ></plattag:radio>
		   </td>
		   <td>
		           <plattag:input name="scale_${DATA.NAME_INDEX}"  maxlength=""
		                auth_type="write" 
						allowblank="true" placeholder="小数点数" value="${DATA.scale}" comp_col_num="0"
						datarule="" attach_props="unsubmit='true'" >
					</plattag:input>
		   </td>
	</tr>
</c:forEach>
<script type="text/javascript">

</script>
