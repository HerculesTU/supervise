<#if label_value?? >
<label class="col-sm-${label_col_num} control-label" id="SELECT_${name}_LABEL"
   <#if auth_type=='hidden' >style="display: none;"</#if>
>
  <font class="redDot" <#if allowblank=='true' >style="display: none;"</#if> 
  >*</font>
  ${label_value!''}：
</label>
</#if>
<#if comp_col_num!='0' >
<div class="col-sm-${comp_col_num}" id="SELECT_${name}_DIV" 
   <#if auth_type=='hidden' >style="display: none;"</#if>
>
</#if>
	<select class="form-control select2" name="${name}" istree="${istree}" auth_type="${auth_type}"
	    <#if label_value?? >label_value="${label_value}"</#if>
	    <#if id?? >id="${id}"</#if>  
		<#if multiple?? >multiple="multiple" multiselect="true"</#if>
		<#if allowblank=='false' >data-rule="required;" datarule="required;"</#if>
		<#if max_select_num?? >max_select_num="${max_select_num}"</#if>
		<#if placeholder?? >placeholder="${placeholder}"</#if>
		<#if static_values?? >static_values="${static_values}"</#if>
		<#if dyna_interface?? >dyna_interface="${dyna_interface}"</#if>
		<#if dyna_param?? >dyna_param="${dyna_param}"</#if>
		<#if style?? >style="${style}"</#if>
		<#if attach_props?? >${attach_props}</#if>
		<#if auth_type=='readonly' >disabled="disabled"</#if>
	>
	    <#if !multiple?? >
	     <option disabled selected value></option>
	     </#if>
	    <#list optionlist as option>
		   ${option}
		</#list>
	</select>
<#if comp_col_num!='0' >
</div>
</#if>
