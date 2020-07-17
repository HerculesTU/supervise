package com.housoo.platform.core.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成监察证
 * Created by cjr on 2018/8/1.
 */
public class GenJCZImg {

    public static int color_range = 0;

    /**
     * 生成图片
     *
     * @param src     底图
     * @param dest    jcz保存路径
     * @param headSrc 头像路径
     * @param zSrc    章路径
     * @param map     人员信息
     * @throws IOException
     */
    public String genImg(String src, String dest, String headSrc, String zSrc, Map<String, Object> map) throws IOException {
        //1. 底图
        BufferedImage image = ImageIO.read(new File(src));
        // 高度和宽度
        int height = image.getHeight();
        int width = image.getWidth();
        // 生产背景透明和内容透明的图片
        ImageIcon imageIcon = new ImageIcon(image);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        //2. 头像
        //2.1 缩放
        String hTempPath = "";
        String depart = map.get("SYSUSER_DEPART").toString();
        try {
            hTempPath = headSrc.substring(0, headSrc.lastIndexOf(".")) + "_temp" + headSrc.substring(headSrc.lastIndexOf("."));
            zoomImage(headSrc, hTempPath, 300, 360);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage image1 = ImageIO.read(new File(hTempPath));
        // 高度和宽度
        int height1 = image.getHeight();
        int width1 = image.getWidth();
        // 生产背景透明和内容透明的图片
        ImageIcon imageIcon1 = new ImageIcon(image1);
        BufferedImage bufferedImage1 = new BufferedImage(width1, height1, BufferedImage.TYPE_4BYTE_ABGR);

        //3. 章
        String zTempPath = "";
        try {
            zTempPath = zSrc.substring(0, zSrc.lastIndexOf(".")) + "_temp" + zSrc.substring(zSrc.lastIndexOf("."));
            zoomImage(zSrc, zTempPath, 180, 180);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BufferedImage bufferedImage2 = ImageIO.read(new File(zTempPath));

        Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics(); // 获取画笔
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setClip(new RoundRectangle2D.Double(0, 0, width, height, 15, 15));
        g2D.drawImage(imageIcon.getImage(), 0, 0, null); // 绘制Image的图片
        g2D.drawImage(imageIcon1.getImage(), 625, 60, null);
        setImgRgba(bufferedImage);
        //setImgRgba(bufferedImage1);

        // 绘制设置了RGB的新图片
        g2D.drawImage(bufferedImage, 0, 0, null);
        int a = (bufferedImage.getWidth() - bufferedImage1.getWidth()) / 2 + 273;
        int b = (bufferedImage.getHeight() - bufferedImage1.getHeight()) / 2 - 50;
        // 绘制设置了RGB的新图片
        g2D.drawImage(bufferedImage1, a, b, null);
        g2D.drawImage(bufferedImage2, 681, 378, null);
        if (map.size() > 0) {
            g2D.setColor(new Color(28, 28, 28));
            g2D.setFont(new Font("黑体", Font.BOLD, 30));
            g2D.drawString(map.get("SYSUSER_NAME").toString(), 200, 116);
            g2D.drawString("1".equals(map.get("SYSUSER_SEX").toString()) ? "男" : "女", 200, 190);
            g2D.drawString(map.get("SYSUSER_POSITION").toString(), 200, 266);
            if (depart.indexOf(";") != -1) {
                g2D.drawString(map.get("SYSUSER_DEPART").toString().substring(0, map.get("SYSUSER_DEPART").toString().indexOf(";")), 200, 330);
                g2D.drawString(map.get("SYSUSER_DEPART").toString().substring(map.get("SYSUSER_DEPART").toString().indexOf(";") + 1), 200, 365);
            } else if (depart.length() > 10) {
                g2D.drawString(map.get("SYSUSER_DEPART").toString().substring(0, 10), 200, 330);
                g2D.drawString(map.get("SYSUSER_DEPART").toString().substring(10), 200, 365);
            } else {
                g2D.drawString(map.get("SYSUSER_DEPART").toString(), 200, 341);
            }
            g2D.drawString(map.get("SYSUSER_DEPART").toString(), 200, 341);
            String SYSUSER_JCZBH = map.get("SYSUSER_JCZBH").toString();
            paintJCZBH(g2D, SYSUSER_JCZBH);
            // 有效期
            g2D.setColor(new Color(28, 28, 28));
            g2D.setFont(new Font("黑体", Font.BOLD, 28));
            //g2D.drawString(map.get("JCZ_START_TIME").toString().replaceAll("-",".")+"-"+map.get("JCZ_END_TIME").toString().replaceAll("-","."),200,530);
            MyDrawString(PlatDateTimeUtil.formatCSTDate(map.get("JCZ_START_TIME").toString(), "yyyy-MM-dd").replaceAll("-", ".") + "-" + PlatDateTimeUtil.formatCSTDate(map.get("JCZ_END_TIME").toString(), "yyyy-MM-dd").replaceAll("-", "."), 200, 530, 0.85, g2D);
        }
        // 生成图片为PNG
        ImageIO.write(bufferedImage, "png", new File(dest));
        //PlatImageUtil.scaleImage(dest,dest,PlatImageUtil.getScaleTimes(dest,1000),false);
        deleteTempFiles(hTempPath, zTempPath);
        return dest;
    }


    /**
     * 画监察证号
     *
     * @param g
     * @param jczbh
     */
    public void paintJCZBH(Graphics g, String jczbh) {
        char[] chars = jczbh.toCharArray();
        g.setFont(new Font("黑体", Font.BOLD, 30));
        for (int i = 0; i < chars.length; i++) {
            if (i == 3 || i == 4) {
                g.setColor(new Color(170, 32, 29));
            } else if (i == 5 || i == 6) {
                g.setColor(new Color(46, 105, 67));
            } else {
                g.setColor(new Color(28, 28, 28));
            }
            if (i > 0) {
                g.drawChars(chars, i, 1, 215 + i * 16, 415);    //显示指定大小颜色的字符串
            } else {
                g.drawChars(chars, i, 1, 200 + i * 20, 415);    //显示指定大小颜色的字符串
            }
        }
    }


    public static void MyDrawString(String str, int x, int y, double rate, Graphics g) {
        String tempStr = new String();
        int orgStringWight = g.getFontMetrics().stringWidth(str);
        int orgStringLength = str.length();
        int tempx = x;
        int tempy = y;
        while (str.length() > 0) {
            tempStr = str.substring(0, 1);
            str = str.substring(1, str.length());
            g.drawString(tempStr, tempx, tempy);
            tempx = (int) (tempx + (double) orgStringWight / (double) orgStringLength * rate);
        }
    }


    /**
     * 删除临时文件
     *
     * @param hTempPath
     * @param zTempPath
     */
    private void deleteTempFiles(String hTempPath, String zTempPath) {
        try {
            File file = new File(hTempPath);
            if (file.exists()) {
                file.delete();
            }
            File file1 = new File(zTempPath);
            if (file1.exists()) {
                file1.delete();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 判断是背景还是内容
     */
    public static boolean colorInRange(int color) {
        int red = (color & 0xff0000) >> 16;// 获取color(RGB)中R位
        int green = (color & 0x00ff00) >> 8;// 获取color(RGB)中G位
        int blue = (color & 0x0000ff);// 获取color(RGB)中B位
        // 通过RGB三分量来判断当前颜色是否在指定的颜色区间内
        if (red == color_range && green == color_range && blue == color_range) {
            return true;
        }
        return false;
    }

    /**
     * 设置图片像素点透明度
     */
    public void setImgRgba(BufferedImage img) {
        int alpha = 0; // 图片透明度
        // 外层遍历是Y轴的像素
        for (int y = img.getMinY(); y < img.getHeight(); y++) {
            // 内层遍历是X轴的像素
            for (int x = img.getMinX(); x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                // 对当前颜色判断是否在指定区间内
                /*if (colorInRange(rgb)){
                    alpha = 0;
                }else{
                    // 设置为不透明
                    alpha = 255;
                }*/
                if (x > 620 && x < 950 && y < 420 && y > 80) {
                    alpha = 255;
                } else {
                    if (colorInRange(rgb)) {
                        alpha = 0;
                    } else {
                        // 设置为不透明
                        alpha = 255;
                    }
                }
                // #AARRGGBB 最前两位为透明度
                rgb = (alpha << 24) | (rgb & 0x00ffffff);
                img.setRGB(x, y, rgb);
            }
        }
    }


    /**
     * 按长度和宽度缩放图片
     *
     * @param src
     * @param dest
     * @param w
     * @param h
     * @throws Exception
     */
    public void zoomImage(String src, String dest, int w, int h) throws Exception {

        double wr = 0, hr = 0;
        File srcFile = new File(src);
        File destFile = new File(dest);

        BufferedImage bufImg = ImageIO.read(srcFile);  //读取图片
        Image Itemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH); //设置缩放目标图片模板

        wr = w * 1.0 / bufImg.getWidth();     //获取缩放比例
        hr = h * 1.0 / bufImg.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(bufImg, null);
        try {
            ImageIO.write((BufferedImage) Itemp, "png", destFile);   //写入缩减后的图片
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * @param files 要拼接的文件列表
     * @param type  1 横向拼接， 2 纵向拼接
     * @Description:图片拼接 （注意：必须两张图片长宽一致哦）
     * @author:liuyc
     */
    public String mergeImage(String[] files, int type, String targetFile) {
        int len = files.length;
        if (len < 1) {
            throw new RuntimeException("图片数量小于1");
        }
        File[] src = new File[len];
        BufferedImage[] images = new BufferedImage[len];
        int[][] ImageArrays = new int[len][];
        for (int i = 0; i < len; i++) {
            try {
                src[i] = new File(files[i]);
                images[i] = ImageIO.read(src[i]);
                images[i] = roundImage(images[i], 996, 610, 76);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int width = images[i].getWidth();
            int height = images[i].getHeight();
            ImageArrays[i] = new int[width * height];
            ImageArrays[i] = images[i].getRGB(0, 0, width, height, ImageArrays[i], 0, width);
        }
        int newHeight = 0;
        int newWidth = 0;
        for (int i = 0; i < images.length; i++) {
            // 横向
            if (type == 1) { //横向
                newHeight = newHeight > images[i].getHeight() ? newHeight : images[i].getHeight();
                newWidth += images[i].getWidth();
            } else if (type == 2) {// 纵向
                newWidth = newWidth > images[i].getWidth() ? newWidth : images[i].getWidth();
                if (i == images.length - 1) {
                    newHeight += images[i].getHeight();
                } else {
                    newHeight += images[i].getHeight() + 50;
                }
            }
        }
        if (type == 1 && newWidth < 1) {
            return null;
        }
        if (type == 2 && newHeight < 1) {
            return null;
        }

        // 生成新图片
        try {
            BufferedImage ImageNew = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            int height_i = 0;
            int width_i = 0;
            for (int i = 0; i < images.length; i++) {
                if (type == 1) {
                    ImageNew.setRGB(width_i, 0, images[i].getWidth(), newHeight, ImageArrays[i], 0,
                            images[i].getWidth());
                    width_i += images[i].getWidth();
                } else if (type == 2) {
                    ImageNew.setRGB(0, height_i, newWidth, images[i].getHeight(), ImageArrays[i], 0, newWidth);
                    if (i == images.length - 1) {
                        height_i += images[i].getHeight();
                    } else {
                        height_i = height_i + 50 + images[i].getHeight();
                    }
                }
            }

            ImageIcon imageIcon = new ImageIcon(ImageNew);
            BufferedImage bufferedImage = new BufferedImage(
                    imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0,
                    imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage
                    .getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage
                        .getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    /* if (colorInRange(rgb)) {
                        alpha = 0;
                    } else {
                        alpha = 255;
                    }*/
                    if (j2 > 620 && j2 < 950 && j1 < 420 && j1 > 80) {
                        alpha = 255;
                    } else {
                        if (colorInRange(rgb)) {
                            alpha = 0;
                        } else {
                            // 设置为不透明
                            alpha = 255;
                        }
                    }
                    rgb = (alpha << 24) | (rgb & 0x00ffffff);
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            // 生成图片为PNG
            ImageIO.write(bufferedImage, "png", new File(targetFile));
            //输出想要的图片
            //ImageIO.write(ImageNew, "png", new File(targetFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return targetFile;
    }


    /**
     * 导入本地图片到缓冲区
     */
    public BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 生成新图片到本地
     */
    public void writeImageLocal(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File outputfile = new File(newImage);
                ImageIO.write(img, "png", outputfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * 图片设置圆角
     *
     * @param image
     * @param targetSizeW
     * @param targetSizeY
     * @param cornerRadius
     * @return
     */
    private static BufferedImage roundImage(BufferedImage image, int targetSizeW, int targetSizeY, int cornerRadius) {
        BufferedImage outputImage = new BufferedImage(targetSizeW, targetSizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outputImage.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, targetSizeW, targetSizeY, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return outputImage;
    }


    public static void main(String[] args) throws Exception {
        GenJCZImg test = new GenJCZImg();
        Map<String, Object> map = new HashMap();
        map.put("SYSUSER_NAME", "李小丽");
        map.put("SYSUSER_SEX", "2");
        map.put("SYSUSER_POSITION", "党组书记");
        map.put("SYSUSER_DEPART", "山西省监察委员会");
        map.put("SYSUSER_JCZBH", "晋JW000000036");
        map.put("JCZ_START_TIME", "2018-07-31");
        map.put("JCZ_END_TIME", "2018-07-31");
        test.genImg("C:\\Users\\Administrator\\Desktop\\jcz\\底图\\监察证2(1).png", "C:\\Users\\Administrator\\Desktop\\jcz1.png", "C:\\Users\\Administrator\\Desktop\\jcz\\ff8080816a34aaa8016a35224f4502c7_thumb.png", "C:\\Users\\Administrator\\Desktop\\jcz\\底图\\z.png", map);

        String[] strings = {

                "C:\\Users\\Administrator\\Desktop\\jcz1.png",

                "C:\\Users\\Administrator\\Desktop\\jcz\\底图\\监察证1(1).png"};

        test.mergeImage(strings, 2, "C:\\Users\\Administrator\\Desktop\\5.png");
        //makeCircularImg("C:\\\\Users\\\\Administrator\\\\Desktop\\\\jcz\\\\监察证1(1).png","C:\\\\Users\\\\Administrator\\\\Desktop\\\\监察证1(1)_1.png",996,610,75);
    }
}
