<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
   <plattag:textarea name="${CONTROL_NAME}" allowblank="${ALLOW_BLANK}" auth_type="${CONTROL_AUTH}" 
      value="${CONTROL_VALUE!''}" codemirror="codemirror"
      <#if BUTTONCONFIGS?? >buttonconfigs="${BUTTONCONFIGS}"</#if>
      <#if MAX_LENGTH?? >maxlength="${MAX_LENGTH}"</#if>
      <#if MIN_LENGTH?? >minlength="${MIN_LENGTH}"</#if>
      <#if CONTROL_LABEL?? >label_value="${CONTROL_LABEL}"</#if>
      <#if COMP_PLACEHOLDER?? >placeholder="${COMP_PLACEHOLDER}"</#if>
      <#if COMP_COL_NUM?? >comp_col_num="${COMP_COL_NUM}"</#if>
      <#if LABEL_COL_NUM?? >label_col_num="${LABEL_COL_NUM}"</#if>
      <#if CONTROL_STYLE?? >style="${CONTROL_STYLE}"</#if>
      <#if CONTROL_ATTACH?? >attach_props="${CONTROL_ATTACH}"</#if>
      <#if CONTROL_ID?? >id="${CONTROL_ID}"</#if>
      <#if MIRRORWIDTH?? >mirrorwidth="${MIRRORWIDTH}"</#if>
      <#if MIRRORHEIGHT?? >mirrorheight="${MIRRORHEIGHT}"</#if>
   >
   </plattag:textarea>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>

<script type="text/javascript">
<#if operbtns??&&(operbtns?size>0) >
<#list operbtns as btn>
${btn.TABLEBUTTON_FNCONTENT}
</#list>
</#if>

</script>
