<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
   <plattag:ratingstar name="${CONTROL_NAME}" allowblank="${ALLOW_BLANK}" auth_type="${CONTROL_AUTH}" 
      value="${CONTROL_VALUE!''}" 
      <#if CONTROL_LABEL?? >label_value="${CONTROL_LABEL}"</#if>
      <#if COMP_COL_NUM?? >comp_col_num="${COMP_COL_NUM}"</#if>
      <#if LABEL_COL_NUM?? >label_col_num="${LABEL_COL_NUM}"</#if>
      <#if DATA_SIZE?? >datasize="${DATA_SIZE}"</#if>
      <#if DATA_STEP?? >datastep="${DATA_STEP}"</#if>
   >
   </plattag:ratingstar>
   
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
