package team.four.pas.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.DockerComposeResource;
import team.four.pas.exceptions.allocation.AllocationException;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.io.File;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(DockerComposeResource.class)
@Testcontainers
public class ResourceServiceTest {

    @Inject
    ResourceService resourceService;
    @Inject
    AllocationService allocationService;
    @Inject
    UserService userService;
    @Inject
    MongoClient mongoClient;

    private MongoDatabase database;

    @AfterEach
    void after() {
        database = mongoClient.getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

    @Test
    void addPositive() {
        try {
            resourceService.addVM(new VirtualMachine(null, 5, 12, 10));
            resourceService.addVM(new VirtualMachine(null, 5, 11, 10));

            assertEquals(2, resourceService.getAll().size());

            assertNotNull(resourceService.addVM(new VirtualMachine(null, 5, 12, 10)));

            assertEquals(3, resourceService.getAll().size());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void addNegative() {
        try {
            resourceService.addVM(new VirtualMachine(null, 5, 12, 10));
            resourceService.addVM(new VirtualMachine(null, 5, 11, 10));

            assertEquals(2, resourceService.getAll().size());

            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new VirtualMachine(null, -5, 12, 10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new VirtualMachine(null, 5, -12, 10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new VirtualMachine(null, 5, 12, -10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new VirtualMachine(null, 500, 12, 10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new VirtualMachine(null, 5, 2048, 10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new VirtualMachine(null, 5, 12, 10000000)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new VirtualMachine(null, 0, 0, 0)));

            assertEquals(2, resourceService.getAll().size());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findById() {
        try {
            resourceService.addVM(new VirtualMachine(null, 5, 12, 10));
            VirtualMachine resource = (VirtualMachine) resourceService.getAll().getFirst();
            assertEquals(resource, resourceService.findById(resource.getId()));
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findAll() {
        try {
            resourceService.addVM(new VirtualMachine(null, 5, 12, 10));
            resourceService.addVM(new VirtualMachine(null, 5, 12, 10));
            assertEquals(2, resourceService.getAll().size());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void updatePositive() {
        try {
            resourceService.addVM(new VirtualMachine(null, 5, 12, 10));
            VirtualMachine vm = (VirtualMachine) resourceService.getAll().getFirst();
            int ramBefore = vm.getRamGiB();
            int storageBefore = vm.getStorageGiB();
            int cpuBefore = vm.getCpuNumber();

            assertNotEquals(10, cpuBefore);
            assertNotEquals(16, ramBefore);
            assertNotEquals(50, storageBefore);

            assertNotNull(resourceService.updateVM(vm.getId(), 10, 16, 50));

            VirtualMachine updatedVM = (VirtualMachine) resourceService.getAll().getFirst();
            assertEquals(10, updatedVM.getCpuNumber());
            assertEquals(16, updatedVM.getRamGiB());
            assertEquals(50, updatedVM.getStorageGiB());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void updateNegative() {
        try {
            resourceService.addVM(new VirtualMachine(null, 5, 12, 10));
            VirtualMachine vm = (VirtualMachine) resourceService.getAll().getFirst();
            int ramBefore = vm.getRamGiB();
            int storageBefore = vm.getStorageGiB();
            int cpuBefore = vm.getCpuNumber();

            assertNotEquals(-1, ramBefore);
            assertNotEquals(-1, storageBefore);
            assertNotEquals(-1, cpuBefore);

            assertThrows(ResourceDataException.class, () -> resourceService.updateVM(vm.getId(), -1, -1, -1));

            VirtualMachine updatedVM = (VirtualMachine) resourceService.getAll().getFirst();
            assertEquals(ramBefore, updatedVM.getRamGiB());
            assertEquals(storageBefore, updatedVM.getStorageGiB());
            assertEquals(cpuBefore, updatedVM.getCpuNumber());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deletePositive() {
        try {
            resourceService.addVM(new VirtualMachine(null, 5, 12, 10));
            List<VirtualMachine> resources = (List) resourceService.getAll();
            assertNotEquals(Collections.emptyList(), resources);

            VirtualMachine resource = (VirtualMachine) resourceService.getAll().getLast();

            resourceService.deleteVM(resource.getId());
            assertThrows(ResourceNotFoundException.class, () -> resourceService.findById(resource.getId()));
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteNegative() {
        try {
            String login = "HKwinto";

            assertNotNull(userService.add(new Client(null, login, "Henryk", "Kwinto", false)));
            resourceService.addVM(new VirtualMachine(null, 12, 16, 256));

            userService.activate(userService.findByLogin(login).getId());
            allocationService.add(userService.findByLogin(login).getId(), resourceService.getAll().getFirst().getId(), Instant.now());

            List<VirtualMachine> resources = resourceService.getAll();
            assertNotEquals(Collections.emptyList(), resources);

            VirtualMachine resource = resourceService.getAll().getFirst();

            assertThrows(ResourceStillAllocatedException.class, () -> resourceService.deleteVM(resource.getId()));
            assertNotNull(resourceService.findById(resource.getId()));
        } catch (UserException | ResourceException | AllocationException e) {
            fail(e.getMessage());
        }
    }
}