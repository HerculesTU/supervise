package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年2月6日 下午3:14:23
 */
public class TextAreaTag extends BaseCompTag {

    /**
     * 最大输入字符数
     */
    private String maxlength;
    /**
     * 最少输入字符数
     */
    private String minlength;
    /**
     * 按钮配置信息
     * [新增,showModuleWin][修改,editDicTypeWin][删除,delDicType]
     */
    private String buttonconfigs;
    /**
     * 是否支持codemirror:传值codemirror
     */
    private String codemirror;
    /**
     * codemirror的宽度,可以是auto或px
     */
    private String mirrorwidth;
    /**
     * codemirror的高度,可以是auto或px
     */
    private String mirrorheight;

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
        String htmlStr = PlatUICompUtil.getTextareaTagHtml(paramMap);
        try {
            out.print(htmlStr);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the maxlength
     */
    public String getMaxlength() {
        return maxlength;
    }

    /**
     * @param maxlength the maxlength to set
     */
    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    /**
     * @return the minlength
     */
    public String getMinlength() {
        return minlength;
    }

    /**
     * @param minlength the minlength to set
     */
    public void setMinlength(String minlength) {
        this.minlength = minlength;
    }

    /**
     * @return the buttonconfigs
     */
    public String getButtonconfigs() {
        return buttonconfigs;
    }

    /**
     * @param buttonconfigs the buttonconfigs to set
     */
    public void setButtonconfigs(String buttonconfigs) {
        this.buttonconfigs = buttonconfigs;
    }

    /**
     * @return the codemirror
     */
    public String getCodemirror() {
        return codemirror;
    }

    /**
     * @param codemirror the codemirror to set
     */
    public void setCodemirror(String codemirror) {
        this.codemirror = codemirror;
    }

    /**
     * @return the mirrorwidth
     */
    public String getMirrorwidth() {
        return mirrorwidth;
    }

    /**
     * @param mirrorwidth the mirrorwidth to set
     */
    public void setMirrorwidth(String mirrorwidth) {
        this.mirrorwidth = mirrorwidth;
    }

    /**
     * @return the mirrorheight
     */
    public String getMirrorheight() {
        return mirrorheight;
    }

    /**
     * @param mirrorheight the mirrorheight to set
     */
    public void setMirrorheight(String mirrorheight) {
        this.mirrorheight = mirrorheight;
    }

}
