package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "inputUsername")
    WebElement inputUsernameField;

    @FindBy(id = "inputPassword")
    WebElement inputPasswordField;

    @FindBy(className = "alert-dark")
    WebElement successMsg;

    @FindBy(className = "alert-danger")
    WebElement errorMsg;

    @FindBy(id = "submit")
    public WebElement submitButton;

    public LoginPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void signin(String username, String password) {
        inputUsernameField.sendKeys(username);
        inputPasswordField.sendKeys(password);
        submitButton.click();
    }

    public boolean isSuccess() {
        try {
            return successMsg.isDisplayed();
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public String getErrorMsg() {
        try {
            return errorMsg.getText();
        } catch (NoSuchElementException ex) {
            return null;
        }
    }

}
