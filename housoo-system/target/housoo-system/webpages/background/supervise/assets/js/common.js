function auto() {
    var sc = window.screen.width;
    if (sc > 1366 && sc <= 1440) {
        $("#body").addClass("w1440");
    } else if (sc > 1280 && sc <= 1366) {
        $("#body").addClass("w1366");
    } else if (sc <= 1024) {
        $("#body").addClass("w1024");
    }
    var h = $(window).height();
    $(".main_content").height(h);
}
/**
 * 定义数据库模拟菜单字段
 * */
var menustr = [{p_menu: "督办事项管理", iconClass: 'icon-danganhe', srchref: "supMatter.html", submenu: []},
    {p_menu: "事项督办", iconClass: 'icon-HRrenshirenshiguanli', srchref: "feedback.html", submenu: []},
    {p_menu: "事项办结", iconClass: 'icon-saomiaoyi01', srchref: "supEnd.html", submenu: []},
    {p_menu: "字典管理", iconClass: 'icon-dangan1', srchref: "", submenu: []},
    {
        p_menu: "日志管理", iconClass: 'icon-rizhi', srchref: "", submenu: [
        {id: "11", name: "系统日志", type: 1, sort: 1, descript: '', srchref: ""},
        {id: "12", name: "操作日志", type: 1, sort: 1, descript: '', srchref: ""}]
    },
    {
        p_menu: "系统设置", iconClass: 'icon-shezhi', srchref: "", submenu: [
        {id: "21", name: "用户管理", type: 1, sort: 1, descript: '', srchref: ""},
        {id: "22", name: "角色管理", type: 1, sort: 1, descript: '', srchref: ""},
        {id: "23", name: "菜单管理", type: 1, sort: 1, descript: '', srchref: ""}]
    }
];
var onOff = true;
var arr = [];
$(function () {
    window.onresize = auto;
    auto();
    loadMenu();  //加载菜单
});
/*加载菜单方法*/
function loadMenu() {
    $(".menu_con").html("");
    var str = "";
    str += '<ul class="main_menu">';
    for (var i = 0; i < menustr.length; i++) {
        var p_menu = menustr[i].p_menu;
        var iconClass = menustr[i].iconClass;
        var submenu = menustr[i].submenu;
        var srchref = menustr[i].srchref;
        str += '<li mid="tab' + i + 1 + '" funurl=' + srchref + '>';
        if (submenu.length != 0) {
            str += '<a class="main_menu_para" tabindex="-1" href="javascript:void(0);">';
            str += '<i class="iconfont ' + iconClass + ' menuFont"></i>';
            str += '<span>' + p_menu + '</span>';
            str += '<i class="iconfont icon-xiajiantoushixinxiao main_menu_arrow"></i>';
            str += '</a>';
            str += '<ul class="sub_menu">';
            for (var j = 0; j < submenu.length; j++) {
                var name = submenu[j].name;
                var srchref = submenu[j].srchref;
                str += '<li><a href=' + srchref + '><span class="submenutext">' + name + '</span></a></li>';
            }
            str += '</ul>';
        } else {
            if (srchref == "") {
                srchref = '#';
            }
            str += '<a class="main_menu_para">';
            str += '<i class="iconfont ' + iconClass + ' menuFont"></i>';
            str += '<span>' + p_menu + '</span>';
            str += '</a>';
        }
        str += '</li>';
    }
    str += '</ul>';
    $(".menu_con").append(str);

    //一级菜单点击事件
    $(".main_menu li .main_menu_para").click(function (e) {
        var pars = $(".main_menu li .main_menu_para").parent();
        pars.removeClass("active");
        pars.find(".main_menu_arrow").removeClass("icon-unfold");
        pars.find(".main_menu_arrow").addClass("icon-xiangyouzhedie");
        var obj = $(this).parent();
        $(".sub_menu").slideUp();
        if ($(this).parent().children(".sub_menu").length != 0) {
            if ($(this).parent().children(".sub_menu")[0].clientHeight > 0) {
                $(this).parent().children(".sub_menu").slideUp();
                obj.find(".main_menu_arrow").removeClass("icon-unfold");
                obj.find(".main_menu_arrow").addClass("icon-xiangyouzhedie");
                obj.removeClass("active");
            } else {
                $(this).parent().children(".sub_menu").slideToggle();
                obj.find(".main_menu_arrow").removeClass("icon-xiangyouzhedie");
                obj.find(".main_menu_arrow").addClass("icon-unfold");
                obj.addClass("active");
            }
        }
        e.stopPropagation();
        var li = $(this).closest('li');
        var menuId = $(li).attr('mid');
        var url = $(li).attr('funurl');
        var title = $(this).text();
        $('#mainFrameTabs').bTabsAdd(menuId, title, url);
        //计算Tab可用区域高度
        calcHeight();
    });
    //初始化
    $('#mainFrameTabs').bTabs();
    //计算内容区域高度
    var calcHeight = function () {
        /*var browserHeight = $(window).innerHeight();
         var tabMarginTop = parseInt($('#mainFrameTabs').css('margin-top'));//获取间距
         var tabHeadHeight = $('ul.nav-tabs',$('#mainFrameTabs')).outerHeight(true) + tabMarginTop;
         var contentMarginTop = parseInt($('div.tab-content',$('#mainFrameTabs')).css('margin-top'));//获取内容区间距
         var contentHeight = browserHeight - tabHeadHeight - contentMarginTop;*/
        var hh = $(".main_content").height() - $(".nav").height() - 26 - 10;
        $('div.tab-content').height(hh);
    };

    //二级菜单点击事件
    $(".main_menu .sub_menu li").click(function (e) {
        $(".main_menu .sub_menu li").removeClass("active");
        $(this).addClass("active");
    });

    // 判断当前菜单并选中
    /*var curUrl = window.location.href.replace(basePath, '').replace(/[_?].*$/g,'');*/
    var curUrls = window.location.href.split("/")[4];
    var curUrl = curUrls.split("?")[0];
    $(".main_menu li.active ul.sub_menu li").removeClass("active");
    var links = $("a[href='" + curUrl + "']");
    var linkpar = links.parent().parent().parent();
    if (linkpar.is('li')) {
        linkpar.addClass('active');
    }
    linkpar.find(".main_menu_arrow").removeClass("icon-xiangyouzhedie");
    linkpar.find(".main_menu_arrow").addClass("icon-unfold");
    links.parent().addClass('active');

    $(".main_menu li.active ul.sub_menu").slideDown();
}

/*退出系统*/
function exitSys() {
    layer.confirm('确定退出系统？', {
        btn: ['确定', '取消'], //按钮
        icon: 7,
        title: '提示信息',
        area: ['300px', '180px']
    }, function (id) {
        window.location.href = 'login.html';
    });
}

function editPass() {
    layer.open({
        type: 2,
        title: '修改密码',
        shadeClose: true,
        shade: 0.8,
        area: ['880px', '330px'],
        content: 'editPass.html'
    });
}