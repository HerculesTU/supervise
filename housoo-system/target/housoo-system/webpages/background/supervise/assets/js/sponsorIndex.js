$(function () {
    window.onresize = autoi;
    autoi();
    bindEvent();
    //加载各个数量tab
    loadNumberTab();
    //发起的督办任务 进行中的督办任务 结束的督办任务 逾期的督办任务 饼图统计
    loadSectorCommitData();
    loadSectorProcessData();
    loadSectorSectorEndData();
    loadSectorSectorOutTimeData();
    loadLineChart("lineChart");//加载部门督办数量柱线图
    loadTableData('headTable', 'dataTable');
    loadOutDateData();
});

function autoi() {
    $(".prodiv").width($(".col-3").width() - 180);
}

/**
 * 页面元素绑定事件
 */
function bindEvent() {
    //年度、季度、月份、近七天切换事件
    $("#searchUl li").click(function () {
        $(this).parent().find("li[class='active']").removeClass("active");
        $(this).addClass("active");
        $("#startDate").val("");
        $("#endDate").val("");
        loadNumberTab();
        //发起的督办任务 进行中的督办任务 结束的督办任务 逾期的督办任务 饼图统计
        loadSectorCommitData();
        loadSectorProcessData();
        loadSectorSectorEndData();
        loadSectorSectorOutTimeData();
    });
    //日期范围搜索按钮
    $("#dateSearchBtn").click(function () {
        var startDate = $("#startDate").val();
        var endDate = $("#endDate").val();
        if (!startDate) {
            parent.layer.msg("开始日期不能为空！");
            return;
        }
        if (!endDate) {
            parent.layer.msg("结束日期不能为空！");
            return;
        }
        if (startDate && endDate) {
            $("#searchUl").find("li[class='active']").removeClass("active");
            loadNumberTab();//发起的督办任务 进行中的督办任务 结束的督办任务 逾期的督办任务 饼图统计
            loadSectorCommitData();
            loadSectorProcessData();
            loadSectorSectorEndData();
            loadSectorSectorOutTimeData();
        }
    });
    //督办状态下拉选
    $("select[name='status']").change(function () {
        loadLineChart("lineChart");
    })
}

/**
 * 加载各个数量tab
 */
function loadNumberTab() {
    var type = $("#searchUl").find("li[class='active']").attr("id");//年度、季度、月份、近七天
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    if (startDate && endDate) {
        type = "";
    }
    PlatUtil.ajaxProgress({
        url: "/supervise/SponsorController.do?getSponsorIndexNumericData",
        progressMsg: "刷新中...",
        params: {
            type: type,
            startDate: startDate,
            endDate: endDate
        },
        callback: function (data) {
            if (data.success) {
                $("#all").text(data.ALLSUM);
                $("#ing").text(data.INGSUM);
                $("#finish").text(data.FINISHSUM);
                $("#outdate").text(data.OUTDATESUM);
            }
        }
    })
}

/**
 * 加载部门督办数量柱线图
 * @param id
 */
function loadLineChart(id) {
    var status = $("select[name='status']").val();
    PlatUtil.ajaxProgress({
        url: "/supervise/SponsorController.do?findSponsorIndexLineData",
        params: {
            status: status
        },
        callback: function (data) {
            if (data) {
                var datax = [], datay1 = [], datay2 = [], datay3 = [];
                for (var i = 0; i < data.length; i++) {
                    datax.push(data[i].DEPART_NAME);
                    datay1.push(data[i].data.ALLSUM);
                    datay2.push(data[i].data.INGSUM);
                    datay3.push(data[i].data.FINISHSUM);
                }
                var all = {
                    datax: datax,
                    datay: datay1
                };
                var ing = {
                    datax: datax,
                    datay: datay2
                };
                var finish = {
                    datax: datax,
                    datay: datay3
                };
                if (status == '1,2,3,4,5,6,7,8,9,') {
                    lineChart(id, all);
                } else if (status == '9') {
                    lineChart(id, finish);
                } else {
                    lineChart(id, ing);
                }
            }
        }
    })
}

/**
 * 加载部门督办事项统计列表
 * @param id
 */
