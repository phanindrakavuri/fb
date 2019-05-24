package com.qmo.facebook.pages;

import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginPage extends BasePage {

    @Autowired
    Environment environment;

    private static final By LOGIN_USERNAME_INPUT = By.id("email");
    private static final By LOGIN_PASSWORD_INPUT = By.id("pass");
    private static final By LOGIN_BUTTON = By.xpath("//label[contains(@id,'loginbutton')]/input");
    private static final By LOGOUT_LINK = By.id("eusermanagement_logout_logo_logout_id");
   // private static final By LOGOUT_DIALOG_OK = By.id("uicAlertBox_ok");

    private static final By LOGIN_BUTTON_CLICK = By.xpath("//div[contains(@id,'userNavigationLabel')]");

    private static final By LOGOUT_DIALOG_OK = By.xpath(".//*[@id='BLUE_BAR_ID_DO_NOT_USE']/div/div/div[1]/div/div/ul/li[16]/a/span/span");

    public void Login() throws Exception {
        driver.get(environment.getProperty("ard.url"));
        driver.findElement(LOGIN_USERNAME_INPUT).sendKeys(environment.getProperty("ard.username"));
        driver.findElement(LOGIN_PASSWORD_INPUT).sendKeys(environment.getProperty("ard.password"));
        driver.findElement(LOGIN_BUTTON).click();
        sleep(20000);

    }

    public void logout() throws IOException, InterruptedException {
        Thread.sleep(5000);
        waitForElement(LOGIN_BUTTON_CLICK);
        driver.findElement(LOGIN_BUTTON_CLICK).click();
        driver.findElement(LOGOUT_DIALOG_OK).click();
    }

}
