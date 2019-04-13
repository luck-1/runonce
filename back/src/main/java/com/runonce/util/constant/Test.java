
/*package com.runonce.util.constant;*/
/*
import com.runonce.model.system.User;
import com.runonce.util.ImportUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import com.runonce.util.EXUtil;
import com.runonce.util.OssClientUtil;
import com.runonce.util.SysUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import io.swagger.annotations.ApiModelProperty;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ddf.EscherBSERecord;
import org.apache.poi.hssf.model.InternalWorkbook;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.*;
import org.apache.poi.openxml4j.opc.internal.ContentType;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.ss.extractor.EmbeddedData;
import org.apache.poi.ss.extractor.EmbeddedExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTShape;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.List;*/

/*public class Test {*/
//    public static void resize(File originalFile, File resizedFile,
//                              int newWidth, float quality) throws IOException {
//
//        if (quality > 1) {
//            throw new IllegalArgumentException(
//                    "Quality has to be between 0 and 1");
//        }
//
//        ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
//        Image i = ii.getImage();
//
//        Image resizedImage = null;
//
//        int iWidth = i.getWidth(null);
//        int iHeight = i.getHeight(null);
//
//        if (iWidth > iHeight) {
//            resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight)
//                    / iWidth, Image.SCALE_SMOOTH);
//        } else {
//            resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight,
//                    newWidth, Image.SCALE_SMOOTH);
//        }
//
//        // This code ensures that all the pixels in the image are loaded.
//        Image temp = new ImageIcon(resizedImage).getImage();
//
//        // Create the buffered image.
//        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),
//                temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
//
//        // Copy image to buffered image.
//        Graphics g = bufferedImage.createGraphics();
//
//        // Clear background and paint the image.
//        g.setColor(Color.white);
//        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
//        g.drawImage(temp, 0, 0, null);
//        g.dispose();
//
//        // Soften.
//        float softenFactor = 0.05f;
//        float[] softenArray = { 0, softenFactor, 0, softenFactor,
//                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };
//        Kernel kernel = new Kernel(3, 3, softenArray);
//        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
//        bufferedImage = cOp.filter(bufferedImage, null);
//
//        // Write the jpeg to a file.
//        FileOutputStream out = new FileOutputStream(resizedFile);
//
//        // Encodes image as a JPEG data stream
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//
//        JPEGEncodeParam param = encoder
//                .getDefaultJPEGEncodeParam(bufferedImage);
//
//        param.setQuality(quality, true);
//
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(bufferedImage);
//    } // Example usage
//
//    public static void main(String[] args) throws IOException {
////		 File originalImage = new File("C:\\11.jpg");
////		 resize(originalImage, new File("c:\\11-0.jpg"),150, 0.7f);
////		 resize(originalImage, new File("c:\\11-1.jpg"),150, 1f);
//        File originalImage = new File("E:\\微信图片_20190107185622.png");
//        resize(originalImage, new File("E:\\1207-0.jpg"),150, 0.5F);
//        resize(originalImage, new File("E:\\1207-1.jpg"),150, 1f);
//    }


//
//    public static void main(String[] args) throws Exception {
//        User user = new User();
//
//        user.setSex(2);
//
//        user.setUserName(null);
//
//        User user1 = new User();
//
//        user1.setSex(2);
//
//        user1.setUserName("222");
//
//        compare(user, user1);
//        System.out.println(user);
//        System.out.println(user1);
//
//    }
//
//
//    public static<T> void compare(T var1, T var2)
//            throws Exception {
//
//
//        Field[] fs = var1.getClass().getDeclaredFields();
//        for (Field f : fs) {
//            f.setAccessible(true);
//
//            //  No final
//            if(Modifier.isFinal(f.getModifiers())){
//                continue;
//            }
//            ApiModelProperty  apiModelProperty  = var1.getClass().getDeclaredAnnotation(ApiModelProperty.class);
//            apiModelProperty.value();
//            Object oldObjValue = f.get(var1);
//            Object newObjValue = f.get(var2);
//
//            if (oldObjValue != null && newObjValue != null) {
//
//                if (oldObjValue.equals(newObjValue)) {
//                    f.set(var1,null);
//                    f.set(var2,null);
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws NoSuchFieldException,
//            SecurityException, IllegalArgumentException, IllegalAccessException {
//
//        User user = new User();
//        Class userCla = user.getClass();
//
//        Field[] fs = userCla.getDeclaredFields();
//
//        for (int i = 0; i < 1; i++) {
//            Field field  =fs[i];
//            field.set(user,"1111");
//        }
//
//
//        System.out.println(user);
//    }

