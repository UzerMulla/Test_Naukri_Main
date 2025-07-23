package StepDefs;

import Helpers.EmailAccess;
import Helpers.Hooks;
import PageObjects.homepage;
import PageObjects.profilePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import static StepDefs.ProfilePage.getUserName;

public class HomePage {
    @Then("I verify user is logged-in successfully")
    public void iVerifyUserIsLoggedInSuccessfully() {
        Hooks.WaitTillElementPresent(By.xpath("//img[@alt='naukri user profile img']"));
        Hooks.WaitTillElementVisible(homepage.profileImage());
        Assert.assertTrue(homepage.profileImage().isDisplayed());
    }

    @When("I click on profile details and Click on update profile link")
    public void iClickOnProfileDetailsAndClickOnUpdateProfileLink() {
        homepage.profileImage().click();
        Hooks.WaitTillElementClickable(homepage.profileUpdate_link());
        homepage.profileUpdate_link().click();
    }

    @And("I get the Login otp from gmail Account & Enter")
    public void iGetTheLoginOtpFromGmailAccountEnter() throws MessagingException, GeneralSecurityException, IOException, ParseException, InterruptedException {
        String mailSubject="Your OTP for logging in Naukri account";
        String currentClassName = "Naukri_profile_update.feature".replaceAll("[^a-zA-Z0-9]", "");
        String otp= EmailAccess.ReadEmailWithSubject("uzermulla586@gmail.com",mailSubject,LoginFeature.dateobj,currentClassName);

        System.out.println("OTP:"+otp);
    }

}
