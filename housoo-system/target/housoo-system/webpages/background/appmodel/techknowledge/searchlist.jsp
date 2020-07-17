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
    
    <title>My JSP 'dbmanager_view.jsp' starting page</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="X-UA-Compatible" content="IE=9" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<plattag:resources restype="css" loadres="layer,bootstrap-checkbox,jquery-ui,select2,plat-ui,autocomplete,pinyin">
	</plattag:resources>
	<script type="text/javascript" src="plug-in/pin-1.0.0/jquery.pin.js"></script>
	<style type="text/css">
	.ibox-content-head{
		padding-left: 0px !important;
	}
	</style>
  </head>
  
  <body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-content">
                    	<div class="ibox-content-head">
	                        <h3 id="resultShow" style="display:;" >
	                                      为您找到相关结果约1000000个： <span class="text-navy" id="keyWord"></span>
	                        </h3>
	                        <div class="search-form">
	                            <form action="#" method="get">
	                                <div class="input-group">
	                                    <input type="text" placeholder="请输入搜索关键字" name="knowledagesearch" class="form-control input-lg">
	                                    <div class="input-group-btn">
	                                        <button class="btn btn-lg btn-primary" onclick="doSearch();" type="button">
	                                            		搜索
	                                        </button>
	                                    </div>
	                                </div>
	
	                            </form>
	                        </div>
	                        <div class="hr-line-dashed"></div>
                        </div>
                        <div class="ibox-content-result">
	                        <div id="content"></div>
	                        <div class="text-center">
	                            <div id="pageDemo"></div>
	                        </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </body>
</html>
<plattag:resources restype="js" loadres="layer,jquery-ui,select2,jquery-layout,plat-util,autocomplete,pinyin">
</plattag:resources>
<script src="plug-in/laypage-1.3.0/laypage.js"></script>
<script type="text/javascript">

$(function(){
	//初始化UI控件
	PlatUtil.bindAutoComplete({
		url:"appmodel/KnowledgeController.do?autoKnowledage",
		autoCompleteType:"1",
		compName:"knowledagesearch"
	});
	 laypage({
		    cont: $('#pageDemo'),
		    skip: true, //是否开启跳页
		    pages: 10,
		    skin: '#009688',
		    curr:  1,
		    groups: 5, //连续显示分页数
		    jump: function(obj,first){
		    	 
		    }
		  });
});

function doSearch(){
	jumpPage();
}

function jumpPage(curr){
	var rows = 5;
	var keyword = $("input[name='knowledagesearch']").val();
	PlatUtil.ajaxProgress({
		   url:"appmodel/KnowledgeController.do?datagrid",
		   params:{
			   "page":curr || 1,
			   "rows":rows,
			   "keyword":keyword
		   },
		   callback:function(resultJson){
			 	  //调用分页
				 laypage({
				    cont: $('#pageDemo'),
				    skip: true, //是否开启跳页
				    pages: resultJson.total,
				    skin: '#009688',
				    curr: curr || 1,
				    groups: 5, //连续显示分页数
				    jump: function(obj,first){
				    	if(keyword){
				    		$("#keyWord").text(keyword);
				    	}
			    	    $("#pageDemo").find(".laypage_skip").val(obj.curr);
			    	    $("#pageDemo").find(".laypage_skip").attr("max",resultJson.total);
			    	    if(!first){
			    		  jumpPage(obj.curr);
			            }
				    }
				  }); 
			 	showData(resultJson);
		   }
		});
}

function showData(resultJson){
	var itemList = resultJson.rows;
	$("#content").html("");
	var appendHtml = "";
	for(var i=0;i<itemList.length;i++){
		appendHtml+= "<div class=\"search-result\">";
		var FULLTEXT_URL = itemList[i].FULLTEXT_URL;
		var FULLTEXT_RECORDID = itemList[i].FULLTEXT_RECORDID;
		var lookUrl = FULLTEXT_URL+"&RECORD_ID="+FULLTEXT_RECORDID;
		var ahtml = "<h3><a href=\""+lookUrl+"\" target=\"_blank\" >";
		ahtml+=itemList[i].FULLTEXT_INDEXTITLE+"</a></h3>";
		ahtml+="<p>"+itemList[i].FULLTEXT_CONTENT+"</p></div>";
		ahtml+="<div class=\"hr-line-dashed\"></div>";
		appendHtml+=ahtml;
	}
	$("#content").html(appendHtml); 
}

$(function(){
	$(".ibox-content-head").pin({
	      containerSelector: ".ibox-content"
	});
	$(document).scroll(function() {
		  $(".ac").css("display","none");
	});
	jumpPage();
})

</script>
