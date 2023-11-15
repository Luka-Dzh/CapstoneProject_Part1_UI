package org.epam;

import org.epam.dto.Track;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.epam.dto.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

import static org.hamcrest.core.IsNull.nullValue;

public class ApiTest extends BaseTest {
    private static final String BASE_URL_USERS = "https://api.spotify.com/v1/users/31au4yk47miqldgzwqkv7dzwlbqq/playlists";
    private RequestSpecification requestSpecification;

    @BeforeMethod
    public void authSetUp() {
        requestSpecification = RestAssured.given().auth().oauth2("BQC1lfOGwCyPkDTQqs6DpF6s0OCZ3bqqOoJXE3VhfetBZHn3Tfxs" +
                "goEmjs6QBgTRv6IZeb2No5fAc0Lfw95B9oeezSAr_kqJbTx_ZkGDek6PvzYXqfl1ubTrLn_OvjGjtDAhJZoUJo6gJLi8zTQ3OdwS6AyTqZ-" +
                "lCo2XIqiItWGymZL1Ob-I8QV1EjIpNMIPiod76ekQyC4J1USLOCIV31dINcAa_MkhV2cqfBYgzLFVFi0QydUAT5b3WTB6DC-GSdKVAknexckKsg");
        setCommonParams(requestSpecification);
    }
    @AfterMethod
    public void closeDriver() {
        quit();
    }

    private void setCommonParams(RequestSpecification requestSpecification) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        requestSpecification.headers(headers);
    }

    @Test
    public void createPlaylistsTest() {
        Playlist expectedPlaylist = createPlaylist("Demo Playlist", "Sample playlist for Demo");
        Response response = requestSpecification.body(expectedPlaylist).expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post(BASE_URL_USERS);
        String name = response.jsonPath().getString("name");
        String description = response.jsonPath().getString("description");
        Boolean isPublic = response.jsonPath().getBoolean("public");
        Assert.assertEquals(name, "Demo Playlist");
        Assert.assertEquals(description, "Sample playlist for Demo");
        Assert.assertEquals(isPublic, false);
    }

    @Test
    public void editDetailsPlaylistTest() {
        Playlist initialPlaylist = createPlaylist("initial", "initial");
        Response response0 = requestSpecification.body(initialPlaylist).expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post(BASE_URL_USERS);
        String id = response0.jsonPath().getString("id");

        Playlist expectedPlaylist = createPlaylist("Updated Playlist", "Updated playlist description");
        Response response = requestSpecification.body(expectedPlaylist).expect().statusCode(HttpStatus.SC_OK).log().ifError()
                .when().put("https://api.spotify.com/v1/playlists/" + id);
    }

    @Test
    public void addItemPlaylistTest() {
        Playlist initialPlaylist = createPlaylist("initial", "initial");
        Response playlistWithoutItemResponse = requestSpecification.body(initialPlaylist).expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post(BASE_URL_USERS);
        String id = playlistWithoutItemResponse.jsonPath().getString("id");

        Track track = createPlaylistForUris("potify:track:4yIfjMoivhXnY9lZkoVntq", 0);
        Response response = requestSpecification.body(track).expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post("https://api.spotify.com/v1/playlists/" + id + "/tracks");
        String snapshotId = response.jsonPath().getString("snapshot_id");
        Assert.assertNotNull(snapshotId);
    }

    @Test
    public void removeFromPlaylistTest() {
        Playlist initialPlaylist = createPlaylist("initial", "initial");
        Response playlistWithoutItemResponse = requestSpecification.body(initialPlaylist).expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post(BASE_URL_USERS);
        String id = playlistWithoutItemResponse.jsonPath().getString("id");
        Track track = createPlaylistForUris("potify:track:4yIfjMoivhXnY9lZkoVntq", 0);
        Response response = requestSpecification.body(track).expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post("https://api.spotify.com/v1/playlists/" + id + "/tracks");
        String snapshotId = response.jsonPath().getString("snapshot_id");
        Assert.assertNotNull(snapshotId);

        Track trackDelete = createPlaylistForDeletion("potify:track:4yIfjMoivhXnY9lZkoVntq", "NywxOTAyOTJmMzI3ZjBiN2NkYzA4MTNlZGJlMzM3YjE2Y2MzZDRmOTA5");
        Response responseDeletion = requestSpecification.body(trackDelete).expect().statusCode(HttpStatus.SC_OK).log().ifError()
                .when().delete("https://api.spotify.com/v1/playlists/" + id + "/tracks");
        String snapshotIdForDeletion = responseDeletion.jsonPath().getString("snapshot_id");
        Assert.assertNotNull(snapshotIdForDeletion);
    }

    @Test
    public void addSongToPlaylistApiUi() {
        Playlist expectedPlaylist = createPlaylist("New Playlist", "New playlist description");
        requestSpecification.body(expectedPlaylist).expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post(BASE_URL_USERS);

        setUpWebDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open().logIn().searchAndAddToPlaylist();
    }

    @Test
    public void editDetailsApiUi() {
        editDetailsPlaylistTest();
        setUpWebDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open().logIn().assertEditedPlaylistName("initial");
    }

    private Playlist createPlaylist(String name, String description) {
        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setPublic(false);

        return playlist;
    }

    private Track createPlaylistForUris(String uri, int position) {
        Track track = new Track();
        track.setUris(uri);
        track.setPosition(position);

        return track;
    }
    private Track createPlaylistForDeletion(String uri, String snapshot_id) {
        Track track = new Track();
        track.setUris(uri);
        track.setSnapshot_id(snapshot_id);

        return track;
    }
}
