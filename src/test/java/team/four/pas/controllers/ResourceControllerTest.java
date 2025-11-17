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
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.controllers.exceptions.service.AddVMException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.AllocationService;
import team.four.pas.services.ResourceService;
import team.four.pas.services.UserService;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.implementation.AllocationServiceImpl;
import team.four.pas.services.implementation.ResourceServiceImpl;
import team.four.pas.services.implementation.UserServiceImpl;
import team.four.pas.services.mappers.UserToDTO;

import javax.management.BadAttributeValueExpException;
import java.io.File;
import java.rmi.ServerException;
import java.security.KeyManagementException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
public class ResourceControllerTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static ResourceRepository resourceRepository;
    private static AllocationRepository allocationRepository;
    private static ResourceService resourceService;
    private static AllocationService allocationService;
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
        resourceRepository = context.getBean(ResourceRepository.class);
        allocationRepository = context.getBean(AllocationRepository.class);
        userRepository = context.getBean(UserRepository.class);
        allocationService = new AllocationServiceImpl(allocationRepository, context.getBean(UserToDTO.class));
        userService = new UserServiceImpl(userRepository, context.getBean(UserToDTO.class));
        resourceService = new ResourceServiceImpl(resourceRepository, allocationRepository);
        ResourceController resourceController = new ResourceControllerImpl(resourceService);
        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void afterEach(){
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

    @Test
    void getAll() throws AddVMException {
        resourceService.addVM(2, 4, 50);
        RestAssured.given()
                .log().parameters()
                .when()
                .get("/resources")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("cpuNumber", hasItem(2));
    }

    @Test
    void createPositiveVM() {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("cpus", 2);
        requestBody.put("ram", 4);
        requestBody.put("memory", 50);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().body()
                .when()
                .post("/resources")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().body()
                .body("cpuNumber", equalTo(2))
                .body("ramGiB", equalTo(4))
                .body("storageGiB", equalTo(50));
    }

    @Test
    void createNegativeVM() {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("cpus", -2);
        requestBody.put("ram", 4);
        requestBody.put("memory", 50);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().body()
                .when()
                .post("/resources")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .log().body();
    }

    @Test
    void updatePositiveVM() throws AddVMException {
        resourceService.addVM(2, 4, 50);
        VirtualMachine vm = resourceService.getAll().getLast();

        RestAssured.given()
                .when()
                .get("/resources/{vm}", vm.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("cpuNumber", equalTo(2));

        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("cpus", 10);
        requestBody.put("ram", 4);
        requestBody.put("memory", 50);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().body()
                .when()
                .put("/resources/{vm}", vm.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().body()
                .body("cpuNumber", equalTo(10));

        RestAssured.given()
                .when()
                .get("/resources/{vm}", vm.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("cpuNumber", equalTo(10));
    }

    @Test
    void updateNegativeVM() throws AddVMException {
        resourceService.addVM(2, 4, 50);
        VirtualMachine vm = resourceService.getAll().getLast();

        RestAssured.given()
                .when()
                .get("/resources/{vm}", vm.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("cpuNumber", equalTo(2));

        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("cpus", -10);
        requestBody.put("ram", 4);
        requestBody.put("memory", 50);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().body()
                .when()
                .put("/resources/{vm}", vm.getId())
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        RestAssured.given()
                .when()
                .get("/resources/{vm}", vm.getId())
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("cpuNumber", equalTo(2));
    }

    @Test
    void deletePositiveVM() throws AddVMException {
        resourceService.addVM(2, 4, 50);
        VirtualMachine vm = resourceService.getAll().getLast();

        RestAssured.given()
                .when()
                .delete("/resources/{vm}", vm.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertTrue(resourceService.getAll().isEmpty());
    }

    @Test
    void deleteNegativeVM() throws AddVMException, ServerException, KeyManagementException, BadAttributeValueExpException {
        resourceService.addVM(2, 4, 50);
        UserDTO client = userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.CLIENT));
        VirtualMachine vm = resourceService.getAll().getLast();
        allocationService.add(client, vm, Instant.now());


        RestAssured.given()
                .when()
                .delete("/resources/{vm}", vm.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        assertFalse(resourceService.getAll().isEmpty());
    }
}

