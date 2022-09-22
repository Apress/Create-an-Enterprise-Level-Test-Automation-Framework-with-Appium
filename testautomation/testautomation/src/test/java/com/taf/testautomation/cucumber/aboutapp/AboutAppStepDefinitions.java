package com.taf.testautomation.cucumber.aboutapp;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

public class AboutAppStepDefinitions extends SpringIntegration {

    @Given("application is installed and launched")
    public void application_is_installed_and_launched() throws Exception {
        setUp();
    }

    @When("user opens the application")
    public void user_opens_the_application() {
        navigateToScreen();
    }

    @Then("verify create account screen is displayed")
    public void verify_create_account_screen_is_displayed() {
        log("leaving the implementation to reader");
    }

    @When("user fills in details")
    public void user_fills_in_details() {
        log("this step is covered in navigateToScreen()");
    }

    @And("clicks on create account button")
    public void clicks_on_create_account_button() {
        log("this step is covered in navigateToScreen()");
    }

    @Then("verify user is taken to the app login screen")
    public void verify_user_is_taken_to_the_app_login_screen() {
        log("leaving the implementation to reader");
    }

    @When("user logs in with email and password")
    public void user_logs_in_with_email_and_password() {
        log("this step is covered in navigateToScreen()");
    }

    @And("clicks on sign in button")
    public void clicks_on_sign_in_button() {
        log("this step is covered in navigateToScreen()");
    }

    @Then("verify user is taken to the about app screen")
    public void verify_user_is_taken_to_the_about_app_screen() {
        log("leaving the implementation to reader");
    }

    @And("verify user sees the following in app screen")
    public void verify_user_sees_the_following_in_app_screen(DataTable dt) {
        List<String> list = dt.asList(String.class);
        dataTable = new String[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            dataTable[i][0] = list.get(i);
        }
        for (String str : list) {
            switch (str) {
                case "Screen_Title":
                    testScenario1();
                    break;
                case "App_Logo":
                    testScenario2();
                    break;
                case "App_Name":
                    testScenario3();
                    break;
                case "App_Version":
                    testScenario4();
                    break;
                case "App_Images":
                    testScenario5();
                    break;
                case "Copyright_Txt":
                    try {
                        testScenario6();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    try {
                        testScenario7();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    @Then("close application and update HP QC test run")
    public void close_application_and_update_HP_QC_test_run() throws Exception {
        create_pdf_report_update_hpalm();
        tearDown();
    }

}
