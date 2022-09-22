package com.taf.testautomation;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Setter
@Slf4j
public class Session {

    private WebDriver webDriver;
    private AppiumDriver<MobileElement> appiumDriver;
    private AndroidDriver<MobileElement> androidDriver;
    private IOSDriver<MobileElement> iosDriver;
    private Boolean isMobile;
    private HashMap<String, String> customProperties = new HashMap<>();
    protected String prevBuild = "no";

    Reader reader;
    ClassLoader loader = this.getClass().getClassLoader();
    URL myURL = loader.getResource("uitest.properties");
    String path = myURL.getPath();

    {
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
    }

    public void startSession() throws Exception {
        if (customProperties.get("executionTarget").isEmpty()) {
            throw new RuntimeException("Session configuration error, execution target is empty");
        } else if (customProperties.get("executionTarget").equals("LOCAL")) {
            startLocalSession();
        } else if (customProperties.get("executionTarget").equals("GRID")) {
            startGridSession();
        } else if (customProperties.get("executionTarget").equals("EXPERITEST")) {
            startExperiTestSession();
        }
    }

    private void startLocalSession() throws IOException {
        StringBuilder urlString = new StringBuilder();
        urlString.append(customProperties.get("hubAddress"));
        URL url = new URL(urlString.toString());
        DesiredCapabilities desiredCapabilities = this.getDesiredCapabilities();
        this.startRemoteSession(url, desiredCapabilities);
    }

    private void startRemoteSession(URL url, final DesiredCapabilities desiredCapabilities) throws IOException {
        if (Boolean.parseBoolean(customProperties.get("isMobile"))) {
            if (Boolean.parseBoolean(customProperties.get("isAndroid"))) {
                if (customProperties.get("defaultService").equals("no")) {
                    this.androidDriver = new AndroidDriver(url, desiredCapabilities);
                    this.appiumDriver = this.androidDriver;
                } else {
                    Runtime.getRuntime().exec("./command.txt");
                    AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();
                    service.start();
                    this.androidDriver = new AndroidDriver(service, desiredCapabilities);
                    this.appiumDriver = this.androidDriver;
                }
            } else if (Boolean.parseBoolean(customProperties.get("isIos"))) {
                if (customProperties.get("iosDefaultService").equals("no")) {
                    this.iosDriver = new IOSDriver(url, desiredCapabilities);
                    this.appiumDriver = this.iosDriver;
                } else {
                    File testLogFile = new File("./log.txt");
                    Runtime.getRuntime().exec("./command.txt");
                    AppiumDriverLocalService service = new AppiumServiceBuilder().withLogFile(testLogFile).build();
                    service.start();
                    this.iosDriver = new IOSDriver(service, desiredCapabilities);
                    this.appiumDriver = this.iosDriver;
                }
                this.webDriver = this.appiumDriver;
            } else {
                RemoteWebDriver remoteWebDriver = new RemoteWebDriver(url, desiredCapabilities);
                this.webDriver = remoteWebDriver;
                this.webDriver.manage().window().maximize();
            }
        }
    }

    private void startGridSession() {

    }

    private void startExperiTestSession() throws IOException {
        StringBuilder urlString = new StringBuilder();
        urlString.append(customProperties.get("seeTestWdHub"));
        URL url = new URL(urlString.toString());
        DesiredCapabilities desiredCapabilities = this.getDesiredCapabilitiesSeeTest();
        this.startRemoteSession(url, desiredCapabilities);
    }

