package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 描述 单选框自定义标签
 *
 * @author housoo
 * @created 2017年2月6日 下午1:11:31
 */
public class RadioTag extends BaseCompTag {
    /**
     * 是否横排(true,false)
     */
    private String is_horizontal;
    /**
     * 是否默认选中第一个(true,false)
     */
    private String select_first;

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
        String htmlStr = PlatUICompUtil.getRadioTagHtml(paramMap);
        try {
            out.print(htmlStr);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the select_first
     */
    public String getSelect_first() {
        return select_first;
    }

    /**
     * @param select_first the select_first to set
     */
    public void setSelect_first(String select_first) {
        this.select_first = select_first;
    }

    /**
     * @return the is_horizontal
     */
    public String getIs_horizontal() {
        return is_horizontal;
    }

    /**
     * @param is_horizontal the is_horizontal to set
     */
    public void setIs_horizontal(String is_horizontal) {
        this.is_horizontal = is_horizontal;
    }
}
