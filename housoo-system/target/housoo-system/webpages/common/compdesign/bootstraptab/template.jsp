<div class="tabs-container platdesigncomp" class="tab-content platdesigncomp" platcompname="BootStrapTab控件" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del" platundragable="true"
        compcontrolid="${FORMCONTROL_ID}" compcode="${FORMCONTROL_COMPCODE}" id="${CONTROL_ID}" style="width:100%;height:100%;">
	<ul class="nav nav-tabs">
	    <#list tabList as tab>
		<li class="${(tab_index==0)?string('active','')}" subtabid="${tab.SUBTAB_ID}"
		  onclick="PlatUtil.onBootstrapTabClick('${CONTROL_ID}','${tab.SUBTAB_ID}','${tab.SUBTAB_CLICKFN}','${tab.SUB_RESIZEHEIGHT}');"
		><a data-toggle="tab" href="#${tab.SUBTAB_ID}"
			aria-expanded="${(tab_index==0)?string('true','false')}">${tab.SUBTAB_NAME}</a></li>
		</#list>
	</ul>
	<div class="tab-content platdesigncomp" platcompname="Tab控件容器" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del" platundragable="true"
        compcontrolid="${FORMCONTROL_ID}" compcode="${FORMCONTROL_COMPCODE}" style="height: calc(100% - 42px);" >
	    <#list tabList as tab>
		<div id="${tab.SUBTAB_ID}" class="tab-pane platdesigncomp ${(tab_index==0)?string('active','')}" style="height:100%;"
		  platcompname="${tab.SUBTAB_NAME}" platcomid="${FORMCONTROL_ID}_${tab.SUBTAB_ID}" uibtnsrights="add" platundragable="true"
	      compcontrolid="${FORMCONTROL_ID}" compcode="${FORMCONTROL_COMPCODE}"
		 >
		</div>
		</#list>
	</div>
</div>

<script type="text/javascript">
<#list tabList as tab>
<#if tab.SUBTAB_CLICKFN?? >
${tab.SUBTAB_CLICKCONTENT}
</#if>
</#list>
</script>