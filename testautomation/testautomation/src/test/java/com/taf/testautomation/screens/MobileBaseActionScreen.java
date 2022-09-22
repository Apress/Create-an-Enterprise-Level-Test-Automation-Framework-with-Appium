package com.taf.testautomation.screens;

import com.taf.testautomation.Direction;
import com.taf.testautomation.SwipeDirection;
import com.taf.testautomation.utilities.pdfutil.PdfUtil;
import io.appium.java_client.MobileDriver;
import com.taf.testautomation.Session;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static io.appium.java_client.MobileBy.AndroidUIAutomator;
import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@Slf4j
public class MobileBaseActionScreen {

    private Session session;
    public static final int MIN_WAIT = 2;
    public static final int SMALL_WAIT = 10;
    public static final int MED_WAIT = 30;
    public static final int LONG_WAIT = 60;
    public static final int MAX_WAIT = 120;
    public static final int MAX_SCROLL = 100;
    protected static String email;
    protected static String password;
    protected static String firstName;
    protected static String lastName;

    public MobileBaseActionScreen(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return this.session;
    }

    public SoftAssertions getSoftAssertions() {
        if (null != this.getSession()) {
            return new SoftAssertions();
        }
        throw new IllegalArgumentException("Please initialize session before attempting to access Soft Assertion");
    }

    /**
     * Checking that an element is present on the screen and visible.
     *
     * @param element
     * @param timeout
     * @return True if the element is visible otherwise return false.
     */
    protected boolean doesElementExist(MobileElement element, int timeout) {
        try {
            waitOn(getSession().getAppiumDriver(), timeout).until(visibilityOf(element));
        } catch (Exception toe) {
            return false;
        }
        return true;
    }

