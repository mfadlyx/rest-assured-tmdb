package com.juaracoding;

import io.restassured.RestAssured;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TmdbTest {

    String myToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMGIyNTJiYzAwNTY1OTFjOWI3NDMzZjRhYWExNjc2YSIsIm5iZiI6MTcyOTk1MzA4OC43NTc4MjIsInN1YiI6IjY3MTkwOWRjNGJlMTU0NjllNzBkNTdlYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.19rQa4dDM1Hdp8a99uVBTUqrBbmJJKDicRXXxp-Morc";

    @BeforeClass
    public void setUp(){
        RestAssured.baseURI = "https://api.themoviedb.org/3";
    }

    //Scenario Test 1: Pengujian Endpoint Get Movie Now Playing
    @Test
    public void testMovieNewPlaying(){
        given()
                .header("Authorization", myToken)
                .when()
                .get("/movie/now_playing")
                .then()
                .statusCode(200)
                .log().all()
                .body("results.id[0]", equalTo(1184918))
                .body("results.title[0]", equalTo("The Wild Robot"));
    }

    @Test
    public void testMovieNewPlayingNegative() {
        given()
                .when()
                .get("/movie/now_playing")
                .then()
                .statusCode(401);
    }

    //Scenario Test 2: Pengujian Endpoint Get Movie Popular
    @Test
    public void testMoviePopular(){
        given()
                .header("Authorization", myToken)
                .when()
                .get("/movie/popular")
                .then()
                .statusCode(200)
                .log().all()
                .body("results.id[1]", equalTo(912649))
                .body("results.title[1]", equalTo("Venom: The Last Dance"));
    }

    @Test
    public void testMoviePopularNegative(){
        given()
                .when()
                .get("/movie/now_playing")
                .then()
                .statusCode(401);
    }

    //Scenario Test 3: Pengujian Endpoint Post Rating
    @Test
    public void testRating(){
        JSONObject request = new JSONObject();
        request.put("value", 8.5);
        System.out.println(request.toJSONString());

        String message = "The item/record was updated successfully.";

        given()
                .header("Authorization", myToken)
                .header("content-type", "application/json")
                .body(request.toJSONString())
                .when()
                .post("/movie/912649/rating")
                .then()
                .statusCode(201)
                .body("status_message", equalTo(message));

        System.out.println(message);
    }

    @Test
    public void testRatingNegative(){

        String messageInvalid = "Invalid parameters: Your request parameters are incorrect.";

        given()
                .header("Authorization", myToken)
                .header("content-type", "application/json")
                .when()
                .post("/movie/912649/rating")
                .then()
                .statusCode(400)
                .body("status_message", equalTo(messageInvalid));

        System.out.println(messageInvalid);
    }

    //Scenario Test 4: Pengujian Endpoint Get Movie Details
    @Test
    public void testMovieDetails(){
        given()
                .header("Authorization", myToken)
                .when()
                .get("/movie/129")
                .then()
                .statusCode(200)
                .body("id", equalTo(129))
                .body("title", equalTo("Spirited Away"))
                .body("vote_count", equalTo(16423));
    }

    @Test
    public void testMovieDetailsNegative(){

        String messageInvalid = "Invalid id: The pre-requisite id is invalid or not found.";
        given()
                .queryParam("language", "en-us")
                .header("Authorization", myToken)
                .when()
                .get("/movie/movie_id")
                .then()
                .statusCode(404)
                .body("status_message", equalTo(messageInvalid));
    }
}
