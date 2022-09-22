package com.taf.testautomation.utilities.imagecomparison;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
public class ImageComparison {

    private static BufferedImage img1 = null;
    private static BufferedImage img2 = null;
    private static double percentage = 0.0;

    public static double compareImages(String image1, String image2) {

        String image1Path = "./" + image1;
        String image2Path = "./src/test/resources/testimages/" + image2;

        try {
            File file1 = new File(image1Path);
            File file2 = new File(image2Path);
            img1 = ImageIO.read(file1);
            img2 = ImageIO.read(file2);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        int width1 = img1.getWidth();
        int width2 = img2.getWidth();
        int height1 = img1.getHeight();
        int height2 = img2.getHeight();
        if ((width1 != width2) || (height1 != height2))
            log.error("Error: Image dimensions mismatch");
        else {
            long difference = 0;
            for (int y = 0; y < height1; y++) {
                for (int x = 0; x < width1; x++) {
                    int rgbA = img1.getRGB(x, y);
                    int rgbB = img2.getRGB(x, y);
                    int redA = (rgbA >> 16) & 0xff;
                    int greenA = (rgbA >> 8) & 0xff;
                    int blueA = (rgbA) & 0xff;
                    int redB = (rgbB >> 16) & 0xff;
                    int greenB = (rgbB >> 8) & 0xff;
                    int blueB = (rgbB) & 0xff;
                    difference += Math.abs(redA - redB);
                    difference += Math.abs(greenA - greenB);
                    difference += Math.abs(blueA - blueB);
                }
            }

            double total_pixels = width1 * height1 * 3;

            double avg_different_pixels = difference /
                    total_pixels;

            percentage = (avg_different_pixels /
                    255) * 100;
        }
        log.info("The difference in percentage for image comparison" + percentage);
        return percentage;
    }
}
