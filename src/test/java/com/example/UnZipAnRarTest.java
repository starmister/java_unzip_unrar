package com.example;


import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UnZipAnRarTest {
    @Test
    public void getUnZipAnnRarTest() {
        String originDir = "d:/test/";
//        String zipPath = originDir + "西班牙建模.zip";
//        File zipFile = new File(zipPath);
//        try {
//            UnZipAnRar.unZip(zipFile, "d:/test/",2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String rarPath = originDir + "西班牙建模.rar";
        File rarFile = new File(rarPath);
        try {
            UnZipAnRar.unRar(rarFile, "d:/test/",2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
