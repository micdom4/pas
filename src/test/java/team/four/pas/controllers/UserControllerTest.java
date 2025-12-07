package team.four.pas.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.services.UserService;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Manager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserControllerTest {

    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;
    @Autowired
    private MongoClient mongoClient;

    private MongoDatabase database;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";
        System.setProperty("pas.data.mongodb.uri", dynamicUri);
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;

        this.database = mongoClient.getDatabase("pas");
    }

    @AfterEach
    void afterEach() {
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

    @Test
    void getAll() {
        try {
            userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true));

            RestAssured.given()
                    .log().parameters()
                    .when()
                    .get("/users")
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value())
                    .body("login", hasItem("BLis"));
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void getUser() {
        try {
            userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true));
            String id = userService.findByLogin("BLis").getId();
            RestAssured.given()
                    .log().parameters()
                    .when()
                    .get("/users/{id}", id)
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", equalTo("Bartosz"))
                    .body("type", equalTo("ADMIN"));
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void searchByLogin() {
        try {
            userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true));
            userService.add(new Admin(null, "BLis2", "Bartosz", "Lis", true));
            userService.add(new Admin(null, "KLrol", "Bartosz", "Lis", true));

            String login = "BL";

            RestAssured.given()
                    .log().parameters()
                    .when()
                    .get("/users/search/{login}", login)
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value())
                    .body("login", hasItems("BLis", "BLis2"))
                    .body("login", not(hasItems("KLrol")));
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void findByLoginPass() {
        assertDoesNotThrow(() -> userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));

        RestAssured.given()
                .when()
                .get("/users/login/{login}", "BLis")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().body()
                .body("login", equalTo("BLis"));
    }

    @Test
    void findByLoginFail() {
        RestAssured.given()
                .when()
                .get("/users/login/{login}", "BLis")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void activateDeactivateUser() {
        try {
            userService.add(new Admin(null, "BLis", "Bartosz", "Lis", false));

            String id = userService.findByLogin("BLis").getId();

            RestAssured.given()
                    .log().parameters()
                    .when()
                    .get("/users/{id}", id)
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", equalTo("Bartosz"))
                    .body("type", equalTo("ADMIN"))
                    .body("active", equalTo(false));

            RestAssured.given()
                    .log().parameters()
                    .when()
                    .put("/users/{id}/activate", id)
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value());

            RestAssured.given()
                    .log().parameters()
                    .when()
                    .get("/users/{id}", id)
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", equalTo("Bartosz"))
                    .body("type", equalTo("ADMIN"))
                    .body("active", equalTo(true));

            RestAssured.given()
                    .log().parameters()
                    .when()
                    .put("/users/{id}/deactivate", id)
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value());

            RestAssured.given()
                    .log().parameters()
                    .when()
                    .get("/users/{id}", id)
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", equalTo("Bartosz"))
                    .body("type", equalTo("ADMIN"))
                    .body("active", equalTo(false));
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void createUser() {
        Map<String, String> newUser = new HashMap<>();
        newUser.put("login", "BLis");
        newUser.put("name", "Bartosz");
        newUser.put("surname", "Lis");
        newUser.put("type", "ADMIN");

        RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value())
                .body("login", equalTo("BLis"))
                .body("name", equalTo("Bartosz"))
                .body("type", equalTo("ADMIN"));
    }

    @Test
    void createUserConflict() {
        Map<String, String> existingUser = new HashMap<>();
        existingUser.put("login", "BLis");
        existingUser.put("name", "Bartosz");
        existingUser.put("surname", "Lis");
        existingUser.put("type", "ADMIN");

        RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(existingUser)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(existingUser)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    void createUserBadRequest() {
        Map<String, String> badUser = new HashMap<>();
        badUser.put("login", "BLis");
        badUser.put("name", "bLISSASD");
        badUser.put("surname", "Lis");
        badUser.put("type", "ADMIN");

        RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(badUser)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void editUserPass() {
        try {
            assertDoesNotThrow(() -> userService.add(new Manager(null, "ASkywalker", "Anakin", "Skywalker", true)));
            String login = "ASkywalker";

            assertEquals("Skywalker", userService.findByLogin(login).getSurname());

            Map<String, String> mod = new HashMap<>();
            mod.put("surname", "Vader");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(mod)
                    .log().body()
                    .when()
                    .put("users/{id}", userService.findByLogin(login).getId())
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value())
                    .body("surname", equalTo("Vader"));

            assertEquals("Vader", userService.findByLogin(login).getSurname());
        } catch (UserException e) {
            fail(e.getMessage());
        }
    }
}