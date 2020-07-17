package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 描述网格控件标签
 *
 * @author housoo
 * @created 2017年4月21日 上午9:37:33
 */
public class GridItemTag extends BaseCompTag {
    /**
     * 网格面板的标题
     */
    private String paneltitle;
    /**
     * 模版路径
     */
    private String itemtplpath;
    /**
     * 字体图标名称
     */
    private String iconfont;
    /**
     * 列的KEY,是否隐藏(1,-1),标签的名称
     * [colkey,ishide,label]
     */
    private String itemconf;
    /**
     * 是否支持拖拽排序true false
     */
    private String dragable;
    /**
     * 以选择的记录值
     */
    private String selectedrecordids;

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
        String htmlStr = PlatUICompUtil.getGridItemTagHtml(paramMap);
        try {
            out.print(htmlStr);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the itemtplpath
     */
    public String getItemtplpath() {
        return itemtplpath;
    }

    /**
     * @param itemtplpath the itemtplpath to set
     */
    public void setItemtplpath(String itemtplpath) {
        this.itemtplpath = itemtplpath;
    }

    /**
     * @return the iconfont
     */
    public String getIconfont() {
        return iconfont;
    }

    /**
     * @param iconfont the iconfont to set
     */
    public void setIconfont(String iconfont) {
        this.iconfont = iconfont;
    }

    /**
     * @return the itemconf
     */
    public String getItemconf() {
        return itemconf;
    }

    /**
     * @param itemconf the itemconf to set
     */
    public void setItemconf(String itemconf) {
        this.itemconf = itemconf;
    }

    /**
     * @return the paneltitle
     */
    public String getPaneltitle() {
        return paneltitle;
    }

    /**
     * @param paneltitle the paneltitle to set
     */
    public void setPaneltitle(String paneltitle) {
        this.paneltitle = paneltitle;
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


    /**
     * @return the selectedrecordids
     */
    public String getSelectedrecordids() {
        return selectedrecordids;
    }

    /**
     * @param selectedrecordids the selectedrecordids to set
     */
    public void setSelectedrecordids(String selectedrecordids) {
        this.selectedrecordids = selectedrecordids;
    }
}
