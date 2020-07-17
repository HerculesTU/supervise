$(function () {
    $(".tabhh").height($(window).height() - $(".main_header").height() - 76);
});

function bindEvent() {
    /*点击收缩*/
    $(".nameo").click(function () {

        var self = $(this),
            oDiv = self.parent().parent().next('.e_con'),
            src = self.find(".e_h_r").attr('class');
        if (src.indexOf('icon-xiajiantou') != -1) {
            self.find(".e_h_r").removeClass("icon-xiajiantou");
            self.find(".e_h_r").addClass("icon-youjiantou1");
        } else {
            self.find(".e_h_r").removeClass("icon-youjiantou1");
            self.find(".e_h_r").addClass("icon-xiajiantou");
        }
        oDiv.slideToggle();
    });
    /*编辑事件*/
    $(".icon-bianji").click(function () {
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
            url: "/supervise/SponsorController.do?findSuperviseInfoById",
            type: "post",
            dataType: 'json',
            params: {
                id: id
            },
            callback: function (resultJson) {
                if (typeof (resultJson.SUPERVISE_NO) == "undefined" || resultJson.SUPERVISE_NO == "" || resultJson.SUPERVISE_NO == null || resultJson.SUPERVISE_NO == "null") {
                    PlatUtil.openWindow({
                        title: "新建事项",
                        area: ["1000px", "650px"],
                        content: "supervise/SponsorController.do?goGenUiViewDraft&DESIGN_CODE=supervise_form&recordId=" + id + "&parentFrameId=" + frameId,
                        end: function () {
                            PlatUtil.closeWindow();
                        }
                    })
                } else {
                    layer.msg("只可以操作草稿状态的任务！");
                }
            }
        })
    })
    /*删除事件*/
    $(".icon-del").click(function () {
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
            url: "/supervise/SponsorController.do?findSuperviseInfoById",
            type: "post",
            dataType: 'json',
            params: {
                id: id
            },
            callback: function (resultJson) {
                if (typeof (resultJson.SUPERVISE_NO) == "undefined" || resultJson.SUPERVISE_NO == "" || resultJson.SUPERVISE_NO == null || resultJson.SUPERVISE_NO == "null") {
                    PlatUtil.ajaxProgress({
                        url: "/supervise/SuperviseController.do?multiDel",
                        type: "post",
                        dataType: 'json',
                        params: {
                            selectColValues: id,
                            parentFrameId: frameId
                        },
                        callback: function (resultJson) {
                            if (resultJson.success) {
                                window.parent.document.getElementById(resultJson.parentFrameId).contentWindow.location.reload(true);
                                layer.msg("删除成功！");
                            } else {
                                window.parent.document.getElementById(resultJson.parentFrameId).contentWindow.location.reload(true);
                                layer.msg("删除失败！");
                            }
                        }
                    })
                } else {
                    layer.msg("只可以操作草稿状态的任务！");
                }
            }
        })
    })
}


