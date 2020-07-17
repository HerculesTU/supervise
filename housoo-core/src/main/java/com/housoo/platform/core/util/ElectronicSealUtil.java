package com.housoo.platform.core.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;

/**
 * 电子签章工具类
 *
 * @author gf
 * date 2019-03-22
 */
public class ElectronicSealUtil {

    /**
     * 测试方法
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        BufferedImage image = startGraphics2D(500, 500, "山西省监察委员会", "证件专用章", "2018年06月11日");
        try {
            //将其保存在D:\\下，得有这个目录
            String filePath = "D:\\" + System.currentTimeMillis() + ".png";
            ImageIO.write(image, "png", new File(filePath));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 开始绘制2D图片
     *
     * @param width         章的宽度
     * @param height        章的高度
     * @param companyName   单位名称
     * @param centerName    章中心的内容
     * @param customContent 自定义内容，比如年月日
     * @return
     */
    public static BufferedImage startGraphics2D(int width, int height, String companyName, String centerName, String customContent) {
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 增加下面代码使得背景透明
        buffImg = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g.dispose();
        g = buffImg.createGraphics();
        // 背景透明代码结束

        g.setColor(Color.RED);
        //设置锯齿圆滑
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //绘制圆
        int radius = height / 3;//周半径
        int center_x = width / 2;//画图所出位置
        int center_y = height / 2;//画图所处位置

        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(center_x, center_y, center_x + radius, center_y + radius);
        g.setStroke(new BasicStroke(10));//设置圆的宽度
        g.draw(circle);

        //绘制中间的五角星
        g.setFont(new Font("宋体", Font.BOLD, 120));
        g.drawString("★", center_x - (120 / 2), center_y + (120 / 3));
        //绘制中间图形
        //g.drawImage();

        //添加姓名
        g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 30));// 写入签名
        g.drawString(centerName, center_x - (65), center_y + (30 + 50));

        //添加年份
        g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 20));// 写入签名
        g.drawString(customContent, center_x - (60), center_y + (30 + 80));

        //根据输入字符串得到字符数组
        String[] messages1 = companyName.split("", 0);
        String[] messages2 = new String[messages1.length];
        System.arraycopy(messages1, 0, messages2, 0, messages1.length);

        //输入的字数
        int inputLength = messages2.length;

        //设置字体属性
        int fontSize = 40;
        Font f = new Font("Serif", Font.BOLD, fontSize);

        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = f.getStringBounds(companyName, context);

        //字符宽度＝字符串长度/字符数
        double char_interval = (bounds.getWidth() / inputLength);
        //上坡度
        double ascent = -bounds.getY();

        int first = 0, second = 0;
        boolean odd = false;
        if (inputLength % 2 == 1) {
            first = (inputLength - 1) / 2;
            odd = true;
        } else {
            first = (inputLength) / 2 - 1;
            second = (inputLength) / 2;
            odd = false;
        }

        double radius2 = radius - ascent;
        double x0 = center_x;
        double y0 = center_y - radius + ascent;
        //旋转角度
        double a = 2 * Math.asin(char_interval / (2 * radius2));

        if (odd) {
            g.setFont(f);
            g.drawString(messages2[first], (float) (x0 - char_interval / 2), (float) y0);

            //中心点的右边
            for (int i = first + 1; i < inputLength; i++) {
                double aa = (i - first) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages2[i], (float) (x0 + ax - char_interval / 2 * Math.cos(aa)), (float) (y0 + ay - char_interval / 2 * Math.sin(aa)));
            }
            //中心点的左边
            for (int i = first - 1; i > -1; i--) {
                double aa = (first - i) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages2[i], (float) (x0 - ax - char_interval / 2 * Math.cos(aa)), (float) (y0 + ay + char_interval / 2 * Math.sin(aa)));
            }

        } else {
            //中心点的右边
            for (int i = second; i < inputLength; i++) {
                double aa = (i - second + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages2[i], (float) (x0 + ax - char_interval / 2 * Math.cos(aa)), (float) (y0 + ay - char_interval / 2 * Math.sin(aa)));
            }

            //中心点的左边
            for (int i = first; i > -1; i--) {
                double aa = (first - i + 0.5) * a;
                double ax = radius2 * Math.sin(aa);
                double ay = radius2 - radius2 * Math.cos(aa);
                AffineTransform transform = AffineTransform.getRotateInstance(-aa);//,x0 + ax, y0 + ay);
                Font f2 = f.deriveFont(transform);
                g.setFont(f2);
                g.drawString(messages2[i], (float) (x0 - ax - char_interval / 2 * Math.cos(aa)), (float) (y0 + ay + char_interval / 2 * Math.sin(aa)));
            }
        }

        return buffImg;
    }

}
