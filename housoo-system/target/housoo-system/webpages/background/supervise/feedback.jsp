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
    String restTime1 = (String) request.getAttribute("restTime1");//办理反馈剩余期限
    String restTime2 = (String) request.getAttribute("restTime2");//落实反馈剩余期限
    Map<String, Object> supervise = (Map<String, Object>) request.getAttribute("supervise");
    String jsonStr = JSON.toJSONString(supervise);
    Map<String, Object> feedback = (Map<String, Object>) request.getAttribute("feedback");
    String parentFrameId = (String) request.getAttribute("parentFrameId");
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>督办反馈</title>
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
    <style>
        hr {
            margin-bottom: 15px;
            margin-top: 16px;
            border: none;
            background-color: #f0e7e6;
            height: 1px;
        }
    </style>
</head>
<script>
    var supervise = JSON.parse('<%=jsonStr%>');
    var nodeId = '<%=nodeId%>';
    var parentFrameId = '<%=parentFrameId%>';
    var isRead = '<%=isRead%>';
</script>
<body id="body">
<div class="row" style="margin-top: 0">
    <div class="col-3 fl repcol-3" style="margin-right: 3px;">
        <div class="rhed">
            <span>督办流程</span>
        </div>
        <div class="content">
            <article id="superviseProgress">
            </article>
        </div>
    </div>
    <div class="col-6 fl repcol-6" style="margin-right: 3px;">
        <div class="rhed">
            <span>督办详情</span>
        </div>
        <div class="forminfo">
            <form class="af-form">
                <div class="af-inner">
                    <label for="input-bh">督办编号：</label>
                    <input type="text" name="bh" id="input-bh" value="${supervise['SUPERVISE_NO']}" readonly>
                </div>
                <div class="af-inner">
                    <label>督办事项分类：</label>
                    <label style="text-align: left;width: 15%;">
                        <c:if test="${supervise.NO_CUSTOM == 1}">
                            <input type="radio" name="lyfl" id="input-lyfl1" CHECKED disabled>
                        </c:if>
                        <c:if test="${supervise.NO_CUSTOM != 1}">
                            <input type="radio" name="lyfl" id="input-lyfl1" disabled>
                        </c:if>
                        <span class="radio"></span>
                        <span class="item">自定义</span>
                    </label>
                    <label style="text-align: left;">
                        <c:if test="${supervise.NO_CUSTOM == 2}">
                            <input type="radio" name="lyfl" id="input-lyfl2" CHECKED disabled>
                        </c:if>
                        <c:if test="${supervise.NO_CUSTOM != 2}">
                            <input type="radio" name="lyfl" id="input-lyfl2" disabled>
                        </c:if>
                        <span class="radio"></span>
                        <span class="item">非自定义</span>
                    </label>
                </div>
                <div class="af-inner">
                    <label for="input-ly">督办来源：</label>
                    <input type="text" name="ly" id="input-ly" value="${supervise['SUPERVISE_SOURCE']}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-lx">督办类型：</label>
                    <input type="text" name="lx" id="input-lx" value="${supervise['SUPERVISE_CLAZZ']}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-sxfl">督办事项：</label>
                    <input type="text" name="ly" id="input-sxfl" value="${supervise['SUPERVISE_ITEM']}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-dbbm">督办部门：</label>
                    <input type="text" name="dbbm" id="input-dbbm" value="${supervise['DEPART_NAME']}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-cbbm">承办部门：</label>
                    <input type="text" name="cbbm" id="input-cbbm" value="${sessionScope.__BACK_PLAT_USER.DEPART_NAME}"
                           readonly>
                </div>
                <div class="af-inner">
                    <label for="input-bt">标题：</label>
                    <input type="text" name="bt" id="input-bt" value="${supervise['TITLE']}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-dbnr">督办内容：</label>
                    <textarea name="dbnr" id="input-dbnr" readonly>${supervise['SUPERVISE_CONTENT']}</textarea>
                </div>
                <div class="af-inner">
                    <label for="input-gjz">关键字：</label>
                    <input type="text" name="gjz" id="input-gjz" value="${supervise['KEYWORDS']}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-sx">办理时限：</label>
                    <input type="text" name="gjz" id="input-sx" value="${supervise['HANDLE_LIMIT']}日" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-gjz">附件：</label>
                    <div class="filedown">
                        <c:if test="${not empty supervise.FILENAME}">
                            <img src="webpages/background/supervise/assets/images/files.png"/>
                            <span class="filename" style="width: 200px;">${supervise['FILENAME']}</span>
                            <span class="oprdown" onclick="downloadSponsorFiles('${supervise['RECORD_ID']}')">下载</span>
                        </c:if>
                        <c:if test="${empty supervise.FILENAME}">
                            <span class="oprdown">暂无附件</span>
                        </c:if>
                    </div>
                </div>
                <div class="af-inner">
                    <label for="input-gjz">督办状态：</label>
                    <ul class="inner_ul" id="nodeUl">
                        <%--<li class="grn">发</li>
                        <li class="grn">收</li>
                        <li>办</li>
                        <li>结</li>--%>
                    </ul>
                </div>
            </form>
        </div>
    </div>
    <!--督办确认-->
    <div class="col-3 fl repcol-3" name="dbqr" style="display: none;margin-right: 3px;">
        <div class="rhed">
            <span>督办确认</span>
        </div>
        <div class="forminfo">
            <form class="af-form af-formr fk-form">
                <div class="af-inner">
                    <label for="input-fk">确认时限：</label>
                    <input type="text" name="fk" id="input-fk" value="${restTime4}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-bz">备注：</label>
                    <textarea name="bz" id="input-bz" class="fkcon"
                              placeholder="若拒收该督办任务，请在此处说明">${confirm["REMARKS"]}</textarea>
                </div>
                <div class="af-foot">
                    <button type="button" class="search_btn" onclick="confirmSupervise('确认拒收该督办任务？', '2');">拒收</button>
                    <button type="button" style="background-color: #1ab370" class="search_btn"
                            onclick="confirmSupervise('确认接收该督办任务？' ,'1');">接收
                    </button>
                </div>
            </form>
        </div>
    </div>
    <!--办理反馈-->
    <div class="col-3 fl repcol-3" name="blfk" style="display: none;margin-right: 3px;">
        <div class="rhed">
            <span>办理反馈</span>
        </div>
        <div class="forminfo">
            <form class="af-form af-formr fk-form">
                <div class="af-inner">
                    <label for="input-fk">反馈时限：</label>
                    <input type="text" name="fk" id="input-fk" value="${restTime1}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-fknr">反馈内容：</label>
                    <textarea name="fknr" id="input-fknr" class="fkcon"
                              placeholder="请填写反馈内容">${feedback['FEEDBACK_CONTENT']}</textarea>
                </div>
                <div class="af-inner">
                    <label for="input-scfj">上传附件：</label>
                    <input type="hidden" id="fileurl" value="${feedback['FILE_URL']}">
                    <input type="text" name="scfj" id="input-scfj" placeholder="请上传附件" value="${feedback['FILE_NAME']}">
                    <input type="file" id="upword" name="upword" accept=".pdf" style="opacity:0"
                           onchange="uploadFile(this)"/>
                </div>
                <div class="af-inner">
                    <label for="input-bz">备注：</label>
                    <input type="text" name="bz" id="input-bz" placeholder="请填写备注内容" value="${feedback['REMARKS']}">
                </div>
                <c:if test="${not empty feedback.REPLY_CONTENT}">
                    <hr>
                    <div class="af-inner">
                        <label for="input-fknr" style="color: #bc0c17;">批复意见：</label>
                        <textarea name="fknr" id="input-pfyj" class="fkcon" style="height: 120px;color: #bc0c17;"
                                  readonly>${feedback['REPLY_CONTENT']}</textarea>
                    </div>
                </c:if>
                <div class="af-foot">
                    <c:if test="${empty feedback}">
                        <button type="button" class="search_btn" onclick="feedback('确认提交？');">提交</button>
                    </c:if>
                    <c:if test="${not empty feedback}">
                        <button type="button" class="search_btn" onclick="feedback('确认重新提交？');">编辑</button>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
    <!--落实反馈-->
    <div class="col-3 fl repcol-3" name="lsfk" style="display: none;margin-right: 3px;">
        <div class="rhed">
            <span>落实反馈</span>
        </div>
        <div class="forminfo">
            <form class="af-form af-formr fk-form">
                <div class="af-inner">
                    <label for="input-fk">反馈时限：</label>
                    <input type="text" name="fk" id="input-fk" value="${restTime2}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-fknr">反馈内容：</label>
                    <textarea name="fknr" id="input-fknr" class="fkcon">${feedback['FEEDBACK_CONTENT']}</textarea>
                </div>
                <div class="af-inner">
                    <label for="input-scfj">上传附件：</label>
                    <input type="hidden" id="fileurl" value="${feedback['FILE_URL']}">
                    <input type="text" name="scfj" id="input-scfj" placeholder="请上传附件" value="${feedback['FILE_NAME']}">
                    <input type="file" id="upword" name="upword" accept=".pdf" style="opacity:0"
                           onchange="uploadFile(this)"/>
                </div>
                <div class="af-inner">
                    <label for="input-bz">备注：</label>
                    <input type="text" name="bz" id="input-bz" placeholder="请填写备注" value="${feedback['REMARKS']}">
                </div>
                <c:if test="${not empty feedback.REPLY_CONTENT}">
                    <hr>
                    <div class="af-inner">
                        <label for="input-fknr" style="color: #bc0c17;">批复意见：</label>
                        <textarea name="fknr" id="input-pfyj" class="fkcon" style="height: 120px;color: #bc0c17;"
                                  readonly>${feedback['REPLY_CONTENT']}</textarea>
                    </div>
                </c:if>
                <div class="af-foot">
                    <c:if test="${empty feedback}">
                        <button type="button" class="search_btn" onclick="feedback('确认提交？');">提交</button>
                    </c:if>
                    <c:if test="${not empty feedback}">
                        <button type="button" class="search_btn" onclick="feedback('确认重新提交？');">编辑</button>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
    <!--办结申请-->
    <div class="col-3 fl repcol-3" name="bjfk" style="display: none;margin-right: 3px;">
        <div class="rhed">
            <span>办结申请</span>
        </div>
        <div class="forminfo">
            <form class="af-form af-formr fk-form">
                <div class="af-inner">
                    <label for="input-fk">反馈时限：</label>
                    <input type="text" name="fk" id="input-fk" value="${restTime2}" readonly>
                </div>
                <div class="af-inner">
                    <label for="input-fknr">申请内容：</label>
                    <textarea name="fknr" id="input-fknr" class="fkcon"
                              placeholder="请填写申请内容">${feedback['FEEDBACK_CONTENT']}</textarea>
                </div>
                <div class="af-inner">
                    <label for="input-scfj">上传附件：</label>
                    <input type="hidden" id="fileurl" value="${feedback['FILE_URL']}">
                    <input type="text" name="scfj" id="input-scfj" placeholder="请上传附件" value="${feedback['FILE_NAME']}">
                    <input type="file" id="upword" name="upword" accept=".pdf" style="opacity:0"
                           onchange="uploadFile(this)"/>
                </div>
                <div class="af-inner">
                    <label for="input-bz">备注：</label>
                    <input type="text" name="bz" id="input-bz" placeholder="请填写备注" value="${feedback['REMARKS']}">
                </div>
                <c:if test="${not empty feedback.REPLY_CONTENT}">
                    <hr>
                    <div class="af-inner">
                        <label for="input-fknr" style="color: #bc0c17;">批复意见：</label>
                        <textarea name="fknr" id="input-pfyj" class="fkcon" style="height: 120px;color: #bc0c17;"
                                  readonly>${feedback['REPLY_CONTENT']}</textarea>
                    </div>
                </c:if>
                <div class="af-foot">
                    <c:if test="${empty feedback}">
                        <button type="button" class="search_btn" onclick="feedback('确认提交？');">提交</button>
                    </c:if>
                    <c:if test="${not empty feedback}">
                        <button type="button" class="search_btn" onclick="feedback('确认重新提交？');">编辑</button>
                    </c:if>
                </div>
            </form>
        </div>
    </div>
