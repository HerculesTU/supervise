<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
   <plattag:datetime name="${CONTROL_NAME}" allowblank="${ALLOW_BLANK}" auth_type="${CONTROL_AUTH}"
       value="${CONTROL_VALUE!''}" format="${DATEFORMAT}"  istime="${ISTIME}" 
       <#if CONTROL_LABEL?? >label_value="${CONTROL_LABEL}"</#if>    
       <#if CONTROL_ID?? >id="${CONTROL_ID}"</#if>
       <#if COMP_PLACEHOLDER?? >placeholder="${COMP_PLACEHOLDER}"</#if>
       <#if MAX_DATE?? >max_date="${MAX_DATE}"</#if>
       <#if MIN_DATE?? >min_date="${MIN_DATE}"</#if>
       <#if LABEL_COL_NUM?? >label_col_num="${LABEL_COL_NUM}"</#if>
       <#if COMP_COL_NUM?? >comp_col_num="${COMP_COL_NUM}"</#if>
       <#if CHOOSEFN?? >choosefun="${CHOOSEFN}"</#if>
       <#if DEFAULTNOW?? >defaultnow="${DEFAULTNOW}"</#if>
   >
   </plattag:datetime>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>

<script type="text/javascript">
   ${CHOOSEFNCONTENT!''}
</script>
