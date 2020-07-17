package com.housoo.platform.core.service;

import com.housoo.platform.core.model.SqlFilter;

import java.util.List;
import java.util.Map;

/**
 * 描述 用户分组业务相关service
 *
 * @version 1.0
 * @created 2017-07-13 11:00:59
 */
public interface ChatOnlineService extends BaseService {
    /**
     * 个人发送消息类型
     */
    public static String CHATMESSAGE = "chatMessage";
    /**
     * 加入群类型
     */
    public static String APPLYGROUP = "applyGroup";
    /**
     * 退出群类型
     */
    public static String REMOVEGROUP = "removeGroup";
    /**
     * 新增或者修改群信息
     */
    public static String SAVEUPDATEGROUP = "saveupdateGroup";

    /**
     * 上线
     */
    public static String ONLINE = "online";

    /**
     * 下线
     */
    public static String OFFLINE = "offline";
    /**
     * 删除用户
     */
    public static String REMOVEFRIEND = "removeFriend";
    /**
     * 新增用户
     */
    public static String ADDFRIEND = "addFriend";

    /**
     * 描述 获取初始化列表
     *
     * @return
     * @created 2017年7月13日 下午3:45:44
     */
    public Map<String, Object> getInitData();

    /**
     * 描述 发送信息
     *
     * @param mine
     * @param to
     * @created 2017年7月15日 下午2:26:24
     */
    public void sendMessageFromUserToUser(Map<String, Object> mine,
                                          Map<String, Object> to);

    /**
     * 描述
     *
     * @param sysUser
     * @return
     * @created 2017年7月15日 下午4:02:07
     */
    public List<Map<String, Object>> updateNoReadMsgUser(
            Map<String, Object> sysUser);

    /**
     * 描述 获取列表
     *
     * @param filter
     * @return
     * @created 2017年7月15日 下午7:42:26
     */
    public List<Map<String, Object>> findBySqlFilter(SqlFilter filter);

    /**
     * 描述查看结果
     *
     * @param val
     * @return
     * @created 2017年7月16日 上午11:32:54
     */
    public List<Map<String, Object>> findSearchGroupResult(String val);

    /**
     * 描述
     *
     * @return
     * @created 2017年7月16日 下午12:05:52
     */
    public List<Map<String, Object>> findMyGroupResult();

    /**
     * 描述
     *
     * @param id
     * @return
     * @created 2017年7月16日 下午2:54:10
     */
    public boolean isCanAddGroup(String id);

    /**
     * 描述
     *
     * @param id
     * @return
     * @created 2017年7月16日 下午3:14:26
     */
    public Map<String, Object> getMembers(String id);

    /**
     * 描述
     *
     * @param userMap
     * @param isMine
     * @return
     * @created 2017年7月16日 下午3:43:56
     */
    public Map<String, Object> getMineMap(Map<String, Object> userMap, boolean isMine);

    /**
     * 描述
     *
     * @param id
     * @param systemuserId
     * @created 2017年7月16日 下午4:18:40
     */
    public void removeGroup(String id, String systemuserId);

    /**
     * 描述
     *
     * @param id
     * @created 2017年7月16日 下午4:44:49
     */
    public void removeAllGroup(String id);

    /**
     * 描述
     *
     * @param id
     * @param checkUserIds
     * @return
     * @created 2017年7月17日 上午11:11:54
     */
    public List<Map<String, Object>> invitationGroup(String id,
                                                     String checkUserIds);

    /**
     * 推送消息
     *
     * @param msgContent
     */
    public void sendWebSocketMsg(String msgContent);

    /**
     * @param sysUser
     */
    public void deleteChatUser(Map<String, Object> sysUser);

    /**
     * @param sysUser
     */
    public void addChatUser(Map<String, Object> sysUser);

}
