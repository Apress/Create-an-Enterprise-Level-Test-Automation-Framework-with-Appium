package com.taf.testautomation.testmanagement.alm.application;



import com.taf.testautomation.testmanagement.alm.infrastructure.Assert;
import com.taf.testautomation.testmanagement.alm.infrastructure.Constants;
import com.taf.testautomation.testmanagement.alm.infrastructure.Response;
import com.taf.testautomation.testmanagement.alm.infrastructure.RestConnector;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * This example shows how to work with attachments. While the other examples
 * show how to read/write/update/delete resources, usually entities,
 * this example shows all of those operations for files.
 */
public class Attachments {

    public static void main(String[] args) throws Exception {
        new Attachments().attachments(
                "http://" + Constants.HOST + ":" +
                        Constants.PORT + "/qcbin",
                Constants.DOMAIN,
                Constants.PROJECT,
                Constants.USERNAME,
                Constants.PASSWORD);
    }

    public void attachments(final String serverUrl, final String domain,
                                   final String project, String username, String password)
            throws Exception {

        RestConnector con =
                RestConnector.getInstance().init(
                        new HashMap<String, String>(),
                        serverUrl,
                        domain,
                        project);

        /*
          We use the login example to handle our login for this example.
          You can view that code to learn more on authentication.

          We use the write example to create an entity for us to attach files
          to. To learn more on creating entities view that code.

          We use the update example to lock the entity to which we want
          to attach files. This is an "updating" operation.
          View that code to learn more on updating.
        */
        AuthenticateLoginLogout login =
                new AuthenticateLoginLogout();
        CreateDelete writeExample = new CreateDelete();
        Update updater = new Update();
        Attachments example = new Attachments();

        boolean loginResponse = login.login(username, password);
        Assert.assertTrue("login failed", loginResponse);

        final String requirementsUrl =
                con.buildEntityCollectionUrl("requirement");

        final String createdEntityUrl =
                writeExample.createEntity(requirementsUrl,
                        Constants.entityToPostXml);

        // Before editing an entity, lock it is versioning is not enabled,
        // or check it out, if versioning is enabled.
        boolean isVersioned = Constants.isVersioned("requirement",
                domain, project);
        String preModificationXml = null;
        if (isVersioned) {

            // Note that we selected an entity that supports versioning
            // on a project that supports versioning. Would fail otherwise.
            String firstCheckoutComment = "check out comment1";
            preModificationXml =
                    updater.checkout(createdEntityUrl, firstCheckoutComment, -1);
            Assert.assertTrue(
                    "checkout comment missing",
                    preModificationXml.contains(Constants.generateFieldXml(
                            "vc-checkout-comments",
                            firstCheckoutComment)));
        }

        else {

            preModificationXml = updater.lock(createdEntityUrl);
        }

        Assert.assertTrue(
                "posted field value not found",
                preModificationXml.contains(Constants.entityToPostFieldXml));

        //The file names to use on the server side
        String multipartFileName = "multiPartFileName.txt";
        String octetStreamFileName = "octetStreamFileName.txt";

        // Attach the file data to entity
        String multipartFileDescription = "some random description";
        String octetstreamFileContent = "a completely different file";
        String multipartFileContent = "content of file";

        String newMultiPartAttachmentUrl =
                example.attachWithMultipart(
                        createdEntityUrl,
                        multipartFileContent.getBytes(),
                        "text/plain",
                        multipartFileName,
                        multipartFileDescription);

        String newOctetStreamAttachmentUrl =
                example.attachWithOctetStream(
                        createdEntityUrl,
                        octetstreamFileContent.getBytes(),
                        octetStreamFileName);

        // Changes aren't visible to other users until we check them
        //  in if versioned
        if (isVersioned) {
            String firstCheckinComment = "check in comment1";
            boolean checkin =
                    updater.checkin(createdEntityUrl, firstCheckinComment, false);
            Assert.assertTrue("checkin failed", checkin);
        }

        else {

            boolean unlock = updater.unlock(createdEntityUrl);
            Assert.assertTrue("unlock failed", unlock);
        }

        //read the data and it's metadata back from the server
        String readAttachments = example.readAttachments(createdEntityUrl);
        Assert.assertTrue(
                "multipart attachment description missing",
                readAttachments.contains(Constants.generateFieldXml(
                        "description",
                        multipartFileDescription)));

        Assert.assertTrue(
                "attachment count incorrect or missing",
                readAttachments.contains("<Entities TotalResults=\"2\">"));

        byte[] readAttachmentData =
                example.readAttachmentData(newOctetStreamAttachmentUrl);
        String readAttachmentsString = new String(readAttachmentData);
        Assert.assertEquals(
                "uploaded octet stream file content differs from read file content",
                readAttachmentsString,
                octetstreamFileContent);

        readAttachmentData =
                example.readAttachmentData(newMultiPartAttachmentUrl);
        readAttachmentsString = new String(readAttachmentData);
        Assert.assertEquals(
                "uploaded multipart stream file content differs from read file content",
                readAttachmentsString,
                multipartFileContent);

        String readAttachmentDetails =
                example.readAttachmentDetails(newMultiPartAttachmentUrl);
        Assert.assertTrue(
                "multipart attachment description missing",
                readAttachmentDetails.contains(Constants.generateFieldXml(
                        "description",
                        multipartFileDescription)));

        //again with the checkout checkin procedure
        if (isVersioned) {

            // Note that we selected an entity that supports versioning
            // on a project that supports versioning. Would fail otherwise.
            String firstCheckoutComment = "check out comment1";
            preModificationXml =
                    updater.checkout(createdEntityUrl, firstCheckoutComment, -1);
            Assert.assertTrue(
                    "checkout comment missing",
                    preModificationXml.contains(Constants.generateFieldXml(
                            "vc-checkout-comments",
                            firstCheckoutComment)));
        }

        else {

            preModificationXml = updater.lock(createdEntityUrl);
        }

        Assert.assertTrue(
                "posted field value not found",
                preModificationXml.contains(Constants.entityToPostFieldXml));

        //update data of file
        String updatedOctetStreamFileData = "updated file contents";
        String updatedOctetstreamFileDescription =
                "completely new description";

        example.updateAttachmentData(
                createdEntityUrl,
                updatedOctetStreamFileData.getBytes(),
                octetStreamFileName);

        readAttachmentsString =
                new String(example.readAttachmentData(newOctetStreamAttachmentUrl));
        Assert.assertEquals(
                "updated octet stream data not changed",
                updatedOctetStreamFileData,
                readAttachmentsString);

        //update description of file
        String attachmentMetadataUpdateResponseXml =
                example.updateAttachmentDescription(
                        createdEntityUrl,
                        updatedOctetstreamFileDescription,
                        octetStreamFileName);

        Assert.assertTrue(
                "updated octet stream description not changed",
                attachmentMetadataUpdateResponseXml.contains(updatedOctetstreamFileDescription));

        //checkin
        if (isVersioned) {
            final String firstCheckinComment = "check in comment1";
            boolean checkin =
                    updater.checkin(createdEntityUrl, firstCheckinComment, false);
            Assert.assertTrue("checkin failed", checkin);
        }

        else {
            boolean unlock = updater.unlock(createdEntityUrl);
            Assert.assertTrue("unlock failed", unlock);
        }

        //cleanup

        //check out attachment owner
        if (isVersioned) {
            updater.checkout(createdEntityUrl, "", -1);
        }

        else {
            updater.lock(createdEntityUrl);
        }

        //delete attachments
        writeExample.deleteEntity(newOctetStreamAttachmentUrl);
        writeExample.deleteEntity(newMultiPartAttachmentUrl);

        //checkin attachment owner
        if (isVersioned) {
            updater.checkin(createdEntityUrl, "", false);
        }

        else {
            updater.unlock(createdEntityUrl);
        }

        //delete attachment owner
        writeExample.deleteEntity(createdEntityUrl);
        login.logout();
    }

