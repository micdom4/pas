package team.four.pas.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.exceptions.allocation.*;
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.repositories.implementation.MongoAllocationRepository;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.implementation.AllocationServiceImpl;
import team.four.pas.services.implementation.ResourceServiceImpl;
import team.four.pas.services.implementation.UserServiceImpl;
import team.four.pas.services.mappers.UserToDTO;

import java.io.File;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@Testcontainers
class AllocationServiceTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static AllocationService allocationService;
    private static ResourceService resourceService;
    private static UserService userService;
    private static MongoDatabase database;

    @BeforeAll
    static void each() {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        AllocationRepository allocationRepository = context.getBean(MongoAllocationRepository.class);
        ResourceRepository resourceRepository = context.getBean(ResourceRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        resourceService = new ResourceServiceImpl(resourceRepository, allocationRepository);
        userService = new UserServiceImpl(userRepository, context.getBean(UserToDTO.class));
        allocationService = new AllocationServiceImpl(allocationRepository, userService, resourceService, context.getBean(UserToDTO.class));
        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void afterEach() {
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
        try {
            String login = "HKwinto";
            assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));
            VirtualMachine virtualMachine = resourceService.addVM(8, 16, 256);

            int initialSize = allocationService.getAll().size();

            userService.activate(userService.findByLogin(login).id());
            assertNotNull(allocationService.add(userService.findByLogin(login), virtualMachine, Instant.now()));

            assertEquals(initialSize + 1, allocationService.getAll().size());
        } catch (ResourceException | AllocationException | UserException e) {
            fail(e.getMessage());
        }
    }


    @Test
    void addNegative() {
        try {
            String login = "HKwinto";
            assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));
            VirtualMachine virtualMachine = resourceService.addVM(8, 16, 256);

            int initialSize = allocationService.getAll().size();

            assertThrows(InactiveClientException.class, () -> allocationService.add(userService.findByLogin(login), virtualMachine, Instant.now()));

            userService.activate(userService.findByLogin(login).id());

            allocationService.add(userService.findByLogin(login), virtualMachine, Instant.now());

            String login2 = "PBateman";
            assertNotNull(userService.add(new UserAddDTO(login2, "Patrick", "Bateman", UserType.MANAGER)));
            userService.activate(userService.findByLogin(login2).id());

            assertThrows(AllocationClientException.class, () -> allocationService.add(userService.findByLogin(login2), virtualMachine, Instant.now()));

            String login3 = "WWhite";
            assertNotNull(userService.add(new UserAddDTO(login3, "Walter", "White", UserType.CLIENT)));
            userService.activate(userService.findByLogin(login3).id());

            assertThrows(ResourceAlreadyAllocatedException.class, () -> allocationService.add(userService.findByLogin(login3), virtualMachine, Instant.now()));

            assertEquals(initialSize + 1, allocationService.getAll().size());
        } catch (ResourceException | UserException | AllocationException e) {
            fail(e.getMessage());
        }
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void getAll() {
        try {
            String login = "HKwinto";

            assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));
            assertNotNull(resourceService.addVM(12, 16, 256));

            userService.activate(userService.findByLogin(login).id());
            assertNotNull(allocationService.add(userService.findByLogin(login), resourceService.getAll().getFirst(), Instant.now()));

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

            assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));
            assertNotNull(resourceService.addVM(12, 16, 256));

            userService.activate(userService.findByLogin(login).id());

            VMAllocation vmAllocation = allocationService.add(userService.findByLogin(login), resourceService.getAll().getLast(), Instant.now());
            assertNotNull(vmAllocation);

            assertEquals(vmAllocation, allocationService.findById(vmAllocation.getId()));
        } catch (UserException | ResourceException | AllocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findByIdFail() {
        assertThrows(AllocationIdException.class, () -> allocationService.findById(""));
        assertThrows(AllocationNotFoundException.class, () -> allocationService.findById("notfound"));
    }

    @Test
    void finishAllocation() {
        try {
            String login = "HKwinto";
            assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));

            VirtualMachine vm = resourceService.addVM(12, 16, 256);

            userService.activate(userService.findByLogin(login).id());

            VMAllocation vmAllocation = allocationService.add(userService.findByLogin(login), vm, Instant.now());

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
}