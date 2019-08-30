package com.example;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;


/**
 * 解压zip和rar压缩包
 * @author WeiYi
 */
public class UnZipAnRar {
    private static void isMakeDir(String outDir){
        File outFileDir = new File(outDir);
        if (!outFileDir.exists()) {
            boolean isMakDir = outFileDir.mkdirs();
            if (isMakDir) {
                System.out.println("创建压缩目录成功");
            }
        }
    }

    /**
     * 解压zip
     * @param zipFile zip文件
     * @param outDir 输出目录
     * @param level 目录层级
     * @throws IOException io异常
     */
    public static void unZip(File zipFile, String outDir,int level) throws IOException {
        //定义计数器
        int count = 0;
        isMakeDir(outDir);
        //防止解压出现乱码
        ZipFile zip = new ZipFile(zipFile, "GBK");
        Enumeration enumerationTemp = zip.getEntries();
        //首先判断目录层级，若目录层级太大直接返回
        while (enumerationTemp.hasMoreElements()){
            ZipEntry entry = (ZipEntry) enumerationTemp.nextElement();
            if (entry.isDirectory()) {
                if(count==level){
                    System.out.println("目录层级太长，解压失败");
                    return;
                }
                count++;
            }
        }
        Enumeration enumeration = zip.getEntries();
        for (; enumeration.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) enumeration.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            //处理压缩文件包含文件夹的情况
            if (entry.isDirectory()) {
                File fileDir = new File(outDir + zipEntryName);
                boolean isMkDir = fileDir.mkdir();
                if (isMkDir) {
                    System.out.println("创建目录成功");
                }
                continue;
            }

            String filePath = outDir + zipEntryName;
            String path = filePath.substring(0,filePath.lastIndexOf("/")+1);
            File fileDir = new File(path);
            if(!fileDir.exists()){
                boolean isMkDir = fileDir.mkdirs();
                if (isMkDir) {
                    System.out.println("创建目录成功");
                }
            }
            File file = new File(outDir, zipEntryName);
            boolean isCreateNewFile = file.createNewFile();
            if(isCreateNewFile){
                System.out.println("创建文件成功");
            }
            OutputStream out = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) > 0) {
                out.write(buff, 0, len);
            }
            in.close();
            out.close();
        }
    }

    /**
     * 解压rar
     * @param rarFile zip文件
     * @param outDir 输出目录
     * @param level  目录层级
     * @throws Exception 异常
     */
    public static void unRar(File rarFile, String outDir,int level) throws Exception {
        int count = 0;
        isMakeDir(outDir);
        Archive archiveTemp = new Archive(new FileInputStream(rarFile));
        Archive archive = new Archive(new FileInputStream(rarFile));
        FileHeader fileHeaderTemp = archiveTemp.nextFileHeader();
        while (fileHeaderTemp != null) {
            if (fileHeaderTemp.isDirectory()) {
                if (count == level) {
                    System.out.println("目录层级太长，解压失败");
                    return;
                }
                count++;
            }
            fileHeaderTemp = archiveTemp.nextFileHeader();
        }

        FileHeader fileHeader = archive.nextFileHeader();
        while (fileHeader != null) {
            if (fileHeader.isDirectory()) {
                fileHeader = archive.nextFileHeader();
                continue;
            }
            //防止解压出现乱码
            String fileName = fileHeader.getFileNameW().isEmpty() ? fileHeader
                    .getFileNameString() : fileHeader.getFileNameW();
            File out = new File(outDir+fileName);
            if (!out.exists()) {
                if (!out.getParentFile().exists()) {
                    boolean isSuccess = out.getParentFile().mkdirs();
                    if(isSuccess){
                        System.out.println("创建文件夹成功");
                    }
                }
                boolean isSuccess = out.createNewFile();
                if(isSuccess){
                    System.out.println("创建文件成功");
                }
            }
            FileOutputStream os = new FileOutputStream(out);
            archive.extractFile(fileHeader, os);

            os.close();

            fileHeader = archive.nextFileHeader();
        }
        archive.close();
    }

}
