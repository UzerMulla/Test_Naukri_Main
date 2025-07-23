package StepDefs;

import Helpers.Hooks;
import PageObjects.profilePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class ProfilePage {

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    private static String UserName="";

    @And("I click on edit icon of profile name")
    public void iClickOnEditIconOfProfileName() {
        Hooks.WaitTillElementVisible(profilePage.ProfileDashBoard());
        Hooks.WaitTillElementClickable(profilePage.Edit_Icon());
        profilePage.Edit_Icon().click();
    }

    @And("I updated the name with {string}")
    public void iUpdatedTheNameWith(String updated_Name) {
        UserName=updated_Name;
        Hooks.WaitTillElementVisible(profilePage.name_text());
        profilePage.name_text().clear();
        profilePage.name_text().sendKeys(UserName);

    }

    @And("I click on Save Button")
    public void iClickOnSaveButton() {
        Hooks.ScrollToElement(profilePage.submit_button());
        profilePage.submit_button().click();
    }

    @Then("I verify the profile name is successfully updated")
    public void iVerifyTheProfileNameIsSuccessfullyUpdated() {
        Hooks.WaitTillElementClickable(profilePage.Edit_Icon());
        Hooks.WaitTillElementVisible(profilePage.profileName());
        Assert.assertEquals("Profile Name is not updated correctly!",getUserName(),profilePage.profileName().getText().trim());
    }

    @And("I verify that profile last updated as Today")
    public void iVerifyThatProfileLastUpdatedAsToday() {
        Assert.assertEquals("profile Updated Status!","Today",profilePage.profileUpdatedValue().getText().trim());

    }
}
