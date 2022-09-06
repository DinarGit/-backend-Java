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



package com.geekbrains.builder;

import org.junit.jupiter.api.Test;

public class User {
    private final String name;
    private final String surname;
    private final String address;
    private final String email;

    private User(Builder builder) {
        name = builder.name;
        surname = builder.surname;
        address = builder.address;
        email = builder.email;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private String name;
        private String surname;
        private String address;
        private String email;

        public Builder setName(String surname) {
            this.name = name;
        }
}
    @Test
    void getRecipePositiveTest() {
        given()
                .when()
                .get("https://api.spoonacular.com/recipes/716429/information")
                .then()
                .spec(responseSpecification);
    }


    <?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.geekbrains.test.aug2022</groupId>
    <artifactId>backend-test</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>RELEASE</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-core</artifactId>
        <version>${jackson.version}</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-annotations</artifactId>
        <version>${jackson.version}</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>${jackson.version}</version>
    </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>
    </dependencies>
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

</project>
