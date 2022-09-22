package com.taf.testautomation.testmanagement.alm.infrastructure;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;

/**
 * These constants are used throughout the code to set the
 * server to work with.
 * To execute this code, change these settings to fit
 * those of your server.
 */
@Slf4j
public class Constants {
    private Constants() {
    }

    private static HashMap<String, String> customProperties = new HashMap<>();
    public static final String HOST = "https://adcqc.freestyleserver.com/qcbin";
    public static final String PORT = "443";

    public static final String USERNAME = "DASKX13";
    public static final String PASSWORD = "Kd###6509";

    public static final String DOMAIN = "ADC_TEST";
    public static final String PROJECT = "TestAutomation";

    /**
     * Populate customProperties
     * with the value from uitest.properties file
     *
     */
    private static Reader reader;
    public static HashMap<String, String> getCustomProperties() {
        ClassLoader loader = Constants.class.getClassLoader();
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

    /**
     * Supports running tests correctly on both versioned
     * and non-versioned projects.
     *
     * @return true if entities of entityType support versioning
     */
    public static boolean isVersioned(String entityType,
                                      final String domain, final String project)
            throws Exception {

        RestConnector con = RestConnector.getInstance();
        String descriptorUrl =
                con.buildUrl("rest/domains/"
                        + domain
                        + "/projects/"
                        + project
                        + "/customization/entities/"
                        + entityType);

        String descriptorXml =
                con.httpGet(descriptorUrl, null, null).toString();
        EntityDescriptor descriptor =
                EntityMarshallingUtils.marshal
                        (EntityDescriptor.class, descriptorXml);

        boolean isVersioned = descriptor.getSupportsVC().getValue();

        return isVersioned;
    }

    public static String generateFieldXml(String field, String value) {
        return "<Field Name=\"" + field
                + "\"><Value>" + value
                + "</Value></Field>";
    }

    /**
     * This string used to create new "requirement" type entities.
     */
    public static final String entityToPostName = "req"
            + Double.toHexString(Math.random());
    public static final String entityToPostFieldName =
            "type-id";
    public static final String entityToPostFieldValue = "1";
    public static final String entityToPostFormat =
            "<Entity Type=\"requirement\">"
                    + "<Fields>"
                    + Constants.generateFieldXml("%s", "%s")
                    + Constants.generateFieldXml("%s", "%s")
                    + "</Fields>"
                    + "</Entity>";

    public static final String entityToPostXml =
            String.format(
                    entityToPostFormat,
                    "name",
                    entityToPostName,
                    entityToPostFieldName,
                    entityToPostFieldValue);

    /**
     * This string used to create new "testrun" type entities.
     */
    public static final String entityToPostFormatTR =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                    +
                    "<Entity Type=\"run\">"
                    + "<ChildrenCount>"
                    + "<Value>" + 0 + "</Value>"
                    + "</ChildrenCount>"
                    + "<Fields>"
                    + Constants.generateFieldXml("name", getCustomProperties().get("name"))
                    + Constants.generateFieldXml("id", getCustomProperties().get("id"))
                    + Constants.generateFieldXml("test-id", getCustomProperties().get("test-id"))
                    + Constants.generateFieldXml("test-name", getCustomProperties().get("test-name"))
                    + Constants.generateFieldXml("has-linkage", getCustomProperties().get("has-linkage"))
                    + Constants.generateFieldXml("path", getCustomProperties().get("path"))
                    + Constants.generateFieldXml("cycle-id", getCustomProperties().get("cycle-id"))
                    + Constants.generateFieldXml("vc-version-number", getCustomProperties().get("vc-version-number"))
                    + Constants.generateFieldXml("draft", getCustomProperties().get("draft"))
                    + Constants.generateFieldXml("status", getCustomProperties().get("status"))
                    + Constants.generateFieldXml("owner", getCustomProperties().get("owner"))
                    + Constants.generateFieldXml("test-config-id", getCustomProperties().get("test-config-id"))
                    + Constants.generateFieldXml("ver-stamp", getCustomProperties().get("ver-stamp"))
                    + Constants.generateFieldXml("testcycl-name", getCustomProperties().get("testcycl-name"))
                    + Constants.generateFieldXml("testcycl-id", getCustomProperties().get("testcycl-id"))
                    + Constants.generateFieldXml("vc-locked-by", getCustomProperties().get("vc-locked-by"))
                    + Constants.generateFieldXml("vc-status", getCustomProperties().get("vc-status"))
                    + Constants.generateFieldXml("duration", getCustomProperties().get("duration"))
                    + Constants.generateFieldXml("attachment", getCustomProperties().get("attachment"))
                    + Constants.generateFieldXml("test-instance", getCustomProperties().get("test-instance"))
                    + Constants.generateFieldXml("cycle-name", getCustomProperties().get("cycle-name"))
                    + Constants.generateFieldXml("subtype-id", getCustomProperties().get("subtype-id"))
                    + Constants.generateFieldXml("execution-date", new java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Calendar.getInstance().getTime()))
                    + Constants.generateFieldXml("execution-time", new java.text.SimpleDateFormat("HH:mm:ss").format(java.util.Calendar.getInstance().getTime()))
                    + Constants.generateFieldXml("last-modified", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Calendar.getInstance().getTime()))
                    + Constants.generateFieldXml("user-01", getCustomProperties().get("user-01"))
                    + Constants.generateFieldXml("user-02", getCustomProperties().get("user-02"))
                    + "</Fields>"
                    + "<RelatedEntities/>"
                    + "</Entity>";

    public static final String entityToPostXmlTR = String.format(entityToPostFormatTR);

    public static final CharSequence entityToPostFieldXml =
            generateFieldXml(Constants.entityToPostFieldName,
                    Constants.entityToPostFieldValue);
}
