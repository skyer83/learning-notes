package com.tanji168.webp.example;

import com.luciad.imageio.webp.WebPWriteParam;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EncodeTest {
    public static void main(String args[]) throws IOException {
        encodeTest2();
    }

    private static void encodeTest2() throws IOException {
        String inputJpgPath = "test_pic/test.jpg";
        String outputWebpPath = "test_pic/test_01.webp";

        long st = System.currentTimeMillis();
        ImageWriter writer = null;
        FileImageOutputStream fileImageOutputStream = null;
        try {
            // Obtain a WebP ImageWriter instance
            writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

            // Configure encoding parameters
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(WebPWriteParam.MODE_DEFAULT);

            // Configure the output on the ImageWriter
            fileImageOutputStream = new FileImageOutputStream(new File(outputWebpPath));
            writer.setOutput(new FileImageOutputStream(new File(outputWebpPath)));

            // Obtain an image to encode from somewhere
            BufferedImage image = ImageIO.read(new File(inputJpgPath));
            // Encode
            writer.write(null, new IIOImage(image, null, null), writeParam);
            System.out.println("cost: " + (System.currentTimeMillis() - st));
        } finally {
            if (writer != null) {
                // 释放 reader
                writer.dispose();
            }
            if (fileImageOutputStream != null) {
                // 关闭文件流
                fileImageOutputStream.close();
            }
        }
    }
    
    /**
     * 原代码的测试样例
     * @author shenjh
     * @since 2024/12/25 10:08
     */
    private static void encodeTest() throws IOException {
        String inputPngPath = "test_pic/test.png";
        String inputJpgPath = "test_pic/test.jpg";
        String outputWebpPath = "test_pic/test_.webp";

        // Obtain an image to encode from somewhere
        BufferedImage image = ImageIO.read(new File(inputJpgPath));

        // Obtain a WebP ImageWriter instance
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();

        // Configure encoding parameters
        WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
        writeParam.setCompressionMode(WebPWriteParam.MODE_DEFAULT);

        // Configure the output on the ImageWriter
        writer.setOutput(new FileImageOutputStream(new File(outputWebpPath)));

        // Encode
        long st = System.currentTimeMillis();
        writer.write(null, new IIOImage(image, null, null), writeParam);
        System.out.println("cost: " + (System.currentTimeMillis() - st));
    }
}
