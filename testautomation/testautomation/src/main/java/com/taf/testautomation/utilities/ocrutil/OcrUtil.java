package com.taf.testautomation.utilities.ocrutil;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class OcrUtil {

    private static String result = "";
    private static Tesseract tesseract = new Tesseract();

    static {
        System.setProperty("jna.library.path", "/usr/local/Cellar/tesseract/4.1.1/lib/");
        tesseract.setDatapath("./tessdata");
    }

    public static String readImage() {
        try {
            result = tesseract.doOCR(new File("./screenshot.png"));
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return result;
    }
}