var init = {
    /**
     * 页面加载数据
     * @param clazz
     */
    initSuperviseData: function (userId, obj, clazz) {
        var selected = "";
        if (typeof (obj) == "undefined" || obj == "" || obj == null || obj == "null") {
            selected = "1";
        } else {
            selected = $(obj).children('option:selected').attr("value");
        }
        var str = "";
        PlatUtil.ajaxProgress({
            url: "/supervise/SuperviseController.do?getSuperviseInfo",
            type: "post",
            dataType: 'json',
            params: {
                userId: userId,
                clazzId: clazz,
                status: selected
            },
            callback: function (resultJson) {
                if (resultJson) {
                    var superviseInfo = resultJson.superviseInfo;
                    var numStr = "共" + superviseInfo.length + "条";
                    $("#superviseNum").html(numStr);
                    if (superviseInfo) {
                        str += "<table class=\"grid dbsxtable\" id=\"dbsxtable\">";
                        for (var i = 0; i < superviseInfo.length; i++) {
                            str += "<thead> <tr> <th class=\"ts\"><input type=\"checkbox\" id=\"chk_" + superviseInfo[i][0].RECORD_ID + "\"/></th>";
                            str += "<th colspan=\"6\" class=\"nameo\">  <input type=\"hidden\" value=\"a\"" + "/>";
                            str += "<span>" + superviseInfo[i][0].TITLE + "</span>";
                            str += " <i class=\"iconfont icon-xiajiantou e_h_r\" style=\"font-size: 10px;margin: 5px;\"></i>";
                            if (superviseInfo[i][0].SUPERVISE_CLAZZ.indexOf("日常") != '-1') {
                                str += '<span class="level levcor3">' + superviseInfo[i][0].SUPERVISE_CLAZZ + '</span></th>';
                            } else if (superviseInfo[i][0].SUPERVISE_CLAZZ.indexOf("重点") != '-1') {
                                str += '<span class="level levcor2">' + superviseInfo[i][0].SUPERVISE_CLAZZ + '</span></th>';
                            } else if (superviseInfo[i][0].SUPERVISE_CLAZZ.indexOf("重大") != '-1') {
                                str += '<span class="level levcor1">' + superviseInfo[i][0].SUPERVISE_CLAZZ + '</span></th>';
                            }
                            str += "<th class=\"lefto\">" + (superviseInfo[i][0].SUPERVISE_SOURCE ? superviseInfo[i][0].SUPERVISE_SOURCE : '--') + '：' + Tools.removeNullOrUndefined(superviseInfo[i][0].SUPERVISE_NO) + "</th> </tr> </thead> ";
                            str += "<tbody class=\"e_con\">";
                            for (var j = 0; j < superviseInfo[i].length; j++) {
                                var num = j + 1;
                                if (superviseInfo[i][j].DRAFT === "1") {

                                    str += "<tr><td>" + num + "</td><td>" + Tools.removeNullOrUndefined(superviseInfo[i][j].CREATE_TIME) + "</td>";
                                    str += "<td></td><td></td><td></td><td>" + Tools.removeNullOrUndefined(superviseInfo[i][j].KEYWORDS) + "</td><td></td>"
                                    str += '<td><button type="button" class="tbtn" id="" onclick="checkSuperviseDraftInfo(\'' + superviseInfo[i][j].RECORD_ID + '\');">查看</button></td></tr>';
                                } else {
                                    str += "<tr><td>" + num + "</td><td>" + Tools.removeNullOrUndefined(superviseInfo[i][j].CREATE_TIME) + "</td>";
                                    str += "<td>" + Tools.removeNullOrUndefined(superviseInfo[i][j].NODE_NAME) + "</td>";

                                    if ("完成" === superviseInfo[i][j].timeStatus) {
                                        str += '<td><span class="dbstate staCor1">' + superviseInfo[i][j].timeStatus + '</span></td>';
                                    }
                                    if ("办理中" === superviseInfo[i][j].timeStatus) {
                                        str += '<td><span class="dbstate staCor2">' + superviseInfo[i][j].timeStatus + '</span></td>';
                                    }
                                    if ("未办理" === superviseInfo[i][j].timeStatus) {
                                        str += '<td><span class="dbstate staCor3">' + superviseInfo[i][j].timeStatus + '</span></td>';
                                    }
                                    if ("逾期" === superviseInfo[i][j].timeStatus) {
                                        str += '<td><span class="dbstate staCor4">' + superviseInfo[i][j].timeStatus + '</span></td>';
                                    }

                                    if (j === 0) {
                                        str += "<td>" + Tools.removeNullOrUndefined(superviseInfo[i][j].COMMIT_DEPART_NAME) + "</td>";
                                    } else {
                                        str += "<td>" + Tools.removeNullOrUndefined(superviseInfo[i][j].TAKER_DEPART_NAME) + "</td>";
                                    }
                                    str += "<td>" + Tools.removeNullOrUndefined(superviseInfo[i][j].KEYWORDS) + "</td>";

                                    if (superviseInfo[i][j].feedbackStatus == '1') {
                                        str += '<td><img src="webpages/background/supervise/assets/images/right.png"></td>';
                                    } else {
                                        str += '<td><img src="webpages/background/supervise/assets/images/error.png"></td>';
                                    }

                                    //当前任务已终止
                                    if ("1" === superviseInfo[i][j].stopStatus) {
                                        str += '<td><button type="button" class="tbtn">任务已终止</button></td></tr>';
                                    } else {
                                        if ("9" == superviseInfo[i][j].STATUS) {
                                            str += '<td><button type="button" class="tbtn"  onclick="viewSuperviseEnd(\'' + superviseInfo[i][j].RECORD_ID + '\'' +
                                                ',' + '\'' + superviseInfo[i][j].NODE_ID + '\');">查看</button></td></tr>';
                                        } else {
                                            if ("5" === superviseInfo[i][j].NODE_ID) {
                                                if ("完成" === superviseInfo[i][j].timeStatus || "办理中" === superviseInfo[i][j].timeStatus || "逾期" === superviseInfo[i][j].timeStatus) {
                                                    str += '<td><button type="button" class="tbtn"  onclick="viewSuperviseEnd(\'' + superviseInfo[i][j].RECORD_ID + '\'' +
                                                        ',' + '\'' + superviseInfo[i][j].NODE_ID + '\');">查看</button></td></tr>';
                                                }
                                                if ("未办理" === superviseInfo[i][j].timeStatus) {
                                                    str += '<td><button type="button" disabled class="tbtn" >--</button></td></tr>';
                                                }
                                            } else {
                                                if ("完成" === superviseInfo[i][j].timeStatus || "办理中" === superviseInfo[i][j].timeStatus || "逾期" === superviseInfo[i][j].timeStatus) {
                                                    str += '<td><button type="button" class="tbtn"  onclick="checkSuperviseInfo(\'' + superviseInfo[i][j].RECORD_ID + '\'' +
                                                        ',' + '\'' + superviseInfo[i][j].NODE_ID + '\');">查看</button></td></tr>';
                                                }
                                                if ("未办理" === superviseInfo[i][j].timeStatus) {
                                                    str += '<td><button type="button" disabled class="tbtn" >--</button></td></tr>';
                                                }
                                            }
                                        }
                                    }

                                }

                            }
                            str += "</tbody>";
                        }
                        str += "</table>";
                    }
                    $("#superviseContent").html(str);
                    bindEvent();
                }
            }
        });
    },

};

