<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
   <plattag:select name="${CONTROL_NAME}" allowblank="${ALLOW_BLANK}" auth_type="${CONTROL_AUTH}" 
      value="${CONTROL_VALUE!''}" istree="${IS_TREE}" onlyselectleaf="${ONLYSELECTLEAF}" 
      <#if IS_MULTIPLE=='1' >multiple="multiple"
         <#if MAX_SELECT_NUM?? >max_select_num="${MAX_SELECT_NUM}"</#if>
      </#if>
      <#if CONTROL_LABEL?? >label_value="${CONTROL_LABEL}"</#if>
      <#if COMP_PLACEHOLDER?? >placeholder="${COMP_PLACEHOLDER}"</#if>
      <#if COMP_COL_NUM?? >comp_col_num="${COMP_COL_NUM}"</#if>
      <#if LABEL_COL_NUM?? >label_col_num="${LABEL_COL_NUM}"</#if>
      <#if CONTROL_STYLE?? >style="${CONTROL_STYLE}"</#if>
      <#if CONTROL_ATTACH?? >attach_props="${CONTROL_ATTACH}"</#if>
      <#if DATA_TYPE=='1' >static_values="${STATICVALUES}"</#if>
      <#if DATA_TYPE=='2' >dyna_interface="${DYNAINTERFACE}" dyna_param="${DYNAPARAM}" </#if>
   >
   </plattag:select>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
