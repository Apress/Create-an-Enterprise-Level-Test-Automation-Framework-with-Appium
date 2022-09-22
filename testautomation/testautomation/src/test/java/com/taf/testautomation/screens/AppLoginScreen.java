package com.taf.testautomation.screens;

import com.taf.testautomation.Session;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.*;
import io.qameta.allure.Step;
import org.openqa.selenium.support.PageFactory;

public class AppLoginScreen extends MobileBaseActionScreen {

    public AppLoginScreen(Session session) {
        super(session);
        PageFactory.initElements(new AppiumFieldDecorator(session.getAppiumDriver()), this);
    }

    private String XPATH_ANDROID = "", XPATH_IOS = "";

    @AndroidFindBy(id = "xxxx")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement emailField;

    @AndroidFindBy(xpath = "//android.widget.EditText[@text='xxxx']")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement passwordField;

    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE,
            iOSXCUITAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
    @AndroidFindBy(id = "xxxx")
    @AndroidFindBy(xpath = "//android.widget.Button[contains(@resource-id,'xxxx')]")
    @iOSXCUITFindBy(accessibility = "xxxx")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='xxxx']")
    private MobileElement signInButton;

    /**
     * Get AboutAppScreen after login to App with new account
     *
     * @return {@link AboutAppScreen}
     */
    @Step("Sign-in to App with new account")
    public AboutAppScreen signInToApp() {
        if (getSession().getCustomProperties().get("isAndroid").equals("true")) {
            type(emailField, email);
            type(passwordField, password);
        } else {
            clickAndType(emailField, email);
            clickAndType(passwordField, password);
            getSession().getAppiumDriver().hideKeyboard();
        }
        click(signInButton);
        waitInSeconds(SMALL_WAIT);
        return new AboutAppScreen(getSession());
    }

    /**
     * Get AboutAppScreen after login to App with existing account from properties file
     *
     * @return {@link AboutAppScreen}
     */
    @Step("Sign-in to App with existing account")
    public AboutAppScreen signInToApp(String email, String password) {
        if (getSession().getCustomProperties().get("isAndroid").equals("true")) {
            type(emailField, email);
            type(passwordField, password);
        } else {
            clickAndType(emailField, email);
            clickAndType(passwordField, password);
            getSession().getAppiumDriver().hideKeyboard();
        }
        click(signInButton);
        waitInSeconds(SMALL_WAIT);
        return new AboutAppScreen(getSession());
    }
}
