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
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,plat-ui,tipsy,autocomplete,layer">
	</plattag:resources>
	<link rel="stylesheet" type="text/css" href="webpages/background/framework/css/portal.css">
	<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin,layer,slimscroll,echart,superslide">
	</plattag:resources>
    <%-- 
	<script type="text/javascript" src="webpages/background/framework/js/dragMove.js"></script>
	--%>
  </head>
  
  <body>
     <div class="container-fluid">
    	<div class="row">
        	<div class="eui-btn"><a href="javascript:void(0)"><span class="glyphicon glyphicon-pencil"></span>更改布局</a><a href="javascript:void(0)" class="eui-del"><span class="glyphicon glyphicon-remove-sign"></span>整行删除</a></div>
        	<div class="col-sm-4">
            	<div class="eui-ibox eui-bdcolor" DR_drag="1" DR_replace="1">
                	<div class="eui-itt"><span>
                	<a href="javascript:void(0)"><img src="webpages/background/framework/images/icon.png"></a>
                	<a href="javascript:void(0)"><img src="webpages/background/framework/images/icon2.png"></a>
                	</span>申报办结</div>
                    <div class="eui-icon">
                    	<div style="height:230px;" id="mymain">
                    	
                        </div>
                    </div>
                    <i class="drag"></i>
                </div>
            </div>
            <div class="col-sm-8">
            	<div class="eui-ibox eui-bdcolor3" DR_drag="1" DR_replace="1">
                	<div class="eui-itt">
                	<span>
                	   <a href="javascript:void(0)"><img src="webpages/background/framework/images/icon.png"></a>
                	   <a href="javascript:void(0)"><img src="webpages/background/framework/images/icon2.png"></a>
                	</span>重大事件</div>
                    <div class="eui-icon">
                    	<div style="height:230px;" id="testtable">
                    	   
                        </div>
                    </div>
                    <i class="drag"></i>
                </div>
            </div>
        </div>
        <div class="row">
        	<div class="eui-btn"><a href="javascript:void(0)"><span class="glyphicon glyphicon-pencil"></span>更改布局</a><a href="javascript:void(0)" class="eui-del"><span class="glyphicon glyphicon-remove-sign"></span>整行删除</a></div>
        	<div class="col-sm-9">
            	<div class="eui-ibox" DR_drag="1" DR_replace="1">
                	<div class="eui-itt"><span><font>1小时前</font><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon1.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon2.png"></a></span>案件数量趋势图</div>
                    <div class="eui-icon">
                    	<div class="eui-idata1"><img src="webpages/background/framework/images/img1.png"></div>
                    </div>
                    <i class="drag"></i>
                </div>
            </div>
            <div class="col-sm-3">
            	<div class="eui-ibox eui-bdcolor3" DR_drag="1" DR_replace="1">
                	<div class="eui-itt"><span><font>1天前</font><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon1.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon2.png"></a></span>统计数据总览</div>
                    <div class="eui-icon">
                    	<div class="eui-idata1">
                        	<ul class="list-group eui-listgroup">
                            	<li class="list-group-item">
                                    <span class="badge">14</span>
                                    食品企业总数
                            	</li>
                                <li class="list-group-item">
                                    <span class="badge badge-success">10</span>
                                    食品证书总数
                            	</li>
                                <li class="list-group-item">
                                    <span class="badge badge-info">1</span>
                                    行政处罚总数
                            	</li>
                                <li class="list-group-item">
                                    <span class="badge badge-danger">6</span>
                                    行政检查总数
                            	</li>
                                <li class="list-group-item">
                                    <span class="badge badge-warning">7</span>
                                    抽检监测总数
                            	</li>
                                <li class="list-group-item">
                                    <span class="badge badge-warning">7</span>
                                    抽检监测总数
                            	</li>
                                <li class="list-group-item">
                                    <span class="badge badge-warning">7</span>
                                    抽检监测总数
                            	</li>
                                <li class="list-group-item">
                                    <span class="badge badge-warning">7</span>
                                    抽检监测总数
                            	</li>
                            </ul>
                        </div>
                    </div>
                    <i class="drag"></i>
                </div>
            </div>
        </div>
        <div class="row">
        	<div class="eui-btn"><a href="javascript:void(0)"><span class="glyphicon glyphicon-pencil"></span>更改布局</a><a href="javascript:void(0)" class="eui-del"><span class="glyphicon glyphicon-remove-sign"></span>整行删除</a></div>
        	<div class="col-sm-4">
            	<div class="eui-ibox eui-bdcolor3" DR_drag="1" DR_replace="1">
                	<div class="eui-itt"><span><font>1天前</font><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon1.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon2.png"></a></span>行政检查情况（周）</div>
                    <div class="eui-icon">
                    	<div class="eui-idata1"><img src="webpages/background/framework/images/img.png"></div>
                    </div>
                    <i class="drag"></i>
                </div>
            </div>
            <div class="col-sm-4">
            	<div class="eui-ibox eui-bdcolor3" DR_drag="1" DR_replace="1">
                	<div class="eui-itt"><span><font>1天前</font><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon1.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon2.png"></a></span>重大事件分布情况（舆情、召回）</div>
                    <div class="eui-icon">
                    	<div class="eui-idata1"><img src="webpages/background/framework/images/img.png"></div>
                    </div>
                    <i class="drag"></i>
                </div>
            </div>
            <div class="col-sm-4"> 
            	<div class="eui-ibox eui-bdcolor3" DR_drag="1" DR_replace="1">
                	<div class="eui-itt"><span><font>1天前</font><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon1.png"></a><a href="javascript:void(0)"><img src="webpages/background/framework/images/icon2.png"></a></span>信用评级情况</div>
                    <div class="eui-icon">
                    	<div class="eui-idata1"><img src="webpages/background/framework/images/img.png"></div>
                    </div>
                    <i class="drag"></i>
                </div>
            </div>
        </div>
        <div class="row">
        	<div class="col-sm-12">
            	<div class="eui-info"><span>「操作提示」</span>点击设计模式按钮进行主题定制</div>
            </div>
        </div>
        <div class="eui-clicksj">
        	<div class="btn-group dropup">
                <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">请选择使用主题 <span class="caret"></span></button>
                <ul class="dropdown-menu dropdown-menu-right" role="menu">
                    <li><a href="#">主题一</a></li>
                    <li><a href="#">主题二</a></li>
                    <li><a href="#">主题三</a></li>
                    <li><a href="#">主题四</a></li>
                </ul>
            </div>
            <a href="javascript:void(0)" class="eui-sjms">设计模式</a>
        </div>
    </div>
    <div class="eui-sjbtn" style="display:block;">
        <a href="javascript:void(0);" onclick="newTheme();" class="btn btn-success">新建主题</a><a href="#" class="btn btn-danger">删除主题</a>
        <div class="eui-theme"><input type="text" class="form-control pull-left"><a href="#" class="btn btn-primary">保存修改</a><a href="#" class="btn btn-info">增加一行</a></div>
        <a href="#" class="btn btn-success pull-right">退出设计模式</a>
    </div>
  </body>
