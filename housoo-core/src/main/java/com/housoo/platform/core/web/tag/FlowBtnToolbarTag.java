/*
package com.housoo.platform.core.web.tag;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.model.JbpmFlowInfo;
import com.housoo.platform.core.service.ButtonBindService;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

*/
/**
 * 描述 流程按钮工具栏
 *
 * @author housoo
 * @created 2017年5月14日 上午8:50:23
 * <p>
 * 方法doEndTag
 * @return 返回值int
 * <p>
 * 方法doEndTag
 * @return 返回值int
 *//*

public class FlowBtnToolbarTag extends BaseCompTag {

    */
/**
 * 方法doEndTag
 *
 * @return 返回值int
 *//*

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        Map<String, Object> paramMap = PlatBeanUtil.getSonAndSuperClassField(this);
        String tplPath = PlatAppUtil.getAppAbsolutePath() + "webpages/common/plattagtpl/flowbtntoolbar_tag.jsp";
        String tplString = PlatFileUtil.readFileString(tplPath);
        JbpmFlowInfo jbpmFlowInfo = (JbpmFlowInfo) PlatAppUtil.getRequest().getAttribute("jbpmFlowInfo");
        ButtonBindService buttonBindService = (ButtonBindService) PlatAppUtil.getBean("buttonBindService");
        List<Map<String, Object>> buttonList = buttonBindService.findButtonBind(jbpmFlowInfo);
        paramMap.put("buttonList", buttonList);
        String jbpmFlowInfoJson = JSON.toJSONString(jbpmFlowInfo);
        paramMap.put("JbpmFlowInfoJson", StringEscapeUtils.escapeHtml3(jbpmFlowInfoJson));
        String htmlContent = PlatStringUtil.getFreeMarkResult(tplString, paramMap);
        try {
            out.print(htmlContent);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }
}
*/