/*
    public static void main(String[] args) throws Exception {


        new Test().test1();
    }

    private void test1() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(new File("E:\\M100甲醇汽车燃料加注站变更 事项3.0.xlsx"));
*/

//        System.out.println(list);
//        //读取sheet固定表名的index
//        int index = workbook.getSheetIndex("材料分组");
//        //读取index的sheet
//        XSSFSheet sheet = workbook.getSheetAt(index);
//
//        EmbeddedExtractor embeddedExtractor = new EmbeddedExtractor();
//
//        for (EmbeddedData ed : embeddedExtractor.extractAll(sheet)) {
//
//            String name =  ed.getFilename();
//            byte[] bytes = name.getBytes(Charset.forName("iso8859-1"));
//            name= new String(bytes,"GBK");
//            System.out.println(name + " (" + ed.getContentType() + ") - " + ed.getEmbeddedData().length + " bytes");
//            byte[] dataBuffer = ed.getEmbeddedData();
//
//            File dir = new File("D:\\impro");
//            if (!dir.exists() && !dir.isDirectory()) {
//                //判断文件目录是否存在
//                dir.mkdirs();
//            }
//            File file = new File("D:\\impro\\" + name);
//            FileOutputStream fos = new FileOutputStream(file);
//            BufferedOutputStream bos = new BufferedOutputStream(fos);
//            bos.write(dataBuffer);
//            fos.close();
//            bos.close();
//
//        }


//        for (POIXMLDocumentPart dr : sheet.getRelations()) {
//            if (dr instanceof XSSFDrawing) {
//                XSSFDrawing drawing = (XSSFDrawing) dr;
//                List<XSSFShape> shapes = drawing.getShapes();
//                for (XSSFShape shape : shapes) {
//                    XSSFObjectData obj = (XSSFObjectData) shape;
//
//
//                    PackagePart part = obj.getObjectPart();
//                    boolean bol = obj.hasDirectoryEntry();
//                    System.out.println(bol);
//
//                    int i = 0;
//                    if (bol) {
//                        InputStream inputStream = FileMagic.prepareToCheckMagic(part.getInputStream());
//
//                        POIFSFileSystem npoifsFileSystem = new POIFSFileSystem(inputStream);
//
//                        if (isOle10Native(npoifsFileSystem.getRoot())) {
//
//                            DirectoryNode directoryNode = npoifsFileSystem.getRoot();
//                            Set set = directoryNode.getEntryNames();
//                            System.out.println(set);
//                            byte[] dataBuffer = Ole10Native.createFromEmbeddedOleObject(npoifsFileSystem.getRoot()).getDataBuffer();
//                            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(dataBuffer);
//                            Image image = ImageIO.read(byteArrayInputStream);
//
//                            File dir = new File("D:\\");
//                            if (!dir.exists() && !dir.isDirectory()) {
//                                //判断文件目录是否存在
//                                dir.mkdirs();
//                            }
//                            File file;
//                            if (image!=null){
//                                int  o =((BufferedImage) image).getType();
//                                System.out.println(o);
//                                 file = new File("D:\\" + "未知嵌入文档："+i+".jpg");
//                            }else {
//                                 file = new File("D:\\" + "未知嵌入文档："+i);
//
//                            }
//
//                            FileOutputStream fos = new FileOutputStream(file);
//                            BufferedOutputStream bos = new BufferedOutputStream(fos);
//                            bos.write(dataBuffer);
//                            fos.close();
//                            bos.close();
//                            i++;
//                        }
//
//                    }
//
//                }
//            }
//        }


