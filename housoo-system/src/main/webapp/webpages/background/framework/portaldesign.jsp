<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
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

    <title>${portalTheme.THEME_NAME}-在线设计</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=9"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css"
                       loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,bootswitch,plat-ui,tipsy,layer,fancybox">
    </plattag:resources>
    <link rel="stylesheet" type="text/css" href="webpages/background/framework/css/portal.css">
    <style>
        .col-lg-1, .col-lg-10, .col-lg-11, .col-lg-12, .col-lg-2, .col-lg-3, .col-lg-4,
        .col-lg-5, .col-lg-6, .col-lg-7, .col-lg-8, .col-lg-9, .col-md-1, .col-md-10,
        .col-md-11, .col-md-12, .col-md-2, .col-md-3, .col-md-4, .col-md-5, .col-md-6,
        .col-md-7, .col-md-8, .col-md-9, .col-sm-1, .col-sm-10, .col-sm-11, .col-sm-12,
        .col-sm-2, .col-sm-3, .col-sm-4, .col-sm-5, .col-sm-6, .col-sm-7, .col-sm-8,
        .col-sm-9, .col-xs-1, .col-xs-10, .col-xs-11, .col-xs-12, .col-xs-2, .col-xs-3,
        .col-xs-4, .col-xs-5, .col-xs-6, .col-xs-7, .col-xs-8, .col-xs-9 {
            padding-right: 3px !important;
            padding-left: 3px !important;
        }
    </style>
    <plattag:resources restype="js"
                       loadres="jquery-ui,jqgrid,jedate,select2,jquery-layout,bootswitch,plat-util,tipsy,layer,slimscroll,fancybox,echart,superslide">
    </plattag:resources>
    <script type="text/javascript" src="webpages/background/framework/js/dragMove.js"></script>
</head>

<body>
<div class="container-fluid">
    <c:forEach items="${rowList}" var="row">
        <div class="row" rowid="${row.ROW_ID}" id="${row.ROW_ID}">
            <div class="eui-btn">
                <a href="javascript:void(0)" onclick="editRowInfo('${row.ROW_ID}');">
                    <span class="glyphicon glyphicon-pencil"></span>更改布局
                </a>
                <a href="javascript:void(0)" class="eui-del" onclick="delRowInfo('${row.ROW_ID}');">
                    <span class="glyphicon glyphicon-remove-sign"></span>整行删除
                </a>
            </div>
            <c:forEach items="${row.confList}" var="conf">
                <div class="col-sm-${conf.CONF_COLNUM}">
                    <div id="${conf.CONF_ID}_COMP" class="eui-ibox ${conf.CONF_BORDERCOLOR}" DR_drag="1" DR_replace="1">
                        <div class="eui-itt">
	                	<span>
	                	  <a href="javascript:void(0)" onclick="refreshComp('${conf.CONF_ID}','${conf.CONF_COMPURL}');"
                             title="重新刷新"><img src="webpages/background/framework/images/icon.png"></a>
	                	  <a href="javascript:void(0)" onclick="compConfig('${conf.CONF_ID}');"
                             style="display:${isAdminDesign=='true'?'':'none'};"
                             class="compconfig" title="组件配置"><img src="webpages/background/framework/images/icon2.png">
	                	   </a>
	                	  <c:if test="${conf.RES_MENUURL!=null&&conf.RES_MENUURL!=''}">
	                	  <a href="javascript:void(0)"
                             onclick="queryMore('${conf.RES_NAME}','${conf.RES_MENUICON}','${conf.RES_CODE}','${conf.RES_MENUURL}');"
                             title="查看更多">
	                	  <img src="webpages/background/framework/images/more.png">
	                	  </a>
                          </c:if>
	                	</span>
                            <div id="${conf.CONF_ID}_TITLE" style="font-size: 18px;">${conf.CONF_TITLE}</div>
                        </div>
                        <div class="eui-icon">
                            <div style="height: ${row.ROW_HEIGHT}px;" confurl="${conf.CONF_COMPURL}"
                                 confid="${conf.CONF_ID}" id="${conf.CONF_ID}">

                            </div>
                        </div>
                        <i class="drag"></i>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
    <div class="eui-clicksj">
        <%--
        <c:if test="${!(isAdminDesign=='true')}">
        <div class="btn-group dropup">
            <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">请选择使用主题 <span class="caret"></span></button>
            <ul class="dropdown-menu dropdown-menu-right" role="menu">
                <c:forEach items="${mythemeList}" var="theme">
                <li><a href="appmodel/PortalThemeController.do?goportal&THEME_ID=${theme.THEME_ID}">${theme.THEME_NAME}</a></li>
                </c:forEach>
            </ul>
        </div>
        </c:if>
        --%>
        <a href="javascript:void(0)" onclick="enterDesign();" class="eui-sjms">设计模式</a>
    </div>
