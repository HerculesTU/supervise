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
    <input type="hidden" name="${name}" value="${value!''}" queryreset="true"> 
	<input type="text" class="form-control plat-queryicon" readonly="readonly" name="${name}_LABELS" auth_type="${auth_type}" 
	     maxselect="${maxselect}" minselect="${minselect}" checktype="${checktype!''}"
	     checkcascadey="${checkcascadey!''}" checkcascaden="${checkcascaden!''}" selectorurl="${selectorurl}"
	     <#if auth_type!='readonly' >onclick="PlatUtil.showWinSelector(this);" </#if> 
	     title="${title}" width="${width}" height="${height}"
	     <#if label_value?? >label_value="${label_value}"</#if>
	     <#if allowblank=='false' >data-rule="required;"</#if> 
	     <#if id?? >id="${id}"</#if>  
	     <#if selectedlabels?? >value="${selectedlabels}"</#if> 
	     <#if placeholder?? >placeholder="${placeholder}"</#if> 
	     <#if style?? >style="${style}"</#if>  
	     <#if attach_props?? >${attach_props}</#if>
	     <#if datarule?? >data-rule="${datarule}" datarule="${datarule}"</#if>
	     <#if callbackfn?? >callbackfn="${callbackfn}"</#if>  
    >
<#if comp_col_num!='0' >
</div>
</#if>