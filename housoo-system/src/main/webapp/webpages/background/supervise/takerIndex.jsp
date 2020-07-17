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
    <title>承办人首页</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/iconfont/iconfont.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/comon.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/b.tabs.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/ftable.css"/>
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/style.css"/>
    <script type="text/javascript" src="webpages/background/supervise/assets/js/jquery-1.10.2.js"></script>
    <plattag:resources restype="js" loadres="jquery-ui,plat-util,layer,nicevalid,cryptojs">
    </plattag:resources>
    <script type="text/javascript" src="webpages/background/supervise/assets/js/echarts.min.js"></script>
    <script type="text/javascript" src="webpages/background/supervise/assets/My97DatePicker/WdatePicker.js"></script>
</head>
<body id="body">
<div class="main_content">
    <div class="main_r" id="mainFrameTabs">
        <div class="tab-content">
            <div class="main_header">
                <ul class="in_ul fl" id="searchUl">
                    <li class="active" id="year">年度</li>
                    <li id="quarter">季度</li>
                    <li id="month">月度</li>
                    <li id="days">近7天</li>
                </ul>
                <div class="fr" style="margin-top: 5px;">
                    <input type="text" class="Wdate w-col-l3" id="startDate"
                           onfocus="WdatePicker({firstDayOfWeek:1,dateFmt:'yyyy-MM-dd'})"/>
                    <span class="zhispan">至</span>
                    <input type="text" class="Wdate w-col-l3" id="endDate"
                           onfocus="WdatePicker({firstDayOfWeek:1,dateFmt:'yyyy-MM-dd'})"/>
                    <button type="button" class="search_btn" id="dateSearchBtn">搜索</button>
                </div>
            </div>
            <div class="tab-pane active" id="bTabs_navTabsMainPage">
                <div class="row">
                    <div class="col-3 fl orCor">
                        <div class="fl ml50">
                            <p>收到任务数量：</p>
                            <h2 id="all"></h2>
                        </div>
                        <div class="fr prodiv">
                            <div class="chart" id="pieChart1"></div>
                        </div>
                    </div>
                    <div class="col-3 fl yelCor">
                        <div class="fl ml50">
                            <p>正在办理数量：</p>
                            <h2 id="ing"></h2>
                        </div>
                        <div class="fr prodiv">
                            <div class="chart" id="pieChart2"></div>
                        </div>
                    </div>
                    <div class="col-3 fl blueCor">
                        <div class="fl ml50">
                            <p>办结数量：</p>
                            <h2 id="finish"></h2>
                        </div>
                        <div class="fr prodiv">
                            <div class="chart" id="pieChart3"></div>
                        </div>
                    </div>
                    <div class="col-3 fl redCor">
                        <div class="fl ml50">
                            <p>逾期数量：</p>
                            <h2 id="outdate"></h2>
                        </div>
                        <div class="fr prodiv">
                            <div class="chart" id="pieChart4"></div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-9 fl">
                        <div class="col9-t">
                            <div class="ched">
                                <%--<span>部门数量统计</span>--%>
                                <select class="fl" name="status">
                                    <option value="1,2,3,4,5,6,7,8,9,">全部督办</option>
                                    <option value="1,2,3,4,5,6,7,8,">在办督办</option>
                                    <option value="9">办结督办</option>
                                </select>
                            </div>
                            <%--<div class="chart" id="lineChart" style="width: 100%;height: 349px;top: 50px;"></div>--%>
                            <div class="chart" id="barChart" style="width: 100%;height: 349px;top: 50px;"></div>
                            <%--<div class="chart" id="barChart2"></div>--%>
                        </div>
                        <div class="col9-b">
                            <div class="table-wrap">
                                <div class="table-head">
                                    <div class="table-head-wrap">
                                        <table class="grid" id="headTable"></table>
                                    </div>
                                </div>
                                <div class="table-content">
                                    <table class="grid" id="dataTable">
                                        <tbody></tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-3 fl">
                        <div class="rhed">
                            <span>逾期统计</span>
                            <%--<button type="button" class="search_btn fr">搜索</button>--%>
                        </div>
                        <div class="chart" id="pieChart5" style="width: 100%;height: 350px;"></div>
                        <div class="cbom">
                            <ul>
                                <li>
                                    <p>督办确认</p>
                                    <p class="p2 color1" id="dbqr"></p>
                                </li>
                                <li>
                                    <p>办理反馈</p>
                                    <p class="p2 color2" id="blfk"></p>
                                </li>
                                <%--<li style="border-bottom: 0px">
                                    <p>落实反馈</p>
                                    <p class="p2 color3" id="lsfk"></p>
                                </li>--%>
                                <li style="border-bottom: 0px">
                                    <p>办结反馈</p>
                                    <p class="p2 color4" id="bjfk"></p>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="webpages/background/supervise/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/b.tabs.min.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/common.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/pieChart.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/barChart.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/pieCharts.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/pieChartsNew.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/takerIndex.js"></script>
</body>
</html>