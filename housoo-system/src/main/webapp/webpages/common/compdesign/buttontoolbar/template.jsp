<div class="row platdesigncomp" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true"
    compcode="${FORMCONTROL_COMPCODE}" platcompname="按钮工具栏" compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
     <div class="col-sm-12 text-right">
        <#list operbtns as btn>
		<button type="button" onclick="${btn.TABLEBUTTON_FN}();" platreskey="${btn.TABLEBUTTON_RESKEY!''}"
		    <#if btn.TABLEBUTTON_BTNID?? >id="${btn.TABLEBUTTON_BTNID}"</#if>  
		    <#if (btn.TABLEBUTTON_DISABLED??)&&(btn.TABLEBUTTON_DISABLED=="-1") >disabled="disabled"</#if>
			class="btn btn-outline btn-${btn.TABLEBUTTON_COLOR} btn-sm">
			<i class="${btn.TABLEBUTTON_ICON}"></i>&nbsp;${btn.TABLEBUTTON_NAME}
		</button>
		</#list>
     </div>
</div>

<script type="text/javascript">
<#if operbtns??&&(operbtns?size>0) >
<#list operbtns as btn>
${btn.TABLEBUTTON_FNCONTENT!''}
</#list>
</#if>

</script>

