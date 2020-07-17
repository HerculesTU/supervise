package com.housoo.platform.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatPropUtil;
import com.housoo.platform.core.web.socket.PlatWebSocket;
import com.housoo.platform.core.dao.ChatOnlineDao;
import com.housoo.platform.core.service.ChatOnlineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 用户分组业务相关service实现类
 *
 * @version 1.0
 * @created 2017-07-13 11:00:59
 */
@Service("chatOnlineService")
public class ChatOnlineServiceImpl extends BaseServiceImpl implements ChatOnlineService {

    /**
     * 所引入的dao
     */
    @Resource
    private ChatOnlineDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 描述
     *
     * @return
     * @created 2017年7月13日 下午3:46:12
     */
    @Override
    public Map<String, Object> getInitData() {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> userMap = PlatAppUtil.getBackPlatLoginUser();
        String SYSUSER_COMPANYID = (String) userMap.get("SYSUSER_COMPANYID");
        Map<String, Object> mine = getMineMap(userMap, true);
        mine.remove("avatar");
        mine.put("avatar", "plug-in/platform-1.0/images/onlinechat.png");
        result.put("mine", mine);
        List<Map<String, Object>> friend = this.findFriend(SYSUSER_COMPANYID, userMap.get("SYSUSER_ID").toString());
        result.put("friend", friend);
        List<Map<String, Object>> group = this.findGroup(userMap);
        result.put("group", group);
        return result;
    }

