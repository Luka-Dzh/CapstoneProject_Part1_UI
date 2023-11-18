package org.epam;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.epam.dto.Playlist;
import org.testng.annotations.Test;


public class E2eTest extends ApiTest {
    @Test
    public void addSongToPlaylistApiUi() {
        var test = Playlist.builder().name("New Playlist")
                .description("New playlist description")
                .build();

        requestSpecification
                .body(test).when()
                .post(BASE_URL_USERS)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .extract().as(Playlist.class);

        setUpWebDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open()
                .logIn()
                .searchAndAddToPlaylist();
    }

    @Test
    public void editDetailsApiUi() {
        editDetailsPlaylistTest();
        setUpWebDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage
                .open()
                .goToPlaylistWithoutLogging()
                .assertEditedPlaylistName("initial"); //it will be Updated after a few minutes
    }
}