</html>

<script type="text/javascript">

function newTheme(){
	top.PlatTab.refreshTab();
}

$(function(){
	/* WinMove()
	function WinMove() {
		var o = ".container-fluid",
		e = ".eui-btn",
		i = "[class*=col]";
		$(o).sortable({
			handle: e,
			connectWith: i,
			tolerance: "pointer",
			forcePlaceholderSize: !0,
			opacity: .8
		}).disableSelection();
	}
	// 调用插件
	$("body").dragMove({  
		limit: true,// 限制在窗口内  
		callback: function($move, $replace) {
			
		}  
	});   */
	/* $(".row").mouseenter(function(){
	 	$(this).addClass("eui-border");
		$(this).children(".eui-btn").show();
	});
	$(".row").mouseleave(function(){
	 	$(this).removeClass("eui-border");
		$(this).children(".eui-btn").hide();
	}); */
	
	PlatUtil.ajaxProgress({
		url:"appmodel/PortalThemeController.do?goTest",
		callback : function(resultText) {
			$("#mymain").html("");
			$("#mymain").append(resultText);
		}
	});
	
	/* PlatUtil.ajaxProgress({
		url:"appmodel/PortalThemeController.do?goTest2",
		callback : function(resultText) {
			$("#testtable").html("");
			$("#testtable").append(resultText);
		}
	}); */
	
});
</script>