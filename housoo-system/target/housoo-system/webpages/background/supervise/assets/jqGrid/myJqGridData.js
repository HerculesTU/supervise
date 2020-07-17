function stateQH(cellvalue, rowObject) {
    var temp = "";
    if (cellvalue == 0) {
        temp = "<img class='onoffimg' src='assets/images/on_switch.png'/><input type='hidden' id='user_state_hid" + rowObject.rowId + "' value=" + cellvalue + ">";
    } else if (cellvalue == 1) {
        temp = "<img class='onoffimg' src='assets/images/off_switch.png'/><input type='hidden' id='user_state_hid" + rowObject.rowId + "' value=" + cellvalue + ">";
    }
    return temp;
}
function loadJqgrid(pager_sel, grid_sel, myUrl, colNames, colModel) {
    GationJqgrid(pager_sel, grid_sel, myUrl, colNames, colModel);
    JQquto(grid_sel);
    jqGridResize(grid_sel);
}
function GationJqgrid(pager_sel, grid_sel, myUrl, colNames, colModel) {
    var pager_selector = "#" + pager_sel;
    var grid_selector = "#" + grid_sel;
    jQuery(grid_selector).jqGrid({
        data: myUrl,
        datatype: "local",
        /*url: myUrl,
         datatype: "json",*/
        rownumbers: false,
        mtype: "POST",
        height: "auto",
        colNames: colNames,
        colModel: colModel,
        sortable: false, // 是否可排序
        sortname: 'id', // 默认的排序列
        sortorder: 'asc', // 排序顺序，升序或者降序（asc or desc）
        shrinkToFit: false,
        viewrecords: true, // 是否显示 总记录数
        rowNum: 10, // 默认显示行数
        rowList: [10, 20, 30],
        pager: pager_selector,
        altRows: true, // 设置表格是否允许行交替变色值
        multiselect: true, // 是否显示 复选框
        multiboxonly: true,
        loadComplete: function () {
            /* 操作按钮 */
            var table = this;
            setTimeout(function () {
                updatePagerIcons(table);
            }, 0);

            $(jQuery(grid_selector).closest("div.ui-jqgrid-view").children("div.ui-corner-top")).find("div#jqgh_" + grid_sel + "_cb").css("text-align", "center").find("input#cb_" + grid_sel + "").css("margin-left", "-2px");

            var re_records = $(grid_selector).getGridParam('records');
            if (re_records == 0 || re_records == null) {
                if ($(".norecords").html() == null) {
                    $(grid_selector).parent().append("<div class=\"norecords\"></div>");
                    $(".norecords").width(yww);
                    $(".norecords").height(yhh);
                    $(".norecords").parent().parent().css('overflow-x', 'hidden');
                    $(".norecords").parent().parent().css('overflow-y', 'hidden');
                }
                $(".norecords").show();
            }
        },
        pagerpos: 'left',
        recordpos: 'right'
    });

    $(window).triggerHandler('resize.jqGrid'); // trigger window resize to make
    $(document).on('ajaxloadstart', function (e) {
        $(grid_selector).jqGrid('GridUnload');
        $('.ui-jqdialog').remove();
    });
    function updatePagerIcons(table) {
        var replacement = {
            'ui-icon-seek-first': 'ace-icon fa fa-step-backward ',
            'ui-icon-seek-prev': 'ace-icon fa fa-caret-left',
            'ui-icon-seek-next': 'ace-icon fa fa-caret-right',
            'ui-icon-seek-end': 'ace-icon fa fa-step-forward '
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
        });
    }
}
var yww, yhh = 0;
function JQquto(grid_sel) {
    var grid_selector = "#" + grid_sel;
    var hh = $(window).height() - $(".main_header").height() - 20;
    $(".list_jqgrid").width($(".main_header").width());
    $(".list_jqgrid").height(hh);
    yww = $(".list_jqgrid").width();
    $(grid_selector).jqGrid('setGridWidth', yww);
    yhh = $(".list_jqgrid").height() - 70;
    $(grid_selector).jqGrid('setGridHeight', yhh);
    var ww = $("#" + grid_sel).width();
    var ws = $("#gview_" + grid_sel).width();
    if (ww < ws) {
        $("#gview_" + grid_sel).find(".ui-jqgrid-htable").width(ws);
        $("#" + grid_sel).width(ws);
    }
}
function jqGridResize(grid_sel) {
    var grid_selector = "#" + grid_sel;
    $(window).triggerHandler('resize.jqGrid');// trigger window resize to
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            setTimeout(function () {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }, 0);
        }
    });
}
