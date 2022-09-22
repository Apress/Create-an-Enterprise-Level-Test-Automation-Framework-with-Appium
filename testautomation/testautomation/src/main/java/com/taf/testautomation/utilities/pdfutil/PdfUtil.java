package com.taf.testautomation.utilities.pdfutil;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.taf.testautomation.utilities.excelutil.ExcelUtil.getCustomProperties;

public class PdfUtil extends PdfPageEventHelper {

    public void getPdfFromImageList(List<String> props, List<String> image, File pdfFile) {
        try {
            int count = 1;
            int lineCount = 30;
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            addHeaderFooter(document, writer, count);
            document.addAuthor("Koushik Das");
            document.addCreationDate();
            document.addCreator("Koushik Das");
            document.addTitle("Test ScreenShots");
            document.addSubject("Test ScreenShots");
            Paragraph pg = new Paragraph();
            pg.add("\n");
            pg.add("\n Test Name:      " + props.get(0));
            pg.add("\n =============================================================");
            pg.add("\n Time Zone:      " + props.get(1));
            pg.add("\n Report Date:      " + props.get(2));
            pg.add("\n Report Time:      " + props.get(3));
            pg.add("\n Device Name:      " + props.get(4));
            pg.add("\n Device OS:      " + props.get(5));
            pg.add("\n Phone Model:      " + props.get(6));
            pg.add("\n Device Mfg:      " + props.get(7));
            pg.add("\n OS Version:      " + props.get(8));
            pg.add("\n Device SN:      " + props.get(9));
            pg.add("\n Build Number:      " + props.get(10));
            IntStream.range(0, 33).forEach(i -> pg.add("\n"));
            pg.add("\n =============================================================");
            document.add(pg);
            for (String str : image) {
                document.add(new Paragraph(str.substring(str.lastIndexOf("/") + 1, str.indexOf("."))));
                Image image1 = Image.getInstance(str);
                image1.scaleAbsolute(200, 200);
                document.add(image1);
                Paragraph para = new Paragraph();
                count++;
                if ((count % 2) == 0) lineCount = 33;
                else lineCount = 28;
                IntStream.range(0, lineCount).forEach(i -> para.add("\n"));
                para.add("\n =============================================================");
                addHeaderFooter(document, writer, count);
                document.add(para);
            }
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addHeaderFooter(Document doc, PdfWriter writer, int count) {
        doc.setPageCount(count);
        onStartPage(doc, writer);
        onEndPage(doc, writer);
    }

    private void onStartPage(Document doc, PdfWriter writer) {
        int numberOfPages = doc.getPageNumber();
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + numberOfPages), 550, 800, 0);
    }

    private void onEndPage(Document doc, PdfWriter writer) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("www.xxx.com"), 550, 30, 0);
    }

    public static void getPdfFromImage(String image, String pdfFile) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
            document.addAuthor("Koushik Das");
            document.addCreationDate();
            document.addCreator("Koushik Das");
            document.addTitle("Test ScreenShots");
            document.addSubject("Test ScreenShots");
            document.add(new Paragraph("Screenshots"));
            Image image1 = Image.getInstance(image);
            image1.scaleAbsolute(200, 200);
            document.add(image1);
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mergePdf(File pdfFile) {
        try {
            String folder = getCustomProperties().get("reportPrefix") + "test-result/pdfreport";
            List<String> inputPdfList = new ArrayList<>();
            Files.newDirectoryStream(Paths.get(folder),
                    path -> path.toString().endsWith(".pdf")).forEach(filePath -> inputPdfList.add(filePath.toString()));

            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(pdfFile));
            document.open();

            for (String file : inputPdfList) {
                if (!file.equals(getCustomProperties().get("mergedReport"))) {
                    File pdf = new File(file);
                    URI uri = pdf.toURI();
                    URL url = uri.toURL();
                    PdfReader reader = new PdfReader(url);
                    copy.addDocument(reader);
                    copy.freeReader(reader);
                    reader.close();
                }
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPdfFromHtml(String htmlSource, String pdfFile) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
