<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
   <plattag:number name="${CONTROL_NAME}" allowblank="${ALLOW_BLANK}" auth_type="${CONTROL_AUTH}" 
      value="${CONTROL_VALUE!''}" step="${STEP}" 
      <#if DECIMALS?? >decimals="${DECIMALS}"</#if>
      <#if PREFIX?? >prefix="${PREFIX}"</#if>
      <#if POSTFIX?? >postfix="${POSTFIX}"</#if>
      <#if CONTROL_LABEL?? >label_value="${CONTROL_LABEL}"</#if>
      <#if COMP_PLACEHOLDER?? >placeholder="${COMP_PLACEHOLDER}"</#if>
      <#if COMP_COL_NUM?? >comp_col_num="${COMP_COL_NUM}"</#if>
      <#if LABEL_COL_NUM?? >label_col_num="${LABEL_COL_NUM}"</#if>
      <#if CONTROL_STYLE?? >style="${CONTROL_STYLE}"</#if>
      <#if CONTROL_ATTACH?? >attach_props="${CONTROL_ATTACH}"</#if>
      <#if COMP_MAX_VALUE?? >max="${COMP_MAX_VALUE}"</#if>
      <#if COMP_MIN_VALUE?? >min="${COMP_MIN_VALUE}"</#if>
   >
   </plattag:number>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
