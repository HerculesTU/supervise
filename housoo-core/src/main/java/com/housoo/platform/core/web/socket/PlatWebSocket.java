package com.housoo.platform.core.web.socket;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.util.PlatAppUtil;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author gf
 */
@ServerEndpoint("/platwebsocket")
public class PlatWebSocket {
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的PlatWebSocket对象。
     * 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
     */
    private static CopyOnWriteArraySet<PlatWebSocket> webSocketSet = new CopyOnWriteArraySet<PlatWebSocket>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     *
     */
    public static Map<String, Object> webSocketMap = new HashMap<String, Object>();

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        //System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1    
        //System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //System.out.println("来自客户端的消息:" + message+",会话ID:"+session.getId());
        Map<String, Object> messageMap = JSON.parseObject(message, Map.class);
        String clientId = (String) messageMap.get("clientId");
        String msgType = (String) messageMap.get("msgType");
        if (StringUtils.isNotEmpty(msgType) && "1".equals(msgType)) {
            PlatWebSocket.webSocketMap.put(clientId, session);
        } else if (StringUtils.isNotEmpty(msgType) && "2".equals(msgType)) {
            PlatWebSocket.webSocketMap.remove(clientId);
        } else {
            //获取消息内容
            String msgContent = (String) messageMap.get("msgContent");
            String invokeJavaInter = (String) messageMap.get("invokeJavaInter");
            String beanId = invokeJavaInter.split("[.]")[0];
            String method = invokeJavaInter.split("[.]")[1];
            Object serviceBean = PlatAppUtil.getBean(beanId);
            if (serviceBean != null) {
                Method invokeMethod;
                try {
                    invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                            new Class[]{String.class});
                    invokeMethod.invoke(serviceBean,
                            new Object[]{msgContent});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送消息给所有的客户端
     *
     * @param message
     */
    public static void sendAllClientsMsg(String message) {
        for (PlatWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        //error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 发送消息给某个特定的客户端
     *
     * @param message
     * @throws IOException
     */
    public static void sendMessage(String clientId, String message) {
        try {
            Session se = (Session) PlatWebSocket.webSocketMap.get(clientId);
            if (se != null) {
                se.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        PlatWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        PlatWebSocket.onlineCount--;
    }
}
