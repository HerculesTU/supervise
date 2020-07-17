package com.housoo.platform.appmodel.controller;

import com.alibaba.fastjson.JSON;
import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.util.*;
import com.housoo.platform.core.service.DesignService;
import com.housoo.platform.core.service.FormControlService;
import com.housoo.platform.core.service.ModuleService;
import com.housoo.platform.common.controller.BaseController;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.Node;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 描述 设计控制器
 *
 * @author housoo
 * @created 2017年2月4日 上午11:26:18
 */
@Controller
@RequestMapping("/appmodel/DesignController")
public class DesignController extends BaseController {
    /**
     * 缺省内部资源
     */
    public static final String DEFAULT_INTERNALRESES = "bootstrap-checkbox,jqgrid,jedate,select2,nicevalid,jquery-ui";
    /**
     *
     */
    @Resource
    private DesignService designService;
    /**
     *
     */
    @Resource
    private ModuleService moduleService;
    /**
     *
     */
    @Resource
    private FormControlService formControlService;

    /**
     * 跳转到字体图标选择器
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goFontSelect")
    public ModelAndView goFontSelect(HttpServletRequest request) {
        return new ModelAndView("common/fonticon/fonticon_selector");
    }

    /**
     * 跳转到列表界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goView")
    public ModelAndView goView(HttpServletRequest request) {
        return new ModelAndView("background/appmodel/visual/visual_view");
    }

    /**
     * 删除设计所生成的代码
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "delDesignCode")
    public void delDesignCode(HttpServletRequest request,
                              HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        for (String designId : selectColValues.split(",")) {
            designService.deleteGenCodeByDesignId(designId);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 更新组件模版
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "updateControlTpl")
    public void updateControlTpl(HttpServletRequest request,
                                 HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        designService.updateNewestTpl(selectColValues);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }


    /**
     * 删除设计信息
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping(params = "multiDel")
    public void multiDel(HttpServletRequest request,
                         HttpServletResponse response) {
        String selectColValues = request.getParameter("selectColValues");
        if (selectColValues.contains("402848a55c800cab015c800f11750003")) {
            selectColValues = selectColValues.replace("402848a55c800cab015c800f11750003", "");
        }
        designService.deleteCascadeFormControl(selectColValues);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        this.printObjectJsonString(result, response);
    }

    /**
     * 保存基本信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveBaseInfo")
    public void saveBaseInfo(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> design = PlatBeanUtil.getMapFromRequest(request);
        //产生一个token 
        String designToken = UUIDGenerator.getUUID();
        PlatAppUtil.setSessionCache(designToken, design);
        design.put("success", true);
        design.put("designToken", designToken);
        this.printObjectJsonString(design, response);
    }

    /**
     * 保存设计完整信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "saveCompInfo")
    public void saveCompInfo(HttpServletRequest request,
                             HttpServletResponse response) {
        Map<String, Object> resInfo = PlatBeanUtil.getMapFromRequest(request);
        String designToken = request.getParameter("designToken");
        Map<String, Object> design = (Map<String, Object>) PlatAppUtil.getSessionCache(designToken);
        design.putAll(resInfo);
        design = designService.saveAndGenJavaCode(design);
        String designId = (String) design.get("DESIGN_ID");
        designService.deleteGenCodeByDesignId(designId);
        PlatAppUtil.removeSessionCache(designToken);
        design.put("success", true);
        this.printObjectJsonString(design, response);
    }

    /**
     * 获取列表的JSON数据
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "datagrid")
    public void datagrid(HttpServletRequest request,
                         HttpServletResponse response) {
        SqlFilter filter = new SqlFilter(request);
        filter.addFilter("O_T.DESIGN_CREATETIME", "DESC", SqlFilter.FILTER_TYPE_ORDER);
        List<Map<String, Object>> list = designService.findBySqlFilter(filter);
        this.printListJsonString(filter.getPagingBean(), list, response);
    }

    /**
     * 跳转到表单界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goform")
    public ModelAndView goform(HttpServletRequest request) {
        String DESIGN_ID = request.getParameter("DESIGN_ID");
        String MODULE_ID = request.getParameter("MODULE_ID");
        Map<String, Object> module = null;
        Map<String, Object> design = null;
        if (StringUtils.isNotEmpty(DESIGN_ID)) {
            design = this.designService.getRecord("PLAT_APPMODEL_DESIGN",
                    new String[]{"DESIGN_ID"}, new Object[]{DESIGN_ID});
            module = designService.getRecord("PLAT_APPMODEL_MODULE"
                    , new String[]{"MODULE_ID"}, new Object[]{design.get("DESIGN_MODULEID")});
            String DESIGN_EXTERNALRESES = (String) design.get("DESIGN_EXTERNALRESES");
            if (StringUtils.isNotEmpty(DESIGN_EXTERNALRESES)) {
                String[] reses = DESIGN_EXTERNALRESES.split(",");
                StringBuffer resesLabels = new StringBuffer("");
                for (int i = 0; i < reses.length; i++) {
                    if (i > 0) {
                        resesLabels.append(",");
                    }
                    String resName = reses[i].substring(reses[i].lastIndexOf("\\") + 1, reses[i].length());
                    resesLabels.append(resName);
                }
                design.put("DESIGN_EXTERNALRESES_LABELS", resesLabels.toString());
            }
        } else {
            design = new HashMap<String, Object>();
            module = designService.getRecord("PLAT_APPMODEL_MODULE"
                    , new String[]{"MODULE_ID"}, new Object[]{MODULE_ID});
            design.put("DESIGN_MODULEID", MODULE_ID);
            design.put("DESIGN_INTERNALRESES", DEFAULT_INTERNALRESES);
        }
        if (module != null) {
            design.put("MODULE_NAME", module.get("MODULE_NAME"));
        }
        request.setAttribute("design", design);
        return new ModelAndView("background/appmodel/visual/design_form");
    }


    /**
     * @param parentNode
     * @param parentMap
     */
    public void getChildNode(Node parentNode, Map<String, Object> parentMap) {
        List<Map<String, Object>> childrenList = new ArrayList<Map<String, Object>>();
        for (Node child : parentNode.getChildNodes()) {
            String platcompname = child.getAttribute("platcompname");
            String platcomid = child.getAttribute("platcomid");
            String uibtnsrights = child.getAttribute("uibtnsrights");
            String compcontrolid = child.getAttribute("compcontrolid");
            String compcode = child.getAttribute("compcode");
            String platundragable = child.getAttribute("platundragable");
            if (StringUtils.isNotEmpty(platcompname) && StringUtils.isNotEmpty(uibtnsrights)) {
                Map<String, Object> node = new HashMap<String, Object>();
                node.put("id", platcomid);
                node.put("name", platcompname);
                node.put("compcontrolid", compcontrolid);
                node.put("uibtnsrights", uibtnsrights);
                node.put("compcode", compcode);
                node.put("open", true);
                node.put("platundragable", platundragable);
                this.getChildNode(child, node);
                childrenList.add(node);
            }
        }
        if (childrenList.size() > 0) {
            parentMap.put("children", childrenList);
        }
    }

