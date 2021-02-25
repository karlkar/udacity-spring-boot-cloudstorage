package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(className = "display-5")
    WebElement resultText;

    @FindBy(id = "errorMsg")
    WebElement errorMsg;

    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public boolean isSuccess() {
        return resultText.getText().equalsIgnoreCase("Success");
    }

    public String getErrorMsg() {
        if (isSuccess()) {
            return null;
        }
        try {
            return errorMsg.getText();
        } catch (NoSuchElementException ex) {
            return null;
        }
    }
}
