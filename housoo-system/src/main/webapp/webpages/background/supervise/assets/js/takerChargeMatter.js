$(function () {
    $(".tabhh").height($(window).height() - $(".main_header").height() - 76);
    //下拉选事件
    $("select[name='status']").change(function () {
        loadTakerSuperviseList(clazz);
    })
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
        var status = $('#dbsxtable').find("input[type='checkbox']:checked").attr("status");
        var url = "/supervise/TakerChargeController.do?goTakerSuperviseInfo&isRead=1&id=" + id;
        if (status == '9') {
            url = "/supervise/TakerChargeController.do?goTakerSupEndInfo&id=" + id;
        }
        parent.PlatTab.newTab({
            id: "info",
            title: "督办详情",
            closed: true,
            icon: "fa fa-tasks",
            url: url
        })
    })
}

/**
 * 获取承办的事项列表
 */
function loadTakerSuperviseList(clazzId) {
    $("#dbsxtable").empty();
    var status = $("select[name='status']").val();
    PlatUtil.ajaxProgress({
        url: "/supervise/TakerChargeController.do?findTakerSuperviseList",
        progressMsg: "刷新中...",
        params: {
            status: status,
            clazzId: clazzId
        },
        callback: function (data) {
            if (data) {
                $("#count").text("共" + data.length + "条");
                var str = "";
                for (var i = 0; i < data.length; i++) {
                    var temp = data[i];
                    str += '<thead><tr id="' + temp.RECORD_ID + '">';
                    str += '<th class="ts singlecheck"><input type="checkbox" id="chk_' + temp.RECORD_ID + '" status="' + temp.STATUS + '"/></th>';
                    str += '<th colspan="6" class="nameo"><input type="hidden" value="a"/><span>' + temp.TITLE + '</span><i class="iconfont icon-xiajiantou e_h_r" style="font-size: 10px;margin: 5px;"></i>';
                    if (temp.SUPERVISE_CLAZZ.indexOf("日常") != '-1') {
                        str += '<span class="level levcor3">' + temp.SUPERVISE_CLAZZ + '</span>';
                    } else if (temp.SUPERVISE_CLAZZ.indexOf("重点") != '-1') {
                        str += '<span class="level levcor2">' + temp.SUPERVISE_CLAZZ + '</span>';
                    } else if (temp.SUPERVISE_CLAZZ.indexOf("重大") != '-1') {
                        str += '<span class="level levcor1">' + temp.SUPERVISE_CLAZZ + '</span>';
                    }
                    str += '</th><th class="lefto">' + (temp.SUPERVISE_SOURCE ? temp.SUPERVISE_SOURCE : '--') + '：' + temp.SUPERVISE_NO + '</th>';
                    str += '</tr></thead>';
                    if (temp.nodes && temp.nodes.length > 0) {
                        str += '<tbody class="e_con">';
                        for (var j = 0; j < temp.nodes.length; j++) {
                            str += '<tr>';
                            str += '<td>' + (eval(j) + 1) + '</td>';
                            str += '<td>' + (temp.nodes[j].CREATE_TIME ? temp.nodes[j].CREATE_TIME : '--') + '</td>';
                            str += '<td>' + temp.nodes[j].NODE_NAME + '</td>';
                            if (temp.nodes[j].timeStatus == '完成') {
                                str += '<td><span class="dbstate staCor1">' + temp.nodes[j].timeStatus + '</span></td>';
                                str += '<td>' + (temp.nodes[j].DEPART_NAME ? temp.nodes[j].DEPART_NAME : '--') + '</td>';
                                str += '<td>' + temp.KEYWORDS + '</td>';
                            }
                            if (temp.nodes[j].timeStatus == '办理中') {
                                str += '<td><span class="dbstate staCor2">' + temp.nodes[j].timeStatus + '</span></td>';
                                str += '<td>' + (temp.nodes[j].DEPART_NAME ? temp.nodes[j].DEPART_NAME : '--') + '</td>';
                                str += '<td>' + temp.KEYWORDS + '</td>';
                            }
                            if (temp.nodes[j].timeStatus == '逾期') {
                                str += '<td><span class="dbstate staCor4">' + temp.nodes[j].timeStatus + '</span></td>';
                                str += '<td>' + (temp.nodes[j].DEPART_NAME ? temp.nodes[j].DEPART_NAME : '--') + '</td>';
                                str += '<td>' + temp.KEYWORDS + '</td>';
                            }
                            if (temp.nodes[j].timeStatus == '未办理') {
                                str += '<td><span class="dbstate staCor3">' + temp.nodes[j].timeStatus + '</span></td>';
                                str += '<td>' + (temp.nodes[j].DEPART_NAME ? temp.nodes[j].DEPART_NAME : '--') + '</td>';
                                str += '<td>' + temp.KEYWORDS + '</td>';
                            }
                            if (temp.nodes[j].feedbackStatus == '1') {
                                str += '<td><img src="webpages/background/supervise/assets/images/right.png"></td>';
                            } else {
                                str += '<td><img src="webpages/background/supervise/assets/images/error.png"></td>';
                            }
                            str += '<td>';
                            if (temp.nodes[j].timeStatus == '未办理') {
                                str += '<button type="button" disabled class="tbtn" onclick="viewSupervise(\'' + temp.RECORD_ID + '\'' + ',' + '\'' + temp.nodes[j].NODE_ID + '\'' + ',' + '\'' + temp.STATUS + '\');">--</button>';
                                /*str += '<button type="button" class="tbtn">--</button>';*/
                            } else {
                                str += '<button type="button" class="tbtn" onclick="viewSupervise(\'' + temp.RECORD_ID + '\'' + ',' + '\'' + temp.nodes[j].NODE_ID + '\'' + ',' + '\'' + temp.STATUS + '\');">查看</button>';
                                /*str += '<button type="button" class="tbtn">导出</button>';*/
                            }
                            str += '</td>';
                            str += '</tr>';
                        }
                        str += '</tbody>';
                    }
                }
                $("#dbsxtable").append(str);
                bindEvent();
            }
        }
    })
}

function viewSupervise(id, nodeId, status) {
    var isRead = '1';
    if ((nodeId == '3' && status == '4') || (nodeId == '5' && status == '6' )) {
        isRead = '0';
    }
    var frameId = window.frameElement && window.frameElement.id || '';
    var url = "/supervise/TakerChargeController.do?goTakerSuperviseInfo&isRead=" + isRead + "&id=" + id + "&nodeId=" + nodeId + "&parentFrameId=" + frameId;
    if (status == '9') {
        url = "/supervise/TakerChargeController.do?goTakerSupEndInfo&id=" + id;
    }
    parent.PlatTab.newTab({
        id: "info",
        title: "督办详情",
        closed: true,
        icon: "fa fa-tasks",
        url: url
    })
}