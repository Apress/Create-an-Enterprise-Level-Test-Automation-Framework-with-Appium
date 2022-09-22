package com.taf.testautomation.uitests;

import com.taf.testautomation.BaseTest;
import com.taf.testautomation.annotations.*;
import com.taf.testautomation.screens.AboutAppScreen;
import com.taf.testautomation.testmanagement.alm.application.AlmWorkflowUtil;
import com.taf.testautomation.utilities.excelutil.ExcelUtil;
import com.taf.testautomation.utilities.fileutil.FileUtil;
import com.taf.testautomation.utilities.jsonutil.JsonUtil;
import com.taf.testautomation.utilities.logutil.LogUtil;
import com.taf.testautomation.utilities.pdfutil.PdfUtil;
import com.taf.testautomation.workflows.ScreenNavigation;
import com.taf.testautomation.devicemanagement.AppiumUtil;
import io.qameta.allure.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Epic("xxxx")
@Feature("About App Page Layout")
public class AboutAppTestSuite extends BaseTest {

    private AboutAppScreen aboutAppScreen;
    private JsonUtil jsonUtil;
    private ExcelUtil excelUtil = new ExcelUtil();
    private AlmWorkflowUtil almWorkflowUtil = new AlmWorkflowUtil();
    private FileUtil fileUtil;
    private AppiumUtil appiumUtil;
    protected String testStatus = "";
    private static final String SCREEN_NAME = "aboutAppScreen";
    private static final String TEST_NAME = "AboutApp-Screen-Verification-";
    private static int i = 0, j = 0;
    private static List<String> imageList = new ArrayList<>();

