<#if label_value?? >
<label class="col-sm-${label_col_num} control-label" id="CHECKBOX_${name}_LABEL"
   <#if auth_type=='hidden' >style="display: none;"</#if>
>
  <font class="redDot" <#if allowblank=='true' >style="display: none;"</#if> 
  >*</font>
  ${label_value!''}：
</label>
</#if>
<div class="col-sm-${comp_col_num}" id="CHECKBOX_${name}_DIV" allowblank="${allowblank}"
    is_horizontal="${is_horizontal}" auth_type="${auth_type}"
   <#if label_value?? >label_value="${label_value}"</#if>
   <#if auth_type=='hidden' >style="display: none;"</#if>
   <#if static_values?? >static_values="${static_values}"</#if>
   <#if dyna_interface?? >dyna_interface="${dyna_interface}"</#if>
   <#if dyna_param?? >dyna_param="${dyna_param}"</#if>
   <#if style?? >style="${style}"</#if>
   <#if max?? >style="${max}"</#if>
   <#if min?? >style="${min}"</#if>
>
	<#list optionlist as option>
	   ${option}
	</#list>
</div>

