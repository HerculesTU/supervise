package com.housoo.platform.core.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 描述 封装图片处理类库
 *
 * @author
 * @created 2017年1月7日 上午10:06:37
 */
public class PlatImageUtil {


    /**
     * 指定高宽缩放图片
     *
     * @param srcImageFile:源图片
     * @param destImageFile:目标图片
     * @param height:高
     * @param width:框
     * @param bb:是否补白
     */
    public static void scaleImage(String srcImageFile, String destImageFile, int height, int width, boolean bb) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (Integer.valueOf(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (Integer.valueOf(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform
                        .getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {//补白
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null)) {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                } else {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                }
                g.dispose();
                itemp = image;
            }
            PlatFileUtil.createDir(destImageFile);
            ImageIO.write((BufferedImage) itemp, "JPEG", new File(destImageFile));
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * @param srcImageFile
     * @param sourceBase
     * @return
     */
    public static int getScaleTimes(String srcImageFile, int sourceBase) {
        BufferedImage src;
        try {
            src = ImageIO.read(new File(srcImageFile));
            Integer width = src.getWidth(); // 得到源图宽
            Integer height = src.getHeight(); // 得到源图长
            int sourceScaleTimes = 1;
            Integer base = sourceBase;
            if (width >= height) {
                double value = width.doubleValue() / base.doubleValue();
                sourceScaleTimes = (int) Math.rint(value);
            } else {
                double value = height.doubleValue() / base.doubleValue();
                sourceScaleTimes = (int) Math.rint(value);
            }
            if (sourceScaleTimes == 0) {
                sourceScaleTimes = 1;
            }
            return sourceScaleTimes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 压缩图片
     *
     * @param srcImageFile  原图
     * @param destImageFile 目标原图
     * @param sourceBase    原图基准
     * @param thumbBase     缩略图基准
     */
    public static void compressImage(String srcImageFile, String destImageFile, int sourceBase, int thumbBase) {
        int sourceScaleTimes = getScaleTimes(srcImageFile, sourceBase);
        //压缩原图
        PlatImageUtil.scaleImage(srcImageFile, destImageFile, sourceScaleTimes, false);
        //压缩缩略图
        String fileExt = PlatFileUtil.getFileExt(destImageFile);
        String thumbPath = destImageFile.substring(0, destImageFile.lastIndexOf(".")) + "_thumb." + fileExt;
        int thumbScaleTimes = getScaleTimes(destImageFile, thumbBase);
        PlatImageUtil.scaleImage(destImageFile, thumbPath, thumbScaleTimes, false);
    }

    /**
     * 等比例缩放图片
     *
     * @param srcImageFile:源图片
     * @param destImageFile:目标图片
     * @param scaleTimes:缩放倍数
     * @param isEnlarge          是否放大 true:放大 false:缩小
     */
    public static void scaleImage(String srcImageFile, String destImageFile, int scaleTimes, boolean isEnlarge) {
        try {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            if (isEnlarge) {// 放大
                width = width * scaleTimes;
                height = height * scaleTimes;
            } else {// 缩小
                width = width / scaleTimes;
                height = height / scaleTimes;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            PlatFileUtil.createDir(destImageFile);
            ImageIO.write(tag, "JPEG", new File(destImageFile));// 输出到文件流
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 将图片格式进行转换
     *
     * @param srcImage:源图片
     * @param destImage:目标文件
     */
    public static void convertImageType(String srcImage, String destImage) {
        try {
            File f = new File(srcImage);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            PlatFileUtil.createDir(destImage);
            String newImageExt = PlatFileUtil.getFileExt(destImage);
            ImageIO.write(src, newImageExt.toUpperCase(), new File(destImage));
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 水印文字
     *
     * @param text:文件内容
     * @param srcImageFile:源文件
     * @param destImageFile:目标文件
     * @param fontName:字体名称
     * @param fontStyle:文字样式
     * @param color:颜色
     * @param fontSize:字体大小
     * @param x:x坐标
     * @param y:y坐标
     * @param alpha:透明度
     */
    public static void watermarkText(String text, String srcImageFile, String destImageFile, String fontName,
                                     int fontStyle, Color color, int fontSize, int x,
                                     int y, float alpha, String type, boolean isUnderLine) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, Color.white, null);
            g.setColor(color);
            Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
            map.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            if (isUnderLine) {
                g.setFont(new Font(fontName, fontStyle, fontSize).deriveFont(map));
            } else {
                g.setFont(new Font(fontName, fontStyle, fontSize));
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.setStroke(new BasicStroke(3));
            // 在指定坐标绘制水印文字
            g.drawString(text, x, y + fontSize);
            g.dispose();
            PlatFileUtil.createDir(destImageFile);
            ImageIO.write((BufferedImage) image, type, new File(destImageFile));// 输出到文件流
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 水印图片
     *
     * @param watermarkImage:水印内容图片
     * @param srcImageFile:源文件
     * @param destImageFile:目标文件
     * @param x:X坐标
     * @param y:Y坐标
     * @param alpha:透明度
     */
    public static void watermarkImage(String watermarkImage, String srcImageFile,
                                      String destImageFile, int x, int y, float alpha, String type) {
        try {
            File img = new File(srcImageFile);
            Image src = ImageIO.read(img);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, Color.white, null);
            // 水印文件
            Image src_biao = ImageIO.read(new File(watermarkImage));
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));
            g.drawImage(src_biao, x,
                    y, wideth_biao, height_biao, null);
            // 水印文件结束
            g.dispose();
            PlatFileUtil.createDir(destImageFile);
            ImageIO.write((BufferedImage) image, type, new File(destImageFile));
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 获取文字的长度
     *
     * @param text
     * @return
     */
    private static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            if (new String(text.charAt(i) + "").getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length / 2;
    }

    /**
     * 根据列和行来切割图片
     *
     * @param srcImageFile:源图片
     * @param destDir:目标文件夹
     * @param rows:行
     * @param cols:列
     */
    public static void curImage(String srcImageFile, String destDir, int rows, int cols) {
        try {
            if (rows <= 0 || rows > 20) {
                rows = 2; // 切片行数
            }
            if (cols <= 0 || cols > 20) {
                cols = 2; // 切片列数
            }
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image img;
                ImageFilter cropFilter;
                Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
                int destWidth = srcWidth; // 每张切片的宽度
                int destHeight = srcHeight; // 每张切片的高度
                // 计算切片的宽度和高度
                if (srcWidth % cols == 0) {
                    destWidth = srcWidth / cols;
                } else {
                    destWidth = (int) Math.floor(srcWidth / cols) + 1;
                }
                if (srcHeight % rows == 0) {
                    destHeight = srcHeight / rows;
                } else {
                    destHeight = (int) Math.floor(srcWidth / rows) + 1;
                }
                // 循环建立切片
                // 改进的想法:是否可用多线程加快切割速度
                PlatFileUtil.createDir(destDir);
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        // 四个参数分别为图像起点坐标和宽高
                        cropFilter = new CropImageFilter(j * destWidth, i * destHeight,
                                destWidth, destHeight);
                        img = Toolkit.getDefaultToolkit().createImage(
                                new FilteredImageSource(image.getSource(),
                                        cropFilter));
                        BufferedImage tag = new BufferedImage(destWidth,
                                destHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = tag.getGraphics();
                        g.drawImage(img, 0, 0, null); // 绘制缩小后的图
                        g.dispose();
                        // 输出为文件
                        ImageIO.write(tag, "JPEG", new File(destDir + "/"
                                + "_r" + i + "_c" + j + ".jpg"));
                    }
                }
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }


    /**
     * 按照坐标来切割图片
     *
     * @param srcImageFile:源图片
     * @param destImageFile:目标图片
     * @param x:X坐标
     * @param y:Y坐标
     * @param width:宽度
     * @param height:高度
     */
    public static void cutImage(String srcImageFile, String destImageFile,
                                int x, int y, int width, int height) {
        try {
            // 读取源图像
            BufferedImage bi = ImageIO.read(new File(srcImageFile));
            int srcWidth = bi.getHeight(); // 源图宽度
            int srcHeight = bi.getWidth(); // 源图高度
            if (srcWidth > 0 && srcHeight > 0) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
                // 四个参数分别为图像起点坐标和宽高
                ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
                Image img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(),
                                cropFilter));
                BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
                g.dispose();
                // 输出为文件
                PlatFileUtil.createDir(destImageFile);
                ImageIO.write(tag, "JPEG", new File(destImageFile));
            }
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 根据类型指定高宽缩放图片
     *
     * @param srcImageFile:源图片
     * @param destImageFile:目标图片
     * @param height:高
     * @param width:框
     * @param bb:是否补白
     * @param type               例如JPEG,PNG,BMP
     */
    public static void scaleImageByType(String srcImageFile, String destImageFile, int height,
                                        int width, boolean bb, String type) {
        try {
            double ratio = 0.0; // 缩放比例
            File f = new File(srcImageFile);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (Integer.valueOf(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (Integer.valueOf(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(AffineTransform
                        .getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {//补白
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null)) {
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                } else {
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                }
                g.dispose();
                itemp = image;
            }
            PlatFileUtil.createDir(destImageFile);
            ImageIO.write((BufferedImage) itemp, type, new File(destImageFile));
        } catch (IOException e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 坐标来切割矩形图片
     *
     * @param srcImageFile
     * @param destImageFile
     * @param x
     * @param y
     * @param destWidth
     * @param destHeight
     * @param type          例如JPEG,PNG,BMP
     */
    public static void cutRectangleImage(String srcImageFile, String destImageFile, int x, int y,
                                         int destWidth, int destHeight, String type) {
        try {
            Iterator iterator = ImageIO.getImageReadersByFormatName(type);/*PNG,BMP*/
            ImageReader reader = (ImageReader) iterator.next();/*获取图片尺寸*/
            InputStream inputStream = new FileInputStream(srcImageFile);
            ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
            reader.setInput(iis, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rectangle = new Rectangle(x, y, destWidth, destHeight);/*指定截取范围*/
            param.setSourceRegion(rectangle);
            BufferedImage bi = reader.read(0, param);
            PlatFileUtil.createDir(destImageFile);
            ImageIO.write(bi, type, new File(destImageFile));
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * 旋转图片
     *
     * @param srcImageFile:源文件的路径
     * @param destImageFile:目标文件的路径
     * @param degree
     * @param type
     */
    public static void rotateImg(String srcImageFile, String destImageFile, int degree, String type) {
        try {
            // 读取源图像
            //BufferedImage bi = ImageIO.read(new File(srcImageFile));
            Image src = Toolkit.getDefaultToolkit().getImage(new File(srcImageFile).getPath());
            BufferedImage bi = PlatImageUtil.toBufferedImage(src);
            int srcWidth = bi.getWidth(); // 源图宽度
            int srcHeight = bi.getHeight(); // 源图高度
            int w = 0;
            int h = 0;
            int x = 0;
            int y = 0;
            degree = degree % 360;
            if (degree < 0) {
                degree = 360 + degree;//将角度转换到0-360度之间
            }
            double ang = Math.toRadians(degree);//将角度转为弧度
            if (degree == 180 || degree == 0 || degree == 360) {
                w = srcWidth;
                h = srcHeight;
            } else if (degree == 90 || degree == 270) {
                w = srcHeight;
                h = srcWidth;
            } else {
                int d = srcWidth + srcHeight;
                w = (int) (d * Math.abs(Math.cos(ang)));
                h = (int) (d * Math.abs(Math.sin(ang)));
            }
            x = (w / 2) - (srcWidth / 2);//确定原点坐标
            y = (h / 2) - (srcHeight / 2);
            BufferedImage rotatedImage = new BufferedImage(w, h, bi.getType());
            Graphics2D gs = (Graphics2D) rotatedImage.getGraphics();
            rotatedImage = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.OPAQUE);
            AffineTransform at = new AffineTransform();
            at.rotate(ang, w / 2, h / 2);//旋转图象
            at.translate(x, y);
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
            op.filter(bi, rotatedImage);
            bi = rotatedImage;
//           ByteArrayOutputStream  byteOut= new ByteArrayOutputStream();  
//           ImageOutputStream iamgeOut = ImageIO.createImageOutputStream(byteOut);  
            // 输出为文件
            PlatFileUtil.createDir(destImageFile);
            ImageIO.write(bi, type, new File(destImageFile));
        } catch (Exception e) {
            PlatLogUtil.printStackTrace(e);
        }
    }

    /**
     * @param image
     * @return
     */
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        // This code ensures that all the pixels in the image are loaded  
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null),
                    image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen  
        }
        if (bimage == null) {
            // Create a buffered image using the default color model  
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), type);
        }
        // Copy image to buffered image  
        Graphics g = bimage.createGraphics();
        // Paint the image onto the buffered image  
        //g.drawImage(image, 0, 0, null);  
        g.drawImage(image, 0, 0, Color.white, null);

        g.dispose();
        return bimage;
    }

    /**
     * 二进制转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                sb.append("0" + stmp);
            } else {
                sb.append(stmp);
            }

        }
        return sb.toString();
    }

    /**
     * 将二进制字符串转换成图片
     *
     * @param data
     * @param filePath
     */
    public static void binaryToImg(String data, String filePath) {
        String folderPath = filePath.substring(0, filePath.lastIndexOf("/"));
        File filefolder = new File(folderPath);
        if (!filefolder.exists()) {
            filefolder.mkdirs();
        }
        String type = PlatFileUtil.getFileExt(filePath);
        BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_BYTE_BINARY);
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, byteOutputStream);
            byte[] bytes = hex2byte(data);
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            file.write(bytes);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 反格式化byte
     *
     * @param s
     * @return
     */
    public static byte[] hex2byte(String s) {
        byte[] src = s.toLowerCase().getBytes();
        byte[] ret = new byte[src.length / 2];
        for (int i = 0; i < src.length; i += 2) {
            byte hi = src[i];
            byte low = src[i + 1];
            hi = (byte) ((hi >= 'a' && hi <= 'f') ? 0x0a + (hi - 'a') : hi - '0');
            low = (byte) ((low >= 'a' && low <= 'f') ? 0x0a + (low - 'a') : low - '0');
            ret[i / 2] = (byte) (hi << 4 | low);
        }
        return ret;
    }

    /**
     * 将图片转换成二进制字符串
     *
     * @param filePath
     * @return
     */
    public static String imgToBinary(String filePath) {
        String type = PlatFileUtil.getFileExt(filePath);
        String res = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
            BufferedImage bm = ImageIO.read(bis);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bm, type, bos);
            bos.flush();
            byte[] data = bos.toByteArray();
            res = byte2hex(data);
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
