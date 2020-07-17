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
    <title>导入word表单</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,webuploader"></plattag:resources>
    <plattag:resources restype="js" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,webuploader"></plattag:resources>
  </head>
  
  <body>
    <script type="text/javascript">
	$(function(){
		  PlatUtil.initUIComp();
	});
	</script>
    <div class="plat-directlayout" style="height:100%" platundragable="true" compcode="direct_layout">
   <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
		<div class="btns platListFileUploadDiv" uploadrootfolder="importword"
		   extensions="doc,docx" filesinglesizelimit="10485760">
		    <span id="importword"><i class="fa fa-cloud-upload"></i>&nbsp;选择word文件</span>
		</div>
	</div>
   <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
   <div class="row" style="height:40px; margin-top: -6px;padding-top: 4px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
     <div class="col-sm-12 text-right">
		<button type="button" onclick="closeWindow();" platreskey="" class="btn btn-outline btn-danger btn-sm">
			<i class="fa fa-times"></i>&nbsp;关闭
		</button>
     </div>
</div>

<script type="text/javascript">
function closeWindow(){
  PlatUtil.closeWindow();
}


$(function(){
	var pickerId = "importword";
	var GUID = WebUploader.Base.guid();
	var config = {
		pickerId:pickerId,
		pick: {
	    	id:"#"+pickerId,
	    	multiple:false
	    },
	    accept:{
	        title: "Files",
	        extensions: "doc,docx",
	    },
	    //单个文件最大允许大小单位是字节
	    fileSingleSizeLimit:20971520,
	    formData:{
	    	"uploadRootFolder":"importWord"
	    }
	}
	var initConfig = {
			// swf文件路径
		    swf: "plug-in/webuploader-0.1.5/dist/Uploader.swf",
		    // 文件接收服务端。
		    server: __ctxPath+"/system/FileAttachController/upload.do",
		    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
		    accept:{
		        title: "Files"
		    },
		    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
		    resize: false,
		    auto:true,
		    prepareNextFile:true,
		    chunked:true,
		    //默认20M进行分块
		    chunkSize:20971520,
		    //允许并发上传的文件个数
		    threads: 1,
		    duplicate:true,
		    formData:{
		    	guid:GUID
		    }
		};
		initConfig = PlatUtil.mergeObject(initConfig,config);
		WebUploader.create(initConfig).on("uploadSuccess", function( file,response) {
			var dbfilepath = response.dbfilepath;
			parent.layer.close(initConfig.progressWinIndex);
			PlatUtil.ajaxProgress({
				progressMsg:"正在解析...",
				url:"appmodel/ImportWordController.do?impWord",
				params:{
					 "dbfilepath":dbfilepath
				},
				callback:function(resultJson){
					if(resultJson.success){
						PlatUtil.setData("wordHtml",{
							//选择的类型,单选还是多选框
							"wordHtmlContent":resultJson.wordHtmlContent,
							
						});
						PlatUtil.closeWindow();
					}else{
						parent.layer.alert("解析失败!",{icon: 2,resize:false});
					}
			   	    
			   }
			});
			
		}).on("error",function(errortype){
			PlatUtil.webuploadErrorEvent(errortype,initConfig);
		}).on("startUpload",function(){
			initConfig.progressWinIndex = parent.layer.msg("正在上传中...", {
				  icon: 16,
				  shade:0.01,
				  time:60000
			 });
		}).on("fileQueued",function(file){
			
		}).on("uploadProgress",function(file, percentage){
			
		}).on("uploadError",function(file){
			
		}); 
})

</script>

</div>
</div>
  </body>
</html>

<script type="text/javascript">

</script>
