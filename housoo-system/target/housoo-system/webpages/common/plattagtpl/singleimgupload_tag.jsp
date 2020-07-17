<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatBeanUtil"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatPropUtil"%>
<%@ page language="java" import="com.housoo.platform.core.service.FileAttachService"%>
<%@ page language="java" import="org.apache.commons.lang3.StringUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
    Map<String, Object> singleimgconfig = PlatBeanUtil
            .getMapFromRequest(request);
    String BUS_TABLENAME = request.getParameter("BUS_TABLENAME");
    String BUS_RECORDID = request.getParameter("BUS_RECORDID");
    String FILE_TYPEKEY = request.getParameter("FILE_TYPEKEY");
    FileAttachService fileAttachService = (FileAttachService) PlatAppUtil
            .getBean("fileAttachService");
    if (StringUtils.isNotEmpty(BUS_RECORDID)) {
        List<Map<String, Object>> fileList = fileAttachService
                .findList(BUS_TABLENAME, BUS_RECORDID, FILE_TYPEKEY);
        if (fileList != null && fileList.size() > 0) {
            Map<String, Object> fileAttach = fileList.get(0);
            String FILE_NAME = (String) fileAttach.get("FILE_NAME");
            String FILE_PATH = (String) fileAttach.get("FILE_PATH");
            String attachFileUrl = PlatPropUtil.getPropertyValue(
                    "config.properties", "attachFileUrl");
            String imgsrc = attachFileUrl + FILE_PATH;
            singleimgconfig.put("imgsrc", imgsrc);
            singleimgconfig.put("dbfilepath", FILE_PATH);
            singleimgconfig.put("originalfilename", FILE_NAME);
        }
    }
    String FILE_UPSERVER = request.getParameter("FILE_UPSERVER");
    if (StringUtils.isNotEmpty(FILE_UPSERVER)
            && "2".equals(FILE_UPSERVER)) {
        String fileServerUrl = PlatPropUtil.getPropertyValue(
                "config.properties", "fileServerUrl");
        singleimgconfig.put("FILESERVER_URL", fileServerUrl);
    }
    request.setAttribute("singleimgconfig", singleimgconfig);
%>

<%-- 
<jsp:include page="/webpages/common/plattagtpl/singleimgupload_tag.jsp">
    <jsp:param name="FILESIGNLE_LIMIT" value="${FILESIGNLE_LIMIT}" />
    <jsp:param name="IMG_WIDTH" value="${IMG_WIDTH}" />
    <jsp:param name="IMG_HEIGHT" value="${IMG_HEIGHT}" />
    <jsp:param name="UPLOAD_ROOTFOLDER" value="${UPLOAD_ROOTFOLDER}" />
    <jsp:param name="DEFAULT_IMGPATH" value="${DEFAULT_IMGPATH}" />
    <jsp:param name="BIND_ID" value="${BIND_ID}" />
    <jsp:param name="BUS_TABLENAME" value="${BUS_TABLENAME}" />
    <jsp:param name="BUS_RECORDID" value="${BUS_RECORDID}" />
    <jsp:param name="FILE_TYPEKEY" value="${FILE_TYPEKEY}" />
</jsp:include>
--%> 

<a <c:if test="${singleimgconfig.imgsrc!=null}">href="${singleimgconfig.imgsrc}"</c:if>
   <c:if test="${singleimgconfig.imgsrc==null}">href="${singleimgconfig.DEFAULT_IMGPATH}"</c:if>
	class="fancybox">
	<div id="${singleimgconfig.BIND_ID}" class="platSingleImgUploadPicker" fileserverurl="${singleimgconfig.FILESERVER_URL}">
		<img height="${singleimgconfig.IMG_HEIGHT}px" width="${singleimgconfig.IMG_WIDTH}px"
		    <c:if test="${singleimgconfig.dbfilepath!=null}">
		       dbfilepath="${singleimgconfig.dbfilepath}"
		       originalfilename="${singleimgconfig.originalfilename}"
		    </c:if>
			defaultimgpath="${singleimgconfig.DEFAULT_IMGPATH}" onerror="this.src='${singleimgconfig.DEFAULT_IMGPATH}'"
			<c:if test="${singleimgconfig.imgsrc!=null}">
		       src="${singleimgconfig.imgsrc}"
		       data-imgurl="${singleimgconfig.imgsrc}"
		    </c:if>
		    <c:if test="${singleimgconfig.imgsrc==null}">
		       src="${singleimgconfig.DEFAULT_IMGPATH}"
		    </c:if>
		>
	</div>
</a>
<div class="imgUploadToolDiv">
	<span id="${singleimgconfig.BIND_ID}_imgUploadBtnContainer" title="编辑"></span> <span
		id="${singleimgconfig.BIND_ID}_imgUploadToolButton" class="imgUploadToolButton">
			<c:if test="${singleimgconfig.imgsrc!=null}">
				<i class="fa fa-crop" onclick="PlatUtil.openPicCutWindow('${singleimgconfig.BIND_ID}');" title="裁剪"></i>
				<i class="fa fa-trash" onclick="PlatUtil.deleteSingleUploadImg('${singleimgconfig.BIND_ID}');" title="删除"></i>
		 	</c:if>
		 </span>
</div>

