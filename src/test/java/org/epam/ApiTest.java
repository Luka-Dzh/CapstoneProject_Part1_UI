package org.epam;

import io.restassured.http.ContentType;
import org.epam.dto.Track;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.epam.dto.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;


public class ApiTest extends BaseTest {
    private static final String BASE_URL_USERS = "https://api.spotify.com/v1/users/31au4yk47miqldgzwqkv7dzwlbqq/playlists";
    private RequestSpecification requestSpecification;


    @BeforeClass
    public void setUpRest(){
        RestAssured.baseURI = "https://api.spotify.com/v1/";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeMethod
    public void authSetUp() {
        requestSpecification = RestAssured
                .given()
                .auth()
                .oauth2("BQBLVNkjPYh4tP_Io23n2Erq0lRGwj4R-WgVUcnSe4e_9XbZwI20b" +
                "BhEk8SVD6iUHYBjwAabCRcgp6FCF9r_a3xUrjhf3aSiM4OOlUSwTl3-y4ATuG037HFIBaNP9zdGxfp-fYvMfyM7z14P_j_KFMlWG0AQ" +
                "kpvsvR4Te4_MLJPRK-sHdp9cWxCu1QQEWVo3LIopR0RPECFcp2DziTcIlUrbaQ7aOJLSWekjWv46lntHWvgHXne39y1vcGyNTl26quTAJiBXh1V8CA");
        setCommonParams(requestSpecification);
    }
//    @AfterClass
//    public void closeDriver() {
//       //quit(); if quit - song disappears
//    }

    private void setCommonParams(RequestSpecification requestSpecification) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        requestSpecification.headers(headers);
    }

    @Test
    public void createPlaylistsTest() {
        var test = Playlist.builder().name("Demo Playlist")
                .description("Sample playlist for Demo")
                .build();

        var response = requestSpecification
                .body(test).when()
                .post(BASE_URL_USERS)
                .then()
                .statusCode(HttpStatus.SC_CREATED).contentType(ContentType.JSON)
                .extract().as(Playlist.class);

        Assert.assertEquals(response.getName(), "Demo Playlist");
        Assert.assertEquals(response.getDescription(), "Sample playlist for Demo");
        Assert.assertEquals(response.isPublic(), false);
    }

    @Test
    public void editDetailsPlaylistTest() {
        String id = createInitialPlaylist();

        Playlist expectedPlaylist = createPlaylist("Updated Playlist", "Updated playlist description");
        requestSpecification.body(expectedPlaylist).expect().statusCode(HttpStatus.SC_OK).log().ifError()
                .when().put("https://api.spotify.com/v1/playlists/" + id);
    }

    @Test
    public void addItemPlaylistTest() {
        String id = createInitialPlaylist();

        //Track track = createPlaylistForUris("potify:track:4yIfjMoivhXnY9lZkoVntq", 0);
        String requestBody = "{\n" +
                "\"uris\": [\n" +
                "\"spotify:track:4yIfjMoivhXnY9lZkoVntq\"\n" +
                "],\n" +
                "\"position\": 0\n" +
                "}";
        Response response = requestSpecification.body(requestBody).expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post("https://api.spotify.com/v1/playlists/" + id + "/tracks");
        String snapshotId = response.jsonPath().getString("snapshot_id");
        Assert.assertNotNull(snapshotId);
    }

    @Test
    public void removeFromPlaylistTest() {
        String id = createInitialPlaylist();
        final String trackId = "4yIfjMoivhXnY9lZkoVntq";

        String requestBody = "{\n" +
                "\"uris\": [\n" +
                "\"spotify:track:" + trackId + "\"\n" +
                "],\n" +
                "\"position\": 0\n" +
                "}";
        Response response = requestSpecification.body(requestBody).expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post("https://api.spotify.com/v1/playlists/" + id + "/tracks");
        String snapshotId = response.jsonPath().getString("snapshot_id");
        Assert.assertNotNull(snapshotId);

        //Track trackDelete = createPlaylistForDeletion("potify:track:4yIfjMoivhXnY9lZkoVntq", "NywxOTAyOTJmMzI3ZjBiN2NkYzA4MTNlZGJlMzM3YjE2Y2MzZDRmOTA5");
        String requestBodyDeletion = "{\n" +
                " \"tracks\": [\n" +
                " {\n" +
                " \"uri\": \"spotify:track:" + trackId + "\"\n" +
                " }\n" +
                " ],\n" +
                " \"snapshot_id\":\"NywxOTAyOTJmMzI3ZjBiN2NkYzA4MTNlZGJlMzM3YjE2Y2MzZDRmOTA5\"\n" +
                "}";
        Response responseDeletion = requestSpecification.body(requestBodyDeletion).expect().statusCode(HttpStatus.SC_OK).log().ifError()
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
        loginPage
                .open()
                .goToPlaylistWithoutLogging()
                .assertEditedPlaylistName("initial"); //it will be Updated after a few minutes
    }
    public String createInitialPlaylist() {
        Playlist initialPlaylist = createPlaylist("initial", "initial");
        Response response = requestSpecification
                .body(initialPlaylist)
                .expect().statusCode(HttpStatus.SC_CREATED).log().ifError()
                .when().post(BASE_URL_USERS);
        return response.jsonPath().getString("id");
    }

    private Playlist createPlaylist(String name, String description) {
        return new Playlist(name, description, false);
    }

    private Track createPlaylistForUris(String uri, int position) {
        Track track = new Track();
        track.setUri(uri);
        track.setPosition(position);

        return track;
    }
    private Track createPlaylistForDeletion(String uri, String snapshot_id) {
        Track track = new Track();
        track.setUri(uri);
        track.setSnapshot_id(snapshot_id);

        return track;
    }
}