</div>
<div class="eui-sjbtn" style="display:${isAdminDesign=='true'?'block':'none'};" id="DESIGN_TOOLBAR">

    <%--
    <c:if test="${isAdminDesign=='false'}">
    <a href="javascript:void(0);" onclick="newTheme('${portalTheme.THEME_ID}');"
        class="btn btn-success">新建主题</a>
    <a href="javascript:void(0);" onclick="deleteTheme('${portalTheme.THEME_ID}');" class="btn btn-danger">
    删除主题</a>
    </c:if>
    --%>
    <div class="eui-theme">
        <input type="hidden" name="THEME_NAME" value="${portalTheme.THEME_NAME}">
        <%--
        <input type="text" name="THEME_NAME"
            value="${portalTheme.THEME_NAME}" class="form-control pull-left">

        <a href="javascript:void(0);"
            onclick="saveTheme('${portalTheme.THEME_ID}');"
            class="btn btn-primary">保存修改</a>
        <a href="javascript:void(0)"
            onclick="addRow();" class="btn btn-info">增加一行</a>
        --%>
    </div>
    <a href="javascript:void(0);"
       onclick="saveTheme('${portalTheme.THEME_ID}');"
       class="btn btn-primary">保存修改</a>
    <a href="javascript:void(0)"
       onclick="addRow();" class="btn btn-info">增加一行</a>
    <a href="javascript:void(0);" onclick="exitDesign();" class="btn btn-success pull-right">退出设计模式</a>
</div>
</body>
</html>

