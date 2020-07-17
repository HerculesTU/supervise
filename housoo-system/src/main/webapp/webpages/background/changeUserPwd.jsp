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
    <title>用户个人信息</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=9"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <plattag:resources restype="css"
                       loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,fancybox,webuploader"></plattag:resources>
    <plattag:resources restype="js"
                       loadres="bootstrap-checkbox,jquery-ui,jqgrid,jedate,select2,nicevalid,fancybox,webuploader"></plattag:resources>
    <link href="webpages/background/framework/css/passwordRulesHelper.min.css" rel="stylesheet"/>
    <style>
        * {
            font-size: 14px;
        }

        .form-horizontal .control-label {
            width: 97px;
            padding-right: 0px;
            float: left;
            padding-left: 0px;
        }

        .form-horizontal .form-group .col-sm-10 {
            width: 330px;
            float: left;
        }

        #passForm input:focus {
            border: 1px solid #2196f3;
        }
    </style>
    <script src="webpages/background/framework/js/passwordRulesHelper.js" type="text/javascript"
            charset="utf-8"></script>
</head>

<body>
<script type="text/javascript">
    $(function () {
        PlatUtil.initUIComp();
    });
    function updatePassword() {
        if (PlatUtil.isFormValid("#passForm")) {
            var formData = PlatUtil.getFormEleData("passForm");
            PlatUtil.ajaxProgress({
                url: 'system/SysUserController.do?updatePass',
                params: formData,
                callback: function (resultJson) {
                    if (resultJson.success) {
                        parent.layer.alert("密码修改成功，请使用新密码重新登录！", {icon: 1, closeBtn: 0}, function () {
                            parent.location.href = "security/LoginController/backLogoff.do";
                        });
                        /*PlatUtil.setData("submitSuccess", true);
                         PlatUtil.closeWindow();
                         var href = window.top.location.href;
                         if (href.indexOf("login.jsp") == -1 &&
                         href.indexOf("backLogoff.do") == -1 && href.indexOf("goBackLogin.do") == -1) {
                         }
                         window.top.location.href = "security/LoginController/backLogoff.do";*/
                    } else {
                        parent.layer.alert(resultJson.msg, {icon: 2, resize: false});
                    }
                }
            });
        }
    }
</script>

<div class="plat-directlayout ui-layout-container" style="height: 100%; overflow: hidden; position: relative;"
     platundragable="true" compcode="direct_layout">
    <div class="ui-layout-south" platundragable="true" compcode="direct_layout">
        <div class="row" style="height:40px; background: #e5e5e5;" platundragable="true" compcode="buttontoolbar">
            <div class="col-sm-12 text-center">
                <button type="button" onclick="updatePassword();" platreskey=""
                        class="btn btn-outline btn-success btn-sm" style="font-size: 16px"><i class="fa fa-check"></i>确&nbsp;&nbsp;定
                </button>
            </div>
        </div>
    </div>
    <div class="ui-layout-center" platundragable="true" compcode="direct_layout">
        <form method="post" class="form-horizontal nice-validator n-red" compcode="formcontainer"
              action="system/SysUserController.do?updatePass" id="passForm" style="" novalidate="novalidate">
            <div class="form-group" compcode="formgroup">
                <label class="col-sm-2 control-label" id="INPUT_OLD_PASSWORD_LABEL"><font
                        class="redDot">*</font>原密码：</label>
                <div class="col-sm-10" id="INPUT_OLD_PASSWORD_DIV">
                    <input type="password" class="form-control" name="OLD_PASSWORD" auth_type="write" autocomplete="off"
                           label_value="原密码" value="" maxlength="20" datarule="required;onlyLetterNumberUnderLine;"
                           placeholder="请输入原密码" aria-required="true" datarule="required;"/>
                </div>
            </div>
            <div class="hr-line-dashed"></div>
            <div class="form-group" compcode="formgroup">
                <label class="col-sm-2 control-label" id="INPUT_NEW_PASSWORD_LABEL"><font
                        class="redDot">*</font>新密码：</label>
                <div class="col-sm-10" id="INPUT_NEW_PASSWORD_DIV">
                    <input type="password" class="form-control" name="NEW_PASSWORD" auth_type="write" autocomplete="off"
                           label_value="新密码" value="" maxlength="20" datarule="required;onlyLetterNumberUnderLine;"
                           placeholder="请输入新密码" aria-required="true"/>
                </div>
            </div>
            <div class="hr-line-dashed"></div>
            <div class="form-group" compcode="formgroup">
                <label class="col-sm-2 control-label" id="INPUT_CONFIRM_PASSWORD_LABEL"><font class="redDot">*</font>确认新密码：</label>
                <div class="col-sm-10" id="INPUT_CONFIRM_PASSWORD_DIV">
                    <input type="password" class="form-control" name="CONFIRM_PASSWORD" auth_type="write"
                           autocomplete="off" label_value="确认新密码" value="" maxlength="20" datarule="required;"
                           placeholder="请再次输入新密码" aria-required="true"/>
                </div>
            </div>
            <%--<div class="hr-line-dashed"></div>--%>
        </form>
    </div>

</div>

</body>
</html>
