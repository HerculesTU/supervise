<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del" platundragable="true"
>
</#if>
   ${CODE_TEXT!''}
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
