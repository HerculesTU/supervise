package com.housoo.platform.test.util;

import com.housoo.platform.core.util.PlatFileUtil;
import com.housoo.platform.core.util.PlatImageUtil;

/**
 * @author 胡裕
 *
 * 
 */
public class PlatImageUtilTestCase {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //System.out.println(1052/600);
        PlatImageUtil.compressImage("d:/22.jpg", "d:/22.jpg",1000,600);
        //规则200KB~500KB 3倍
        //500KB~1MB 3
        //1MB~
        // TODO Auto-generated method stub
        //PlatImageUtil.scaleImage("d:/22.jpg", "d:/22_thumb.jpg",4,false);
        //PlatImageUtil.scaleImage("d:/23.jpg", "d:/23_thumb.jpg",3,false);
        //PlatImageUtil.scaleImage("d:/24.jpg", "d:/24_thumb.jpg",2,false);
        //PlatImageUtil.scaleImage("d:/5兆.png", "d:/11_thumb.png",6,false);
        //PlatImageUtil.scaleImage("d:/10兆.jpg", "d:/11_thumb.jpg",3,false);
        //PlatImageUtil.scaleImage("d:/10兆.png", "d:/11_thumb.png",6,false);
    }

}
