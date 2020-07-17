/**
 * 提交
 */
function approveSupervise(msg, result, feedbackId) {
    var content = $(".feedback").find("textarea[name='fknr']").val();
    if (content) {
        parent.layer.confirm(msg ? msg : "确认提交？", {
            title: "提示",
            icon: 7,
            resize: false
        }, function () {
            PlatUtil.ajaxProgress({
                url: "/supervise/TakerChargeController.do?takerChargeApprove",//承办部门负责人审批
                params: {
                    SUPERVISE_ID: supervise.RECORD_ID,
                    NODE_ID: nodeId,
                    APPROVE_RESULT: result,
                    FEEDBACK_ID: feedbackId
                },
                callback: function (data) {
                    if (data.success) {
                        window.parent.document.getElementById(parentFrameId).contentWindow.location.reload(true);
                        $(".page-tabs-content", parent.document).find(".active .tab_close").trigger("click");
                        parent.layer.msg("提交成功！");
                    } else {
                        parent.layer.msg(data.msg);
                    }
                }
            })
        });
    } else {
        layer.msg("反馈内容不能为空！");
    }
}
/**
 * 上传
 * @param obj
 */
function uploadFile(obj) {
    var files = obj.files;
    var formData = new FormData();
    for (var i = 0; i < files.length; i++) {
        formData.append("uploadFile[]", files[i]);
    }
    formData.append("superviseId", supervise.RECORD_ID);
    formData.append("uploadRootFolder", "supervise/feedback");
    $.ajax({
        url: "system/FileAttachController/upload.do",
        type: "POST",
        data: formData,
        cache: false, //不设置缓存
        processData: false, // 不处理数据
        contentType: false, // 不设置内容类型
        success: function (responseJsonObj, status) {
            if (responseJsonObj.success) {
                $(obj).parent().find("#fileurl").val(responseJsonObj.dbfilepath);
                $(obj).parent().find("input[name='scfj']").val(responseJsonObj.originalfilename);
            } else {
                parent.layer.msg("文件上传失败！")
            }
        },
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
                var str1 = '';
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

                //拼接督办状态
                for (var j = 0; j < data.length; j++) {
                    status = data[j].status;
                    if (data[j].SHORT_NAME) {
                        if (status == '1') {//已完结
                            str1 += '<li class="grn">' + data[j].SHORT_NAME + '</li>';
                        } else if (status == '0') {//进行中
                            str1 += '<li class="crn">' + data[j].SHORT_NAME + '</li>';
                        } else {//未开始
                            str1 += '<li>' + data[j].SHORT_NAME + '</li>';
                        }
                    }
                }
                $("#nodeUl").append(str1);
            }
        }
    })
}

/**
 * cjr 20200508新增
 * 左侧督办流程节点联动右侧反馈内容
 */
function loadTakerFeedbackInfo(checkNodeId) {
    if (checkNodeId == '1') {
        return;
    }
    var isRead = '0';
    if (checkNodeId != nodeId) {
        isRead = '1';//只读
    } else {
        if ((checkNodeId == '2' && supervise.STATUS != '3') || (checkNodeId == '3' && supervise.STATUS != '5') || (nodeId == '4' && supervise.STATUS != '7')) {
            isRead = '1';//只读
        }
    }
    PlatUtil.ajaxProgress({
        url: "/supervise/TakerChargeController.do?loadTakerFeedbackInfo",
        params: {
            id: supervise.RECORD_ID,
            nodeId: checkNodeId,
        },
        callback: function (data) {
            if (data.success) {
                var confirm = data.confirm;
                var feedback = data.feedback;
                //回显数据
                if (confirm || feedback) {
                    if (checkNodeId == '2') {
                        $("div[name='blfk']").css("display", "none");
                        $("div[name='lsfk']").css("display", "none");
                        $("div[name='bjfk']").css("display", "none");
                        $("div[name='dbqr']").css("display", "");
                        $("div[name='dbqr']").addClass("feedback");
                        $(".feedback").find("input[name='fk']").val(data.restTime4);
                    }
                    if (checkNodeId == '3') {
                        $("div[name='blfk']").css("display", "");
                        $("div[name='blfk']").addClass("feedback");
                        $("div[name='lsfk']").css("display", "none");
                        $("div[name='dbqr']").css("display", "none");
                        $("div[name='bjfk']").css("display", "none");
                        $(".feedback").find("input[name='fk']").val(data.restTime1);
                    }
                    if (checkNodeId == '4') {
                        $("div[name='blfk']").css("display", "none");
                        $("div[name='lsfk']").css("display", "");
                        $("div[name='lsfk']").addClass("feedback");
                        $("div[name='dbqr']").css("display", "none");
                        $("div[name='bjfk']").css("display", "none");
                        $(".feedback").find("input[name='fk']").val(data.restTime2);
                    }
                    if (checkNodeId == '5') {
                        $("div[name='blfk']").css("display", "none");
                        $("div[name='lsfk']").css("display", "none");
                        $("div[name='dbqr']").css("display", "none");
                        $("div[name='bjfk']").css("display", "");
                        $("div[name='bjfk']").addClass("feedback");
                        $(".feedback").find("input[name='fk']").val(data.restTime3);
                    }
                    if (checkNodeId == '2') {
                        $(".feedback").find("textarea[name='bz']").val(confirm.REMARKS);
                    } else {
                        $(".feedback").find("textarea[name='fknr']").val(feedback.FEEDBACK_CONTENT);
                        $(".feedback").find("input[name='bz']").val(feedback.REMARKS);
                        $(".feedback").find("#fileurl").val(feedback.FILE_URL);
                        $(".feedback").find("input[name='scfj']").val(feedback.FILE_NAME);
                    }
                    if (isRead == '1') {
                        $(".feedback").find(".search_btn").attr("disabled", "disabled");
                    } else {
                        $(".feedback").find(".search_btn").removeAttr("disabled");
                    }
                }
            }
        }
    })
}