    /**
     * 获取设计控件的JSON
     *
     * @param jspPath
     * @return
     */
    private String getDesignControlJson(String jspPath) {
        String htmlContent = PlatFileUtil.readFileString(jspPath);
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("id", "0");
        root.put("open", true);
        root.put("name", "body容器");
        root.put("compcontrolid", "0");
        root.put("uibtnsrights", "add");
        root.put("compcode", "body");
        root.put("platundragable", "true");
        Jerry doc = Jerry.jerry(htmlContent);
        Jerry body = doc.$("body[platcomid='0']");
        Jerry children = body.children();
        List<Map<String, Object>> topList = new ArrayList<Map<String, Object>>();
        for (Node child : children.get()) {
            String platcompname = child.getAttribute("platcompname");
            String platcomid = child.getAttribute("platcomid");
            String uibtnsrights = child.getAttribute("uibtnsrights");
            String compcontrolid = child.getAttribute("compcontrolid");
            String compcode = child.getAttribute("compcode");
            String platundragable = child.getAttribute("platundragable");
            if (StringUtils.isNotEmpty(platcompname) && StringUtils.isNotEmpty(uibtnsrights)) {
                Map<String, Object> node = new HashMap<String, Object>();
                node.put("id", platcomid);
                node.put("name", platcompname);
                node.put("compcontrolid", compcontrolid);
                node.put("uibtnsrights", uibtnsrights);
                node.put("compcode", compcode);
                node.put("open", true);
                node.put("platundragable", platundragable);
                this.getChildNode(child, node);
                topList.add(node);
            }

        }
        if (topList.size() > 0) {
            root.put("children", topList);
        }
        return JSON.toJSONString(root);
    }