</div>
<script type="text/javascript" src="webpages/background/supervise/assets/js/b.tabs.min.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/common.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/feedback.js"></script>
</body>
</html>
<script>
    $(function () {
        //初始化督办流程
        initSuperviseProgress(supervise.RECORD_ID);
        if (nodeId == '1') {
            $(".col-3").width($("body").width()/3-10);
            $(".col-6").width($("body").width()/3*2-10);
        }
        if (nodeId == '2') {
            $("div[name='dbqr']").css("display", "");
            $("div[name='dbqr']").addClass("feedback");
            $("div[name='blfk']").css("display", "none");
            $("div[name='lsfk']").css("display", "none");
            $("div[name='bjfk']").css("display", "none");
        }
        if (nodeId == '3') {
            $("div[name='blfk']").css("display", "");
            $("div[name='blfk']").addClass("feedback");
            $("div[name='dbqr']").css("display", "none");
            $("div[name='lsfk']").css("display", "none");
            $("div[name='bjfk']").css("display", "none");
        }
        if (nodeId == '4') {
            $("div[name='blfk']").css("display", "none");
            $("div[name='lsfk']").css("display", "");
            $("div[name='lsfk']").addClass("feedback");
            $("div[name='dbqr']").css("display", "none");
            $("div[name='bjfk']").css("display", "none");
        }
        if (nodeId == '5') {
            $("div[name='blfk']").css("display", "none");
            $("div[name='lsfk']").css("display", "none");
            $("div[name='dbqr']").css("display", "none");
            $("div[name='bjfk']").css("display", "");
            $("div[name='bjfk']").addClass("feedback");
        }
        if (isRead == '1') {
            $(".feedback").find(".search_btn").attr("disabled", "disabled");
        }
        $(".feedback").find("#input-scfj").click(function () {
            $(".feedback").find("#upword").click();
        });
        $(".feedback").find("#upword").change(function () {
            var files = $(".feedback").find("#upword")[0].files[0];
            $(".feedback").find("#input-scfj").val(files.name);
        });
    })
    /**
     * 打包下载立项人上传的附件
     * @param superviseId
     */
    function downloadSponsorFiles(superviseId) {
        PlatUtil.ajaxProgress({
            traditional: true,
            url: "/supervise/SponsorController.do?downloadSponsorFile",
            type: "post",
            dataType: 'json',
            params: {
                recordId: superviseId
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
                    parent.layer.msg(resultJson.msg);
                }
            }
        });
    }
</script>