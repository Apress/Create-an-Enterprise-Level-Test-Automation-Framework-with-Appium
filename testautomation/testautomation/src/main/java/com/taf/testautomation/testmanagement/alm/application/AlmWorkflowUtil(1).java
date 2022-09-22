package com.taf.testautomation.testmanagement.alm.application;

import com.taf.testautomation.testmanagement.alm.infrastructure.*;
import com.taf.testautomation.utilities.emailutil.EmailUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.taf.testautomation.utilities.excelutil.ExcelUtil.getCustomProperties;

public class AlmWorkflowUtil {

    AuthenticateLoginLogout alm = new AuthenticateLoginLogout();
    RestConnector conn = RestConnector.getInstance();

    /**
     * Login to HP ALM
     */
    public void almLogin(String userName, String password) throws Exception {
        conn.init(new HashMap<String, String>(), Constants.HOST,
                Constants.DOMAIN, Constants.PROJECT);
        alm.login(userName, password);
    }

    /**
     * Get the defect with given id.
     */
    public void getAlmDefect(String defectID) throws Exception {
        String defectUrl = conn.buildEntityCollectionUrl("defect");
        defectUrl += "/" + defectID;

        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("Accept", "application/xml");

        Response res = conn.httpGet(defectUrl, null, requestHeaders);

        // xml -> class instance
        String postedEntityReturnedXml = res.toString();
        Entity entity = EntityMarshallingUtils.marshal(Entity.class,
                postedEntityReturnedXml);

        /*
         * Print all names available in entity defect to screen.
         */
        List<Entity.Fields.Field> fields = entity.getFields().getField();
        for (Entity.Fields.Field field : fields) {
            System.out.println(field.getName() + " : "
                    + field.getValue());
        }
    }

    /**
     * Get the testrun with given id.
     */
    public void getAlmTestRun(String testRunID) throws Exception {
        conn.getQCSession();
        String testRunUrl = conn.buildEntityCollectionUrl("run");
        testRunUrl += "/" + testRunID;
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Test Run URL is" + testRunUrl);

        Map<String, String> map = new HashMap<String, String>();
        map.put("Accept", "application/xml");

        Response resp = conn.httpGet(testRunUrl, null, map);
        System.out.println("\n");
        System.out.println("\n");
        System.out.println(resp.getStatusCode());
    }

    /**
     * Create a new testrun in testlab from existing testset
     */
    public void createAlmTestRun() throws Exception {
        conn.getQCSession();
        String requirementsUrl = conn.buildEntityCollectionUrl("run");

        CreateDelete createTestRun = new CreateDelete();
        String newCreatedResourceUrl =
                createTestRun.createEntity(requirementsUrl,
                        Constants.entityToPostXmlTR);
    }

    /**
     * Update the testrun in testlab
     */
    /**
     * Print all names available in entity testrun to screen.
     */
    public void updateAlmTestRun(String tcStatus, String updateKey, String updateValue) throws Exception {
        conn.getQCSession();
        String updateURL = getCustomProperties().get("updateURL");
        String id = "id";
        String idValue = "";
        // xml -> class instance
        Entity testRun = EntityMarshallingUtils.marshal(Entity.class,
                Constants.entityToPostXmlTR);
        List<Entity.Fields.Field> fieldsTR = testRun.getFields().getField();
        for (
                Entity.Fields.Field field : fieldsTR) {
            System.out.println(field.getName() + " : "
                    + field.getValue());
            if (field.getName().equals("id")) {
                String str = field.getValue().toString();
                idValue = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
            }
        }
        updateURL = updateURL + idValue;
        String name = updateKey;
        String nameUpdateValue = updateValue;
        System.out.println("String parameters are " + id + " " + idValue + " " + name + " " + nameUpdateValue);
        Update update = new Update();
        //create the update content
        String updatedEntityXml =
                update.generateSingleFieldUpdateXmlTR(name,
                        nameUpdateValue, id, idValue);

        //update the testrun
        String put = update.update(updateURL,
                updatedEntityXml);
    }

    /**
     * Update the testrun in testlab
     */
    /**
     * Print all names available in entity testrun to screen.
     */
    public void updateAlmTestRunWithAttachment(String tcStatus, String filePath, String contentType) throws Exception {
        String updateURL = getCustomProperties().get("updateURL");
        String id = "id";
        String idValue = "";
        // xml -> class instance
        Entity testRun = EntityMarshallingUtils.marshal(Entity.class,
                Constants.entityToPostXmlTR);
        List<Entity.Fields.Field> fieldsTR = testRun.getFields().getField();
        for (
                Entity.Fields.Field field : fieldsTR) {
            System.out.println(field.getName() + " : "
                    + field.getValue());
            if (field.getName().equals("id")) {
                String str = field.getValue().toString();
                idValue = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
            }
        }
        updateURL = updateURL + idValue;

        System.out.println("String parameters are " + id + idValue);

        //update the testrun
        String url = attachFile(updateURL, filePath, contentType);

        //Send emails to distribution list
        EmailUtil.sendEmailAttachment("Test run was updated with attachment after test execution - " + updateURL, tcStatus);
    }

    /**
     * Delete an existing testrun in testlab
     */
    public void deleteAlmTestRun() throws Exception {
        String deleteURL = getCustomProperties().get("deleteURL");
        String id = "id";
        String idValue = "";
        // xml -> class instance
        Entity testRun = EntityMarshallingUtils.marshal(Entity.class,
                Constants.entityToPostXmlTR);
        List<Entity.Fields.Field> fieldsTR = testRun.getFields().getField();
        for (
                Entity.Fields.Field field : fieldsTR) {
            System.out.println(field.getName() + " : "
                    + field.getValue());
            if (field.getName().equals("id")) {
                String str = field.getValue().toString();
                idValue = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
            }
        }
        deleteURL = deleteURL + idValue;

        CreateDelete deleteTestRun = new CreateDelete();
        String serverResponse =
                deleteTestRun.deleteEntity(deleteURL);
        System.out.println(serverResponse);
    }

    /**
     * Logout from HP ALM
     */
    public void almLogout() throws Exception {
        alm.logout();
    }

    private String attachFile(String updateUrl, String filePath, String contentType) throws Exception {
        File uploadFile = new File(filePath);
        String fileName = uploadFile.getName();
        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] byteSteam = new byte[inputStream.available()];
        inputStream.read(byteSteam);
        String multipartFileDescription = "File description";

        Attachments attachments = new Attachments();

        String newImageAttachmentUrl =
                attachments.attachWithMultipart(
                        updateUrl,
                        byteSteam,
                        contentType,
                        fileName,
                        multipartFileDescription);
        return newImageAttachmentUrl;
    }
}
