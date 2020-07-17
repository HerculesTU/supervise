<#if label_value?? >
<label class="col-sm-${label_col_num} control-label" id="INPUT_${name}_LABEL"
   <#if auth_type=='hidden' >style="display: none;"</#if>
>
  <font class="redDot" <#if allowblank=='true' >style="display: none;"</#if> 
  >*</font>
  ${label_value!''}：
</label>
</#if>
<#if comp_col_num!='0' >
<div class="col-sm-${comp_col_num}" id="INPUT_${name}_DIV"
   <#if auth_type=='hidden' >style="display: none;"</#if>
>
</#if>
	<input type="text" class="wicon form-control" name="${name}" istime="${istime}" 
	     format="${format}" auth_type="${auth_type}" readonly="readonly" posttimefmt="${posttimefmt!''}"
	     <#if label_value?? >label_value="${label_value}"</#if>
	     <#if auth_type=='readonly' >disabled="disabled"</#if>
	     <#if id?? >id="${id}"</#if>  
	     <#if value?? >value="${value}"</#if> 
	     <#if placeholder?? >placeholder="${placeholder}"</#if> 
	     <#if style?? >style="${style}"</#if>  
	     <#if attach_props?? >${attach_props}</#if>
	     <#if allowblank=='false' >data-rule="required;" datarule="required;"</#if>
	     <#if min_date?? >min_date="${min_date}"</#if> 
	     <#if max_date?? >max_date="${max_date}"</#if> 
	     <#if choosefun?? >choosefun="${choosefun}"</#if>
	     <#if start_name?? >start_name="${start_name}"</#if>
	     <#if end_name?? >end_name="${end_name}"</#if>
    >
<#if comp_col_num!='0' >
</div>
</#if>

