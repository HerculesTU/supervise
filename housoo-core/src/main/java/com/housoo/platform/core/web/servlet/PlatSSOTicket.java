package com.housoo.platform.core.web.servlet;

import com.housoo.platform.core.util.SysConstants;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author housoo
 * 产生票据的servlet
 */
@WebServlet(name = "PlatSSOTicket", urlPatterns = "/PlatSSOTicket")
public class PlatSSOTicket extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ticket = request.getParameter("ticket");
        String userAccount = SysConstants.SSO_TICKET_AND_NAME.get(ticket);
        SysConstants.SSO_TICKET_AND_NAME.remove(ticket);
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotEmpty(userAccount)) {
            pw.write(userAccount);
        } else {
            pw.write("");
        }
        pw.flush();
        pw.close();
    }
}
