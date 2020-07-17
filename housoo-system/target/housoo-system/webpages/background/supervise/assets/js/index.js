function autoi() {
    $(".prodiv").width($(".col-3").width() - 180);
}
$(function () {
    window.onresize = autoi;
    autoi();
    tab();
    pieChart("pieChart1", "#ec6941", 62);
    pieChart("pieChart2", "#f8ac59", 45);
    pieChart("pieChart3", "#5eadff", 68);
    pieChart("pieChart4", "#e13e3c", 22);
    var data = {
        datax: ["部门一", "部门二", "部门三", "部门四", "部门五", "部门六", "部门七", "部门八", "部门九", "部门十"],
        datay: [66, 40, 60, 40, 46, 66, 40, 60, 40, 46]
    };
    lineArea("lineChart", data);
    loadtab('depcon');
    pieCharts("piecharts");
    /*var legend = ["正常","逾期"];
     var data = {datax:["部门一","部门二","部门三","部门四","部门五","部门六","部门七","部门八","部门九","部门十"],
     datay:{datay1:[200,400,300,150,130,300,200,400,200,400],datay2:[140,120,250,200,400,190,340,100,140,250]}};
     barChart("barChart",legend,data);*/
    /*var legend = ["正常","逾期"];
     var data = {datax:["日常管理","重点工作","重大改革","重点工程","重点技改"],
     datay:{datay1:[200,400,300,150,130,300],datay2:[190,340,100,140,250]}};
     barChart("barChart2",legend,data);*/
});
var json = [{
    param1: '部门一',
    param2: '16464',
    param3: '16464',
    param4: '16464',
    param5: '16464',
    param6: '16464',
    param7: '16464'
},
    {
        param1: '部门二',
        param2: '16464',
        param3: '16464',
        param4: '16464',
        param5: '16464',
        param6: '16464',
        param7: '16464'
    },
    {
        param1: '部门三',
        param2: '16464',
        param3: '16464',
        param4: '16464',
        param5: '16464',
        param6: '16464',
        param7: '16464'
    },
    {
        param1: '部门四',
        param2: '16464',
        param3: '16464',
        param4: '16464',
        param5: '16464',
        param6: '16464',
        param7: '16464'
    },
    {
        param1: '部门五',
        param2: '16464',
        param3: '16464',
        param4: '16464',
        param5: '16464',
        param6: '16464',
        param7: '16464'
    },
    {
        param1: '部门六',
        param2: '16464',
        param3: '16464',
        param4: '16464',
        param5: '16464',
        param6: '16464',
        param7: '16464'
    },
    {
        param1: '部门七',
        param2: '16464',
        param3: '16464',
        param4: '16464',
        param5: '16464',
        param6: '16464',
        param7: '16464'
    },
    {
        param1: '部门八',
        param2: '16464',
        param3: '16464',
        param4: '16464',
        param5: '16464',
        param6: '16464',
        param7: '16464'
    },
    {
        param1: '部门九',
        param2: '16464',
        param3: '16464',
        param4: '16464',
        param5: '16464',
        param6: '16464',
        param7: '16464'
    },
    {
        param1: '部门十',
        param2: '16464',
        param3: '16464',
        param4: '16464',
        param5: '16464',
        param6: '16464',
        param7: '16464'
    }]
function loadtab(id) {
    var str = '';
    for (var i = 0; i < json.length; i++) {
        str += '<tr>';
        str += '<td>' + json[i].param1 + '</td>';
        str += '<td>' + json[i].param2 + '</td>';
        str += '<td>' + json[i].param3 + '</td>';
        str += '<td>' + json[i].param4 + '</td>';
        str += '<td>' + json[i].param5 + '</td>';
        str += '<td>' + json[i].param6 + '</td>';
        str += '<td>' + json[i].param7 + '</td>';
        str += '</tr>';
    }
    $("#" + id).append(str);
}
function tab() {
    var $li = $(".in_ul li");
    $li.click(function (e) {
        $li.removeClass("active");
        $(this).addClass("active");
    });
}
