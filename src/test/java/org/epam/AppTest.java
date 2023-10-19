package org.epam;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class AppTest extends BaseTest {
    @BeforeMethod
    public void setUp() {
        setUpWebDriver();
    }
    @AfterMethod
    public void closeDriver() {
        //quit();
    }
    @Test
    public void spotifyLoginTest(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open().setLanguage();

        loginPage.emptyCredentials();
        loginPage.assertLoginErrorMessage("Введите имя пользователя или адрес электронной почты из аккаунта Spotify.");
        loginPage.assertPasswordErrorMessage("Введите пароль.");

        loginPage.incorrectCredentials();
        loginPage.assertIncorrectLoginOrPasswordErrorMessage("Неправильное имя пользователя или пароль.");

        loginPage.correctCredentials();
        loginPage.assertAccountName("Luka");
    }
    @Test
    public void spotifyPlaylistTest(){
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open().setLanguage();

        loginPage.logIn().createPlaylist();
    }

}
