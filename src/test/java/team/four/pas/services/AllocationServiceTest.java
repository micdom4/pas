package team.four.pas.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.repositories.implementation.MongoAllocationRepository;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;
import team.four.pas.services.data.users.User;
import team.four.pas.services.implementation.AllocationServiceImpl;
import team.four.pas.services.implementation.ResourceServiceImpl;
import team.four.pas.services.implementation.UserServiceImpl;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
class AllocationServiceTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private AnnotationConfigApplicationContext context;

    private AllocationService allocationService;
    private ResourceService resourceService;
    private UserService userService;

    @BeforeEach
    void each() {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        context = new AnnotationConfigApplicationContext(Config.class);
        AllocationRepository allocationRepository = context.getBean(MongoAllocationRepository.class);
        ResourceRepository resourceRepository = context.getBean(ResourceRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        allocationService = new AllocationServiceImpl(allocationRepository);
        resourceService = new ResourceServiceImpl(resourceRepository, allocationRepository);
        userService = new UserServiceImpl(userRepository);
    }

    @AfterEach
    void afterEach() {
        MongoDatabase database = context.getBean(MongoClient.class).getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

  /* CCC
    C
    C
    C
     CCC  */

    @Test
    void addPositive() {
        String login = "HKwinto";
        assertTrue(userService.add(login, "Henryk", "Kwinto", Client.class));
        assertTrue(resourceService.addVM(8, 16, 256));

        int initialSize = allocationService.getAll().size();

        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        userService.activate(userService.findByLogin(login).getId());

        assertTrue(allocationService.add((Client) userService.findByLogin(login), virtualMachine, Instant.now()));

        assertEquals(initialSize + 1, allocationService.getAll().size());
        assertEquals(1, allocationService.getActive(virtualMachine).size());
        assertEquals(0, allocationService.getPast(virtualMachine).size());
    }

    @Test
    void addNegativeDeactivatedClient() {
        String login = "HKwinto";
        assertTrue(userService.add(login, "Henryk", "Kwinto", Client.class));
        assertTrue(resourceService.addVM(8, 16, 256));

        int initialSize = allocationService.getAll().size();

        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        userService.deactivate(userService.findByLogin(login).getId());

        assertFalse(allocationService.add((Client) userService.findByLogin(login), virtualMachine, Instant.now()));

        assertEquals(initialSize, allocationService.getAll().size());
    }

    @Test
    void addNegativeResourceAllocated() {
        String login = "HKwinto";
        assertTrue(userService.add(login, "Henryk", "Kwinto", Client.class));
        assertTrue(resourceService.addVM(8, 16, 256));

        int initialSize = allocationService.getAll().size();

        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        userService.activate(userService.findByLogin(login).getId());

        assertTrue(allocationService.add((Client) userService.findByLogin(login), virtualMachine, Instant.now()));

        assertEquals(initialSize + 1, allocationService.getAll().size());
        assertEquals(1, allocationService.getActive(virtualMachine).size());

        assertFalse(allocationService.add((Client) userService.findByLogin("JKernel"), virtualMachine, Instant.now()));

        assertEquals(initialSize + 1, allocationService.getAll().size());
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void getAll() {
        assertEquals(2, allocationService.getAll().size());
        System.out.println(allocationService.getAll());
    }

    @Test
    void findById() {
        VMAllocation vmAllocation = allocationService.getAll().getFirst();
        assertNotNull(vmAllocation);

        assertEquals(vmAllocation, allocationService.findById(vmAllocation.getId()));
    }

    @Test
    void findByIds() {
        VMAllocation vmAllocation = allocationService.getAll().getFirst();
        VMAllocation vmAllocation2 = allocationService.getAll().getLast();

        assertNotEquals(vmAllocation, vmAllocation2);

        System.out.println(vmAllocation);
        System.out.println(vmAllocation2);
        assertNotNull(vmAllocation);
        assertNotNull(vmAllocation2);

        List<VMAllocation> allocations = new ArrayList<>();
        allocations.add(vmAllocation);
        allocations.add(vmAllocation2);

        List<String> ids = new ArrayList<>();
        ids.add(vmAllocation.getId());
        ids.add(vmAllocation2.getId());

        assertEquals(allocations, allocationService.findById(ids));
    }

    @Test
    void getActive() {
        assertEquals(1, allocationService.getActive((Client) userService.findByLogin("JKernel")).size());
    }

    @Test
    void getPast() {
        assertEquals(1, allocationService.getPast((Client) userService.findByLogin("JKernel")).size());
    }

    /* U   U
       U   U
       U   U
       U   U
        UUU  */

    @Test
    void finishAllocation() {
        VirtualMachine vm = resourceService.getAll().getFirst();
        int initialActives = allocationService.getActive(vm).size();
        int initialPasts = allocationService.getPast(vm).size();
        VMAllocation vmAllocation = allocationService.getActive(vm).getFirst();

        assertTrue(allocationService.finishAllocation(vmAllocation.getId()));

        assertEquals(initialActives - 1, allocationService.getActive(vm).size());
        assertEquals(initialPasts + 1, allocationService.getPast(vm).size());
    }

    /* DDD
       D  D
       D  D
       D  D
       DDD  */

    @Test
    void deletePositive() {
        int initialSize = allocationService.getAll().size();
        VMAllocation activeAllocation = allocationService.getActive(resourceService.getAll().getFirst()).getFirst();

        assertNotNull(activeAllocation);

        assertTrue(allocationService.delete(activeAllocation.getId()));

        assertEquals(initialSize - 1, allocationService.getAll().size());
    }

    @Test
    void deleteNegative() {
        int initialSize = allocationService.getAll().size();
        VMAllocation pastAllocation = allocationService.getPast(resourceService.getAll().getFirst()).getFirst();

        assertNotNull(pastAllocation);

        assertFalse(allocationService.delete(pastAllocation.getId()));

        assertEquals(initialSize, allocationService.getAll().size());
    }
}