package com.housoo.platform.core.ehcache;

import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.service.SysEhcacheService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年5月3日 下午3:01:23
 */
public class SelectEhcacheList extends RegexpMethodPointcutAdvisor {
    /**
     * log4j声明
     */
    private static Log log = LogFactory.getLog(SelectEhcacheList.class);
    /**
     * 引入Service
     */
    @Resource
    private SysEhcacheService sysEhcacheService;

    /**
     *
     */
    @Override
    public void setPatterns(String[] patterns) {
        log.debug("开始获取缓存列表----------------------");
        List<String> list = new ArrayList<String>();
        try {
            List<Map<String, Object>> elist = sysEhcacheService.findByStatue("1");
            if (elist != null & elist.size() > 0) {
                for (int i = 0; i < elist.size(); i++) {
                    String estr = elist.get(i).get("EHCACHE_CLASS_NAME").toString().trim();
                    list.add(estr);
                    log.debug("缓存" + i + ":" + estr);
                }
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
        if (list.size() > 0) {
            patterns = list.toArray(new String[list.size()]);
        }
        super.setPatterns(patterns);
    }
}
