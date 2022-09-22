package com.taf.testautomation.devicemanagement;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.taf.testautomation.utilities.excelutil.ExcelUtil.getCustomProperties;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static java.time.Duration.ofMillis;

@Slf4j
public class AppiumUtil {

    private AppiumDriver mobileDriver;
    private Map<String, Object> targetMap;
    private String hour = "", minute = "", targetYear = "", targetMonth = "", targetDate = "", XPATH_IOS = "";
    private String XPATH_YEAR_PICKER_CURRENT = "", XPATH_YEAR_PICKER_TARGET = "", XPATH_DATE = "", XPATH_HOUR = "", XPATH_MINUTE = "";
    private static int i = 0;
    private static final String XPATH_LOCALE_DEFAULT = "//*[contains(@text,'Use locale default')]";
    private static final String XPATH_24HOUR_FORMAT = "//*[contains(@text,'Use 24-hour format')]";
    private static final String XPATH_NETWORK_TIME_TOGGLE = "//*[contains(@text,'Use network-provided time')]";
    private static final String XPATH_TIME_SET = "//*[contains(@text,'Time')]";
    private static final String XPATH_DATE_SET = "//*[@text='Date']";
    private static final String XPATH_YEAR = "//*[@resource-id='android:id/date_picker_header_year']";
    private static final String XPATH_PREV_MONTH = "//*[@content-desc='Previous month']";
    private static final String XPATH_CLOCK_OK = "//*[contains(@resource-id,'android:id/button1')]";
    private static final String XPATH_LANGUAGE_CURRENT = "//*[contains(@content-desc,'1,')]/android.widget.ImageView";
    private static final String XPATH_LANGUAGE_ENG = "//*[contains(@content-desc,'English') or contains(@content-desc,'Englisch') or contains(@content-desc,'Inglés')]/android.widget.ImageView";
    private static final String XPATH_LANGUAGE_DE = "//*[contains(@content-desc,'German') or contains(@content-desc,'Deutsch') or contains(@content-desc,'Alemán')]/android.widget.ImageView";
    private static final String XPATH_LANGUAGE_ES = "//*[contains(@content-desc,'Spanish') or contains(@content-desc,'Spanisch') or contains(@content-desc,'Español')]/android.widget.ImageView";
    private static final String XPATH_WIFI_ICON_IOS = "//XCUIElementTypeStaticText[@name='Wi-Fi']";
    private static final String XPATH_WIFI_TOGGLE_IOS = "//XCUIElementTypeSwitch[@name='Wi-Fi']";
    private static final String XPATH_SETTINGS_ICON_IOS = "//XCUIElementTypeButton[@name='Settings']";
    private static final String XPATH_GENERAL_MENU_IOS = "//XCUIElementTypeStaticText[@name='General']";
    private static final String XPATH_NOTIFICATIONS_MENU_IOS = "//XCUIElementTypeStaticText[@name='Notifications']";
    private static final String XPATH_ALLOW_NOTIFICATIONS_TOGGLE_IOS = "//XCUIElementTypeSwitch[@name='Allow Notifications']";
    private static final String XPATH_TIME_FORMAT_TOGGLE_IOS = "//XCUIElementTypeSwitch[@name='24-Hour Time']";
    private static final String XPATH_AUTOMATIC_TIME_TOGGLE_IOS = "//XCUIElementTypeSwitch[@name='Set Automatically']";
    private static final String XPATH_ABOUT_MENU_IOS = "//XCUIElementTypeStaticText[@name='About']";
    private static final String XPATH_HOME_MENU_IOS = "//XCUIElementTypeStaticText[@name='Home']";
    private static final String XPATH_DATE_TIME_MENU_IOS = "//XCUIElementTypeStaticText[@name='Date & Time']";
    private static final String XPATH_BACK_TO_GENERAL_IOS = "//XCUIElementTypeButton[@name='General']";
    private static final String XPATH_BACK_TO_NOTIFICATIONS_IOS = "//XCUIElementTypeButton[@name='Notifications']";
    private static final String XPATH_BACK_TO_SETTINGS_IOS = "//XCUIElementTypeButton[@name='Settings']";
    private static final String XPATH_DATE_PICKER_WHEEL_IOS = "//XCUIElementTypePickerWheel[@value='Today']";
    private static final String XPATH_HOUR_PICKER_WHEEL_IOS = "//XCUIElementTypePickerWheel[contains(@value,'clock')]";
    private static final String XPATH_MINUTE_PICKER_WHEEL_IOS = "//XCUIElementTypePickerWheel[contains(@value,'minutes')]";
    private static final String XPATH_TIME_ZONE_CELL_IOS = "//XCUIElementTypeCell[@name='Time Zone']";
    private static final String XPATH_TIME_ZONE_RESULT_IOS = "//XCUIElementTypeStaticText[contains(@name,'U.S.A')]";
    private final String serialNumber = "Serial Number";
    private final String modelName = "Model Name";
    private final String deviceName = "Name";
    private final String timeZone = "Time Zone";

