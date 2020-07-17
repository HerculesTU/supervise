<#if LABEL_COL_NUM?? >
<label class="col-sm-${LABEL_COL_NUM} control-label" >
${CONTROL_LABEL}：
</label>
</#if>
<#if COMP_COL_NUM?? >
<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
<div class="col-sm-${COMP_COL_NUM}" >
</#if>
<div class="imageUploadDiv platSingleImgUploadDiv platdesigncomp"
	platcompname="${PLAT_COMPNAME}" compcode="${FORMCONTROL_COMPCODE}"
	compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}"
	uibtnsrights="edit,del"  filesinglesizelimit="${FILESIGNLE_LIMIT}" style="width:${IMG_WIDTH}px;height:${IMG_HEIGHT}px;"
    uploadrootfolder="${UPLOAD_ROOTFOLDER}" 
>
<jsp:include page="/webpages/common/plattagtpl/singleimgupload_tag.jsp">
    <jsp:param name="FILESIGNLE_LIMIT" value="${FILESIGNLE_LIMIT}" />
    <jsp:param name="IMG_WIDTH" value="${IMG_WIDTH}" />
    <jsp:param name="IMG_HEIGHT" value="${IMG_HEIGHT}" />
    <jsp:param name="UPLOAD_ROOTFOLDER" value="${UPLOAD_ROOTFOLDER}" />
    <jsp:param name="DEFAULT_IMGPATH" value="${DEFAULT_IMGPATH}" />
    <jsp:param name="BIND_ID" value="${BIND_ID}" />
    <jsp:param name="BUS_TABLENAME" value="${BUS_TABLENAME}" />
    <jsp:param name="BUS_RECORDID" value="${BUS_RECORDID}" />
    <jsp:param name="FILE_TYPEKEY" value="${FILE_TYPEKEY!''}" />
    <jsp:param name="CONTROL_LABEL" value="${CONTROL_LABEL!''}" />
    <jsp:param name="COMP_COL_NUM" value="${COMP_COL_NUM!''}" />
    <jsp:param name="LABEL_COL_NUM" value="${LABEL_COL_NUM!''}" />
    <jsp:param name="FILE_UPSERVER" value="${FILE_UPSERVER!''}" />
</jsp:include>

</div>

<#if COMP_COL_NUM?? >
<#if PLAT_DESIGNMODE?? >
</div>
</#if>
</div>
</#if>