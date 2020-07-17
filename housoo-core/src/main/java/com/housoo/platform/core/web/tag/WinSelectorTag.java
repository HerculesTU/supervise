package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatBeanUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatUICompUtil;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.Map;

/**
 * 描述 弹出框选择器标签框
 *
 * @author housoo
 * @created 2017年2月27日 下午7:33:10
 */
public class WinSelectorTag extends BaseCompTag {

    /**
     * 选择器类型
     * checkbox,radio
     */
    private String checktype;
    /**
     * 允许最大选择器数量
     * //0标识不控制选择的条数
     */
    private String maxselect;
    /**
     * 至少需要选择的数量
     */
    private String minselect;
    /**
     * 选中时的级联方式ps
     * p代表关联父
     * s代表关联子
     */
    private String checkcascadey;
    /**
     * 取消选中的级联方式ps
     * p代表关联父
     * s代表关联子
     */
    private String checkcascaden;
    /**
     * 选择器的url
     */
    private String selectorurl;
    /**
     * 选择器的标题
     */
    private String title;
    /**
     * 选择器的宽度
     */
    private String width;
    /**
     * 选择器的高度
     */
    private String height;
    /**
     * 所选中记录的标签值
     */
    private String selectedlabels;
    /**
     * 验证规则
     */
    private String datarule;
    /**
     * 选择完成后的回调函数
     */
    private String callbackfn;

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
        String htmlStr = PlatUICompUtil.getWinSelectorTagHtml(paramMap);
        try {
            out.print(htmlStr);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the checktype
     */
    public String getChecktype() {
        return checktype;
    }

    /**
     * @param checktype the checktype to set
     */
    public void setChecktype(String checktype) {
        this.checktype = checktype;
    }

    /**
     * @return the maxselect
     */
    public String getMaxselect() {
        return maxselect;
    }

    /**
     * @param maxselect the maxselect to set
     */
    public void setMaxselect(String maxselect) {
        this.maxselect = maxselect;
    }

    /**
     * @return the minselect
     */
    public String getMinselect() {
        return minselect;
    }

    /**
     * @param minselect the minselect to set
     */
    public void setMinselect(String minselect) {
        this.minselect = minselect;
    }

    /**
     * @return the checkcascadey
     */
    public String getCheckcascadey() {
        return checkcascadey;
    }

    /**
     * @param checkcascadey the checkcascadey to set
     */
    public void setCheckcascadey(String checkcascadey) {
        this.checkcascadey = checkcascadey;
    }

    /**
     * @return the checkcascaden
     */
    public String getCheckcascaden() {
        return checkcascaden;
    }

    /**
     * @param checkcascaden the checkcascaden to set
     */
    public void setCheckcascaden(String checkcascaden) {
        this.checkcascaden = checkcascaden;
    }

    /**
     * @return the selectorurl
     */
    public String getSelectorurl() {
        return selectorurl;
    }

    /**
     * @param selectorurl the selectorurl to set
     */
    public void setSelectorurl(String selectorurl) {
        this.selectorurl = selectorurl;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the width
     */
    public String getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * @return the height
     */
    public String getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * @return the selectedlabels
     */
    public String getSelectedlabels() {
        return selectedlabels;
    }

    /**
     * @param selectedlabels the selectedlabels to set
     */
    public void setSelectedlabels(String selectedlabels) {
        this.selectedlabels = selectedlabels;
    }

    /**
     * @return the datarule
     */
    public String getDatarule() {
        return datarule;
    }

    /**
     * @param datarule the datarule to set
     */
    public void setDatarule(String datarule) {
        this.datarule = datarule;
    }

    /**
     * @return the callbackfn
     */
    public String getCallbackfn() {
        return callbackfn;
    }

    /**
     * @param callbackfn the callbackfn to set
     */
    public void setCallbackfn(String callbackfn) {
        this.callbackfn = callbackfn;
    }


}
