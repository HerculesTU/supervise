<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<c:forEach items="${r'${EDIT_DATAS}'}" var="DATA" >
   <tr id="${r'${TABLE_ID}'}_${r'${DATA.NAME_INDEX}'}">
		<td>${r'${DATA.TR_INDEX}'}</td>
		<td><input type="checkbox" class="edittable_checkbox" unsubmit="true"
			name="edittable_checkbox" value="${r'${DATA.NAME_INDEX}'}">
		  <#list tableCols as col>
          	 <#if col.TABLECOL_COMPTYPE=='hidden' >
          	    <input type="hidden" name="${col.TABLECOL_FIELDNAME}_${r'${DATA.NAME_INDEX}'}" unsubmit="true" value="${col.FIELD_DATANAME}">
             </#if>
	      </#list>	
		</td>
		<#list tableCols as col>
		   <#if col.TABLECOL_COMPTYPE!='hidden' >
		   <td>
		        <#if col.TABLECOL_COMPTYPE=='input' >
		           <plattag:input name="${col.TABLECOL_FIELDNAME}_${r'${DATA.NAME_INDEX}'}"  maxlength="${col.TABLECOL_MAXLENGTH!''}"
		                auth_type="${(col.TABLECOL_READONLY??&&col.TABLECOL_READONLY=='true')?string('readonly', 'write')}" 
						allowblank="${col.TABLECOL_ALLOWBLANK}" placeholder="${col.TABLECOL_PLACEHOLD!''}" value="${col.FIELD_DATANAME}" comp_col_num="0"
						datarule="${col.TABLECOL_VALIDRULES!''}" attach_props="unsubmit='true'" >
					</plattag:input>
		        </#if>
		        <#if col.TABLECOL_COMPTYPE=='select' >
		           <plattag:select name="${col.TABLECOL_FIELDNAME}_${r'${DATA.NAME_INDEX}'}" auth_type="write" istree="${col.TABLECOL_ISTREE}"
						allowblank="${col.TABLECOL_ALLOWBLANK}" placeholder="${col.TABLECOL_PLACEHOLD!''}" comp_col_num="0" attach_props="unsubmit='true'"
						value="${col.FIELD_DATANAME}" dyna_interface="${col.TABLECOL_DYNAINTERFACE!''}"
						<#if col.TABLECOL_MULTIPLE=='true' >
						multiple="multiple" 
						</#if>
						static_values="${col.TABLECOL_STATICVALUES!''}" onlyselectleaf="${col.TABLECOL_ONSELECTLEAF}"
						dyna_param="${col.TABLECOL_DYNAPARAMS!''}" style="width:90%;"
				   ></plattag:select>
		        </#if>
		        <#if col.TABLECOL_COMPTYPE=='radio' >
		           <plattag:radio name="${col.TABLECOL_FIELDNAME}_${r'${DATA.NAME_INDEX}'}" auth_type="write" 
						allowblank="${col.TABLECOL_ALLOWBLANK}" is_horizontal="true" comp_col_num="0" attach_props="unsubmit='true'"
						value="${col.FIELD_DATANAME}" dyna_interface="${col.TABLECOL_DYNAINTERFACE!''}"
						static_values="${col.TABLECOL_STATICVALUES!''}" select_first="${col.TABLECOL_SELECT_FIRST}"
						dyna_param="${col.TABLECOL_DYNAPARAMS!''}" 
				   ></plattag:radio>
		        </#if>
		        <#if col.TABLECOL_COMPTYPE=='checkbox' >
		           <plattag:checkbox name="${col.TABLECOL_FIELDNAME}_${r'${DATA.NAME_INDEX}'}" auth_type="write" 
						allowblank="${col.TABLECOL_ALLOWBLANK}" is_horizontal="true" comp_col_num="0" attach_props="unsubmit='true'"
						value="${col.FIELD_DATANAME}" dyna_interface="${col.TABLECOL_DYNAINTERFACE!''}"
						static_values="${col.TABLECOL_STATICVALUES!''}" select_first="${col.TABLECOL_SELECT_FIRST}"
						dyna_param="${col.TABLECOL_DYNAPARAMS!''}" 
				   ></plattag:checkbox>
		        </#if>
		        <#if col.TABLECOL_COMPTYPE=='button' >
		           <button type="button" onclick="${col.TABLECOL_BTNCLICKFN}('${col.TABLECOL_BTNASSFIELD}_${r'${DATA.NAME_INDEX}'}');"
						class="btn btn-outline btn-info btn-sm">
						${col.TABLECOL_NAME}
				    </button>
		        </#if>
		        <#if col.TABLECOL_COMPTYPE=='winselector' >
		           <plattag:winselector placeholder="${col.TABLECOL_PLACEHOLD!''}" allowblank="${col.TABLECOL_ALLOWBLANK}"
		            height="${col.TABLECOL_HEIGHT}" comp_col_num="0" auth_type="write" width="${col.TABLECOL_WIDTH}" 
		            maxselect="${col.TABLECOL_WINMAX}" minselect="${col.TABLECOL_WINMIN}"
		            selectorurl="${col.TABLECOL_WINURL}" title="${col.TABLECOL_WINTITLE}" name="${col.TABLECOL_FIELDNAME}_${r'${DATA.NAME_INDEX}'}">
		            </plattag:winselector>
		        </#if>
		   </td>
		   </#if>
		</#list>
	</tr>
</c:forEach>
<script type="text/javascript">
${LOADTR_JS!''}
</script>
