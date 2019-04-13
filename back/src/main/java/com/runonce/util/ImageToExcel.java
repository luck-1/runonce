package com.runonce.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.ClientAnchor;

public class ImageToExcel {

 private HSSFWorkbook wb = null;

 private HSSFSheet sheet = null;
 
 private HSSFPatriarch patriarch = null;

 public ImageToExcel() {

  wb = new HSSFWorkbook();
  sheet = wb.createSheet("mysheets1");
  sheet.setDefaultColumnWidth((short) 50);
  patriarch = sheet.createDrawingPatriarch();
 }

//这个是从网上找到的方法

 private void setImageToCell(short col1, int row1, short col2, int row2,
   String imagePath) {

  BufferedImage bufferImg = null;
  ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

  try {
   bufferImg = ImageIO.read(new File(imagePath));
   ImageIO.write(bufferImg, "jpg", byteArrayOut);
   

//能否写多张图片，关键在与HSSFPatriarch patriarch 这个变量。写多张图片时，

//HSSFPatriarch patriarch 应该时一个对象，不应该每次多新创建对象。
   //HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
   //HSSFClientAnchor anchor = new HSSFClientAnchor(2, 1, 1023, 255, col1, row1, col2, row2);
  // anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);


  // HSSFSheet sheet1 = hssfworkbook.CreateSheet("Sheet1");
  // HSSFPatriarch patriarch = sheet1.CreateDrawingPatriarch();
   HSSFClientAnchor a1 = new HSSFClientAnchor(255, 125, 1023, 150, (short) 1, 1,(short)1, 1);
  // patriarch.
   //HSSFSimpleShape line1 =patriarch.createSimpleShape(a1);
  // a1.set
   //line1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
  // line1.setLineStyle(HSSFShape.LINESTYLE_SOLID);
//在NPOI中线的宽度12700表示1pt,所以这里是0.5pt粗的线条。
  // line1.setLineWidth(6350);



   patriarch.createPicture(a1, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
   
  } catch (IOException e) {
   e.printStackTrace();
  }
 }

 public void createExcel() {

  FileOutputStream fileOut = null;

  try {

   setImageToCell((short) 1, 1, (short) 1, 1, "E:/11111.png");
   setImageToCell((short) 1, 1, (short) 1, 1, "E:/11111.png");
   setImageToCell((short) 1, 1, (short) 1, 1, "E:/11111.png");
   
   fileOut = new FileOutputStream("e:/myexcel.xls");
   wb.write(fileOut);
  } catch (FileNotFoundException e) {
   e.printStackTrace();
  } catch (IOException e) {
   e.printStackTrace();
  } finally {

   if (fileOut != null) {
    try {
     fileOut.close();
    } catch (IOException e) {
     e.printStackTrace();
    }
   }
  }
 }

 public static void main(String[] args) {

  ImageToExcel excel = new ImageToExcel();
  excel.createExcel();
 }
}