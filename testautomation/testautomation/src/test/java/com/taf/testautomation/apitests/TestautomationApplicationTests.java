package com.taf.testautomation.apitests;

import com.google.gson.JsonElement;
import com.taf.testautomation.TestAutomationProperties;
import com.taf.testautomation.TestautomationApplication;
import com.taf.testautomation.services.RestServices;
import com.taf.testautomation.testmanagement.alm.application.AlmWorkflowUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestautomationApplication.class})
class TestautomationApplicationTests {
    AlmWorkflowUtil almWorkflowUtil = new AlmWorkflowUtil();

    @Autowired
    TestAutomationProperties testAutomationProperties;

    @Test
    public void testGetServiceCall() throws Exception {
        JsonElement response = RestServices.getJson(testAutomationProperties.getUrl(), "", "", "sample-service.json");
        log.info(String.valueOf(response));

        if (response.isJsonNull()) {
            Assertions.fail();
        }

        /**
         * Login to HP ALM with username and password
         */
        almWorkflowUtil.almLogin(testAutomationProperties.getAlmUsername(), testAutomationProperties.getAlmPassword());

        /**
         * Get the defect with id x.
         */
        almWorkflowUtil.getAlmDefect(testAutomationProperties.getDefectID());

        /**
         * Get the testrun with id y.
         */
        almWorkflowUtil.getAlmTestRun(testAutomationProperties.getTestRunID());

        /**
         * Create a new testrun in testlab from existing testset
         */
		almWorkflowUtil.createAlmTestRun();

        /**
         * Update the testrun in testlab
         */
        almWorkflowUtil.updateAlmTestRun(testAutomationProperties.getUpdateURL(), testAutomationProperties.getUpdateKeyPass(), testAutomationProperties.getUpdateValuePass());
        almWorkflowUtil.updateAlmTestRunWithAttachment(testAutomationProperties.getUpdateURL(), testAutomationProperties.getPdfFilePath(), testAutomationProperties.getPdfContentType());

        /**
         * Delete a testrun in testlab in delete URL
         */
        almWorkflowUtil.deleteAlmTestRun();

        /**
         * Logout of HP ALM
         */
        almWorkflowUtil.almLogout();
    }
}