    /**
     * An implementation of the Wait interface with its timeout and
     * polling interval configured on the fly.
     *
     * @param <K>
     * @param object
     * @param timeOutSeconds
     * @return the fluent wait
     */
    protected <K> FluentWait<K> waitOn(K object, int timeOutSeconds) {
        return new FluentWait<>(object).ignoring(NoSuchElementException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(Exception.class)
                .withTimeout(Duration.ofSeconds(timeOutSeconds));
    }

    /**
     * Gets the waitOn instance.
     *
     * @return A FluentWait instance.
     */
    public FluentWait<MobileDriver<MobileElement>> getWait() {
        return waitOn(getSession().getAppiumDriver(), MAX_WAIT);
    }

    /**
     * Retrieves a TouchAction instance.
     *
     * @return a TouchAction instance.
     */
    protected TouchAction getTouchAction() {
        return new TouchAction(getSession().getAppiumDriver());

    }

    /**
     * This method performs touch action based
     * on the coordinates provided
     *
     * @param width
     * @param start
     * @param end
     */
    public void touchAction(int width, int start, int end) {
        getTouchAction().press(point(width, start))
                .waitAction(waitOptions(ofMillis(1000)))
                .moveTo(point(width, end))
                .release()
                .perform();
    }

    /**
     * Swipe action by touch between two points
     *
     * @param startx
     * @param starty
     * @param endx
     * @param endy
     * @param duration in milliseconds
     */
    protected void swipeTouchAction(int startx, int starty, int endx, int endy, int duration) {
        getTouchAction().press(PointOption.point(startx, starty))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
                .moveTo(PointOption.point(endx, endy))
                .release()
                .perform();
    }

    /**
     * Tap on.
     *
     * @param element the MobileElement
     */
    protected void tapOn(MobileElement element) {
        getTouchAction().tap(point(element.getLocation().getX(), element.getLocation().getY())).perform();
    }

    /**
     * Tap on element by co-ordinatess
     *
     * @param x co-ordinate,
     * @param y co-ordinate
     */
    protected void tapByCoordinates(int x, int y) {
        getTouchAction().tap(point(x, y)).perform();
    }

    /**
     * Wait in seconds.
     *
     * @param seconds
     */
    protected void waitInSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            log.error("Interrupted!", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Wait for displayed element.
     *
     * @param element
     * @param seconds
     */
    protected void waitForDisplayedElement(MobileElement element, int seconds) {
        waitOn(getSession().getAppiumDriver(), seconds).until(elementToBeClickable(element));
    }

    /**
     * Wait for a given element instance to be invisible.
     *
     * @param element
     * @param timeout
     */
    protected void waitForInvisibilityOf(MobileElement element, int timeout) {
        try {
            waitOn(getSession().getAppiumDriver(), timeout).until(invisibilityOf(element));
        } catch (Exception e) {
            log.info("The element is not displayed");
        }
    }

    /**
     * Get y coordinate of the center.
     *
     * @param element
     * @return value
     */
    public int getYCenter(MobileElement element) {
        int upperY = element.getLocation().getY();
        int lowerY = upperY + element.getSize().getHeight();
        return (upperY + lowerY) / 2;
    }

    /**
     * Get x coordinate of the center.
     *
     * @param element
     * @return the x center
     */
    public int getXCenter(MobileElement element) {
        int leftX = element.getLocation().getX();
        int rightX = leftX + element.getSize().getWidth();
        return (rightX + leftX) / 2;
    }

    /**
     * Get the X location of an element.
     *
     * @param element
     * @return X value of element
     */
    protected Integer getX(MobileElement element) {
        return element.getLocation().getX();
    }

    /**
     * Get the Y location of an element.
     *
     * @param element
     * @return Y value of element
     */
    protected Integer getY(MobileElement element) {
        return element.getLocation().getY();
    }

    /**
     * Get the width size of an element.
     *
     * @param element
     * @return width value of element
     */
    protected Integer getWidth(MobileElement element) {
        return element.getSize().getWidth();
    }

    /**
     * Get the Height size of an element.
     *
     * @param element
     * @return Height value of element
     */
    protected Integer getHeight(MobileElement element) {
        return element.getSize().getHeight();
    }

    /**
     * Get end position in x integer
     *
     * @param element
     * @return the integer
     */
    public Integer getEndPositionInX(MobileElement element) {
        return getX(element) + getWidth(element);
    }

    /**
     * Get end position in y integer
     *
     * @param element
     * @return the integer
     */
    public Integer getEndPositionInY(MobileElement element) {
        return getY(element) + getHeight(element);
    }

    /**
     * Compares Y axis position between two elements
     * Checks if element1 is below element2
     * @param element1
     * @param element2
     * @return True if first element is displayed below second element
     * else false
     */
    public boolean checkIfFirstElementIsDisplayedBelowSecondElement(MobileElement element1, MobileElement element2) {
        return element1.getLocation().getY() > element2.getLocation().getY();
    }

    /**
     * Compares X axis position between two elements
     * Checks if element2 is at right of element1
     * @param element1
     * @param element2
     * @return true, if element2 is at right side of element1
     */
    public boolean isElementRightSideOfOther(MobileElement element1, MobileElement element2) {
        return element1.getLocation().getX() < element2.getLocation().getX();
    }

    /**
     * Send app to background
     *
     * @param seconds
     */
    @Step("Send app to background")
    public void sendAppToBackground(int seconds) {
        log.info("Send app to background " + seconds + " seconds");
        getSession().getAppiumDriver().runAppInBackground(Duration.ofSeconds(seconds));
    }

    /**
     * Click on a given MobileElement element.
     *
     * @param element
     */
    protected void click(MobileElement element) {
        getWait().until(elementToBeClickable(element)).click();
    }

    /**
     * Use this method to simulate typing into an element, which may set its
     * value.
     *
     * @param element
     * @param text
     */
    public void type(MobileElement element, String text) {
        getWait().until(elementToBeClickable(element));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Use this method to simulate click and typing into an element, which may set its
     * value.
     *
     * @param element
     * @param text
     */
    public void clickAndType(MobileElement element, String text) {
        getWait().until(elementToBeClickable(element));
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Use this method to swipe horizontally
     *
     * @param startPoint the start X coordinate
     * @param endPoint   the end X coordinate
     * @param anchor     the Y coordinate
     */
    private void swipeAction(int startPoint, int endPoint, int anchor) {
        getTouchAction().press(point(startPoint, anchor))
                .waitAction(waitOptions(ofMillis(1000)))
                .moveTo(point(endPoint, anchor))
                .release()
                .perform();
    }

    /**
     * Higher level method for swipeActionDirectional
     *
     * @param direction the horizontal swipe direction
     */
    public void horizontalSwipe(SwipeDirection direction) {
        waitInSeconds(MIN_WAIT);
        Dimension size = getSession().getAppiumDriver().manage().window().getSize();
        int anchor = size.getHeight();
        int startPoint = size.getWidth();
        int endPoint = size.getWidth();

        swipeActionDirectional(direction, anchor, startPoint, endPoint, 0.50);
    }

    /**
     * Higher level method for swipeActionDirectional. Swipes end to end
     *
     * @param direction the horizontal swipe direction
     */
    public void horizontalSwipeAcrossWidth(SwipeDirection direction) {
        waitInSeconds(MIN_WAIT);
        Dimension size = getSession().getAppiumDriver().manage().window().getSize();
        int startPoint = 0, endPoint = 0;
        int anchor = size.getHeight();
        if (direction.name()
                .equalsIgnoreCase(SwipeDirection.RIGHT.direction())) {
            startPoint = size.getWidth();
            endPoint = size.getWidth();
        } else {
            startPoint = 1;
            endPoint = size.getWidth();
        }

        swipeActionDirectional(direction, anchor, startPoint, endPoint, 0.50);
    }

    /**
     * Horizontal Swipe with element Y position as anchor.
     *
     * @param element   the MobileElement
     * @param direction the horizontal swipe direction
     */
    public void horizontalSwipeOnContent(MobileElement element, SwipeDirection direction) {
        scrollFastToElement(element);
        int yCenter = getYCenter(element);
        int startPoint = 0;
        int endPoint = 0;
        Dimension size = getSession().getAppiumDriver().manage()
                .window()
                .getSize();
        if (direction.equals(SwipeDirection.RIGHT)) {
            startPoint = getXCenter(element);
            endPoint = (int) (size.getWidth() * 0.1);
        } else {
            startPoint = getXCenter(element);
            endPoint = (int) (size.getWidth() * 0.9);
        }

        swipeTouchAction(startPoint, yCenter, endPoint, yCenter, 500);
    }

    /**
     * This calls the swipeAction method for a given swipe direction
     *
     * @param direction  the horizontal swipe direction
     * @param percentage used to update the anchor
     */
    private void swipeActionDirectional(SwipeDirection direction, int anchor, int startPoint, int endPoint, double percentage) {
        if (direction.name()
                .equalsIgnoreCase(SwipeDirection.RIGHT.direction())) {
            anchor = (int) (anchor * percentage);
            startPoint = (int) (startPoint * 0.8);
            endPoint = (int) (endPoint * 0.2);
        } else {
            anchor = (int) (anchor * percentage);
            startPoint = (int) (startPoint * 0.2);
            endPoint = (int) (endPoint * 0.8);
        }
        log.info("Swiping " + direction.name() + "...");
        swipeAction(startPoint, endPoint, anchor);
    }

    /**
     * This method is used to scroll vertically.
     *
     * @param direction the vertical scroll direction
     */
    protected void scroll(Direction direction) {
        HashMap<String, String> scrollObject = new HashMap<String, String>();
        scrollObject.put("direction", direction.name().toLowerCase());
        getSession().getAppiumDriver().executeScript("mobile: scroll", new Object[]{scrollObject});
    }

    /**
     * This method calls scrollUpTouchAction for MAX_SCROLL.
     */
    public void scrollToTop() {
        int scrollCount = 1;
        while (scrollCount <= MAX_SCROLL) {
            scrollUpTouchAction();
            scrollCount++;
        }
    }

    /**
     * This method calls scrollUpTouchAction.
     *
     * @param count the initial count
     */
    public void scrollToTop(int count) {
        int scrollCount = count;
        while (scrollCount <= MAX_SCROLL) {
            scrollUpTouchAction();
            scrollCount++;
        }
    }

    /**
     * This method is used for scrolling up.
     */
    public void scrollUpTouchAction() {
        Dimension size = getSession().getAppiumDriver().manage().window().getSize();

        int width = size.getWidth() / 2;
        int start = (int) (size.getHeight() * 0.2);
        int end = (int) (size.getHeight() * 0.8);
        touchAction(width, start, end);
    }

    /**
     * This method is used for scrolling up, slowly.
     */
    public void slowScrollUpTouchAction() {
        Dimension size = getSession().getAppiumDriver().manage().window().getSize();
        int width = size.getWidth() / 2;
        int start = (int) (size.getHeight() * 0.6);
        int end = (int) (size.getHeight() * 0.8);
        touchAction(width, start, end);
    }

    /**
     * This method is used for scrolling down, faster.
     */
    public void fastScrollDownTouchAction() {
        Dimension size = getSession().getAppiumDriver().manage().window().getSize();
        int width = size.getWidth() / 2;
        int start = (int) (size.getHeight() * 0.8);
        int end = (int) (size.getHeight() * 0.2);
        touchAction(width, start, end);
    }

    /**
     * This method is used for scrolling down, slower.
     */
    public void slowScrollDownTouchAction() {
        Dimension size = getSession().getAppiumDriver().manage().window().getSize();
        int width = size.getWidth() / 2;
        int start = (int) (size.getHeight() * 0.8);
        int end = (int) (size.getHeight() * 0.6);
        touchAction(width, start, end);
    }

    /**
     * Scroll fast to bottom.
     *
     * @param element
     * @return the element
     */
    public MobileElement scrollFastToElement(MobileElement element) {

        for (int scroll = 0; scroll < MAX_SCROLL; scroll++) {
            try {
                if (element.isDisplayed())
                    return element;
                else
                    fastScrollDownTouchAction();
            } catch (Exception e) {
                fastScrollDownTouchAction();
            }
        }
        return null;
    }

    /**
     * Scroll fast to bottom.
     *
     * @param element
     */
    public void scrollToElement(MobileElement element) {

        for (int scroll = 0; scroll < MAX_SCROLL; scroll++) {
            try {
                if (doesElementExist(element, MIN_WAIT)) {
                    scroll = MAX_SCROLL;
                }
            } catch (Exception e) {
                fastScrollDownTouchAction();
            }
        }
    }

    /**
     * Scroll slow to bottom.
     *
     * @param element
     * @return the element
     */
    public MobileElement scrollSlowToElement(MobileElement element) {

        for (int scroll = 0; scroll < MAX_SCROLL; scroll++) {
            try {
                if (element.isDisplayed()) {
                    return element;
                } else {
                    slowScrollDownTouchAction();
                }
            } catch (NoSuchElementException e) {
                slowScrollDownTouchAction();
            }

        }
        return null;
    }

    /**
     * Scroll to a given text.
     *
     * @param text
     * @return An AndroidElement instance.
     */
    public MobileElement scrollToText(String text) {
        if (getSession().getCustomProperties().get("isAndroid").equals("true")) return scrollToTextAndroid(text);
        else if (getSession().getCustomProperties().get("isIos").equals("true")) return scrollToTextIos(text);
        return null;
    }

    /**
     * Scroll to a given text.
     *
     * @param text
     * @return An AndroidElement instance.
     */
    public MobileElement scrollToTextAndroid(String text) {
        String automator = "new UiScrollable(new UiSelector().scrollable(true))"
                + ".scrollIntoView(new UiSelector().text(\"%s\"))";
        return (AndroidElement) getWait().until(
                visibilityOfElementLocated(AndroidUIAutomator(format(automator, text))));
    }

    /**
     * Scroll to text.
     *
     * @param text
     * @return the element
     */
    public MobileElement scrollToTextIos(String text) {
        MobileElement element =
                getSession().getAppiumDriver().findElementByXPath(text);
        IntStream.range(0, MAX_SCROLL).forEach(i -> {
            if (!element.isDisplayed()) {
                scroll(Direction.DOWN);
            }
        });
        return element;
    }

    /**
     * Restart iOS/Android App.
     */

    public void restartApp() {
        if (getSession().getCustomProperties().get("isIos").equals("true"))
            restartAppIos();
        else if (getSession().getCustomProperties().get("isAndroid").equals("true"))
            restartAppAndroid();
        else
            log.error("Error: Wrong Platform!");
    }

    /**
     * Restart the iOS app.
     */
    private void restartAppIos() {
        getSession().getAppiumDriver().closeApp();
        getSession().getAppiumDriver().launchApp();
    }

    /**
     * Restart the Android app.
     */
    @Step("Restart the Android application.")
    private void restartAppAndroid() {
        if (getSession().getCustomProperties().get("defaultService").equals("yes")) {
            getSession().getAppiumDriver().launchApp();
            getSession().getAppiumDriver().activateApp(getSession().getCustomProperties().get("appPackage"));
        } else {
            try {
                getSession().getAppiumDriver().activateApp(getSession().getCustomProperties().get("appPackage"));
            } catch (Exception e) {
                AndroidDriver<MobileElement> androidDriver = (AndroidDriver<MobileElement>) getSession().getAppiumDriver();
                getSession().getAppiumDriver().closeApp();
                Activity activity = new Activity(getSession().getCustomProperties().get("appPackage"),
                        getSession().getCustomProperties().get("appActivity"));
                activity.setAppWaitActivity("com.xxxx");
                androidDriver.startActivity(activity);
            }
        }
    }

    /**
     * Restart the Session.
     */
    @Step("Restart the Session")
    public void restartSession() {
        if (getSession().getAppiumDriver().terminateApp("com.android.settings")) {
            getSession().getAppiumDriver().activateApp(getSession().getCustomProperties().get("appPackage"));
        } else {
            AndroidDriver<MobileElement> androidDriver = (AndroidDriver<MobileElement>) getSession().getAppiumDriver();
            getSession().getAppiumDriver().closeApp();
            Activity activity = new Activity(getSession().getCustomProperties().get("appPackage"),
                    getSession().getCustomProperties().get("appActivity"));
            activity.setAppWaitActivity("com.xxxx");
            androidDriver.startActivity(activity);
        }
    }

    /**
     * Takes a screenshot of the current screen
     * <p>
     * and writes it into the project path.
     */
    public void takeScreenShot() throws IOException {
        File scrFile = ((TakesScreenshot) getSession().getAppiumDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(scrFile);
        File outputFile = new File("screenshot.png");
        ImageIO.write(image, "png", outputFile);
    }

    /**
     * Takes a screenshot of the current screen
     * <p>
     * and writes it into the project path with a given file Name.
     */
    public void takeScreenShot(String fileName) throws IOException {
        File scrFile = ((TakesScreenshot) getSession().getAppiumDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(scrFile);
        File outputFile = new File(fileName);
        ImageIO.write(image, "png", outputFile);
    }

    /**
     * Takes a screenshot of the current screen
     * <p>
     * and writes it into the project path with a given file Name after converting to pdf.
     */
    public void takeScreenShotPdf(String fileName) throws IOException {
        File scrFile = ((TakesScreenshot) getSession().getAppiumDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(scrFile);
        File outputFile = new File(fileName);
        ImageIO.write(image, "png", outputFile);
        String name = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.indexOf("."));
        PdfUtil.getPdfFromImage(fileName, "test-output/PdfReport/" + name + ".pdf");
    }

    /**
     * Takes a page back.
     */
    public void navigateBack() {
        getSession().getAppiumDriver().navigate().back();
    }

    /**
     * Validate the Background Color in graph.
     *
     * @param element The element
     * @param number  The increment distance from Y center
     * @param color   The expected color
     * @return True if Background Color is displayed , otherwise returns
     * false.
     */
    @Step("Verifying if Background Color is Displayed in graph")
    public boolean isBackGroundColorDisplayed(MobileElement element, double number, String color) throws IOException {
        int x1 = getXCenter(element);
        int y = getYCenter(element);
        int y1 = (int) (y + (getHeight(element) * number));
        File scrFile = ((TakesScreenshot) getSession().getAppiumDriver()).getScreenshotAs(OutputType.FILE);
        BufferedImage image = ImageIO.read(scrFile);
        int clr = image.getRGB(x1, y1);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;
        if (color.equals("blue"))
            return ((blue > 200) && (red < 100) && (green < 100));
        if (color.equals("green"))
            return ((green > 200) && (red < 100) && (blue < 100));
        if (color.equals("red"))
            return ((red > 200) && (green < 100) && (blue < 100));
        else return false;
    }

    /**
     * get Coordinates to scroll in wheel
     *
     * @param direction
     * @param wheel
     * @return @instance Point
     */
    public Point getScrollCoordinates(Direction direction, MobileElement wheel) {
        int adjustedWheelHeight = wheel.getSize().getHeight();
        int xPoint = wheel.getCenter().getX() - adjustedWheelHeight;
        int yPoint = wheel.getCenter().getY() + adjustedWheelHeight;
        if (direction.equals(Direction.DOWN)) {
            xPoint = wheel.getCenter().getX() + adjustedWheelHeight;
        }
        return new Point(xPoint, yPoint);
    }

    public void scrollUpFromCoordinates(MobileElement wheel) {
        Point point = getScrollCoordinates(Direction.UP, wheel);
        scrollFromPoint(point, Direction.UP);
    }

    public void scrollDownFromCoordinates(MobileElement wheel) {
        Point point = getScrollCoordinates(Direction.DOWN, wheel);
        scrollFromPoint(point, Direction.DOWN);
    }

    public void scrollFastUpFromCoordinates(MobileElement wheel) {
        Point point = getScrollCoordinates(Direction.UP, wheel);
        scrollFastFromPoint(point, Direction.UP);
    }

    public void scrollFastDownFromCoordinates(MobileElement wheel) {
        Point point = getScrollCoordinates(Direction.DOWN, wheel);
        scrollFastFromPoint(point, Direction.DOWN);
    }

    /**
     * Scroll in wheel from a Point.
     *
     * @param direction
     * @param point
     */
    public void scrollFromPoint(Point point, Direction direction) {
        Dimension size = getSession().getAppiumDriver().manage().window().getSize();
        if (direction.equals(Direction.UP)) {
            int width = point.getX();
            int start = point.getY();
            int end = (int) (point.getY() * 0.9);
            touchAction(width, start, end);
        } else {
            int width = point.getX();
            int start = point.getY();
            int end = (int) (point.getY() * 1.1);
            touchAction(width, start, end);
        }
    }

    /**
     * Scroll in wheel from a Point.
     *
     * @param direction
     * @param point
     */
    public void scrollFastFromPoint(Point point, Direction direction) {
        Dimension size = getSession().getAppiumDriver().manage().window().getSize();
        if (direction.equals(Direction.UP)) {
            int width = point.getX();
            int start = point.getY();
            int end = (int) (point.getY() * 0.7);
            touchAction(width, start, end);
        } else {
            int width = point.getX();
            int start = point.getY();
            int end = (int) (point.getY() * 1.3);
            touchAction(width, start, end);
        }
    }

    /**
     * Scroll on a wheel.
     *
     * @param scrollText
     * @param targetText
     */
    public void scrollOnWheelFromText(String scrollText, String targetText, Direction direction) {
        int cycle = 0;
        boolean elementFound = false;
        while (!elementFound && cycle < 10) {
            if (direction.equals(Direction.DOWN)) {
                scrollDownFromCoordinates(getSession().getAppiumDriver().findElementByXPath("//*[contains(@value,'" + scrollText + "')]"));
            } else {
                scrollUpFromCoordinates(getSession().getAppiumDriver().findElementByXPath("//*[contains(@value,'" + scrollText + "')]"));
            }
            try {
                elementFound = doesElementExist(getSession().getAppiumDriver().findElementByXPath("//*[contains(@value,'" + targetText + "')]"), MIN_WAIT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (elementFound)
                tapOn(getSession().getAppiumDriver().findElementByXPath("//*[contains(@value,'" + targetText + "')]"));
            cycle++;
        }
    }

    /**
     * Scroll on a wheel.
     *
     * @param scrollText
     * @param targetText
     */
    public void scrollFastOnWheelFromText(String scrollText, String targetText, Direction direction) {
        int cycle = 0;
        boolean elementFound = false;
        while (!elementFound && cycle < 10) {
            if (direction.equals(Direction.DOWN)) {
                scrollFastDownFromCoordinates(getSession().getAppiumDriver().findElementByXPath("//*[contains(@value,'" + scrollText + "')]"));
            } else {
                scrollFastUpFromCoordinates(getSession().getAppiumDriver().findElementByXPath("//*[contains(@value,'" + scrollText + "')]"));
            }
            try {
                elementFound = doesElementExist(getSession().getAppiumDriver().findElementByXPath("//*[contains(@value,'" + targetText + "')]"), MIN_WAIT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (elementFound)
                tapOn(getSession().getAppiumDriver().findElementByXPath("//*[contains(@value,'" + targetText + "')]"));
            cycle++;
        }
    }
}
