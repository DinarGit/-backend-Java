package com.geekbrains.builder;

import jdk.jfr.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ResponseSpecification {

    @BeforeEach
    void beforeTest() {
        Object responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .expectHeader("Access-Control-Allow-Credentials", "true")
                .build();
    }
// Get recipe request
// авторизация в примере опущена, для повторения теста необходимо добавить параметр
    apiKey
    @Test
    void getRecipePositiveTest() {
        given()
                .when()
                .get("https://api.spoonacular.com/recipes/456698/information")
                .then()
                .spec(responseSpecification);
    }


    @BeforeEach
    void beforeTest() {
        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .expectHeader("Access-Control-Allow-Credentials", "true")
                .build();
    }

    RestAssured.responseSpecification = responseSpecification;

    RestAssured.responseSpecification =
            responseSpecification
                    .expect()
            .body(containsString("84578389"));

    @Test
    void getRecipePositiveTest() {
        given()
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information");

        RequestSpecification requestSpecification = null;
        @BeforeEach
        void beforeTest() {
            requestSpecification = new RequestSpecBuilder()
                    .addQueryParam("apiKey", apiKey)
                    .addQueryParam("includeNutrition", "false")
                    .log(LogDetail.ALL)
                    .build();
        }

        Object RestAssured;
        RestAssured.requestSpecification = requestSpecification;

        @Test
        void getRecipePositiveTest() {
            given()
                    .spec(requestSpecification)
                    .when()
                    .get("https://api.spoonacular.com/recipes/456698/information")
                    .then()
                    .spec(responseSpecification);
        }
    }



