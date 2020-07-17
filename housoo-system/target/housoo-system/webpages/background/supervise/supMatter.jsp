<%@ page import="java.util.Map" %>
<%@ page import="com.housoo.platform.core.util.PlatAppUtil" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="plattag" uri="/plattag" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String clazz = (String) request.getAttribute("clazz");
    Map<String, Object> user = PlatAppUtil.getBackPlatLoginUser();
    String userId = user.get("SYSUSER_ID").toString();
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>事项督办</title>
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
    <input type="hidden" id="clazz" value="">
    <div class="fl mthed">
        <select name="statusSelect">
            <option value="1">全部督办</option>
            <option value="2">正在督办</option>
            <option value="3">待发督办</option>
            <option value="4">完成督办</option>
        </select>
        <img src="webpages/background/supervise/assets/images/sepline.png"/>
        <span id="superviseNum"></span>
    </div>
    <div class="fr oprbtnv" style="margin-top: 5px;">
        <button type="button" class="search_btn" id="superviseFormBtn">新建</button>
        <button type="button" class="search_btn" id="superviseFormStopBtn">终止</button>
        <img src="webpages/background/supervise/assets/images/sepline.png"/>
        <i class="iconfont icon-bianji"></i>
        <i class="iconfont icon-del"></i>
        <img src="webpages/background/supervise/assets/images/sepline.png"/>
        <i class="iconfont icon-sousuo"></i>
        <i class="iconfont icon-quanxianfenpei"></i>
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
        <div class="table-content tabhh mt10" id="superviseContent">

        </div>
    </div>
</div>
<script type="text/javascript" src="webpages/background/supervise/assets/js/b.tabs.min.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/common.js"></script>
<script type="text/javascript" src="webpages/background/supervise/assets/js/supMatter.js"></script>
</body>
</html>
<script>
    $(function () {
        var frameId = window.frameElement && window.frameElement.id || '';
        var clazz = "<%=clazz%>";
        var userId = "<%=userId%>";
        $("#clazz").val(clazz);
        init.initSuperviseData(userId, "", clazz);
        $("#superviseFormBtn").click(function () {
            PlatUtil.openWindow({
                title: "新建事项",
                area: ["1000px", "650px"],
                content: "supervise/SponsorController.do?goGenUiView&DESIGN_CODE=supervise_form&clazz=" + clazz + "&parentFrameId=" + frameId,
                end: function () {
                    if (PlatUtil.isSubmitSuccess()) {

                    }
                }
            })
        });
        $("#superviseFormStopBtn").click(function () {
            var frameId = window.frameElement && window.frameElement.id || '';
            var len = $('#dbsxtable').find("input[type='checkbox']:checked").length;
            if (len == 0) {
                parent.layer.msg("未选择任何数据！");
                return;
            }
            if (len > 1) {
                parent.layer.msg("只可以选择一行数据！");
                return;
            }
            var id = $('#dbsxtable').find("input[type='checkbox']:checked")[0].id.substr(4);
            PlatUtil.ajaxProgress({
                url: "/supervise/SponsorController.do?stopSupervise",
                type: "post",
                dataType: 'json',
                params: {
                    superviseId: id,
                    parentFrameId: frameId
                },
                callback: function (resultJson) {
                    if (resultJson.success) {
                        layer.msg("终止成功！");
                        window.parent.document.getElementById(resultJson.parentFrameId).contentWindow.location.reload(true);
                    } else {
                        layer.msg(resultJson.msg);
                    }
                }
            });
        });
        $("select[name='statusSelect']").change(function () {
            init.initSuperviseData(userId, this, clazz);
        });

    })
</script>