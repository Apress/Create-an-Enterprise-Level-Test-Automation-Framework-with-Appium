package com.taf.testautomation.utilities.excelutil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

public class ExcelUtil {

    private Workbook dataTable;
    private static HashMap<String, String> customProperties = new HashMap<>();
    private static final String JSON_LOCATION = "src/test/resources/testdata/json/";
    private static final String JSON1_POSTFIX = "TestData1.json";
    private static final String JSON2_POSTFIX = "TestData2.json";

    /**
     * Populate customProperties map
     * with the value from uitest.properties file
     */
    private static Reader reader;

    public static HashMap<String, String> getCustomProperties() {
        ClassLoader loader = ExcelUtil.class.getClassLoader();
        URL myURL = loader.getResource("uitest.properties");
        String path = myURL.getPath();
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();
        try {
            prop.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop.forEach((k, v) -> customProperties.put(k.toString(), v.toString()));
        return customProperties;
    }

    public void generateJsonFilesFromExcel1() throws Exception {
        File excelFile = new File(getCustomProperties().get("dataTable1"));
        getJsonFromExcel1(excelFile);
    }

    public void generateJsonFilesFromExcel2() throws Exception {
        File excelFile = new File(getCustomProperties().get("dataTable2"));
        getJsonFromExcel2(excelFile);
    }

    public void getJsonFromExcel1(File excelFile) throws Exception {
        dataTable = new XSSFWorkbook(excelFile);
        int sheetNumber = dataTable.getNumberOfSheets() - 1;
        ArrayList<String> columnNames = new ArrayList<String>();
        Sheet sheet = dataTable.getSheetAt(sheetNumber);
        Iterator<Row> sheetIterator = sheet.iterator();

        while (sheetIterator.hasNext()) {
            Row currentRow = sheetIterator.next();
            if (currentRow.getRowNum() != 0) {
                JsonObject jsonObject = new JsonObject();

                for (int j = 0; j < columnNames.size(); j++) {
                    if (currentRow.getCell(j) != null) {
                        if (currentRow.getCell(j).getCellTypeEnum() == CellType.STRING) {
                            jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getStringCellValue());
                        } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.NUMERIC) {
                            jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getNumericCellValue());
                        } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.BOOLEAN) {
                            jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getBooleanCellValue());
                        } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.BLANK) {
                            jsonObject.addProperty(columnNames.get(j), "");
                        }
                    } else {
                        jsonObject.addProperty(columnNames.get(j), "");
                    }

                }
                saveJSON(currentRow.getCell(1).getStringCellValue().toLowerCase(), jsonObject);
            } else if (currentRow.getRowNum() == 0) {
                // store column names
                for (int k = 0; k < currentRow.getPhysicalNumberOfCells(); k++) {
                    columnNames.add(currentRow.getCell(k).getStringCellValue());
                }
            }
        }
    }

    public void getJsonFromExcel2(File excelFile) throws Exception {
        dataTable = new XSSFWorkbook(excelFile);
        int sheetNumber = dataTable.getNumberOfSheets() - 1;
        ArrayList<String> columnNames = new ArrayList<String>();
        Sheet sheet = dataTable.getSheetAt(sheetNumber);
        Iterator<Row> sheetIterator = sheet.iterator();

        while (sheetIterator.hasNext()) {
            Row currentRow = sheetIterator.next();
            if (currentRow.getRowNum() != 0) {
                JsonObject jsonObject = new JsonObject();

                for (int j = 0; j < columnNames.size(); j++) {
                    if (currentRow.getCell(j) != null) {
                        if (currentRow.getCell(j).getCellTypeEnum() == CellType.STRING) {
                            jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getStringCellValue());
                        } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.NUMERIC) {
                            jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getNumericCellValue());
                        } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.BOOLEAN) {
                            jsonObject.addProperty(columnNames.get(j), currentRow.getCell(j).getBooleanCellValue());
                        } else if (currentRow.getCell(j).getCellTypeEnum() == CellType.BLANK) {
                            jsonObject.addProperty(columnNames.get(j), "");
                        }
                    } else {
                        jsonObject.addProperty(columnNames.get(j), "");
                    }

                }
                saveJSON2(currentRow.getCell(0).getStringCellValue(), jsonObject);
            } else if (currentRow.getRowNum() == 0) {
                // store column names
                for (int k = 0; k < currentRow.getPhysicalNumberOfCells(); k++) {
                    columnNames.add(currentRow.getCell(k).getStringCellValue());
                }
            }
        }
    }

    public static void saveJSON(String fileName, JsonObject jsonObject) throws Exception {
        fileName = JSON_LOCATION + fileName + JSON1_POSTFIX;
        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            gson.toJson(jsonObject, writer);
        }
    }

    public static void saveJSON2(String fileName, JsonObject jsonObject) throws Exception {
        fileName = JSON_LOCATION + fileName + JSON2_POSTFIX;
        try (Writer writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
            gson.toJson(jsonObject, writer);
        }
    }

}
