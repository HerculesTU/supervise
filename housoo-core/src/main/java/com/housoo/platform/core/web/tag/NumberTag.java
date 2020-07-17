package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 描述 数字输入框标签
 *
 * @author housoo
 * @created 2017年5月9日 下午8:12:37
 */
public class NumberTag extends BaseCompTag {
    /**
     * 允许输入最大值
     */
    private String max;
    /**
     * 允许输入最小值
     */
    private String min;
    /**
     * 每步跨度值
     */
    private String step;
    /**
     * 小数点位数
     */
    private String decimals;
    /**
     * 前标签
     */
    private String prefix;
    /**
     * 后标签
     */
    private String postfix;

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
        String htmlStr = PlatUICompUtil.getNumberTagHtml(paramMap);
        try {
            out.print(htmlStr);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the max
     */
    public String getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(String max) {
        this.max = max;
    }

    /**
     * @return the min
     */
    public String getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(String min) {
        this.min = min;
    }

    /**
     * @return the step
     */
    public String getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(String step) {
        this.step = step;
    }

    /**
     * @return the decimals
     */
    public String getDecimals() {
        return decimals;
    }

    /**
     * @param decimals the decimals to set
     */
    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the postfix
     */
    public String getPostfix() {
        return postfix;
    }

    /**
     * @param postfix the postfix to set
     */
    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }
}
