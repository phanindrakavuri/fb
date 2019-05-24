package com.qmo.facebook.steps;


import com.qmo.facebook.pages.LoginPage;
import org.jbehave.core.annotations.When;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class LoginSteps{
    @Autowired
    LoginPage loginPage;

    @When("I login")
    public void login() throws IOException, ParseException, Exception {
        loginPage.Login();
    }

    @When("Facebook")
    public void logout() throws InterruptedException, IOException {
        //loginPage.logout();
    }



}