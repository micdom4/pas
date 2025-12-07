package team.four.pas.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.DockerComposeResource;
import team.four.pas.exceptions.allocation.AllocationException;
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.services.AllocationService;
import team.four.pas.services.ResourceService;
import team.four.pas.services.UserService;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.User;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;

@QuarkusTest
@QuarkusTestResource(DockerComposeResource.class)
@Testcontainers
public class ResourceControllerTest {

    @Inject
    ResourceService resourceService;
    @Inject
    AllocationService allocationService;
    @Inject
    UserService userService;
    @Inject
    MongoClient mongoClient;

    private MongoDatabase database;

    @BeforeEach
    void beforeEach() {
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
            resourceService.addVM(new VirtualMachine(null, 2, 4, 50));
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
        RestAssured.given()
                .log().parameters()
                .when()
                .get("/resources")
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("cpuNumber", hasItem(2));
    }

    @Test
    void createPositiveVM() {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("cpuNumber", 2);
        requestBody.put("ramGiB", 4);
        requestBody.put("storageGiB", 50);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().body()
                .when()
                .post("/resources")
                .then()
                .statusCode(Status.CREATED.getStatusCode())
                .log().body()
                .body("cpuNumber", equalTo(2))
                .body("ramGiB", equalTo(4))
                .body("storageGiB", equalTo(50));
    }

    @Test
    void createNegativeVM() {
        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put("cpuNumber", -2);
        requestBody.put("ramGiB", 4);
        requestBody.put("storageGiB", 50);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().body()
                .when()
                .post("/resources")
                .then()
                .statusCode(Status.BAD_REQUEST.getStatusCode())
                .log().body();
    }

    @Test
    void updatePositiveVM() {
        try {
            resourceService.addVM(new VirtualMachine(null, 2, 4, 50));
            VirtualMachine vm = (VirtualMachine) resourceService.getAll().getLast();

            RestAssured.given()
                    .when()
                    .get("/resources/{vm}", vm.getId())
                    .then()
                    .log().body()
                    .statusCode(Status.OK.getStatusCode())
                    .body("cpuNumber", equalTo(2));

            Map<String, Integer> requestBody = new HashMap<>();
            requestBody.put("cpuNumber", 10);
            requestBody.put("ramGiB", 4);
            requestBody.put("storageGiB", 50);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .log().body()
                    .when()
                    .put("/resources/{vm}", vm.getId())
                    .then()
                    .statusCode(Status.OK.getStatusCode())
                    .log().body()
                    .body("cpuNumber", equalTo(10));

            RestAssured.given()
                    .when()
                    .get("/resources/{vm}", vm.getId())
                    .then()
                    .log().body()
                    .statusCode(Status.OK.getStatusCode())
                    .body("cpuNumber", equalTo(10));
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void updateNegativeVM() {
        try {
            resourceService.addVM(new VirtualMachine(null, 2, 4, 50));
            VirtualMachine vm = (VirtualMachine) resourceService.getAll().getLast();

            RestAssured.given()
                    .when()
                    .get("/resources/{vm}", vm.getId())
                    .then()
                    .log().body()
                    .statusCode(Status.OK.getStatusCode())
                    .body("cpuNumber", equalTo(2));

            Map<String, Integer> requestBody = new HashMap<>();
            requestBody.put("cpuNumber", -10);
            requestBody.put("ramGiB", 4);
            requestBody.put("storageGiB", 50);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .log().body()
                    .when()
                    .put("/resources/{vm}", vm.getId())
                    .then()
                    .statusCode(Status.BAD_REQUEST.getStatusCode());

            RestAssured.given()
                    .when()
                    .get("/resources/{vm}", vm.getId())
                    .then()
                    .log().body()
                    .statusCode(Status.OK.getStatusCode())
                    .body("cpuNumber", equalTo(2));
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deletePositiveVM() {
        try {
            resourceService.addVM(new VirtualMachine(null, 2, 4, 50));
            VirtualMachine vm = (VirtualMachine) resourceService.getAll().getLast();

            RestAssured.given()
                    .when()
                    .delete("/resources/{vm}", vm.getId())
                    .then()
                    .statusCode(Status.NO_CONTENT.getStatusCode());

            assertTrue(resourceService.getAll().isEmpty());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteNegativeVM() {
        try {
            resourceService.addVM(new VirtualMachine(null, 2, 4, 50));

            User client = userService.add(new Client(null, "BLis", "Bartosz", "Lis", false));
            userService.activate(client.getId());

            client = userService.findByLogin("BLis");

            VirtualMachine vm = resourceService.getAll().getLast();
            allocationService.add(client.getId(), vm.getId(), Instant.now());

            RestAssured.given()
                    .when()
                    .delete("/resources/{vm}", vm.getId())
                    .then()
                    .statusCode(Status.CONFLICT.getStatusCode());

            assertFalse(resourceService.getAll().isEmpty());
        } catch (ResourceException | UserException | AllocationException e) {
            fail(e.getMessage());
        }
    }
}