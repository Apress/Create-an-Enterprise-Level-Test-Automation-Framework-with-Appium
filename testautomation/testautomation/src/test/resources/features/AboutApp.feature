@AboutApp
Feature: AboutApp
  Req number: xxxx

  Background:
    Given application is installed and launched


  @AboutApp @Regression
  Scenario: client wants to verify about app screen elements
    When user opens the application
    Then verify create account screen is displayed
    When user fills in details
    And clicks on create account button
    Then verify user is taken to the app login screen
    When user logs in with email and password
    And clicks on sign in button
    Then verify user is taken to the about app screen
    And verify user sees the following in app screen
      | Screen_Title    |
      | App_Logo        |
      | App_Name        |
      | App_Version     |
      | App_Images      |
      | Copyright_Txt   |
      | App_Description |
    Then close application and update HP QC test run


