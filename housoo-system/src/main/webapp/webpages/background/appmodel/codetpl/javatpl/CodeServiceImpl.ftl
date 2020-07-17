/*
 * Copyright (c) 2000 www.cnnetchina.com All rights reserved.18888
 * ZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.housoo.platform.${PACK_NAME}.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.service.impl.BaseServiceImpl;
import com.housoo.platform.${PACK_NAME}.dao.${CLASS_NAME}Dao;
import com.housoo.platform.${PACK_NAME}.service.${CLASS_NAME}Service;

/**
 * 描述 ${CN_NAME}业务相关service实现类
 *
 * @author ${AUTHOR}
 * @version 1.0
 * @created ${FILE_CREATETIME}
 */
@Service("${CLASS_NAME?uncap_first}Service")
public class ${CLASS_NAME}ServiceImpl extends BaseServiceImpl implements ${CLASS_NAME}Service {

    /**
     * 所引入的dao
     */
    @Resource
    private ${CLASS_NAME}Dao dao;

    /**
     * 获取dao
     */
    @Override
    protected BaseDao getDao() {
        return dao;
    }
  
}
