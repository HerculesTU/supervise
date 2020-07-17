package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.WordTplDao;
import com.housoo.platform.core.service.WordTplService;
import com.housoo.platform.core.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 WORD模版业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-05-31 10:59:58
 */
@Service("wordTplService")
public class WordTplServiceImpl extends BaseServiceImpl implements WordTplService {

    /**
     * 所引入的dao
     */
    @Resource
    private WordTplDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据模版编码和参数生成模版
     *
     * @param tplCode
     * @param params
     * @return
     */
    @Override
    public String genWordByTplCodeAndParams(String tplCode, Map<String, Object> params) {
        Map<String, Object> wordTpl = dao.getRecord("PLAT_APPMODEL_WORDTPL",
                new String[]{"TPL_CODE"}, new Object[]{tplCode});
        String dyna_interface = (String) wordTpl.get("TPL_INTER");
        String TPL_CONTENT = (String) wordTpl.get("TPL_CONTENT");
        String beanId = dyna_interface.split("[.]")[0];
        String method = dyna_interface.split("[.]")[1];
        Object serviceBean = PlatAppUtil.getBean(beanId);
        Map<String, Object> rootData = new HashMap<String, Object>();
        if (serviceBean != null) {
            Method invokeMethod;
            try {
                invokeMethod = serviceBean.getClass().getDeclaredMethod(method,
                        new Class[]{Map.class});
                rootData = (Map<String, Object>) invokeMethod.invoke(serviceBean,
                        new Object[]{params});
            } catch (Exception e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        String uuid = UUIDGenerator.getUUID();
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        StringBuffer wordTplpath = new StringBuffer("genword/").append(uuid).append(".doc");
        String htmlCode = PlatStringUtil.getFreeMarkResult(TPL_CONTENT, rootData);
        PlatOfficeUtil.htmlCodeToWordByJacob(htmlCode, attachFilePath + wordTplpath);
        return wordTplpath.toString();
    }

}
