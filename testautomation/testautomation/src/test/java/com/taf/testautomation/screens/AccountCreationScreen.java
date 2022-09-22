package com.taf.testautomation.screens;

import com.taf.testautomation.Session;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.*;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.support.PageFactory;

public class AccountCreationScreen extends MobileBaseActionScreen {

    public AccountCreationScreen(Session session) {
        super(session);
        PageFactory.initElements(new AppiumFieldDecorator(session.getAppiumDriver()), this);
    }

    private String XPATH_ANDROID = "", XPATH_IOS = "";

    @AndroidFindBy(xpath = "//android.widget.EditText[@text='xxxx']")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement firstNameField;

    @AndroidFindBy(xpath = "//android.widget.EditText[@text='xxxx']")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement lastNameField;

    @AndroidFindBy(xpath = "//android.widget.EditText[@text='xxxx']")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement emailIdField;

    @AndroidFindBy(xpath = "//android.widget.EditText[@text='xxxx']")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement passwordField;

    @AndroidFindBy(xpath = "//android.widget.EditText[@text='xxxx']")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement confirmPasswordField;

    @AndroidFindBy(xpath = "//android.widget.Button[@text='xxxx']")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement createAccountButton;

    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE,
            iOSXCUITAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
    @AndroidFindBy(accessibility = "xxxx")
    @AndroidFindBy(xpath = "//android.widget.Button[@text='xxxx']")
    @iOSXCUITFindBy(accessibility = "xxxx")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='xxxx']")
    private MobileElement okButton;

    /**
     * Get AppLoginScreen instance
     *
     * @return {@link AppLoginScreen}
     */
    @Step("Create account with firstNam, lastName, email and password")
    public AppLoginScreen createAccount() {
        firstName = RandomStringUtils.randomAlphanumeric(10);
        lastName = RandomStringUtils.randomAlphanumeric(10);
        email = RandomStringUtils.randomAlphanumeric(10) + "@gmail.com";
        password = RandomStringUtils.randomAlphanumeric(4) + RandomStringUtils.randomNumeric(2) + RandomStringUtils.random(2).toLowerCase() + RandomStringUtils.random(2).toUpperCase();
        if (getSession().getCustomProperties().get("isAndroid").equals("true")) {
            type(firstNameField, firstName);
            type(lastNameField, lastName);
            type(emailIdField, email);
            type(passwordField, password);
        } else {
            clickAndType(firstNameField, firstName);
            clickAndType(lastNameField, lastName);
            clickAndType(emailIdField, email);
            clickAndType(passwordField, password);
            getSession().getAppiumDriver().hideKeyboard();
        }
        click(createAccountButton);
        if (doesElementExist(okButton, MIN_WAIT)) {
            click(okButton);
        }
        waitInSeconds(SMALL_WAIT);
        return new AppLoginScreen(getSession());
    }
}