    /**
     * 跳转到在线设计界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goDesign")
    public ModelAndView goDesign(HttpServletRequest request) {
        String DESIGN_ID = request.getParameter("DESIGN_ID");
        //更新parentId和compID不一致的情况
        designService.updateParentCompIdNotSame(DESIGN_ID);
        Map<String, Object> design = this.designService.
                getRecord("PLAT_APPMODEL_DESIGN", new String[]{"DESIGN_ID"}, new Object[]{DESIGN_ID});
        String appPath = PlatAppUtil.getAppAbsolutePath();
        PlatFileUtil.deleteFilesOfDir(appPath + "webpages/background/designui");
        StringBuffer jspPath = new StringBuffer(appPath);
        String fileId = UUIDGenerator.getUUID();
        jspPath.append("webpages/background/designui/" + fileId + ".jsp");
        //更新组件代码
        formControlService.updateCmpCode(DESIGN_ID, "true");
        String tplPath = appPath + "webpages/background/appmodel/visual/online_design_tpl.jsp";
        String genHtmlCode = designService.getDesignCode(DESIGN_ID, "true");
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("DESIGN_ID", DESIGN_ID);
        root.put("DESIGN_NAME", design.get("DESIGN_NAME"));
        if (StringUtils.isNotEmpty(genHtmlCode)) {
            root.put("genHtmlCode", genHtmlCode);
        }
        root.put("DESIGN_JSENHANCE", design.get("DESIGN_JSENHANCE"));
        String viewJspTplStr = PlatFileUtil.readFileString(tplPath);
        String genJspCode = PlatStringUtil.getFreeMarkResult(viewJspTplStr, root);
        PlatFileUtil.writeDataToDisk(genJspCode, jspPath.toString(), null);
        String designControlJson = this.getDesignControlJson(jspPath.toString());
        request.setAttribute("fileId", fileId);
        request.setAttribute("DESIGN_ID", DESIGN_ID);
        request.setAttribute("DESIGN_NAME", design.get("DESIGN_NAME"));
        request.setAttribute("designControlJson", StringEscapeUtils.escapeHtml3(designControlJson));
        return new ModelAndView("background/appmodel/visual/online_design");
    }

    /**
     * 跳转到在线设计界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goGenDesignView")
    public ModelAndView goGenDesignView(HttpServletRequest request) {
        String fileId = request.getParameter("fileId");
        request.setAttribute("fileId", fileId);
        return new ModelAndView("background/designui/" + fileId);
    }

    /**
     * 跳转到克隆UI界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goCloneView")
    public ModelAndView goCloneView(HttpServletRequest request) {
        String DESIGN_ID = request.getParameter("DESIGN_ID");
        //更新parentId和compID不一致的情况
        designService.updateParentCompIdNotSame(DESIGN_ID);
        Map<String, Object> design = this.designService.
                getRecord("PLAT_APPMODEL_DESIGN", new String[]{"DESIGN_ID"}, new Object[]{DESIGN_ID});
        design.remove("DESIGN_CODE");
        request.setAttribute("design", design);
        return PlatUICompUtil.goDesignUI("cloneui", request);
    }

    /**
     * 跳转到生成的UI界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goGenUiView")
    public ModelAndView goGenUiView(HttpServletRequest request) {
        String DESIGN_CODE = request.getParameter("DESIGN_CODE");
        Map<String, Object> design = designService.getRecord("PLAT_APPMODEL_DESIGN",
                new String[]{"DESIGN_CODE"}, new Object[]{DESIGN_CODE});
        String appPath = PlatAppUtil.getAppAbsolutePath();
        String jspPath = designService.getGenPathByDesignIdOrCode(DESIGN_CODE, DESIGN_CODE + ".jsp", false, false);
        File jspFile = new File(jspPath);
        if (!jspFile.exists()) {
            String genJspCode = getGenJspCode(design, appPath);
            PlatFileUtil.writeDataToDisk(genJspCode, jspPath, null);
        }
        return new ModelAndView("background/genui/" + DESIGN_CODE + "/" + DESIGN_CODE);
    }

    /**
     * @param design
     * @param appPath
     * @return
     */
    private String getGenJspCode(Map<String, Object> design, String appPath) {
        String tplPath = appPath + "webpages/background/appmodel/visual/uipreview_tpl.jsp";
        //获取外部资源信息
        String DESIGN_EXTERNALRESES = (String) design.get("DESIGN_EXTERNALRESES");
        if (StringUtils.isNotEmpty(DESIGN_EXTERNALRESES)) {
            String[] reses = DESIGN_EXTERNALRESES.split(",");
            List<String> externalcss = new ArrayList<String>();
            List<String> externaljs = new ArrayList<String>();
            for (String res : reses) {
                if (res.endsWith("css")) {
                    externalcss.add(res.replaceAll("\\\\", "/"));
                } else if (res.endsWith("js")) {
                    externaljs.add(res.replaceAll("\\\\", "/"));
                }
            }
            design.put("externalcss", externalcss);
            design.put("externaljs", externaljs);
        }
        String DESIGN_ID = (String) design.get("DESIGN_ID");
        //更新组件代码
        formControlService.updateCmpCode(DESIGN_ID, null);
        String genHtmlCode = designService.getDesignCode(DESIGN_ID, null);
        if (StringUtils.isNotEmpty(genHtmlCode)) {
            design.put("genHtmlCode", genHtmlCode);
        }
        String viewJspTplStr = PlatFileUtil.readFileString(tplPath);
        String genJspCode = PlatStringUtil.getFreeMarkResult(viewJspTplStr, design);
        return genJspCode;
    }

