package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.SysConstants;
import com.housoo.platform.core.util.PlatDateTimeUtil;
import com.housoo.platform.core.dao.FullTextDao;
import com.housoo.platform.core.service.DictionaryService;
import com.housoo.platform.core.service.FullTextService;
import com.housoo.platform.core.util.LuceneUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.Term;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述 全文检索业务相关service实现类
 *
 * @author housoo
 * @version 1.0
 * @created 2017-07-01 10:15:45
 */
@Service("fullTextService")
public class FullTextServiceImpl extends BaseServiceImpl implements FullTextService {

    /**
     * 所引入的dao
     */
    @Resource
    private FullTextDao dao;
    /**
     *
     */
    @Resource
    private DictionaryService dictionayService;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     * 新增或者修改全文检索信息
     *
     * @param fullText
     * @param infoId
     * @return
     */
    public String saveOrUpdateCascadeIndex(Map<String, Object> fullText, String infoId) {
        fullText = dao.saveOrUpdate("PLAT_SYSTEM_FULLTEXT", fullText, SysConstants.ID_GENERATOR_UUID, null);
        String fullTextId = (String) fullText.get("FULLTEXT_ID");
        Document doc = this.getNewDocument(fullText, fullTextId);
        if (StringUtils.isNotEmpty(infoId)) {
            new LuceneUtil().updateDocument(doc, new Term("FULLTEXT_ID", infoId));
        } else {
            new LuceneUtil().addDocument(doc);
        }
        return fullTextId;
    }

    /**
     * @param fullTextInfo
     * @param recordId
     * @return
     */
    private Document getNewDocument(Map<String, Object> fullTextInfo, String recordId) {
        String FULLTEXT_INDEXTITLE = (String) fullTextInfo.get("FULLTEXT_INDEXTITLE");
        String FULLTEXT_CONTENT = (String) fullTextInfo.get("FULLTEXT_CONTENT");
        String FULLTEXT_TABLENAME = (String) fullTextInfo.get("FULLTEXT_TABLENAME");
        String FULLTEXT_RECORDID = (String) fullTextInfo.get("FULLTEXT_RECORDID");
        String FULLTEXT_TYPE = (String) fullTextInfo.get("FULLTEXT_TYPE");
        String FULLTEXT_PUBTIME = (String) fullTextInfo.get("FULLTEXT_PUBTIME");
        String FULLTEXT_URL = (String) fullTextInfo.get("FULLTEXT_URL");
        Date ORDER_DATE = PlatDateTimeUtil.formatStr(FULLTEXT_PUBTIME, "yyyy-MM-dd HH:mm:ss");
        Document doc = new Document();
        doc.add(new Field("FULLTEXT_ID", recordId, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        doc.add(new Field("FULLTEXT_TABLENAME", FULLTEXT_TABLENAME,
                Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        doc.add(new Field("FULLTEXT_RECORDID", FULLTEXT_RECORDID, Field.Store.YES,
                Field.Index.NOT_ANALYZED_NO_NORMS));
        doc.add(new Field("FULLTEXT_INDEXTITLE", FULLTEXT_INDEXTITLE,
                Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("FULLTEXT_CONTENT", FULLTEXT_CONTENT,
                Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("FULLTEXT_TYPE", FULLTEXT_TYPE,
                Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("FULLTEXT_PUBTIME", FULLTEXT_PUBTIME,
                Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        doc.add(new Field("FULLTEXT_URL", FULLTEXT_URL,
                Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        doc.add(new NumericField("ORDER_DATE", Field.Store.YES, true).setLongValue(ORDER_DATE.getTime()));
        return doc;
    }

    /**
     * 删除数据,并且级联删除索引
     *
     * @param fullTextIds
     */
    @Override
    public void deleteCascadeIndex(String[] fullTextIds) {
        for (String fullTextId : fullTextIds) {
            new LuceneUtil().deleteDocument(new Term("FULLTEXT_ID", fullTextId));
            dao.deleteRecords("PLAT_SYSTEM_FULLTEXT", "FULLTEXT_ID", new String[]{fullTextId});
        }
    }

    /**
     * 同步索引
     *
     * @param fullText
     * @param operType 操作类型1新增或者修改 3删除
     */
    @Override
    public void synchIndex(Map<String, Object> fullText, String operType) {
        String FULLTEXT_TABLENAME = (String) fullText.get("FULLTEXT_TABLENAME");
        String FULLTEXT_RECORDID = (String) fullText.get("FULLTEXT_RECORDID");
        String FULLTEXT_TYPE = (String) fullText.get("FULLTEXT_TYPE");
        if (StringUtils.isNotEmpty(FULLTEXT_TYPE)) {
            String URL = dictionayService.getAttachValue(FULLTEXT_TYPE, "fullinfotype",
                    "URL");
            fullText.put("FULLTEXT_URL", URL);
        }
        String fullTextId = dao.getId(FULLTEXT_TABLENAME, FULLTEXT_RECORDID);
        if ("1".equals(operType) || "2".equals(operType)) {
            if (StringUtils.isNotEmpty(fullTextId)) {
                this.saveOrUpdateCascadeIndex(fullText, fullTextId);
            } else {
                this.saveOrUpdateCascadeIndex(fullText, null);
            }
        } else if ("3".equals(operType)) {
            this.deleteCascadeIndex(new String[]{fullTextId});
        }
    }

    /**
     * 重建索引
     */
    @Override
    public void rebuildIndex() {
        new LuceneUtil().deleteAllDocuments();
        StringBuffer sql = new StringBuffer("SELECT * FROM PLAT_SYSTEM_FULLTEXT T");
        sql.append(" ORDER BY T.FULLTEXT_CREATETIME ASC");
        List<Map<String, Object>> list = dao.findBySql(sql.toString(), null, null);
        for (Map<String, Object> map : list) {
            String recordId = (String) map.get("FULLTEXT_ID");
            Document doc = this.getNewDocument(map, recordId);
            new LuceneUtil().addDocument(doc);
        }
    }

}