    public DesiredCapabilities getDesiredCapabilities() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        if (Boolean.parseBoolean(customProperties.get("isAndroid"))) {
            desiredCapabilities.setCapability("platformName", customProperties.get("platformName"));
            desiredCapabilities.setCapability("platformVersion", customProperties.get("platformVersion"));
            desiredCapabilities.setCapability("deviceName", customProperties.get("deviceName"));
            desiredCapabilities.setCapability("noReset", customProperties.get("noReset"));
            desiredCapabilities.setCapability("appPackage", customProperties.get("appPackage"));
            desiredCapabilities.setCapability("appActivity", customProperties.get("appActivity"));
            desiredCapabilities.setCapability("automationName", customProperties.get("automationName"));
            if (prevBuild.equals("yes")) {
                desiredCapabilities.setCapability("app", customProperties.get("appOld"));
            } else {
                desiredCapabilities.setCapability("app", customProperties.get("app"));
            }
        }
        if (Boolean.parseBoolean(customProperties.get("isIos"))) {
            if (customProperties.get("localization").equals("yes")) {
                System.out.println("localization value " + customProperties.get("localization"));
                System.out.println("language value " + customProperties.get("appLanguage"));
                desiredCapabilities.setCapability("platformName", customProperties.get("iosPlatformName"));
                desiredCapabilities.setCapability("platformVersion", customProperties.get("iosPlatformVersion"));
                desiredCapabilities.setCapability("deviceName", customProperties.get("iosDeviceName"));
                desiredCapabilities.setCapability("app", customProperties.get("iosApp"));
                desiredCapabilities.setCapability("noReset", customProperties.get("iosNoReset"));
                desiredCapabilities.setCapability("automationName", customProperties.get("iosAutomationName"));
                desiredCapabilities.setCapability("udid", customProperties.get("iosUdid"));
                desiredCapabilities.setCapability("xcodeOrgId", customProperties.get("iosXcodeOrgId"));
                desiredCapabilities.setCapability("xcodeSigningId", customProperties.get("iosXcodeSigningId"));
                desiredCapabilities.setCapability("language", customProperties.get("appLanguage"));
                desiredCapabilities.setCapability("locale", customProperties.get("appLanguage"));
            } else {
                desiredCapabilities.setCapability("platformName", customProperties.get("iosPlatformName"));
                desiredCapabilities.setCapability("platformVersion", customProperties.get("iosPlatformVersion"));
                desiredCapabilities.setCapability("deviceName", customProperties.get("iosDeviceName"));
                desiredCapabilities.setCapability("app", customProperties.get("iosApp"));
                desiredCapabilities.setCapability("noReset", customProperties.get("iosNoReset"));
                desiredCapabilities.setCapability("automationName", customProperties.get("iosAutomationName"));
                desiredCapabilities.setCapability("udid", customProperties.get("iosUdid"));
                desiredCapabilities.setCapability("xcodeOrgId", customProperties.get("iosXcodeOrgId"));
                desiredCapabilities.setCapability("xcodeSigningId", customProperties.get("iosXcodeSigningId"));
                desiredCapabilities.setCapability("showIOSLog", customProperties.get("showIOSLog"));
            }
        }
        if (customProperties.get("browserName").equals("chrome")) {
            ChromeOptions chromeOptions = getChromeOptions();
            desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }
        if (customProperties.get("browserName").equals("firefox")) {
            FirefoxOptions firefoxOptions = getFireFoxOptions();
            desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, firefoxOptions);
        }
        return desiredCapabilities;
    }

    public DesiredCapabilities getDesiredCapabilitiesSeeTest() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        if (Boolean.parseBoolean(customProperties.get("isAndroid"))) {
            desiredCapabilities.setCapability("accessKey", customProperties.get("accessKey"));
            desiredCapabilities.setCapability("testName", customProperties.get("testNameAndroid"));
            desiredCapabilities.setCapability("deviceQuery", customProperties.get("deviceQueryAndroid"));
            desiredCapabilities.setCapability(MobileCapabilityType.APP, customProperties.get("cloudAppAndroid"));
            desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, customProperties.get("appPackage"));
            desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, customProperties.get("appActivity"));
        }
        if (Boolean.parseBoolean(customProperties.get("isIos"))) {
            desiredCapabilities.setCapability("accessKey", customProperties.get("accessKey"));
            desiredCapabilities.setCapability("testName", customProperties.get("testNameIos"));
            desiredCapabilities.setCapability("deviceQuery", customProperties.get("deviceQueryIos"));
            desiredCapabilities.setCapability("appVersion", customProperties.get("appVersionIos"));
            desiredCapabilities.setCapability("platformName", customProperties.get("iosPlatformName"));
            desiredCapabilities.setCapability("newCommandTimeout", customProperties.get("newCommandTimeout"));
            desiredCapabilities.setCapability("automationName", customProperties.get("iosAutomationName"));
            desiredCapabilities.setCapability("app", customProperties.get("cloudAppIos"));
            desiredCapabilities.setCapability("bundleId", customProperties.get("iosBundleId"));
        }
        return desiredCapabilities;
    }

    public Boolean isMobile() {
        return isMobile;
    }

    private void setMobile(Boolean mobile) {
        isMobile = mobile;
    }

    public AppiumDriver<MobileElement> getAppiumDriver() {
        if (Boolean.parseBoolean(customProperties.get("isMobile"))) {
            return this.appiumDriver;
        }
        throw new IllegalArgumentException("Appium driver requested, but session is not configured to use an Appium driver");
    }

    public AndroidDriver<MobileElement> getAndroidDriver() {
        if (Boolean.parseBoolean(customProperties.get("isAndroid"))) {
            return this.androidDriver;
        }
        throw new IllegalArgumentException("Android driver requested, but session is not configured to use an Android driver");
    }

    public IOSDriver<MobileElement> getIosDriver() {
        if (Boolean.parseBoolean(customProperties.get("isIos"))) {
            return this.iosDriver;
        }
        throw new IllegalArgumentException("IOS driver requested, but session is not configured to use an iOS driver");
    }

    public WebDriver getWebDriver() {
        this.setMobile(Boolean.parseBoolean(customProperties.get("isMobile")));
        if (!isMobile()) {
            getWebDriver().manage().timeouts().pageLoadTimeout(30L, TimeUnit.SECONDS);
        }
        return this.webDriver;
    }

    protected ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        return chromeOptions;
    }

    protected FirefoxOptions getFireFoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        return firefoxOptions;
    }

    public HashMap<String, String> getCustomProperties() {
        return customProperties;
    }

    public void closeSession() {
        if (Boolean.parseBoolean(customProperties.get("isMobile"))) {
            try {
                if (customProperties.get("isAndroid").equals("true")) {
                    appiumDriver.resetApp();
                } else if (!appiumDriver.removeApp(customProperties.get("iosBundleId"))) {
                    appiumDriver.resetApp();
                }
            } catch (Exception e) {
                log.error("Error closing App");
                e.printStackTrace();
            }
            try {
                appiumDriver.quit();
            } catch (Exception e) {
                log.error("Error closing session");
                e.printStackTrace();
            }
        } else {
            getWebDriver().quit();
        }
    }
}

