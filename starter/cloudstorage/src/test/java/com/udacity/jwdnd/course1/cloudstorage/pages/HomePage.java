package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class HomePage {

    @FindBy(id = "logoutSubmit")
    WebElement logoutButton;

    @FindBy(id = "fileSubmit")
    WebElement fileSubmit;

    @FindBy(id = "fileUpload")
    WebElement fileUploadButton;

    @FindAll({@FindBy(className = "filename")})
    List<WebElement> filenames;

    @FindBy(id = "nav-files-tab")
    WebElement filesTab;

    @FindBy(id = "nav-notes-tab")
    WebElement notesTab;

    @FindBy(id = "nav-credentials-tab")
    WebElement credentialsTab;

    @FindBy(id = "add_note_btn")
    WebElement addNoteBtn;

    @FindBy(id = "note-title")
    WebElement noteTitleInput;

    @FindBy(id = "note-description")
    WebElement noteDescriptionInput;

    @FindBy(id= "note-save-changes")
    WebElement noteSaveChangesBtn;

    @FindAll({@FindBy(className = "row-note-title")})
    List<WebElement> noteTitles;

    @FindAll({@FindBy(className = "row-note-desc")})
    List<WebElement> noteDescs;

    @FindAll({@FindBy(className = "note-edit-btn")})
    List<WebElement> noteEditBtns;

    @FindAll({@FindBy(className = "note-delete-btn")})
    List<WebElement> noteDeleteBtns;

    @FindBy(id = "add_cred_btn")
    WebElement addCredentialBtn;

    @FindBy(id = "credential-url")
    WebElement credentialUrlInput;

    @FindBy(id = "credential-username")
    WebElement credentialUsernameInput;

    @FindBy(id = "credential-password")
    WebElement credentialPassInput;

    @FindBy(id= "credential-save-changes")
    WebElement credentialSaveChangesBtn;

    @FindAll({@FindBy(className = "row-cred-url")})
    List<WebElement> credentialUrls;

    @FindAll({@FindBy(className = "row-cred-user")})
    List<WebElement> credentialUsers;

    @FindAll({@FindBy(className = "cred-edit-btn")})
    List<WebElement> credEditBtns;

    @FindAll({@FindBy(className = "cred-delete-btn")})
    List<WebElement> credDeleteBtns;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        logoutButton.submit();
    }

    public void uploadFile(String filepath) throws InterruptedException {
        filesTab.click();
        fileUploadButton.sendKeys(filepath);
        Thread.sleep(500);
        fileSubmit.click();
    }

    public boolean listContainsFile(String filename) {
        filesTab.click();
        return filenames.stream()
                .anyMatch(fileElement -> fileElement.getText().equals(filename));
    }

    public void uploadNote(WebDriver driver, String noteTitle, String noteDescription) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(notesTab));
        Thread.sleep(500);
        notesTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addNoteBtn));
        addNoteBtn.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(noteTitleInput));
        noteTitleInput.sendKeys(noteTitle);
        noteDescriptionInput.sendKeys(noteDescription);
        noteSaveChangesBtn.click();
    }

    public boolean listContainsNote(WebDriver driver, String noteTitle, String noteDescription) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(notesTab));
        Thread.sleep(500);
        notesTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addNoteBtn));
        final int index = noteTitles.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList())
                .indexOf(noteTitle);
        if (index == -1) {
            return false;
        }
        return noteDescs.size() >= index
                && noteDescs.get(index).getText().equals(noteDescription);
    }

    public void editFirstNote(WebDriver driver, String noteTitle, String noteDescription) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(notesTab));
        Thread.sleep(500);
        notesTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addNoteBtn));
        noteEditBtns.get(0).click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(noteTitleInput));
        noteTitleInput.sendKeys(noteTitle);
        noteDescriptionInput.sendKeys(noteDescription);
        noteSaveChangesBtn.click();
    }

    public void deleteFirstNote(WebDriver driver) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(notesTab));
        Thread.sleep(500);
        notesTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addNoteBtn));
        noteDeleteBtns.get(0).click();
    }

    public int noteCount(WebDriver driver) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(notesTab));
        Thread.sleep(500);
        notesTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addNoteBtn));
        return noteTitles.size();
    }

    public void uploadCredential(WebDriver driver, String url, String username, String password) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(credentialsTab));
        Thread.sleep(500);
        credentialsTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addCredentialBtn));
        addCredentialBtn.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(credentialUrlInput));
        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.sendKeys(username);
        credentialPassInput.sendKeys(password);
        credentialSaveChangesBtn.click();
    }

    public boolean listContainsCredential(WebDriver driver, String url, String username) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(credentialsTab));
        Thread.sleep(500);
        credentialsTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addCredentialBtn));
        final int index = credentialUrls.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList())
                .indexOf(url);
        if (index == -1) {
            return false;
        }
        return credentialUsers.size() >= index
                && credentialUsers.get(index).getText().equals(username);
    }

    public void editFirstCredential(WebDriver driver, String url, String username, String password) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(credentialsTab));
        Thread.sleep(500);
        credentialsTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addCredentialBtn));
        credEditBtns.get(0).click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(credentialUrlInput));
        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.sendKeys(username);
        credentialPassInput.sendKeys(password);
        credentialSaveChangesBtn.click();
    }

    public int credentialCount(WebDriver driver) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(credentialsTab));
        Thread.sleep(500);
        credentialsTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addCredentialBtn));
        return credentialUrls.size();
    }

    public void deleteFirstCred(WebDriver driver) throws InterruptedException {
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(credentialsTab));
        Thread.sleep(500);
        credentialsTab.click();
        new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(addCredentialBtn));
        credDeleteBtns.get(0).click();
    }
}
