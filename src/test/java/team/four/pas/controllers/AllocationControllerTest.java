package team.four.pas.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.restassured.RestAssured;
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
import team.four.pas.controllers.DTOs.*;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.services.AllocationService;
import team.four.pas.services.ResourceService;
import team.four.pas.services.UserService;

import java.io.File;
import java.time.Instant;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class AllocationControllerTest {

    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;
    @Autowired
    private AllocationService allocationService;
    @Autowired
    private ResourceService resourceService;

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
        try {
            userService.add(new UserAddDTO("MCorleone", "Michael", "Corleone", UserType.CLIENT));
            resourceService.addVM(new ResourceAddDTO(8, 16, 256));
        } catch (UserException | ResourceDataException e) {
            throw new RuntimeException(e);
        }
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
            ResourceDTO virtualMachine = resourceService.getAll().getLast();
            userService.activate(userService.getAll().getLast().id());
            UserDTO userDTO = userService.getAll().getLast();

            assertEquals(0, allocationService.getAll().size());

            assertDoesNotThrow(() -> allocationService.add(userDTO, virtualMachine, Instant.now()));

            assertEquals(1, allocationService.getAll().size());

            RestAssured.given()
                    .log().parameters()
                    .when()
                    .get("/allocations")
                    .then()
                    .log().body()
                    .statusCode(HttpStatus.OK.value())
                    .body("client.login", hasItem("MCorleone"));
        } catch (UserException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void createAllocationPass() {

    }
}