function loadTableData(headId, dataId) {
    PlatUtil.ajaxProgress({
        url: "/supervise/SponsorController.do?findSponsorIndexTableData",
        params: {},
        callback: function (result) {
            if (result.success) {
                var clazz = result.clazz;
                var str = '';
                str += '<thead><tr>';
                str += '<th>部门</th>';
                str += '<th>承办总数</th>';
                for (var i = 0; i < clazz.length; i++) {
                    str += '<th id="' + clazz[i].VALUE + '">' + clazz[i].LABEL + '</th>';
                }
                str += '</tr></thead>';
                $("#" + headId).append(str);

                var data = result.data;
                if (data) {
                    str = '';
                    for (var i = 0; i < data.length; i++) {
                        str += '<tr>';
                        str += '<td>' + data[i].DEPART_NAME + '</td>';
                        str += '<td>' + (data[i].clazzs[0].count + data[i].clazzs[1].count + data[i].clazzs[2].count + data[i].clazzs[3].count + data[i].clazzs[4].count) + '</td>';
                        str += '<td>' + data[i].clazzs[0].count + '</td>';
                        str += '<td>' + data[i].clazzs[1].count + '</td>';
                        str += '<td>' + data[i].clazzs[2].count + '</td>';
                        str += '<td>' + data[i].clazzs[3].count + '</td>';
                        str += '<td>' + data[i].clazzs[4].count + '</td>';
                        str += '</tr>';
                    }
                    $("#" + dataId).append(str);
                }
            }
        }
    });
}

/**
 * 逾期统计
 */
function loadOutDateData() {
    PlatUtil.ajaxProgress({
        url: "/supervise/SponsorController.do?findSponsorIndexOutDateData",
        params: {},
        callback: function (result) {
            if (result.success) {
                var sum = result.bjfk + result.dbqr + result.blfk + result.dbfk;
                var data = [
                    {value: result.bjfk, name: '办结反馈'},
                    {value: result.dbqr, name: '督办确认'},
                    {value: result.blfk, name: '办理反馈'},

                ];
                pieCharts("pieChart5", data, sum);//逾期统计
                $("#bjfk").text(result.bjfk);
                $("#dbqr").text(result.dbqr);
                $("#blfk").text(result.blfk);
             
            }
        }
    });
}

/**
 * 项目发起 饼图统计
 */
function loadSectorCommitData() {
    var type = $("#searchUl").find("li[class='active']").attr("id");//年度、季度、月份、近七天
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    if (startDate && endDate) {
        type = "";
    }
    PlatUtil.ajaxProgress({
        url: "/supervise/SponsorController.do?getSponsorIndexSectorCommitData",
        progressMsg: "刷新中...",
        params: {
            type: type,
            startDate: startDate,
            endDate: endDate
        },
        callback: function (result) {
            if (result) {
                var dataArr = [];
                for (var i in result) {
                    dataArr.push({value: result[i].NUM, name: result[i].SUPERVISE_CLAZZ});
                }
                pieChartsNew("pieChart1", dataArr, '')
            }
        }
    });
}

/**
 * 正在进行中的督办任务 饼图统计
 */
function loadSectorProcessData() {
    var type = $("#searchUl").find("li[class='active']").attr("id");//年度、季度、月份、近七天
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    if (startDate && endDate) {
        type = "";
    }
    PlatUtil.ajaxProgress({
        url: "/supervise/SponsorController.do?getSponsorIndexSectorProcessData",
        params: {
            type: type,
            startDate: startDate,
            endDate: endDate
        },
        progressMsg: "刷新中...",
        callback: function (result) {
            if (result) {
                var dataArr = [];
                for (var i in result) {
                    dataArr.push({value: result[i].NUM, name: result[i].SUPERVISE_CLAZZ});
                }
                pieChartsNew("pieChart2", dataArr, '')
            }
        }
    });
}

/**
 * 结束的督办任务 饼图统计
 */
function loadSectorSectorEndData() {
    var type = $("#searchUl").find("li[class='active']").attr("id");//年度、季度、月份、近七天
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    if (startDate && endDate) {
        type = "";
    }
    PlatUtil.ajaxProgress({
        url: "/supervise/SponsorController.do?getSponsorIndexSectorEndData",
        params: {
            type: type,
            startDate: startDate,
            endDate: endDate
        },
        progressMsg: "刷新中...",
        callback: function (result) {
            if (result) {
                var dataArr = [];
                for (var i in result) {
                    dataArr.push({value: result[i].NUM, name: result[i].SUPERVISE_CLAZZ});
                }
                pieChartsNew("pieChart3", dataArr, '')
            }
        }
    });
}

/**
 * 逾期的督办任务 饼图统计
 */
function loadSectorSectorOutTimeData() {
    var type = $("#searchUl").find("li[class='active']").attr("id");//年度、季度、月份、近七天
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    if (startDate && endDate) {
        type = "";
    }
    PlatUtil.ajaxProgress({
        url: "/supervise/SponsorController.do?getSponsorIndexSectorOutTimeData",
        params: {
            type: type,
            startDate: startDate,
            endDate: endDate
        },
        progressMsg: "刷新中...",
        callback: function (result) {
            if (result) {
                var dataArr = [];
                for (var i in result) {
                    dataArr.push({value: result[i].NUM, name: result[i].SUPERVISE_CLAZZ});
                }
                pieChartsNew("pieChart4", dataArr, '')
            }
        }
    });
}
