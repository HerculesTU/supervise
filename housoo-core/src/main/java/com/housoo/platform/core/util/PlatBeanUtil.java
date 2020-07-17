package com.housoo.platform.core.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 描述 封装bean的主要API
 *
 * @author housoo
 * @created 2017年1月5日 下午3:41:22
 */
public class PlatBeanUtil {

    /**
     * 获取某个class的文件路径
     *
     * @param clazz
     * @return
     */
    public static String getClassFilePath(Class clazz) {
        String className = clazz.getName();
        String classNamePath = className.replace(".", "/") + ".class";
        URL is = clazz.getClassLoader().getResource(classNamePath);
        String path = is.getFile();
        path = StringUtils.replace(path, "%20", " ");
        return StringUtils.removeStart(path, "/");
    }

    /**
     * 获取某个class的文件夹路径
     *
     * @param clazz
     * @return
     */
    public static String getClassFolderPath(Class clazz) {
        String path = PlatBeanUtil.getClassFilePath(clazz);
        path = path.substring(0, path.lastIndexOf("/"));
        return path;
    }

    /**
     * 获取request请求参数的所有值
     *
     * @param request
     * @return
     * @author
     */
    public static Map<String, Object> getMapFromRequest(
            HttpServletRequest request) {
        Map reqMap = request.getParameterMap();
        HashMap<String, Object> datas = new HashMap<String, Object>();
        Iterator it = reqMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String[] val = (String[]) entry.getValue();
            if (val.length == 1) {
                if (StringUtils.isNotEmpty(val[0])) {
                    datas.put(key, val[0]);
                } else {
                    datas.put(key, "");
                }
            } else {
                if (val != null) {
                    datas.put(key, val);
                } else {
                    datas.put(key, "");
                }
            }
        }
        return datas;
    }


    /**
     * 获取子父类字段值map
     *
     * @param sonObj
     * @return
     */
    public static Map<String, Object> getSonAndSuperClassField(Object sonObj) {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        Field[] superFields = sonObj.getClass().getSuperclass().getDeclaredFields();
        setClassFieldValues(sonObj, fieldMap, superFields);
        Field[] sonFields = sonObj.getClass().getDeclaredFields();
        setClassFieldValues(sonObj, fieldMap, sonFields);
        return fieldMap;

    }

    /**
     * @param sonObj
     * @param fieldMap
     * @param fields
     */
    private static void setClassFieldValues(Object sonObj,
                                            Map<String, Object> fieldMap, Field[] fields) {
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                fieldMap.put(f.getName(), f.get(sonObj));
            } catch (IllegalArgumentException e) {
                PlatLogUtil.printStackTrace(e);
            } catch (IllegalAccessException e) {
                PlatLogUtil.printStackTrace(e);
            }
        }
    }

    /**
     * 将bean转换成map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性  
                if (!"class".equals(key)) {
                    // 得到property对应的getter方法  
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
        return map;
    }

    /**
     * 将map转换成bean
     *
     * @param bean
     * @param mapValue
     * @return
     */
    public static Object mapToBean(Object bean, Map<String, Object> mapValue) {
        try {
            BeanUtils.populate(bean, mapValue);
        } catch (IllegalAccessException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (InvocationTargetException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return bean;
    }

    /**
     * 清除非必要字段,保留必须字段
     *
     * @param keepFieldSet
     * @param srcList
     * @return
     */
    public static List<Map<String, Object>> keepMustFields(Set<String> keepFieldSet,
                                                           List<Map<String, Object>> srcList) {
        List<Map<String, Object>> destList = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> src : srcList) {
            Map<String, Object> dest = new HashMap<String, Object>();
            for (String fieldName : keepFieldSet) {
                dest.put(fieldName, src.get(fieldName));
            }
            destList.add(dest);
        }
        return destList;
    }

    /**
     * 拷贝对象属性,不支持MAP对象
     *
     * @param dest
     * @param orig
     */
    @SuppressWarnings("AlibabaAvoidApacheBeanUtilsCopy")
    public static void copyProperties(Object dest, Object orig) {
        try {
            //noinspection AlibabaAvoidApacheBeanUtilsCopy
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝MAP
     *
     * @param dest
     * @param orig
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map copyMapProps(Map dest, Map orig) {
        if (dest == null) {
            dest = new HashMap();
        }
        dest.putAll(orig);
        return dest;
    }

    /**
     * 合并对象
     *
     * @param origin
     * @param destination
     */
    public static <T> void mergeObject(T origin, T destination) {
        if (origin == null || destination == null) {
            return;
        }
        if (!origin.getClass().equals(destination.getClass())) {
            return;
        }

        Field[] fields = origin.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                fields[i].setAccessible(true);
                Object value = fields[i].get(origin);
                if (null != value) {
                    fields[i].set(destination, value);
                }
                fields[i].setAccessible(false);
            } catch (Exception e) {
            }
        }
    }
}
