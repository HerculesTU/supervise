<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
<jsp:include page="/webpages/common/plattagtpl/selectedtable_tag.jsp">
    <jsp:param value="${TABLE_ID}" name="tableid"></jsp:param>
    <jsp:param value="${SHOWCOL_NAMES}" name="showcolnames"></jsp:param>
    <jsp:param value="${SHOWCOL_CODES}" name="showcolcodes"></jsp:param>
    <jsp:param value="${ID_CODE}" name="checkvaluecol"></jsp:param>
    <jsp:param value="${LABEL_CODE}" name="checklabelcol"></jsp:param>
    <jsp:param value="${JAVA_INTER}" name="dynainterface"></jsp:param>
    <jsp:param value="${SELECTED_ID}" name="selectedRecordIds"></jsp:param>
</jsp:include>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>