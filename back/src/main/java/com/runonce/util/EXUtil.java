package com.runonce.util;

import org.apache.poi.hpsf.ClassIDPredefined;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ooxml.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.*;
import org.apache.poi.poifs.filesystem.Ole10Native;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtension;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOfficeArtExtensionList;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTPicture;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTOleObjects;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import javax.imageio.ImageIO;
import javax.xml.namespace.QName;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class EXUtil {

    /**
     * 默认偏移像素单位（ DEFAULT_OFFSET_PIX * 1 为偏移一像素）
     */
    public static final int DEFAULT_OFFSET_PIX = Units.EMU_PER_CHARACTER / 7;

    /**
     * 默认行高
     */
    public static final int DEFAULT_ROW_HEIGHT_PIX = 18;    //row.getHeightInPoints()/Units.POINT_DPI) * Units.PIXEL_DPI;

    /**
     * 默认行宽（非像素）
     */
    public static final int DEFAULT_COL_WIDGHT = 2300;

    /**
     * 默认行宽像素值
     */
    public static final int DEFAULT_COL_WIDGHT_PIX = 54;    //sheet.getColumnWidthInPixels(20);

    /**
     * 默认行高（非像素setHeight() 使用）
     */
    public static final int DEFAULT_ROW_HEIGHT = 270;

    /**
     * 最大行高（非像素setHeight() 使用）
     */
    public static final int MAX_ROW_HEIGHT = 9527;

    /**
     * 最大行高像素值
     */
    public static final int MAX_ROW_HEIGHT_PIX = MAX_ROW_HEIGHT / (DEFAULT_ROW_HEIGHT / DEFAULT_ROW_HEIGHT_PIX); //635

    /**
     * 文件嵌入cell
     *
     * @param sheet
     * @param image
     * @param fileName
     * @param startColumn
     * @param startRow
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static void embedObjectToCell(XSSFSheet sheet, byte[] image, String fileName, int startColumn, int startRow, int x_index, int y_index) throws IOException, OpenXML4JException {
        XSSFWorkbook workbook = sheet.getWorkbook();
        int templateId = workbook.addPicture(image, Workbook.PICTURE_TYPE_PNG);
        System.out.println("templateId :" + templateId);
        String imgPckRelId = addImageToSheet(sheet, templateId, Workbook.PICTURE_TYPE_PNG);
        System.out.println("imgPckRelId :" + imgPckRelId);
        //嵌入位置



        XSSFClientAnchor imgAnchor1 = new XSSFClientAnchor(setEmbedDx(x_index), setEmbedDy(y_index), 0, 0, startColumn, startRow, startColumn + 1, startRow + 1);
        //打开后显示的文件
        String oleRelId1 = showPicture(sheet, image, fileName, templateId);
        //显示对象图标
        addImageToShape(sheet, imgAnchor1, templateId);
        //嵌入文件与图标关联
        addObjectToShape(sheet, imgAnchor1, templateId, oleRelId1, imgPckRelId, "包装程序外壳对象");
    }

    private static String showPicture(XSSFSheet sheet, byte[] image, String fileName, int oleId) throws IOException, InvalidFormatException {

        Ole10Native ole10 = new Ole10Native(fileName, fileName, fileName, image);
        //写入OLE文件
        ByteArrayOutputStream bos = new ByteArrayOutputStream(500);
        ole10.writeOut(bos);

        //POI文件系统
        POIFSFileSystem poifs = new POIFSFileSystem();
        poifs.getRoot().createDocument(Ole10Native.OLE10_NATIVE, new ByteArrayInputStream(bos.toByteArray()));
        poifs.getRoot().setStorageClsid(ClassIDPredefined.OLE_V1_PACKAGE.getClassID());

        final PackagePartName pnOLE = PackagingURIHelper.createPartName("/xl/embeddings/oleObject" + oleId + ".bin");
        final PackagePart partOLE = sheet.getWorkbook().getPackage().createPart(pnOLE, "application/vnd.openxmlformats-officedocument.oleObject");
        PackageRelationship prOLE = sheet.getPackagePart().addRelationship(pnOLE, TargetMode.INTERNAL, POIXMLDocument.OLE_OBJECT_REL_TYPE);
        OutputStream os = partOLE.getOutputStream();
        poifs.writeFilesystem(os);
        os.close();
        poifs.close();
        return prOLE.getId();
    }

    private static String addImageToSheet(XSSFSheet sh, int imgId, int pictureType) throws InvalidFormatException {

        final PackagePartName pnIMG = PackagingURIHelper.createPartName("/xl/media/image" + (imgId + 1) + (pictureType == Workbook.PICTURE_TYPE_PNG ? ".png" : ".jpeg"));
        PackageRelationship prIMG = sh.getPackagePart().addRelationship(pnIMG, TargetMode.INTERNAL, PackageRelationshipTypes.IMAGE_PART);
        return prIMG.getId();
    }

    private static void addImageToShape(XSSFSheet sh, XSSFClientAnchor imgAnchor, int imgId) {
        final String drawNS = "http://schemas.microsoft.com/office/drawing/2010/main";
        //每个sheet只能获取一个
        XSSFDrawing pat = sh.getDrawingPatriarch();
        if (pat == null) {
            pat = sh.createDrawingPatriarch();
        }
        XSSFPicture pic = pat.createPicture(imgAnchor, imgId);

        CTPicture cPic = pic.getCTPicture();
//        int shapeId = (int) cPic.getNvPicPr().getCNvPr().getId()-1;
        cPic.getNvPicPr().getCNvPr().setHidden(false);
        CTOfficeArtExtensionList extLst = cPic.getNvPicPr().getCNvPicPr().addNewExtLst();
        CTOfficeArtExtension ext = extLst.addNewExt();
        ext.setUri("{63B3BB69-23CF-44E3-9099-C40C66FF867C}");
        XmlCursor cur = ext.newCursor();
        cur.toEndToken();
        cur.beginElement(new QName(drawNS, "compatExt", "a14"));
        cur.insertNamespace("a14", drawNS);
        cur.insertAttributeWithValue("spid", "_x0000_s" + imgId);
        cur.dispose();



    }

    private static void addObjectToShape(XSSFSheet sh, XSSFClientAnchor imgAnchor, int shapeId, String objRelId, String imgRelId, String progId) {

        final String relationshipsNS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
        CTWorksheet cwb = sh.getCTWorksheet();
        CTOleObjects oo = cwb.isSetOleObjects() ? cwb.getOleObjects() : cwb.addNewOleObjects();

        CTOleObject ole1 = oo.addNewOleObject();
        ole1.setProgId(progId);
        ole1.setShapeId(shapeId);
        ole1.setId(objRelId);
        ole1.setAutoLoad(true);
        XmlCursor cur1 = ole1.newCursor();
        cur1.toEndToken();
        cur1.beginElement("objectPr", XSSFRelation.NS_SPREADSHEETML);
        cur1.insertAttributeWithValue("id", relationshipsNS, imgRelId);
        cur1.insertAttributeWithValue("defaultSize", "0");
        cur1.beginElement("anchor", XSSFRelation.NS_SPREADSHEETML);
        cur1.insertAttributeWithValue("moveWithCells", "1");
        imgAnchor.setAnchorType(XSSFClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);

        CTTwoCellAnchor anchor = CTTwoCellAnchor.Factory.newInstance();
        anchor.setFrom(imgAnchor.getFrom());
        anchor.setTo(imgAnchor.getTo());

        XmlCursor cur2 = anchor.newCursor();
        cur2.copyXmlContents(cur1);
        cur2.dispose();

        cur1.toParent();
        cur1.toFirstChild();
        cur1.setName(new QName(XSSFRelation.NS_SPREADSHEETML, "from"));
        cur1.toNextSibling();
        cur1.setName(new QName(XSSFRelation.NS_SPREADSHEETML, "to"));

        cur1.dispose();
    }

    public static void output(XSSFWorkbook workbook) throws Exception {
        OutputStream outputStream = new FileOutputStream("C:\\Users\\000000\\Desktop\\" + SysUtil.getUUID().substring(0, 4) + ".xlsx");
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }

    public static void removeSheet(XSSFWorkbook workbook, List<String> list) {
        list.forEach((s) -> {
            int index = workbook.getSheetIndex(s);
            if (index != -1) {
                workbook.removeSheetAt(index);
            }
        });
    }

    public static Boolean isPicture(String objectName) {
        String suffixName = objectName.substring(objectName.lastIndexOf(".") + 1).toLowerCase();
        if (suffixName.equals("png") || suffixName.equals("jpg") || suffixName.equals("bmp")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 添加图片到sheet里
     *
     * @param sheet1
     * @param bytes
     * @param showWidgth
     * @param x
     * @param y
     * @throws IOException
     */
    public static void addPictureToSheet(XSSFSheet sheet1, byte[] bytes, String picName, int showWidgth, int x, int y) throws IOException {
        XSSFWorkbook workbook = sheet1.getWorkbook();

        XSSFCreationHelper creationHelper = workbook.getCreationHelper();
        XSSFClientAnchor anchor = creationHelper.createClientAnchor();

        XSSFDrawing drawing = sheet1.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet1.createDrawingPatriarch();
        }
        anchor.setRow1(y);
        anchor.setCol1(x);

        //获得图片
        BufferedImage sourceImg = ImageIO.read(new ByteArrayInputStream(bytes));
        //图片高
        float imgHeight = sourceImg.getHeight();
        //图片宽
        float imgWidght = sourceImg.getWidth() ;
        //显示宽-列数
        float widght = showWidgth;
        //比例
        float ratio = imgWidght/EXUtil.DEFAULT_COL_WIDGHT_PIX / widght;
        //显示宽
        float height = imgHeight / EXUtil.DEFAULT_ROW_HEIGHT_PIX / ratio;

        //图片类型
        int type = picName.substring(picName.lastIndexOf(".")).toLowerCase().equals("png") ? Workbook.PICTURE_TYPE_PNG : Workbook.PICTURE_TYPE_JPEG;
        int pictureIdx1 = workbook.addPicture(bytes, type);
        XSSFPicture picture = drawing.createPicture(anchor, pictureIdx1);
        picture.resize(showWidgth,height);
    }


    /**
     * 由图片缩放宽度获得缩放高度
     *
     * @param bytes
     * @param showWight
     * @return
     * @throws IOException
     */
    public static int getHight(byte[] bytes, int showWight) throws IOException {

        BufferedImage sourceImg = ImageIO.read(new ByteArrayInputStream(bytes));

        float imgHeight = sourceImg.getHeight();  //图片高
        float imgWidght = sourceImg.getWidth();  //图片宽
        int showHeight = (int) (imgHeight / (imgWidght / showWight));
        return showHeight;
    }

    /**
     * 由图片缩放高度获得缩放宽度
     *
     * @param bytes
     * @param showHeight
     * @return
     * @throws IOException
     */
    public static int getWidght(byte[] bytes, int showHeight) throws IOException {
        BufferedImage sourceImg = ImageIO.read(new ByteArrayInputStream(bytes));
        float imgHeight = sourceImg.getHeight();  //图片高
        float imgWidght = sourceImg.getWidth();  //图片宽
        int showWight = (int) (imgWidght / (imgHeight / showHeight));
        return showWight;
    }

    /**
     * 由像素值设置行高
     *
     * @param row
     * @param height_pix
     */
    public static void setRowHeight(XSSFRow row, int height_pix) {
        row.setHeight((short) (height_pix * (EXUtil.DEFAULT_ROW_HEIGHT / EXUtil.DEFAULT_ROW_HEIGHT_PIX)));
    }

    /**
     * 根据像素值在单元格内纵向偏移
     *
     * @param anchor
     * @param height_pix
     */
    public static void setDy(XSSFClientAnchor anchor, int height_pix) {
        anchor.setDy1(height_pix * EXUtil.DEFAULT_OFFSET_PIX);
    }

    /**
     * 根据像素值在单元格内纵向偏移
     *
     * @param anchor
     * @param widght_pix
     */
    public static void setDx(XSSFClientAnchor anchor, int widght_pix) {
        anchor.setDx1(widght_pix * EXUtil.DEFAULT_OFFSET_PIX);
    }

    /**
     * 同一个单元格内嵌入多个对象，设置横向偏移
     *
     * @param x
     * @return
     */
    public static int setEmbedDx(float x) {
        return (int) ((DEFAULT_OFFSET_PIX * 7 * 10) * x);
    }

    /**
     * 同一个单元格内嵌入多个对象，设置纵向偏移
     *
     * @param y
     * @return
     */
    public static int setEmbedDy(float y) {
        return (int) ((DEFAULT_OFFSET_PIX * 7 * 12) * y);
    }

    /**
     * 同一个单元格添加多张图片（竖向排列）
     *
     * @param sheet1
     * @param anchor
     * @param bytes
     * @param showHeight
     * @param showWight
     * @return
     * @throws IOException
     */
    public static int addPictureToCell(XSSFSheet sheet1, XSSFClientAnchor anchor, byte[] bytes, int showHeight, int showWight) throws IOException {

        XSSFWorkbook workbook = sheet1.getWorkbook();

        XSSFDrawing drawing = sheet1.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet1.createDrawingPatriarch();
        }
        //获得图片
        BufferedImage sourceImg = ImageIO.read(new ByteArrayInputStream(bytes));

        float imgHeight = sourceImg.getHeight();  //图片高
        float imgWidght = sourceImg.getWidth();  //图片宽

        if (showHeight == 0) {
            showHeight = (int) (imgHeight / (imgWidght / showWight));
        } else {
            showWight = (int) (imgWidght / (imgHeight / showHeight));
        }

        ByteArrayOutputStream byteArrayOutputStream = ExportUtil.zoomByScale(showWight, showHeight, sourceImg);
        int pictureIdx1 = workbook.addPicture(byteArrayOutputStream.toByteArray(), Workbook.PICTURE_TYPE_PNG);
        byteArrayOutputStream.close();

        XSSFPicture picture = drawing.createPicture(anchor, pictureIdx1);
        picture.resize();
        return showHeight;
    }

    /**
     * 导出多张图片
     * @param sheet
     * @param bytes
     * @param title
     * @param maxWidth
     * @param maxHeight
     * @param startCell
     * @param startRow
     * @throws IOException
     */
    public static void sheetAddPic(XSSFSheet sheet,byte[] bytes,String title,int maxWidth,int maxHeight,int startCell,int startRow) throws IOException {
        XSSFWorkbook workbook = sheet.getWorkbook();
        int picIdx = workbook.addPicture(bytes,Workbook.PICTURE_TYPE_PNG);

        sheet.addMergedRegion(new CellRangeAddress(startRow,startRow,startCell,startCell + maxWidth - 1));
        XSSFCell cell = sheet.createRow(startRow).createCell(startCell);
        cell.setCellValue(title);
        cell.setCellStyle(getStyle(workbook,BorderStyle.NONE,getFont(workbook)));

        XSSFDrawing drawing = sheet.getDrawingPatriarch();
        if (drawing == null) {
            drawing = sheet.createDrawingPatriarch();
        }
        //获得图片
        BufferedImage sourceImg = ImageIO.read(new ByteArrayInputStream(bytes));
        float imgHeight = sourceImg.getHeight();  //图片高
        float imgWidght = sourceImg.getWidth();  //图片宽

        float showHeight = imgHeight / (imgWidght / maxWidth) * (EXUtil.DEFAULT_COL_WIDGHT_PIX / EXUtil.DEFAULT_ROW_HEIGHT_PIX);
        float showWidth = maxWidth;

        if(showHeight > maxHeight){
            showHeight = maxHeight;
            showWidth = imgWidght / (imgHeight / showHeight) / (EXUtil.DEFAULT_COL_WIDGHT_PIX / EXUtil.DEFAULT_ROW_HEIGHT_PIX);
        }

        XSSFCreationHelper creationHelper = workbook.getCreationHelper();
        XSSFClientAnchor anchor = creationHelper.createClientAnchor();

        anchor.setRow1(startRow + 1);
        anchor.setCol1(startCell );

        XSSFPicture picture = drawing.createPicture(anchor, picIdx);
        picture.resize(showWidth,showHeight);
    }

    /**
     * 默认样式
     *
     * @param workbook
     * @param border
     * @param font
     * @return
     */
    public static XSSFCellStyle getStyle(XSSFWorkbook workbook, BorderStyle border, XSSFFont font) {
        XSSFCellStyle style = workbook.createCellStyle();

        //设置单元格边框
        style.setBorderBottom(border);
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);

        style.setAlignment(HorizontalAlignment.CENTER);//水平居中对齐
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中对齐
        style.setWrapText(true);//自动换行

        style.setFont(font);
        return style;
    }

    /**
     * 字体：微软雅黑，黑色，不加粗，9号
     *
     * @param workbook
     * @return
     */
    public static XSSFFont getFont(XSSFWorkbook workbook) {

        //设置字体
        XSSFFont font = workbook.createFont();
        //颜色
        font.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //字体
        font.setFontName("微软雅黑");
        //设置字体大小
        font.setFontHeightInPoints((short) 9);
        return font;
    }
}