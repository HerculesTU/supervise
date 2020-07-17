package com.housoo.platform.metadata.service;

import com.housoo.platform.core.model.SqlFilter;
import com.housoo.platform.core.service.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 描述 数据服务信息业务相关service
 *
 * @author 高飞
 * @version 1.0
 * @created 2018-05-08 16:49:06
 */
public interface DataSerService extends BaseService {
    /**
     * 成功
     */
    public static final String CODE_SUCCESS = "000";
    /**
     * 缺失必要参数
     */
    public static final String CODE_LOSEPARAM = "001";
    /**
     * 未查询到对应服务
     */
    public static final String CODE_NOSERVICE = "002";
    /**
     * 服务停止
     */
    public static final String CODE_STOPSERVICE = "003";
    /**
     * 请求方IP地址无访问权限
     */
    public static final String CODE_IPNOAUTH = "004";
    /**
     * 输入参数不合法
     */
    public static final String CODE_UNLEGALPARAM = "005";
    /**
     * 请求方授权码无效
     */
    public static final String CODE_INVALID = "006";
    /**
     * 请求服务未被授权
     */
    public static final String CODE_NOAUTHSERVICE = "007";
    /**
     * 超出当日调用次数
     */
    public static final String CODE_OVERCOUNT = "008";
    /**
     * 其它异常
     */
    public static final String CODE_OTHER = "999";
    /**
     * 缓存KEY前缀名称
     */
    public static final String CACHEPRE_CODE = "PLATDATASER_";

    /**
     * 根据filter和配置信息获取数据列表
     *
     * @param filter
     * @param fieldInfo
     * @return
     */
    public List<Map<String, Object>> findList(SqlFilter filter, Map<String, Object> fieldInfo);

    /**
     * 保存目录服务中间表
     *
     * @param serId
     * @param catalogIds
     */
    public void saveOrUpdateSerCatalog(String serId, String catalogIds);

    /**
     * 获取已经选择的服务记录
     *
     * @param filter
     * @return
     */
    public List<Map<String, Object>> findSelected(SqlFilter filter);

    /**
     * 根据服务IDS获取服务信息
     *
     * @param serIds
     * @return
     */
    public Map<String, String> getDataSerInfo(String serIds);

    /**
     * 获取查询参数列表
     *
     * @param sqlFilter
     * @return
     */
    public List<Map> findQueryParams(SqlFilter sqlFilter);

    /**
     * 调用请求服务
     *
     * @param request
     * @param isTest
     * @return
     * @throws Exception
     */
    public Map<String, Object> invokeService(HttpServletRequest request, boolean isTest) throws Exception;

    /**
     * 获取自动清除缓存的服务
     *
     * @return
     */
    public List<Map<String, Object>> findAutoClearList();

    /**
     * 调用请求服务
     *
     * @param request
     * @return
     */
    public Map<String, Object> invokeService(HttpServletRequest request);
}
