package com.taf.testautomation.screens;

import com.taf.testautomation.Session;
import com.taf.testautomation.utilities.logutil.LogUtil;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.*;
import io.qameta.allure.Step;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AboutAppScreen extends MobileBaseActionScreen {

    public AboutAppScreen(Session session) {
        super(session);
        PageFactory.initElements(new AppiumFieldDecorator(session.getAppiumDriver()), this);
    }

    private String XPATH_ANDROID = "", XPATH_IOS = "";

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='xxxx']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='xxxx'][@label='xxxx']")
    private MobileElement screenTitle;

    @AndroidFindBy(accessibility = "xxxx")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement appVersion;

    @HowToUseLocators(androidAutomation = LocatorGroupStrategy.ALL_POSSIBLE,
            iOSXCUITAutomation = LocatorGroupStrategy.ALL_POSSIBLE)
    @AndroidFindBy(xpath = "//android.widget.TextView[@text='xxxx']")
    @AndroidFindBy(accessibility = "xxxx")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='xxxx']")
    @iOSXCUITFindBy(accessibility = "xxxx")
    private MobileElement appName;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='xxxx' or @text='xxxx']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[contains(@name,'xxxx') or contains(@value,'xxxx')]")
    private MobileElement copyrightText;

    @AndroidFindBy(xpath = "//android.widget.ImageView[@index='xxxx']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeImage[contains(@name,'xxxx')]")
    private MobileElement appLogo;

    @AndroidFindBy(xpath = "//android.widget.ImageView")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeImage")
    private List<MobileElement> appImages;

    /**
     * Validate if Screen Title is displayed
     *
     * @return True if Screen Title is displayed , otherwise returns
     * false.
     */
    @Step("Verifying the Screen Title is Displayed")
    public boolean isScreenTitleDisplayed() {
        return doesElementExist(screenTitle, SMALL_WAIT);
    }

    /**
     * Validate if App logo is displayed
     *
     * @return True if App logo is displayed , otherwise returns
     * false.
     */
    @Step("Verifying the App logo is Displayed")
    public boolean isAppLogoDisplayed() {
        fastScrollDownTouchAction();
        if (getSession().getCustomProperties().get("isAndroid").equals("true")) {
            return doesElementExist(appLogo, SMALL_WAIT);
        } else {
            return appLogo.isEnabled();
        }
    }

    /**
     * Validate if App Name is displayed
     *
     * @return True if App Name is displayed , otherwise returns
     * false.
     */
    @Step("Verifying the App Name is Displayed")
    public boolean isAppNameDisplayed() {
        fastScrollDownTouchAction();
        return doesElementExist(appName, SMALL_WAIT) && doesElementExist(appName, MIN_WAIT);
    }

    /**
     * Validate if App Version is displayed
     *
     * @return True if App Version is displayed , otherwise returns
     * false.
     */
    @Step("Verifying the App Version is Displayed")
    public boolean isAppVersionDisplayed(String text) {
        IntStream.range(0, 2).forEach(i -> scrollUpTouchAction());
        waitInSeconds(MIN_WAIT);
        LogUtil.logOutput("App Version label on Screen " + "is: " + appVersion.getText() + "\n");
        return doesElementExist(appVersion, SMALL_WAIT) && appVersion.getText().contains(text);
    }

    /**
     * Validate if Copyright text is displayed
     *
     * @return True if Copyright text is displayed , otherwise returns
     * false.
     */
    @Step("Verifying the Copyright text is Displayed")
    public boolean isCopyrightTextDisplayed(String str) {
        setLocatorText(str);
        if (getSession().getCustomProperties().get("isAndroid").equals("true")) {
            try {
                return doesElementExist(getSession().getAppiumDriver().findElementByXPath(XPATH_ANDROID), MIN_WAIT);
            } catch (NoSuchElementException e) {
                return false;
            }
        } else {
            try {
                fastScrollDownTouchAction();
                return doesElementExist(getSession().getAppiumDriver().findElementByXPath(XPATH_IOS), MIN_WAIT);
            } catch (NoSuchElementException e) {
                return false;
            }
        }
    }

    /**
     * Validate if App Description is displayed
     *
     * @return True if App Description is displayed , otherwise returns
     * false.
     */
    @Step("Verifying the App Description is Displayed")
    public boolean isAppDescriptionDisplayed(String str1, String str2) {
        boolean result = true;
        Stream<String> locatorTextStream = Stream.of(str1, str2);
        List<String> locatorTextList = locatorTextStream.collect(Collectors.toList());
        for (String str : locatorTextList) {
            setLocatorText(str);
            if (getSession().getCustomProperties().get("isAndroid").equals("true")) {
                result = result && doesElementExist(getSession().getAppiumDriver().findElementByXPath(XPATH_ANDROID), MIN_WAIT);
            } else {
                result = result && doesElementExist(getSession().getAppiumDriver().findElementByXPath(XPATH_IOS), MIN_WAIT);
            }
        }
        return result;
    }

    /**
     * Validate if app images are displayed
     *
     * @return True if app images are displayed , otherwise returns
     * false.
     */
    @Step("Verifying the app images are Displayed")
    public boolean areAppImagesDisplayed() {
        getScreenShot();
        return doesElementExist(appImages.get(0), MIN_WAIT) && doesElementExist(appImages.get(1), MIN_WAIT) && doesElementExist(appImages.get(2), MIN_WAIT);
    }

    private void setLocatorText(String text) {
        XPATH_ANDROID = "//android.widget.TextView[@text='" + text + "']";
        XPATH_IOS = "//XCUIElementTypeStaticText[@name='" + text + "']";
    }

    private void getScreenShot() {
        String name = new Object() {
        }.getClass().getName();
        name = name.substring(name.lastIndexOf('.') + 1, name.indexOf('$'));
        try {
            this.takeScreenShot("test-result/screenshots/" + name + ".png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
