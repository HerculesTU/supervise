$(function () {
    $("#input-scfj").click(function () {
        $("#upword").click();
    });
    $("#upword").change(function () {
        var files = $("#upword")[0].files[0];
        $("#input-scfj").val(files.name);
    });

})

/**
 * 驳回
 */
function disAgree(option, feedbackId, nodeId) {
    var content = $("#replyAdvice").val();
    if (content) {
        parent.layer.confirm("确认提交？", {
            title: "提示",
            icon: 7,
            resize: false
        }, function () {
            PlatUtil.ajaxProgress({
                url: "/supervise/SponsorController.do?sponsorHandleFeedback",
                params: {
                    SUPERVISE_ID: supervise.RECORD_ID,
                    FEEDBACK_ID: feedback.FEEDBACK_ID,
                    REPLY_CONTENT: $("#replyAdvice").val(),
                    SUPERVISE_TASK_ID: supervise.TASK_ID,
                    isPass: option,
                    NODE_ID: nodeId
                },
                callback: function (data) {
                    if (data.success) {
                        window.parent.document.getElementById(parentFrameId).contentWindow.location.reload(true);
                        $(".page-tabs-content", parent.document).find(".active .tab_close").trigger("click");
                        parent.layer.msg("提交成功！");
                    }
                }
            })
        });
    } else {
        layer.msg("驳回意见不能为空！");
    }
}

/**
 * 同意
 * @param option
 * @param feedbackId
 * @param nodeId
 */
function agreeOption(option, feedbackId, nodeId) {
    parent.layer.confirm("确认提交？", {
        title: "提示",
        icon: 7,
        resize: false
    }, function () {
        PlatUtil.ajaxProgress({
            url: "/supervise/SponsorController.do?sponsorHandleFeedback",
            params: {
                SUPERVISE_ID: supervise.RECORD_ID,
                FEEDBACK_ID: feedback.FEEDBACK_ID,
                REPLY_CONTENT: $("#replyAdvice").val(),
                SUPERVISE_TASK_ID: supervise.TASK_ID,
                isPass: option,
                NODE_ID: nodeId
            },
            callback: function (data) {
                if (data.success) {
                    window.parent.document.getElementById(parentFrameId).contentWindow.location.reload(true);
                    $(".page-tabs-content", parent.document).find(".active .tab_close").trigger("click");
                    parent.layer.msg("提交成功！");
                }
            }
        })
    });
}

/**
 * 设置常用意见
 */
function setOptions() {
    var content = $("#replyAdvice").val();
    if (content && content.trim() !== "") {
        parent.layer.confirm("确认设置为常用意见？", {
            title: "提示",
            icon: 7,
            resize: false
        }, function () {
            PlatUtil.ajaxProgress({
                url: "/supervise/OpinionController.do?saveOrUpdate",
                params: {
                    OPINION_CONTENT: $("#replyAdvice").val(),
                },
                callback: function (data) {
                    if (resultJson.success) {
                        parent.layer.msg(PlatUtil.SUCCESS_MSG, {icon: 1});
                    } else {
                        parent.layer.alert("保存失败!", {icon: 2, resize: false});
                    }
                }
            })
        });
    } else {
        parent.layer.alert("意见内容不能为空!", {icon: 2, resize: false});
    }
}

/**
 选择常用意见
 */
function chooseOption() {
    PlatUtil.openWindow({
        title: "常用意见选择",
        area: ["1000px", "550px"],
        content: "supervise/OpinionController.do?goSelector",
        end: function () {
            if (PlatUtil.isSubmitSuccess()) {
                var OPINION_CONTENT = PlatUtil.getData("OPINION_CONTENT");
                $("#replyAdvice").val(OPINION_CONTENT)
                PlatUtil.removeData("OPINION_CONTENT");
            }
        }
    });
}

/**
 * 初始化督办流程
 */
function initSuperviseProgress(SUPERVISE_ID) {
    $("#superviseProgress").empty();
    PlatUtil.ajaxProgress({
        url: "/supervise/TakerController.do?initSuperviseProgress",
        params: {
            SUPERVISE_ID: SUPERVISE_ID
        },
        callback: function (data) {
            if (data) {
                var str = '';
                for (var i = 0; i < data.length; i++) {
                    var status = data[i].status;
                    if (status == '1') {//已完结
                        str += '<section class="seredy" onclick="loadTakerFeedbackInfo(\'' + data[i].NODE_ID + '\')" style="cursor: pointer">';
                        if (data[i].SHORT_NAME) {
                            str += '<span class="point-time point-red pointf">' + data[i].SHORT_NAME + '</span>';
                        } else {
                            str += '<span class="point-time point-red cirsx"></span>';
                        }
                        str += '<aside><p class="things">' + data[i].VALUE + '</p>';
                    } else if (status == '0') {//进行中
                        str += '<section class="seredy" onclick="loadTakerFeedbackInfo(\'' + data[i].NODE_ID + '\')" style="cursor: pointer">';
                        if (data[i].SHORT_NAME) {
                            str += '<span class="point-time points">' + data[i].SHORT_NAME + '</span>';
                        } else {
                            str += '<span class="point-time point-red cirsx"></span>';
                        }
                        str += '<aside><p class="things text-gray">' + data[i].VALUE + '</p>';
                    } else {//未开始
                        str += '<section onclick="loadTakerFeedbackInfo(\'' + data[i].NODE_ID + '\')" style="cursor: pointer">';
                        if (data[i].SHORT_NAME) {
                            str += '<span class="point-time pointg">' + data[i].SHORT_NAME + '</span>';
                        } else {
                            str += '<span class="point-time point-gray cirsx"></span>';
                        }
                        str += '<aside><p class="things text-gray">' + data[i].VALUE + '</p>';
                    }
                    str += '<p class="brief"><span class="text-gray">' + (data[i].date ? data[i].date : '--') + '</span></p></aside>';
                    str += '</section>';
                }
                $("#superviseProgress").append(str);
            }
        }
    })
}