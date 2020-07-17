<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
  <plattag:operlistgroup onclickfn="${TREE_CLICKFNNAME}" grouptitle="${PLAT_COMPNAME}" 
	 dyna_interface="${JAVA_INTERCODE}" dyna_param="${DYNA_PARAMS}" 
	 addclickfn="${ADDCLICKFN!''}" editclickfn="${EDITCLICKFN!''}" delclickfn="${DELCLICKFN!''}"
	 <#if CONTROL_ID?? >id="${CONTROL_ID}"</#if>  
   ></plattag:operlistgroup>
<script type="text/javascript">
  ${TREE_CLICKFN}
  ${ADDCLICKFNCONTENT!''}
  ${EDITCLICKFNCONTENT!''}
  ${DELCLICKFNCONTENT!''}
</script>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
