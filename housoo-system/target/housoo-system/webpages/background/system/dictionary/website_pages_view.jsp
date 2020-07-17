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
	<plattag:resources restype="css" loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,ztree,bootswitch,plat-ui,tipsy,autocomplete">
	</plattag:resources>
	<style type="text/css">
	.contentHead{
		margin-bottom: 18px;
	}
	.item_con_list{
    	color: #fff;
	}
	.item_con_list .con_list_item {
	    border: 1px solid #ededed;
	    color: #555;
	    margin-top: 18px;
	    height: 40px;
    	padding: 14px 15px 0;
	}
	.item_con_list .con_list_item a{
		color: #00b38a;
	    float: left;
	    margin: 0;
	    max-width: 270px;
	    overflow: hidden;
	    overflow-wrap: normal;
	    text-overflow: ellipsis;
	    white-space: nowrap;
	    text-decoration: none;
	}
	ul.filter-wrapper {
	    background-color: #fafafa;
	    border: 1px solid #ededed;
	    min-height: 36px;
	    padding: 6px 0;
	    position: relative;
	    list-style: outside none none;
    	margin: 0;
	}
	ul.filter-wrapper li::before, ul.filter-wrapper li::after {
    	content: "";
    	display: table;
	}
	ul.filter-wrapper li::after {
	    clear: both;
	}
	ul.filter-wrapper li::before, ul.filter-wrapper li::after {
	    content: "";
	    display: table;
	}
	ul.filter-wrapper li.li-taller {
	    left: 0;
	    padding: 12px 16px;
	    top: 0;
	    z-index: 10;
	}
	ul.filter-wrapper .details li {
    	padding: 6px 16px;
	}
	ul.filter-wrapper li {
	    color: #555;
	}
	ul.filter-wrapper li span {
	    font-weight: 600;
	    margin-right: -5px;
	}
	ul.filter-wrapper li a.active, ul.filter-wrapper li a:hover {
	    background-color: #00b38a;
	    color: #fff;
	}
	ul.filter-wrapper li span, ul.filter-wrapper li a, ul.filter-wrapper li .btn-more, ul.filter-wrapper li .btn-more-hy {
	    float: left;
	    height: 20px;
	    line-height: 13px;
	    margin-right: 5px;
	    padding: 5px 8px;
	    cursor: pointer;
	}
	.btn-collapse-wrapper {
	    margin-top: -1px;
	    position: relative;
	    text-align: center;
	}
	.btn-collapse-wrapper .btn-collapse.collapsed {
    	background-image: url("//www.lgstatic.com/www/static/search-result/modules/filter/img/arrow_down_861e6c6.png");
	}
	.btn-collapse-wrapper .btn-collapse {
	    -moz-border-bottom-colors: none;
	    -moz-border-left-colors: none;
	    -moz-border-right-colors: none;
	    -moz-border-top-colors: none;
	    background: #fafafa url("//www.lgstatic.com/www/static/search-result/modules/filter/img/arrow_up_b7a7618.png") no-repeat scroll center center;
	    border-color: #fafafa #ededed #ededed;
	    border-image: none;
	    border-style: solid;
	    border-width: 1px;
	    display: block;
	    height: 15px;
	    margin: 0 auto;
	    width: 36px;
	}
	ul.filter-wrapper a {
	    background-color: transparent;
	    color: #555;
	    text-decoration: none;
	}
	
	ul.filter-wrapper .multi-chosen .chosen .delete {
	    height: 11px;
	    position: absolute;
	    right: 5px;
	    top: 5px;
	    width: 11px;
	}
	ul.filter-wrapper .multi-chosen .chosen {
	    background-color: #00b38a;
	    color: #fff;
	    padding: 5px 22px 5px 8px;
	    position: relative;
	}
	
	ul.filter-wrapper li .btn-more, ul.filter-wrapper li .btn-more-hy {
	    background-color: transparent;
	    box-sizing: content-box;
	    color: #555;
	    float: right;
	    font-weight: 400;
	    margin-bottom: -6px;
	    margin-top: -6px;
	    padding-bottom: 11px;
	    padding-top: 11px;
	    position: relative;
	    z-index: 11;
	}
	
	ul.filter-wrapper li .btn-more i, ul.filter-wrapper li .btn-more-hy i {
	    border-color: #00b38a transparent transparent;
	    border-style: solid dashed;
	    border-width: 6px 6px 0;
	    font-size: 0;
	    height: 0;
	    overflow: hidden;
	    position: absolute;
	    right: -3px;
	    top: 15px;
	    transition: all 0.4s ease 0s;
	    width: 0;
	}
	ul.filter-wrapper .has-more {
	    position: relative;
	}
	ul.filter-wrapper li .btn-more:hover i, ul.filter-wrapper li .btn-more-hy:hover i {
	    animation-fill-mode: forwards;
	    transform: rotate(180deg);
	}
	ul.filter-wrapper .has-more .more, ul.filter-wrapper .has-more .more-hy {
	    background-color: #fff;
	    border: 1px solid #ededed;
	    display: none;
	    left: -1px;
	    margin-top: -1px;
	    position: absolute;
	    right: -1px;
	    top: 0;
	    z-index: 10;
	}
	ul.filter-wrapper .has-more .more.unfolded, ul.filter-wrapper .has-more .more-hy.unfolded {
	    display: block;
	}
	ul.filter-wrapper .has-more .more .other, ul.filter-wrapper .has-more .more-hy .other {
   		border-top: 1px dashed #ededed;
	}
	</style>
  </head>
  
  <body>
 	<div class="contentHead">
 		<ul class="filter-wrapper">
 			<li id="filterBrief" class="li-taller brief dn" style="display: none;">
				<span class="title">工作地点：</span>
				<a class="active" rel="nofollow" href="javascript:;">全国</a>
				<span class="title">工作经验：</span>
				<a class="active" rel="nofollow" href="javascript:;">不限</a>
				<span class="title">学历要求：</span>
				<a class="active" rel="nofollow" href="javascript:;">不限</a>
				<span class="title">融资阶段：</span>
				<a class="active" rel="nofollow" href="javascript:;">不限</a>
				<span class="title">行业领域：</span>
				<a class="active" rel="nofollow" href="javascript:;">不限</a>
			</li>
			<div id="filterCollapse" class="details" style="opacity: 1; height: 184px; display: block;">
				<li class="multi-chosen">
					<span class="title">工作经验：</span>
					<a class="active" href="javascript:;" >不限 </a>
					<a rel="nofollow" href="javascript:;" class="chosen">
					应届毕业生
					<i class="fa fa-times delete"></i>
					</a>
					<a rel="nofollow" href="javascript:;">
					3年及以下
					</a>
				</li>
				<div class="has-more hy-area">
					<li class="multi-chosen active">
						<span class="title">行业领域：</span>
						<a class="active"  href="javascript:;" >不限 </a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<span class="btn-more-hy" href="javascript:;" >
							更多 
							<i></i>
						</span>
					</li>
					<div class="more-hy more-fields">
						<li class="multi-chosen active">
						<span class="title">行业领域：</span>
						<a class="active"  href="javascript:;" >不限 </a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						</li>
						<li class="multi-chosen other">
						<a rel="nofollow" href="javascript:;">
						移动互联网
						<i class="delete"></i>
						</a>
						</li>
					</div>
				</div>
			</div>
 		</ul>
 		<div class="btn-collapse-wrapper">
			<a class="btn-collapse collapsed" rel="nofollow" title="点击展开筛选项" href="javascript:"></a>
		</div>
 	</div>
 	<div id="content"></div>
	<div id="pageDemo"></div>
  </body>
