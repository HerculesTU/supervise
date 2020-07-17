package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 描述 星级评定自定义控件
 *
 * @author housoo
 * @created 2017年6月16日 上午10:06:47
 */
public class RatingStarTag extends BaseCompTag {
    /**
     * 图标大小
     */
    private String datasize;
    /**
     * 每步大小
     */
    private String datastep;

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
        String htmlStr = PlatUICompUtil.getRatingStarTagHtml(paramMap);
        try {
            out.print(htmlStr);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the datasize
     */
    public String getDatasize() {
        return datasize;
    }

    /**
     * @param datasize the datasize to set
     */
    public void setDatasize(String datasize) {
        this.datasize = datasize;
    }

    /**
     * @return the datastep
     */
    public String getDatastep() {
        return datastep;
    }

    /**
     * @param datastep the datastep to set
     */
    public void setDatastep(String datastep) {
        this.datastep = datastep;
    }
}
