package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 描述 定制化引入资源标签
 *
 * @author housoo
 * @created 2017年1月25日 上午10:26:24
 */
public class ResourcesTag extends TagSupport {
    /**
     *
     */
    private static final String RES_BOOTSTRAP_SUBMENU = "bootstrap-submenu";
    /**
     *
     */
    private static final String RES_PLAT_MAIN = "plat-main";
    /**
     *
     */
    private static final String RES_PLAT_TAB = "plat-tab";
    /**
     *
     */
    private static final String RES_JQUERY_COOKIE = "jquery-cookie";
    /**
     *
     */
    private static final String RES_JQGRID = "jqgrid";
    /**
     *
     */
    private static final String RES_JEDATE = "jedate";
    /**
     *
     */
    private static final String RES_SELECT2 = "select2";
    /**
     *
     */
    private static final String RES_CHECKBOX = "bootstrap-checkbox";
    /**
     *
     */
    private static final String RES_JQUERYUI = "jquery-ui";
    /**
     *
     */
    private static final String RES_NICEVALID = "nicevalid";
    /**
     *
     */
    private static final String RES_ZTREE = "ztree";
    /**
     *
     */
    private static final String RES_BOOTSWITCH = "bootswitch";
    /**
     *
     */
    private static final String RES_TIPSY = "tipsy";
    /**
     *
     */
    private static final String RES_AUTOCOMPLETE = "autocomplete";
    /**
     *
     */
    private static final String RES_PINYIN = "pinyin";
    /**
     *
     */
    private static final String RES_SLIMSCROLL = "slimscroll";
    /**
     *
     */
    private static final String RES_FANCYBOX = "fancybox";
    /**
     *
     */
    private static final String RES_CODEMIRROR = "codemirror";
    /**
     * 百度上传控件
     */
    private static final String RES_WEBUPLOAD = "webuploader";
    /**
     * 流程设计控件
     */
    private static final String RES_FLOWDESIGN = "flowdesign";
    /**
     * 数字输入框插件
     */
    private static final String RES_TOUCHSPIN = "touchspin";
    /**
     * 百度echart控件
     */
    private static final String RES_ECHART = "echart";
    /**
     * 星级评定插件
     */
    private static final String RES_RATINGSTAR = "ratingstar";
    /**
     * 图片滑动插件
     */
    private static final String RES_SUPERSLIDE = "superslide";
    /**
     * 百度在线编辑器
     */
    private static final String RES_UEDITOR = "ueditor";
    /**
     * 客户端加密
     */
    private static final String RES_CRYPTOJS = "cryptojs";
    /**
     * 定义加载的资源
     */
    private String loadres;
    /**
     * 定义加载的资源类型
     */
    private String restype;

    /**
     * 方法doStartTag
     *
     * @return 返回值int
     */
    @Override
    public int doStartTag() throws JspException {
        return EVAL_PAGE;
    }

