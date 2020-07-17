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
    <input type="text" class="form-control"  name="${name}" auth_type="${auth_type}" 
         <#if datasize?? >data-size="${datasize}"</#if>  
	     <#if id?? >id="${id}"</#if>  
	     <#if value?? >value="${value}"</#if> 
	     <#if allowblank=='false' >data-rule="required"</#if>
	     <#if datastep?? >data-step="${datastep}"</#if>  
    >
<#if comp_col_num!='0' >
</div>
</#if>

<script type="text/javascript">
$(function(){
	$("input[name='${name}']").rating({showClear:false,"language":"zh"});
});
</script>
