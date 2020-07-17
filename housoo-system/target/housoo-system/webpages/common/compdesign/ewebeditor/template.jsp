<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
<#if LABEL_COL_NUM?? >
<label class="col-sm-${LABEL_COL_NUM} control-label" >
  <#if ALLOW_BLANK=='false' >
  <font class="redDot">*</font>
  </#if>${CONTROL_LABEL}：
</label>
</#if>
<#if COMP_COL_NUM?? >
<div class="col-sm-${COMP_COL_NUM}" >
</#if>
   <input type="hidden" name="${CONTROL_NAME}" id="${CONTROL_NAME}" value="${CONTROL_VALUE!''}"  isewebeditor="true" > 
   <IFRAME
		ID="${CONTROL_NAME}_EWEB"
		SRC="plug-in/ewebeditor-10.8/ewebeditor.htm?id=${CONTROL_NAME}&style=${EWEBSTYLE}&skin=flat3"
		FRAMEBORDER="0" SCROLLING="no" WIDTH="100%" HEIGHT="${CONTROL_HEIGHT}"></IFRAME>
<#if COMP_COL_NUM?? >
</div>
</#if>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
