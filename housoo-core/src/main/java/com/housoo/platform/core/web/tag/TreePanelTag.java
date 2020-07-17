package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 描述 属性面板标签
 *
 * @author housoo
 * @created 2017年2月10日 下午5:25:41
 */
public class TreePanelTag extends BaseCompTag {
    /**
     * 面板标题
     */
    private String panel_title;
    /**
     * 右键菜单,例子
     * [新增,fa-plus,showModuleWin,addModule][修改,fa-pencil,editDicTypeWin,editModule][删除,fa-trash-o,delDicType,delModule]
     * 第一个值是菜单项目的值,第二值是菜单项目的图标,第三个值是菜单项目响应事件,第四个值是菜单的key
     */
    private String right_menu;
    /**
     * 是否只能选择叶子节点
     * true,false
     */
    private String onlyselectleaf;
    /**
     * 是否有操作提示(true,false)
     */
    private String opertip;

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
        String htmlStr = PlatUICompUtil.getTreePanelTagHtml(paramMap);
        try {
            out.print(htmlStr);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the panel_title
     */
    public String getPanel_title() {
        return panel_title;
    }

    /**
     * @param panel_title the panel_title to set
     */
    public void setPanel_title(String panel_title) {
        this.panel_title = panel_title;
    }

    /**
     * @return the right_menu
     */
    public String getRight_menu() {
        return right_menu;
    }

    /**
     * @param right_menu the right_menu to set
     */
    public void setRight_menu(String right_menu) {
        this.right_menu = right_menu;
    }

    /**
     * @return the onlyselectleaf
     */
    public String getOnlyselectleaf() {
        return onlyselectleaf;
    }

    /**
     * @param onlyselectleaf the onlyselectleaf to set
     */
    public void setOnlyselectleaf(String onlyselectleaf) {
        this.onlyselectleaf = onlyselectleaf;
    }


    /**
     * @return the opertip
     */
    public String getOpertip() {
        return opertip;
    }

    /**
     * @param opertip the opertip to set
     */
    public void setOpertip(String opertip) {
        this.opertip = opertip;
    }

}