    public AppiumUtil(AppiumDriver<MobileElement> driver) {
        this.mobileDriver = driver;
        this.targetMap = new HashMap<String, Object>();
    }

    public void unlockDevice() {
        if (getCustomProperties().get("isAndroid").equals("true")) {
            List<String> enterPinArgs = Arrays.asList(
                    "text",
                    "6509"
            );
            targetMap = ImmutableMap.of(
                    "command", "input",
                    "args", enterPinArgs
            );
            mobileDriver.executeScript("mobile: shell", targetMap);

            List<String> unlockPhoneArgs = Arrays.asList(
                    "keyevent",
                    "66"
            );

            targetMap = ImmutableMap.of(
                    "command", "input",
                    "args", unlockPhoneArgs
            );
            mobileDriver.executeScript("mobile: shell", targetMap);
        } else {
            log.info("do nothing");
        }
    }

    public void toggleWifiOff() {
        if (getCustomProperties().get("isAndroid").equals("true")) {
            List<String> wifiOffArgs = Arrays.asList(
                    "broadcast",
                    "-a",
                    "io.appium.settings.wifi",
                    "--es",
                    "setstatus",
                    "disable"
            );

            targetMap = ImmutableMap.of(
                    "command", "am",
                    "args", wifiOffArgs
            );
            mobileDriver.executeScript("mobile: shell", targetMap);
        } else {
            String bundleId = getCustomProperties().get("iosBundleId");
            iosToggleWifi(bundleId);
        }
    }

    public void toggleWifiOn() {
        if (getCustomProperties().get("isAndroid").equals("true")) {
            List<String> wifiOnArgs = Arrays.asList(
                    "broadcast",
                    "-a",
                    "io.appium.settings.wifi",
                    "--es",
                    "setstatus",
                    "enable"
            );

            targetMap = ImmutableMap.of(
                    "command", "am",
                    "args", wifiOnArgs
            );
            mobileDriver.executeScript("mobile: shell", targetMap);
        } else {
            String bundleId = getCustomProperties().get("iosBundleId");
            iosToggleWifi(bundleId);
        }
    }

    private void iosToggleWifi(String bundleId) {
        mobileDriver.activateApp("com.apple.Preferences");
        MobileElement wifiIcon = (MobileElement) mobileDriver.findElementByXPath(XPATH_WIFI_ICON_IOS);
        Map<String, Object> tapWifiArgs = new HashMap<>();
        tapWifiArgs.put("element", wifiIcon.getId());
        tapWifiArgs.put("x", 2);
        tapWifiArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", tapWifiArgs);
        MobileElement wifiToggle = (MobileElement) mobileDriver.findElementByXPath(XPATH_WIFI_TOGGLE_IOS);
        Map<String, Object> tapWifiToggleArgs = new HashMap<>();
        tapWifiToggleArgs.put("element", wifiToggle.getId());
        tapWifiToggleArgs.put("x", 0);
        tapWifiToggleArgs.put("y", 0);
        mobileDriver.executeScript("mobile: tap", tapWifiToggleArgs);
        MobileElement settingsLink = (MobileElement) mobileDriver.findElementByXPath(XPATH_SETTINGS_ICON_IOS);
        Map<String, Object> settingsIconArgs = new HashMap<>();
        settingsIconArgs.put("element", settingsLink.getId());
        settingsIconArgs.put("x", 2);
        settingsIconArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", settingsIconArgs);
        mobileDriver.executeScript("mobile: tap", settingsIconArgs);
        mobileDriver.activateApp(bundleId);
    }

