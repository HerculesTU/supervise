$(function () {
    init.initTable(); //表格数据初始化
    $("#searchBtn").click(function () {
        init.initTable();
    });
    //下拉选事件
    $("select[name='statusSelect']").change(function () {
        $("#myTable").jqGrid('setGridParam', {
            url: "/supervise/SponsorController.do?findSponsorSupEndList",
            datatype: 'json',
            postData: {
                status: this.value,
                page: 1
            },
            loadComplete: function () {
                var re_records = $("#myTable").getGridParam('records');
                $("#count").text("共" + re_records + "条");
            }
        }).trigger('reloadGrid');
    })
});

var init = {
    initTable: function () {

        PlatUtil.ajaxProgress({
            url: "/supervise/SponsorController.do?findSponsorSupEndList",
            type: "post",
            dataType: 'json',
            params: {
                KEYWORDS: $("input[name='keywords']").val(),
                TITLE: $("input[name='title']").val(),
                status: $("select[name='statusSelect']").val()
            },
            callback: function (resultJson) {
                if (resultJson) {
                    $('#myTable').clearGridData();
                    $('#myTable').remove();
                    $('#tableContent').empty();
                    var strTable = '<table id="myTable" class="scroll" cellpadding="0" cellspacing="0"></table>' +
                        '<div id="pager" class="scroll"></div>';
                    $("#tableContent").append(strTable);
                    var myUrl = resultJson.rows;
                    colNames = ['督办编号', '督办类型', '标题', '关键字', '承办部门', '操作'];
                    colModel = [
                        {name: 'SUPERVISE_NO', index: 'SUPERVISE_NO', width: 40},
                        {name: 'SUPERVISE_CLAZZ', index: 'SUPERVISE_CLAZZ', width: 80},
                        {name: 'TITLE', index: 'TITLE', width: 100},
                        {name: 'KEYWORDS', index: 'KEYWORDS', width: 80},
                        {name: 'DEPART_NAME', index: 'DEPART_NAME', width: 80},
                        {
                            name: 'D_CreateTime',
                            index: 'D_CreateTime',
                            formatter: function (cellvalue, options, rowObject) {
                                var temp = '';
                                temp += '<span class="colorr mr15" onclick="init.viewSupervise(\'' + rowObject.RECORD_ID + '\')">办结</span>';
                                return temp;
                            },
                            width: 50
                        }
                    ];
                    loadJqgrid("pager", "myTable", myUrl, colNames, colModel);
                }
            }
        });
    },
    viewSupervise: function (recordId) {
        var frameId = window.frameElement && window.frameElement.id || '';
        parent.PlatTab.newTab({
            id: "endInfo",
            title: "办结详情",
            closed: true,
            icon: "fa fa-tasks",
            url: "/supervise/SponsorController.do?goSponsorSuperviseEndInfo&recordId=" + recordId + "&parentFrameId=" + frameId
        })
    }
};