    /**
     * @param entityUrl
     *            the entity whose attachment we'd like to update
     * @param bytes
     *            the data to write instead of previously stored data
     * @param attachmentFileName
     *            the attachment we'd like to update file name on the server side.
     * @return
     */
    private String updateAttachmentData(String entityUrl, byte[] bytes,
                                        String attachmentFileName)
            throws Exception {

        Map<String, String> requestHeaders = new HashMap<String, String>();

        // This line makes the update be on data and not properties
        // such as description.
        requestHeaders.put("Content-Type", "application/octet-stream");

        requestHeaders.put("Accept", "application/xml");

        Response putResponse =
                con.httpPut(entityUrl + "/attachments/" + attachmentFileName,
                        bytes, requestHeaders);

        if (putResponse.getStatusCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception(putResponse.toString());
        }

        byte[] ret = putResponse.getResponseData();

        return new String(ret);
    }

    /**
     * @param entityUrl
     *            url of entity whose attachment's description we want to update
     * @param description
     *            string to store as description
     * @param attachmentFileName
     *            the attachment file name on the server-side whose
     *            description we'd like to update
     * @return
     */
    private String updateAttachmentDescription(
            String entityUrl,
            String description,
            String attachmentFileName) throws Exception {

        Map<String, String> requestHeaders = new HashMap<String, String>();

        // This line makes the update be on properties such as description
        // and not on the files binary data
        requestHeaders.put("Content-Type", "application/xml");

        requestHeaders.put("Accept", "application/xml");
        Response putResponse =
                con.httpPut(
                        entityUrl + "/attachments/" + attachmentFileName,
                        ("<Entity Type=\"attachment\"><Fields><Field Name=\"description\"><Value>"
                                + description + "</Value></Field></Fields></Entity>").getBytes(),
                        requestHeaders);

        if (putResponse.getStatusCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception(putResponse.toString());
        }

        byte[] ret = putResponse.getResponseData();

        return new String(ret);
    }

