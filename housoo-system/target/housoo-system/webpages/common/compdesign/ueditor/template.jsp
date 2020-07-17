<#if PLAT_DESIGNMODE?? >
<div class="platdesigncomp" platcompname="${PLAT_COMPNAME!''}" compcode="${FORMCONTROL_COMPCODE}"
  compcontrolid="${FORMCONTROL_ID}" platcomid="${FORMCONTROL_ID}" uibtnsrights="edit,del"
>
</#if>
<#if LABEL_COL_NUM?? >
<label class="col-sm-${LABEL_COL_NUM} control-label" >
  <#if ALLOW_BLANK=='false' >
  <font class="redDot">*</font>
  </#if>${CONTROL_LABEL}：
</label>
</#if>
<#if COMP_COL_NUM?? >
<div class="col-sm-${COMP_COL_NUM}" >
</#if>
   <script id="${CONTROL_NAME}" name="${CONTROL_NAME}" type="text/plain">${CONTROL_VALUE!''}</script>
<#if COMP_COL_NUM?? >
</div>
</#if>

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
	var ue = UE.getEditor('${CONTROL_NAME}', {
	    autoHeightEnabled: true,
	    autoFloatEnabled: true,
	    maximumWords:${MAX_WORDS},
	    initialFrameHeight:${CONTROL_HEIGHT},
	    /* 上传图片配置项 */
	    "imageActionName": "uploadimage", /* 执行上传图片的action名称 */
	    "imageFieldName": "file", /* 提交的图片表单名称 */
	    "imageMaxSize": ${IMAGE_MAXSIZE}, /* 上传大小限制，单位B */
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
        "fileMaxSize": ${FILE_MAXSIZE}, /* 上传大小限制，单位B，默认50MB */
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

<#if PLAT_DESIGNMODE?? >
</div>
</#if>
