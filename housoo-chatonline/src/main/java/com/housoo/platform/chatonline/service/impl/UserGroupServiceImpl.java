package com.housoo.platform.chatonline.service.impl;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.util.PlatPropUtil;
import com.housoo.platform.core.web.socket.PlatWebSocket;
import com.housoo.platform.core.service.FileAttachService;
import com.housoo.platform.chatonline.dao.UserGroupDao;
import com.housoo.platform.core.service.ChatOnlineService;
import com.housoo.platform.chatonline.service.UserGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 描述 用户分组业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-07-13 10:59:23
 */
@Service("userGroupService")
public class UserGroupServiceImpl extends BaseServiceImpl implements UserGroupService {

    /**
     * 所引入的dao
     */
    @Resource
    private UserGroupDao dao;
    /**
     *
     */
    @Resource
    private FileAttachService fileAttachService;
    /**
     *
     */
    @Resource
    private ChatOnlineService chatOnlineService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 描述 保存或者群
     *
     * @param userGroup
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateGroup(Map<String, Object> userGroup) {
        String USERGROUP_ID = (String) userGroup.get("USERGROUP_ID");
        String sysuserId = PlatAppUtil.getBackPlatLoginUser().get("SYSUSER_ID").toString();
        userGroup.put("SYSUSER_ID", sysuserId);
        userGroup.put("USERGROUP_CREATETIME", PlatDateTimeUtil.formatDate(new Date(),
                "yyyy-MM-dd HH:mm:ss"));
        userGroup = this.saveOrUpdate("PLAT_CHATONLINE_USERGROUP",
                userGroup, SysConstants.ID_GENERATOR_UUID, null);
        if (StringUtils.isEmpty(USERGROUP_ID)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("SYSUSER_ID", sysuserId);
            map.put("USERGROUP_ID", userGroup.get("USERGROUP_ID"));
            map.put("GROUPMEMBER_ISMASTER", "1");
            this.saveOrUpdate("PLAT_CHATONLINE_GROUPMEMBER",
                    map, SysConstants.ID_GENERATOR_UUID, null);
        }
        String USER_PHOTO_JSON = (String) userGroup.get("USER_PHOTO_JSON");
        fileAttachService.saveFileAttachs(USER_PHOTO_JSON, "PLAT_CHATONLINE_USERGROUP",
                userGroup.get("USERGROUP_ID").toString(), "photo");


        StringBuffer newsql = new StringBuffer("");
        newsql.append("select t.file_path from PLAT_SYSTEM_FILEATTACH t where t.file_busrecordid=? ");
        newsql.append(" AND T.file_bustablelname=? ");
        Map<String, Object> fileMap = dao.getBySql(newsql.toString(), new Object[]{userGroup.get("USERGROUP_ID"),
                "PLAT_CHATONLINE_USERGROUP"});
        String avatar = "";
        String attachFileUrl = PlatPropUtil.getPropertyValue("config.properties", "attachFileUrl");
        if (fileMap != null) {
            avatar = attachFileUrl + (String) fileMap.get("FILE_PATH");
        } else {
            avatar = "plug-in/platform-1.0/images/defaultuserphoto.png";
        }
        Map<String, Object> messageClintMap = new HashMap<String, Object>();
        messageClintMap.put("msgType", ChatOnlineService.SAVEUPDATEGROUP);
        Map<String, Object> newMessage = new HashMap<String, Object>();
        newMessage.put("groupname", userGroup.get("USERGROUP_NAME"));
        newMessage.put("avatar", avatar);
        newMessage.put("id", userGroup.get("USERGROUP_ID").toString());
        newMessage.put("type", "group");
        messageClintMap.put("newMessage", newMessage);
        PlatWebSocket.sendMessage(sysuserId, JSON.toJSONString(messageClintMap));
        return userGroup;
    }

