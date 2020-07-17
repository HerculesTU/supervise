package com.housoo.platform.core.service.impl;

import com.housoo.platform.core.dao.BaseDao;
import com.housoo.platform.core.util.PlatFileUtil;
import com.housoo.platform.core.util.PlatLogUtil;
import com.housoo.platform.core.util.PlatOfficeUtil;
import com.housoo.platform.core.dao.ImportWordDao;
import com.housoo.platform.core.service.ImportWordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * 描述 word导入业务相关service实现类
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-04-10 17:26:07
 */
@Service("importWordService")
public class ImportWordServiceImpl extends BaseServiceImpl implements ImportWordService {

    /**
     * 所引入的dao
     */
    @Resource
    private ImportWordDao dao;

    @Override
    protected BaseDao getDao() {
        return dao;
    }

    /**
     *
     */
    @Override
    public String getWordHtmlContent(String dbfilepath) {
        String fileExt = PlatFileUtil.getFileExt(dbfilepath);
        String wordHtmlContent = "";
        if ("doc".equals(fileExt)) {
            try {
                wordHtmlContent = PlatOfficeUtil.docToHtmlByPoi(dbfilepath);
            } catch (TransformerException e) {
                PlatLogUtil.printStackTrace(e);
            } catch (IOException e) {
                PlatLogUtil.printStackTrace(e);
            } catch (ParserConfigurationException e) {
                PlatLogUtil.printStackTrace(e);
            }
        } else if ("docx".equals(fileExt)) {
            try {
                wordHtmlContent = PlatOfficeUtil.docxToHtmlByPoi(dbfilepath);
            } catch (Exception e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        return wordHtmlContent;
    }

}
