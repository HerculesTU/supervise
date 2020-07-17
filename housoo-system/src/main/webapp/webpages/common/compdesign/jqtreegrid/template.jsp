<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="Jq树形表格" compcode="${FORMCONTROL_COMPCODE}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
   compcontrolid="${FORMCONTROL_ID}">
</#if>
<#if PANEL_TITLE?? >
<div class="panel-Title">
	<h5 id="${CONTROL_ID}_paneltitle">${PANEL_TITLE}</h5>
</div>
</#if>
<div id="${CONTROL_ID}_search">
    <#list querys as query>
        <#if query.QUERYCONDITION_CTRLTYPE=='1' >
         <input type="hidden" name="${query.QUERYCONDITION_CTRLNAME}" value="${query.QUERYCONDITION_CONTROLVALUE!''}">
        </#if>
	</#list>
</div>

<div class="gridPanel">
	<table id="${CONTROL_ID}"></table>
</div>
<div id="jqtreeContextmenu">
      <#if operbtns??&&(operbtns?size>0) >
      <ul class="dropdown-menu" role="menu">
          <#list operbtns as btn>
          <li platreskey="${btn.TABLEBUTTON_RESKEY!''}" ><a fnname="${btn.TABLEBUTTON_FN}" ><i class="${btn.TABLEBUTTON_ICON}"></i>&nbsp;${btn.TABLEBUTTON_NAME}</a></li>
          </#list>
      </ul>
      </#if>
 </div>
<script type="text/javascript">
<#if operbtns??&&(operbtns?size>0) >
<#list operbtns as btn>
${btn.TABLEBUTTON_FNCONTENT}
</#list>
</#if>

$(function(){
	PlatUtil.initJqTreeGrid({
		  tableId:"${CONTROL_ID}",
		  searchPanelId:"${CONTROL_ID}_search",
		  url: "appmodel/CommonUIController.do?jqtreetabledata&FORMCONTROL_ID=${FORMCONTROL_ID}",
		  ExpandColumn: "${ExpandColumn}",
		  colModel: [
		      <#list columns as column>
		         {name: "${column.FIELD_NAME}",label:"${column.FIELD_COMMENT}",
		         width: ${column.FIELD_WIDTH},align:"left",
		         <#if column.FIELD_ISHIDE=='1' >hidden:true,</#if>
		         <#if column.FIELD_NAME==primaryKeyName >key:true,</#if>
		         <#if column.FORMAT_FN??&&column.FORMAT_FN!='' >formatter:${column.FORMAT_FN},</#if>
		         sortable:false
		         }<#if column_index!=(columns?size-1) >,</#if>
		      </#list>
           ]
	 });
});
</script>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>