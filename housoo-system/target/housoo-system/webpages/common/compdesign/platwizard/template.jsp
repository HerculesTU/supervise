<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME}" compcode="${FORMCONTROL_COMPCODE}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
   compcontrolid="${FORMCONTROL_ID}" style="height:100%" >
</#if>

<div class="plat-wizard">
	<ul class="steps">
	    <#list stepList as step>
		<li class="${(step_index==0)?string('active','')}">
		<span class="step" 
		<#if (step.STEPNEXTFN_NAME??)&&(step.STEPNEXTFN_NAME!="") >nextstep_fnname="${step.STEPNEXTFN_NAME}"</#if>
		>${step_index+1}</span>${step.STEP_NAME}<span class="chevron"></span>
		</li>	
		</#list>
	</ul>
</div>
<#if !PLAT_DESIGNMODE?? >
<div id="wizard-showdiv" style="height: calc(100% - 48px);" >
</#if>
   <#list stepList as step>
		<div platcompname="${step.STEP_NAME}" platcomid="${FORMCONTROL_ID}_${step.STEP_NAME}" uibtnsrights="add" platundragable="true"
	      compcontrolid="${FORMCONTROL_ID}" compcode="${FORMCONTROL_COMPCODE}" class="plat-wizard-everystep" id="${step.STEP_NAME}"
	      <#if step_index!=0 >style="height:100%"</#if>
	      <#if step_index==0 >style="height:100%"</#if>
	      >
   		</div>
	</#list>
<#if !PLAT_DESIGNMODE?? >
</div>
</#if>
<script type="text/javascript">
<#list stepList as step>
<#if step.STEPNEXTFN_NAME?? >
${step.STEPNEXT_FNCONTENT!''}
</#if>
</#list>
</script>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>