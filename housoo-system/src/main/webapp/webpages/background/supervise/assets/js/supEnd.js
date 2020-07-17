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
 * 提交
 */
function reply(RECORD_ID, option, feedbackId, nodeId, TASK_ID) {
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
                    SUPERVISE_ID: RECORD_ID,
                    FEEDBACK_ID: feedbackId,
                    REPLY_CONTENT: $("#replyAdvice").val(),
                    SUPERVISE_TASK_ID: TASK_ID,
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
        })
    } else {
        layer.msg("批复意见不能为空！");
    }
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
                        str += '<section class="seredy btmbig">';
                        if (data[i].SHORT_NAME) {
                            str += '<span class="point-time point-red pointf">' + data[i].SHORT_NAME + '</span>';
                        } else {
                            str += '<span class="point-time point-red cirsx"></span>';
                        }
                        str += '<aside><p class="things">' + data[i].VALUE + '</p>';
                    } else if (status == '0') {//进行中
                        str += '<section class="seredy btmbig" onclick="loadTakerFeedbackInfo(\'' + data[i].NODE_ID + '\')" style="cursor: pointer">';
                        if (data[i].SHORT_NAME) {
                            str += '<span class="point-time points">' + data[i].SHORT_NAME + '</span>';
                        } else {
                            str += '<span class="point-time point-red cirsx"></span>';
                        }
                        str += '<aside><p class="things text-gray">' + data[i].VALUE + '</p>';
                    } else {//未开始
                        str += '<section class="seredy btmbig" onclick="loadTakerFeedbackInfo(\'' + data[i].NODE_ID + '\')" style="cursor: pointer">';
                        if (data[i].SHORT_NAME) {
                            str += '<span class="point-time pointg">' + data[i].SHORT_NAME + '</span>';
                        } else {
                            str += '<span class="point-time point-gray cirsx"></span>';
                        }
                        str += '<aside><p class="things text-gray">' + data[i].VALUE + '</p>';
                    }
                    if (data[i].outDateStatus == '1' && data[i].disagreeStatus == '1') {
                        str += '<p class="descp">逾期<span>' + data[i].outDateTimes + '</span>次，</p>';
                        var title = '';
                        var disagreeList = data[i].disagreeList;
                        for (var j = 0; j < disagreeList.length; j++) {
                            title += disagreeList[j].CREATE_TIME + '：' + disagreeList[j].REPLY_CONTENT + '\n';
                        }
                        str += '<p class="descp" style="right: 3%;cursor: pointer;" title="' + title + '">驳回<span>' + data[i].disagreeTimes + '</span>次</p>';
                    } else {
                        if (data[i].outDateStatus == '1') {
                            str += '<p class="descp">逾期<span>' + data[i].outDateTimes + '</span>次</p>';
                        }
                        if (data[i].disagreeStatus == '1') {
                            var title = '';
                            var disagreeList = data[i].disagreeList;
                            for (var j = 0; j < disagreeList.length; j++) {
                                title += disagreeList[j].CREATE_TIME + '：' + disagreeList[j].REPLY_CONTENT + '\n';
                            }
                            str += '<p class="descp" style="cursor: pointer;" title="' + title + '">驳回<span>' + data[i].disagreeTimes + '</span>次</p>';
                        }
                    }
                    str += '<p class="brief"><span class="text-gray">' + (data[i].date ? data[i].date : '--') + '</span></p></aside>';
                    str += '</section>';
                }
                $("#superviseProgress").append(str);
            }
        }
    })
}

