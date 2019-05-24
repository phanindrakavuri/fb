package com.qmo.facebook.pages;

import com.qmo.facebook.utils.WebDriverProvider;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
//import com.swacorp.qmo.jbehave.util.StoryData;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.plexus.util.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class BasePage {
    public static WebDriver driver = WebDriverProvider.driver;
    public static String PNR_NO;
    static Integer DEFAULT_WAIT_RETRY = 5000; // Checking for each
    static Integer DEFAULT_WAIT_LIMIT = 200;
    static Integer SHORT_WAIT = 50;
    protected WebDriverWait wait;
    //StoryData storyData;
    static String SCREEN_SHOT_DIR = "\\target\\jbehave\\view\\images\\";
    static String DELETE_EXISTING = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;

    public String[] getSubStrings(String fullString, String delimiter) throws Exception {
        return fullString.split(delimiter);
    }

    public int getWebElementsSize(By locator) throws Exception {
        return driver.findElements(locator).size();
    }

    public String getWebElementText(By locator) throws IOException {
        // sleep(1000);
        WebElement webElementText = waitForElement(locator);
        return webElementText.getText();
    }

    public void switchWindowHandle() throws IOException {
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }
    }

    public String getWebElementAttribute(By locator, String webElementAttribute) throws Exception {
        WebElement webElementEnter = waitForElement(locator);
        return webElementEnter.getAttribute(webElementAttribute);
    }

    public void enterText(By locator, String text, WebDriver driver) throws IOException,TimeoutException {
        WebElement webElementEnter = waitForElement(locator);
        webElementEnter.sendKeys(text);
    }

    public void clearAndEnterText(By locator, String text) throws IOException,TimeoutException {
        WebElement webElementEnter = waitForElement(locator);
        webElementEnter.sendKeys(Keys.chord(Keys.CONTROL, "a"), text);
    }

    public void clickClearAndEnterText(By locator, String text) throws Exception {
        clickWaitForElement(locator);
        WebElement webElement = waitForElement(locator);
        /*List elementList = driver.findElements(locator);
        if (elementList.size() >= 1) {
            webElement.sendKeys(DELETE_EXISTING);
        }*/
        webElement.clear();
        sleep(500);
        Actions action = new Actions(driver);
        action.sendKeys(webElement, text).build().perform();

    }

    public void clickWaitForElement(By locator) throws IOException,TimeoutException {
        WebElement webElementClick = waitForElement(locator);
        if(webElementClick.isDisplayed()) {
            webElementClick.click();
        }
        else {
           // Loggers.CONSOLE_LOGGER.info(locator.toString() + "Is not visible");
        }
        //sleep(1000);
    }

    public void doubleClickWaitForElement(By locator) throws IOException,TimeoutException {
        WebElement webElementClick = waitForElement(locator);

        if(webElementClick.isDisplayed()) {
            Actions action = new Actions(driver);
            action.doubleClick(webElementClick).perform();
        }
        else {
            //Loggers.CONSOLE_LOGGER.info(locator.toString() + "Is not visible");
        }
        //sleep(1000);
    }

    public void checkAndclickForEnabledButton(By locator) throws IOException,TimeoutException {
        WebElement webElementButton = waitForElement(locator);

        WebDriverWait wait = new WebDriverWait(driver,DEFAULT_WAIT_LIMIT,DEFAULT_WAIT_RETRY);
        wait.until(ExpectedConditions.elementToBeClickable(webElementButton));

        if(webElementButton.isDisplayed() && webElementButton.isEnabled()){
            webElementButton.click();
        }else {
            //Loggers.CONSOLE_LOGGER.info("Button is not enabled or Button is not found");
        }
    }

    public void selectText(By locator, String text) throws IOException,TimeoutException {
        WebElement webElementSelect = waitForElement(locator);
        Select select = new Select(webElementSelect);
        select.selectByVisibleText(text);

    }

    public void selectValue(By locator, String text) throws IOException,TimeoutException {
        WebElement webElementSelect = waitForElement(locator);
        Select select = new Select(webElementSelect);
        select.selectByValue(text);

    }

    /*
    * Name          : getWebElementIndex
    * Description   : returns web element index

    */
    public int getWebElementIndex(String webElementsLocator) throws IOException,TimeoutException {
        List<WebElement> webElementsList = getWebElementObjects(webElementsLocator);
        return webElementsList.size();
    }

    /*
    * Name          : getWebElementObjects
    * Description   : returns list of web element objects
    *
    */
    public List<WebElement> getWebElementObjects(String webElementDesc) {
        String[] delimiters = new String[]{"="};
        String[] arrFindByValues = webElementDesc.split(delimiters[0]);
        String FindBy = arrFindByValues[0];
        String val = arrFindByValues[1];

        try {
            String strElement = FindBy.toLowerCase();
            if (strElement.equalsIgnoreCase("linkText")) {
                return driver.findElements(By.linkText(val));
            } else if (strElement.equalsIgnoreCase("partialLinkText")) {
                return driver.findElements(By.partialLinkText(val));
            } else if (strElement.equalsIgnoreCase("xpath")) {
                return driver.findElements(By.xpath(val));
            } else if (strElement.equalsIgnoreCase("name")) {
                return driver.findElements(By.name(val));
            } else if (strElement.equalsIgnoreCase("id")) {
                return driver.findElements(By.id(val));
            } else if (strElement.equalsIgnoreCase("className")) {
                return driver.findElements(By.className(val));
            } else if (strElement.equalsIgnoreCase("cssSelector")) {
                return driver.findElements(By.cssSelector(val));
            } else if (strElement.equalsIgnoreCase("tagName")) {
                return driver.findElements(By.tagName(val));
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

//    public void executeJS(String jScript, String locatorId) {
//        WebElement webElement =  getObject(locatorId);
//        JavascriptExecutor objJSE = (JavascriptExecutor) driver;
//        objJSE.executeScript(jScript, webElement);
//    }

    public void executeJS(String jScript, By locator) throws IOException {
        JavascriptExecutor objJSE = (JavascriptExecutor) driver;
        objJSE.executeScript(jScript, waitForElement(locator));
        // sleep(1000);
    }

    /*
     * Name             : doMouseOverAndClick
     * Description      : This function mouse hovers on a certain element and clicks on it

     */
    public boolean doMouseOverAndClick(String strDesc) {
        WebElement objClick;
        objClick = getObject(strDesc);
        if (objClick == null)
            return false;
        Actions action1 = new Actions(driver);
        action1.moveToElement(getObject(strDesc)).click().build().perform();
        return true;
    }

    /*
     * Name             : getObject
     * Description      : Returns the webelement based on the description

     */
    public WebElement getObject(String objDesc) {
        String[] delimiters = new String[]{"="};
        String[] arrFindByValues = objDesc.split(delimiters[0]);
        String FindBy = arrFindByValues[0];
        String val = arrFindByValues[1];

        try {
            String strElement = FindBy.toLowerCase();
            if (strElement.equalsIgnoreCase("linkText")) {
                return driver.findElement(By.linkText(val));
            } else if (strElement.equalsIgnoreCase("partialLinkText")) {
                return driver.findElement(By.partialLinkText(val));
            } else if (strElement.equalsIgnoreCase("xpath")) {
                return driver.findElement(By.xpath(val));
            } else if (strElement.equalsIgnoreCase("name")) {
                return driver.findElement(By.name(val));
            } else if (strElement.equalsIgnoreCase("id")) {
                return driver.findElement(By.id(val));
            } else if (strElement.equalsIgnoreCase("className")) {
                return driver.findElement(By.className(val));
            } else if (strElement.equalsIgnoreCase("cssSelector")) {
                return driver.findElement(By.cssSelector(val));
            } else if (strElement.equalsIgnoreCase("tagName")) {
                return driver.findElement(By.tagName(val));
            } else {
                System.out.println("Property name " + FindBy + " specified for object " + objDesc + " is invalid");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.toString() + " occured while fetching the object");
            return null;
        }

    }

    /*
     * Name             : enterTabKey
     * Description      : Simulates hitting tab key

     */
    public void enterTabKey() {

        Actions action = new Actions(driver);
        action.sendKeys(Keys.TAB).build().perform();
    }

    /*
     * Name             : sleep
     * Description      : sleeps for specified time

     */
    public void sleep(int miliSec) {
        try {
            Thread.sleep(miliSec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // <editor-fold desc="Date & Time Utility - Methods">

    /*
    * Name          : getDateTimeStamp
    * Description   : returns current date and time stamp

    */
    public String getDateTimeStamp() {
        Date today = new Date();
        java.sql.Timestamp now = new java.sql.Timestamp(today.getTime());
        String timeStamp = now.toString().replaceAll(":", "")
                .replaceAll("-", "").replaceAll(" ", "");
        timeStamp = timeStamp.split("\\.")[0];
        return timeStamp;
    }

    /*
    * Name          : getCurrentDate
    * Description   : returns current date in MM/dd/yyyy format

    */
    public String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        //get current date time with Date()
        Date date = new Date();
        return dateFormat.format(date);
    }

    /*
    * Name          : getModifiedDate
    * Description   : returns updated date by adding or subtracting required days to current date

    */
    public String getModifiedDate(int days) {
        Calendar objCalendar = Calendar.getInstance();
        objCalendar.add(Calendar.DATE, days);
        return objCalendar.get(Calendar.MONTH) + 1 + "/" + objCalendar.get(Calendar.DATE) + "/" + objCalendar.get(Calendar.YEAR);
    }

    /*
    * Name          : getCurrentTimeWithOutSeconds
    * Description   : returns current time with out seconds

    */
    public String getCurrentTimeWithOutSeconds() {
        int minute, hour;
        GregorianCalendar date = new GregorianCalendar();
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);
        String strCurrentTime = hour + ":" + minute;
        return strCurrentTime;
    }

    /*
    * Name          : getModifiedTimeWithOutSeconds
    * Description   : returns updated time by adding or subtracting required hours to current time

    */
    public String getModifiedTimeWithOutSeconds(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    /*
    * Name          : getModifiedTimeByUpdatedMinutes
    * Description   : returns current time with updated minutes

    */
    public String getModifiedTimeByUpdatedMinutes(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public String getCurrentTime() {
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();
        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);
        String strCurrentTime = hour + ":" + minute + ":" + second;
        return strCurrentTime;
    }

    // </editor-fold desc="Date & Time Utility - Methods">

    // <editor-fold desc="Existing Base Page - Methods">

/*
    public void report(String s) {
        this.storyData.report(s);
    }*/

/*
    public void fail(String message) {
        try {
            addScreenshot("<b style='color:red'>[VALIDATION ERROR]</b> " + message);
            //throw new CirrusSystemError(message, driver);
        } catch (IOException e) {
            e.fillInStackTrace();
            e.printStackTrace();
        }
    }*/
/*

    public void success(String message) {
        try {
            addScreenshot("<b style='color:green'>[PASSED]</b> " + message);
        } catch (IOException e) {
            e.fillInStackTrace();
            e.printStackTrace();
        }
    }
*/


    public String getScreenshot() throws IOException {
        File screenshot;
        if (driver instanceof TakesScreenshot) {
            screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        } else {
            // for Selenium Grid
            WebDriver augmentedDriver = new Augmenter().augment(driver);
            screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
        }

        String fileName = UUID.randomUUID().toString().substring(0, 18).toUpperCase() + ".png";
        String path = System.getProperty("user.dir") + SCREEN_SHOT_DIR + fileName;
        // Needs Commons IO library
        FileUtils.copyFile(screenshot, new File(path));

        return "images/" + fileName;
    }
/*
    public void addScreenshot(String text) throws IOException {
        String fileName = getScreenshot();
        report((DefaultGroovyMethods.asBoolean(text) ? text : "") + " <a href='" + fileName + "'>[screenshot]</a>");
    }*/
/*
    public BasePage withStoryData(StoryData storyData) {
        this.storyData = storyData;
        return this;
    }*/

    public static boolean isElementPresent(By locator) {
        WebElement element;
        try {
            element = driver.findElement(locator);
        } catch (Exception e) {
            // Element is not present
            return false;
        }

        return element != null;
    }

    /**
     * Here Validating the PNR field Values.
     *
     * @param value
     * @return
     */
    public static boolean isElementValuePresent(String value) {
        String element;
        try {
            element = value;
        } catch (Exception e) {
            // Element is not present
            return false;
        }

        return element != null;
    }


    public boolean isElementDisplayed(By locator) {
        boolean isElementDisplayed;
        try {
            isElementDisplayed = driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            // Element is not displayed or present
            isElementDisplayed = false;
        }

        return isElementDisplayed;
    }

    public void addText(By locator, String text) throws IOException {
        //WebElement element = waitForElement(locator)
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        // highlightElement(locator);
        waitForElement(locator).click();
        waitForElement(locator).clear();
        waitForElement(locator).sendKeys(text);

    }

    public void addText(WebElement element, String text) {
        //highlightElement(element);
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    public void radioBtn(By locator) throws IOException {
        WebElement element = waitForElement(locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    public void selectOption(By locator, String opt) throws IOException {
        WebElement element = waitForElement(locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        //highlightElement(locator);
        Select select = new Select(element);
        select.selectByVisibleText(opt);
    }

    public void selectOptionByIndex(By locator, int index) throws IOException {
        WebElement element = waitForElement(locator);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
        //highlightElement(locator);
        Select select = new Select(element);
        select.selectByIndex(index);
    }

    //Original fetch
   /* public Object fetch(String k) {
        return this.storyData.getStoredValue(k);
    }*/

    /*public Map<String, String> fetch(String k) {
        return (Map<String, String>) this.storyData.getStoredValue(k);
    }*/

    //Original store
   /* public void store(String k, Object v) {
        this.storyData.storeValue(k, v);
    }*/

    /*public void store(String k, String v) {
        this.storyData.storeValue(k, v);
    }*/

    public WebElement getElement(By element) throws IOException {
        boolean isExist = false;
        String errorMSG;
        errorMSG = "";  //?
        for (int i = 0; i < SHORT_WAIT * 10; i++) {
            try {
                WebElement myElement = driver.findElement(element);
                if (myElement.isDisplayed()) {
                    isExist = true;
                    return myElement;
                }

            } catch (Exception e) {
                errorMSG = e.getMessage();
            }

            //Thread.sleep(100);    //?
        }

        //verifyIsTrue(isExist, "Element was not found  " + errorMSG, "");
        return null;
    }

    public void buttonClick(By element) throws IOException {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        String buttonName = getElement(element).getText().split("\n")[0];
        //addScreenshot("App state before clicking <b style='color:blue'>[" + buttonName + "]</b>");
        getElement(element).click();
        //addScreenshot("App state after clicking <b style='color:blue'>[" + buttonName + "]</b>");
    }

    //***************************************** waitForElement *******************************************************//

    /**
     *
     * @param byElement The element for which wait will take place
     * @param parentElement The parent alement of the above mentioned element
     * @param throwExceptions
     * @param waitLimit The default wait limit for timeout of the webdriver
     * @param retryInterval The default retry time limit  of the webdriver
     * @param ensureDisplayed whether the element displayed or not
     * @param ensureAjax if there is any ajax call
     * @return Final webelement
     * @throws IOException
     */
    public WebElement waitForElement(final By byElement, final WebElement parentElement, boolean throwExceptions, int waitLimit, int retryInterval, final boolean ensureDisplayed, boolean ensureAjax) throws IOException,TimeoutException,ElementNotVisibleException {

        final List<WebElement> resultElement = new ArrayList<WebElement>();
        int timeout = waitLimit;
        int retry = retryInterval;
        long start = System.currentTimeMillis();
        /* First it will check if the element is found in the driver or not */
        if(!isElementPresent(byElement)){
        WebDriverWait webDriverWait = new WebDriverWait(driver, timeout, retry);

        try {

            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(byElement));
            List<WebElement> elements;
            if (null != parentElement) {
                elements = parentElement.findElements(byElement);
            } else {
                elements = driver.findElements(byElement);
            }

            if(elements.size()> 0) {
                resultElement.add(elements.get(0));
            }
        } catch (TimeoutException te) {
            if (throwExceptions) {
                //addScreenshot("App state at time of error");
                throw new WebDriverException("Timeout - Element '" + byElement + "' not found in configured " + timeout + " seconds, actual: " + (System.currentTimeMillis() - start) + "millis.");
            }

        } catch (WebDriverException wde) {
            if (throwExceptions) {
                //addScreenshot("App state at time of error");
                throw new WebDriverException(wde.getMessage() + " JBehave - Element '" + byElement + "' failed lookup through Selenium.");
            }

        }
    }
    else {
            resultElement.add(driver.findElements(byElement).get(0));
        }
        if(resultElement.size() > 0) {

            return ((WebElement) (resultElement.get(0)));
        }
        else {
            return null;
        }
    }

    public static void waitForElementLoading(final By byElement) throws IOException {

            //if (null != driver) {
                try {


/*                    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                            .withTimeout(30, TimeUnit.SECONDS)
                            .pollingEvery(10, TimeUnit.SECONDS)
                            .ignoring(NoSuchElementException.class)
                            .ignoring(StaleElementReferenceException.class);
                    wait.until(ExpectedConditions.presenceOfElementLocated(byElement));*/
                    Wait<WebDriver> wait = new WebDriverWait(driver, 30,10);
                    wait.until(new Function<WebDriver, Boolean>() {
                        public Boolean apply(WebDriver driver) {
                            System.out.println("Current Window State       : "
                                    + String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState")));
                            return String
                                    .valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"))
                                    .equals("complete");
                        }
                    });
                }catch (TimeoutException e){
                   // Loggers.CONSOLE_LOGGER.info("Element could not found");
                }
          /*  } else {
                throw new NoSuchElementException(byElement.getClass().getName() + "Not found");
            }*/
    }

    /**
     * This method is used for searching the first element after the pageloading takes place
     * @param byElement
     * @param timeout
     * @throws IOException
     */
    public static void waitForElementLoadingDuringPageLoad(final By byElement,int timeout) throws IOException {


            try {


                Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                        .withTimeout(timeout, TimeUnit.SECONDS)
                        .pollingEvery(10, TimeUnit.SECONDS)
                        .ignoring(NoSuchElementException.class)
                        .ignoring(StaleElementReferenceException.class);
                wait.until(ExpectedConditions.presenceOfElementLocated(byElement));
            }catch (TimeoutException e){
                //Loggers.CONSOLE_LOGGER.info("");
            }
    }

    public static void waitForElementLoadingone(final By byElement) throws IOException {
        if(!isElementPresent(byElement)) {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(750, TimeUnit.SECONDS)
                    .pollingEvery(10, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(TimeoutException.class);
            wait.until(ExpectedConditions.presenceOfElementLocated(byElement));
        }
    }


    public WebElement waitForElement(By byElement, WebElement parentElement, boolean throwExceptions, int waitLimit, int retryInterval, boolean ensureDisplayed) throws IOException {
        return waitForElement(byElement, parentElement, throwExceptions, waitLimit, retryInterval, ensureDisplayed, true);
    }

    public WebElement waitForElement(By byElement, WebElement parentElement, boolean throwExceptions, int waitLimit, int retryInterval) throws IOException {
        return waitForElement(byElement, parentElement, throwExceptions, waitLimit, retryInterval, false, true);
    }

    public WebElement waitForElement(By byElement, WebElement parentElement, boolean throwExceptions, int waitLimit) throws IOException {
        return waitForElement(byElement, parentElement, throwExceptions, waitLimit, DEFAULT_WAIT_RETRY, false, true);
    }

    public WebElement waitForElement(By byElement, WebElement parentElement, boolean throwExceptions) throws IOException {
        return waitForElement(byElement, parentElement, throwExceptions, DEFAULT_WAIT_LIMIT, DEFAULT_WAIT_RETRY, false, true);
    }

    public WebElement waitForElement(By byElement, WebElement parentElement) throws IOException {
        return waitForElement(byElement, parentElement, true, DEFAULT_WAIT_LIMIT, DEFAULT_WAIT_RETRY, false, true);
    }

    public WebElement waitForElement(By byElement) throws IOException,TimeoutException,ElementNotVisibleException {
        return waitForElement(byElement, null, true, DEFAULT_WAIT_LIMIT, DEFAULT_WAIT_RETRY, false, true);
    }

    //***************************************** waitForElements *******************************************************//

    public List<WebElement> waitForElements(final By byElement, final WebElement parentElement, boolean handleFail, int waitLimit, int retryInterval) throws InterruptedException, IOException {
        final List<WebElement> resultElement = new ArrayList<WebElement>();
        int timeout = (int) waitLimit;
        int retry = (int) retryInterval;
        WebDriverWait wdw = new WebDriverWait(driver, timeout, retry);
        //waitForAjaxRequestToFinish(); //?
        try {
            wdw.until(new Predicate<WebDriver>() {
                public boolean apply(WebDriver wd) {
                    List<WebElement> elements;
                    if (DefaultGroovyMethods.asBoolean(parentElement)) {
                        elements = parentElement.findElements(byElement);
                    } else {
                        elements = wd.findElements(byElement);
                    }

                    if (elements.size() > 0) {
                        return true;
                    } else return false;
                }

            });
        } catch (TimeoutException te) {
            if (handleFail) {
                //addScreenshot("App state at time of error");
                throw new WebDriverException("Timeout - Element '" + byElement + "' not found in " + timeout + " seconds");
            }
        }

        return resultElement;
    }

    public List<WebElement> waitForElements(By byElement, WebElement parentElement, boolean handleFail, int waitLimit) throws InterruptedException, IOException {
        return waitForElements(byElement, parentElement, handleFail, waitLimit, DEFAULT_WAIT_RETRY);
    }

    public List<WebElement> waitForElements(By byElement, WebElement parentElement, boolean handleFail) throws InterruptedException, IOException {
        return waitForElements(byElement, parentElement, handleFail, DEFAULT_WAIT_LIMIT, DEFAULT_WAIT_RETRY);
    }

    public List<WebElement> waitForElements(By byElement, WebElement parentElement) throws InterruptedException, IOException {
        return waitForElements(byElement, parentElement, true, DEFAULT_WAIT_LIMIT, DEFAULT_WAIT_RETRY);
    }

    public List<WebElement> waitForElements(By byElement) throws InterruptedException, IOException {
        return waitForElements(byElement, null, true, DEFAULT_WAIT_LIMIT, DEFAULT_WAIT_RETRY);
    }

    public void scrollToView(By locator) throws IOException {
        WebElement element = waitForElement(locator);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        // an element must be visible before it can be clicked on
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    void scrollToView(WebElement element) {
        // an element must be visible before it can be clicked on
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

    }

    //**************************************** clickWithWait *********************************************************//

    public void clickWithWait(final By locator, final WebElement parentElement) throws IOException {
        WebDriverWait wdw = new WebDriverWait(driver, DEFAULT_WAIT_LIMIT, DEFAULT_WAIT_RETRY);
        wdw.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver wd) {
                try {
                    WebElement element;
                    if (DefaultGroovyMethods.asBoolean(parentElement)) {
                        element = waitForElement(locator, parentElement);
                    } else {
                        element = waitForElement(locator);
                    }
                    wait.until(ExpectedConditions.elementToBeClickable(locator));
                    //highlightElement(locator); //?
                    //addScreenshot("App state before clicking <b style='color:blue'>[" + element.getText().split("\n")[0] + "]</b>"); //?
                    //removeHighlight(locator); //?
                    element.click();
                    return true;
                } catch (WebDriverException wde) {
                   /* try {
                       // addScreenshot("App state when attempting to click");  //?
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

        });

    }

    public void clickWithWait(By locator) throws IOException {
        clickWithWait(locator, null);
    }

    //************************************** waitForAjaxRequestToFinish **********************************************//

    public void waitForAjaxRequestToFinish(final WebDriver wd) {
        WebDriverWait wdw = new WebDriverWait(wd, DEFAULT_WAIT_LIMIT, DEFAULT_WAIT_RETRY);
        wdw.until(new Predicate<WebDriver>() {
            public boolean apply(WebDriver webdriver) {
                // Skip the Ajax check if the Web Browser is IE, otherwise tests take 3x longer to run.
                if (webdriver instanceof JavascriptExecutor) {
                    int ajaxPendingRequest = (int) ((JavascriptExecutor) wd).executeScript("return (aria.core.IO.nbRequests - (aria.core.IO.nbOKResponses + aria.core.IO.nbKOResponses))");
                    boolean ajaxWheelPresent = (isElementPresent(By.className("uicLo-loading")) && !isElementPresent(By.cssSelector(".uicLo-loading.uicHidden")) && isElementDisplayed(By.className("uicLo-loading"))) || isElementPresent(By.className("acLoading")) || isElementPresent(By.className("uicLoaderOverlay uicLo-loading")) || isElementPresent(By.className("spinneron")) || isElementPresent(By.className("ygtvloading")) || isElementPresent(By.className("uicApfAppLoading"));
                    if (ajaxPendingRequest == 0 && !ajaxWheelPresent) {
                        return true;
                    }

                    return false;
                } else {
                    return true;
                }

            }

        });
    }

    public void waitForAjaxRequestToFinish() {
        waitForAjaxRequestToFinish(driver);
    }

    public void waitForAjaxRequestToFinishEDBI() {
        waitForAjaxRequestToFinish(driver);
    }

    /**
     * The method is used to take the tag name and the title
     * @param tagName The HTML tag name
     * @param title The title of the HTML title
     * @return Webelement
     */
    public WebElement getAttributeByTitle(String tagName,String title){
        List<WebElement> elements = driver.findElements(By.tagName(tagName));
        for (WebElement element : elements) {
            if (element.getAttribute("title").equals(title)) {
                return element;
            }
        }
        return null;
    }


}
