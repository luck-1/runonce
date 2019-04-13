package com.runonce.util;


import java.io.File;
import java.io.IOException;

public class Realname {

 public static void main(String[] args) throws IOException{
File file=new File("e:/22222.xlsx"); //指定文件名及路径  


 file.renameTo(new File("e:/22222"+".zip")); //改名     




 }




}

