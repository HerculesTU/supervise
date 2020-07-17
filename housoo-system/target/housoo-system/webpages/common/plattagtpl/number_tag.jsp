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
	<input type="text" class="form-control platnumber"  name="${name}" auth_type="${auth_type}" 
	     step="${step}" 
	     <#if label_value?? >label_value="${label_value}"</#if>
	     <#if id?? >id="${id}"</#if>  
	     <#if value?? >value="${value}"</#if>
	     <#if decimals?? >decimals="${decimals}"</#if>  
	     <#if prefix?? >prefix="${prefix}"</#if>
	     <#if postfix?? >postfix="${postfix}"</#if>
	     <#if placeholder?? >placeholder="${placeholder}"</#if> 
	     <#if style?? >style="${style}"</#if>  
	     <#if attach_props?? >${attach_props}</#if>
	     <#if auth_type=='readonly' >readonly="readonly"</#if>
	     <#if allowblank=='false' >
	     data-rule="required;" datarule="required;"
	     </#if> 
	     <#if max?? >max="${max}"</#if>
	     <#if min?? >min="${min}"</#if>
    >
<#if comp_col_num!='0' >
</div>
</#if>
