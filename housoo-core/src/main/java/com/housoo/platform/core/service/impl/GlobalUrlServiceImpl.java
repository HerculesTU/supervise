package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.dao.GlobalUrlDao;
import com.housoo.platform.core.service.GlobalUrlService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 描述 全局URL业务相关service实现类
 *
 * @author gf
 * @version 1.0
 * @created 2017-04-23 09:22:54
 */
@Service("globalUrlService")
public class GlobalUrlServiceImpl extends BaseServiceImpl implements GlobalUrlService {

    /**
     * 所引入的dao
     */
    @Resource
    private GlobalUrlDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 根据过滤类型获取全局URL列表
     *
     * @param filterType
     * @return
     */
    @Override
    public List<String> findByFilterType(String filterType) {
        return dao.findByFilterType(filterType);
    }

    /**
     * 获取全部的可匿名访问的URL
     *
     * @return
     */
    @Override
    public Set<String> getAllAnonUrl() {
        Set<String> grantUrlSet = new HashSet<String>();
        //获取公共URL权限
        List<String> filterUrls = this.findByFilterType("1");
        grantUrlSet.addAll(filterUrls);
        return grantUrlSet;
    }

}
