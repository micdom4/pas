package team.four.pas.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.bson.Document;
import org.bson.types.ObjectId;
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
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.services.AllocationService;
import team.four.pas.services.ResourceService;
import team.four.pas.services.UserService;
import team.four.pas.services.data.allocations.VMAllocation;

import java.io.File;
import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

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

//    @BeforeEach
//    void beforeEach() {
//        RestAssured.port = port;
//
//        this.database = mongoClient.getDatabase("pas");
//        try {
//            userService.add(new UserAddDTO("MCorleone", "Michael", "Corleone", UserType.CLIENT));
//            resourceService.addVM(new ResourceAddDTO(8, 16, 256));
//            userService.activate(userService.getAll().getLast().id());
//        } catch (UserException | ResourceDataException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @AfterEach
//    void afterEach() {
//        database.getCollection("users").deleteMany(new Document());
//        database.getCollection("virtualMachines").deleteMany(new Document());
//        database.getCollection("vmAllocations").deleteMany(new Document());
//    }
//
//    @Test
//    void getAll() {
//        ResourceDTO virtualMachine = resourceService.getAll().getLast();
//        UserDTO userDTO = userService.getAll().getLast();
//
//        assertEquals(0, allocationService.getAll().size());
//
//        assertDoesNotThrow(() -> allocationService.add(userDTO, virtualMachine, Instant.now()));
//
//        assertEquals(1, allocationService.getAll().size());
//
//        RestAssured.given()
//                .log().parameters()
//                .when()
//                .get("/allocations")
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.OK.value())
//                .body("client.login", hasItem("MCorleone"));
//    }
//
//    @Test
//    void findAllocationPass() {
//        ResourceDTO virtualMachine = resourceService.getAll().getLast();
//        UserDTO userDTO = userService.getAll().getLast();
//
//        assertDoesNotThrow(() -> allocationService.add(userDTO, virtualMachine, Instant.now()));
//
//        VMAllocation allocation = allocationService.getAll().getLast();
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/{id}", allocation.getId())
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .log().body()
//                .body("client.login", equalTo("MCorleone"))
//                .body("startTime", equalTo(allocation.getStartTime().toString()));
//    }
//
//    @Test
//    void findAllocationFail() {
//        String fakeId = ObjectId.get().toHexString();
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/{id}", fakeId)
//                .then()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//    }
//
//    @Test
//    void createPass() {
//        UserDTO client = userService.getAll().getFirst();
//        ResourceDTO vm = resourceService.getAll().getLast();
//        AllocationAddDTO addDTO = new AllocationAddDTO(client, vm);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(addDTO)
//                .log().body()
//                .when()
//                .post("/allocations")
//                .then()
//                .statusCode(HttpStatus.CREATED.value())
//                .log().body()
//                .body("client.login", equalTo("MCorleone"));
//    }
//
//    @Test
//    void createFailClientInactive() {
//        assertDoesNotThrow(() -> userService.deactivate(userService.getAll().getLast().id()));
//        UserDTO client = userService.getAll().getFirst();
//        assertFalse(client.active());
//
//        ResourceDTO vm = resourceService.getAll().getLast();
//
//        AllocationAddDTO addDTO = new AllocationAddDTO(client, vm);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(addDTO)
//                .log().body()
//                .when()
//                .post("/allocations")
//                .then()
//                .statusCode(HttpStatus.FORBIDDEN.value());
//    }
//
//    @Test
//    void createFailInvalidUserType() {
//        assertDoesNotThrow(() -> userService.add(new UserAddDTO("VCorleone", "Vito", "Corleone", UserType.ADMIN)));
//        assertDoesNotThrow(() -> userService.activate(userService.getAll().getLast().id()));
//
//        UserDTO user = userService.getAll().getLast();
//        assertEquals(UserType.ADMIN, user.type());
//
//        ResourceDTO vm = resourceService.getAll().getLast();
//
//        AllocationAddDTO addDTO = new AllocationAddDTO(user, vm);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(addDTO)
//                .log().body()
//                .when()
//                .post("/allocations")
//                .then()
//                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
//    }
//
//    @Test
//    void createFailResourceAlreadyAllocated() {
//        ResourceDTO virtualMachine = resourceService.getAll().getLast();
//        UserDTO userDTO = userService.getAll().getLast();
//
//        assertDoesNotThrow(() -> allocationService.add(userDTO, virtualMachine, Instant.now()));
//
//        assertEquals(1, allocationService.getAll().size());
//
//        AllocationAddDTO addDTO = new AllocationAddDTO(userDTO, virtualMachine);
//
//        RestAssured.given()
//                .contentType(ContentType.JSON)
//                .body(addDTO)
//                .when()
//                .post("/allocations")
//                .then()
//                .statusCode(HttpStatus.CONFLICT.value());
//    }
//
//    @Test
//    void getVmAllocationsPass() {
//        UserDTO client = userService.getAll().getLast();
//        ResourceDTO vm = resourceService.getAll().getLast();
//
//        assertDoesNotThrow(() -> allocationService.add(client, vm, Instant.now()));
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/active/vm/{id}", vm.id())
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.OK.value())
//                .body("vm.id", hasItem(vm.id()))
//                .body("endTime", hasItem(nullValue()));
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/past/vm/{id}", vm.id())
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.OK.value())
//                .body("", empty());
//
//        assertDoesNotThrow(() -> allocationService.finishAllocation(allocationService.getAll().getLast().getId()));
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/active/vm/{id}", vm.id())
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.OK.value())
//                .body("", empty());
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/past/vm/{id}", vm.id())
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.OK.value())
//                .body("vm.id", hasItem(vm.id()))
//                .body("endTime", hasItem(notNullValue()));
//    }
//
//    @Test
//    void getClientAllocationsPass() {
//        UserDTO client = userService.getAll().getLast();
//        ResourceDTO vm = resourceService.getAll().getLast();
//
//        assertDoesNotThrow(() -> allocationService.add(client, vm, Instant.now()));
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/active/client/{id}", client.id())
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.OK.value())
//                .body("client.id", hasItem(client.id()))
//                .body("endTime", hasItem(nullValue()));
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/past/client/{id}", client.id())
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.OK.value())
//                .body("", empty());
//
//        assertDoesNotThrow(() -> allocationService.finishAllocation(allocationService.getAll().getLast().getId()));
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/active/client/{id}", client.id())
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.OK.value())
//                .body("", empty());
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/past/client/{id}", client.id())
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.OK.value())
//                .body("client.id", hasItem(client.id()))
//                .body("endTime", hasItem(notNullValue()));
//    }
//
//    @Test
//    void getVmAllocationsFail() {
//        String fakeId = ObjectId.get().toHexString();
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/active/vm/{id}", fakeId)
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/past/vm/{id}", fakeId)
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//    }
//
//    @Test
//    void getClientAllocationsFail() {
//        String fakeId = ObjectId.get().toHexString();
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/active/client/{id}", fakeId)
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//
//        RestAssured.given()
//                .when()
//                .get("/allocations/past/client/{id}", fakeId)
//                .then()
//                .log().body()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//    }
//
//    @Test
//    void finishAllocationPass() {
//        try {
//            UserDTO client = userService.getAll().getLast();
//            ResourceDTO vm = resourceService.getAll().getLast();
//
//            assertDoesNotThrow(() -> allocationService.add(client, vm, Instant.now()));
//
//            assertEquals(1, allocationService.getActiveVm(vm.id()).size());
//            assertEquals(0, allocationService.getPastVm(vm.id()).size());
//
//            VMAllocation allocation = allocationService.getActiveVm(vm.id()).getLast();
//
//            RestAssured.given()
//                    .when()
//                    .put("/allocations/{id}/finish", allocation.getId())
//                    .then()
//                    .statusCode(HttpStatus.OK.value());
//
//            assertEquals(0, allocationService.getActiveVm(vm.id()).size());
//            assertEquals(1, allocationService.getPastVm(vm.id()).size());
//        } catch (ResourceException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    void finishAllocationNotFound() {
//        String fakeId = ObjectId.get().toHexString();
//
//        RestAssured.given()
//                .log().uri()
//                .when()
//                .put("/allocations/{id}/finish", fakeId)
//                .then()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//    }
//
//    @Test
//    void finishAllocationInvalidId() {
//        String fakeId = "f";
//
//        RestAssured.given()
//                .log().uri()
//                .when()
//                .put("/allocations/{id}/finish", fakeId)
//                .then()
//                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
//    }
//
//    @Test
//    void deleteAllocationPass() {
//        UserDTO client = userService.getAll().getLast();
//        ResourceDTO vm = resourceService.getAll().getLast();
//
//        assertDoesNotThrow(() -> allocationService.add(client, vm, Instant.now()));
//
//        VMAllocation allocation = allocationService.getAll().getLast();
//
//        RestAssured.given()
//                .when()
//                .delete("/allocations/{id}", allocation.getId())
//                .then()
//                .statusCode(HttpStatus.NO_CONTENT.value());
//    }
//
//    @Test
//    void deleteAllocationNotFound() {
//        String fakeId = ObjectId.get().toHexString();
//
//        RestAssured.given()
//                .when()
//                .delete("/allocations/{id}", fakeId)
//                .then()
//                .statusCode(HttpStatus.NOT_FOUND.value());
//    }
//
//    @Test
//    void deleteAllocationFailNotActive() {
//        UserDTO client = userService.getAll().getLast();
//        ResourceDTO vm = resourceService.getAll().getLast();
//
//        assertDoesNotThrow(() -> allocationService.add(client, vm, Instant.now()));
//
//        assertDoesNotThrow(() -> allocationService.finishAllocation(allocationService.getAll().getLast().getId()));
//
//        VMAllocation allocation = allocationService.getAll().getLast();
//        assertNotNull(allocation.getEndTime());
//
//        RestAssured.given()
//                .when()
//                .delete("/allocations/{id}", allocation.getId())
//                .then()
//                .statusCode(HttpStatus.CONFLICT.value());
//    }
}