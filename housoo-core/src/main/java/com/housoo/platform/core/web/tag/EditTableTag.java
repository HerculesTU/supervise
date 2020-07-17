package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 描述自定义可编辑表格标签
 *
 * @author housoo
 * @created 2017年2月12日 下午2:49:25
 */
public class EditTableTag extends BaseCompTag {
    /**
     * 行模版路径
     */
    private String tr_tplpath;
    /**
     * 列样式值第一个值是列的宽度可以是百分比或者px,第二个值是列的名称
     * [20%,姓名],[20%,性别]
     */
    private String col_style;
    /**
     * 搜索面板ID
     */
    private String searchpanel_id;
    /**
     * 是否支持拖拽排序true false
     */
    private String dragable;

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
     * 方法doEndTag
     *
     * @return 返回值int
     */
    @Override
    public int doEndTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        Map<String, Object> paramMap = PlatBeanUtil.getSonAndSuperClassField(this);
        String htmlStr = PlatUICompUtil.getEditTableTagHtml(paramMap);
        try {
            out.print(htmlStr);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the tr_tplpath
     */
    public String getTr_tplpath() {
        return tr_tplpath;
    }

    /**
     * @param tr_tplpath the tr_tplpath to set
     */
    public void setTr_tplpath(String tr_tplpath) {
        this.tr_tplpath = tr_tplpath;
    }

    /**
     * @return the col_style
     */
    public String getCol_style() {
        return col_style;
    }

    /**
     * @param col_style the col_style to set
     */
    public void setCol_style(String col_style) {
        this.col_style = col_style;
    }


    /**
     * @return the searchpanel_id
     */
    public String getSearchpanel_id() {
        return searchpanel_id;
    }

    /**
     * @param searchpanel_id the searchpanel_id to set
     */
    public void setSearchpanel_id(String searchpanel_id) {
        this.searchpanel_id = searchpanel_id;
    }

    /**
     * @return the dragable
     */
    public String getDragable() {
        return dragable;
    }

    /**
     * @param dragable the dragable to set
     */
    public void setDragable(String dragable) {
        this.dragable = dragable;
    }

}