<script type="text/javascript">

    function queryMore(RES_NAME, RES_MENUICON, RES_CODE, RES_MENUURL) {
        top.PlatTab.newTab({
            id: RES_CODE,
            title: RES_NAME,
            closed: true,
            icon: RES_MENUICON,
            url: RES_MENUURL
        });
    }

    function deleteTheme(themeId) {
        parent.layer.confirm("您确定要删除该主题吗?", {
            icon: 7,
            resize: false
        }, function () {
            PlatUtil.ajaxProgress({
                url: "appmodel/PortalThemeController.do?multiDel",
                params: {
                    selectColValues: themeId
                },
                callback: function (resultJson) {
                    if (resultJson.success) {
                        window.location.reload();
                    } else {
                        parent.layer.alert(PlatUtil.FAIL_MSG, {icon: 2, resize: false});
                    }
                }
            });
        }, function () {

        });
    }

    function exitDesign() {
        $("#DESIGN_TOOLBAR").attr("style", "display:none;");
        $(".row").unbind("mouseenter");
        $(".row").unbind("mouseleave");
        $(".drag").each(function () {
            $(this).attr("class", "nodrag");
        });
        $(".compconfig").attr("style", "display:none;");
    }

    function enterDesign() {
        $("#DESIGN_TOOLBAR").attr("style", "display:block;");
        $(".row").mouseenter(function () {
            $(this).addClass("eui-border");
            $(this).children(".eui-btn").show();
        });
        $(".row").mouseleave(function () {
            $(this).removeClass("eui-border");
            $(this).children(".eui-btn").hide();
        });
        $(".compconfig").attr("style", "");
        if ("${isAdminDesign}" == "false") {
            WinMove();
            $("body").dragMove({
                limit: true,// 限制在窗口内
                callback: function ($move, $replace) {

                }
            });
        }
        $(".nodrag").each(function () {
            $(this).attr("class", "drag");
        });

    }

    function newTheme(THEME_ID) {
        var THEME_NAME = $("input[name='THEME_NAME']").val();
        var rowList = $("div[rowid]");
        var rowJsonList = [];
        $(rowList).each(function (index, obj) {
            var rowObj = {};
            var ROW_ID = $(obj).attr("rowid");
            rowObj.ROW_ID = ROW_ID;
            var CONF_IDS = "";
            $("#" + ROW_ID).find("div[confid]").each(function (index, obj) {
                var confId = $(obj).attr("confid");
                if (index > 0) {
                    CONF_IDS += ",";
                }
                CONF_IDS += confId;
            });
            rowObj.CONF_IDS = CONF_IDS;
            rowJsonList.push(rowObj);
        });
        var rowJson = null;
        if (rowJsonList.length > 0) {
            rowJson = JSON.stringify(rowJsonList);
        }
        PlatUtil.ajaxProgress({
            url: "appmodel/PortalRowController.do?newTheme",
            params: {
                THEME_ID: THEME_ID,
                THEME_NAME: THEME_NAME,
                rowJson: rowJson
            },
            callback: function (resultJson) {
                if (resultJson.success) {
                    window.location.reload();
                } else {
                    parent.layer.alert(PlatUtil.FAIL_MSG, {icon: 2, resize: false});
                }
            }
        });
        //window.location.reload();
        //top.PlatTab.refreshTab();
    }

    function refreshComp(CONF_ID, CONF_COMPURL) {
        if (CONF_COMPURL) {
            PlatUtil.ajaxProgress({
                url: CONF_COMPURL,
                callback: function (resultText) {
                    $("#" + CONF_ID).html("");
                    $("#" + CONF_ID).append(resultText);
                }
            });
        }
    }

    function compConfig(CONF_ID) {
        PlatUtil.openWindow({
            title: "配置组件",
            area: ["800px", "350px"],
            content: "appmodel/PortalRowConfController.do?goForm&UI_DESIGNCODE=compconfform&CONF_ID=" + CONF_ID + "&THEME_ID=${portalTheme.THEME_ID}",
            end: function () {
                if (PlatUtil.isSubmitSuccess()) {
                    var compConf = PlatUtil.getData("compConf");
                    var CONF_COMPURL = compConf.CONF_COMPURL;
                    $("#" + CONF_ID + "_TITLE").text(compConf.CONF_TITLE);
                    $("#" + CONF_ID + "_COMP").attr("class", "eui-ibox " + compConf.CONF_BORDERCOLOR);
                    PlatUtil.ajaxProgress({
                        url: CONF_COMPURL,
                        callback: function (resultText) {
                            $("#" + CONF_ID).html("");
                            $("#" + CONF_ID).append(resultText);
                        }
                    });
                }
            }
        });
    }

    function saveTheme(THEME_ID) {
        var THEME_NAME = $("input[name='THEME_NAME']").val();
        var rowList = $("div[rowid]");
        var rowJsonList = [];
        $(rowList).each(function (index, obj) {
            var rowObj = {};
            var ROW_ID = $(obj).attr("rowid");
            rowObj.ROW_ID = ROW_ID;
            var CONF_IDS = "";
            $("#" + ROW_ID).find("div[confid]").each(function (index, obj) {
                var confId = $(obj).attr("confid");
                if (index > 0) {
                    CONF_IDS += ",";
                }
                CONF_IDS += confId;
            });
            rowObj.CONF_IDS = CONF_IDS;
            rowJsonList.push(rowObj);
        });
        var rowJson = null;
        if (rowJsonList.length > 0) {
            rowJson = JSON.stringify(rowJsonList);
        }
        PlatUtil.ajaxProgress({
            url: "appmodel/PortalRowController.do?updateRowSnAndTitle",
            params: {
                THEME_ID: THEME_ID,
                THEME_NAME: THEME_NAME,
                rowJson: rowJson,
                isAdminDesign: "${isAdminDesign}"
            },
            callback: function (resultJson) {
                if (resultJson.success) {
                    window.location.reload();
                } else {
                    parent.layer.alert(PlatUtil.FAIL_MSG, {icon: 2, resize: false});
                }
            }
        });
    }

    function editRowInfo(ROW_ID) {
        PlatUtil.openWindow({
            title: "编辑行信息",
            area: ["800px", "200px"],
            content: "appmodel/PortalRowController.do?goForm&UI_DESIGNCODE=portalrowform&ROW_ID=" + ROW_ID,
            end: function () {
                if (PlatUtil.isSubmitSuccess()) {
                    window.location.reload();
                }
            }
        });
    }

    function delRowInfo(ROW_ID) {
        parent.layer.confirm("您确定要删除该行吗?", {
            icon: 7,
            resize: false
        }, function () {
            PlatUtil.ajaxProgress({
                url: "appmodel/PortalRowController.do?multiDel&ROW_ID=" + ROW_ID,
                params: {
                    ROW_ID: ROW_ID
                },
                callback: function (resultJson) {
                    if (resultJson.success) {
                        window.location.reload();
                    } else {
                        parent.layer.alert(PlatUtil.FAIL_MSG, {icon: 2, resize: false});
                    }
                }
            });
        }, function () {

        });
    }

    function addRow() {
        var themeId = "${portalTheme.THEME_ID}";
        PlatUtil.openWindow({
            title: "添加行信息",
            area: ["800px", "200px"],
            content: "appmodel/PortalRowController.do?goForm&UI_DESIGNCODE=portalrowform&ROW_THEMEID=" + themeId,
            end: function () {
                if (PlatUtil.isSubmitSuccess()) {
                    window.location.reload();
                }
            }
        });
    }

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

    $(function () {
        if ("${isAdminDesign}" == "true") {
            $(".row").mouseenter(function () {
                $(this).addClass("eui-border");
                $(this).children(".eui-btn").show();
            });
            $(".row").mouseleave(function () {
                $(this).removeClass("eui-border");
                $(this).children(".eui-btn").hide();
            });
            WinMove();
            $("body").dragMove({
                limit: true,// 限制在窗口内
                callback: function ($move, $replace) {

                }
            });
        }
        $(".eui-listgroup").slimScroll({
            width: 'auto', //可滚动区域宽度
            height: 'auto', //可滚动区域高度
        });

        $("div[id][confurl]").each(function (index, obj) {
            var divId = $(obj).attr("id");
            var confurl = $(obj).attr("confurl");
            if (confurl) {
                PlatUtil.ajaxProgress({
                    url: confurl,
                    callback: function (resultText) {
                        $("#" + divId).html("");
                        $("#" + divId).append(resultText);
                    }
                });
            }
        });
    });
</script>