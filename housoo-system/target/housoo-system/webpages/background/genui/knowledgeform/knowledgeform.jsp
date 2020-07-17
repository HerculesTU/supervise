<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@ taglib prefix="plattag" uri="/plattag"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    <title>知识库表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,ueditor"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,ueditor"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout" style="overflow-y:auto;">
   <form method="post" class="form-horizontal" compcode="formcontainer" action="appmodel/KnowledgeController.do?saveOrUpdate" id="knowledgeform" style="">

  <input type="hidden" name="KNOWLEDGE_ID" value="${knowledge.KNOWLEDGE_ID}">
<div class="form-group" compcode="formgroup">
   <plattag:select name="KNOWLEDGE_TYPE" allowblank="false" auth_type="write" value="${knowledge.KNOWLEDGE_TYPE}" istree="false" onlyselectleaf="false" label_value="技术类别" placeholder="请选择技术类别" comp_col_num="4" label_col_num="2" dyna_interface="dictionaryService.findList" dyna_param="{TYPE_CODE:'techtype',ORDER_TYPE:'ASC'}">
   </plattag:select>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
   <plattag:input name="KNOWLEDGE_TITLE" allowblank="false" auth_type="write" value="${knowledge.KNOWLEDGE_TITLE}" datarule="required;" maxlength="62" label_value="知识标题" placeholder="请输入知识标题" comp_col_num="10" label_col_num="2">
   </plattag:input>
</div>
<div class="hr-line-dashed"></div><div class="form-group" compcode="formgroup">
<label class="col-sm-2 control-label">
知识内容：
</label>
<div class="col-sm-10">
   <script id="KNOWLEDGE_CONTENT" name="KNOWLEDGE_CONTENT" type="text/plain">${knowledge.KNOWLEDGE_CONTENT}</script>
</div>

<script type="text/javascript">
$(function() {
	UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
	UE.Editor.prototype.getActionUrl = function(action) {
		if (action == 'uploadimage') {
		    //这里调用后端我们写的图片上传接口
		    return __ctxPath+'/system/FileAttachController/uploadUE.do';
		}else if (action == 'uploadfile') {
		    //这里调用后端我们写的图片上传接口
		    return __ctxPath+'/system/FileAttachController/uploadUE.do';
		}else if (action == 'uploadvideo') {
		    //这里调用后端我们写的图片上传接口
		    return __ctxPath+'/system/FileAttachController/uploadUE.do';
		}else{
		     return this._bkGetActionUrl.call(this, action);
		}
	} 
	var ue = UE.getEditor('KNOWLEDGE_CONTENT', {
	    autoHeightEnabled: true,
	    autoFloatEnabled: true,
	    maximumWords:100000,
	    initialFrameHeight:350,
	    /* 上传图片配置项 */
	    "imageActionName": "uploadimage", /* 执行上传图片的action名称 */
	    "imageFieldName": "file", /* 提交的图片表单名称 */
	    "imageMaxSize": 2097152, /* 上传大小限制，单位B */
	    "imageAllowFiles": [".png", ".jpg", ".jpeg", ".gif", ".bmp"], /* 上传图片格式显示 */
	    "imageCompressEnable": true, /* 是否压缩图片,默认是true */
	    "imageCompressBorder": 1600, /* 图片压缩最长边限制 */
	    "imageInsertAlign": "none", /* 插入的图片浮动方式 */
	    "imageUrlPrefix": "", /* 图片访问路径前缀 */
	    "imagePathFormat": "",
    	/* 上传文件配置 */
        "fileActionName": "uploadfile", /* controller里,执行上传视频的action名称 */
        "fileFieldName": "file", /* 提交的文件表单名称 */
        "filePathFormat": "", /* 上传保存路径,可以自定义保存路径和文件名格式 */
        "fileUrlPrefix": "", /* 文件访问路径前缀 */
        "fileMaxSize": 2097152, /* 上传大小限制，单位B，默认50MB */
        "fileAllowFiles": [
            ".png", ".jpg", ".jpeg", ".gif", ".bmp",
            ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",
            ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid",
            ".rar", ".zip", ".tar", ".gz", ".7z", ".bz2", ".cab", ".iso",
            ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".pdf", ".txt", ".md", ".xml"
        ], /* 上传文件格式显示 */
        /* 上传视频配置 */
        "videoActionName": "uploadvideo", /* 执行上传视频的action名称 */
        "videoFieldName": "file", /* 提交的视频表单名称 */
        "videoPathFormat": "", /* 上传保存路径,可以自定义保存路径和文件名格式 */
        "videoUrlPrefix": "", /* 视频访问路径前缀 */
        "videoMaxSize": 102400000, /* 上传大小限制，单位B，默认100MB */
        /* 上传视频格式显示 */
        "videoAllowFiles": [
            ".flv", ".swf", ".mkv", ".avi", ".rm", ".rmvb", ".mpeg", ".mpg",
            ".ogg", ".ogv", ".mov", ".wmv", ".mp4", ".webm", ".mp3", ".wav", ".mid"] 
            
	});
});
</script>

</div>
<div class="hr-line-dashed"></div></form></div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="submitBusForm();" platreskey="" class="btn btn-outline btn-primary btn-sm">
			<i class="fa fa-check"></i>&nbsp;提交
		</button>
		<button type="button" onclick="closeWindow();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function submitBusForm(){
	if(PlatUtil.isFormValid("#knowledgeform")){
		var url = $("#knowledgeform").attr("action");
		var formData = PlatUtil.getFormEleData("knowledgeform");
		PlatUtil.ajaxProgress({
			url:url,
			params : formData,
			callback : function(resultJson) {
				if (resultJson.success) {
					parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
					PlatUtil.setData("submitSuccess",true);
					PlatUtil.closeWindow();
				} else {
					parent.layer.alert(PlatUtil.FAIL_MSG,{icon: 2,resize:false});
				}
			}
		});
	}
}
function closeWindow(){
  PlatUtil.closeWindow();
}

</script>

</div>
</div>
  </body>
</html>

<script type="text/javascript">

</script>
