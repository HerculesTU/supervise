<#if label_value?? >
<label class="col-sm-${label_col_num} control-label" id="TEXTAREA_${name}_LABEL"
   <#if auth_type=='hidden' >style="display: none;"</#if>
>
  <font class="redDot" <#if allowblank=='true' >style="display: none;"</#if> 
   >*
  </font>
  ${label_value!''}：
</label>
</#if>
<div class="col-sm-${comp_col_num}" id="TEXTAREA_${name}_DIV"
   <#if auth_type=='hidden' >style="display: none;"</#if>
>
    <#if buttonList?? && (buttonList?size > 0) >
	<div style="text-align: right;padding-bottom: 2px; padding-top: 2px;" >
	   <#list buttonList as button>
	   <button type="button"  style="margin-left: 2px;" onclick="${button.fnName}();" 
	   class="btn btn-outline btn-primary btn-sm" >${button.btnName}
	   </button>
	   </#list>
	</div>
	</#if>
    <textarea class="form-control" name="${name}" auth_type="${auth_type}" 
       <#if label_value?? >label_value="${label_value}"</#if>
       <#if id?? >id="${id}"</#if>
       <#if placeholder?? >placeholder="${placeholder}"</#if> 
	   <#if style?? >style="${style}"</#if>  
	   <#if !(style??) >style="height:80px;"</#if>  
	   <#if attach_props?? >${attach_props}</#if>
       <#if auth_type=='readonly' >readonly="readonly"</#if>
       <#if maxlength?? >maxlength="${maxlength}"</#if>
       <#if datarule?? >data-rule="${datarule}" datarule="${datarule}"</#if>
       <#if codemirror?? >codemirror="${codemirror}"</#if>
       <#if mirrorwidth?? >mirrorwidth="${mirrorwidth}"</#if>
       <#if mirrorheight?? >mirrorheight="${mirrorheight}"</#if>
     >${value!''}</textarea>
     
</div>
