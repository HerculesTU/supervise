package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 描述 自定义时间控件标签
 *
 * @author housoo
 * @created 2017年2月8日 下午8:39:10
 */
public class DateTimeTag extends BaseCompTag {

    /**
     * 是否精确到时间(true,false)
     */
    private String istime;
    /**
     * 最小日期
     */
    private String min_date;
    /**
     * 最大日期
     */
    private String max_date;
    /**
     * 格式化参数值
     */
    private String format;
    /**
     * 选择完成之后调用的回调函数
     */
    private String choosefun;
    /**
     * 用于范围限制控件,指的是开始时间控件的名称
     */
    private String start_name;
    /**
     * 用于范围限制控件,指的是结束时间控件的名称
     */
    private String end_name;
    /**
     * 提交时间格式
     */
    private String posttimefmt;
    /**
     * 默认当前时间
     */
    private String defaultnow;

    /**
     * @return the defaultnow
     */
    public String getDefaultnow() {
        return defaultnow;
    }

    /**
     * @param defaultnow the defaultnow to set
     */
    public void setDefaultnow(String defaultnow) {
        this.defaultnow = defaultnow;
    }

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
        String htmlStr = PlatUICompUtil.getDateTimeTagHtml(paramMap);
        try {
            out.print(htmlStr);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the istime
     */
    public String getIstime() {
        return istime;
    }

    /**
     * @param istime the istime to set
     */
    public void setIstime(String istime) {
        this.istime = istime;
    }

    /**
     * @return the min_date
     */
    public String getMin_date() {
        return min_date;
    }

    /**
     * @param min_date the min_date to set
     */
    public void setMin_date(String min_date) {
        this.min_date = min_date;
    }

    /**
     * @return the max_date
     */
    public String getMax_date() {
        return max_date;
    }

    /**
     * @param max_date the max_date to set
     */
    public void setMax_date(String max_date) {
        this.max_date = max_date;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the choosefun
     */
    public String getChoosefun() {
        return choosefun;
    }

    /**
     * @param choosefun the choosefun to set
     */
    public void setChoosefun(String choosefun) {
        this.choosefun = choosefun;
    }

    /**
     * @return the start_name
     */
    public String getStart_name() {
        return start_name;
    }

    /**
     * @param start_name the start_name to set
     */
    public void setStart_name(String start_name) {
        this.start_name = start_name;
    }

    /**
     * @return the end_name
     */
    public String getEnd_name() {
        return end_name;
    }

    /**
     * @param end_name the end_name to set
     */
    public void setEnd_name(String end_name) {
        this.end_name = end_name;
    }

    /**
     * @return the posttimefmt
     */
    public String getPosttimefmt() {
        return posttimefmt;
    }

    /**
     * @param posttimefmt the posttimefmt to set
     */
    public void setPosttimefmt(String posttimefmt) {
        this.posttimefmt = posttimefmt;
    }
}
