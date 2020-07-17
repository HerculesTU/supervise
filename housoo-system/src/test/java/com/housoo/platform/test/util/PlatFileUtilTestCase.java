package com.housoo.platform.test.util;

import java.io.File;
import java.io.IOException;

import jodd.io.FileUtil;

import com.housoo.platform.core.util.PlatFileUtil;

/**
 * @author 胡裕
 *
 * 
 */
public class PlatFileUtilTestCase {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String content = PlatFileUtil.readFileString("d:/CodeDao.ftl");
        if(content.contains("net.evecom.")){
            System.out.println("有..");
        }else{
            System.out.println("不行有..");
        }
    }

}
