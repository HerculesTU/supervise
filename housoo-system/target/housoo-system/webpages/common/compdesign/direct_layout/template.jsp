<div class="plat-directlayout platdesigncomp" style="height:${CONTAINER_HEIGHT}" platcompname="东西南北中布局" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del" platundragable="true"
   compcontrolid="${FORMCONTROL_ID}" compcode="${FORMCONTROL_COMPCODE}"
   <#if layoutsize?? >layoutsize="${layoutsize}"</#if>  
   <#if CONTROL_ID??&&CONTROL_ID!="" >id="${CONTROL_ID}"</#if>  
>
   <#list layoutlist as layout>
   <div class="ui-layout-${layout.LAYOUT_TYPE} platdesigncomp" platcompname="${layout.platcompname}" platcomid="${FORMCONTROL_ID}_${layout_index}" uibtnsrights="add" platundragable="true"
	  compcontrolid="${FORMCONTROL_ID}" compcode="${FORMCONTROL_COMPCODE}"
	  <#if layout.LAYOUT_ID??&&layout.LAYOUT_ID!="" >id="${layout.LAYOUT_ID}"</#if>  
	  <#if layout.SUPPORT_YSCROLL=="1" >style="overflow-y:auto;"</#if>  
   >
   </div>
   </#list>
</div>