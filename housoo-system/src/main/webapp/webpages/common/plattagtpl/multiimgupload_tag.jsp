<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.SysConstants"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatBeanUtil"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatPropUtil"%>
<%@ page language="java" import="com.housoo.platform.core.util.PlatFileUtil"%>
<%@ page language="java"
	import="com.housoo.platform.core.service.FileAttachService"%>
<%@ page language="java" import="org.apache.commons.lang3.StringUtils"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
    Map<String, Object> multifileconfig = PlatBeanUtil
            .getMapFromRequest(request);
    String BUS_TABLENAME = request.getParameter("BUS_TABLENAME");
    String BUS_RECORDID = request.getParameter("BUS_RECORDID");
    String FILE_TYPEKEY = request.getParameter("FILE_TYPEKEY");
    String BIND_ID = request.getParameter("BIND_ID");
    String FILE_UPSERVER = request.getParameter("FILE_UPSERVER");
    String IS_INIT = request.getParameter("IS_INIT");
    String FILESIGNLE_LIMIT = request.getParameter("FILESIGNLE_LIMIT");
    request.setAttribute("SIGNFILELIMIT", PlatFileUtil.getFormatFileSize(Long.parseLong(FILESIGNLE_LIMIT)));
    if (StringUtils.isNotEmpty(FILE_UPSERVER)&&"2".equals(FILE_UPSERVER)) {
        String fileServerUrl = PlatPropUtil.getPropertyValue("config.properties","fileServerUrl");
        multifileconfig.put("FILESERVER_URL", fileServerUrl);
    }
    if(StringUtils.isEmpty(IS_INIT)){
        IS_INIT = "1";
    }
    //权限值upload,del或者none
    String RIGHTS = request.getParameter("FILE_RIGHTS");
    if (StringUtils.isEmpty(RIGHTS)) {
        Map<String, String> changeDefCtrlMap = (Map<String, String>) request
                .getAttribute(SysConstants.CHANGE_DEF_CTRL_AUTH_MAP_KEY);
        if (changeDefCtrlMap != null) {
            String changeAuth = changeDefCtrlMap.get(BIND_ID);
            if (StringUtils.isNotEmpty(changeAuth)) {
                RIGHTS = changeAuth;
            }
        }
    }
    if (StringUtils.isEmpty(RIGHTS)) {
        RIGHTS = "upload,del";
    }
    FileAttachService fileAttachService = (FileAttachService) PlatAppUtil
            .getBean("fileAttachService");
    if (StringUtils.isNotEmpty(BUS_RECORDID)) {
        List<Map<String, Object>> fileList = fileAttachService
                .findList(BUS_TABLENAME, BUS_RECORDID, FILE_TYPEKEY);
        if (fileList != null && fileList.size() > 0) {
            String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties","attachFileUrl");
            for(Map<String,Object> file:fileList){
                String FILE_PATH = (String)file.get("FILE_PATH");
                String getFilePath = attachFileUrl+FILE_PATH;
                file.put("GETFILE_PATH", getFilePath);
            }
            request.setAttribute("fileList", fileList);
        }else{
            request.setAttribute("fileList", null);
        }
    }else{
        request.setAttribute("fileList", null);
    }
    multifileconfig.put("BIND_ID", BIND_ID);
    request.setAttribute("IS_INIT", IS_INIT);
    request.setAttribute("uploadfilerights", RIGHTS);
    request.setAttribute("multifileconfig", multifileconfig);
    
%>

<div id="${multifileconfig.BIND_ID}_wrapper"  class="mulimageuploaderwrapper" uploadrootfolder="${multifileconfig.UPLOAD_ROOTFOLDER}" filesinglesizelimit="${multifileconfig.FILESIGNLE_LIMIT}">

	<div id="${multifileconfig.BIND_ID}_container" class="mulimageuploadercontainer">
		<!--头部，相册选择和格式选择-->

		<div id="${multifileconfig.BIND_ID}" class="uploader">
			<div class="queueList">
				<ul class="filelist" id="${multifileconfig.BIND_ID}_filelist" maxuploadcount="${multifileconfig.MAXUPLOAD_COUNT}">
				    <c:if test="${fileList!=null}">
                        <c:forEach items="${fileList}" var="file">
                           <li id="${file.FILE_ID}" dbfilepath="${file.FILE_PATH}" getfileurl="${file.GETFILE_PATH}" originalfilename="${file.FILE_NAME}">
							    <p class="title">${file.FILE_NAME}</p>
								<p class="imgWrap">
								    <a href="${file.GETFILE_PATH}" class="fancybox">
									<img src="${file.GETFILE_PATH}" >
									</a>
								</p>
								<p class="progress">
									<span></span>
								</p>
								<c:if test="${fn:contains(uploadfilerights,'del')}">
								<div class="file-panel" style="height:0px;">
								   <span class="cancel">删除</span>
								</div>
								</c:if>
								<span class="success"></span>
							</li>
                        </c:forEach>
				    </c:if>
				</ul>

			</div>
			<c:if test="${fn:contains(uploadfilerights,'upload')}">
			<div class="statusBar" >
			    <div class="info">最多允许上传${multifileconfig.MAXUPLOAD_COUNT}张图片,每张大小不超过${SIGNFILELIMIT}</div>
			    <div class="info" id="${multifileconfig.BIND_ID}_filecountdiv"></div>
				<div class="btns">
					<div id="${multifileconfig.BIND_ID}_filePicker2"></div>
				</div>
			</div>
			</c:if>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	if("${IS_INIT}"=="1"){
		PlatUtil.initMulImageUploader("${multifileconfig.BIND_ID}");
	}
});


</script>
