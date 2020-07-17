package com.housoo.platform.core.util;

import freemarker.cache.TemplateLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述
 *
 * @author
 * @version 1.0
 * @created 2014年9月16日 上午10:13:10
 */
public class StringTemplateLoader implements TemplateLoader {
    /**
     * 缺省KEY
     */
    private static final String DEFAULT_TEMPLATE_KEY = "_default_template_key";
    /**
     * 缺省模版
     */
    private Map templates = new HashMap();

    /**
     * 描述
     *
     * @param defaultTemplate
     * @author
     * @created 2014年9月16日 上午10:13:42
     */
    public StringTemplateLoader(String defaultTemplate) {
        if (defaultTemplate != null && !"".equals(defaultTemplate)) {
            templates.put(DEFAULT_TEMPLATE_KEY, defaultTemplate);
        }
    }

    /**
     * 描述
     *
     * @param name
     * @param template
     * @author
     * @created 2014年9月16日 上午10:13:47
     */
    public void addTemplate(String name, String template) {
        if (name == null || template == null || "".equals(name)
                || "".equals(template)) {
            return;
        }
        if (!templates.containsKey(name)) {
            templates.put(name, template);
        }
    }

    /**
     * 描述
     *
     * @param templateSource
     * @throws IOException
     * @author
     * @created 2014年9月16日 上午10:13:51
     * @see TemplateLoader#closeTemplateSource(Object)
     */
    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {

    }

    /**
     * 描述
     *
     * @param name
     * @return
     * @throws IOException
     * @author
     * @created 2014年9月16日 上午10:13:54
     * @see TemplateLoader#findTemplateSource(String)
     */
    @Override
    public Object findTemplateSource(String name) throws IOException {
        if (name == null || "".equals(name)) {
            name = DEFAULT_TEMPLATE_KEY;
        }
        return templates.get(name);
    }

    /**
     * 描述
     *
     * @param templateSource
     * @return
     * @author
     * @created 2014年9月16日 上午10:13:57
     * @see TemplateLoader#getLastModified(Object)
     */
    @Override
    public long getLastModified(Object templateSource) {
        return 0;
    }

    /**
     * 描述
     *
     * @param templateSource
     * @param encoding
     * @return
     * @throws IOException
     * @author
     * @created 2014年9月16日 上午10:14:00
     * @see TemplateLoader#getReader(Object, String)
     */
    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return new StringReader((String) templateSource);
    }

}