</html>
<plattag:resources restype="js" loadres="jquery-ui,jqgrid,jedate,select2,ztree,jquery-layout,bootswitch,plat-util,tipsy,autocomplete,pinyin">
</plattag:resources>
<script src="plug-in/laypage-1.3.0/laypage.js"></script>
<script type="text/javascript">
$(function(){
	jumpPage();
});


function jumpPage(curr){
	var rows = 5;
	PlatUtil.ajaxProgress({
		   url:"appmodel/DesignController.do?datagrid",
		   params:{
			   "page":curr || 1,
			   "rows":rows
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
	var appendHtml = "<ul class=\"item_con_list\">";
	for(var i=0;i<itemList.length;i++){
		appendHtml += "<li class=\"con_list_item\"><a>"+itemList[i].DESIGN_NAME+"</a></li>";
	}
	appendHtml += "</ul>";
	$("#content").html(appendHtml);
}

$(function(){
	$(".btn-more-hy").click(function(){
		var moreDiv = $(this).closest(".has-more").find("div.more-hy");
		if(moreDiv!=undefined){
			if($(moreDiv).hasClass("unfolded")){
				$(moreDiv).removeClass("unfolded");
			}else{
				$(moreDiv).addClass("unfolded");
			}
		}
	});
})
</script>
