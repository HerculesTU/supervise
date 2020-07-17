<%@ page language="java" import="com.housoo.platform.core.service.GlobalConfigService" pageEncoding="UTF-8" %>
<%@ page language="java" import="com.housoo.platform.core.service.SysUserService" %>
<%@ page language="java" import="com.housoo.platform.core.util.PlatAppUtil" %>
<%@ page language="java" import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="plattag" uri="/plattag" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    Map<String, Object> backUser = PlatAppUtil.getBackPlatLoginUser();
    SysUserService sysUserService = (SysUserService) PlatAppUtil.getBean("sysUserService");
    String userId = (String) backUser.get("SYSUSER_ID");
    Map<String, Object> sysUser = sysUserService.getRecord("PLAT_SYSTEM_SYSUSER",
            new String[]{"SYSUSER_ID"}, new Object[]{userId});
    String SYSUSER_MENUTYPE = (String) sysUser.get("SYSUSER_MENUTYPE");
    String SYSUSER_THEMECOLOR = (String) sysUser.get("SYSUSER_THEMECOLOR");
    if (StringUtils.isEmpty(SYSUSER_MENUTYPE)) {
        SYSUSER_MENUTYPE = "1";
    }
    if (StringUtils.isEmpty(SYSUSER_THEMECOLOR)) {
        SYSUSER_THEMECOLOR = "red";
    }
    request.setAttribute("SYSUSER_MENUTYPE", SYSUSER_MENUTYPE);
    request.setAttribute("SYSUSER_THEMECOLOR", SYSUSER_THEMECOLOR);
    request.getSession().setAttribute("SYSUSER_THEMECOLOR", SYSUSER_THEMECOLOR);
    String firstLogin = (String) request.getAttribute("firstLogin");
    //获取系统全局配置
    GlobalConfigService globalConfigService = (GlobalConfigService) PlatAppUtil.getBean("globalConfigService");
    Map<String, Object> globalConfig = globalConfigService.getFirstConfigMap();
    int month = Integer.parseInt(globalConfig.get("CONFIG_PWDOVERTIME").toString());
    String CONFIG_FIRST_LOGIN_MODIFY_PWD = globalConfig.get("CONFIG_FIRST_LOGIN_MODIFY_PWD").toString();
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>${sessionScope.globalProjectName}</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=9"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="${sessionScope.CONFIG_FAVICONURL}" type="image/x-icon"/>
    <link rel="shortcut icon" href="${sessionScope.CONFIG_FAVICONURL}" type="image/x-icon"/>
    <plattag:resources restype="css" loadres="bootstrap-submenu,plat-main"></plattag:resources>
    <script type="text/javascript" src="plug-in/jquery-slimscroll-1.3.3/jquery.slimscroll.js"></script>
    <script type="text/javascript" src="plug-in/jquery-accordion-menu-1.0/jquery-accordion-menu.js"></script>
    <link href="plug-in/jquery-accordion-menu-1.0/jquery-accordion-menu.css" rel="stylesheet"/>
    <link href="plug-in/toastr-1.0/css/toastr.min.css" rel="stylesheet"/>
    <link href="webpages/background/supervise/assets/iconfont/iconfont.css" rel="stylesheet"/>
    <link href="webpages/background/supervise/assets/css/comon.css" rel="stylesheet"/>
    <script type="text/javascript" src="plug-in/toastr-1.0/js/toastr.min.js"></script>
    <c:if test="${SYSUSER_THEMECOLOR!='red'}">
        <link href="plug-in/platform-1.0/css/plat-${SYSUSER_THEMECOLOR}.css" rel="stylesheet"/>
    </c:if>
    <style type="text/css">
        .plat-gray-bg {
            background-color: #f1f1f1;
            overflow: hidden;
        }

        .plat-wrapper-content {
            margin: 10px 10px 0px;
            padding: 0px;
        }

        .plat-Head {
            background: url(/webpages/background/supervise/assets/images/topbg.jpg) left center no-repeat !important;
        }

        .plat-Head .logo {
            padding: 0px 0px !important;
            float: left;
            text-align: center;
            line-height: 66px;
            font-size: 28px;
            color: #ffe375;
            font-weight: bold;
        }

        .plat-Head .logo img {
            height: 36px;
        }

        .plat-LeftNav {
            position: fixed;
            top: 66px;
            bottom: 19px;
            width: 200px;
            box-shadow: 0 0 5px rgba(0, 0, 0, .1);
            background: #fff;
        }

        .plat-body .lea-tabs {
            margin-left: 200px;
            width: auto;
        }

        .plat-body .content-iframe {
            padding-left: 200px;
            width: 100%;
        }
    </style>
</head>
<c:if test="${SYSUSER_MENUTYPE=='1'}">
<body style="overflow: hidden;" class="">
</c:if>
<c:if test="${SYSUSER_MENUTYPE=='2'}">
<body style="overflow: hidden;" class="plat-body">
</c:if>
<div id="ajax-loader"
     style="cursor: progress; position: fixed; top: -50%; left: -50%; width: 200%; height: 200%; background: #fff; z-index: 10000; overflow: hidden;">
    <img src="plug-in/platform-1.0/images/ajax-loader.gif"
         style="position: absolute; top: 0; left: 0; right: 0; bottom: 0; margin: auto;"/>
