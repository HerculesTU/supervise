<#if label_value?? >
<label class="col-sm-${label_col_num} control-label" id="LABEL_${id}_LABEL"
   <#if auth_type=='hidden' >style="display: none;"</#if>
>
  <font class="redDot" <#if allowblank=='true' >style="display: none;"</#if> 
  >*</font>
  ${label_value!''}：
</label>
</#if>
<#if comp_col_num!='0' >
<div class="col-sm-${comp_col_num}" id="LABEL_${id}_DIV"
   <#if auth_type=='hidden' >style="display: none;"</#if>
>
</#if>
    <label class="control-label" id="${id}" >
    ${value!''}
	</label>
<#if comp_col_num!='0' >
</div>
</#if>
