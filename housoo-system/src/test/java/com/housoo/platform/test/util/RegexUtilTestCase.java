package com.housoo.platform.test.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author 胡裕
 *
 * 
 */
public class RegexUtilTestCase {
    

    @Test
    public void test1(){
        String str = "fdsfs${username}fdsfsd${password}你好飞机迪斯科";
        Pattern p = Pattern.compile("\\$\\{(.*?)}");
        Matcher m = p.matcher(str);
        ArrayList<String> strs = new ArrayList<String>();
        while (m.find()) {
            strs.add(m.group(1));            
        } 
        for (String s : strs){
            System.out.println("字符串:"+s);
        }        
    }
    
    @Test
    public void test2(){
        String sql = "SELECT * FROM PLAT_SYSTEM_SYSLOG T WHERE ${T.BROWSER LIKE '%[浏览器名称]%'}";
        sql+= " AND ${T.OPER_TYPE=[操作类型]} ";
        Pattern p = Pattern.compile("\\$\\{(.*?)}");
        Matcher m = p.matcher(sql);
        ArrayList<String> strs = new ArrayList<String>();
        List<String> conditionList = new ArrayList<String>();
        while (m.find()) {
            String conditionSql = m.group(1);
            System.out.println("匹配到的是:"+conditionSql);
            System.out.println("匹配的索引是:"+m.start()+","+m.end());
            conditionList.add(conditionSql);
        } 
        
        //System.out.println(sql.substring(41, 70));
    }
    
    @Test
    public void test3(){
        String condition = "T.BROWSER LIKE '%[浏览器名称]%' AND T.USERNAME LIKE '%[姓名]' ";
        condition = StringUtils.replace(condition, "'%[浏览器名称]%'","?");
        condition = StringUtils.replace(condition, "'%[姓名]'","?");
        System.out.println(condition);
    }
    
    public static void main(String[] args){
        String condition = "T.BROWSER LIKE '%[浏览器名称]%'";
        //获取擦操作符
        String operate = null;
        if(StringUtils.containsIgnoreCase(condition,"LIKE")){
            operate = "LIKE";
        }else if(StringUtils.containsIgnoreCase(condition,">=")){
            operate= ">=";
        }else if(StringUtils.containsIgnoreCase(condition,"<=")){
            operate= "<=";
        }else if(StringUtils.containsIgnoreCase(condition,"=")){
            operate= "=";
        }else if(StringUtils.containsIgnoreCase(condition,">")){
            operate= ">";
        }else if(StringUtils.containsIgnoreCase(condition,"<")){
            operate= "<";
        }
        Pattern p = Pattern.compile("\\[(.*?)]");
        Matcher m = p.matcher(condition);
        while (m.find()) {
            String value = m.group(1);
            if(operate.equals("LIKE")){
                //System.out.println("前一个字符:"+condition.charAt(m.start()-1));
                //System.out.println("后一个字符:"+condition.charAt(m.end()));
            }
            //System.out.println("值:"+value);
        } 
    }
}
