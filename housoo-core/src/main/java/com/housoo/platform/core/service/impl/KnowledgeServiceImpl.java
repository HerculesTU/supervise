package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.dao.KnowledgeDao;
import com.housoo.platform.core.service.KnowledgeService;
import com.housoo.platform.core.service.FullTextService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述 技术知识业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2017-06-29 09:57:28
 */
@Service("knowledgeService")
public class KnowledgeServiceImpl extends BaseServiceImpl implements KnowledgeService {

    /**
     * 所引入的dao
     */
    @Resource
    private KnowledgeDao dao;
    /**
     *
     */
    @Resource
    private FullTextService fullTextService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 保存或者更新并且级联索引
     *
     * @param knowledage
     * @return
     */
    @Override
    public Map<String, Object> saveOrUpdateCascadeIndex(Map<String, Object> knowledage) {
        String KNOWLEDGE_ID = (String) knowledage.get("KNOWLEDGE_ID");
        if (StringUtils.isEmpty(KNOWLEDGE_ID)) {
            knowledage.put("KNOWLEDGE_CREATETIME", PlatDateTimeUtil.formatDate(new Date(),
                    "yyyy-MM-dd HH:mm:ss"));
        }
        //先保存业务表数据
        knowledage = dao.saveOrUpdate("PLAT_APPMODEL_KNOWLEDGE",
                knowledage, SysConstants.ID_GENERATOR_UUID, null);
        KNOWLEDGE_ID = (String) knowledage.get("KNOWLEDGE_ID");
        //开始创建索引
        Map<String, Object> fullText = new HashMap<String, Object>();
        //涉及的业务主表名称
        fullText.put("FULLTEXT_TABLENAME", "PLAT_APPMODEL_KNOWLEDGE");
        //涉及的业务主表记录ID
        fullText.put("FULLTEXT_RECORDID", KNOWLEDGE_ID);
        //作为索引标题的字段
        fullText.put("FULLTEXT_INDEXTITLE", knowledage.get("KNOWLEDGE_TITLE"));
        //作为索引内容的字段
        fullText.put("FULLTEXT_CONTENT", knowledage.get("KNOWLEDGE_CONTENT"));
        //信息类别,查看字典类别(字典类别:fullinfotype)
        fullText.put("FULLTEXT_TYPE", "1");
        //索引的发布时间
        fullText.put("FULLTEXT_PUBTIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        fullText.put("FULLTEXT_CREATETIME", PlatDateTimeUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
        //创建索引的文档对象
        fullTextService.synchIndex(fullText, FullTextService.OPERTYPE_SAVEORUPDATE);
        return knowledage;
    }

    /**
     * 删除并且级联删除索引
     *
     * @param knowledageIds
     */
    @Override
    public void deleteCascadeIndex(String[] knowledageIds) {
        dao.deleteRecords("PLAT_APPMODEL_KNOWLEDGE", "KNOWLEDGE_ID", knowledageIds);
        for (String knowledageId : knowledageIds) {
            Map<String, Object> fullText = new HashMap<String, Object>();
            fullText.put("FULLTEXT_TABLENAME", "PLAT_APPMODEL_KNOWLEDGE");
            fullText.put("FULLTEXT_RECORDID", knowledageId);
            //调用API同步索引
            fullTextService.synchIndex(fullText, FullTextService.OPERTYPE_DEL);
        }
    }

}
