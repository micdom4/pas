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
import team.four.pas.controllers.DTOs.ResourceAddDTO;
import team.four.pas.controllers.DTOs.ResourceDTO;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.exceptions.allocation.AllocationException;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.implementation.AllocationServiceImpl;
import team.four.pas.services.implementation.ResourceServiceImpl;
import team.four.pas.services.implementation.UserServiceImpl;
import team.four.pas.services.mappers.ResourceToDTO;
import team.four.pas.services.mappers.UserToDTO;

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
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

        ResourceRepository resourceRepository = context.getBean(ResourceRepository.class);
        AllocationRepository allocationRepository = context.getBean(AllocationRepository.class);

        resourceService = new ResourceServiceImpl(resourceRepository, allocationRepository, context.getBean(ResourceToDTO.class));
        allocationService = new AllocationServiceImpl(allocationRepository, userService, resourceService, context.getBean(UserToDTO.class), context.getBean(ResourceToDTO.class));
        userService = new UserServiceImpl(context.getBean(UserRepository.class), context.getBean(UserToDTO.class));

        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void after() {
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
            resourceService.addVM(new ResourceAddDTO(5, 12, 10));
            resourceService.addVM(new ResourceAddDTO(5, 11, 10));

            assertEquals(2, resourceService.getAll().size());

            assertNotNull(resourceService.addVM(new ResourceAddDTO(5, 12, 10)));

            assertEquals(3, resourceService.getAll().size());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void addNegative() {
        try {
            resourceService.addVM(new ResourceAddDTO(5, 12, 10));
            resourceService.addVM(new ResourceAddDTO(5, 11, 10));

            assertEquals(2, resourceService.getAll().size());

            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new ResourceAddDTO(-5, 12, 10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new ResourceAddDTO(5, -12, 10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new ResourceAddDTO(5, 12, -10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new ResourceAddDTO(500, 12, 10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new ResourceAddDTO(5, 2048, 10)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new ResourceAddDTO(5, 12, 10000000)));
            assertThrows(ResourceDataException.class, () -> resourceService.addVM(new ResourceAddDTO(0, 0, 0)));

            assertEquals(2, resourceService.getAll().size());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void findById() {
        try {
            resourceService.addVM(new ResourceAddDTO(5, 12, 10));
            ResourceDTO resource = resourceService.getAll().getFirst();
            assertEquals(resource, resourceService.findById(resource.id()));
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findAll() {
        try {
            resourceService.addVM(new ResourceAddDTO(5, 12, 10));
            resourceService.addVM(new ResourceAddDTO(5, 12, 10));
            assertEquals(2, resourceService.getAll().size());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    /* U   U
       U   U
       U   U
       U   U
        UUU  */

    @Test
    void updatePositive() {
        try {
            resourceService.addVM(new ResourceAddDTO(5, 12, 10));
            ResourceDTO vm = resourceService.getAll().getFirst();
            int ramBefore = vm.ramGiB();
            int storageBefore = vm.storageGiB();
            int cpuBefore = vm.cpuNumber();

            assertNotEquals(10, cpuBefore);
            assertNotEquals(16, ramBefore);
            assertNotEquals(50, storageBefore);

            assertNotNull(resourceService.updateVM(vm.id(), new ResourceAddDTO(10, 16, 50)));

            ResourceDTO updatedVM = resourceService.getAll().getFirst();
            assertEquals(10, updatedVM.cpuNumber());
            assertEquals(16, updatedVM.ramGiB());
            assertEquals(50, updatedVM.storageGiB());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void updateNegative() {
        try {
            resourceService.addVM(new ResourceAddDTO(5, 12, 10));
            ResourceDTO vm = resourceService.getAll().getFirst();
            int ramBefore = vm.ramGiB();
            int storageBefore = vm.storageGiB();
            int cpuBefore = vm.cpuNumber();

            assertNotEquals(-1, ramBefore);
            assertNotEquals(-1, storageBefore);
            assertNotEquals(-1, cpuBefore);

            assertThrows(ResourceDataException.class, () -> resourceService.updateVM(vm.id(), new ResourceAddDTO(-1, -1, -1)));

            ResourceDTO updatedVM = resourceService.getAll().getFirst();
            assertEquals(ramBefore, updatedVM.ramGiB());
            assertEquals(storageBefore, updatedVM.storageGiB());
            assertEquals(cpuBefore, updatedVM.cpuNumber());
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    /* DDD
       D  D
       D  D
       D  D
       DDD  */

    @Test
    void deletePositive() {
        // should work but doesn't rn :,)
        try {
            resourceService.addVM(new ResourceAddDTO(5, 12, 10));
            List<ResourceDTO> resources = resourceService.getAll();
            assertNotEquals(Collections.emptyList(), resources);

            ResourceDTO resource = resourceService.getAll().getLast();

            resourceService.deleteVM(resource.id());
            assertThrows(ResourceNotFoundException.class, () -> resourceService.findById(resource.id()));
        } catch (ResourceException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void deleteNegative() {
        try {
            String login = "HKwinto";

            assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));
            resourceService.addVM(new ResourceAddDTO(12, 16, 256));

            userService.activate(userService.findByLogin(login).id());
            allocationService.add(userService.findByLogin(login), resourceService.getAll().getFirst(), Instant.now());

            List<ResourceDTO> resources = resourceService.getAll();
            assertNotEquals(Collections.emptyList(), resources);

            ResourceDTO resource = resourceService.getAll().getFirst();

            assertThrows(ResourceStillAllocatedException.class, () -> resourceService.deleteVM(resource.id()));
            assertNotNull(resourceService.findById(resource.id()));
        } catch (UserException | ResourceException | AllocationException e) {
            fail(e.getMessage());
        }
    }
}
