<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
	</#if>
	<plattag:labeltag allowblank="${ALLOW_BLANK}" id="${CONTROL_NAME}" value="${CONTROL_VALUE!''}" auth_type="${CONTROL_AUTH}"
	    <#if CONTROL_LABEL?? >label_value="${CONTROL_LABEL}"</#if>
	    <#if COMP_COL_NUM?? >comp_col_num="${COMP_COL_NUM}"</#if>
        <#if LABEL_COL_NUM?? >label_col_num="${LABEL_COL_NUM}"</#if>
    >
    </plattag:labeltag>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
