$(function () {
    window.onresize = autoi;
    autoi();
    bindEvent();
    loadNumberTab();//加载各个数量tab
    loadBarChart("barChart");//加载部门督办数量柱线图
    loadTableData('headTable', 'dataTable');
    loadOutDateData();
    loadPieCharts();
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
        loadPieCharts();
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
            loadNumberTab();
            loadPieCharts();
        }
    });
    //督办状态下拉选
    $("select[name='status']").change(function () {
        loadBarChart("barChart");
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
        url: "/supervise/TakerController.do?getTakerIndexNumericData",
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
                var outDateData = data.outDateData;
                if (outDateData) {
                    var OUTDATESUM = 0;
                    var pie = [];
                    for (var i = 0; i < outDateData.length; i++) {
                        OUTDATESUM += outDateData[i].count;
                        pie.push({value: outDateData[i].count, name: outDateData[i].LABEL});
                    }
                    $("#outdate").text(OUTDATESUM);
                    pieChartsNew("pieChart4", pie);
                }
            }
        }
    })
}

/**
 * 加载部门督办数量柱线图
 * @param id
 */
function loadBarChart(id) {
    var status = $("select[name='status']").val();
    PlatUtil.ajaxProgress({
        url: "/supervise/TakerController.do?findTakerIndexChartData",
        params: {
            status: status
        },
        callback: function (data) {
            if (data) {
                var datax = [], datay1 = [], datay11 = [], datay2 = [], datay21 = [], datay3 = [], datay31 = [];
                for (var i = 0; i < data.length; i++) {
                    //datax.push(data[i].NODE_NAME);
                    datax.push(data[i].LABEL);
                    datay1.push(data[i].normalData.ALLSUM);
                    datay2.push(data[i].normalData.INGSUM);
                    datay3.push(data[i].normalData.FINISHSUM);

                    datay11.push(data[i].outDateData.ALLSUM);
                    datay21.push(data[i].outDateData.INGSUM);
                    datay31.push(data[i].outDateData.FINISHSUM);

                }
                var all = {
                    datax: datax,
                    datay: {
                        datay1: datay1,
                        datay2: datay11
                    }
                };
                var ing = {
                    datax: datax,
                    datay: {
                        datay1: datay2,
                        datay2: datay21
                    }
                };
                var finish = {
                    datax: datax,
                    datay: {
                        datay1: datay3,
                        datay2: datay31
                    }
                };
                var legend = ["正常", "逾期"];
                if (status == '1,2,3,4,5,6,7,8,9,') {
                    barChart(id, legend, all);
                } else if (status == '9') {
                    barChart(id, legend, finish);
                } else {
                    barChart(id, legend, ing);
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
        url: "/supervise/TakerController.do?findTakerIndexTableData",
        params: {},
        callback: function (result) {
            if (result.success) {
                var clazz = result.clazz;
                var str = '';
                str += '<thead><tr>';
                str += '<th>状态</th>';
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
                        str += '<td>' + data[i].NODE_NAME + '</td>';
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
        url: "/supervise/TakerController.do?findTakerIndexOutDateData",
        params: {},
        callback: function (result) {
            if (result.success) {
                var sum = result.bjfk + result.blfk + result.dbqr;
                var data = [
                    {value: result.bjfk, name: '办结反馈'},
                    /*{value: result.lsfk, name: '落实反馈'},*/
                    {value: result.blfk, name: '办理反馈'},
                    /*{value: result.dbfk, name: '督办反馈'},*/
                    {value: result.dbqr, name: '督办确认'}
                ];
                pieCharts("pieChart5", data, sum);//逾期统计
                $("#bjfk").text(result.bjfk);
                /*$("#lsfk").text(result.lsfk);*/
                $("#blfk").text(result.blfk);
                /*$("#dbfk").text(result.dbfk);*/
                $("#dbqr").text(result.dbqr);
            }
        }
    });
}

/**
 * 加载各个督办类型占比
 */
function loadPieCharts() {
    var type = $("#searchUl").find("li[class='active']").attr("id");//年度、季度、月份、近七天
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    if (startDate && endDate) {
        type = "";
    }
    PlatUtil.ajaxProgress({
        url: "/supervise/TakerController.do?getTakerIndexPieData",
        params: {
            type: type,
            startDate: startDate,
            endDate: endDate
        },
        callback: function (result) {
            if (result) {
                var pie1 = [], pie2 = [], pie3 = [], pie4 = [];
                for (var i = 0; i < result.length; i++) {
                    pie1.push({value: result[i].data.ALLSUM, name: result[i].LABEL});
                    pie2.push({value: result[i].data.INGSUM, name: result[i].LABEL});
                    pie3.push({value: result[i].data.FINISHSUM, name: result[i].LABEL});
                    pie4.push({value: result[i].data.OUTDATESUM, name: result[i].LABEL});
                }
                pieChartsNew("pieChart1", pie1);
                pieChartsNew("pieChart2", pie2);
                pieChartsNew("pieChart3", pie3);
                //pieChartsNew("pieChart4", pie4);
            }
        }
    });
}