    public void changeLanguageSettings(String deviceLanguage) {
        List<String> languageArgs = Arrays.asList(
                "start",
                "-a",
                "android.settings.LOCALE_SETTINGS"
        );

        targetMap = ImmutableMap.of(
                "command", "am",
                "args", languageArgs
        );
        mobileDriver.executeScript("mobile: shell", targetMap);
        MobileElement currentLanguage = (MobileElement) mobileDriver.findElementByXPath(XPATH_LANGUAGE_CURRENT);
        MobileElement desiredLanguage = getDesiredLangElement(deviceLanguage);
        Dimension size = mobileDriver.manage().window().getSize();
        int width = (int) (size.getWidth() * 0.95);
        int start = desiredLanguage.getLocation().getY() + desiredLanguage.getSize().getHeight() / 2;
        int end = (int) (currentLanguage.getLocation().getY() + currentLanguage.getSize().getHeight() * 0.25);

        new TouchAction(mobileDriver).press(PointOption.point(width, start)).waitAction(waitOptions(ofMillis(1000))).moveTo(PointOption.point(width, end)).release().perform();
        mobileDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        mobileDriver.activateApp(getCustomProperties().get("appPackage"));
    }

    public void changeLanguageADB(String language, String country) {
        List<String> permissionArgs = Arrays.asList(
                "grant",
                "net.sanapeli.adbchangelanguage",
                "android.permission.CHANGE_CONFIGURATION"
        );

        targetMap = ImmutableMap.of(
                "command", "pm",
                "args", permissionArgs
        );
        mobileDriver.executeScript("mobile: shell", targetMap);

        List<String> changeLangArgs = Arrays.asList(
                "start",
                "-n",
                "net.sanapeli.adbchangelanguage/.AdbChangeLanguage",
                "-e",
                "language",
                language,
                "-e",
                "country",
                country
        );

        targetMap = ImmutableMap.of(
                "command", "am",
                "args", changeLangArgs
        );
        mobileDriver.executeScript("mobile: shell", targetMap);
        mobileDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    public void setDeviceDate(String date) {
        mobileDriver.runAppInBackground(Duration.ofMillis(10000));
        if (i == 0) {
            tapOn(XPATH_NETWORK_TIME_TOGGLE);
        }
        targetYear = date.substring(0, 4);
        targetMonth = date.substring(5, 7);
        targetDate = date.substring(8).replaceAll("^0+(?!$)", "");
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        tapOn(XPATH_DATE_SET);
        tapOn(XPATH_YEAR);
        setLocatorsDatePicker(String.valueOf(currentYear), targetYear, targetDate);
        MobileElement yearText = (MobileElement) mobileDriver.findElementByXPath(XPATH_YEAR_PICKER_CURRENT);
        int x4Point = yearText.getLocation().getX() + yearText.getSize().getHeight() / 2;
        int y4Point = yearText.getLocation().getY() + yearText.getSize().getHeight() / 2;
        boolean yearFound = false;
        while (!yearFound) {
            new TouchAction(mobileDriver).press(PointOption.point(x4Point, y4Point)).waitAction(waitOptions(ofMillis(500))).moveTo(PointOption.point(x4Point, y4Point + 200)).release().perform();
            try {
                tapOn(XPATH_YEAR_PICKER_TARGET);
                yearFound = true;
            } catch (Exception e) {
                e.getMessage();
                yearFound = false;
            }
        }
        MobileElement prevMonth = (MobileElement) mobileDriver.findElementByXPath(XPATH_PREV_MONTH);
        int x6Point = prevMonth.getLocation().getX() + prevMonth.getSize().getWidth() / 2;
        int y6Point = prevMonth.getLocation().getY() + prevMonth.getSize().getHeight() / 2;
        while (currentMonth > Integer.valueOf(targetMonth)) {
            new TouchAction(mobileDriver).press(PointOption.point(x6Point, y6Point)).waitAction(waitOptions(ofMillis(500))).release().perform();
            currentMonth--;
        }
        tapOn(XPATH_DATE);
        tapOn(XPATH_CLOCK_OK);
        mobileDriver.activateApp(getCustomProperties().get("appPackage"));
    }

    public void setDeviceTime(String time) {
        hour = time.substring(0, 2);
        minute = time.substring(3).equals("00") ? "0" : time.substring(3);
        setLocatorsHourMinute(hour, minute);
        tapOn(XPATH_NETWORK_TIME_TOGGLE);
        mobileDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        tapOn(XPATH_TIME_SET);
        tapOn(XPATH_HOUR);
        tapOn(XPATH_MINUTE);
        tapOn(XPATH_CLOCK_OK);
        i++;
        mobileDriver.activateApp(getCustomProperties().get("appPackage"));
    }

    public void setDeviceTimeZone(String timeZone) {
        switch (timeZone) {
            case "PST":
                timeZone = "America/Los_Angeles";
                break;
            case "EST":
                timeZone = "America/New_York";
                break;
            default:
                timeZone = "America/Chicago";
                break;
        }
        List<String> timeZoneArgs = Arrays.asList(
                "call",
                "alarm",
                "3",
                "s16",
                timeZone
        );

        targetMap = ImmutableMap.of(
                "command", "service",
                "args", timeZoneArgs
        );
        mobileDriver.executeScript("mobile: shell", targetMap);
    }

    public void setDeviceTimeFormat(String format) {
        if (format.equals("24")) {
            mobileDriver.runAppInBackground(Duration.ofMillis(10000));
            openAndroidDateTimeSettings();
            tapOn(XPATH_LOCALE_DEFAULT);
            mobileDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            tapOn(XPATH_24HOUR_FORMAT);
        } else {
            mobileDriver.runAppInBackground(Duration.ofMillis(10000));
            openAndroidDateTimeSettings();
            tapOn(XPATH_LOCALE_DEFAULT);
        }
    }

    public void IosSetDeviceDate(String bundleId, String targetDate) {
        openIosDateTimeSettings();
        MobileElement automaticTimeToggle = (MobileElement) mobileDriver.findElementByXPath(XPATH_AUTOMATIC_TIME_TOGGLE_IOS);
        Map<String, Object> automaticTimeArgs = new HashMap<>();
        automaticTimeArgs.put("element", automaticTimeToggle.getId());
        automaticTimeArgs.put("x", 2);
        automaticTimeArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", automaticTimeArgs);
        String currentDate = new java.text.SimpleDateFormat("MMM dd, yyyy").format(Calendar.getInstance().getTime());
        setLocatorStaticText(currentDate);
        MobileElement dateLink = (MobileElement) mobileDriver.findElementByXPath(XPATH_IOS);
        Map<String, Object> dateArgs = new HashMap<>();
        dateArgs.put("element", dateLink.getId());
        dateArgs.put("x", 2);
        dateArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", dateArgs);
        currentDate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        LocalDate dateBefore = LocalDate.parse(targetDate);
        LocalDate dateAfter = LocalDate.parse(currentDate);
        long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
        int days = (int) noOfDaysBetween;
        swipeOnWheel(XPATH_DATE_PICKER_WHEEL_IOS, days, "UP");
        mobileDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        MobileElement backToGeneral = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_GENERAL_IOS);
        backToGeneral.click();
        MobileElement backToSettings = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_SETTINGS_IOS);
        backToSettings.click();
        mobileDriver.activateApp(bundleId);
    }

    public void IosSetDeviceTime(String bundleId, String targetTime) {
        openIosDateTimeSettings();
        MobileElement automaticTimeToggle = (MobileElement) mobileDriver.findElementByXPath(XPATH_AUTOMATIC_TIME_TOGGLE_IOS);
        Map<String, Object> automaticTimeArgs = new HashMap<>();
        automaticTimeArgs.put("element", automaticTimeToggle.getId());
        automaticTimeArgs.put("x", 2);
        automaticTimeArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", automaticTimeArgs);
        String currentDate = new java.text.SimpleDateFormat("MMM dd, yyyy").format(Calendar.getInstance().getTime());
        setLocatorStaticText(currentDate);
        MobileElement dateLink = (MobileElement) mobileDriver.findElementByXPath(XPATH_IOS);
        Map<String, Object> dateArgs = new HashMap<>();
        dateArgs.put("element", dateLink.getId());
        dateArgs.put("x", 2);
        dateArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", dateArgs);
        String currentTime = new java.text.SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        int currentHour = Integer.parseInt(currentTime.substring(0, 2));
        int currentMinute = Integer.parseInt(currentTime.substring(3, 5).equals("00") ? "0" : currentTime.substring(3, 5));
        int targetHour = Integer.parseInt(targetTime.substring(0, 2));
        int targetMinute = Integer.parseInt(targetTime.substring(3).equals("00") ? "0" : targetTime.substring(3));
        int diffHour = Math.abs(currentHour - targetHour);
        if (currentHour > targetHour) {
            swipeOnWheel(XPATH_HOUR_PICKER_WHEEL_IOS, diffHour, "UP");
        } else {
            swipeOnWheel(XPATH_HOUR_PICKER_WHEEL_IOS, diffHour, "DOWN");
        }
        int diffMin = Math.abs(currentMinute - targetMinute);
        if (currentMinute > targetMinute) {
            swipeOnWheel(XPATH_MINUTE_PICKER_WHEEL_IOS, diffMin, "UP");
        } else {
            swipeOnWheel(XPATH_MINUTE_PICKER_WHEEL_IOS, diffMin, "DOWN");
        }
        mobileDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        MobileElement backToGeneral = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_GENERAL_IOS);
        backToGeneral.click();
        MobileElement backToSettings = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_SETTINGS_IOS);
        backToSettings.click();
        mobileDriver.activateApp(bundleId);
    }

    public void IosSetDeviceTimeZone(String bundleId, String targetTimeZone) {
        openIosDateTimeSettings();
        MobileElement automaticTimeToggle = (MobileElement) mobileDriver.findElementByXPath(XPATH_AUTOMATIC_TIME_TOGGLE_IOS);
        Map<String, Object> automaticTimeArgs = new HashMap<>();
        automaticTimeArgs.put("element", automaticTimeToggle.getId());
        automaticTimeArgs.put("x", 2);
        automaticTimeArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", automaticTimeArgs);
        MobileElement timeZoneCell = (MobileElement) mobileDriver.findElementByXPath(XPATH_TIME_ZONE_CELL_IOS);
        String timeZoneValue = timeZoneCell.getAttribute("value");
        setLocatorStaticText(timeZone);
        MobileElement tZ = (MobileElement) mobileDriver.findElementByXPath(XPATH_IOS);
        tZ.click();
        MobileElement timeZoneSearch = (MobileElement) mobileDriver.findElementByXPath("//XCUIElementTypeSearchField[@name='" + timeZoneValue + "']");
        timeZoneSearch.sendKeys(targetTimeZone);
        MobileElement timeZoneResult = (MobileElement) mobileDriver.findElementByXPath(XPATH_TIME_ZONE_RESULT_IOS);
        Map<String, Object> tZResultArgs = new HashMap<>();
        tZResultArgs.put("element", timeZoneResult.getId());
        tZResultArgs.put("x", 2);
        tZResultArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", tZResultArgs);
        mobileDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        MobileElement backToGeneral = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_GENERAL_IOS);
        backToGeneral.click();
        MobileElement backToSettings = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_SETTINGS_IOS);
        backToSettings.click();
        mobileDriver.activateApp(bundleId);
    }

    public void IosToggleAutomaticTimeZone(String bundleId) {
        openIosDateTimeSettings();
        MobileElement automaticTimeToggle = (MobileElement) mobileDriver.findElementByXPath(XPATH_AUTOMATIC_TIME_TOGGLE_IOS);
        Map<String, Object> automaticTimeArgs = new HashMap<>();
        automaticTimeArgs.put("element", automaticTimeToggle.getId());
        automaticTimeArgs.put("x", 2);
        automaticTimeArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", automaticTimeArgs);
        mobileDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        MobileElement backToGeneral = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_GENERAL_IOS);
        backToGeneral.click();
        MobileElement backToSettings = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_SETTINGS_IOS);
        backToSettings.click();
        mobileDriver.activateApp(bundleId);
    }

    public void IosSetDeviceTimeFormat(String bundleId) {
        openIosDateTimeSettings();
        MobileElement timeFormatToggle = (MobileElement) mobileDriver.findElementByXPath(XPATH_TIME_FORMAT_TOGGLE_IOS);
        Map<String, Object> timeFormatArgs = new HashMap<>();
        timeFormatArgs.put("element", timeFormatToggle.getId());
        timeFormatArgs.put("x", 2);
        timeFormatArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", timeFormatArgs);
        mobileDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        MobileElement backToGeneral = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_GENERAL_IOS);
        backToGeneral.click();
        MobileElement backToSettings = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_SETTINGS_IOS);
        backToSettings.click();
        mobileDriver.activateApp(bundleId);
    }

    public String getDeviceProperties(String devProp) {
        switch (devProp) {
            case "Name":
                if (getCustomProperties().get("isAndroid").equals("true")) {
                    devProp = getCustomProperties().get("deviceName");
                } else {
                    devProp = iosAboutMenu(getCustomProperties().get("iosBundleId"), deviceName);
                }
                break;
            case "Model":
                if (getCustomProperties().get("isAndroid").equals("true")) {
                    List<String> modelArgs = Arrays.asList(
                            "ro.product.model"
                    );

                    targetMap = ImmutableMap.of(
                            "command", "getprop",
                            "args", modelArgs
                    );
                    devProp = String.valueOf(mobileDriver.executeScript("mobile: shell", targetMap));
                } else {
                    devProp = iosAboutMenu(getCustomProperties().get("iosBundleId"), modelName);
                }
                break;
            case "OS":
                if (getCustomProperties().get("isAndroid").equals("true")) {
                    devProp = getCustomProperties().get("platformName");
                } else {
                    devProp = getCustomProperties().get("iosPlatformName");
                }
                break;
            case "Manufacturer":
                if (getCustomProperties().get("isAndroid").equals("true")) {
                    List<String> mfgArgs = Arrays.asList(
                            "ro.product.manufacturer"
                    );

                    targetMap = ImmutableMap.of(
                            "command", "getprop",
                            "args", mfgArgs
                    );
                    devProp = String.valueOf(mobileDriver.executeScript("mobile: shell", targetMap));
                } else {
                    devProp = "Apple";
                }
                break;
            case "Version":
                if (getCustomProperties().get("isAndroid").equals("true")) {
                    devProp = getCustomProperties().get("platformVersion");
                } else {
                    devProp = getCustomProperties().get("iosPlatformVersion");
                }
                break;
            case "Serial_Number":
                if (getCustomProperties().get("isAndroid").equals("true")) {
                    List<String> snArgs = Arrays.asList(
                            "ro.boot.serialno"
                    );

                    targetMap = ImmutableMap.of(
                            "command", "getprop",
                            "args", snArgs
                    );
                    devProp = String.valueOf(mobileDriver.executeScript("mobile: shell", targetMap));
                } else {
                    devProp = iosAboutMenu(getCustomProperties().get("iosBundleId"), serialNumber);
                }
                break;
            case "TimeZone":
                Calendar now = Calendar.getInstance();
                TimeZone timeZone = now.getTimeZone();
                devProp = timeZone.getDisplayName();
                break;
            case "ReportDate":
                devProp = new java.text.SimpleDateFormat("dd-MMM-yyyy").format(Calendar.getInstance().getTime());
                break;
            case "ReportTime":
                devProp = new java.text.SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
                break;
            default:
                if (getCustomProperties().get("isAndroid").equals("true")) {
                    devProp = "Google 3 XL";
                } else {
                    devProp = iosAboutMenu(getCustomProperties().get("iosBundleId"), modelName);
                }
                break;
        }
        return devProp;
    }

    private MobileElement getDesiredLangElement(String devLanguage) {
        MobileElement desiredLanguage;
        switch (devLanguage) {
            case "de-DE":
                desiredLanguage = (MobileElement) mobileDriver.findElementByXPath(XPATH_LANGUAGE_DE);
                break;
            case "en-US":
                desiredLanguage = (MobileElement) mobileDriver.findElementByXPath(XPATH_LANGUAGE_ENG);
                break;
            case "es-ES":
                desiredLanguage = (MobileElement) mobileDriver.findElementByXPath(XPATH_LANGUAGE_ES);
                break;
            default:
                desiredLanguage = (MobileElement) mobileDriver.findElementByXPath(XPATH_LANGUAGE_CURRENT);
                break;
        }
        return desiredLanguage;
    }

    private String iosAboutMenu(String bundleId, String locator) {
        mobileDriver.activateApp("com.apple.Preferences");
        MobileElement generalMenu = (MobileElement) mobileDriver.findElementByXPath(XPATH_GENERAL_MENU_IOS);
        Map<String, Object> generalMenuArgs = new HashMap<>();
        generalMenuArgs.put("element", generalMenu.getId());
        generalMenuArgs.put("x", 2);
        generalMenuArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", generalMenuArgs);
        MobileElement aboutMenu = (MobileElement) mobileDriver.findElementByXPath(XPATH_ABOUT_MENU_IOS);
        Map<String, Object> aboutMenuArgs = new HashMap<>();
        aboutMenuArgs.put("element", aboutMenu.getId());
        aboutMenuArgs.put("x", 2);
        aboutMenuArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", aboutMenuArgs);
        mobileDriver.activateApp(bundleId);
        mobileDriver.activateApp("com.apple.Preferences");
        setLocatorText(locator);
        MobileElement targetElement = (MobileElement) mobileDriver.findElementByXPath(XPATH_IOS);
        String target = targetElement.getAttribute("value");
        MobileElement backToGeneral = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_GENERAL_IOS);
        backToGeneral.click();
        MobileElement backToSettings = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_SETTINGS_IOS);
        backToSettings.click();
        mobileDriver.activateApp(bundleId);
        return target;
    }

    public void disableAppNotification(String appPackage) {
        if (getCustomProperties().get("isAndroid").equals("true")) {
            List<String> disableNotificationArgs = Arrays.asList(
                    "call",
                    "notification",
                    "10",
                    "s16",
                    appPackage,
                    "i32",
                    "11346",
                    "i32",
                    "0"
            );

            targetMap = ImmutableMap.of(
                    "command", "service",
                    "args", disableNotificationArgs
            );
            mobileDriver.executeScript("mobile: shell", targetMap);
        } else {
            String bundleId = getCustomProperties().get("iosBundleId");
            String appName = getCustomProperties().get("isAndroid").equals("true") ? "xxxx" : "yyyy";
            iOSToggleAppNotification(bundleId, appName);
        }
    }

    public void enableAppNotification(String appPackage) {
        if (getCustomProperties().get("isAndroid").equals("true")) {
            List<String> disableNotificationArgs = Arrays.asList(
                    "call",
                    "notification",
                    "10",
                    "s16",
                    appPackage,
                    "i32",
                    "11346",
                    "i32",
                    "1"
            );

            targetMap = ImmutableMap.of(
                    "command", "service",
                    "args", disableNotificationArgs
            );
            mobileDriver.executeScript("mobile: shell", targetMap);
        } else {
            String bundleId = getCustomProperties().get("iosBundleId");
            String appName = getCustomProperties().get("isAndroid").equals("true") ? "xxxx" : "yyyy";
            iOSToggleAppNotification(bundleId, appName);
        }
    }

    public void iOSToggleAppNotification(String bundleId, String appName) {
        mobileDriver.activateApp("com.apple.Preferences");
        MobileElement notificationsMenu = (MobileElement) mobileDriver.findElementByXPath(XPATH_NOTIFICATIONS_MENU_IOS);
        Map<String, Object> notificationsMenuArgs = new HashMap<>();
        notificationsMenuArgs.put("element", notificationsMenu.getId());
        notificationsMenuArgs.put("x", 2);
        notificationsMenuArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", notificationsMenuArgs);
        setLocatorText(appName);
        MobileElement homeText = (MobileElement) mobileDriver.findElementByXPath(XPATH_HOME_MENU_IOS);
        int x4Point = homeText.getLocation().getX() + homeText.getSize().getWidth() / 2;
        int y4Point = homeText.getLocation().getY() + homeText.getSize().getHeight() / 2;
        boolean appFound = false;
        while (!appFound) {
            new TouchAction(mobileDriver).press(PointOption.point(x4Point, y4Point)).waitAction(waitOptions(ofMillis(500))).moveTo(PointOption.point(x4Point, y4Point - 200)).release().perform();
            try {
                tapOn(XPATH_IOS);
                appFound = true;
            } catch (Exception e) {
                e.getMessage();
                appFound = false;
            }
        }
        MobileElement notificationToggle = (MobileElement) mobileDriver.findElementByXPath(XPATH_ALLOW_NOTIFICATIONS_TOGGLE_IOS);
        Map<String, Object> notificationToggleArgs = new HashMap<>();
        notificationToggleArgs.put("element", notificationToggle.getId());
        notificationToggleArgs.put("x", 2);
        notificationToggleArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", notificationToggleArgs);
        MobileElement backToNotifications = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_NOTIFICATIONS_IOS);
        backToNotifications.click();
        MobileElement backToSettings = (MobileElement) mobileDriver.findElementByXPath(XPATH_BACK_TO_SETTINGS_IOS);
        backToSettings.click();
        mobileDriver.activateApp(bundleId);
    }

    private void openAndroidDateTimeSettings() {
        List<String> dateTimeArgs = Arrays.asList(
                "start",
                "-n",
                "com.android.settings/.Settings\\$DateTimeSettingsActivity"
        );

        targetMap = ImmutableMap.of(
                "command", "am",
                "args", dateTimeArgs
        );
        mobileDriver.executeScript("mobile: shell", targetMap);
    }

    private void openIosDateTimeSettings() {
        mobileDriver.activateApp("com.apple.Preferences");
        MobileElement generalMenu = (MobileElement) mobileDriver.findElementByXPath(XPATH_GENERAL_MENU_IOS);
        Map<String, Object> generalMenuArgs = new HashMap<>();
        generalMenuArgs.put("element", generalMenu.getId());
        generalMenuArgs.put("x", 2);
        generalMenuArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", generalMenuArgs);
        MobileElement dateTimeMenu = (MobileElement) mobileDriver.findElementByXPath(XPATH_DATE_TIME_MENU_IOS);
        Map<String, Object> dateTimeMenuArgs = new HashMap<>();
        dateTimeMenuArgs.put("element", dateTimeMenu.getId());
        dateTimeMenuArgs.put("x", 2);
        dateTimeMenuArgs.put("y", 2);
        mobileDriver.executeScript("mobile: tap", dateTimeMenuArgs);
    }

    private void setLocatorsHourMinute(String hour, String minute) {
        XPATH_HOUR = "//android.widget.RadialTimePickerView.RadialPickerTouchHelper[@content-desc='" + hour + "']";
        XPATH_MINUTE = "//android.widget.RadialTimePickerView.RadialPickerTouchHelper[@content-desc='" + minute + "']";
    }

    private void setLocatorsDatePicker(String currentYear, String targetYear, String date) {
        XPATH_YEAR_PICKER_CURRENT = "//*[@text='" + currentYear + "' and @resource-id='android:id/text1']";
        XPATH_YEAR_PICKER_TARGET = "//*[@text='" + targetYear + "' and @resource-id='android:id/text1']";
        XPATH_DATE = "//*[@text='" + date + "']";
    }

    private void tapOn(String locator) {
        MobileElement element = (MobileElement) mobileDriver.findElementByXPath(locator);
        int xPoint = element.getLocation().getX() + element.getSize().getWidth() / 2;
        int yPoint = element.getLocation().getY() + element.getSize().getHeight() / 2;
        new TouchAction(mobileDriver).press(PointOption.point(xPoint, yPoint)).waitAction(waitOptions(ofMillis(500))).release().perform();
    }

    private void swipeOnWheel(String locator, int number, String direction) {
        MobileElement element = (MobileElement) mobileDriver.findElementByXPath(locator);
        int xPoint = element.getLocation().getX() + element.getSize().getWidth() / 2;
        int yPoint = element.getLocation().getY() + element.getSize().getHeight() / 2;
        if (direction.equals("UP")) {
            IntStream.range(0, number).forEach(i -> new TouchAction(mobileDriver).press(PointOption.point(xPoint, yPoint)).waitAction(waitOptions(ofMillis(500))).moveTo(PointOption.point(xPoint, yPoint + 40)).release().perform());
        } else {
            IntStream.range(0, number).forEach(i -> new TouchAction(mobileDriver).press(PointOption.point(xPoint, yPoint)).waitAction(waitOptions(ofMillis(500))).moveTo(PointOption.point(xPoint, yPoint - 40)).release().perform());
        }
    }

    private void setLocatorText(String text) {
        XPATH_IOS = "//XCUIElementTypeCell[@name='" + text + "']";
    }

    private void setLocatorStaticText(String text) {
        XPATH_IOS = "//XCUIElementTypeStaticText[@name='" + text + "']";
    }
}
