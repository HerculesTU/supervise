<#if PLAT_DESIGNMODE?? >
<div class="form-group platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
    <plattag:input name="HIDDEN_NAME" auth_type="readonly" allowblank="true" 
       placeholder="隐藏域控件:${PLAT_COMPNAME!''}" comp_col_num="12" 
       ></plattag:input>
</div>
</#if>
<#if !PLAT_DESIGNMODE?? >
  <input type="hidden"  name="${CONTROL_NAME}" value="${CONTROL_VALUE!''}"
     <#if CONTROL_ID?? >id="${CONTROL_ID}"</#if>  
  >
</#if>