    /**
     * 描述
     *
     * @return
     * @created 2017年7月13日 下午4:53:14
     */
    public List<Map<String, Object>> findGroup(Map<String, Object> userMap) {
        String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
        String SYSUSER_ID = (String) userMap.get("SYSUSER_ID");
        List<Map<String, Object>> group = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer("");
        sql.append(" select T.* from PLAT_CHATONLINE_USERGROUP t ");
        sql.append(" WHERE T.USERGROUP_ID IN (SELECT M.USERGROUP_ID FROM PLAT_CHATONLINE_GROUPMEMBER M ");
        sql.append(" WHERE M.SYSUSER_ID=?) ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{SYSUSER_ID}, null);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("id", list.get(i).get("USERGROUP_ID"));
                m.put("groupname", list.get(i).get("USERGROUP_NAME"));
                String avatar = "";
                StringBuffer newsql = new StringBuffer("");
                newsql.append("select t.file_path from PLAT_SYSTEM_FILEATTACH t where t.file_busrecordid=? ");
                newsql.append(" AND T.file_bustablelname=? ");
                Map<String, Object> fileMap = dao.getBySql(newsql.toString(),
                        new Object[]{list.get(i).get("USERGROUP_ID")
                                , "PLAT_CHATONLINE_USERGROUP"});
                if (fileMap != null) {
                    avatar = attachFileUrl + (String) fileMap.get("FILE_PATH");
                } else {
                    avatar = "plug-in/platform-1.0/images/defaultuserphoto.png";
                }
                m.put("avatar", avatar);
                group.add(m);
            }
        }
        return group;
    }

    /**
     * 描述
     *
     * @param userMap
     * @param isMine
     * @created 2017年7月13日 下午4:32:38
     */
    @Override
    public Map<String, Object> getMineMap(Map<String, Object> userMap, boolean isMine) {
        String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
        Map<String, Object> mine = new HashMap<String, Object>();
        String SYSUSER_NAME = (String) userMap.get("SYSUSER_NAME");
        String SYSUSER_ID = (String) userMap.get("SYSUSER_ID");
        String SYSUSER_SIGN = (String) userMap.get("SYSUSER_SIGN");
        String avatar = "";
        StringBuffer sql = new StringBuffer("");
        sql.append("select t.file_path from PLAT_SYSTEM_FILEATTACH t where t.file_busrecordid=? ");
        sql.append(" AND T.file_bustablelname=? ");
        Map<String, Object> fileMap = dao.getBySql(sql.toString(), new Object[]{SYSUSER_ID, "PLAT_SYSTEM_SYSUSER"});
        if (fileMap != null) {
            avatar = attachFileUrl + (String) fileMap.get("FILE_PATH");
        } else {
            avatar = "plug-in/platform-1.0/images/defaultuserphoto.png";
        }
        mine.put("id", SYSUSER_ID);
        if (null == PlatWebSocket.webSocketMap.get(SYSUSER_ID) && (!isMine)) {
            mine.put("status", "offline");
        } else {
            mine.put("status", "online");
        }
        mine.put("avatar", avatar);
        mine.put("sign", SYSUSER_SIGN);
        mine.put("username", SYSUSER_NAME);
        return mine;
    }

    /**
     * 描述
     *
     * @param sysUserCompanyId
     * @return
     * @created 2017年7月13日 下午4:10:41
     */
    public List<Map<String, Object>> findFriend(String sysUserCompanyId, String userId) {
        List<Map<String, Object>> friend = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT T.* FROM PLAT_SYSTEM_DEPART T WHERE T.DEPART_COMPANYID=? ");
        sql.append(" ORDER BY T.DEPART_TREESN ASC ");
        List<Map<String, Object>> companyList = dao.findBySql(sql.toString(),
                new Object[]{sysUserCompanyId}, null);
        if (companyList != null && companyList.size() > 0) {
            for (int i = 0; i < companyList.size(); i++) {
                String DEPART_ID = (String) companyList.get(i).get("DEPART_ID");
                String DEPART_NAME = (String) companyList.get(i).get("DEPART_NAME");
                Map<String, Object> friendMap = new HashMap<String, Object>();
                friendMap.put("id", DEPART_ID);
                friendMap.put("groupname", DEPART_NAME);
                StringBuffer friendSql = new StringBuffer("");
                friendSql.append("SELECT U.* FROM PLAT_SYSTEM_SYSUSER U  ");
                friendSql.append("where U.SYSUSER_COMPANYID=? AND U.SYSUSER_DEPARTID=? ");
                friendSql.append("AND U.SYSUSER_ID!=? AND U.SYSUSER_STATUS=? ");
                friendSql.append(" ORDER BY U.SYSUSER_CREATETIME DESC ");
                List<Map<String, Object>> friendList = dao.findBySql(friendSql.toString(),
                        new Object[]{sysUserCompanyId, DEPART_ID, userId, "1"}, null);
                if (friendList != null && friendList.size() > 0) {
                    List<Map<String, Object>> mineList = new ArrayList<Map<String, Object>>();
                    for (int j = 0; j < friendList.size(); j++) {
                        mineList.add(getMineMap(friendList.get(j), false));
                    }
                    friendMap.put("list", mineList);
                }
                friend.add(friendMap);
            }
            Map<String, Object> friendMap = new HashMap<String, Object>();
            friendMap.put("id", "999999999");
            friendMap.put("groupname", "未分组用户");
            StringBuffer friendSql = new StringBuffer("");
            friendSql.append("SELECT U.* FROM PLAT_SYSTEM_SYSUSER U  ");
            friendSql.append("where U.SYSUSER_COMPANYID=? AND U.SYSUSER_DEPARTID IS NULL ");
            friendSql.append("AND U.SYSUSER_ID!=? AND U.SYSUSER_STATUS=? ");
            friendSql.append(" ORDER BY U.SYSUSER_CREATETIME DESC ");
            List<Map<String, Object>> friendList = dao.findBySql(friendSql.toString(),
                    new Object[]{sysUserCompanyId, userId, "1"}, null);
            if (friendList != null && friendList.size() > 0) {
                List<Map<String, Object>> mineList = new ArrayList<Map<String, Object>>();
                for (int j = 0; j < friendList.size(); j++) {
                    mineList.add(getMineMap(friendList.get(j), false));
                }
                friendMap.put("list", mineList);
            }
            friend.add(friendMap);

        }
        return friend;
    }

    /**
     * 描述 发送信息
     *
     * @param mine
     * @param to
     * @created 2017年7月15日 下午2:26:41
     */
    @Override
    public void sendMessageFromUserToUser(Map<String, Object> mine,
                                          Map<String, Object> to) {
        String type = (String) to.get("type");
        if ("friend".equals(type)) {
            String NEWS_ISSEDN = "0";
            String NEWS_TO_USERID = (String) to.get("id");
            String NEWS_FROM_USERID = (String) mine.get("id");
            if (PlatWebSocket.webSocketMap.get(NEWS_TO_USERID) != null) {
                NEWS_ISSEDN = "1";
                Map<String, Object> messageClintMap = new HashMap<String, Object>();
                messageClintMap.put("msgType", ChatOnlineService.CHATMESSAGE);
                Map<String, Object> newMessage = new HashMap<String, Object>();
                newMessage.put("username", mine.get("username"));
                newMessage.put("avatar", mine.get("avatar"));
                newMessage.put("id", mine.get("id"));
                newMessage.put("fromid", mine.get("id"));
                newMessage.put("type", "friend");
                newMessage.put("mine", false);
                newMessage.put("content", mine.get("content"));
                newMessage.put("timestamp", System.currentTimeMillis());
                messageClintMap.put("newMessage", newMessage);
                PlatWebSocket.sendMessage(NEWS_TO_USERID, JSON.toJSONString(messageClintMap));
            }
            String NEWS_INFO = (String) mine.get("content");
            Map<String, Object> news = new HashMap<String, Object>();
            news.put("NEWS_ISSEDN", NEWS_ISSEDN);
            news.put("NEWS_FROM_USERID", NEWS_FROM_USERID);
            news.put("NEWS_TO_USERID", NEWS_TO_USERID);
            news.put("NEWS_INFO", NEWS_INFO);
            news.put("NEWS_CREATETIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            dao.saveOrUpdate("PLAT_CHATONLINE_NEWS", news, SysConstants.ID_GENERATOR_UUID, null);
        } else {
            String GROUPNEWS_FROM_USERID = (String) mine.get("id");
            String USERGROUP_ID = (String) to.get("id");
            String GROUPNEWS_INFO = (String) mine.get("content");
            StringBuffer sql = new StringBuffer("");
            sql.append("select P.* FROM  PLAT_CHATONLINE_GROUPMEMBER P WHERE P.USERGROUP_ID=? ");
            sql.append("AND P.SYSUSER_ID!=? ");
            List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                    new Object[]{USERGROUP_ID, GROUPNEWS_FROM_USERID}, null);
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String SYSUSER_ID = list.get(i).get("SYSUSER_ID").toString();
                    if (null != PlatWebSocket.webSocketMap.get(SYSUSER_ID)) {
                        Map<String, Object> messageClintMap = new HashMap<String, Object>();
                        messageClintMap.put("msgType", ChatOnlineService.CHATMESSAGE);
                        Map<String, Object> newMessage = new HashMap<String, Object>();
                        newMessage.put("username", mine.get("username"));
                        newMessage.put("avatar", mine.get("avatar"));
                        newMessage.put("id", to.get("id"));
                        newMessage.put("fromid", mine.get("id"));
                        newMessage.put("type", to.get("type"));
                        newMessage.put("mine", false);
                        newMessage.put("content", mine.get("content"));
                        newMessage.put("timestamp", System.currentTimeMillis());
                        messageClintMap.put("newMessage", newMessage);
                        PlatWebSocket.sendMessage(SYSUSER_ID, JSON.toJSONString(messageClintMap));
                    } else {
                        Map<String, Object> offlineMap = new HashMap<String, Object>();
                        offlineMap.put("OFFLINENEWS_FROM_USERID", GROUPNEWS_FROM_USERID);
                        offlineMap.put("OFFLINENEWS_INFO", GROUPNEWS_INFO);
                        offlineMap.put("USERGROUP_ID", USERGROUP_ID);
                        offlineMap.put("OFFLINENEWS_TO_USERID", SYSUSER_ID);
                        offlineMap.put("OFFLINENEWS_ISSEDN", "0");
                        offlineMap.put("OFFLINENEWS_CREATETIME", PlatDateTimeUtil.formatDate(new Date(),
                                "yyyy-MM-dd HH:mm:ss"));
                        dao.saveOrUpdate("PLAT_CHATONLINE_OFFLINENEWS",
                                offlineMap, SysConstants.ID_GENERATOR_UUID, null);
                    }
                }
            }
            Map<String, Object> news = new HashMap<String, Object>();
            news.put("GROUPNEWS_FROM_USERID", GROUPNEWS_FROM_USERID);
            news.put("GROUPNEWS_INFO", GROUPNEWS_INFO);
            news.put("USERGROUP_ID", USERGROUP_ID);
            news.put("GROUPNEWS_CREATETIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
            dao.saveOrUpdate("PLAT_CHATONLINE_GROUPNEWS", news, SysConstants.ID_GENERATOR_UUID, null);
        }


    }

    /**
     * 描述 获取未读信息
     *
     * @param sysUser
     * @return
     * @created 2017年7月15日 下午4:02:17
     */
    @Override
    public List<Map<String, Object>> updateNoReadMsgUser(
            Map<String, Object> sysUser) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        String SYSUSER_ID = (String) sysUser.get("SYSUSER_ID");
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT T.*,U.* FROM PLAT_CHATONLINE_NEWS T ");
        sql.append("LEFT JOIN PLAT_SYSTEM_SYSUSER U ON U.SYSUSER_ID=T.NEWS_FROM_USERID ");
        sql.append("WHERE T.NEWS_TO_USERID=? AND T.NEWS_ISSEDN='0' ");
        sql.append("ORDER BY NEWS_CREATETIME ASC ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{SYSUSER_ID}, null);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> mine = this.getMineMap(list.get(i), false);
                Map<String, Object> messageClintMap = new HashMap<String, Object>();
                messageClintMap.put("msgType", ChatOnlineService.CHATMESSAGE);
                Map<String, Object> newMessage = new HashMap<String, Object>();
                newMessage.put("username", mine.get("username"));
                newMessage.put("avatar", mine.get("avatar"));
                newMessage.put("id", mine.get("id"));
                newMessage.put("fromid", mine.get("id"));
                newMessage.put("type", "friend");
                newMessage.put("mine", false);
                newMessage.put("content", list.get(i).get("NEWS_INFO"));
                newMessage.put("timestamp", PlatDateTimeUtil.formatStr(list.get(i).get("NEWS_CREATETIME").toString(),
                        "yyyy-MM-dd HH:mm:ss").getTime());
                messageClintMap.put("newMessage", newMessage);
                PlatWebSocket.sendMessage(SYSUSER_ID, JSON.toJSONString(messageClintMap));
                String updatesql = "update PLAT_CHATONLINE_NEWS set NEWS_ISSEDN='1' where NEWS_ID=? ";
                dao.executeSql(updatesql, new Object[]{list.get(i).get("NEWS_ID").toString()});
            }
        }
        StringBuffer groupSql = new StringBuffer("");
        groupSql.append("SELECT T.*,U.* FROM PLAT_CHATONLINE_OFFLINENEWS T  ");
        groupSql.append("LEFT JOIN PLAT_SYSTEM_SYSUSER U ON U.SYSUSER_ID=T.OFFLINENEWS_FROM_USERID ");
        groupSql.append("WHERE T.OFFLINENEWS_TO_USERID=?");
        groupSql.append(" AND T.OFFLINENEWS_ISSEDN='0' ORDER BY T.OFFLINENEWS_CREATETIME ASC ");
        List<Map<String, Object>> grouplist = dao.findBySql(groupSql.toString(), new Object[]{SYSUSER_ID}, null);
        if (grouplist != null && grouplist.size() > 0) {
            for (int i = 0; i < grouplist.size(); i++) {
                Map<String, Object> mine = this.getMineMap(grouplist.get(i), false);
                Map<String, Object> messageClintMap = new HashMap<String, Object>();
                messageClintMap.put("msgType", ChatOnlineService.CHATMESSAGE);
                Map<String, Object> newMessage = new HashMap<String, Object>();
                newMessage.put("username", mine.get("username"));
                newMessage.put("avatar", mine.get("avatar"));
                newMessage.put("id", grouplist.get(i).get("USERGROUP_ID").toString());
                newMessage.put("fromid", mine.get("id"));
                newMessage.put("type", "group");
                newMessage.put("mine", false);
                newMessage.put("content", grouplist.get(i).get("OFFLINENEWS_INFO"));
                newMessage.put("timestamp",
                        PlatDateTimeUtil.formatStr(grouplist.get(i).get("OFFLINENEWS_CREATETIME").toString(),
                                "yyyy-MM-dd HH:mm:ss").getTime());
                messageClintMap.put("newMessage", newMessage);
                PlatWebSocket.sendMessage(SYSUSER_ID, JSON.toJSONString(messageClintMap));
                String updatesql = "update PLAT_CHATONLINE_OFFLINENEWS set OFFLINENEWS_ISSEDN='1'"
                        + " where OFFLINENEWS_ID=? ";
                dao.executeSql(updatesql, new Object[]{grouplist.get(i).get("OFFLINENEWS_ID").toString()});
            }
        }

        return resultList;
    }

    /**
     * 描述
     *
     * @param sqlFilter
     * @return
     * @created 2017年7月15日 下午7:42:41
     */
    @Override
    public List<Map<String, Object>> findBySqlFilter(SqlFilter sqlFilter) {
        Map<String, Object> userMap = PlatAppUtil.getBackPlatLoginUser();
        String SYSUSER_ID = (String) userMap.get("SYSUSER_ID");
        String id = sqlFilter.getRequest().getParameter("id");
        String type = sqlFilter.getRequest().getParameter("type");
        String queryParam = sqlFilter.getRequest().getParameter("queryParam");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if ("friend".equals(type)) {
            List<Object> params = new ArrayList<Object>();
            sqlFilter.addFilter("O_T.NEWS_CREATETIME", "DESC", SqlFilter.FILTER_TYPE_ORDER);
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.*  FROM PLAT_CHATONLINE_NEWS T ");
            sql.append(" WHERE ((T.NEWS_FROM_USERID=? AND T.NEWS_TO_USERID=?) OR ");
            sql.append("  (T.NEWS_FROM_USERID=? AND T.NEWS_TO_USERID=?))  ");
            params.add(SYSUSER_ID);
            params.add(id);
            params.add(id);
            params.add(SYSUSER_ID);
            if (StringUtils.isNotEmpty(queryParam)) {
                sql.append(" AND T.NEWS_INFO LIKE ?  ");
                params.add("%" + queryParam + "%");
            }
            String exeSql = dao.getQuerySql(sqlFilter, sql.toString(), params);
            list = dao.findBySql(exeSql, params.toArray(),
                    sqlFilter.getPagingBean());
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String NEWS_FROM_USERID = (String) list.get(i).get("NEWS_FROM_USERID");
                    if (NEWS_FROM_USERID.equals(SYSUSER_ID)) {
                        list.get(i).put("ISMINE", true);
                        Map<String, Object> m = this.getMineMap(userMap, false);
                        list.get(i).putAll(m);
                    } else {
                        list.get(i).put("ISMINE", false);
                        //String  NEWS_TO_USERID =(String)list.get(i).get("NEWS_TO_USERID");
                        Map<String, Object> newuserMap = dao.getRecord("PLAT_SYSTEM_SYSUSER",
                                new String[]{"SYSUSER_ID"}, new Object[]{NEWS_FROM_USERID});
                        Map<String, Object> m = this.getMineMap(newuserMap, false);
                        list.get(i).putAll(m);
                    }
                }
            }
        } else {
            List<Object> params = new ArrayList<Object>();
            sqlFilter.addFilter("O_T.GROUPNEWS_CREATETIME", "DESC", SqlFilter.FILTER_TYPE_ORDER);
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.GROUPNEWS_FROM_USERID,T.GROUPNEWS_INFO as NEWS_INFO,  ");
            sql.append("t.GROUPNEWS_CREATETIME AS NEWS_CREATETIME,U.* ");
            sql.append(" FROM PLAT_CHATONLINE_GROUPNEWS T  ");
            sql.append("LEFT JOIN PLAT_SYSTEM_SYSUSER U ON T.GROUPNEWS_FROM_USERID=U.SYSUSER_ID ");
            sql.append(" WHERE T.USERGROUP_ID=? ");
            params.add(id);
            if (StringUtils.isNotEmpty(queryParam)) {
                sql.append(" AND T.GROUPNEWS_INFO LIKE ?  ");
                params.add("%" + queryParam + "%");
            }
            String exeSql = dao.getQuerySql(sqlFilter, sql.toString(), params);

            list = dao.findBySql(exeSql, params.toArray(),
                    sqlFilter.getPagingBean());
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String GROUPNEWS_FROM_USERID = (String) list.get(i).get("GROUPNEWS_FROM_USERID");
                    if (GROUPNEWS_FROM_USERID.equals(SYSUSER_ID)) {
                        list.get(i).put("ISMINE", true);
                    } else {
                        list.get(i).put("ISMINE", false);
                    }
                    userMap = dao.getRecord("PLAT_SYSTEM_SYSUSER",
                            new String[]{"SYSUSER_ID"}, new Object[]{GROUPNEWS_FROM_USERID});
                    Map<String, Object> m = this.getMineMap(userMap, false);
                    list.get(i).putAll(m);
                }
            }
        }

        return list;
    }

    /**
     * 描述 查询结果
     *
     * @param val
     * @return
     * @created 2017年7月16日 上午11:33:16
     */
    @Override
    public List<Map<String, Object>> findSearchGroupResult(String val) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT P.* FROM PLAT_CHATONLINE_USERGROUP P ");
        sql.append(" WHERE P.USERGROUP_NAME LIKE ? ");
        sql.append(" ORDER BY P.USERGROUP_CREATETIME ASC ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{"%" + val + "%"}, null);
        setUserGroupInfo(list);
        return list;
    }

    /**
     * 描述
     *
     * @param list
     * @created 2017年7月16日 下午12:08:37
     */
    public void setUserGroupInfo(List<Map<String, Object>> list) {
        if (list != null && list.size() > 0) {
            String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
            for (int i = 0; i < list.size(); i++) {
                String USERGROUP_ID = (String) list.get(i).get("USERGROUP_ID");
                String avatar = "";
                StringBuffer newsql = new StringBuffer("");
                newsql.append("select t.file_path from PLAT_SYSTEM_FILEATTACH t where t.file_busrecordid=? ");
                newsql.append(" AND T.file_bustablelname=? ");
                Map<String, Object> fileMap = dao.getBySql(newsql.toString(),
                        new Object[]{USERGROUP_ID, "PLAT_CHATONLINE_USERGROUP"});
                if (fileMap != null) {
                    avatar = attachFileUrl + (String) fileMap.get("FILE_PATH");
                } else {
                    avatar = "plug-in/platform-1.0/images/defaultuserphoto.png";
                }
                list.get(i).put("USERGROUP_IMG", avatar);
                StringBuffer numsql = new StringBuffer("");
                numsql.append("SELECT COUNT(P.GROUPMEMBER_ID) as NUM FROM PLAT_CHATONLINE_GROUPMEMBER P ");
                numsql.append(" WHERE P.USERGROUP_ID=? ");
                Map<String, Object> numMap = dao.getBySql(numsql.toString(), new Object[]{USERGROUP_ID});
                String USERGROUP_NUM = numMap.get("NUM").toString();
                list.get(i).put("USERGROUP_NUM", USERGROUP_NUM);
            }
        }
    }

    /**
     * 描述
     *
     * @return
     * @created 2017年7月16日 下午12:06:03
     * @see com.housoo.platform.core.service.ChatOnlineService#findMyGroupResult()
     */
    @Override
    public List<Map<String, Object>> findMyGroupResult() {
        Map<String, Object> userMap = PlatAppUtil.getBackPlatLoginUser();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT P.* FROM PLAT_CHATONLINE_USERGROUP P ");
        sql.append(" WHERE P.USERGROUP_ID IN (SELECT G.USERGROUP_ID ");
        sql.append(" FROM PLAT_CHATONLINE_GROUPMEMBER G WHERE G.SYSUSER_ID=?) ");
        sql.append(" ORDER BY P.USERGROUP_CREATETIME ASC ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{userMap.get("SYSUSER_ID").toString()}, null);
        setUserGroupInfo(list);
        return list;
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @created 2017年7月16日 下午2:54:21
     */
    @Override
    public boolean isCanAddGroup(String id) {
        boolean b = false;
        Map<String, Object> userMap = PlatAppUtil.getBackPlatLoginUser();
        String sysuserId = userMap.get("SYSUSER_ID").toString();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT P.* FROM PLAT_CHATONLINE_GROUPMEMBER P ");
        sql.append("WHERE P.USERGROUP_ID=? AND P.SYSUSER_ID=? ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{id, sysuserId}, null);
        if (list != null && list.size() > 0) {

        } else {
            b = true;
            Map<String, Object> memberMap = new HashMap<String, Object>();
            memberMap.put("USERGROUP_ID", id);
            memberMap.put("SYSUSER_ID", sysuserId);
            memberMap.put("GROUPMEMBER_ISMASTER", "0");
            dao.saveOrUpdate("PLAT_CHATONLINE_GROUPMEMBER", memberMap, SysConstants.ID_GENERATOR_UUID, null);

            StringBuffer gruopsql = new StringBuffer("");
            gruopsql.append("SELECT P.* FROM PLAT_CHATONLINE_USERGROUP P ");
            gruopsql.append(" WHERE P.USERGROUP_ID=? ");
            Map<String, Object> m = dao.getBySql(gruopsql.toString(), new Object[]{id});
            StringBuffer newsql = new StringBuffer("");
            newsql.append("select t.file_path from PLAT_SYSTEM_FILEATTACH t where t.file_busrecordid=? ");
            newsql.append(" AND T.file_bustablelname=? ");
            Map<String, Object> fileMap = dao.getBySql(newsql.toString(), new Object[]{id, "PLAT_CHATONLINE_USERGROUP"});
            String avatar = "";
            String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
            if (fileMap != null) {
                avatar = attachFileUrl + (String) fileMap.get("FILE_PATH");
            } else {
                avatar = "plug-in/platform-1.0/images/defaultuserphoto.png";
            }
            Map<String, Object> messageClintMap = new HashMap<String, Object>();
            messageClintMap.put("msgType", ChatOnlineService.APPLYGROUP);
            Map<String, Object> newMessage = new HashMap<String, Object>();
            newMessage.put("groupname", m.get("USERGROUP_NAME"));
            newMessage.put("avatar", avatar);
            newMessage.put("id", id);
            newMessage.put("type", "group");
            messageClintMap.put("newMessage", newMessage);
            PlatWebSocket.sendMessage(sysuserId, JSON.toJSONString(messageClintMap));

        }
        return b;
    }

    /**
     * 描述
     *
     * @param id
     * @return
     * @created 2017年7月16日 下午3:14:38
     */
    @Override
    public Map<String, Object> getMembers(String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT U.* FROM PLAT_CHATONLINE_GROUPMEMBER P ");
        sql.append("LEFT JOIN PLAT_SYSTEM_SYSUSER U ON P.SYSUSER_ID=U.SYSUSER_ID ");
        sql.append("WHERE P.USERGROUP_ID=? ORDER BY P.GROUPMEMBER_ISMASTER DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{id}, null);
        if (list != null && list.size() > 0) {
            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> m = this.getMineMap(list.get(i), false);
                data.add(m);
            }
            map.put("list", data);
        }
        return map;
    }

    /**
     * 描述
     *
     * @param id
     * @param systemuserId
     * @created 2017年7月16日 下午4:18:51
     */
    @Override
    public void removeGroup(String id, String systemuserId) {
        StringBuffer sql = new StringBuffer("");
        sql.append("delete PLAT_CHATONLINE_GROUPMEMBER  ");
        sql.append("where USERGROUP_ID=? and SYSUSER_ID=? ");
        dao.executeSql(sql.toString(), new Object[]{id, systemuserId});
        if (null != PlatWebSocket.webSocketMap.get(systemuserId)) {
            Map<String, Object> messageClintMap = new HashMap<String, Object>();
            messageClintMap.put("msgType", ChatOnlineService.REMOVEGROUP);
            Map<String, Object> newMessage = new HashMap<String, Object>();
            newMessage.put("id", id);
            newMessage.put("type", "group");
            messageClintMap.put("newMessage", newMessage);
            PlatWebSocket.sendMessage(systemuserId, JSON.toJSONString(messageClintMap));
        }

    }

    /**
     * 描述
     *
     * @param id
     * @created 2017年7月16日 下午4:45:14
     */
    @Override
    public void removeAllGroup(String id) {
        dao.deleteRecord("PLAT_CHATONLINE_USERGROUP", new String[]{"USERGROUP_ID"}, new Object[]{id});
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT U.* FROM PLAT_CHATONLINE_GROUPMEMBER P ");
        sql.append("LEFT JOIN PLAT_SYSTEM_SYSUSER U ON P.SYSUSER_ID=U.SYSUSER_ID ");
        sql.append("WHERE P.USERGROUP_ID=? ORDER BY P.GROUPMEMBER_ISMASTER DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{id}, null);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String systemuserId = list.get(i).get("SYSUSER_ID").toString();
                this.removeGroup(id, systemuserId);
            }
        }

    }

    /**
     * 描述
     *
     * @param id
     * @param checkUserIds
     * @return
     * @created 2017年7月17日 上午11:12:04
     */
    @Override
    public List<Map<String, Object>> invitationGroup(String id,
                                                     String checkUserIds) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String[] userId = checkUserIds.split(",");
        for (int i = 0; i < userId.length; i++) {
            if (PlatWebSocket.webSocketMap.get(userId[i]) != null) {
                StringBuffer gruopsql = new StringBuffer("");
                gruopsql.append("SELECT P.* FROM PLAT_CHATONLINE_USERGROUP P ");
                gruopsql.append(" WHERE P.USERGROUP_ID=? ");
                Map<String, Object> m = dao.getBySql(gruopsql.toString(), new Object[]{id});
                StringBuffer newsql = new StringBuffer("");
                newsql.append("select t.file_path from PLAT_SYSTEM_FILEATTACH t where t.file_busrecordid=? ");
                newsql.append(" AND T.file_bustablelname=? ");
                Map<String, Object> fileMap = dao.getBySql(newsql.toString(),
                        new Object[]{id, "PLAT_CHATONLINE_USERGROUP"});
                String avatar = "";
                String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
                if (fileMap != null) {
                    avatar = attachFileUrl + (String) fileMap.get("FILE_PATH");
                } else {
                    avatar = "plug-in/platform-1.0/images/defaultuserphoto.png";
                }
                Map<String, Object> messageClintMap = new HashMap<String, Object>();
                messageClintMap.put("msgType", ChatOnlineService.APPLYGROUP);
                Map<String, Object> newMessage = new HashMap<String, Object>();
                newMessage.put("groupname", m.get("USERGROUP_NAME"));
                newMessage.put("avatar", avatar);
                newMessage.put("id", id);
                newMessage.put("type", "group");
                messageClintMap.put("newMessage", newMessage);
                PlatWebSocket.sendMessage(userId[i], JSON.toJSONString(messageClintMap));
            }
            Map<String, Object> userMap = dao.getRecord("PLAT_SYSTEM_SYSUSER",
                    new String[]{"SYSUSER_ID"}, new Object[]{userId[i]});
            Map<String, Object> mine = this.getMineMap(userMap, false);
            list.add(mine);
            Map<String, Object> memberMap = new HashMap<String, Object>();
            memberMap.put("SYSUSER_ID", userId[i]);
            memberMap.put("USERGROUP_ID", id);
            memberMap.put("GROUPMEMBER_ISMASTER", "0");
            dao.saveOrUpdate("PLAT_CHATONLINE_GROUPMEMBER", memberMap, SysConstants.ID_GENERATOR_UUID, null);
        }
        return list;
    }

    /**
     * 描述
     *
     * @param msgContent
     * @created 2018年1月19日 上午10:39:40
     */
    @Override
    public void sendWebSocketMsg(String msgContent) {
        Map<String, Object> messageMap = JSON.parseObject(msgContent, Map.class);
        String msgType = (String) messageMap.get("msgType");
        if (msgType.equals(ChatOnlineService.CHATMESSAGE)) {
            Map<String, Object> data = (Map) messageMap.get("data");
            Map<String, Object> mine = (Map) data.get("mine");
            Map<String, Object> to = (Map) data.get("to");
            this.sendMessageFromUserToUser(mine, to);
        } else if (msgType.equals(ChatOnlineService.ONLINE)) {
            String clientId = (String) messageMap.get("clientId");
            Map<String, Object> messageClintMap = new HashMap<String, Object>();
            messageClintMap.put("msgType", ChatOnlineService.ONLINE);
            messageClintMap.put("onlineUserId", clientId);
            PlatWebSocket.sendAllClientsMsg(JSON.toJSONString(messageClintMap));
        } else if (msgType.equals(ChatOnlineService.OFFLINE)) {
            String clientId = (String) messageMap.get("clientId");
            Map<String, Object> messageClintMap = new HashMap<String, Object>();
            messageClintMap.put("msgType", ChatOnlineService.OFFLINE);
            messageClintMap.put("onlineUserId", clientId);
            PlatWebSocket.sendAllClientsMsg(JSON.toJSONString(messageClintMap));
        }

    }

    /**
     *
     */
    @Override
    public void deleteChatUser(Map<String, Object> sysUser) {
        String sysuserId = sysUser.get("SYSUSER_ID").toString();
        Map<String, Object> messageClintMap = new HashMap<String, Object>();
        messageClintMap.put("msgType", ChatOnlineService.REMOVEFRIEND);
        Map<String, Object> newMessage = new HashMap<String, Object>();
        newMessage.put("id", sysuserId);
        newMessage.put("type", "friend");
        messageClintMap.put("newMessage", newMessage);
        PlatWebSocket.sendAllClientsMsg(JSON.toJSONString(messageClintMap));
    }

    /**
     *
     */
    @Override
    public void addChatUser(Map<String, Object> sysUser) {
        String SYSUSER_DEPARTID = (String) sysUser.get("SYSUSER_DEPARTID");
        Map<String, Object> mine = this.getMineMap(sysUser, false);
        String sysuserId = sysUser.get("SYSUSER_ID").toString();
        Map<String, Object> messageClintMap = new HashMap<String, Object>();
        messageClintMap.put("msgType", ChatOnlineService.ADDFRIEND);
        mine.put("type", "friend");
        if (StringUtils.isNotEmpty(SYSUSER_DEPARTID)) {
            mine.put("groupid", SYSUSER_DEPARTID);
        } else {
            mine.put("groupid", "999999999");
        }
        messageClintMap.put("newMessage", mine);
        PlatWebSocket.sendAllClientsMsg(JSON.toJSONString(messageClintMap));

    }

}
