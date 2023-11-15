package org.epam;

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
        quit();
    }

    @Test
    public void spotifyLoginTest() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open()
                .emptyCredentials()
                .assertLoginErrorMessage("Введите имя пользователя или адрес электронной почты из аккаунта Spotify.")
                .assertPasswordErrorMessage("Введите пароль.")

                .incorrectCredentials()
                .assertIncorrectLoginOrPasswordErrorMessage("Неправильное имя пользователя или пароль.")

                .correctCredentials()
                .assertAccountName("Luka");
    }

    @Test
    public void spotifyPlaylistTest() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open().logIn().createPlaylist().assertPlaylistName()
                .editPlaylistName().assertEditedPlaylistName("My Favourite Playlist");
    }

    @Test
    public void addTrackToPlaylistTest() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open().logIn()
                .createPlaylist().searchAndAddToPlaylist()
                .assertTrackAddedToPlaylist("I Will Always Love You - Film Version");
    }

    @Test
    public void removeTrackFromPlaylistTest() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open().logIn()
                .createPlaylist().removeTrackFromPlaylist()
                .assertTrackIsRemoved();
    }

    @Test
    public void deletePlaylist() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open().logIn()
                .createPlaylist().deletePlaylist()
                .assertPlaylistIsDeleted();
    }
}
