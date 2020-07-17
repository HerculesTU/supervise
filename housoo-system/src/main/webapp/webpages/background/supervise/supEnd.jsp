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
    String nodeId = (String) request.getAttribute("nodeId");
    Map<String, Object> supervise = (Map<String, Object>) request.getAttribute("supervise");
    Map<String, Object> endMap = (Map<String, Object>) request.getAttribute("endMap");
    Map<String, Object> feedbackInfo3 = (Map<String, Object>) request.getAttribute("feedbackInfo3");
    Map<String, Object> feedbackInfo4 = (Map<String, Object>) request.getAttribute("feedbackInfo4");
    String jsonStr = JSON.toJSONString(supervise);
    String jsonStrEndMap = JSON.toJSONString(endMap);
    String status = (String) endMap.get("status");
    String currentStatus = "";
    if ("1".equals(status)) {
        currentStatus = "办理中";
    }
    if ("2".equals(status)) {
        currentStatus = "完成";
    }
    if ("3".equals(status)) {
        currentStatus = "办理中";
    }
    String parentFrameId = (String) request.getAttribute("parentFrameId");
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>事项办结-详情(立项人)</title>
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
    var parentFrameId = '<%=parentFrameId%>';
    var currentStatus = '<%=currentStatus%>';
    $(function () {
        var feedback = JSON.parse('<%=jsonStrEndMap%>');
        var status = "<%=status%>";
        $("#currentStatus").html(currentStatus);
        if ("1" === status) {
            document.getElementById("feedbackDiv").style.display = "none";
        }
        if ("1" === status || "2" === status) {
            document.getElementById("replyAdvice").readOnly = true;
            $("#disagree").hide();
            $("#agree").hide();
        }
    });

    /**
     * 下载立项人提交附件
     */
    function download(recordId) {
        PlatUtil.ajaxProgress({
            traditional: true,
            url: "/supervise/SponsorController.do?downloadSponsorFile",
            type: "post",
            dataType: 'json',
            params: {
                recordId: recordId
            },
            callback: function (resultJson) {
                if (resultJson.success) {
                    var flag = true;
                    for (var i = 0; i < resultJson.list.length; i++) {
                        try {
                            var elemIF = document.createElement("iframe");
                            elemIF.src = resultJson.list[i];
                            elemIF.style.display = "none";
                            document.body.appendChild(elemIF);
                            $.ajax({
                                xhr: function () {
                                    var xhr = new window.XMLHttpRequest();
                                    xhr.addEventListener("progress", function (evt) {
                                        if (evt.lengthComputable) {
                                        }
                                    }, false);
                                    return xhr;
                                },
                                type: 'GET',
                                url: "/",
                                data: {},
                                success: function (data) {
                                },
                                error: function (data) {
                                    flag = false;
                                }
                            });
                        } catch (e) {
                            flag = false;
                        }
                    }
                } else {
                    layer.msg(resultJson.msg);
                }
            }
        });
    }
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
                        <p class="pp1" id="currentStatus"></p>
                        <p class="pp2">当前状态</p>
                    </dd>
                    <dt>
                        <img src="webpages/background/supervise/assets/images/bgt.png"/>
                    </dt>
                    <dd class="dd1">
                        <p class="pp1">${supervise.TAKER_DEPART_NAME}</p>
                        <p class="pp2">承办部门</p>
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
                        <p class="pp1">${fn:substringBefore(endMap.END_TIME,' ')}</p>
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
                            <p class="sp2">${supervise.SUPERVISE_NO}</p>
                        </li>
                        <li>
                            <p class="sp1">督办类型</p>
                            <p class="sp2">${supervise.SUPERVISE_CLAZZ}</p>
                        </li>
                        <li>
                            <p class="sp1">督办来源</p>
                            <p class="sp2">${supervise.SUPERVISE_SOURCE}</p>
                        </li>
                        <li>
                            <p class="sp1">督办时限</p>
                            <p class="sp2">${supervise.HANDLE_LIMIT}日</p>
                        </li>
                        <li>
                            <p class="sp1">关键字</p>
                            <p class="sp2">${supervise.KEYWORDS}</p>
                        </li>
                        <li class="lastli">
                            <p class="sp1">督办标题</p>
                            <p class="sp2">${supervise.TITLE}</p>
                        </li>
                        <li class="tsli">
                            <p class="sp1">督办内容</p>
                            <p class="sp2">${supervise.SUPERVISE_CONTENT}</p>
                        </li>
                    </ul>
                </div>
                <div class="tabinfo">
                    <ul class="tabinfo-ul bline">
                        <li class="tsli">
                            <p class="sp1">办理反馈（${feedbackInfo3.CREATE_TIME}）</p>
                            <p class="sp2">${feedbackInfo3.FEEDBACK_CONTENT}</p>
                        </li>

                        <li class="tsli">
                            <p class="sp1">附件</p>
                            <c:if test="${not empty feedbackInfo3.FILE_NAME}">
                                <p class="sp2">${feedbackInfo3['FILE_NAME']}
                                    <button type="button" class="xzfj"
                                            onclick="download('${feedbackInfo3.FEEDBACK_ID}')">下载
                                    </button>
                                </p>
                            </c:if>
                            <c:if test="${empty feedbackInfo3.FILE_NAME}">
                                <p class="sp2">暂无附件</p>
                            </c:if>
                        </li>
                        <li class="tsli">
                            <p class="sp1">备注</p>
                            <p class="sp2">${feedbackInfo3.REMARKS}</p>
                        </li>
                    </ul>
                    <%--<ul class="tabinfo-ul">
                        <li class="tsli">
                            <p class="sp1">落实反馈（${feedbackInfo4.CREATE_TIME}）</p>
                            <p class="sp2">${feedbackInfo4.FEEDBACK_CONTENT}</p>
                        </li>

                        <li class="tsli">

                            <p class="sp1">附件</p>

                            <c:if test="${not empty feedbackInfo4.FILE_NAME}">
                                <p class="sp2">${feedbackInfo4['FILE_NAME']}
                                    <button type="button" class="xzfj"
                                            onclick="download('${feedbackInfo4.FEEDBACK_ID}')">下载
                                    </button>
                                </p>
                            </c:if>
                            <c:if test="${empty feedbackInfo4.FILE_NAME}">
                                <p class="sp2">暂无附件</p>
                            </c:if>

                        </li>

                        <li class="tsli">
                            <p class="sp1">备注</p>
                            <p class="sp2">${feedbackInfo4.REMARKS}</p>
                        </li>
                    </ul>--%>
                </div>
                <div class="tabinfo" id="feedbackDiv">
                    <ul class="tabinfo-ul">
                        <li class="tsli">
                            <p class="sp1">办结申请（${not empty endMap.CREATE_TIME ? endMap.CREATE_TIME : "--"}）</p>
                            <p class="sp2">${endMap.FEEDBACK_CONTENT}</p>
                        </li>
                        <li class="tsli">
                            <p class="sp1">附件</p>

                            <c:if test="${not empty endMap.FILE_NAME}">
                                <p class="sp2">${endMap['FILE_NAME']}
                                    <button type="button" class="xzfj" onclick="download('${endMap.RECORD_ID}')">下载
                                    </button>
                                </p>
                            </c:if>
                            <c:if test="${empty endMap.FILE_NAME}">
                                <p class="sp2">暂无附件</p>
                            </c:if>

                        </li>
                        <li class="tsli">
                            <p class="sp1">备注</p>
                            <p class="sp2">${endMap.REMARKS}</p>
                        </li>
                    </ul>
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

            </article>
            <div class="pjdiv">
                <p class="ppj">评价：</p>
                <textarea id="replyAdvice">${endMap.REPLY_CONTENT}</textarea>
                <button type="button" id="disagree" class="search_btn rebtn"
                        onclick="reply('${supervise.RECORD_ID}','2','${endMap.RECORD_ID}','${nodeId}','${supervise.TASK_ID}');">
                    驳回
                </button>
                <button type="button" id="agree" class="search_btn btn"
                        onclick="reply('${supervise.RECORD_ID}','1','${endMap.RECORD_ID}','${nodeId}','${supervise.TASK_ID}')">
                    提交
                </button>

            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="webpages/background/supervise/assets/js/b.tabs.min.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/common.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/supEnd.js"></script>
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