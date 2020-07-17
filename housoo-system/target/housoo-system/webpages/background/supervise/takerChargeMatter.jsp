<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="plattag" uri="/plattag" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String clazz = (String) request.getAttribute("clazz");
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>事项督办(承办部门负责人)</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/iconfont/iconfont.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/comon.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/b.tabs.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/ftable.css"/>
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/style.css"/>
    <script type="text/javascript" src="webpages/background/supervise/assets/js/jquery-1.10.2.js"></script>
    <plattag:resources restype="js" loadres="jquery-ui,plat-util,layer,nicevalid,cryptojs">
    </plattag:resources>
</head>
<body id="body">
<div class="main_header">
    <div class="fl mthed">
        <select name="status">
            <option value="1,2,3,4,5,6,7,8,9,">全部督办</option>
            <option value="1,2,3,4,5,6,7,8,">在办督办</option>
            <option value="9">办结督办</option>
        </select>
        <img src="webpages/background/supervise/assets/images/sepline.png"/>
        <span id="count">共0条</span>
    </div>
    <div class="fr oprbtnv" style="margin-top: 5px;">
        <%--<img src="webpages/background/supervise/assets/images/sepline.png"/>
        <i class="iconfont icon-bianji"></i>
        <img src="webpages/background/supervise/assets/images/sepline.png"/>
        <i class="iconfont icon-sousuo"></i>
        <i class="iconfont icon-quanxianfenpei"></i>--%>
    </div>
</div>
<div class="row">
    <div class="table-wrap">
        <div class="table-head">
            <div class="table-head-wrap">
                <table class="grid dbsxtt">
                    <thead>
                    <tr>
                        <th class="ts allcheck">
                            <input type="checkbox" disabled/>
                        </th>
                        <th>
                            <span class="tab-link">创建时间</span>
                        </th>
                        <th>
                            <span class="tab-link">流程</span>
                        </th>
                        <th>
                            <span class="tab-link">督办状态</span>
                        </th>
                        <th>
                            <span class="tab-link">部门</span>
                        </th>
                        <th>
                            <span class="tab-link">关键字</span>
                        </th>
                        <th>
                            <span class="tab-link">反馈状态</span>
                        </th>
                        <th>
                            <span class="tab-link">操作</span>
                        </th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
        <div class="table-content tabhh mt10">
            <table class="grid dbsxtable" id="dbsxtable">
            </table>
        </div>
    </div>
</div>
<script type="text/javascript" src="webpages/background/supervise/assets/js/b.tabs.min.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/common.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/takerChargeMatter.js"></script>
</body>
</html>
<script>
    var clazz = '<%=clazz%>';
    $(function () {
        loadTakerSuperviseList("<%=clazz%>");
    })
</script>