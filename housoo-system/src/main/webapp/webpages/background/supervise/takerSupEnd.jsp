<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="plattag" uri="/plattag" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    Map<String, Object> supervise = (Map<String, Object>) request.getAttribute("supervise");
    String jsonStr = JSON.toJSONString(supervise);
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>事项办结-详情(承办人)</title>
    <meta charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/iconfont/iconfont.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/comon.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/b.tabs.css">
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/ftable.css"/>
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/timeline.css"/>
    <link rel="stylesheet" type="text/css" href="webpages/background/supervise/assets/css/style.css"/>
    <script type="text/javascript" src="webpages/background/supervise/assets/js/jquery-1.10.2.js"></script>
    <plattag:resources restype="js" loadres="jquery-ui,plat-util,layer,nicevalid,cryptojs">
    </plattag:resources>
</head>
<script>
    var supervise = JSON.parse('<%=jsonStr%>');
</script>
<body id="body">
<div class="row">
    <div class="col-8 fl">
        <div class="rhed">
            <span>督办事项详情</span>
        </div>
        <div class="content">
            <div class="seone">
                <h2 class="h4">项目名称</h2>
                <dl>
                    <dt>
                        <img src="webpages/background/supervise/assets/images/yjwc.png"/>
                    </dt>
                    <dd class="dd1">
                        <p class="pp1">${supervise.STATUS==9 ? '已完结' : "办理中"}</p>
                        <p class="pp2">当前状态</p>
                    </dd>
                    <dt>
                        <img src="webpages/background/supervise/assets/images/bgt.png"/>
                    </dt>
                    <dd class="dd1">
                        <p class="pp1">${supervise['DEPART_NAME']}</p>
                        <p class="pp2">督办部门</p>
                    </dd>
                    <dt>
                        <img src="webpages/background/supervise/assets/images/time.png"/>
                    </dt>
                    <dd class="dd1">
                        <p class="pp1">${fn:substringBefore(supervise.CREATE_TIME,' ')}</p>
                        <p class="pp2">开始时间</p>
                    </dd>
                    <dt>
                        <img src="webpages/background/supervise/assets/images/time.png"/>
                    </dt>
                    <dd class="dd1">
                        <p class="pp1">
                            <c:if test="${not empty supervise.UPDATE_TIME}">
                                ${fn:substringBefore(supervise.UPDATE_TIME,' ')}
                            </c:if>
                            <c:if test="${empty supervise.UPDATE_TIME}">
                                --
                            </c:if>
                        </p>
                        <p class="pp2">结束时间</p>
                    </dd>
                </dl>
            </div>
            <div class="setwo">
                <ul class="setwo-topul">
                    <li class="active">基本信息</li>
                    <li>反馈内容</li>
                    <li>办结申请</li>
                </ul>
                <div class="tabinfo select">
                    <ul class="tabinfo-ul">
                        <li>
                            <p class="sp1">督办编号</p>
                            <p class="sp2">${supervise['SUPERVISE_NO']}</p>
                        </li>
                        <li>
                            <p class="sp1">督办类型</p>
                            <p class="sp2">${supervise['SUPERVISE_CLAZZ']}</p>
                        </li>
                        <li>
                            <p class="sp1">督办来源</p>
                            <p class="sp2">${supervise['SUPERVISE_SOURCE']}</p>
                        </li>
                        <li>
                            <p class="sp1">督办时限</p>
                            <p class="sp2">${supervise['HANDLE_LIMIT']}天</p>
                        </li>
                        <li>
                            <p class="sp1">关键字</p>
                            <p class="sp2">${supervise['KEYWORDS']}</p>
                        </li>
                        <li class="lastli">
                            <p class="sp1">督办标题</p>
                            <p class="sp2">${supervise['TITLE']}</p>
                        </li>
                        <li class="tsli">
                            <p class="sp1">督办内容</p>
                            <p class="sp2">${supervise['SUPERVISE_CONTENT']}</p>
                        </li>
                    </ul>
                </div>
                <div class="tabinfo">
                    <c:if test="${not empty blfk}">
                        <ul class="tabinfo-ul">
                            <li class="tsli">
                                <p class="sp1">办理反馈（${blfk['CREATE_TIME']}）</p>
                                <p class="sp2">${blfk['FEEDBACK_CONTENT']}</p>
                            </li>
                            <li class="tsli">
                                <p class="sp1">附件</p>
                                <c:if test="${not empty blfk.FILE_NAME}">
                                    <p class="sp2">${blfk['FILE_NAME']}
                                        <a href="../../../attachfiles/${blfk['FILE_URL']}"
                                           download="${blfk['FILE_NAME']}"
                                           style="text-decoration: none;color:red;"
                                           onmouseover="this.style.cssText='color:red;text-decoration: none;'"
                                           onmouseout="this.style.cssText='color:red;'"><span class="oprdown">下载</span></a>
                                    </p>
                                </c:if>
                                <c:if test="${empty blfk.FILE_NAME}">
                                    <p class="sp2">暂无附件</p>
                                </c:if>
                            </li>
                            <li class="tsli">
                                <p class="sp1">备注</p>
                                <p class="sp2">${blfk['REMARKS']}</p>
                            </li>
                        </ul>
                    </c:if>
                    <c:if test="${not empty lsfk}">
                        <ul class="tabinfo-ul">
                            <li class="tsli">
                                <p class="sp1">落实反馈（${lsfk['CREATE_TIME']}）</p>
                                <p class="sp2">${lsfk['FEEDBACK_CONTENT']}</p>
                            </li>
                            <li class="tsli">
                                <p class="sp1">附件</p>
                                <c:if test="${not empty lsfk.FILE_NAME}">
                                    <p class="sp2">${lsfk['FILE_NAME']}
                                        <a href="../../../attachfiles/${lsfk['FILE_URL']}"
                                           download="${lsfk['FILE_NAME']}"
                                           style="text-decoration: none;color:red;"
                                           onmouseover="this.style.cssText='color:red;text-decoration: none;'"
                                           onmouseout="this.style.cssText='color:red;'"><span class="oprdown">下载</span></a>
                                    </p>
                                </c:if>
                                <c:if test="${empty lsfk.FILE_NAME}">
                                    <p class="sp2">暂无附件</p>
                                </c:if>
                            </li>
                            <li class="tsli">
                                <p class="sp1">备注</p>
                                <p class="sp2">${lsfk['REMARKS']}</p>
                            </li>
                        </ul>
                    </c:if>

                </div>
                <div class="tabinfo">
                    <c:if test="${not empty bjfk}">
                        <ul class="tabinfo-ul">
                            <li class="tsli">
                                <p class="sp1">办结申请（${bjfk['CREATE_TIME']}）</p>
                                <p class="sp2">${bjfk['FEEDBACK_CONTENT']}</p>
                            </li>
                            <li class="tsli">
                                <p class="sp1">附件</p>
                                <c:if test="${not empty bjfk.FILE_NAME}">
                                    <p class="sp2">${bjfk['FILE_NAME']}
                                        <a href="../../../attachfiles/${bjfk['FILE_URL']}"
                                           download="${bjfk['FILE_NAME']}"
                                           style="text-decoration: none;color:red;"
                                           onmouseover="this.style.cssText='color:red;text-decoration: none;'"
                                           onmouseout="this.style.cssText='color:red;'"><span class="oprdown">下载</span></a>
                                    </p>
                                </c:if>
                                <c:if test="${empty bjfk.FILE_NAME}">
                                    <p class="sp2">暂无附件</p>
                                </c:if>
                            </li>
                            <li class="tsli">
                                <p class="sp1">备注</p>
                                <p class="sp2">${bjfk['REMARKS']}</p>
                            </li>
                        </ul>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
    <div class="col-4 fl">
        <div class="rhed">
            <span>质效统计</span>
        </div>
        <div class="content">
            <article class="zxtj" id="superviseProgress">
                <section class="seredy btmbig">
                    <span class="point-time point-red pointf">发</span>
                    <aside>
                        <p class="things">办公厅发起</p>
                        <p class="brief"><span class="text-gray">2020-02-26 10:20:30</span></p>
                    </aside>
                </section>
                <section class="seredy btmbig">
                    <span class="point-time point-red pointf">收</span>
                    <aside>
                        <p class="things">一室负责人接受</p>
                        <p class="brief"><span class="text-gray">2020-02-26 10:20:30</span></p>
                    </aside>
                    <p class="descp">逾期<span>1</span>次</p>
                </section>
                <section class="seredy btmbig">
                    <span class="point-time point-red pointf">办</span>
                    <aside>
                        <p class="things">办公厅督办</p>
                        <p class="brief"><span class="text-gray">2020-02-26 10:20:30</span></p>
                    </aside>
                    <p class="descp">驳回<span>1</span>次</p>
                </section>
                <section class="seredy btmbig">
                    <span class="point-time point-red pointf">结</span>
                    <aside>
                        <p class="things">一室办结</p>
                        <p class="brief"><span class="text-gray">2020-02-26 10:20:30</span></p>
                    </aside>
                </section>
            </article>
            <div class="pjdiv">
                <p class="ppj">评价：${not empty supervise.SUPERVISE_COMMENT ? supervise.SUPERVISE_COMMENT : "暂无评价"}</p>
                <%-- <textarea readonly></textarea>--%>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="webpages/background/supervise/assets/js/b.tabs.min.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/common.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/takerSupEnd.js"></script>
<script>
    $(function () {
        tab();
        initSuperviseProgress(supervise.RECORD_ID);
    })
    function tab() {
        var $li = $(".setwo-topul li");
        var $con = $(".tabinfo");
        $li.click(function (e) {
            var index = $(this).index();
            $li.removeClass("active");
            $(this).addClass("active");
            $con.removeClass("select");
            $con.eq(index).addClass("select");
        });
    }
</script>
</body>
</html>