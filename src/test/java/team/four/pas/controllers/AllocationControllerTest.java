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
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.DockerComposeResource;
import team.four.pas.controllers.DTOs.AllocationAddDTO;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.services.AllocationService;
import team.four.pas.services.ResourceService;
import team.four.pas.services.UserService;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.User;

import java.io.File;
import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(DockerComposeResource.class)
@Testcontainers
public class AllocationControllerTest {

    @Inject
    UserService userService;
    @Inject
    AllocationService allocationService;
    @Inject
    ResourceService resourceService;

    @Inject
    MongoClient mongoClient;

    private MongoDatabase database;

    @BeforeEach
    void beforeEach() {
        this.database = mongoClient.getDatabase("pas");
        try {
            userService.add(new Client(null, "MCorleone", "Michael", "Corleone", true));
            resourceService.addVM(new VirtualMachine(null, 8, 16, 256));
            userService.activate(userService.getAll().getLast().getId());
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
        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        User user = userService.getAll().getLast();

        assertEquals(0, allocationService.getAll().size());

        assertDoesNotThrow(() -> allocationService.add(user.getId(), virtualMachine.getId(), Instant.now()));

        assertEquals(1, allocationService.getAll().size());

        RestAssured.given()
                .log().parameters()
                .when()
                .get("/allocations")
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("client.login", hasItem("MCorleone"));
    }

    @Test
    void findAllocationPass() {
        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        User user = userService.getAll().getLast();

        assertDoesNotThrow(() -> allocationService.add(user.getId(), virtualMachine.getId(), Instant.now()));

        VMAllocation allocation = allocationService.getAll().getLast();

        RestAssured.given()
                .when()
                .get("/allocations/{id}", allocation.getId())
                .then()
                .statusCode(Status.OK.getStatusCode())
                .log().body()
                .body("client.login", equalTo("MCorleone"))
                .body("startTime", equalTo(allocation.getStartTime().toString()));
    }

    @Test
    void findAllocationFail() {
        String fakeId = ObjectId.get().toHexString();

        RestAssured.given()
                .when()
                .get("/allocations/{id}", fakeId)
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void createPass() {
        User client = userService.getAll().getFirst();
        VirtualMachine vm = resourceService.getAll().getLast();

        AllocationAddDTO requestBody = new AllocationAddDTO(client.getId(), vm.getId());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().body()
                .when()
                .post("/allocations")
                .then()
                .statusCode(Status.CREATED.getStatusCode())
                .log().body()
                .body("client.login", equalTo("MCorleone"));
    }

    @Test
    void createFailClientInactive() {
        assertDoesNotThrow(() -> userService.deactivate(userService.getAll().getLast().getId()));
        User client = userService.getAll().getFirst();
        assertFalse(client.isActive());

        VirtualMachine vm = resourceService.getAll().getLast();

        AllocationAddDTO requestBody = new AllocationAddDTO(client.getId(), vm.getId());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().body()
                .when()
                .post("/allocations")
                .then()
                .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    void createFailInvalidUserType() {
        assertDoesNotThrow(() -> userService.add(new Admin(null, "VCorleone", "Vito", "Corleone", true)));
        assertDoesNotThrow(() -> userService.activate(userService.getAll().getLast().getId()));

        User user = userService.getAll().getLast();
        assertEquals(Admin.class, user.getClass());

        VirtualMachine vm = resourceService.getAll().getLast();

        AllocationAddDTO requestBody = new AllocationAddDTO(user.getId(), vm.getId());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().body()
                .when()
                .post("/allocations")
                .then()
                .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void createFailResourceAlreadyAllocated() {
        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        User user = userService.getAll().getLast();

        assertDoesNotThrow(() -> allocationService.add(user.getId(), virtualMachine.getId(), Instant.now()));

        assertEquals(1, allocationService.getAll().size());

        AllocationAddDTO addDTO = new AllocationAddDTO(user.getId(), virtualMachine.getId());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addDTO)
                .when()
                .post("/allocations")
                .then()
                .statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    void getVmAllocationsPass() {
        User client = userService.getAll().getLast();
        VirtualMachine vm = resourceService.getAll().getLast();

        assertDoesNotThrow(() -> allocationService.add(client.getId(), vm.getId(), Instant.now()));

        RestAssured.given()
                .when()
                .get("/allocations/active/vm/{id}", vm.getId())
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("vm.id", hasItem(vm.getId()))
                .body("endTime", hasItem(nullValue()));

        RestAssured.given()
                .when()
                .get("/allocations/past/vm/{id}", vm.getId())
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("", empty());

        assertDoesNotThrow(() -> allocationService.finishAllocation(allocationService.getAll().getLast().getId()));

        RestAssured.given()
                .when()
                .get("/allocations/active/vm/{id}", vm.getId())
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("", empty());

        RestAssured.given()
                .when()
                .get("/allocations/past/vm/{id}", vm.getId())
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("vm.id", hasItem(vm.getId()))
                .body("endTime", hasItem(notNullValue()));
    }

    @Test
    void getClientAllocationsPass() {
        User client = userService.getAll().getLast();
        VirtualMachine vm = resourceService.getAll().getLast();

        assertDoesNotThrow(() -> allocationService.add(client.getId(), vm.getId(), Instant.now()));

        RestAssured.given()
                .when()
                .get("/allocations/active/client/{id}", client.getId())
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("client.id", hasItem(client.getId()))
                .body("endTime", hasItem(nullValue()));

        RestAssured.given()
                .when()
                .get("/allocations/past/client/{id}", client.getId())
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("", empty());

        assertDoesNotThrow(() -> allocationService.finishAllocation(allocationService.getAll().getLast().getId()));

        RestAssured.given()
                .when()
                .get("/allocations/active/client/{id}", client.getId())
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("", empty());

        RestAssured.given()
                .when()
                .get("/allocations/past/client/{id}", client.getId())
                .then()
                .log().body()
                .statusCode(Status.OK.getStatusCode())
                .body("client.id", hasItem(client.getId()))
                .body("endTime", hasItem(notNullValue()));
    }

    @Test
    void getVmAllocationsFail() {
        String fakeId = ObjectId.get().toHexString();

        RestAssured.given()
                .when()
                .get("/allocations/active/vm/{id}", fakeId)
                .then()
                .log().body()
                .statusCode(Status.NOT_FOUND.getStatusCode());

        RestAssured.given()
                .when()
                .get("/allocations/past/vm/{id}", fakeId)
                .then()
                .log().body()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void getClientAllocationsFail() {
        String fakeId = ObjectId.get().toHexString();

        RestAssured.given()
                .when()
                .get("/allocations/active/client/{id}", fakeId)
                .then()
                .log().body()
                .statusCode(Status.NOT_FOUND.getStatusCode());

        RestAssured.given()
                .when()
                .get("/allocations/past/client/{id}", fakeId)
                .then()
                .log().body()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void finishAllocationPass() {
        try {
            User client = userService.getAll().getLast();
            VirtualMachine vm = resourceService.getAll().getLast();

            assertDoesNotThrow(() -> allocationService.add(client.getId(), vm.getId(), Instant.now()));

            assertEquals(1, allocationService.getActiveVm(vm.getId()).size());
            assertEquals(0, allocationService.getPastVm(vm.getId()).size());

            VMAllocation allocation = allocationService.getActiveVm(vm.getId()).getLast();

            RestAssured.given()
                    .when()
                    .put("/allocations/{id}/finish", allocation.getId())
                    .then()
                    .statusCode(Status.OK.getStatusCode());

            assertEquals(0, allocationService.getActiveVm(vm.getId()).size());
            assertEquals(1, allocationService.getPastVm(vm.getId()).size());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void finishAllocationNotFound() {
        String fakeId = ObjectId.get().toHexString();

        RestAssured.given()
                .log().uri()
                .when()
                .put("/allocations/{id}/finish", fakeId)
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void finishAllocationInvalidId() {
        String fakeId = "f";

        RestAssured.given()
                .log().uri()
                .when()
                .put("/allocations/{id}/finish", fakeId)
                .then()
                .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void deleteAllocationPass() {
        User client = userService.getAll().getLast();
        VirtualMachine vm = resourceService.getAll().getLast();

        assertDoesNotThrow(() -> allocationService.add(client.getId(), vm.getId(), Instant.now()));

        VMAllocation allocation = allocationService.getAll().getLast();

        RestAssured.given()
                .when()
                .delete("/allocations/{id}", allocation.getId())
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void deleteAllocationNotFound() {
        String fakeId = ObjectId.get().toHexString();

        RestAssured.given()
                .when()
                .delete("/allocations/{id}", fakeId)
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void deleteAllocationFailNotActive() {
        User client = userService.getAll().getLast();
        VirtualMachine vm = resourceService.getAll().getLast();

        assertDoesNotThrow(() -> allocationService.add(client.getId(), vm.getId(), Instant.now()));

        assertDoesNotThrow(() -> allocationService.finishAllocation(allocationService.getAll().getLast().getId()));

        VMAllocation allocation = allocationService.getAll().getLast();
        assertNotNull(allocation.getEndTime());

        RestAssured.given()
                .when()
                .delete("/allocations/{id}", allocation.getId())
                .then()
                .statusCode(Status.CONFLICT.getStatusCode());
    }
}