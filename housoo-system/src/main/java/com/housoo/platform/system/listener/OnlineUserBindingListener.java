package com.housoo.platform.system.listener;

import com.housoo.platform.core.security.SessionContext;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatLogUtil;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;

/**
 * @author housoo 在线用户列表监听器
 */
public class OnlineUserBindingListener implements HttpSessionListener {

    /**
     *
     */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        SessionContext.addSession(event.getSession());
    }

    /**
     *
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        String sessionId = session.getId();
        try {
            Map<String, Map<String, Object>> onlineUser = PlatAppUtil.getOnlineUser();
            if (onlineUser.get(sessionId) != null) {
                onlineUser.put(sessionId, null);
            }
        } catch (Exception e) {
            PlatLogUtil.doNothing(e);
        }

    }
}
