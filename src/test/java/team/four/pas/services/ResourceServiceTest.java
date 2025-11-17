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
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.implementation.ResourceServiceImpl;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class ResourceServiceTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private AnnotationConfigApplicationContext context;

    private ResourceService resourceService;

    @BeforeEach
    void before(){
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        context = new AnnotationConfigApplicationContext(Config.class);
        ResourceRepository resourceRepository = context.getBean(ResourceRepository.class);
        AllocationRepository allocationRepository = context.getBean(AllocationRepository.class);

        resourceService = new ResourceServiceImpl(resourceRepository, allocationRepository);
    }

    @AfterEach
    void after(){
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
        assertEquals(2, resourceService.getAll().size());

        assertTrue(resourceService.addVM(5, 12, 10));

        assertEquals(3, resourceService.getAll().size());
    }

    @Test
    void addNegative() {
        assertEquals(2, resourceService.getAll().size());

        assertFalse(resourceService.addVM(-5, 12, 10));
        assertFalse(resourceService.addVM(5, -12, 10));
        assertFalse(resourceService.addVM(5, 12, -10));
        assertFalse(resourceService.addVM(500, 12, 10));
        assertFalse(resourceService.addVM(5, 2048, 10));
        assertFalse(resourceService.addVM(5, 12, 10000000));
        assertFalse(resourceService.addVM(0, 0, 0));

        assertEquals(2, resourceService.getAll().size());
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void findById() {
        VirtualMachine resource = resourceService.getAll().getFirst();
        assertEquals(resource, resourceService.findById(resource.getId()));
    }

    @Test
    void findAll() {
        assertEquals(2, resourceService.getAll().size());
    }

    /* U   U
       U   U
       U   U
       U   U
        UUU  */

    @Test
    void updatePositive() {
        VirtualMachine vm = resourceService.getAll().getFirst();
        int ramBefore = vm.getRamGiB();
        int memoryBefore = vm.getStorageGiB();
        int cpuBefore = vm.getCpuNumber();

        assertNotEquals(10, cpuBefore);
        assertNotEquals(16, ramBefore);
        assertNotEquals(50, memoryBefore);

        assertTrue(resourceService.updateVM(vm.getId(), 10, 16 ,50));

        VirtualMachine updatedVM = resourceService.getAll().getFirst();
        assertEquals(10, updatedVM.getCpuNumber());
        assertEquals(16, updatedVM.getRamGiB());
        assertEquals(50, updatedVM.getStorageGiB());
    }

    @Test
    void updateNegative() {
        VirtualMachine vm = resourceService.getAll().getFirst();
        int ramBefore = vm.getRamGiB();
        int memoryBefore = vm.getStorageGiB();
        int cpuBefore = vm.getCpuNumber();

        assertNotEquals(-1, ramBefore);
        assertNotEquals(-1, memoryBefore);
        assertNotEquals(-1, cpuBefore);

        assertFalse(resourceService.updateVM(vm.getId(), -1, -1 ,-1));

        VirtualMachine updatedVM = resourceService.getAll().getFirst();
        assertEquals(ramBefore, updatedVM.getRamGiB());
        assertEquals(memoryBefore, updatedVM.getStorageGiB());
        assertEquals(cpuBefore, updatedVM.getCpuNumber());
    }

    /* DDD
       D  D
       D  D
       D  D
       DDD  */

    @Test
    void deletePositive() {
        List<VirtualMachine> resources = resourceService.getAll();
        assertNotEquals(Collections.emptyList(), resources);

        VirtualMachine resource = resourceService.getAll().getLast();

        assertTrue(resourceService.deleteVM(resource.getId()));
        assertNull(resourceService.findById(resource.getId()));
    }

    @Test
    void deleteNegative() {
        List<VirtualMachine> resources = resourceService.getAll();
        assertNotEquals(Collections.emptyList(), resources);

        VirtualMachine resource = resourceService.getAll().getFirst();

        assertFalse(resourceService.deleteVM(resource.getId()));
        assertNotNull(resourceService.findById(resource.getId()));
    }
}
