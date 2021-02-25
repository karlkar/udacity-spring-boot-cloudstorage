package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.ResultPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrl;

	private WebDriver driver;

	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;
	private ResultPage resultPage;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		baseUrl = "http://localhost:" + port + "/";
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get(baseUrl + "login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void cannotAccessHomeWhenNotLoggedIn() {
		driver.get(baseUrl + "home");
		Assertions.assertEquals(baseUrl + "login", driver.getCurrentUrl());
	}

	@Test
	public void getSignupPage() {
		driver.get(baseUrl + "signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void doesNotShowErrorOnLoginPage() {
		driver.get(baseUrl + "login");
		loginPage = new LoginPage(driver);
		assertNull(loginPage.getErrorMsg());
	}

	@Test
	public void doesNotShowSuccessOnLoginPage() {
		driver.get(baseUrl + "login");
		loginPage = new LoginPage(driver);
		assertFalse(loginPage.isSuccess());
	}

//	@Test
//	public void showsErrorWhenLoginFailed() {
//		driver.get(baseUrl + "login");
//		loginPage = new LoginPage(driver);
//		loginPage.signin("user", "invalid");
//
//		assertNotNull(loginPage.getErrorMsg());
//	}

	@Test
	public void canSignUp() {
		String username = randomizedUsername();
		String password = "password";
		String firstname = "firstname";
		String lastname = "lastname";

		driver.get(baseUrl + "signup");
		signupPage = new SignupPage(driver);

		signupPage.signup(username, password, firstname, lastname);

		assertEquals(baseUrl + "login", driver.getCurrentUrl());
	}

	@Test
	public void cannotSignUpWithoutPassword() {
		String username = randomizedUsername();
		String password = "";
		String firstname = "firstname";
		String lastname = "lastname";

		driver.get(baseUrl + "signup");
		signupPage = new SignupPage(driver);

		signupPage.signup(username, password, firstname, lastname);

		assertFalse(signupPage.isSuccess());
	}

	@Test
	public void cannotSignUpWithExistingUsername() {
		String username = randomizedUsername();
		String password = "password";
		String firstname = "firstname";
		String lastname = "lastname";

		driver.get(baseUrl + "signup");
		signupPage = new SignupPage(driver);

		signupPage.signup(username, password, firstname, lastname);
		assertEquals(baseUrl + "login", driver.getCurrentUrl());

		driver.get(baseUrl + "signup");
		new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(signupPage.submitButton));
		signupPage.signup(username, password, firstname, lastname);
		assertFalse(signupPage.isSuccess());
		assertEquals(signupPage.getErrorMsg(), "Username already exists in the database");
	}

	@Test
	public void canSingInAfterSignUp() throws InterruptedException {
		signUpAndSignIn();

		assertEquals(baseUrl + "home", driver.getCurrentUrl());
	}

	@Test
	public void canSingOutAfterSignUpAndSignIn() throws InterruptedException {
		signUpAndSignIn();

		homePage = new HomePage(driver);
		homePage.logout();
		new WebDriverWait(driver, 5).until(ExpectedConditions.urlToBe(baseUrl + "login"));
	}

	@Test
	public void cannotAccessHomeAfterSigningOut() throws InterruptedException {
		signUpAndSignIn();

		homePage = new HomePage(driver);
		homePage.logout();
		new WebDriverWait(driver, 5).until(ExpectedConditions.urlToBe(baseUrl + "login"));

		driver.get(baseUrl + "home");
		Assertions.assertEquals(baseUrl + "login", driver.getCurrentUrl());
	}

	private void signUpAndSignIn() throws InterruptedException {
		String username = randomizedUsername();
		String password = "password";

		signUpAndSignInWithCreds(username, password);
	}

	private void signUpAndSignInWithCreds(String username, String password) throws InterruptedException {
		String firstname = "firstname";
		String lastname = "lastname";

		driver.get(baseUrl + "signup");
		signupPage = new SignupPage(driver);
		new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(signupPage.submitButton));

		signupPage.signup(username, password, firstname, lastname);
		assertEquals(baseUrl + "login", driver.getCurrentUrl());

		loginPage = new LoginPage(driver);
		Thread.sleep(1000); // Nothing was helping here... Only Thread.sleep...
		new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(loginPage.submitButton));
		loginPage.signin(username, password);
		new WebDriverWait(driver, 5).until(ExpectedConditions.urlToBe(baseUrl + "home"));
	}

	@Test
	public void canSubmitAFileAfterSignUpAndSignIn() throws InterruptedException {
		signUpAndSignIn();

		uploadFile("/Users/ksionekk/Udacity/test.txt");
	}

	@Test
	public void cannotSubmitAFileWithTheSameNameAfterSignUpAndSignIn() throws InterruptedException {
		signUpAndSignIn();

		uploadFile("/Users/ksionekk/Udacity/test2.txt");

		driver.get(baseUrl + "home");
		homePage.uploadFile("/Users/ksionekk/Udacity/test2.txt");
		new WebDriverWait(driver, 15).until(ExpectedConditions.titleIs("Result"));
		assertFalse(resultPage.isSuccess());
		assertEquals("Name already exists!", resultPage.getErrorMsg());
	}

	@Test
	public void canSeeFileOnAListOfFilesAfterUploading() throws InterruptedException {
		signUpAndSignIn();

		uploadFile("/Users/ksionekk/Udacity/test3.txt");

		driver.get(baseUrl + "home");
		assertTrue(homePage.listContainsFile("test3.txt"));
	}

	private void uploadFile(String filepath) throws InterruptedException {
		homePage = new HomePage(driver);
		homePage.uploadFile(filepath);

		resultPage = new ResultPage(driver);
		new WebDriverWait(driver, 15).until(ExpectedConditions.titleIs("Result"));
		assertTrue(resultPage.isSuccess());
	}

	@Test
	public void canCreateANoteAndSeeItOnList() throws InterruptedException {
		signUpAndSignIn();

		uploadNote();

		driver.get(baseUrl + "home");
		assertTrue(homePage.listContainsNote(driver, "note title", "note description"));
	}

	@Test
	public void canUpdateANoteAndSeeItOnList() throws InterruptedException {
		signUpAndSignIn();

		uploadNote();

		driver.get(baseUrl + "home");
		assertTrue(homePage.listContainsNote(driver, "note title", "note description"));

		homePage.editFirstNote(driver, " yeah", " oh yeah");
		driver.get(baseUrl + "home");
		assertTrue(homePage.listContainsNote(driver, "note title yeah", "note description oh yeah"));
	}

	@Test
	public void canDeleteANoteAndNotSeeItOnList() throws InterruptedException {
		signUpAndSignIn();

		uploadNote();

		driver.get(baseUrl + "home");
		assertTrue(homePage.listContainsNote(driver, "note title", "note description"));

		homePage.deleteFirstNote(driver);
		driver.get(baseUrl + "home");
		assertEquals(0, homePage.noteCount(driver));
	}

	private void uploadNote() throws InterruptedException {
		homePage = new HomePage(driver);
		homePage.uploadNote(driver, "note title", "note description");

		resultPage = new ResultPage(driver);
		new WebDriverWait(driver, 15).until(ExpectedConditions.titleIs("Result"));
		assertTrue(resultPage.isSuccess());
	}

	@Test
	public void canCreateACredentialAndSeeItOnList() throws InterruptedException {
		signUpAndSignIn();

		uploadCredential();

		driver.get(baseUrl + "home");
		assertTrue(homePage.listContainsCredential(driver, "www.wp.pl", "username"));
	}

	@Test
	public void canUpdateACredentialAndSeeItOnList() throws InterruptedException {
		signUpAndSignIn();

		uploadCredential();

		driver.get(baseUrl + "home");
		assertTrue(homePage.listContainsCredential(driver, "www.wp.pl", "username"));

		homePage.editFirstCredential(driver, "l", "2", "o");
		driver.get(baseUrl + "home");
		assertTrue(homePage.listContainsCredential(driver, "www.wp.pll", "username2"));
	}

	@Test
	public void canDeleteACredentialAndNotSeeItOnList() throws InterruptedException {
		signUpAndSignIn();

		uploadCredential();

		driver.get(baseUrl + "home");
		assertTrue(homePage.listContainsCredential(driver, "www.wp.pl", "username"));

		homePage.deleteFirstCred(driver);
		driver.get(baseUrl + "home");
		assertEquals(0, homePage.credentialCount(driver));
	}

	private void uploadCredential() throws InterruptedException {
		homePage = new HomePage(driver);
		homePage.uploadCredential(driver, "www.wp.pl", "username", "password");

		resultPage = new ResultPage(driver);
		new WebDriverWait(driver, 15).until(ExpectedConditions.titleIs("Result"));
		assertTrue(resultPage.isSuccess());
	}

	@Test
	public void persistsTheDataBetweenSessions() throws InterruptedException {
		String username = randomizedUsername();
		String password = "pass";
		signUpAndSignInWithCreds(username, password);

		uploadFile("/Users/ksionekk/Udacity/test4.txt");
		driver.get(baseUrl + "home");
		uploadNote();
		driver.get(baseUrl + "home");
		uploadCredential();

		driver.get(baseUrl + "home");
		homePage.logout();

		driver.get(baseUrl + "login");
		loginPage.signin(username, password);
		new WebDriverWait(driver, 5).until(ExpectedConditions.urlToBe(baseUrl + "home"));

		assertTrue(homePage.listContainsFile("test4.txt"));
		assertTrue(homePage.listContainsNote(driver, "note title", "note description"));
		assertTrue(homePage.listContainsCredential(driver, "www.wp.pl", "username"));
	}

	private String randomizedUsername() {
		return "username" + new Random().nextInt();
	}
}