</div>
<div class="plat-Head">
    <div class="logo">
        <img src="${sessionScope.globalLogoUrl}"/>&nbsp;${sessionScope.globalProjectName}
    </div>
    <div class="left-bar" id="left-bar">
        <%--开始引入菜单界面 --%>
        <plattag:platbackmenu userId="${sessionScope.__BACK_PLAT_USER.SYSUSER_ID}"
                              subsyscode="${subsyscode}"></plattag:platbackmenu>
        <%--结束引入菜单界面 --%>
    </div>
    <div class="right-bar">
        <%--<ul>
            <li class="dropdown user user-menu"><a href="#"
                                                   class="dropdown-toggle" data-toggle="dropdown"> <img
                    src="plug-in/platform-1.0/images/default_user.jpg"
                    onerror="javascript: this.src = 'plug-in/platform-1.0/images/default_user.jpg'"
                    class="user-image" alt="User Image"> <span
                    class="hidden-xs">${sessionScope.__BACK_PLAT_USER.SYSUSER_NAME}</span>
            </a>
                <ul class="dropdown-menu pull-right">
                    <li><a href="javascript:void(0);" onclick="goUserInfo();"><i class="fa fa-user"></i>个人中心</a></li>
                    <li class="divider"></li>
                    <li><a onclick="PlatUtil.exitBackgroudSystem();"><i
                            class="ace-icon fa fa-power-off"></i>安全退出</a></li>
                </ul>
            </li>
        </ul>--%>
        <div class="header_r curpoint" onclick="PlatUtil.exitBackgroudSystem();" style="cursor: pointer">
            <i class="icon iconfont icon-icon" style="vertical-align: middle;"></i>
            <span>退出</span>
        </div>
        <div class="header_r mr20" onclick="goUserInfo();" style="cursor: pointer">
            <img class="userSty" src="webpages/background/supervise/assets/images/icon_user.png">
            <span>您好，${sessionScope.__BACK_PLAT_USER.SYSUSER_NAME}！</span>
        </div>
    </div>
</div>
<div class="lea-tabs">
    <div class="menuTabs">
        <div class="page-tabs-content">
            <a href="javascript:;" class="menuTab active"
               data-id="/Home/AdminPrettyDesktop"><i class="fa fa-home"></i>欢迎首页</a>
        </div>
    </div>
    <div class="tabs-right-tool">
        <button class="roll-nav tabLeft">
            <i class="fa fa fa-chevron-left"></i>
        </button>
        <button class="roll-nav tabRight">
            <i class="fa fa fa-chevron-right" style="margin-left: 3px;"></i>
        </button>
        <button class="roll-nav fullscreen" onclick="PlatUtil.fullscreen(this);">
            <i class="fa fa-arrows-alt"></i>
        </button>
        <div class="dropdown">
            <button class="roll-nav dropdown-toggle" data-toggle="dropdown">
                <i class="fa fa-gear "></i>
            </button>
            <ul class="dropdown-menu dropdown-menu-right"
                style="margin-top:40px">
                <li><a class="tabReload" href="javascript:;">刷新当前</a></li>
                <li><a class="tabCloseCurrent" href="javascript:;">关闭当前</a></li>
                <li><a class="tabCloseAll" href="javascript:;">全部关闭</a></li>
                <li><a class="tabCloseOther" href="javascript:;">除此之外全部关闭</a></li>
            </ul>
        </div>
    </div>
</div>
<div class="content-iframe plat-gray-bg">
    <div id="mainContent" class="lea-content  plat-wrapper-content">
        <iframe class="Plat_iframe" width="100%" height="100%"
                src="appmodel/PortalThemeController.do?goportal" frameborder="0"
                data-id="/Home/AdminPrettyDesktop"></iframe>
    </div>
</div>
<!-- 开始引入左侧菜单 -->
<c:if test="${SYSUSER_MENUTYPE=='2'}">
    <jsp:include page="/webpages/background/framework/main-leftmenu.jsp"></jsp:include>
</c:if>
<!-- 结束引入左侧菜单 -->

<div class="footer" id="rightCopyFoot">
    <%--<div style="float: left; width: 40%;">
        &nbsp;&nbsp;技术支持:&nbsp;信息中心&nbsp;&nbsp;&nbsp;&nbsp;联系方式:
    </div>--%>
    <div style="width: 100%; text-align: center;">
        &nbsp;&nbsp;技术支持:&nbsp;信息中心&nbsp;&nbsp;&nbsp;&nbsp;联系人:&nbsp;&nbsp;刘应钦&nbsp;&nbsp;&nbsp;&nbsp;联系方式:&nbsp;&nbsp;13834506841&nbsp;&nbsp;&nbsp;&nbsp;
    </div>