    RestConnector con;

    public Attachments() {
        con = RestConnector.getInstance();
    }

    /**
     * @param attachmentUrl
     *            of attachment
     * @return the xml of the metadata on the requested attachment
     */
    private String readAttachmentDetails(String attachmentUrl)
            throws Exception {

        Map<String, String> requestHeaders = new HashMap<String, String>();

        /* A get operation that specifies via accept header that
           we must have an application/xml reply.
           An alt query parameter could also have been used. */
        requestHeaders.put("Accept", "application/xml");

        Response readResponse =
                con.httpGet(attachmentUrl, null, requestHeaders);

        if (readResponse.getStatusCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception(readResponse.toString());
        }

        return readResponse.toString();
    }

    /**
     * @param attachmentUrl
     *            of attachment
     * @return the contents of the file
     */
    private byte[] readAttachmentData(String attachmentUrl) throws Exception {

        Map<String, String> requestHeaders = new HashMap<String, String>();

        /* A get operation that specifies via accept header that
           we must have an application/octet-stream reply.
           An alt query parameter could also have been used. */
        requestHeaders.put("Accept", "application/octet-stream");

        Response readResponse =
                con.httpGet(attachmentUrl, null, requestHeaders);

        if (readResponse.getStatusCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception(readResponse.toString());
        }

        return readResponse.getResponseData();
    }

    /**
     * @param entityUrl
     *            of the entity whose attachments we want to get
     * @return an xml with metadata on all attachmens of the entity
     * @throws Exception
     */
    private String readAttachments(String entityUrl) throws Exception {

        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("Accept", "application/xml");

        Response readResponse =
                con.httpGet(entityUrl + "/attachments", null, requestHeaders);

        if (readResponse.getStatusCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception(readResponse.toString());
        }

        return readResponse.toString();
    }

