package team.four.pas.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.restassured.RestAssured;
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
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.repositories.implementation.MongoAllocationRepository;
import team.four.pas.services.UserService;
import team.four.pas.services.implementation.UserServiceImpl;
import team.four.pas.services.mappers.UserToDTO;

import javax.management.BadAttributeValueExpException;
import java.io.File;
import java.rmi.ServerException;
import java.security.KeyManagementException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class UserControllerImplTest {

    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static UserRepository userRepository;
    private static UserService userService;
    private static AnnotationConfigApplicationContext context;
    private static MongoDatabase database;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";
        System.setProperty("pas.data.mongodb.uri", dynamicUri);
    }

    @BeforeAll
    static void each(){
        context = new AnnotationConfigApplicationContext(Config.class);
        userRepository = context.getBean(UserRepository.class);
        userService = new UserServiceImpl(userRepository, context.getBean(UserToDTO.class));
        UserControllerImpl userController = new UserControllerImpl(userService);
        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void afterEach(){
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }
    @Test
    void getAll() throws ServerException, KeyManagementException, BadAttributeValueExpException {
        userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN));
        RestAssured.given()
                .log().parameters()
                .when()
                .get("/users")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("login", hasItem("BLis"));
    }

        /*
    @Test
    void getUser() {
    }

    @Test
    void findPersonByLogin() {
    }

    @Test
    void activateUser() {
    }

    @Test
    void deactivatePerson() {
    }

    @Test
    void createPerson() {
    }
         */
}