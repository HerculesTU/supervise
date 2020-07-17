<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
  <plattag:listgroup onclickfn="${TREE_CLICKFNNAME}" grouptitle="${PLAT_COMPNAME}" 
	 dyna_interface="${JAVA_INTERCODE}" dyna_param="${DYNA_PARAMS}"
   ></plattag:listgroup>
<script type="text/javascript">
  ${TREE_CLICKFN}
</script>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