    /**
     * 跳转到查看代码界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goQueryCode")
    public ModelAndView goQueryCode(HttpServletRequest request) {
        String DESIGN_CODE = request.getParameter("DESIGN_CODE");
        Map<String, Object> design = designService.getRecord("PLAT_APPMODEL_DESIGN",
                new String[]{"DESIGN_CODE"}, new Object[]{DESIGN_CODE});
        String appPath = PlatAppUtil.getAppAbsolutePath();
        String jspPath = designService.getGenPathByDesignIdOrCode(DESIGN_CODE, DESIGN_CODE + ".jsp", false, false);
        design.put("DESIGN_URL", "appmodel/DesignController.do?goGenUiView&DESIGN_CODE=" + DESIGN_CODE);
        File jspFile = new File(jspPath);
        if (!jspFile.exists()) {
            String genJspCode = getGenJspCode(design, appPath);
            design.put("DESIGN_CODE", genJspCode);
        } else {
            String designCode = PlatFileUtil.readFileString(jspPath);
            design.put("DESIGN_CODE", designCode);
        }
        request.setAttribute("design", design);
        return new ModelAndView("background/appmodel/visual/design_query");
    }

    /**
     * 跳转到包含设计界面
     *
     * @param request
     * @return
     */
    @RequestMapping("/includeUI")
    public ModelAndView includeUI(HttpServletRequest request) {
        String DESIGN_CODE = request.getParameter("DESIGN_CODE");
        Map<String, Object> design = designService.getRecord("PLAT_APPMODEL_DESIGN",
                new String[]{"DESIGN_CODE"}, new Object[]{DESIGN_CODE});
        String DESIGN_ID = (String) design.get("DESIGN_ID");
        String appPath = PlatAppUtil.getAppAbsolutePath();
        String jspPath = designService.getGenPathByDesignIdOrCode(DESIGN_CODE, DESIGN_CODE + "INCLUDE.jsp", false, false);
        File jspFile = new File(jspPath);
        if (!jspFile.exists()) {
            String genHtmlCode = designService.getDesignCode(DESIGN_ID, null);
            if (StringUtils.isNotEmpty(genHtmlCode)) {
                design.put("genHtmlCode", genHtmlCode);
            }
            String tplPath = appPath + "webpages/background/appmodel/visual/includeui_tpl.jsp";
            String viewJspTplStr = PlatFileUtil.readFileString(tplPath);
            String genJspCode = PlatStringUtil.getFreeMarkResult(viewJspTplStr, design);
            PlatFileUtil.writeDataToDisk(genJspCode, jspPath.toString(), null);
        }
        return new ModelAndView("background/genui/" + DESIGN_CODE + "/" + DESIGN_CODE + "INCLUDE");

    }

