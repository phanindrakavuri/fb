package com.qmo.facebook.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by x216476
 */

public class WebDriverProvider {
    public static WebDriver driver;

    public enum Browser {
        FIREFOX, IE, OPERA, CHROME, SAFARI
    }

    static {
        Browser browser = Browser.valueOf(Browser.class, System.getProperty("browser", "firefox").toUpperCase());
        driver=createDriver(browser);
    }

    private static WebDriver createDriver(Browser browser) {
        switch (browser) {
            case FIREFOX:
                return createFirefoxDriver();
            case IE:
                return createInternetExplorerDriver();
            /*case OPERA:
                return createOperaDriver();
            case CHROME:
                return createChromeDriver();
            case SAFARI:
                return createSafariDriver();*/
            default:
                return createFirefoxDriver();
        }
    }

    protected static FirefoxDriver createFirefoxDriver() {
        ProfilesIni allProfiles = new ProfilesIni();
        FirefoxProfile firefoxProfile;
        try {
            firefoxProfile = allProfiles.getProfile("webdriver");
            if (firefoxProfile == null) {
                firefoxProfile = new FirefoxProfile();
            }
        } catch (Exception e) {
            firefoxProfile = new FirefoxProfile();
        }
        firefoxProfile.setAcceptUntrustedCertificates(true);
        FirefoxDriver driver = new FirefoxDriver(firefoxProfile);
        driver.manage().window().maximize();
        return driver;
    }


    protected static WebDriver createInternetExplorerDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        capabilities.setCapability("requireWindowFocus", true);
        System.setProperty("webdriver.ie.driver","C:\\EDA\\POC\\TestPOC\\src\\main\\resources\\drivers\\IEDriverServer.exe");
        WebDriver driver = new InternetExplorerDriver(capabilities);
        driver.manage().window().maximize();
        return driver;//new InternetExplorerDriver(capabilities);
    }

    public static WebDriverProvider instance;
    public static WebDriverProvider getInstance() {
        if (instance == null) {
            instance = new WebDriverProvider();
        }
        return instance;
    }


}