/**
 * 查看详情页
 */
function checkSuperviseInfo(id, nodeId) {
    var frameId = window.frameElement && window.frameElement.id || '';
    parent.PlatTab.newTab({
        id: "superviseInfo",
        title: "督办详情",
        closed: true,
        icon: "fa fa-tasks",
        url: "/supervise/SponsorController.do?goSuperviseInfo&id=" + id + "&nodeId=" + nodeId + "&parentFrameId=" + frameId
    });
}

/**
 * 办结反馈 时 跳转到 supEnd.jsp
 * @param recordId
 */
function viewSuperviseEnd(id, nodeId) {
    var frameId = window.frameElement && window.frameElement.id || '';
    parent.PlatTab.newTab({
        id: "endInfo",
        title: "办结详情",
        closed: true,
        icon: "fa fa-tasks",
        url: "/supervise/SponsorController.do?goSponsorSuperviseEndInfo&recordId=" + id + "&parentFrameId=" + frameId
    })
}

/**
 * 查看草稿详情
 * @param id
 */
function checkSuperviseDraftInfo(id) {
    var frameId = window.frameElement && window.frameElement.id || '';
    parent.PlatUtil.openWindow({
        title: "新建事项",
        area: ["1000px", "650px"],
        content: "supervise/SponsorController.do?goGenUiViewDraft&DESIGN_CODE=supervise_form&recordId=" + id + "&parentFrameId=" + frameId,
        end: function (resultJson) {

        }
    })
}

var Tools = {
    /**
     * 移除空字符串或者非法字符串，返回""
     * 如果是合法字符串，则返回原值
     * @param obj 文本
     */
    removeNullOrUndefined: function (obj) {
        //typeof 返回的是字符串，有六种可能："number"、"string"、"boolean"、"object"、"function"、"undefined"
        if (typeof (obj) == "undefined" || obj == "" || obj == null || obj == "null" || obj.length == 0) {
            return "--";
        } else {
            //删除全是空格的情况
            var regu = "^[ ]+$";
            var re = new RegExp(regu);
            if (re.test(obj)) {
                return "";
            } else {
                return obj;
            }

        }
    }
}