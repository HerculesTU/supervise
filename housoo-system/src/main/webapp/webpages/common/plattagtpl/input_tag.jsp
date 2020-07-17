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
    <#if !((ispass??)&&ispass=="true") >
	<input type="text" class="form-control"  name="${name}" auth_type="${auth_type}" 
	     <#if label_value?? >label_value="${label_value}"</#if>
	     <#if id?? >id="${id}"</#if>  
	     <#if value?? >value="${value}"</#if> 
	     <#if style?? >style="${style}"</#if>  
	     <#if attach_props?? >${attach_props}</#if>
	     <#if auth_type=='readonly' >readonly="readonly"</#if>
	     <#if maxlength?? >maxlength="${maxlength}"</#if>
	     <#if datarule?? >data-rule="${datarule}" datarule="${datarule}"</#if>
	     <#if max?? >max="${max}"</#if>
	     <#if min?? >min="${min}"</#if>
	     <#if placeholder?? >placeholder="${placeholder}"</#if> 
    >
    </#if>  
    <#if (ispass??)&&ispass=="true" >
    <input type="password" class="form-control"  name="${name}" auth_type="${auth_type}" autocomplete="off"
         <#if label_value?? >label_value="${label_value}"</#if>
	     <#if id?? >id="${id}"</#if>  
	     <#if value?? >value="${value}"</#if> 
	     <#if style?? >style="${style}"</#if>  
	     <#if attach_props?? >${attach_props}</#if>
	     <#if auth_type=='readonly' >readonly="readonly"</#if>
	     <#if maxlength?? >maxlength="${maxlength}"</#if>
	     <#if datarule?? >data-rule="${datarule}" datarule="${datarule}"</#if>
	     <#if max?? >max="${max}"</#if>
	     <#if min?? >min="${min}"</#if>
	     <#if placeholder?? >placeholder="${placeholder}"</#if> 
    >
    </#if>  
<#if comp_col_num!='0' >
</div>
</#if>