</div>

<div id="loading_background" class="loading_background"
     style="display: none;"></div>
<div id="loading_manage">正在拼了命为您加载…</div>
<%--<jsp:include page="/webpages/background/chatonline/chat.jsp"></jsp:include>--%>
</body>
</html>
<plattag:resources restype="js" loadres="bootstrap-submenu,jquery-cookie,plat-util,plat-tab,layer">
</plattag:resources>
<script>
    $(function () {
        PlatUtil.initPlatFrame();
        $("#jquery-accordion-menu").jqueryAccordionMenu();
        $(".jquery-accordion-menu>ul>li").click(function () {
            $(".jquery-accordion-menu>ul>li.active").removeClass("active")
            $(this).addClass("active");
        })
        //	二级
        $(".submenu>li").click(function () {
            $(".submenu>li").removeClass("active")
            $(this).addClass("active");
        });
        $(".jquery-accordion-menu").slimScroll({
            width: '200px',
            height: '100%',
            size: '6px',
            opacity: .2
        });
        var SYSUSER_MENUTYPE = "${SYSUSER_MENUTYPE}";
        if (SYSUSER_MENUTYPE == "2") {
            $("a[data-id]").click(function () {
                var menuId = $(this).attr("data-id");
                if (menuId != "/Home/AdminPrettyDesktop") {
                    $("li[topmenuid][topmenuid='" + menuId + "']").attr("style", "");
                    $("li[topmenuid][topmenuid!='" + menuId + "']").attr("style", "display:none;");
                }
            });
            $("li[resid]").click(function () {
                var resId = $(this).attr("resid");
                var resname = $(this).attr("resname");
                var resicon = $(this).attr("resicon");
                var resurl = $(this).attr("resurl");
                PlatTab.newTab({
                    id: resId,
                    title: resname,
                    closed: true,
                    icon: resicon,
                    url: resurl
                });
            });
        }

        var updatePassFlag = false;
        if ('<%=CONFIG_FIRST_LOGIN_MODIFY_PWD%>' == '1' && '<%=firstLogin%>' == 'yes') {
            updatePassFlag = true;
        }
        PlatUtil.ajaxProgress({
            url: "system/SysUserController.do?getPwdOverTime",
            async: "-1",
            showProgress: false,
            params: {userId: "<%=userId%>"},
            callback: function (resultJson) {
                if (resultJson.success) {
                    var month = resultJson.month;
                    //判断密码过期的话就提示用户修改密码
                    if (<%=month%> !=
                    0 && month >=
                    <%=month%>)
                    {
                        updatePassFlag = true;
                    }
                }
            }
        });
        if (updatePassFlag) {
            PlatUtil.openWindow({
                title: "修改密码",
                area: ["460px", "45%"],
                closeBtn: 0,
                content: "webpages/background/changeUserPwd.jsp",
                end: function () {

                }
            })
        }

    });

    function goUserInfo() {
        PlatTab.newTab({
            id: "userInfo",
            title: "个人中心",
            closed: true,
            icon: "fa fa-user",
            url: "system/SysUserController.do?goUserInfo"
        });
    }
    function canvasSupport() {
        return !!document.createElement('canvas').getContext;
    }
    if (!canvasSupport()) {
        window.open('<%=basePath%>framework/ViewController.do?browserDownload', '_self');
    }

    $(function () {


        PlatUtil.ajaxProgress({
            url: "system/SysMessageController.do?getSystemMessageList",
            async: "-1",
            showProgress: false,
            params: {},
            callback: function (resultJson) {
                if (resultJson.success) {
                    showSystemToastr(resultJson.messageList);
                } else {

                }

            }
        });
    });

    toastr.options = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-top-right",
        "showDuration": "1000",
        "hideDuration": "1000",
        "timeOut": "60000",
        "extendedTimeOut": "10000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut",
        "onclick": null
    }

    function showSystemToastr(messageList) {
        for (i = 0; i < messageList.length; i++) {
            (function (i) {
                toastr.options.onclick = function () {
                    showSystemMessage(messageList[i].SYSMESSAGE_ID);
                }
            })(i)
            if (messageList[i].SYSMESSAGE_TYPE == "1") {
                toastr.info(messageList[i].SYSMESSAGE_TITLE, "系统消息");
            } else if (messageList[i].SYSMESSAGE_TYPE == "2") {
                toastr.error(messageList[i].SYSMESSAGE_TITLE, "系统消息");
            } else if (messageList[i].SYSMESSAGE_TYPE == "3") {
                toastr.warning(messageList[i].SYSMESSAGE_TITLE, "系统消息");
            }

        }
    }

    function showSystemMessage(id) {
        PlatUtil.openWindow({
            title: "系统消息详情",
            area: ["1000px", "500px"],
            content: "system/SysMessageController.do?goForm&UI_DESIGNCODE=systemmessageform&SYSMESSAGE_ID=" + id,
            end: function () {

            }
        });
    }
</script>
