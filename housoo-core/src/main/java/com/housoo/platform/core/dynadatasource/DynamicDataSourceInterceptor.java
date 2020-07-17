package com.housoo.platform.core.dynadatasource;

import com.housoo.platform.core.util.PlatAppUtil;
import com.housoo.platform.core.util.PlatDbUtil;
import com.housoo.platform.core.util.PlatPropUtil;
import com.housoo.platform.core.service.DbConnService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author housoo
 */
public class DynamicDataSourceInterceptor {

    /**
     * @param point
     * @throws Exception
     */
    public void afterIntercept(JoinPoint point) throws Exception {
        //System.out.println("调用结束..");
        String defaultDbType = PlatDbUtil.getDefaultDbType();
        PlatAppUtil.setDbType(defaultDbType);
        PlatAppUtil.setDbSchema(PlatPropUtil.getPropertyValue("config.properties", "dbschema"));
    }

    /**
     * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
     *
     * @param point
     * @throws Exception
     */
    public void intercept(JoinPoint point) throws Exception {
        Class<?> target = point.getTarget().getClass();
        MethodSignature signature = (MethodSignature) point.getSignature();
        resolveDataSource(target, signature.getMethod());
    }

    public static void setDbType(String dbSourceCode) {
        if (StringUtils.isNotEmpty(dbSourceCode)) {
            //获取数据源数据库类型
            DbConnService dbConnService = (DbConnService) PlatAppUtil.getBean("dbConnService");
            String dbType = dbConnService.getDbType(dbSourceCode);
            PlatAppUtil.setDbType(dbType);
        } else {
            PlatAppUtil.setDbType(PlatAppUtil.getDefaultDbType());
            PlatAppUtil.setDbSchema(PlatAppUtil.getDefaultDbSchema());
        }
    }

    /**
     * 提取目标对象方法注解和类型注解中的数据源标识
     *
     * @param clazz
     * @param method
     */
    private void resolveDataSource(Class<?> clazz, Method method) {
        try {
            Class<?>[] types = method.getParameterTypes();
            String dbConnSource = null;
            String currentDataSourceName = PlatAppUtil.getCurrentDbSource();
            // 默认使用类型注解  
            if (clazz.isAnnotationPresent(DataSource.class)) {
                DataSource source = clazz.getAnnotation(DataSource.class);
                dbConnSource = source.value();
                if (StringUtils.isEmpty(currentDataSourceName)
                        || !dbConnSource.equals(currentDataSourceName)) {
                    DynamicDataSourceInterceptor.setDbType(source.value());
                    PlatAppUtil.setCurrentDbSource(source.value());
                    currentDataSourceName = source.value();
                    DynamicDataSourceHolder.setDatasource(source.value());
                }
            }
            // 方法注解可以覆盖类型注解  
            Method m = clazz.getMethod(method.getName(), types);
            if (m != null && m.isAnnotationPresent(DataSource.class)) {
                DataSource source = m.getAnnotation(DataSource.class);
                dbConnSource = source.value();
                if ("unknown".equals(dbConnSource)) {

                } else {
                    if (StringUtils.isEmpty(currentDataSourceName)
                            || !dbConnSource.equals(currentDataSourceName)) {
                        PlatAppUtil.setCurrentDbSource(source.value());
                        currentDataSourceName = source.value();
                        DynamicDataSourceInterceptor.setDbType(source.value());
                        DynamicDataSourceHolder.setDatasource(source.value());
                    }
                }
            }
            if (StringUtils.isEmpty(dbConnSource)) {
                PlatAppUtil.setCurrentDbSource("defaultDataSource");
                currentDataSourceName = "defaultDataSource";
                if (!currentDataSourceName.equals(DynamicDataSourceHolder.getDatasource())) {
                    DynamicDataSourceInterceptor.setDbType(null);
                    DynamicDataSourceHolder.setDatasource("defaultDataSource");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
