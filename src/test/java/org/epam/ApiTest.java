package org.epam;

import io.restassured.http.ContentType;
import org.epam.dto.Track;
import org.epam.dto.TrackDeletion;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.epam.dto.Playlist;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;


public class ApiTest extends BaseTest {
    static final String BASE_URL_USERS = "https://api.spotify.com/v1/users/31au4yk47miqldgzwqkv7dzwlbqq/playlists";
    final String trackId = "4yIfjMoivhXnY9lZkoVntq";
    protected RequestSpecification requestSpecification;


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
                .oauth2("BQCxaMfH2B1zrlFzYlfs6ATdQaij9ziBMVtUYyKn8CBDE_vRvqKBjdug865xKujun_AkHDRwoTLoiGTuF3KaKMZBi0y2ZG" +
                        "r5aBWXasz3wgl-rC88g0KMAsvKVmNiHSfo6mYWv-1WLgyre57JdijHxGYn2YC0AX94hzoGYu-cvjN1fUOXkYcrDmnojht1Hvu" +
                        "VCU_N_jyNQdKPma_eaSR0XOjlG8QGO5p4zd8cAIsvYewk4dK763qSHDrJA9xqk95McTA1B7m4eXuQsA");
        setCommonParams(requestSpecification);
    }

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
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .extract().as(Playlist.class);

        Assert.assertEquals(response.getName(), "Demo Playlist");
        Assert.assertEquals(response.getDescription(), "Sample playlist for Demo");
        Assert.assertFalse(response.isPublic());
    }

    @Test
    public void editDetailsPlaylistTest() {
        var initialPlaylist = Playlist.builder().name("initial")
                .description("initial")
                .build();

        var response = requestSpecification
                .body(initialPlaylist).when()
                .post(BASE_URL_USERS)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .extract().jsonPath();

        String id = response.getString("id");

        var updatedPlaylist = Playlist.builder().name("Updated Playlist")
                .description("Updated playlist description")
                .isPublic(false)
                .build();

        requestSpecification
                .body(updatedPlaylist).when()
                .put("playlists/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void addItemPlaylistTest() {
        var initialPlaylist = Playlist.builder().name("initial")
                .description("initial")
                .build();
        var response = requestSpecification
                .body(initialPlaylist).when()
                .post(BASE_URL_USERS)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .extract().jsonPath();

        String id = response.getString("id");

        var body = Track.builder().uris(trackBody())
                .position(0)
                .build();

        var addTrack = requestSpecification
                .body(body).when()
                .post("playlists/" + id + "/tracks")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .extract().jsonPath();

        Assert.assertNotNull(addTrack.getString("snapshot_id"));
    }

    @Test
    public void removeFromPlaylistTest() {

        var initialPlaylist = Playlist.builder().name("initial")
                .description("initial")
                .build();
        var response = requestSpecification
                .body(initialPlaylist).when()
                .post(BASE_URL_USERS)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .extract().jsonPath();

        String id = response.getString("id");


        var addTrack = requestSpecification
                .body(trackBody()).when()
                .post("playlists/" + id + "/tracks")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .contentType(ContentType.JSON)
                .extract().jsonPath();

        String snapId = addTrack.getString("snapshot_id");
        Assert.assertNotNull(snapId);

        var deletionBody = TrackDeletion.builder().uris(trackBody())
                .snapshot_id(snapId)
                .build();

        var deleteTrack = requestSpecification
                .body(deletionBody).when()
                .delete("playlists/" + id + "/tracks")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .extract().jsonPath();

        Assert.assertNotNull(deleteTrack.getString("snapshot_id"));
    }
    public ArrayList<String> trackBody(){

        ArrayList<String> urisList = new ArrayList<>();
        urisList.add("spotify:track:" + trackId);

        return urisList;
    }
}
