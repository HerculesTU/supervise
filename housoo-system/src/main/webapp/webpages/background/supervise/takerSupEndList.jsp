<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="plattag" uri="/plattag" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>事项办结(承办人)</title>
    <meta charset="utf-8"/>
    <link type="text/css" rel="stylesheet" href="webpages/background/supervise/assets/jqGrid/ui.jqgrid.css">
    <link type="text/css" rel="stylesheet" href="webpages/background/supervise/assets/jqGrid/myjqGrid.css">
    <link type="text/css" rel="stylesheet" href="webpages/background/supervise/assets/jqGrid/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/iconfont/iconfont.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/comon.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/b.tabs.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/ftable.css"/>
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/timeline.css"/>
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/style.css"/>
    <script type="text/javascript" src="webpages/background/supervise/assets/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="webpages/background/supervise/assets/jqGrid/grid.locale-cn.js"></script>
    <script type="text/javascript" src="webpages/background/supervise/assets/jqGrid/jquery.jqGrid.js"></script>
    <script type="text/javascript" src="webpages/background/supervise/assets/jqGrid/myJqGridData.js"></script>
    <script type="text/javascript" src="webpages/background/supervise/assets/js/takerSupEndList.js"></script>
    <plattag:resources restype="js" loadres="jquery-ui,plat-util,layer,nicevalid,cryptojs">
    </plattag:resources>
</head>
<body id="body">
<div class="main_header">
    <div class="fl mthed">
        <select name="status">
            <option value="8">待办结申请</option>
            <option value="9">已办结</option>
        </select>
        <img src="webpages/background/supervise/assets/images/sepline.png"/>
        <span id="count">共0条</span>
    </div>
    <div class="fr oprbtnv" style="margin-top: 5px;">
        <button type="button" class="search_btn" id="searchBtn">查询</button>
        <%--<img src="webpages/background/supervise/assets/images/sepline.png"/>
        <i class="iconfont icon-bianji"></i>
        <i class="iconfont icon-del"></i>
        <img src="webpages/background/supervise/assets/images/sepline.png"/>
        <i class="iconfont icon-sousuo"></i>
        <i class="iconfont icon-funnel"></i>--%>
    </div>
    <ul class="fr sertij">
        <li class="fl">
            <span>关键字：</span>
            <input type="text" name="keywords"/>
        </li>
        <li class="fl">
            <span>标题：</span>
            <input type="text" name="title"/>
        </li>
    </ul>
</div>
<div class="row">
    <div class="list_jqgrid" id="tableContent">
        <table id="myTable" class="scroll" cellpadding="0" cellspacing="0"></table>
        <div id="pager" class="scroll"></div>
    </div>
</div>
<script type="text/javascript" src="webpages/background/supervise/assets/js/b.tabs.min.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/common.js"></script>
</body>
</html>