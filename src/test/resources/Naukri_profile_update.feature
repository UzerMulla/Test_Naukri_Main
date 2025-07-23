Feature: Update Naukri Profile

  Scenario Outline: Update Naukri Profile
    Given I go to Naukri Website
    And I click on Cookie Banner
    When I click on Login button from header for login user
    And I enter "<loginname>" and "<Password>"
    And I click on Login button from login flyout
#    And I get the Login otp from gmail Account & Enter
    Then I verify user is logged-in successfully
    When I click on profile details and Click on update profile link
    And I click on edit icon of profile name
    And I updated the name with "<Updated_name>"
    And I click on Save Button
    Then I verify the profile name is successfully updated
    And I verify that profile last updated as Today


    Examples:
      | loginname              | Password        | Updated_name |
      | uzermulla586@gmail.com | 8446746733@Uzer | Uzer Mulla   |