package com.taf.testautomation.utilities.fileutil;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.taf.testautomation.utilities.excelutil.ExcelUtil.getCustomProperties;

@Slf4j
public class FileUtil {

    private LinkedHashMap<String, String> customSettings = new LinkedHashMap<>();
    private static final String INPUT_XML_BASE_PATH = "src/test/resources/testdata/xml/";
    private static final String INPUT_TXT_BASE_PATH = "src/test/resources/testdata/txt/";
    private static final String XML_FILENAME = "data.xml";
    private static final String DATA_FILENAME = "data.txt";
    private static FileOutputStream fos = null;

    public FileUtil() {
    }

    public Map<String, String> getCustomSettings() {
        String filePath = getInputFilePath();
        try {
            parseXML(filePath, customSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customSettings;
    }

    public String getCustomValues(String key) {
        String filePath = getInputFilePath();
        String value = "";
        try {
            value = parseFileLines(filePath, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public String getCustomValues(String language, String key) {
        String filePath = getInputFilePath(language);
        String value = "";
        try {
            value = parseFileLines(filePath, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private String getInputFilePath() {
        if (getCustomProperties().get("isAndroid").equals("true")) {
            return INPUT_XML_BASE_PATH + XML_FILENAME;
        } else {
            return INPUT_TXT_BASE_PATH + DATA_FILENAME;
        }
    }

    private String getInputFilePath(String language) {
        if (getCustomProperties().get("isAndroid").equals("true")) {
            return INPUT_XML_BASE_PATH + language + "/" + XML_FILENAME;
        } else {
            return INPUT_TXT_BASE_PATH + language + "/" + DATA_FILENAME;
        }
    }

    private void parseXML(String filePath, LinkedHashMap<String, String> customProperties) throws Exception {
        File fXmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        log.info("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("string");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            Element eElement = (Element) nNode;
            String key = eElement.getAttribute("name");
            String value = nNode.getTextContent();
            customProperties.put(key, value);
        }
    }

    private String parseFileLines(String filePath, String key) {
        String strLine = "";
        try {
            Stream<String> lines = Files.lines(Paths.get(filePath));
            strLine = lines.filter(s -> s.contains(key)).collect(Collectors.toList()).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (getCustomProperties().get("isAndroid").equals("true")) {
            return strLine.substring(strLine.indexOf(">") + 1, strLine.lastIndexOf("<"));
        } else {
            return strLine.substring(strLine.indexOf("=") + 3, strLine.indexOf(";") - 1);
        }
    }

    public static void fileCopy(String inputFile, String outputFile) {
        String cleanLine = "";

        try {
            fos = new FileOutputStream(outputFile);
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            String strLine = null;
            int linesCounter = 0;
            while ((strLine = br.readLine()) != null) {// read every line in the file
                cleanLine += strLine + "\n";
            }
            char[] stringToCharArray = cleanLine.toCharArray();
            for (char ch : stringToCharArray)
                fos.write(ch);
            fos.close();
        } catch (IOException e) {
        }
    }
}
