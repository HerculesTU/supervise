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
                        if (data[i].SHORT_NAME) {
                            str1 += '<li class="grn">' + data[i].SHORT_NAME + '</li>';
                        }
                        str += '<section class="seredy btmbig">';
                        if (data[i].SHORT_NAME) {
                            str += '<span class="point-time point-red pointf">' + data[i].SHORT_NAME + '</span>';
                        } else {
                            str += '<span class="point-time point-red cirsx"></span>';
                        }
                        str += '<aside><p class="things">' + data[i].VALUE + '</p>';
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
                $("#nodeUl").append(str1);
            }
        }
    })
}