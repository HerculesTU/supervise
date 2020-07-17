$(function () {
    init.initTable(); //表格数据初始化
    $("#searchBtn").click(function () {
        init.initTable();
    })
    //下拉选事件
    $("select[name='status']").change(function () {
        $("#myTable").jqGrid('setGridParam', {
            url: "/supervise/TakerController.do?findTakerSupEndList",
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
            url: "/supervise/TakerController.do?findTakerSupEndList",
            type: "post",
            dataType: 'json',
            params: {
                KEYWORDS: $("input[name='keywords']").val(),
                TITLE: $("input[name='title']").val(),
                status: $("select[name='status']").val()
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
                    $("#count").text("共" + resultJson.records + "条");
                    //myUrl = queryTab;
                    var colNames = ['督办编号', '督办类型', '标题', '关键字', '督办部门', '操作'];
                    var colModel = [
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
                                if (rowObject.STATUS != 9) {
                                    temp += '<span class="colorr mr15" onclick="init.viewSupervise(\'' + rowObject.RECORD_ID + '\')">申请办结</span>';
                                } else {
                                    temp += '<span class="colorr mr15" onclick="init.viewSupervise(\'' + rowObject.RECORD_ID + '\'' + ',' + '\'' + rowObject.STATUS + '\')">查看</span>';
                                }
                                /*temp += '<span class="colorr">导出</span>';*/
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
    viewSupervise: function (id, status) {
        var frameId = window.frameElement && window.frameElement.id || '';
        var url = "/supervise/TakerController.do?goTakerSuperviseInfo&isRead=0&id=" + id + "&nodeId=5&parentFrameId=" + frameId;
        if (status == '9') {
            url = "/supervise/TakerController.do?goTakerSupEndInfo&id=" + id;
        }
        parent.PlatTab.newTab({
            id: "endInfo",
            title: "督办详情",
            closed: true,
            icon: "fa fa-tasks",
            url: url
        })
    }
};
