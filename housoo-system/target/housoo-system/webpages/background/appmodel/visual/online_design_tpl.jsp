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
    
    <title>${DESIGN_NAME}-在线设计开发</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,plat-ui,tipsy,autocomplete,layer,nicevalid,codemirror,webuploader,fancybox,flowdesign,touchspin,ratingstar,ueditor">
	</plattag:resources>
	<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin,layer,nicevalid,codemirror,slimscroll,webuploader,fancybox,flowdesign,touchspin,echart,ratingstar,superslide,ueditor">
	</plattag:resources>
	<style type="text/css">
	  /* .plat-designborder{position:relative;border:2px dashed #d9534f !important; background:#F6F6F6 !important;}
	  .plat-designtoolbar{position: absolute;z-index: 100000;right: 0;top: 0;}
	  .platdesigncomp{padding: 22px;} */
	  .plat-designhighlightborder{position:relative;border:2px dashed #C81522 !important;}
	  .plat-designhighlighttoolbar{position: absolute;z-index: 100000;right: 0;top: 0;}
	  .plat-designhighlightclear{ clear:both} 
	</style>
  </head>
  
  <body platcomid="0" class="platdesigncomp" platcompname="body容器" uibtnsrights="add" compcontrolid="0">
      <script type="text/javascript">
      $(function(){
    		PlatUtil.initUIComp();
      });
      </script>
      <input type="hidden" id="__BACK_PLAT_USER_JSON" value="${r'${sessionScope.__BACK_PLAT_USER_JSON}'}">
      <input type="hidden" id="ONLINE_DESIGNID" value="${DESIGN_ID}">
      ${genHtmlCode!''}
  </body>
</html>

<script type="text/javascript">

${DESIGN_JSENHANCE!''}

function highlightBorder(id,name){
	$("[platcomid]").each(function(){
		if($(this).is(".plat-designhighlightborder")){
			$(this).removeClass("plat-designhighlightborder");
			$(this).find(".plat-designhighlighttoolbar").remove();
			$(this).find(".plat-designhighlightclear").remove();
		}
	});
	$("[platcomid='"+id+"']").addClass("plat-designhighlightborder");
	var toolbarHtml = "<div class=\"plat-designhighlighttoolbar\"><b>"+name+"</b></div><div class=\"plat-designhighlightclear\"></div>";
	$("[platcomid='"+id+"']").append(toolbarHtml);
}
</script>
