package team.four.pas.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.controllers.exceptions.service.AddVMException;
import team.four.pas.controllers.exceptions.service.DeleteVMException;
import team.four.pas.controllers.exceptions.service.UpdateVMException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class ResourceServiceTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static AnnotationConfigApplicationContext context;

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

        context = new AnnotationConfigApplicationContext(Config.class);
        ResourceRepository resourceRepository = context.getBean(ResourceRepository.class);
        AllocationRepository allocationRepository = context.getBean(AllocationRepository.class);

        resourceService = new ResourceServiceImpl(resourceRepository, allocationRepository);
        allocationService = new AllocationServiceImpl(allocationRepository, userService, resourceService, context.getBean(UserToDTO.class));
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
            resourceService.addVM(5, 12, 10);
            resourceService.addVM(5, 11, 10);

            assertEquals(2, resourceService.getAll().size());

            assertNotNull(resourceService.addVM(5, 12, 10));

            assertEquals(3, resourceService.getAll().size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void addNegative() {
        try {
            resourceService.addVM(5, 12, 10);
            resourceService.addVM(5, 11, 10);

            assertEquals(2, resourceService.getAll().size());

            assertThrows(AddVMException.class, () -> resourceService.addVM(-5, 12, 10));
            assertThrows(AddVMException.class, () -> resourceService.addVM(5, -12, 10));
            assertThrows(AddVMException.class, () -> resourceService.addVM(5, 12, -10));
            assertThrows(AddVMException.class, () -> resourceService.addVM(500, 12, 10));
            assertThrows(AddVMException.class, () -> resourceService.addVM(5, 2048, 10));
            assertThrows(AddVMException.class, () -> resourceService.addVM(5, 12, 10000000));
            assertThrows(AddVMException.class, () -> resourceService.addVM(0, 0, 0));

            assertEquals(2, resourceService.getAll().size());
        } catch (Exception e) {
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
            resourceService.addVM(5, 12, 10);
            VirtualMachine resource = resourceService.getAll().getFirst();
            assertEquals(resource, resourceService.findById(resource.getId()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findAll() {
        try {
            resourceService.addVM(5, 12, 10);
            resourceService.addVM(5, 12, 10);
            assertEquals(2, resourceService.getAll().size());
        } catch (Exception e) {
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
            resourceService.addVM(5, 12, 10);
            VirtualMachine vm = resourceService.getAll().getFirst();
            int ramBefore = vm.getRamGiB();
            int memoryBefore = vm.getStorageGiB();
            int cpuBefore = vm.getCpuNumber();

            assertNotEquals(10, cpuBefore);
            assertNotEquals(16, ramBefore);
            assertNotEquals(50, memoryBefore);

            assertNotNull(resourceService.updateVM(vm.getId(), 10, 16, 50));

            VirtualMachine updatedVM = resourceService.getAll().getFirst();
            assertEquals(10, updatedVM.getCpuNumber());
            assertEquals(16, updatedVM.getRamGiB());
            assertEquals(50, updatedVM.getStorageGiB());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void updateNegative() {
        try {
            resourceService.addVM(5, 12, 10);
            VirtualMachine vm = resourceService.getAll().getFirst();
            int ramBefore = vm.getRamGiB();
            int memoryBefore = vm.getStorageGiB();
            int cpuBefore = vm.getCpuNumber();

            assertNotEquals(-1, ramBefore);
            assertNotEquals(-1, memoryBefore);
            assertNotEquals(-1, cpuBefore);

            assertThrows(UpdateVMException.class, () -> resourceService.updateVM(vm.getId(), -1, -1, -1));

            VirtualMachine updatedVM = resourceService.getAll().getFirst();
            assertEquals(ramBefore, updatedVM.getRamGiB());
            assertEquals(memoryBefore, updatedVM.getStorageGiB());
            assertEquals(cpuBefore, updatedVM.getCpuNumber());
        } catch (Exception e) {
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
        try {
            resourceService.addVM(5, 12, 10);
            List<VirtualMachine> resources = resourceService.getAll();
            assertNotEquals(Collections.emptyList(), resources);

            VirtualMachine resource = resourceService.getAll().getLast();

            resourceService.deleteVM(resource.getId());
            assertNull(resourceService.findById(resource.getId()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

//    @Test
//    void deleteNegative() throws ServerException, KeyManagementException, BadAttributeValueExpException {
//        try {
//            String login = "HKwinto";
//
//            assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));
//            resourceService.addVM(12, 16, 256);
//
//            userService.activate(userService.findByLogin(login).id());
//            assertTrue(allocationService.add((Client) userService.findByLogin(login), resourceService.getAll().getFirst(), Instant.now()));
//
//            List<VirtualMachine> resources = resourceService.getAll();
//            assertNotEquals(Collections.emptyList(), resources);
//
//            VirtualMachine resource = resourceService.getAll().getFirst();
//
//            assertThrows(DeleteVMException.class, () -> resourceService.deleteVM(resource.getId()));
//            assertNotNull(resourceService.findById(resource.getId()));
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }
}
