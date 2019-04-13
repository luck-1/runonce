package com.runonce.util;

import com.runonce.model.http.PdfToImageModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfToImage {

    /**
     *  dpi越大转换后越清晰，相对转换速度越慢
     */
    private static final float DPI = 100f;

    /**
     * 转换后格式
     */
    private static final String TYPE = "jpg";

    public static void main(String[] args){
        String path = "C:\\Users\\joker\\Desktop\\1.pdf";
    }


    public static List<byte[]> pdf2png(InputStream inputStream) {
        List<byte[]> list = new ArrayList<>();
        ByteArrayOutputStream byteArrayOutputStream = null;
        try{
            PDDocument pdDocument = PDDocument.load(inputStream);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pages = pdDocument.getNumberOfPages();
            for (int i=0;i<pages;i++) {
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i,DPI);
                byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage,TYPE,byteArrayOutputStream);
                list.add(byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获得文件后缀名
     * @param filename
     * @return
     */
    public static String getSuffix(String filename) {
        String str = "";
        if (!StringUtils.isEmpty(filename)) {
            try {
                str = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            } catch (Exception e) {
                str = "";
            }
        }
        return str;
    }

}
