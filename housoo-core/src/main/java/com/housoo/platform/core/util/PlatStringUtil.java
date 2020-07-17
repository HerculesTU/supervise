package com.housoo.platform.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述 封装对字符串处理的接口工具类
 *
 * @author housoo
 * @created 2017年2月2日 下午3:46:28
 */
public class PlatStringUtil {

    /**
     * 国标码和区位码转换常量
     */
    private static final int GB_SP_DIFF = 160;

    /**
     * 存放国标一级汉字不同读音的起始区位码
     */
    private static final int[] SECPOSVALUELIST = {1601, 1637, 1833,
            2078, 2274, 2302, 2433, 2594, 2787, 3106,
            3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5600};
    /**
     * 存放国标一级汉字不同读音的起始区位码对应读音
     */
    private static final char[] FIRSTLETTER = {'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'};

    /**
     * 将字符串集合转换成SQL语句的in条件
     *
     * @param values
     * @return
     */
    public static String getSqlInCondition(Set<String> values) {
        return getSqlInCondition(values.toArray(new String[values.size()]));
    }

    /**
     * 将字符串转换成SQL语句的in条件
     *
     * @param values
     * @return
     */
    public static String getSqlInCondition(String values) {
        return getSqlInCondition(values.split(","));
    }

    /**
     * 将字符串数组转换成sql语句的in条件
     *
     * @param valuesArray
     * @return
     */
    public static String getSqlInCondition(String[] valuesArray) {
        StringBuffer sb = new StringBuffer();
        for (String value : valuesArray) {
            sb.append(",'" + value + "'");
        }
        sb.delete(0, 1);
        sb.insert(0, "(");
        sb.append(")");
        return sb.toString();
    }

