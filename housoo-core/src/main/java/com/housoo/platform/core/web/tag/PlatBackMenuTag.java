package com.housoo.platform.core.web.tag;

import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.service.SysUserService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 描述 代表后台菜单标签
 *
 * @author housoo
 * @created 2017年4月24日 下午5:47:42
 */
public class PlatBackMenuTag extends BaseCompTag {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 子系统编码
     */
    private String subsyscode;

    /**
     * @param htmlStr
     * @param res
     */
    private void getChildMenuHtml(StringBuffer htmlStr, Map<String, Object> res) {
        List<Map<String, Object>> childresList = (List<Map<String, Object>>) res.get("childres");
        String RES_ID = (String) res.get("RES_ID");
        String RES_MENUURL = (String) res.get("RES_MENUURL");
        String RES_MENUICON = (String) res.get("RES_MENUICON");
        String RES_NAME = (String) res.get("RES_NAME");
        if (childresList.size() > 0) {
            htmlStr.append("<li class=\"dropdown-submenu\">");
            htmlStr.append("<a data-id=\"");
            htmlStr.append(RES_ID).append("\" >");
            if (StringUtils.isNotEmpty(RES_MENUICON)) {
                htmlStr.append("<i class=\"").append(RES_MENUICON).append("\" ></i>");
            }
            htmlStr.append(RES_NAME);
            htmlStr.append("</a><ul class=\"dropdown-menu\">");
            for (Map<String, Object> childres : childresList) {
                this.getChildMenuHtml(htmlStr, childres);
            }
            htmlStr.append("</ul></li>");
        } else {
            htmlStr.append("<li><a data-id=\"");
            htmlStr.append(RES_ID).append("\" ");
            if (StringUtils.isNotEmpty(RES_MENUURL)) {
                htmlStr.append(" class=\"menuiframe\" href=\"").append(RES_MENUURL);
                htmlStr.append("\"  ");
            }
            htmlStr.append(" >");
            if (StringUtils.isNotEmpty(RES_MENUICON)) {
                htmlStr.append("<i class=\"").append(RES_MENUICON).append("\" ></i>");
            }
            htmlStr.append(RES_NAME).append("</a></li>");
        }
    }

    /**
     * 方法doEndTag
     *
     * @return 返回值int
     */
    @Override
    public int doEndTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        //ResService resService = (ResService)PlatAppUtil.getBean("resService");
        Map<String, Object> sysUser = PlatAppUtil.getBackPlatLoginUser();
        String SYSUSER_MENUTYPE = (String) PlatAppUtil.getRequest().getAttribute("SYSUSER_MENUTYPE");
        //String userId = (String)sysUser.get("SYSUSER_ID");
        //List<Map<String,Object>> resList = resService.findGrantResList(userId,subsyscode);
        List<Map<String, Object>> resList = (List<Map<String, Object>>) sysUser.get(SysUserService.GRANTRESLIST_KEY);
        StringBuffer htmlStr = new StringBuffer("<ul class=\"menu\">");
        int sn = 0;
        for (Map<String, Object> res : resList) {
            String RES_MENUICON = (String) res.get("RES_MENUICON");
            String RES_NAME = (String) res.get("RES_NAME");
            htmlStr.append("<li class=\"dropdown treeview ");
            if (sn == 0 && "2".equals(SYSUSER_MENUTYPE)) {
                htmlStr.append("open");
            }
            htmlStr.append("\">");
            htmlStr.append("<a data-id=\"").append(res.get("RES_ID")).append("\" ");
            htmlStr.append("data-toggle=\"dropdown\" data-submenu=\"\" aria-expanded=\"false\">");
            if (StringUtils.isNotEmpty(RES_MENUICON)) {
                htmlStr.append("<i class=\"").append(RES_MENUICON).append("\" ></i>");
            }
            htmlStr.append("<span>").append(RES_NAME).append("</span></a>");
            if ("1".equals(SYSUSER_MENUTYPE)) {
                htmlStr.append("<ul class=\"dropdown-menu\">");
                List<Map<String, Object>> childresList = (List<Map<String, Object>>) res.get("childres");
                for (Map<String, Object> childres : childresList) {
                    this.getChildMenuHtml(htmlStr, childres);
                }
                htmlStr.append("</ul>");
            }
            htmlStr.append("</li>");
            sn++;
        }
        htmlStr.append("</ul>");
        try {
            out.print(htmlStr.toString());
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return EVAL_PAGE;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the subsyscode
     */
    public String getSubsyscode() {
        return subsyscode;
    }

    /**
     * @param subsyscode the subsyscode to set
     */
    public void setSubsyscode(String subsyscode) {
        this.subsyscode = subsyscode;
    }
}
