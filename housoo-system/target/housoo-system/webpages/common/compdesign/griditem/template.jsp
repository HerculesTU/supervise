<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="网格表格" compcode="${FORMCONTROL_COMPCODE}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
   compcontrolid="${FORMCONTROL_ID}">
</#if>
<plattag:griditem iconfont="${ICONFONT}" dyna_interface="${JAVA_INTERCODE}" dragable="${DRAGABLE!''}"
           selectedrecordids="${r'${selectedRecordIds}'}" 
 		   itemtplpath="common/compdesign/griditem/griditem_tpl" id="${CONTROL_ID}" paneltitle="${PANELTITLE}"
 		   itemconf="${ITEMCONF}" 
 ></plattag:griditem>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>