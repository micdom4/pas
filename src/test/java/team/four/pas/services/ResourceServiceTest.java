package team.four.pas.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.exceptions.allocation.AllocationException;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.implementation.AllocationServiceImpl;
import team.four.pas.services.implementation.ResourceServiceImpl;
import team.four.pas.services.implementation.UserServiceImpl;

import java.io.File;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class ResourceServiceTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static ResourceService resourceService;
    private static AllocationService allocationService;
    private static UserService userService;
    private static MongoDatabase database;

    @BeforeAll
    static void before() {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        System.setProperty("pas.data.mongodb.uri", "mongodb://" + host + ":" + port + "/pas");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        ResourceRepository resourceRepository = context.getBean(ResourceRepository.class);
        AllocationRepository allocationRepository = context.getBean(AllocationRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        resourceService = new ResourceServiceImpl(resourceRepository, allocationRepository);
        userService = new UserServiceImpl(userRepository);
        allocationService = new AllocationServiceImpl(allocationRepository, userService, resourceService);

        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void after() {
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

            assertNotNull(resourceService.updateVM(vm.getId(),  10, 16, 50));

            VirtualMachine updatedVM = (VirtualMachine) resourceService.getAll().getFirst();
            assertEquals(10, updatedVM.getCpuNumber());
            assertEquals(16, updatedVM.getRamGiB());
            assertEquals(50, updatedVM.getStorageGiB());
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