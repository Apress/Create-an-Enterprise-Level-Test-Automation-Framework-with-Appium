package com.taf.testautomation.cucumber.aboutapp;

import com.taf.testautomation.utilities.emailutil.EmailUtil;
import com.taf.testautomation.utilities.fileutil.FileUtil;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

import java.io.File;

import static com.taf.testautomation.utilities.excelutil.ExcelUtil.getCustomProperties;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = {"com.taf.testautomation.cucumber.aboutapp"}, monochrome = true, plugin = {"html:build/cucumber-html-report-normal",
        "json:build/cucumber.json", "io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm", "com.taf.testautomation.cucumber.ExtentCucumberAdapter:"}, tags = {"@AboutApp"})
public class AboutAppTest {
    @AfterClass
    public static void sendExtentReport() {
        String folder = new Object() {
        }.getClass().getName();
        folder = folder.substring(folder.lastIndexOf('.') + 1, folder.indexOf('$'));
        EmailUtil.sendExtentReport("Extent Report is attached.", "Extent Report for last run");
        String htmlFilePath = getCustomProperties().get("reportPrefix") + "test-result/htmlreport/" + folder;
        String htmlFile = htmlFilePath + "/ExtentHtml.html";
        String folderExists = new File(htmlFilePath).mkdir() ? "Folder Created" : "Folder Exists";
        FileUtil.fileCopy("test-result/htmlreport/ExtentHtml.html", new File(htmlFile).getPath());
    }
}
