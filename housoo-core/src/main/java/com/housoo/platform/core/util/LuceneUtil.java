package com.housoo.platform.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.housoo.platform.core.model.PagingBean;

/**
 * 描述
 *
 * @author housoo
 * @created 2017年7月1日 上午10:35:26
 */
public class LuceneUtil {
    /**
     * 目录
     */
    private Directory directory = null;
    /**
     *
     */
    private IndexWriter writer = null;
    /**
     * 查询最大数量
     */
    public static final int QUERY_MAX_NUM = 1000000;
    /**
     * 最大显示内容长度
     */
    public static final int SHOW_CONTENT_MAXLENGTH = 400;
    /**
     *
     */
    public static final String SIGN_VALUE = "Ÿ";

    /**
     * 构造方法
     */
    public LuceneUtil() {
        try {
            // 定义上传目录的根路径
            Properties properties = PlatPropUtil.readProperties("config.properties");
            String indexFolderPath = properties.getProperty("attachFilePath") + "luceneindex";
            directory = FSDirectory.open(new File(indexFolderPath));
            try {
                if (IndexWriter.isLocked(directory)) {
                    IndexWriter.unlock(directory);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                PlatLogUtil.printStackTrace(e);
            }
            writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new MMSegAnalyzer()));
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 获取所有的文档对象
     *
     * @return
     */
    public List<Document> getAll() {
        IndexReader reader = null;
        IndexSearcher searcher = null;
        List<Document> list = new ArrayList<Document>();
        try {
            reader = IndexReader.open(directory);
            searcher = new IndexSearcher(reader);
            TermQuery query = new TermQuery(new Term("SIGNVALUE", "2"));
            Sort sort = new Sort(new SortField("ORDER_DATE", SortField.LONG, true));
            TopDocs tds = searcher.search(query, LuceneUtil.QUERY_MAX_NUM, sort);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                list.add(doc);
            }
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            try {
                reader.close();
                searcher.close();
            } catch (IOException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
        return list;
    }

    /**
     * 添加文档
     *
     * @param document
     */
    public void addDocument(Document document) {
        try {
            document.add(new Field("SIGNVALUE", LuceneUtil.SIGN_VALUE,
                    Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
            writer.addDocument(document);
            writer.close();
        } catch (CorruptIndexException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (LockObtainFailedException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (FileNotFoundException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 更新文档
     *
     * @param document
     * @param term
     */
    public void updateDocument(Document document, Term term) {
        IndexReader reader = null;
        IndexSearcher searcher = null;
        try {
            reader = IndexReader.open(directory);
            searcher = new IndexSearcher(reader);
            TermQuery query = new TermQuery(term);
            TopDocs tds = searcher.search(query, 1);
            if (tds.scoreDocs.length != 0) {
                document.add(new Field("SIGNVALUE", LuceneUtil.SIGN_VALUE,
                        Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
                writer.updateDocument(term, document);
            }
        } catch (CorruptIndexException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (LockObtainFailedException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (FileNotFoundException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                searcher.close();
            } catch (CorruptIndexException e) {
                PlatLogUtil.printStackTrace(e);
            } catch (IOException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 删除索引
     *
     * @param term
     */
    public void deleteDocument(Term term) {
        try {
            writer.deleteDocuments(term);
            writer.commit();
            //nrtMgr.deleteDocuments(new Term("DOC_ID",docId));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (CorruptIndexException e) {
                PlatLogUtil.printStackTrace(e);
            } catch (IOException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 删除所有索引
     */
    public void deleteAllDocuments() {
        try {
            writer.deleteAll();
            writer.close();
        } catch (CorruptIndexException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 高亮字符串
     *
     * @param a
     * @param query
     * @param txt
     * @param fieldname
     * @return
     * @throws IOException
     * @throws InvalidTokenOffsetsException
     */
    private String lighterStr(Analyzer a, Query query, String txt, String fieldname) throws
            IOException, InvalidTokenOffsetsException {
        String str = null;
        QueryScorer scorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        Formatter fmt = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        Highlighter lighter = new Highlighter(fmt, scorer);
        lighter.setTextFragmenter(fragmenter);
        str = lighter.getBestFragments(a.tokenStream(fieldname, new StringReader(txt)), txt, 3, "......\n");
        if (StringUtils.isEmpty(str)) {
            if (txt.length() > LuceneUtil.SHOW_CONTENT_MAXLENGTH) {
                txt = txt.substring(0, LuceneUtil.SHOW_CONTENT_MAXLENGTH);
                txt += "...";
                return txt;
            } else {
                return txt;
            }
        } else {
            return str;
        }
    }

    /**
     * 获取数据列表
     *
     * @param queryText
     * @param infoType
     * @param pb
     * @return
     */
    public List<Map<String, Object>> findList(String queryText, String infoType, PagingBean pb) {
        if (writer != null) {
            try {
                writer.close();
            } catch (CorruptIndexException e) {
                // TODO Auto-generated catch block
                PlatLogUtil.printStackTrace(e);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                PlatLogUtil.printStackTrace(e);
            }
        }
        IndexSearcher searcher = null;
        int pageIndex = pb.getCurrentPage();
        int pageSize = pb.getPageSize();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            IndexReader reader = IndexReader.open(directory);
            searcher = new IndexSearcher(reader);
            Analyzer analyzer = new MMSegAnalyzer();
            Sort sort = new Sort(new SortField("ORDER_DATE", SortField.LONG, true));
            int firstRecord = (pageIndex - 1) * pageSize;
            TopFieldCollector c = TopFieldCollector.create(sort, firstRecord + pageSize, false, false, false, false);
            BooleanQuery booleanQuery = null;
            if (StringUtils.isNotEmpty(queryText)) {
                MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35,
                        new String[]{"FULLTEXT_INDEXTITLE", "FULLTEXT_CONTENT"}, analyzer);
                Query query = parser.parse(queryText);
                booleanQuery = new BooleanQuery();
                booleanQuery.add(query, BooleanClause.Occur.MUST);
                if (StringUtils.isNotEmpty(infoType)) {
                    booleanQuery.add(new TermQuery(new Term("FULLTEXT_TYPE", infoType)), BooleanClause.Occur.MUST);
                }
                searcher.search(booleanQuery, c);
            } else {
                TermQuery q1 = new TermQuery(new Term("SIGNVALUE", "Ÿ"));
                booleanQuery = new BooleanQuery();
                booleanQuery.add(q1, BooleanClause.Occur.MUST);
                if (StringUtils.isNotEmpty(infoType)) {
                    TermQuery q2 = new TermQuery(new Term("FULLTEXT_TYPE", infoType));
                    booleanQuery.add(q2, BooleanClause.Occur.MUST);
                }
                searcher.search(booleanQuery, c);
            }
            TopDocs tds = c.topDocs(firstRecord, pageSize);
            pb.setTotalItems(tds.totalHits);
            int totalPage = (pb.getTotalItems() + pb.getPageSize() - 1) / pb.getPageSize();
            pb.setTotalPage(totalPage);
            for (ScoreDoc sd : tds.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                String FULLTEXT_INDEXTITLE = doc.get("FULLTEXT_INDEXTITLE");
                String FULLTEXT_PUBTIME = doc.get("FULLTEXT_PUBTIME");
                String FULLTEXT_TABLENAME = doc.get("FULLTEXT_TABLENAME");
                String FULLTEXT_RECORDID = doc.get("FULLTEXT_RECORDID");
                String FULLTEXT_CONTENT = doc.get("FULLTEXT_CONTENT");
                String FULLTEXT_TYPE = doc.get("FULLTEXT_TYPE");
                String FULLTEXT_URL = doc.get("FULLTEXT_URL");
                FULLTEXT_INDEXTITLE = lighterStr(analyzer, booleanQuery, FULLTEXT_INDEXTITLE, "FULLTEXT_INDEXTITLE");
                FULLTEXT_CONTENT = lighterStr(analyzer, booleanQuery, FULLTEXT_CONTENT, "FULLTEXT_CONTENT");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("FULLTEXT_INDEXTITLE", FULLTEXT_INDEXTITLE);
                map.put("FULLTEXT_PUBTIME", FULLTEXT_PUBTIME);
                map.put("FULLTEXT_TABLENAME", FULLTEXT_TABLENAME);
                map.put("FULLTEXT_RECORDID", FULLTEXT_RECORDID);
                map.put("FULLTEXT_CONTENT", FULLTEXT_CONTENT);
                map.put("FULLTEXT_TYPE", FULLTEXT_TYPE);
                map.put("FULLTEXT_URL", FULLTEXT_URL);
                //map.put("FULLTEXT_TYPE_TEXT",FULLTEXT_TYPE_TEXT);
                list.add(map);
            }
        } catch (Exception e1) {
            PlatLogUtil.printStackTrace(e1);
        } finally {
            try {
                searcher.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                PlatLogUtil.printStackTrace(e);
            }
        }
        return list;
    }
}
