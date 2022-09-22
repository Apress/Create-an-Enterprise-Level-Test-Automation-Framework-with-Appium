package com.taf.testautomation.utilities.jsonutil;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Map;

import static com.taf.testautomation.utilities.excelutil.ExcelUtil.getCustomProperties;

@Slf4j
public class JsonUtil {

    private LinkedTreeMap<String, String> customSettings;
    private static final String JSON_LOCATION = "src/test/resources/testdata/json";
    private static final String JSON_POSTFIX = "TestData1.json";

    public JsonUtil() {
    }

    public Map<String, String> getCustomSettings() {
        String profileName = getProfileFileName(getCustomProperties().get("dataSource1"));
        loadJsonConfig(profileName);
        return customSettings;
    }

    public void loadJsonConfig(String profileName) {
        File resourcesDirectory = new File(JSON_LOCATION);
        File configurationFile = new File(resourcesDirectory, profileName);
        try {
            Reader reader = new FileReader(configurationFile);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.enableComplexMapKeySerialization().create();
            customSettings = gson.fromJson(reader, (Type) Map.class);
        } catch (Exception e) {
            JsonUtil.log.error("Error parsing configuration file {};", profileName, e);
            throw new RuntimeException(e);
        }
    }

    public String getProfileFileName(String name) {
        return name + JSON_POSTFIX;
    }

}