//    private void writeBinaryPackagePart(PackagePart part, File targetfolder, String extension, String fileName) throws IOException, Ole10NativeException {
//        if (StringUtils.isEmpty(fileName)) {
//
//        }
//        InputStream inputStream = FileMagic.prepareToCheckMagic(part.getInputStream());
//
//        if (FileMagic.valueOf(inputStream) == FileMagic.OLE2) {
//            POIFSFileSystem npoifsFileSystem = new POIFSFileSystem(inputStream);
//            if (isOle10Native(npoifsFileSystem.getRoot())) {
//                byte[] dataBuffer = Ole10Native.createFromEmbeddedOleObject(npoifsFileSystem.getRoot()).getDataBuffer();
//
//            } else if (npoifsFileSystem.getRoot().getEntryNames().contains("CONTENTS")) {
//
//            }
//        }
//
//    }
//
//    private boolean isOle10Native(DirectoryNode directoryNode) {
//        String ole10Native = Ole10Native.OLE10_NATIVE;
//        Iterator<Entry> entries = directoryNode.getEntries();
//        while (entries.hasNext()) {
//            Entry entry = entries.next();
//            if (entry.getName().contains(ole10Native)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     *  
//     *
//     * @param sheet
//     * @throws IOException
//     */
//    public static void process(XSSFSheet sheet) throws Exception {
//        for (POIXMLDocumentPart dr : sheet.getRelations()) {
//            if (dr instanceof XSSFDrawing) {
//                XSSFDrawing drawing = (XSSFDrawing) dr;
//                List<XSSFShape> shapes = drawing.getShapes();
//                for (XSSFShape shape : shapes) {
//                    XSSFObjectData pic = (XSSFObjectData) shape;
////                    pic,
////                    CTShape ctShape =    pic.getCTShape();
//
//                    XSSFPictureData picc = pic.getPictureData();
//                    XSSFClientAnchor anchor = (XSSFClientAnchor) pic.getAnchor();
//                    CTMarker ctMarker = anchor.getFrom();
//                    handle(ctMarker.getRow(), ctMarker.getCol(), picc);
//                }
//            }
//        }
//    }

//    /**
//     *  
//     *
//     * @param rowIndex
//     * @param colIndex
//     * @param picData
//     * @throws IOException
//     */
//    public static void handle(int rowIndex, int colIndex, PictureData picData) throws Exception {
//        String fileName = picData.suggestFileExtension();
//        fileName = "D:\\" + rowIndex + "_" + colIndex + "." + fileName;
//        byte[] data = picData.getData();
//        FileOutputStream out = new FileOutputStream(fileName);
//        out.write(data);
//        out.close();
//    }
//
//    /**
//     * 获取图片和位置 (xlsx)
//     *
//     * @param sheet
//     * @return
//     * @throws IOException
//     */
//    public static Map<String, XSSFPictureData> getPictures(XSSFSheet sheet) throws IOException {
//        Map<String, XSSFPictureData> map = new HashMap<String, XSSFPictureData>();
//        List<POIXMLDocumentPart> list = sheet.getRelations();
//        for (POIXMLDocumentPart part : list) {
//            if (part instanceof XSSFDrawing) {
//                XSSFDrawing drawing = (XSSFDrawing) part;
//                List<XSSFShape> shapes = drawing.getShapes();
//                for (XSSFShape shape : shapes) {
//                    XSSFPicture picture = (XSSFPicture) shape;
//                    XSSFClientAnchor anchor = picture.getPreferredSize();
//                    CTMarker marker = anchor.getFrom();
//                    String key = marker.getRow() + "-" + marker.getCol();
//                    map.put(key, picture.getPictureData());
//                }
//            }
//        }
//        return map;
//    }


//    static class SampleClass {
//        public static int sampleField = 5999;
//    }
  /*  }

/*    private static boolean isOle10Native(DirectoryNode directoryNode) {
        String ole10Native = Ole10Native.OLE10_NATIVE;
        Iterator<Entry> entries = directoryNode.getEntries();
        while (entries.hasNext()) {
            Entry entry = entries.next();
            if (entry.getName().contains(ole10Native)) {
                return true;
            }
        }
        return false;
    }*/
/*
//    public static final InputStream byte2Input(byte[] buf) {
//        return new ByteArrayInputStream(buf);
//    }
//
//    public static final byte[] input2byte(InputStream inStream)
//            throws IOException {
//        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
//        byte[] buff = new byte[100];
//        int rc = 0;
//        while ((rc = inStream.read(buff, 0, 100)) > 0) {
//            swapStream.write(buff, 0, rc);
//        }
//        byte[] in2b = swapStream.toByteArray();
//        return in2b;
//    }
}*/
package com.runonce.util.constant;
class  Test{

    public static void main(String[] args) {
        int a=2147483647;
        System.out.println(a);
    }

}

