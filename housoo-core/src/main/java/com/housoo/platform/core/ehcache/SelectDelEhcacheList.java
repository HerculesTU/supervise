package com.housoo.platform.core.ehcache;

import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.service.SysEhcacheService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 描述 获取刷新缓存器
 *
 * @author housoo
 * @created 2017年5月3日 下午3:09:57
 */
public class SelectDelEhcacheList extends RegexpMethodPointcutAdvisor {
    /**
     * log4j声明
     */
    private static Log log = LogFactory.getLog(SelectDelEhcacheList.class);
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
        log.debug("开始获取删除缓存列表----------------------");
        List<String> list = new ArrayList<String>();
        try {
            Set<String> elist = sysEhcacheService.findDelListByStatue("1");
            if (elist != null & elist.size() > 0) {
                int i = 0;
                for (String str : elist) {
                    //String estr = elist.get(i).get("EHCACHE_CLASS_NAME").toString().trim();
                    list.add(str);
                    log.debug("删除缓存" + i + ":" + str);
                    i++;
                }
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
        if (list.size() > 0) {
            patterns = list.toArray(new String[list.size()]);
        } else {
            patterns = new String[]{""};
        }
        super.setPatterns(patterns);
    }
}