    /**
     * 获取freemarker模版数据填充值
     *
     * @param templateStr
     * @param root
     * @return
     */
    public static String getFreeMarkResult(String templateStr, Map<String, Object> root) {
        Configuration cfg = new Configuration();
        cfg.setTemplateLoader(new StringTemplateLoader(templateStr));
        cfg.setDefaultEncoding("UTF-8");
        Template template;
        try {
            template = cfg.getTemplate("");
            StringWriter writer = new StringWriter();
            template.process(root, writer);
            return writer.toString();
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (TemplateException e) {
            PlatLogUtil.printStackTrace(e);
        }
        return null;
    }

    /**
     * 描述 将数字格式化成百分比显示
     *
     * @param number:数字
     * @param digitCount:小数位数
     * @return
     * @author
     * @created 2016年3月8日 下午3:28:03
     */
    public static String getPercentFormat(double number, int digitCount) {
        //获取格式化对象
        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(2);
        //最后格式化并输出
        return nt.format(number);
    }

    /**
     * 描述 转换字符串的首字母为小写
     *
     * @param srcString
     * @return
     * @author
     * @created 2014年9月28日 上午9:55:23
     */
    public static String convertFirstLetterToLower(String srcString) {
        return srcString.substring(0, 1).toLowerCase() + srcString.substring(1, srcString.length());
    }

    /**
     * 获取MD5加密密文
     *
     * @param sourceStr:原明文字符串
     * @param salt:盐值对象
     * @param hashIterations:加密的次数
     * @return
     */
    public static String getMD5Encode(String sourceStr, Object salt, int hashIterations) {
        Object result = new SimpleHash("MD5", sourceStr, salt, hashIterations);
        return result.toString();
    }

    /**
     * 获取SHA256加密密文
     *
     * @param sourceStr:原明文字符串
     * @param salt:盐值对象
     * @param hashIterations:加密的次数
     * @return
     */
    public static String getSHA256Encode(String sourceStr, Object salt, int hashIterations) {
        Object result = new SimpleHash("sha-256", sourceStr, salt, hashIterations);
        return result.toString();
    }

    /**
     * 获取一个字符串在另一个字符串中的所有索引
     *
     * @param targetStr
     * @param sourceStr
     * @param fromIndex
     * @param indexes
     * @return
     */
    public static List<Integer> getAllIndexes(String targetStr, String sourceStr,
                                              int fromIndex, List<Integer> indexes) {
        if (indexes == null) {
            indexes = new ArrayList<Integer>();
        }
        int lastIndex = sourceStr.lastIndexOf(targetStr);
        if (fromIndex <= lastIndex) {
            int index = sourceStr.indexOf(targetStr, fromIndex);
            indexes.add(index);
            fromIndex = index + 1;
            return getAllIndexes(targetStr, sourceStr, fromIndex, indexes);
        } else {
            return indexes;
        }
    }

    /**
     * 产生某个数字以内的随机整数
     *
     * @param maxNum
     * @return
     * @author
     */
    public static int getRandomIntNumber(int maxNum) {
        Random rand = new Random();
        int num = rand.nextInt(); // int范围类的随机数
        num = rand.nextInt(maxNum);
        return num;
    }

    /**
     * 产生某个范围内的随机数
     *
     * @param minNum
     * @param maxNum
     * @return
     */
    public static int getRandomIntNumber(int minNum, int maxNum) {
        Random random = new Random();
        return random.nextInt(maxNum) % (maxNum - minNum + 1) + minNum;
    }

    /**
     * 将set集合转换成逗号拼接的字符串
     *
     * @param values
     * @return
     */
    public static String getSetStringSplit(Set<String> values) {
        StringBuffer strValues = new StringBuffer();
        for (String value : values) {
            strValues.append(value).append(",");
        }
        strValues.deleteCharAt(strValues.length() - 1);
        return strValues.toString();
    }

    /**
     * 将list集合转换成逗号拼接的字符串
     *
     * @param values
     * @return
     */
    public static String getListStringSplit(List<String> values) {
        StringBuffer strValues = new StringBuffer();
        if (values != null && values.size() > 0) {
            for (int i = 0; i < values.size(); i++) {
                if (i > 0) {
                    strValues.append(",");
                }
                strValues.append(values.get(i));
            }
        }
        return strValues.toString();
    }

    /**
     * 描述 将clob类型转换成string类型
     *
     * @param clob
     * @return
     * @author
     * @created 2015年10月9日 下午5:22:31
     */
    public static String clobToString(Clob clob) {
        if (clob == null) {
            return null;
        }
        try {
            Reader inStreamDoc = clob.getCharacterStream();
            char[] tempDoc = new char[(int) clob.length()];
            inStreamDoc.read(tempDoc);
            inStreamDoc.close();
            return new String(tempDoc);
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (SQLException es) {
            PlatLogUtil.printStackTrace(es);
        }
        return null;
    }

    /**
     * 描述 blob转换成string
     *
     * @param blob
     * @return
     * @author
     * @created 2015年10月16日 上午10:07:39
     */
    public static String blobToString(Blob blob, String encode) {
        String content = null;
        try {
            content = new String(blob.getBytes((long) 1, (int) blob.length()), encode);
        } catch (SQLException e) {
            PlatLogUtil.printStackTrace(e);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
        return content;
    }

    /**
     * 将传入的字符串格式例如a,b,c,转化SQL语句IN sql语句使用('a','b','c')
     */
    public static String getValueArray(String values) {
        if (StringUtils.isNotEmpty(values)) {
            StringBuffer sb = new StringBuffer();
            String[] valuesArray = values.split(",");
            for (String value : valuesArray) {
                sb.append(",'" + value + "'");
            }
            sb.delete(0, 1);
            sb.insert(0, "(");
            sb.append(")");
            return sb.toString();
        } else {
            return "()";
        }
    }

    /**
     * 将对象转换成JSON字符串
     *
     * @param targetObj  目标对象
     * @param fieldNames 过滤的字段名称
     * @param isExclude  如果是true,那么fieldNames是过滤转换的字段，false那么
     *                   fieldNames是只要转换的字段
     * @return
     */
    public static String toJsonString(Object targetObj, String[] fieldNames, final boolean isExclude) {
        if (fieldNames == null) {
            fieldNames = new String[]{};
        }
        final List<String> list = new ArrayList<String>();
        for (String f : fieldNames) {
            list.add(f);
        }
        PropertyFilter propertyfilter = new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                if (list.contains(name)) {
                    if (isExclude) {
                        return false;
                    } else {
                        return true;
                    }

                } else {
                    if (isExclude) {
                        return true;
                    } else {
                        return false;
                    }

                }
            }
        };
        SerializeWriter sw = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(sw);
        serializer.getPropertyFilters().add(propertyfilter);
        serializer.setDateFormat("yyyyMMddHHmmss");
        serializer.write(targetObj);
        return sw.toString();
    }

    /**
     * 描述 获取格式化的字符串,例如原值为10，
     * 长度为6,那么格式化为000010
     *
     * @param formatLength
     * @param value
     * @return
     * @author
     * @created 2015年3月27日 下午6:15:07
     */
    public static String getFormatNumber(int formatLength, String value) {
        int oldLength = value.length();
        if (oldLength < formatLength) {
            StringBuffer zeros = new StringBuffer("");
            for (int i = 0; i < formatLength - oldLength; i++) {
                zeros.append("0");
            }
            zeros.append(value);
            return zeros.toString();
        } else {
            return value;
        }
    }

    /**
     * 打印对象的JSON字符串
     *
     * @param obj
     * @param response
     */
    public static void printObjectJsonString(Object obj, HttpServletResponse response) {
        String json = JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero
                , SerializerFeature.WriteNullStringAsEmpty
        );
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            PlatLogUtil.printStackTrace(e);
        }
        pw.write(json);
        pw.flush();
        pw.close();
    }

    /**
     * 获取ASE算法加密后的值
     *
     * @param content:明文
     * @return
     */
    public static String getAseEncrypt(String content) {
        try {
            return AESOperator.aesEncrypt(content);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用ASE算法进行解密
     *
     * @param content
     * @return
     */
    public static String getAseDecrypt(String content) {
        try {
            return AESOperator.aesDecrypt(content);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取ASE算法加密后的值
     *
     * @param content:明文
     * @param key:密钥
     * @return
     */
    public static String getAseEncrypt(String content, String key) {
        try {
            return AESOperator.aesEncryptCode(content, key);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用ASE算法进行解密
     *
     * @param content
     * @param key
     * @return
     */
    public static String getAseDecrypt(String content, String key) {
        try {
            return AESOperator.aesDecryptCode(content, key);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个汉字的拼音首字母。
     * <p>
     * 　　* GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * <p>
     * 　　* 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * <p>
     * 　　* 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    private static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= SECPOSVALUELIST[i] && secPosValue < SECPOSVALUELIST[i + 1]) {
                result = FIRSTLETTER[i];
                break;
            }
        }
        return result;
    }

    /**
     * 描述 获取拼音,获取全拼
     *
     * @param src
     * @return
     * @created 2014年12月16日 上午9:36:38
     */
    public static String getPingYin(String src) {
        try {
            return PinyinHelper.convertToPinyinString(src, "", PinyinFormat.WITHOUT_TONE);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取一个字符串的拼音码的首字母 比方说:输入必胜 返回的结果将会是:hybs
     *
     * @param chinese
     * @return
     */
    public static String getFirstLetter(String chinese) {
        try {
            return PinyinHelper.getShortPinyin(chinese);
        } catch (PinyinException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取正则表达式匹配的总数量
     *
     * @param content
     * @param regex
     * @return
     */
    public static int getMatchCount(String content, String regex) {
        int i = 0;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            i++;
        }
        return i;
    }


    public static void main(String[] args){
        String pwd = "123456";
        System.out.println(PlatStringUtil.getMD5Encode(pwd, null, 1));
    }
}