    /**
     * 克隆设计信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "cloneDesign")
    public void cloneDesign(HttpServletRequest request,
                            HttpServletResponse response) {
        String sourceDesignId = request.getParameter("DESIGN_ID");
        String newDesignModuleId = request.getParameter("DESIGN_MODULEID");
        String newDesignCode = request.getParameter("DESIGN_CODE");
        String newDesignName = request.getParameter("DESIGN_NAME");
        this.designService.copyDesignInfo(sourceDesignId, newDesignCode, newDesignName, newDesignModuleId);
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("success", true);
        this.printObjectJsonString(root, response);
    }

    /**
     * 拆分导出配置
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/exportSignle")
    public void exportSignle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //压缩文件初始设置
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties", "attachFilePath");
        File file = new File(attachFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uuid = UUIDGenerator.getUUID();
        String filePath = attachFilePath + uuid + ".zip";
        String time = PlatDateTimeUtil.formatDate(new Date(), "yyyyMMddHHmmss");
        String fileZip = time + ".zip";
        //filePathArr为根据前台传过来的信息，通过数据库查询所得出的pdf文件路径集合（具体到后缀），此处省略
        String designIds = request.getParameter("designIds");
        String[] idArray = designIds.split(",");
        List<Map<String, String>> codeList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < idArray.length; i++) {
            Map<String, String> codeInfo = new HashMap<String, String>();
            String designId = idArray[i];
            Map<String, Object> design = designService.getRecord("PLAT_APPMODEL_DESIGN",
                    new String[]{"DESIGN_ID"}, new Object[]{designId});
            Map<String, Object> result = designService.getExportInfo(designId);
            String json = JSON.toJSONString(result);
            codeInfo.put("codekey", design.get("DESIGN_CODE").toString());
            codeInfo.put("codecontent", json);
            codeList.add(codeInfo);
        }
        // 创建临时压缩文件
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            ZipOutputStream zos = new ZipOutputStream(bos);
            ZipEntry ze = null;
            for (int i = 0; i < codeList.size(); i++) {
                Map<String, String> code = codeList.get(i);
                String codekey = code.get("codekey");
                String codecontent = code.get("codecontent");
                ByteArrayInputStream stream = new ByteArrayInputStream(codecontent.getBytes());
                //BufferedInputStream bis = new BufferedInputStream(new FileInputStream(files[i]));
                BufferedInputStream bis = new BufferedInputStream(stream);
                ze = new ZipEntry(codekey + ".json");
                zos.putNextEntry(ze);
                int s = -1;
                while ((s = bis.read()) != -1) {
                    zos.write(s);
                }
                bis.close();
            }
            zos.flush();
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //以上，临时压缩文件创建完成

        //进行浏览器下载       
        //获得浏览器代理信息
        final String userAgent = request.getHeader("USER-AGENT");
        //判断浏览器代理并分别设置响应给浏览器的编码格式
        String finalFileName = null;
        if (StringUtils.contains(userAgent, "MSIE") || StringUtils.contains(userAgent, "Trident")) {//IE浏览器
            finalFileName = URLEncoder.encode(fileZip, "UTF8");
        } else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
            finalFileName = new String(fileZip.getBytes(), "ISO8859-1");
        } else {
            finalFileName = URLEncoder.encode(fileZip, "UTF8");//其他浏览器
        }
        response.setContentType("application/x-download");//告知浏览器下载文件，而不是直接打开，浏览器默认为打开
        response.setHeader("Content-Disposition", "attachment;filename=\"" + finalFileName + "\"");//下载文件的名称
        ServletOutputStream servletOutputStream = response.getOutputStream();
        DataOutputStream temps = new DataOutputStream(
                servletOutputStream);
        DataInputStream in = new DataInputStream(
                new FileInputStream(filePath));//浏览器下载文件的路径
        byte[] b = new byte[2048];
        File reportZip = new File(filePath);//之后用来删除临时压缩文件
        try {
            while ((in.read(b)) != -1) {
                temps.write(b);
            }
            temps.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (temps != null) {
                temps.close();
            }
            if (in != null) {
                in.close();
            }
            if (reportZip != null) {
                reportZip.delete();//删除服务器本地产生的临时压缩文件
            }
            servletOutputStream.close();
        }
    }

    /**
     * 导出配置
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("/exportConfig")
    public void exportConfig(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String designIds = request.getParameter("designIds");
        Map<String, Object> result = designService.getExportInfo(designIds);
        String json = JSON.toJSONString(result);
        try {
            String downloadFileName = PlatDateTimeUtil.formatDate(new Date(), "yyyyMMdd") + "config.json";
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + downloadFileName);
            //用于记录以完成的下载的数据量，单位是byte
            InputStream inputStream = new ByteArrayInputStream(json.getBytes());
            //激活下载操作
            OutputStream os = response.getOutputStream();
            //循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (FileNotFoundException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 导入配置
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "impConfig")
    public void impConfig(HttpServletRequest request,
                          HttpServletResponse response) {
        String dbfilepath = request.getParameter("dbfilepath");
        //获取文件存储路径
        String attachFilePath = PlatPropUtil.getPropertyValue("config.properties"
                , "attachFilePath");
        String jsonFilePath = attachFilePath + dbfilepath;
        //获取文件内容
        String jsonContent = PlatFileUtil.readFileString(jsonFilePath, "UTF-8");
        designService.importConfig(jsonContent);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", true);
        result.put("msg", "导入成功!");
        this.printObjectJsonString(result, response);
    }

    /**
     * 跳转到包含界面
     *
     * @param request
     * @return
     */
    @RequestMapping(params = "goIncludeDesign")
    public ModelAndView goIncludeDesign(HttpServletRequest request) {
        String DESIGN_CODE = request.getParameter("DESIGN_CODE");
        request.setAttribute("DESIGN_CODE", DESIGN_CODE);
        return new ModelAndView("background/appmodel/visual/includedesign");
    }
}