    /**
     * 加载js资源
     *
     * @param res
     * @return
     */
    private String getJsRes(Set<String> res) {
        StringBuffer jsRes = new StringBuffer("");
        jsRes.append("<script src=\"plug-in/bootstrap-3.3.5/js/bootstrap.min.js\"></script>");
        jsRes.append("<script src=\"plug-in/platform-1.0/js/dynamic.jsp\"></script>");

        if (res.contains(RES_BOOTSTRAP_SUBMENU)) {
            jsRes.append("<script src=\"plug-in/bootstrap-submenu-2.0.4/js/bootstrap-submenu.min.js\"></script>");
        }
        if (res.contains(RES_JQUERY_COOKIE)) {
            jsRes.append("<script src=\"plug-in/jquery-cookie-1.4.0/jquery.cookie.js\"></script>");
        }
        if (res.contains(RES_PLAT_TAB)) {
            jsRes.append("<script src=\"plug-in/platform-1.0/js/plat-tab.js\"></script>");
        }
        if (res.contains(RES_JQUERYUI)) {
            jsRes.append("<script src=\"plug-in/jqueryUI-1.10.2/jquery-ui.js\"></script>");
            jsRes.append("<script src=\"plug-in/jqueryUI-1.10.2/jquery.ui.touch-punch.min.js\"></script>");
        }
        if (res.contains(RES_JQGRID)) {
            jsRes.append("<script src=\"plug-in/jqgrid-4.6.0/grid.locale-cn.js\"></script>");
            jsRes.append("<script src=\"plug-in/jqgrid-4.6.0/jqgrid.js\"></script>");
        }
        if (res.contains(RES_JEDATE)) {
            jsRes.append("<script src=\"plug-in/jedate-3.7/jquery.jedate.min.js\"></script>");
        }
        if (res.contains(RES_SELECT2)) {
            jsRes.append("<script src=\"plug-in/select2-4.0.3/js/select2.full.min.js\"></script>");
            jsRes.append("<script src=\"plug-in/select2-4.0.3/js/zh-CN.js\"></script>");
        }
        jsRes.append("<script src=\"plug-in/jquery-layout-1.4.4/jquery.layout-latest.js\"></script>");
        jsRes.append("<script src=\"plug-in/layer-3.0.1/layer.js\"></script>");
        if (res.contains(RES_NICEVALID)) {
            jsRes.append("<script src=\"plug-in/nice-validator-1.0.9/jquery.validator.js\"></script>");
            jsRes.append("<script src=\"plug-in/nice-validator-1.0.9/jquery.validator.themes.js\"></script>");
            jsRes.append("<script src=\"plug-in/nice-validator-1.0.9/zh-CN.js\"></script>");
        }
        if (res.contains(RES_ZTREE)) {
            jsRes.append("<script src=\"plug-in/ztree-3.5/js/jquery.ztree.core-3.5.js\"></script>");
            jsRes.append("<script src=\"plug-in/ztree-3.5/js/jquery.ztree.excheck-3.5.js\"></script>");
            jsRes.append("<script src=\"plug-in/ztree-3.5/js/jquery.ztree.exedit-3.5.js\"></script>");
            jsRes.append("<script src=\"plug-in/ztree-3.5/js/jquery.ztree.exhide-3.5.js\"></script>");
        }
        if (res.contains(RES_BOOTSWITCH)) {
            jsRes.append("<script src=\"plug-in/bootstrap-switch-3.3.2/js/bootstrap-switch.min.js\"></script>");
        }
        if (res.contains(RES_TIPSY)) {
            jsRes.append("<script src=\"plug-in/tipsy-1.0/js/jquery.tipsy.js\"></script>");
        }
        if (res.contains(RES_AUTOCOMPLETE)) {
            jsRes.append("<script src=\"plug-in/jquery-autocomplete-1.1.2/jquery.autocomplete.js\"></script>");
        }
        if (res.contains(RES_PINYIN)) {
            jsRes.append("<script src=\"plug-in/jspinyin-0.1/pinyin.js\"></script>");
        }
        if (res.contains(RES_SLIMSCROLL)) {
            jsRes.append("<script src=\"plug-in/jquery-slimscroll-1.3.3/jquery.slimscroll.js\"></script>");
        }
        if (res.contains(RES_FANCYBOX)) {
            jsRes.append("<script src=\"plug-in/fancybox-2.1.5/jquery.fancybox.js\"></script>");
        }
        if (res.contains(RES_CODEMIRROR)) {
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/lib/codemirror.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/edit/closetag.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/fold/foldcode.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/edit/closebrackets.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/edit/matchbrackets.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/selection/active-line.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/display/fullscreen.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/dialog/dialog.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/search/searchcursor.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/search/search.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/scroll/annotatescrollbar.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/search/matchesonscrollbar.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/addon/search/jump-to-line.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/mode/xml/xml.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/mode/javascript/javascript.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/mode/css/css.js\"></script>");
            jsRes.append("<script src=\"plug-in/codemirror-5.24.2/mode/htmlmixed/htmlmixed.js\"></script>");
        }
        if (res.contains(RES_WEBUPLOAD)) {
            jsRes.append("<script src=\"plug-in/webuploader-0.1.5/dist/webuploader.js\"></script>");
            jsRes.append("<script src=\"plug-in/Jcrop-0.9.12/js/jquery.Jcrop.js\"></script>");
        }
        if (res.contains(RES_FLOWDESIGN)) {
            jsRes.append("<script src=\"plug-in/gojs-1.0/go.js\"></script>");
            jsRes.append("<script src=\"plug-in/gojs-1.0/LinkLabelDraggingTool.js\"></script>");
            jsRes.append("<script src=\"webpages/background/workflow/flowdef/js/design.js\"></script>");
        }
        if (res.contains(RES_TOUCHSPIN)) {
            jsRes.append("<script src=\"plug-in/bootstrap-touchspin-3.1.1/jquery.bo"
                    + "otstrap-touchspin.min.js\"></script>");
        }
        if (res.contains(RES_ECHART)) {
            jsRes.append("<script src=\"plug-in/echart-3.6/echarts.min.js"
                    + "\"></script>");
        }
        if (res.contains(RES_RATINGSTAR)) {
            jsRes.append("<script src=\"plug-in/bootstrap-star-rating-4.0.2/js/star-rating.min.js"
                    + "\"></script>");
            jsRes.append("<script src=\"plug-in/bootstrap-star-rating-4.0.2/js/locales/zh.js"
                    + "\"></script>");
        }
        if (res.contains(RES_SUPERSLIDE)) {
            jsRes.append("<script src=\"plug-in/superslide-2.1.1/jquery.SuperSlide.2.1.1.js\"></script>");
        }
        if (res.contains(RES_UEDITOR)) {
            jsRes.append("<script src=\"plug-in/ueditor-1.4.3/ueditor.config.js\"></script>");
            jsRes.append("<script src=\"plug-in/ueditor-1.4.3/ueditor.all.js\"></script>");
            jsRes.append("<script src=\"plug-in/ueditor-1.4.3/lang/zh-cn/zh-cn.js\"></script>");
        }
        if (res.contains(RES_CRYPTOJS)) {
            jsRes.append("<script src=\"plug-in/cryptojs-1.0/core.js\"></script>");
            jsRes.append("<script src=\"plug-in/cryptojs-1.0/sha256.js\"></script>");
            jsRes.append("<script src=\"plug-in/cryptojs-1.0/md5.js\"></script>");
        }
        jsRes.append("<script src=\"plug-in/bootstrap-contextmenu-1.0/bootstrap-contextmenu.js\"></script>");
        jsRes.append("<!--[if lt IE 9]>");
        jsRes.append("<script src=\"plug-in/platform-1.0/js/html5shiv.min.js\"></script>");
        jsRes.append("<script src=\"plug-in/platform-1.0/js/respond.min.js\"></script>");
        jsRes.append("<![endif]-->");
        jsRes.append("<script src=\"plug-in/platform-1.0/js/plat-util.js\"></script>");
        return jsRes.toString();
    }

    /**
     * 加载css资源
     *
     * @param res
     * @return
     */
    private String getCssRes(Set<String> res) {
        StringBuffer cssRes = new StringBuffer("");
        cssRes.append("<link href=\"plug-in/font-awesome-4.7.0/css/font-awesome.min.css\" rel=\"stylesheet\" />");
        cssRes.append("<link href=\"plug-in/bootstrap-3.3.5/css/bootstrap.min.css\" rel=\"stylesheet\" />");
        cssRes.append("<script src=\"plug-in/jquery-1.11.1/jquery-1.11.1.min.js\"></script>");
        if (res.contains(RES_BOOTSTRAP_SUBMENU)) {
            cssRes.append("<link href=\"plug-in/bootstrap-submenu-2.0.4/css/bootstrap-submenu.min.css\""
                    + " rel=\"stylesheet\" />");
        }
        if (res.contains(RES_PLAT_MAIN)) {
            cssRes.append("<link href=\"plug-in/platform-1.0/css/plat-main.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_CHECKBOX)) {
            cssRes.append("<link href=\"plug-in/bootstrap-checkbox-1.0/awesome-bootstrap-checkbox.css\" "
                    + "rel=\"stylesheet\" />");
        }
        if (res.contains(RES_JQUERYUI)) {
            cssRes.append("<link href=\"plug-in/jqueryUI-1.10.2/jquery-ui.min.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_JQGRID)) {
            cssRes.append("<link href=\"plug-in/jqgrid-4.6.0/jqgrid.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_JEDATE)) {
            cssRes.append("<link href=\"plug-in/jedate-3.7/skin/jedate.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_SELECT2)) {
            cssRes.append("<link href=\"plug-in/select2-4.0.3/css/select2.min.css\" rel=\"stylesheet\" />");
        }
        cssRes.append("<link href=\"plug-in/platform-1.0/css/plat-ui.css\" rel=\"stylesheet\" />");
        if (res.contains(RES_NICEVALID)) {
            cssRes.append("<link href=\"plug-in/nice-validator-1.0.9/jquery.validator.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_ZTREE)) {
            cssRes.append("<link href=\"plug-in/ztree-3.5/css/zTreeStyle/zTreeStyle.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_BOOTSWITCH)) {
            cssRes.append("<link href=\"plug-in/bootstrap-switch-3.3.2/"
                    + "css/bootstrap3/bootstrap-switch.min.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_TIPSY)) {
            cssRes.append("<link href=\"plug-in/tipsy-1.0/css/tipsy.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_AUTOCOMPLETE)) {
            cssRes.append("<link href=\"plug-in/jquery-autocomplete-1.1.2/"
                    + "jquery.autocomplete.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_FANCYBOX)) {
            cssRes.append("<link href=\"plug-in/fancybox-2.1.5/jquery.fancybox.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_CODEMIRROR)) {
            cssRes.append("<link href=\"plug-in/codemirror-5.24.2/lib/codemirror.css\" rel=\"stylesheet\" />");
            cssRes.append("<link href=\"plug-in/codemirror-5.24.2/addon/display/"
                    + "fullscreen.css\" rel=\"stylesheet\" />");
            cssRes.append("<link href=\"plug-in/codemirror-5.24.2/addon/dialog/dialog.css\" rel=\"stylesheet\" />");
            cssRes.append("<link href=\"plug-in/codemirror-5.24.2/"
                    + "addon/search/matchesonscrollbar.css\" rel=\"stylesheet\" />");
            cssRes.append("<link href=\"plug-in/codemirror-5.24.2/theme/blackboard.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_WEBUPLOAD)) {
            cssRes.append("<link href=\"plug-in/webuploader-0.1.5/css/webuploader.css\" rel=\"stylesheet\" />");
            cssRes.append("<link href=\"plug-in/Jcrop-0.9.12/css/jquery.Jcrop.css\" rel=\"stylesheet\" />");
            cssRes.append("<link href=\"plug-in/webuploader-0.1.5/imageUpload/style.css\" rel=\"stylesheet\" />");
            cssRes.append("<link href=\"plug-in/webuploader-0.1.5/imageUpload/websitestyle.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_FLOWDESIGN)) {
            cssRes.append("<link href=\"webpages/background/workflow/flowdef/css/design.css\" rel=\"stylesheet\" />");
        }
        if (res.contains(RES_TOUCHSPIN)) {
            cssRes.append("<link href=\"plug-in/bootstrap-touchspin-3.1.1/jquery.bootstrap-touchspin.min.css\" "
                    + "rel=\"stylesheet\" />");
        }
        if (res.contains(RES_RATINGSTAR)) {
            cssRes.append("<link href=\"plug-in/bootstrap-star-rating-4.0.2/css/star-rating.css\" "
                    + "rel=\"stylesheet\" />");
        }
        cssRes.append("<link href=\"plug-in/bootstrap-contextmenu-1.0"
                + "/bootstrap-contextmenu.css\" rel=\"stylesheet\" />");
        if (PlatAppUtil.getRequest() != null) {
            String themeColor = (String) PlatAppUtil.getRequest().getSession().getAttribute("SYSUSER_THEMECOLOR");
            if (StringUtils.isNotEmpty(themeColor) && !"red".equals(themeColor) && res.contains(RES_SELECT2)) {
                cssRes.append("<link href=\"plug-in/platform-1.0/css/plat-");
                cssRes.append(themeColor).append(".css\" rel=\"stylesheet\" />");
            }
        }

        return cssRes.toString();
    }

    /**
     * 方法doEndTag
     *
     * @return 返回值int
     */
    @Override
    public int doEndTag() throws JspException {
        try {
            JspWriter out = this.pageContext.getOut();
            StringBuffer sb = new StringBuffer();
            Set<String> res = new HashSet<String>();
            for (String resName : loadres.split(",")) {
                res.add(resName);
            }
            if ("css".equals(this.restype)) {
                sb.append(this.getCssRes(res));
            } else if ("js".equals(this.restype)) {
                sb.append(this.getJsRes(res));
            }
            out.print(sb.toString());
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the loadres
     */
    public String getLoadres() {
        return loadres;
    }

    /**
     * @param loadres the loadres to set
     */
    public void setLoadres(String loadres) {
        this.loadres = loadres;
    }

    /**
     * @return the restype
     */
    public String getRestype() {
        return restype;
    }

    /**
     * @param restype the restype to set
     */
    public void setRestype(String restype) {
        this.restype = restype;
    }

}