    /**
     * @param entityUrl
     *            url of entity to attach the file to
     * @param fileData
     *            content of file
     * @param filename
     *            to use on server side
     * @return
     */
    public String attachWithOctetStream(String entityUrl, byte[] fileData,
                                         String filename) throws Exception {

        Map<String, String> requestHeaders = new HashMap<String, String>();
        requestHeaders.put("Slug", filename);
        requestHeaders.put("Content-Type", "application/octet-stream");

        Response response =
                con.httpPost(entityUrl + "/attachments", fileData, requestHeaders);

        if (response.getStatusCode() != HttpURLConnection.HTTP_CREATED) {
            throw new Exception(response.toString());
        }

        return response.getResponseHeaders().get("Location").iterator().next();
    }

    /**
     * @param entityUrl
     *            url of entity to attach the file to
     * @param fileData
     *            content of file
     * @param contentType
     *            of the file - txt/html or xml, or octetstream etc..
     * @param filename
     *            to use on serverside
     * @return
     */
    public String attachWithMultipart(
            String entityUrl,
            byte[] fileData,
            String contentType,
            String filename,
            String description) throws Exception {
        /*

headers:
Content-Type: multipart/form-data; boundary=<boundary>

//template for file mime part:
--<boundary>\r\n
Content-Disposition: form-data; name="<fieldName>"; filename="<filename>"\r\n
Content-Type: <mime-type>\r\n
\r\n
<file-data>\r\n
<boundary>--

//template for post parameter mime part, such as description and/or filename:
--<boundary>\r\n
    Content-Disposition: form-data; name="<fieldName>"\r\n
    \r\n
    <value>\r\n
<boundary>--

//end of parts:
--<boundary>--

we need 3 parts:
filename(template for parameter), description(template for parameter),
and file data(template for file).

         */

        // This can be pretty much any string.
        // It's used to mark the different mime parts
        String boundary = "exampleboundary";

        //template to use when sending field data (assuming none-binary data)
        String fieldTemplate =
                "--%1$s\r\n"
                        + "Content-Disposition: form-data; name=\"%2$s\" \r\n\r\n"
                        + "%3$s"
                        + "\r\n";

        // Template to use when sending file data.
        // Binary data still needs to be suffixed.
        String fileDataPrefixTemplate =
                "--%1$s\r\n"
                        + "Content-Disposition: form-data; name=\"%2$s\"; filename=\"%3$s\"\r\n"
                        + "Content-Type: %4$s\r\n\r\n";

        String filenameData = String.format(fieldTemplate, boundary,
                "filename", filename);
        String descriptionData = String.format(fieldTemplate, boundary,
                "description", description);
        String fileDataSuffix = "\r\n--" + boundary + "--";
        String fileDataPrefix =
                String.format(fileDataPrefixTemplate, boundary, "file",
                        filename, contentType);

        // Note the order - extremely important:
        // Filename and description before file data.
        // Name of file in file part and filename part value MUST MATCH.
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bytes.write(filenameData.getBytes());
        bytes.write(descriptionData.getBytes());
        bytes.write(fileDataPrefix.getBytes());
        bytes.write(fileData);
        bytes.write(fileDataSuffix.getBytes());
        bytes.close();

        Map<String, String> requestHeaders = new HashMap<String, String>();

        requestHeaders.put("Content-Type", "multipart/form-data; boundary="
                + boundary);

        Response response =
                con.httpPost(entityUrl + "/attachments", bytes.toByteArray(),
                        requestHeaders);

        if (response.getStatusCode() != HttpURLConnection.HTTP_CREATED) {
            throw new Exception(response.toString());
        }

        return response.getResponseHeaders().get("Location").iterator().next();
    }

}
