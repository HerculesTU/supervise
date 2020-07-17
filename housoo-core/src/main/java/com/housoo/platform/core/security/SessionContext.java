package com.housoo.platform.core.security;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * @author housoo
 */
public class SessionContext {
    /**
     *
     */
    private static HashMap myMap = new HashMap();

    /**
     * @param session
     */
    public static synchronized void addSession(HttpSession session) {
        if (session != null) {
            myMap.put(session.getId(), session);
        }
    }

    /**
     * @param session
     */
    public static synchronized void delSession(HttpSession session) {
        if (session != null) {
            myMap.remove(session.getId());
        }
    }

    /**
     * @param session_id
     * @return
     */
    public static synchronized HttpSession getSession(String session_id) {
        if (session_id == null) {
            return null;
        }
        return (HttpSession) myMap.get(session_id);
    }

}
