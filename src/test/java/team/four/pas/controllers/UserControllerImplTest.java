package team.four.pas.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.UserService;
import team.four.pas.services.implementation.UserServiceImpl;
import team.four.pas.services.mappers.UserToDTO;

import java.io.File;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class UserControllerImplTest {

    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static AnnotationConfigApplicationContext context;
    private static UserService userService;
    private static MongoDatabase database;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";
        System.setProperty("pas.data.mongodb.uri", dynamicUri);
    }

    @BeforeAll
    static void each() {
        context = new AnnotationConfigApplicationContext(Config.class);

        UserRepository userRepository = context.getBean(UserRepository.class);
        userService = new UserServiceImpl(userRepository, context.getBean(UserToDTO.class));
        UserControllerImpl userController = new UserControllerImpl(userService);
        database = context.getBean(MongoClient.class).getDatabase("pas");
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
            userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN));

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
            userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN));
            String id = userService.findByLogin("BLis").id();
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
            userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN));
            userService.add(new UserAddDTO("BLis2", "Bartosz", "Lis", UserType.ADMIN));
            userService.add(new UserAddDTO("KLrol", "Bartosz", "Lis", UserType.ADMIN));

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
    void activateDeactivateUser() {
        try {
            userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN));

            String id = userService.findByLogin("BLis").id();

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
                    .post("/users/{id}/activate", id)
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
                    .post("/users/{id}/deactivate", id)
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
        UserAddDTO newUser = new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN);

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
        UserAddDTO existingUser = new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN);

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
        UserAddDTO existingUser = new UserAddDTO("BLis", "bLISSASD", "Lis", UserType.ADMIN);

        RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(existingUser)
                .when()
                .post("/users")
                .then()
                .log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}