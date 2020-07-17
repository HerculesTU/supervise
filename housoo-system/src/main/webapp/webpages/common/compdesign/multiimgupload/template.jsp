<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
<jsp:include page="/webpages/common/plattagtpl/multiimgupload_tag.jsp">
    <jsp:param  name="MAXUPLOAD_COUNT" value="${MAXUPLOAD_COUNT}"/>
    <jsp:param name="FILESIGNLE_LIMIT" value="${FILESIGNLE_LIMIT}" />
    <jsp:param name="ALLOW_FILEEXTS" value="${ALLOW_FILEEXTS!''}" />
    <jsp:param name="UPLOAD_ROOTFOLDER" value="${UPLOAD_ROOTFOLDER}" />
    <jsp:param name="BIND_ID" value="${BIND_ID}" />
    <jsp:param name="BUS_TABLENAME" value="${BUS_TABLENAME}" />
    <jsp:param name="BUS_RECORDID" value="${BUS_RECORDID}" />
    <jsp:param name="FILE_TYPEKEY" value="${FILE_TYPEKEY!''}" />
    <jsp:param name="FILE_RIGHTS" value="${FILE_RIGHTS!''}" />
    <jsp:param name="FILE_UPSERVER" value="${FILE_UPSERVER!''}" />
    <jsp:param name="FILE_UPURL" value="${FILE_UPURL!''}" />
    <jsp:param name="IS_INIT" value="${IS_INIT!''}" />
</jsp:include>
<#if PLAT_DESIGNMODE?? >
</div>
</#if>