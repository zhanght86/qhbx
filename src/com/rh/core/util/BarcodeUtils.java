package com.rh.core.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.pdf.BarcodePDF417;

/**
 * 条形码工具类
 * @author wanghg
 */
public class BarcodeUtils {
    /**
     * 生成value对应的二维码，写入out中
     * @param out 输出流
     * @param value 值
     * @param w 宽度
     * @param h 高度
     * @throws Exception 例外
     */
    public static void genQRCode(OutputStream out, String value, int w, int h) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = writer.encode(value, BarcodeFormat.QR_CODE, w, h, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int black = Color.BLACK.getRGB();
        int blank = new Color(255, 255, 255, 0).getRGB();
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                img.setRGB(x, y, matrix.get(x, y) ? black : blank);
            }
        }
        ImageIO.write(img, "png", out);
    }
    
    /**
     * 生成二维码图片文件
     * @param fileName 生成的图片文件名
     * @param width 宽度
     * @param height 高度
     * @param value 需要生成二维码的串
     * @throws Exception 例外
     */
    public static void genQRCodeFile(String fileName, String value, int width, int height) throws Exception {
    	genQRCode(new FileOutputStream(fileName), value, width, height);
    }
    
    /**
     * 生成value对应的PDF417二维码，写入out中
     * @param out 输出流
     * @param value 值
     * @throws Exception 例外
     */
    public static void genPDF417(OutputStream out, String value) throws Exception {
        BarcodePDF417 pdf417 = new BarcodePDF417();
        pdf417.setText(value);
        Image img = pdf417.createAwtImage(Color.BLACK, new Color(255, 255, 255));
        BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        bimg.getGraphics().drawImage(img, 0, 0, null);
        ImageIO.write(bimg, "png", out);
    }
}
