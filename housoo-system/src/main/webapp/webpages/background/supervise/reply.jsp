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
    String isRead = (String) request.getAttribute("isRead");
    String nodeId = (String) request.getAttribute("nodeId");
    Map<String, Object> superviseInfo = (Map<String, Object>) request.getAttribute("superviseInfo");
    Map<String, Object> feedback = (Map<String, Object>) request.getAttribute("feedback");
    String restTime = "";
    //督办节点
    String currentNode = (String) superviseInfo.get("CURRENT_NODE");
    String dataStatus = (String) feedback.get("dataStatus");
    if ("3".equals(nodeId)) {
        //办理反馈剩余期限
        restTime = (String) superviseInfo.get("timeStatus1");
    }
    if ("4".equals(nodeId) || "5".equals(nodeId)) {
        //落实反馈剩余期限
        restTime = (String) superviseInfo.get("timeStatus2");
    }

    String jsonStr = JSON.toJSONString(superviseInfo);
    String jsonStrFeedback = JSON.toJSONString(feedback);
    String parentFrameId = (String) request.getAttribute("parentFrameId");
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>督办批复</title>
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
    var feedback = JSON.parse('<%=jsonStrFeedback%>');
    var parentFrameId = '<%=parentFrameId%>';
    $(function () {
        var currentNode = "<%=currentNode%>";
        var nodeId = "<%=nodeId%>";
        var str = "";
        var rest = "<%=restTime%>";
        $("#restTime").val(rest);
        if (1 <= currentNode) {
            str += "<li class=\"grn\">发</li>";
        } else {
            str += "<li>发</li>";
        }
        if (2 <= currentNode) {
            str += "<li class=\"grn\">收</li>";
        } else {
            str += "<li>收</li>";
        }

        if ( 3 == currentNode) {
            str += "<li class=\"crn\">办</li>";
        } else {
            if (4 < currentNode) {
                str += "<li class=\"grn\">办</li>";
            } else if (4 > currentNode) {
                str += "<li>办</li>";
            }
        }
        if (5 == currentNode) {
            str += "<li class=\"crn\">结</li>";
        } else {
            str += "<li>结</li>";
        }
        $(".inner_ul").html(str);

        //是否启用 回复意见框 只读功能
        var dataStatus = "<%=dataStatus%>";
        if (dataStatus === "1") {
            document.getElementById("replyAdvice").readOnly = true;
            $("#disagree").attr("disabled", "disabled");
            $("#agree").attr("disabled", "disabled");
        }
        //是否展示 反馈DIV
        if (nodeId === '1' || nodeId === '2') {
            document.getElementById("feedbackDiv").style.display = "none";
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
    <div class="col-3 fl repcol-3">
        <div class="rhed">
            <span>督办流程</span>
        </div>
        <div class="content">
            <article id="superviseProgress">

            </article>
        </div>
    </div>
    <div class="col-6 fl repcol-6">
        <div class="rhed">
            <span>督办详情</span>
        </div>
        <div class="forminfo">
            <form class="af-form">
                <div class="af-inner">
                    <label for="input-bh">督办编号：</label>
                    <input type="text" name="bh" id="input-bh" value="${superviseInfo.SUPERVISE_NO}" readonly>
                </div>
                <div class="af-inner">
                    <label>督办事项分类：</label>
                    <label style="text-align: left;width: 15%;">
                        <c:if test="${superviseInfo.NO_CUSTOM == 1}">
                            <input type="radio" name="lyfl" id="input-lyfl1" CHECKED disabled>
                        </c:if>
                        <c:if test="${superviseInfo.NO_CUSTOM != 1}">
                            <input type="radio" name="lyfl" id="input-lyfl1" disabled>
                        </c:if>
                        <span class="radio"></span>
                        <span class="item">自定义</span>
                    </label>
                    <label style="text-align: left;">
                        <c:if test="${superviseInfo.NO_CUSTOM == 2}">
                            <input type="radio" name="lyfl" id="input-lyfl2" CHECKED disabled>
                        </c:if>
                        <c:if test="${superviseInfo.NO_CUSTOM != 2}">
                            <input type="radio" name="lyfl" id="input-lyfl2" disabled>
                        </c:if>
                        <span class="radio"></span>
                        <span class="item">非自定义</span>
                    </label>
                </div>
                <div class="af-inner">
                    <label for="input-ly">督办来源：</label>
                    <input type="text" name="ly" id="input-ly" value="${superviseInfo.SUPERVISE_SOURCE}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-lx">督办类型：</label>
                    <input type="text" name="lx" id="input-lx" value="${superviseInfo.SUPERVISE_CLAZZ}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-sxfl">督办事项：</label>
                    <input type="text" name="ly" id="input-sxfl" value="${superviseInfo.SUPERVISE_ITEM}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-dbbm">督办部门：</label>
                    <input type="text" name="dbbm" id="input-dbbm" value="${superviseInfo.COMMIT_DEPART_NAME}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-cbbm">承办部门：</label>
                    <input type="text" name="cbbm" id="input-cbbm" value="${superviseInfo.TAKER_DEPART_NAME}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-bt">标题：</label>
                    <input type="text" name="bt" id="input-bt" value="${superviseInfo.TITLE}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-dbnr">督办内容：</label>
                    <textarea name="dbnr" id="input-dbnr" readonly>${superviseInfo.SUPERVISE_CONTENT}</textarea>
                </div>
                <div class="af-inner">
                    <label for="input-gjz">关键字：</label>
                    <input type="text" name="gjz" id="input-gjz" value="${superviseInfo.KEYWORDS}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-sx">办理时限：</label>
                    <input type="text" name="gjz" id="input-sx" value="${superviseInfo.HANDLE_LIMIT}日" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-gjz">附件：</label>
                    <div class="filedown">
                        <c:if test="${not empty superviseInfo.FILENAME}">
                            <img src="webpages/background/supervise/assets/images/files.png"/>
                            <span class="filename" style="width: 230px;"
                                  title="${superviseInfo.FILENAME}">${superviseInfo.FILENAME}</span>
                            <span class="oprdown" onclick="download('${superviseInfo.RECORD_ID}')">下载</span>
                        </c:if>
                        <c:if test="${empty superviseInfo.FILENAME}">
                            <span class="oprdown">暂无附件</span>
                        </c:if>
                    </div>
                </div>
                <div class="af-inner">
                    <label for="input-gjz">督办状态：</label>
                    <ul class="inner_ul">
                    </ul>
                </div>
            </form>
        </div>
    </div>
    <div class="col-3 fl repcol-3" id="feedbackDiv">
        <div class="rhed">
            <span>反馈</span>
        </div>
        <div class="forminfo">
            <form class="af-form fk-form">
                <div class="af-inner">
                    <label for="restTime">反馈时限：</label>
                    <input type="text" name="fk" id="restTime" value="${restTime}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-fknr">反馈内容：</label>
                    <textarea name="fknr" id="input-fknr" class="fkcon"
                              readonly>${feedback.FEEDBACK_CONTENT}</textarea>
                </div>

                <div class="af-inner">
                    <label for="input-gjz">附件：</label>
                    <div class="filedown">
                        <c:if test="${not empty feedback.FILE_NAME}">
                            <img src="webpages/background/supervise/assets/images/files.png"/>
                            <span class="filename" title="${feedback.FILE_NAME}">${feedback.FILE_NAME}</span>
                            <span class="oprdown" onclick="download('${feedback.FEEDBACK_ID}')">下载</span>
                        </c:if>
                        <c:if test="${empty feedback.FILE_NAME}">
                            <span class="oprdown">暂无附件</span>
                        </c:if>
                    </div>
                </div>
                <div class="af-inner">
                    <label for="input-bz">备注：</label>
                    <input type="text" name="bz" id="input-bz" value="${feedback.REMARKS}" readonly>
                </div>
                <div class="af-inner">
                    <button type="button" class="setyj ml28" onclick="setOptions()">设置常用意见</button>
                    <button type="button" class="setyj" onclick="chooseOption()">选择常用意见</button>
                </div>
                <div class="af-inner">
                    <label for="input-fknr">批复意见：</label>
                    <textarea name="fknr" id="replyAdvice" class="fkcon" value=""
                              placeholder="请填写批复意见">${feedback.REPLY_CONTENT}</textarea>
                </div>
                <div class="af-foot">
                    <button type="button" id="disagree" class="search_btn rebtn"
                            onclick="disAgree('2','${feedback.FEEDBACK_ID}','${nodeId}');">驳回
                    </button>
                    <button type="button" id="agree" class="search_btn"
                            onclick="agreeOption('1','${feedback.FEEDBACK_ID}','${nodeId}');">提交
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" src="webpages/background/supervise/assets/js/b.tabs.min.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/common.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/reply.js"></script>
</body>
</html>

<script>
    $(function () {
        //初始化督办流程
        initSuperviseProgress(supervise.RECORD_ID);
        $(".setyj").click(function () {
            $(".setyj").removeClass("active");
            $(this).addClass("active");
        });
        $(".repcol-3").height($("#feedbackDiv").height() - 10);
        $(".repcol-6").height($("#feedbackDiv").height());
    })
</script>
