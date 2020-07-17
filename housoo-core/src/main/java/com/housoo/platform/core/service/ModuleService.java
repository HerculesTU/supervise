package com.housoo.platform.core.service;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年2月4日 上午9:22:58
 */
public interface ModuleService extends BaseService {

    /**
     * 删除模块信息，并且级联更新设计的编码
     *
     * @param moduleId
     */
    public void deleteModuleCascade(String moduleId);

}
