package StepDefs;

import Helpers.Hooks;
import PageObjects.login;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

import java.util.Date;

import static Helpers.Hooks.OpenBrowser;
import static Helpers.Hooks.driver;

public class LoginFeature {
    public static Date dateobj ;
    @Given("I go to Naukri Website")
    public void iGoToNaukriWebsite() {
        OpenBrowser("chrome");
        driver.get("https://www.naukri.com/");


    }

    @When("I click on Login button from header for login user")
    public void iClickOnLoginButtonFromHeaderForLoginUser() {
        Hooks.WaitTillElementVisible(login.header_loginButton());
        login.header_loginButton().click();
    }

    @And("I enter {string} and {string}")
    public void iEnterAnd(String userName, String passWord) {
        Hooks.WaitTillElementVisible(login.username_text());
        login.username_text().sendKeys(userName);
        login.password_text().sendKeys(passWord);
    }

    @And("I click on Login button from login flyout")
    public void iClickOnLoginButtonFromLoginFlyout() {
        dateobj = new Date();
        Hooks.WaitTillElementVisible(login.SignIn_button());
        login.SignIn_button().click();
    }

    @And("I click on Cookie Banner")
    public void iClickOnCookieBanner() {
        Hooks.WaitTillElementPresent(By.xpath("//div[@class='acceptance-btn']"));
        Hooks.WaitTillElementVisible(login.cookieAccept_button());
        login.cookieAccept_button().click();
    }
}
