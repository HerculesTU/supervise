<div class="row" style="height:40px;padding-top: 4px; background: #e5e5e5;<#if !(buttonList??)||(buttonList?size==0) >display: none;</#if>" 
     <#if id?? >id="${id}"</#if>  
>
    <input type="hidden" id="JbpmFlowInfoJson" value="${JbpmFlowInfoJson!''}" />
    <div class="col-sm-12 text-right">
       <#list buttonList as button>
       <button class="btn btn-outline btn-${button.BTNBIND_COLOR} btn-sm" onclick="${button.BTNBIND_CLICKFN}('${button.BTNBIND_NAME}');" type="button"><i class="fa ${button.BTNBIND_ICON}"></i>${button.BTNBIND_NAME}</button>
       </#list>
    </div>
</div>

<script type="text/javascript">
<#if buttonList??&&(buttonList?size>0) >
<#list buttonList as btn>
${btn.BTNBIND_CLICKCONTENT!''}
</#list>
</#if>
</script>