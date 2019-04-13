package com.runonce.util;
 
import java.io.*;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
 
public class ExtractZip {
 
	public static final int BUFFER_SIZE = 1024;
 
	/**
	 * 解压 zip 文件
	 * 
	 * @param zipFile
	 *            zip 压缩文件
	 * @param destDir
	 *            zip 压缩文件解压后保存的目录
	 * @param encoding
	 *            zip 文件的编码
	 * @return 返回 zip 压缩文件里的文件名的 list
	 * @throws Exception
	 */
	public static List<String> unZip(File zipFile, String destDir, String encoding) throws Exception {
		// 如果 destDir 为 null, 空字符串, 或者全是空格, 则解压到压缩文件所在目录
		if (destDir == null || destDir.length() == 0) {
			destDir = zipFile.getParent();
		}
 
		destDir = destDir.endsWith(File.separator) ? destDir : destDir + File.separator;
		ZipArchiveInputStream is = null;
		List<String> fileNames = new ArrayList<String>();
 
		try {
			is = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE),
					encoding);
			ZipArchiveEntry entry = null;
 
			while ((entry = is.getNextZipEntry()) != null) {
				fileNames.add(entry.getName());
				File file = new File(destDir, entry.getName());
 
				if (entry.isDirectory()) {
					FileUtils.forceMkdir(file); // 创建文件夹，如果中间有路径会自动创建
				} else {
					OutputStream os = null;
 
					try {
						FileUtils.touch(file);
						os = new FileOutputStream(new File(destDir, entry.getName()));
						IOUtils.copy(is, os);
					} finally {
						IOUtils.closeQuietly(os);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			IOUtils.closeQuietly(is);
		}
 
		return fileNames;
	}
 
	/**
	 * 解压 zip 文件
	 * 
	 * @param zipFile
	 *            zip 压缩文件的路径
	 * @param destDir
	 *            zip 压缩文件解压后保存的目录
	 * @param encoding
	 *            zip 文件的编码
	 * @return 返回 zip 压缩文件里的文件名的 list
	 * @throws Exception
	 */
	public static List<String> unZip(String zipFile, String destDir, String encoding) throws Exception {
		File zipfile = new File(zipFile);
		return unZip(zipfile, destDir, encoding);
	}
 
	public static List<String> unZip(String zipFile, String destDir) throws Exception {
		return unZip(zipFile, destDir, "UTF-8");
	}
 
	public static void main(String[] args) throws Exception {
		List<String> names = unZip("e://22222.zip", "e://22222");
		System.out.println(names);
		readfile("e://22222/xl/media");
	}


	/**
	 * 读取某个文件夹下的所有文件
	 */
	public static boolean readfile(String filepath) throws FileNotFoundException, IOException {
		try {

			File file = new File(filepath);
			if (!file.isDirectory()) {
				System.out.println("文件");
				System.out.println("path=" + file.getPath());
				System.out.println("absolutepath=" + file.getAbsolutePath());
				System.out.println("name=" + file.getName());

			} else if (file.isDirectory()) {
				System.out.println("文件夹");
				String[] filelist = file.list();
				for (int i = 0; i < filelist.length; i++) {
					File readfile = new File(filepath + "\\" + filelist[i]);
					if (!readfile.isDirectory()) {
						System.out.println("path=" + readfile.getPath());
						System.out.println("absolutepath="
								+ readfile.getAbsolutePath());
						System.out.println("name=" + readfile.getName());

					} else if (readfile.isDirectory()) {
						readfile(filepath + "\\" + filelist[i]);
					}
				}

			}

		} catch (FileNotFoundException e) {
			System.out.println("readfile()   Exception:" + e.getMessage());
		}
		return true;
	}


}
