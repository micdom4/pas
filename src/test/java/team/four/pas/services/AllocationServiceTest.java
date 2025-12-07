package team.four.pas.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.DockerComposeResource;
import team.four.pas.exceptions.allocation.*;
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.exceptions.user.UserTypeException;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;

import java.io.File;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(DockerComposeResource.class)
@Testcontainers
class AllocationServiceTest {

    @Inject
    AllocationService allocationService;
    @Inject
    ResourceService resourceService;
    @Inject
    UserService userService;
    @Inject
    MongoClient mongoClient;

    private MongoDatabase database;

    @AfterEach
    void afterEach() {
        database = mongoClient.getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

    @Test
    void addPass() {
        try {
            String login = "HKwinto";
            assertNotNull(userService.add(new Client(null, login, "Henryk", "Kwinto", false)));
            VirtualMachine virtualMachine = (VirtualMachine) resourceService.addVM(new VirtualMachine(null, 8, 16, 256));

            int initialSize = allocationService.getAll().size();

            userService.activate(userService.findByLogin(login).getId());
            assertNotNull(allocationService.add(userService.findByLogin(login).getId(), virtualMachine.getId(), Instant.now()));

            assertEquals(initialSize + 1, allocationService.getAll().size());
        } catch (ResourceException | AllocationException | UserException e) {
            fail(e.getMessage());
        }
    }


    @Test
    void addFail() {
        try {
            String login = "HKwinto";
            assertNotNull(userService.add(new Client(null, login, "Henryk", "Kwinto", false)));
            VirtualMachine virtualMachine = (VirtualMachine) resourceService.addVM(new VirtualMachine(null, 8, 16, 256));

            int initialSize = allocationService.getAll().size();

            assertThrows(InactiveClientException.class, () -> allocationService.add(userService.findByLogin(login).getId(), virtualMachine.getId(), Instant.now()));

            userService.activate(userService.findByLogin(login).getId());

            allocationService.add(userService.findByLogin(login).getId(), virtualMachine.getId(), Instant.now());

            String login2 = "PBateman";
            assertNotNull(userService.add(new Manager(null, login2, "Patrick", "Bateman", true)));
            userService.activate(userService.findByLogin(login2).getId());

            assertThrows(UserTypeException.class, () -> allocationService.add(userService.findByLogin(login2).getId(), virtualMachine.getId(), Instant.now()));

            String login3 = "WWhite";
            assertNotNull(userService.add(new Client(null, login3, "Walter", "White", false)));
            userService.activate(userService.findByLogin(login3).getId());

            assertThrows(ResourceAlreadyAllocatedException.class, () -> allocationService.add(userService.findByLogin(login3).getId(), virtualMachine.getId(), Instant.now()));

            assertEquals(initialSize + 1, allocationService.getAll().size());
        } catch (ResourceException | UserException | AllocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getAll() {
        try {
            String login = "HKwinto";

            assertNotNull(userService.add(new Client(null, login, "Henryk", "Kwinto", false)));
            assertNotNull(resourceService.addVM(new VirtualMachine(null, 12, 16, 256)));

            userService.activate(userService.findByLogin(login).getId());
            assertNotNull(allocationService.add(userService.findByLogin(login).getId(), resourceService.getAll().getFirst().getId(), Instant.now()));

            assertEquals(1, allocationService.getAll().size());
            System.out.println(allocationService.getAll());
        } catch (UserException | ResourceException | AllocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findByIdPass() {
        try {
            String login = "HKwinto";

            assertNotNull(userService.add(new Client(null, login, "Henryk", "Kwinto", false)));
            assertNotNull(resourceService.addVM(new VirtualMachine(null, 12, 16, 256)));

            userService.activate(userService.findByLogin(login).getId());

            VMAllocation vmAllocation = allocationService.add(userService.findByLogin(login).getId(), resourceService.getAll().getLast().getId(), Instant.now());
            assertNotNull(vmAllocation);

            assertEquals(vmAllocation, allocationService.findById(vmAllocation.getId()));
        } catch (UserException | ResourceException | AllocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findByIdFailInvalidId() {
        assertThrows(AllocationIdException.class, () -> allocationService.findById(""));
        assertThrows(AllocationIdException.class, () -> allocationService.findById("wrongID"));
    }

    @Test
    void findByIdFailNotFound() {
        assertThrows(AllocationNotFoundException.class, () -> allocationService.findById(ObjectId.get().toHexString()));
    }

    @Test
    void finishAllocation() {
        try {
            String login = "HKwinto";
            assertNotNull(userService.add(new Client(null, login, "Henryk", "Kwinto", false)));

            VirtualMachine vm = (VirtualMachine) resourceService.addVM(new VirtualMachine(null, 12, 16, 256));

            userService.activate(userService.findByLogin(login).getId());

            VMAllocation vmAllocation = allocationService.add(userService.findByLogin(login).getId(), vm.getId(), Instant.now());

            assertNotNull(vmAllocation);

            assertEquals(1, allocationService.getActiveVm(vm.getId()).size());
            assertEquals(0, allocationService.getPastVm(vm.getId()).size());

            allocationService.finishAllocation(vmAllocation.getId());

            assertEquals(0, allocationService.getActiveVm(vm.getId()).size());
            assertEquals(1, allocationService.getPastVm(vm.getId()).size());
        } catch (UserException | ResourceException | AllocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deletePass() {
        try {
            userService.add(new Client(null, "SGood", "Saul", "Goodman", false));
            VirtualMachine vm = (VirtualMachine) resourceService.addVM(new VirtualMachine(null, 12, 16, 256));
            userService.activate(userService.findByLogin("SGood").getId());

            VMAllocation allocation = allocationService.add(userService.findByLogin("SGood").getId(), vm.getId(), Instant.now());

            assertEquals(1, allocationService.getAll().size());

            assertDoesNotThrow(() -> allocationService.delete(allocation.getId()));

            assertEquals(0, allocationService.getAll().size());
        } catch (UserException | ResourceException | AllocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteFail() {
        try {
            userService.add(new Client(null, "SGood", "Saul", "Goodman", false));
            VirtualMachine vm = resourceService.addVM(new VirtualMachine(null, 12, 16, 256));
            userService.activate(userService.findByLogin("SGood").getId());

            VMAllocation allocation = allocationService.add(userService.findByLogin("SGood").getId(), vm.getId(), Instant.now());

            assertEquals(1, allocationService.getAll().size());

            allocationService.finishAllocation(allocation.getId());

            assertEquals(1, allocationService.getPastVm(vm.getId()).size());

            assertThrows(AllocationNotActiveException.class, () -> allocationService.delete(allocation.getId()));

            assertEquals(1, allocationService.getAll().size());
        } catch (UserException | ResourceException | AllocationException e) {
            fail(e.getMessage());
        }
    }
}