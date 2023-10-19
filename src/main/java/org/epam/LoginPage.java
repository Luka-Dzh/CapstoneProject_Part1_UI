package org.epam;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.sql.Time;
import java.time.Duration;


public class LoginPage extends BasePage {
    @FindBy(xpath = "//button[@data-testid=\"language-selection-button\"]")
    private WebElement openLanguages;
    @FindBy(xpath = "//button[@id=\"en\"]")
    private WebElement setLanguage;
    @FindBy(xpath = "//button[@data-testid='login-button']")
    private WebElement signInButton;
    @FindBy(xpath = "//input[@id='login-username']")
    private WebElement loginField;
    @FindBy(xpath = "//input[@id='login-password']")
    private WebElement passwordField;
    @FindBy(xpath = "//button[@id='login-button']")
    private WebElement signingIn;
    @FindBy(xpath = "//p[@class='Type__TypeElement-sc-goli3j-0 gkqrGP sc-ksBlkl klVIiA']")
    private WebElement fillInLoginMessage;
    @FindBy(xpath = "//span[contains(text(),\"Введите пароль\")]")
    private WebElement fillInPasswordMessage;
    @FindBy(xpath = "//span[@class='Message-sc-15vkh7g-0 dHbxKh']")
    private WebElement incorrectCredentialsMessage;
    @FindBy(xpath = "//button[@class='Button-sc-1dqy6lx-0 grWQsc encore-over-media-set SFgYidQmrqrFEVh65Zrg']")
    private WebElement profileCircle;
    @FindBy(xpath = "//a[@class='wC9sIed7pfp47wZbmU6m']")
    private WebElement profileButton;
    @FindBy(xpath = "//span[@class='o4KVKZmeHsoRZ2Ltl078']")
    private WebElement accountName;


    protected LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get("https://open.spotify.com/");
        return this;
    }

    public void setLanguage() {
        openLanguages.click();
        setLanguage.click();
    }

    public void emptyCredentials() {
        signInButton.click();
        waitForElements(loginField);
        loginField.sendKeys("Something");
        passwordField.sendKeys("Something");
        loginField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        passwordField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
    }
    public void assertLoginErrorMessage(String expectedMessage) {
        String actualMessage = fillInLoginMessage.getText();
        Assert.assertEquals(actualMessage, expectedMessage, "Login error message doesn't match.");
    }

    public void assertPasswordErrorMessage(String expectedMessage) {
        String actualMessage = fillInPasswordMessage.getText();
        Assert.assertEquals(actualMessage, expectedMessage, "Password error message doesn't match.");
    }
    public void incorrectCredentials(){
        signInButton.click();
        waitForElements(loginField);
        loginField.sendKeys("Something");
        passwordField.sendKeys("Something");
        signingIn.click();
    }
    public void assertIncorrectLoginOrPasswordErrorMessage(String expectedMessage){
        waitForElements(incorrectCredentialsMessage);
        String actualMessage = incorrectCredentialsMessage.getText();
        Assert.assertEquals(actualMessage,expectedMessage,"Incorrect Credentials message does not match.");
    }

    public void correctCredentials(){
        loginField.clear();
        passwordField.clear();
        signInButton.click();
        waitForElements(loginField);
        loginField.sendKeys("dzhanibegashvili@gmail.com");
        passwordField.sendKeys("CapstoneProject");
        new WebDriverWait(driver, Duration.ofSeconds(1));
        signingIn.click();

        waitForElements(profileCircle);
        profileCircle.click();
        waitForElements(profileButton);
        profileButton.click();
    }
    public void assertAccountName(String expectedName){
        waitForElements(accountName);
        String actualName = accountName.getText();
        Assert.assertEquals(actualName,expectedName,"Incorrect Name.");
    }

    public PlaylistPage logIn(){
        waitForElements(signInButton);
        signInButton.click();
        waitForElements(loginField);
        loginField.sendKeys("dzhanibegashvili@gmail.com");
        passwordField.sendKeys("CapstoneProject");
        signingIn.click();

        return new PlaylistPage(driver);
    }
}