    @BeforeAll
    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (getCustomProperties().get("loadExcel").equals("Y")) {
            excelUtil.generateJsonFilesFromExcel1();
        }
        jsonUtil = new JsonUtil();
        appiumUtil = new AppiumUtil(getSession().getAppiumDriver());
    }

    @BeforeEach
    public void navigateToScreen() {
        log("Navigating to " + SCREEN_NAME);
        if (testStatus.isEmpty()) {
            aboutAppScreen = new ScreenNavigation(session).getAboutAppScreenFromAccountCreationScreen(getCustomProperties().get("username"), getCustomProperties().get("password"));
        } else {
            log("User in About App screen already");
        }

    }

    @AfterAll
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("xxxx")
    @DisplayName("xxxx")
    @Description("xxxx: Verify that the AboutAppScreen Title is displayed")
    @Test
    @Order(1)
    @Smoke
    @Regression
    @SIT
    @AT
    public void testScenario1() {
        String tcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log("Test Name" + tcName);

        try {
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(aboutAppScreen.isScreenTitleDisplayed()).as("The Screen title is displayed").isTrue();
                    }
            );
        } finally {
            testStatus = aboutAppScreen.isScreenTitleDisplayed() ? "Passed" : "Failed";
            updateTCPassCount();
            try {
                aboutAppScreen.takeScreenShot("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageList.add("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("xxxx")
    @DisplayName("xxxx")
    @Description("xxxx: Verify that the App Logo is displayed")
    @Test
    @Order(2)
    @Smoke
    public void testScenario2() {
        String tcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log("Test Name" + tcName);

        LogUtil.takeDeviceLog();
        try {
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(aboutAppScreen.isAppLogoDisplayed()).as("The App Logo is displayed").isTrue();
                    }
            );
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(LogUtil.deviceLogCheck("xxxx")).as("Device log shows correct data").isTrue();
                    }
            );
        } finally {
            testStatus = aboutAppScreen.isAppLogoDisplayed() ? "Passed" : "Failed";
            updateTCPassCount();
            try {
                aboutAppScreen.takeScreenShot("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageList.add("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("xxxx")
    @DisplayName("xxxx")
    @Description("xxxx: Verify that the App Name is displayed")
    @Test
    @Order(3)
    @Regression
    public void testScenario3() {
        String tcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log("Test Name" + tcName);

        if (session.getAppiumDriver().removeApp(getCustomProperties().get("appPackage"))) {
            try {
                closeSession(sessionName);
                session.setPrevBuild("yes");
                session = startSession(sessionNames);
                sessionName = sessionManagement.getCurrentSessionName();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(aboutAppScreen.isAppNameDisplayed()).as("The App Name is displayed").isTrue();
                    }
            );
        } finally {
            testStatus = aboutAppScreen.isAppNameDisplayed() ? "Passed" : "Failed";
            updateTCPassCount();
            try {
                aboutAppScreen.takeScreenShot("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageList.add("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("xxxx")
    @DisplayName("xxxx")
    @Description("xxxx: Verify that the App Version is displayed")
    @Test
    @Order(4)
    @SIT
    public void testScenario4() {
        String tcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log("Test Name" + tcName);

        String app = getCustomProperties().get("app2");
        closeSession(sessionName);
        getCustomProperties().put("appPackage", "xxxx");
        getCustomProperties().put("app", app);
        try {
            session = startSession(sessionNames);
            sessionName = sessionManagement.getCurrentSessionName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(aboutAppScreen.isAppVersionDisplayed("xxxx")).as("The App Version is displayed").isTrue();
                    }
            );
        } finally {
            testStatus = aboutAppScreen.isAppVersionDisplayed("xxxx") ? "Passed" : "Failed";
            updateTCPassCount();
            try {
                aboutAppScreen.takeScreenShot("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageList.add("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("xxxx")
    @DisplayName("xxxx")
    @Description("xxxx: Verify that the App images are displayed")
    @Test
    @Order(5)
    @AT
    public void testScenario5() {
        String tcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log("Test Name" + tcName);

        try {
            SoftAssertions.assertSoftly(
                    softAssertions -> {
                        softAssertions.assertThat(aboutAppScreen.areAppImagesDisplayed()).as("The App images are displayed").isTrue();
                    }
            );
        } finally {
            testStatus = aboutAppScreen.areAppImagesDisplayed() ? "Passed" : "Failed";
            updateTCPassCount();
            try {
                aboutAppScreen.takeScreenShot("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageList.add("test-result/screenshots/" + tcName + "--" + testStatus + ".png");
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("xxxx")
    @DisplayName("xxxx")
    @Description("xxxx: Verify that the copyright text is displayed")
    @Test
    @Order(6)
    @Smoke
    @Regression
    @SIT
    @AT
    public void testScenario6() throws Exception {
        String tcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log("Test Name" + tcName);

        String copyrightTxt = "";
        String languageCodes = getCustomProperties().get("deviceLanguage");
        List<String> list =
                Stream.of(languageCodes.split("\\s*,\\s*")).collect(Collectors.toList());
        for (String str : list) {
            try {
                log("Language code is: " + str.substring(0, 2) + "\n" + "Country code is: " + str.substring(3));
                if (getCustomProperties().get("isAndroid").equals("true")) {
                    appiumUtil.changeLanguageADB(str.substring(0, 2), str.substring(3));
                } else {
                    closeSession(sessionName);
                    getCustomProperties().put("localization", "yes");
                    getCustomProperties().put("appLanguage", str);
                    session = startSession(sessionNames);
                    sessionName = sessionManagement.getCurrentSessionName();
                    aboutAppScreen = new ScreenNavigation(session).getAboutAppScreenFromAccountCreationScreen(getCustomProperties().get("username"), getCustomProperties().get("password"));
                }
                fileUtil = new FileUtil();
                copyrightTxt = fileUtil.getCustomValues(str, "copyrighttext");
                SoftAssertions.assertSoftly(
                        softAssertions -> {
                            softAssertions.assertThat(aboutAppScreen.isCopyrightTextDisplayed(fileUtil.getCustomValues(str, "copyrighttext"))).as("The Copyright is correctly displayed").isTrue();
                        }
                );
            } finally {
                testStatus = aboutAppScreen.isCopyrightTextDisplayed(copyrightTxt) ? "Passed" : "Failed";
                updateTCPassCount();
                try {
                    aboutAppScreen.takeScreenShot("test-result/screenshots/" + tcName + "-" + str + copyrightTxt + "--" + testStatus + ".png");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageList.add("test-result/screenshots/" + tcName + "-" + str + copyrightTxt + "--" + testStatus + ".png");
            }
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("xxxx")
    @DisplayName("xxxx")
    @Description("xxxx: Verify that the App description texts are displayed")
    @Test
    @Order(7)
    @Smoke
    @Regression
    @SIT
    @AT
    public void testScenario7() throws Exception {
        String tcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log("Test Name" + tcName);

        String appDescTxt1 = "", appDescTxt2 = "";
        String languageCodes = getCustomProperties().get("deviceLanguage");
        List<String> list =
                Stream.of(languageCodes.split("\\s*,\\s*")).collect(Collectors.toList());
        for (String str : list) {
            try {
                log("Language code is: " + str.substring(0, 2) + "\n" + "Country code is: " + str.substring(3));
                if (getCustomProperties().get("isAndroid").equals("true")) {
                    appiumUtil.changeLanguageADB(str.substring(0, 2), str.substring(3));
                } else {
                    closeSession(sessionName);
                    getCustomProperties().put("localization", "yes");
                    getCustomProperties().put("appLanguage", str);
                    session = startSession(sessionNames);
                    sessionName = sessionManagement.getCurrentSessionName();
                    aboutAppScreen = new ScreenNavigation(session).getAboutAppScreenFromAccountCreationScreen(getCustomProperties().get("username"), getCustomProperties().get("password"));
                }
                fileUtil = new FileUtil();
                appDescTxt1 = fileUtil.getCustomValues(str, "appdescstrng1");
                appDescTxt2 = fileUtil.getCustomValues(str, "appdescstrng2");
                SoftAssertions.assertSoftly(
                        softAssertions -> {
                            softAssertions.assertThat(aboutAppScreen.isAppDescriptionDisplayed(fileUtil.getCustomValues(str, "appdescstrng1"), fileUtil.getCustomValues(str, "appdescstrng2"))).as("The App description is correctly displayed").isTrue();
                        }
                );
            } finally {
                testStatus = aboutAppScreen.isAppDescriptionDisplayed(appDescTxt1, appDescTxt2) ? "Passed" : "Failed";
                updateTCPassCount();
                try {
                    aboutAppScreen.takeScreenShot("test-result/screenshots/" + tcName + "-" + str + appDescTxt1 + "##" + appDescTxt2 + "--" + testStatus + ".png");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imageList.add("test-result/screenshots/" + tcName + "-" + str + appDescTxt1 + "##" + appDescTxt2 + "--" + testStatus + ".png");
            }
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("xxxx")
    @DisplayName("xxxx")
    @Description("xxxx: Create Report and update HP ALM")
    @Test
    @Order(8)
    @Smoke
    @Regression
    @SIT
    @AT
    public void create_pdf_report_update_hpalm() throws Exception {
        String tcName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        log("Test Name" + tcName);

        appiumUtil = new AppiumUtil(getSession().getAppiumDriver());
        String pdfFile = getCustomProperties().get("reportPrefix") + "test-result/pdfreport/" + TEST_NAME + "Report.pdf";
        String testCase = TEST_NAME + "Screenshots:";
        String timeZone = appiumUtil.getDeviceProperties("TimeZone");
        String reportDate = appiumUtil.getDeviceProperties("ReportDate");
        String reportTime = appiumUtil.getDeviceProperties("ReportTime");
        String deviceName = appiumUtil.getDeviceProperties("Name");
        String deviceOS = appiumUtil.getDeviceProperties("OS");
        String deviceModel = appiumUtil.getDeviceProperties("Model");
        String deviceMfg = appiumUtil.getDeviceProperties("Manufacturer");
        String osVersion = appiumUtil.getDeviceProperties("Version");
        String deviceSN = appiumUtil.getDeviceProperties("Serial_Number");
        String buildNumber = getCustomProperties().get("build");
        Stream<String> propStream = Stream.of(testCase, timeZone, reportDate, reportTime, deviceName, deviceOS, deviceModel, deviceMfg, osVersion, deviceSN, buildNumber);
        List<String> propList = propStream.collect(Collectors.toList());

        String update = j + " of " + i + " Passed";
        new PdfUtil().getPdfFromImageList(propList, imageList, new File(pdfFile));
        PdfUtil.mergePdf(new File(getCustomProperties().get("mergedReport")));
        updateALMEntity(update);
    }

    private void updateTCPassCount() {
        i++;
        if (testStatus.equals("Passed")) j++;
    }

    private void updateALMEntity(String tcStatus) throws Exception {
        almWorkflowUtil.almLogin(getCustomProperties().get("almUsername"), getCustomProperties().get("almPassword"));
        switch (testStatus) {
            case "Passed":
                almWorkflowUtil.updateAlmTestRun(tcStatus, getCustomProperties().get("updateKeyPass"), getCustomProperties().get("updateValuePass"));
                almWorkflowUtil.updateAlmTestRunWithAttachment(tcStatus, getCustomProperties().get("pdfFilePath"), getCustomProperties().get("pdfContentType"));
                break;
            default:
                almWorkflowUtil.updateAlmTestRun(tcStatus, getCustomProperties().get("updateKeyFail"), getCustomProperties().get("updateValueFail"));
                break;
        }
        almWorkflowUtil.almLogout();
    }

}