    /**
     * 描述 获取创建的组
     *
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getCreateMap(String id) {
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT U.* FROM PLAT_CHATONLINE_USERGROUP P ");
        sql.append("LEFT JOIN PLAT_SYSTEM_SYSUSER U ON P.SYSUSER_ID=U.SYSUSER_ID ");
        sql.append("WHERE P.USERGROUP_ID=? ");
        Map<String, Object> result = dao.getBySql(sql.toString(), new Object[]{id});
        result = chatOnlineService.getMineMap(result, false);
        return result;
    }

    /**
     * 描述 根据用户分组ID获取组成员
     *
     * @param id
     * @return
     */
    @Override
    public List<Map<String, Object>> findGroupMemberListMap(String id) {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT U.* FROM PLAT_CHATONLINE_GROUPMEMBER P ");
        sql.append("LEFT JOIN PLAT_SYSTEM_SYSUSER U ON P.SYSUSER_ID=U.SYSUSER_ID ");
        sql.append("WHERE P.USERGROUP_ID=? AND P.GROUPMEMBER_ISMASTER='0' ORDER BY P.GROUPMEMBER_ISMASTER DESC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(),
                new Object[]{id}, null);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> m = this.chatOnlineService.getMineMap(list.get(i), false);
                data.add(m);
            }
        }
        return data;
    }

    /**
     * 描述 根据groupId获取JSON树
     *
     * @param groupId
     * @return
     */
    @Override
    public String getTreeJsonByGroupId(String groupId) {
        StringBuffer sql = new StringBuffer("");
        sql.append("select U.SYSUSER_ID,U.SYSUSER_NAME,U.SYSUSER_ACCOUNT from PLAT_SYSTEM_SYSUSER U");
        sql.append(" LEFT JOIN PLAT_CHATONLINE_GROUPMEMBER R ON R.SYSUSER_ID=U.SYSUSER_ID");
        sql.append(" WHERE R.GROUPMEMBER_ISMASTER !='1' and r.usergroup_id=? ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{groupId}, null);
        Map<String, Object> rootNode = new HashMap<String, Object>();
        rootNode.put("id", "0");
        rootNode.put("name", "群成员");
        rootNode.put("open", true);
        List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : list) {
            // 获取ID值
            String id = map.get("SYSUSER_ID").toString();
            // 获取NAME值
            String name = map.get("SYSUSER_NAME").toString();
            // 获取NAME值
            String account = map.get("SYSUSER_ACCOUNT").toString();
            map.put("id", id);
            map.put("name", name + "(" + account + ")");
            children.add(map);
        }
        //rootNode.put("children", children);
        return JSON.toJSONString(children);
    }

    /**
     * 描述 查找自动完成
     *
     * @param sqlFilter
     * @return
     */
    @Override
    public List<Map<String, Object>> findAutoComplete(SqlFilter sqlFilter) {
        String groupId = sqlFilter.getRequest().getParameter("groupId");
        StringBuffer sql = new StringBuffer("");
        sql.append("select U.SYSUSER_NAME AS label,U.SYSUSER_NAME AS value from PLAT_SYSTEM_SYSUSER U");
        sql.append(" LEFT JOIN PLAT_CHATONLINE_GROUPMEMBER R ON R.SYSUSER_ID=U.SYSUSER_ID");
        sql.append(" WHERE R.GROUPMEMBER_ISMASTER !='1' and r.usergroup_id=? ");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), new Object[]{groupId}, null);
        return list;
    }

    /**
     * 描述 新增或者修改组
     *
     * @param userGroup
     * @return
     * @see com.housoo.platform.chatonline.service.UserGroupService#assignmentGroup(Map)
     */
    @Override
    public Map<String, Object> assignmentGroup(Map<String, Object> userGroup) {
        String groupId = (String) userGroup.get("groupId");
        String userId = (String) userGroup.get("userId");
        StringBuffer sql = new StringBuffer("");
        sql.append("update PLAT_CHATONLINE_USERGROUP set SYSUSER_ID=? ");
        sql.append(" where USERGROUP_ID=? ");
        dao.executeSql(sql.toString(), new Object[]{userId, groupId});
        sql = new StringBuffer("");
        sql.append("update PLAT_CHATONLINE_GROUPMEMBER set GROUPMEMBER_ISMASTER='0' ");
        sql.append(" where USERGROUP_ID=? AND GROUPMEMBER_ISMASTER='1'   ");
        dao.executeSql(sql.toString(), new Object[]{groupId});
        sql = new StringBuffer("");
        sql.append("update PLAT_CHATONLINE_GROUPMEMBER set GROUPMEMBER_ISMASTER='1' ");
        sql.append(" where SYSUSER_ID=? AND USERGROUP_ID=?   ");
        dao.executeSql(sql.toString(), new Object[]{userId, groupId});
        return userGroup;
    }

}
