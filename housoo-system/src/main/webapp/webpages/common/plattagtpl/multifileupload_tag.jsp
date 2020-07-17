<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.housoo.platform.core.util.SysConstants"%>
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
    Map<String, Object> multifileconfig = PlatBeanUtil
            .getMapFromRequest(request);
    String BUS_TABLENAME = request.getParameter("BUS_TABLENAME");
    String BUS_RECORDID = request.getParameter("BUS_RECORDID");
    String FILE_TYPEKEY = request.getParameter("FILE_TYPEKEY");
    String BIND_ID = request.getParameter("BIND_ID");
    String FILE_UPSERVER = request.getParameter("FILE_UPSERVER");
    String IS_INIT = request.getParameter("IS_INIT");
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
            request.setAttribute("fileList", fileList);
        }
    }
    multifileconfig.put("BIND_ID", BIND_ID);
    request.setAttribute("IS_INIT", IS_INIT);
    request.setAttribute("uploadfilerights", RIGHTS);
    request.setAttribute("multifileconfig", multifileconfig);
%>

<%-- 
<jsp:include page="/webpages/common/plattagtpl/multifileupload_tag.jsp">
    <jsp:param name="FILESIGNLE_LIMIT" value="${FILESIGNLE_LIMIT}" />
    <jsp:param name="ALLOW_FILEEXTS" value="${ALLOW_FILEEXTS}" />
    <jsp:param name="UPLOAD_ROOTFOLDER" value="${UPLOAD_ROOTFOLDER}" />
    <jsp:param name="BIND_ID" value="${BIND_ID}" />
    <jsp:param name="BUS_TABLENAME" value="${BUS_TABLENAME}" />
    <jsp:param name="BUS_RECORDID" value="${BUS_RECORDID}" />
    <jsp:param name="FILE_TYPEKEY" value="${FILE_TYPEKEY}" />
</jsp:include>
--%> 

<c:if test="${fn:contains(uploadfilerights,'upload')}">   
<div class="btns platListFileUploadDiv" uploadrootfolder="${multifileconfig.UPLOAD_ROOTFOLDER}"
   extensions="${multifileconfig.ALLOW_FILEEXTS}" filesinglesizelimit="${multifileconfig.FILESIGNLE_LIMIT}">
    <span id="${multifileconfig.BIND_ID}"><i class="fa fa-cloud-upload"></i>&nbsp;添加附件</span>
</div>
</c:if>
<!--用来存放文件信息-->
<div id="${multifileconfig.BIND_ID}_filelist" class="uploadify-queue" 
fileserverurl="${multifileconfig.FILESERVER_URL}" fileupurl="${multifileconfig.FILE_UPURL}"
<c:if test="${fileList!=null}"> 
style="display: block;"
</c:if>
>
   <c:if test="${fileList!=null}">
     <c:forEach items="${fileList}" var="file">
        <div class="uploadify-queue-item" id="${file.FILE_ID}">
          <span title="成功" class="stateIcon succeed"><i class="fa fa-check-circle"></i></span>
          <div class="cancel">
          
          <c:if test="${fn:contains(uploadfilerights,'del')}">
          <a href="javascript:void(0);" onclick="PlatUtil.deleteMultiFileUpload('${file.FILE_ID}');" title="删除">
          <i class="fa fa-trash-o"></i>
          </a>
          </c:if>
          
          <a href="javascript:void(0);" onclick="PlatUtil.downloadFile('${file.FILE_PATH}','${file.FILE_NAME}','${multifileconfig.BIND_ID}');" title="下载">
          <i class="fa fa-download"></i>
          </a>
          </div>
          <span class="fileName" dbfilepath="${file.FILE_PATH}"
           originalfilename="${file.FILE_NAME}">${file.FILE_NAME}(${file.FILE_SIZE}) </span>
           <span class="state">恭喜您，上传成功</span>
          <div class="fileImage"><i class="fa fa-file-archive-o"></i></div>
          <div class="uploadify-progress" style="display: none;">
          <div style="width: 100%;" class="uploadify-progress-bar"></div>
          </div>
         </div>
     </c:forEach>
   </c:if>
</div>
<script type="text/javascript">
$(function(){
	if("${IS_INIT}"=="1"){
		PlatUtil.initMultiFileUploader();
	}
});
</script>
