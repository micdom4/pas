package team.four.pas.repositories;
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
import team.four.pas.repositories.implementation.MongoAllocationRepository;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
class AllocationRepositoryTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private AllocationRepository allocationRepository;
    private UserRepository userRepository;
    private ResourceRepository resourceRepository;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void each(){
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        context = new AnnotationConfigApplicationContext(Config.class);
        allocationRepository = context.getBean(MongoAllocationRepository.class);
        userRepository = context.getBean(UserRepository.class);
        resourceRepository = context.getBean(ResourceRepository.class);
    }

    @AfterEach
    void afterEach(){
        MongoDatabase database = context.getBean(MongoClient.class).getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

    @Test
    void addPositive() {
        int initial = allocationRepository.getAll().size();

        assertEquals(1,  allocationRepository.getActive(resourceRepository.getAll().getFirst()).size());

        assertTrue(allocationRepository.add((Client) userRepository.findByLogin("JKernel"),resourceRepository.getAll().getFirst(), Instant.now()));

        assertEquals(initial + 1, allocationRepository.getAll().size());
        assertEquals(2,  allocationRepository.getActive(resourceRepository.getAll().getFirst()).size());
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void getAll() {
        assertEquals(2, allocationRepository.getAll().size());
        System.out.println(allocationRepository.getAll());
    }

    @Test
    void findById() {
        VMAllocation vmAllocation = allocationRepository.getAll().getFirst();
        assertNotNull(vmAllocation);

        assertEquals(vmAllocation, allocationRepository.findById(vmAllocation.getId()));
    }

    @Test
    void findByIds() {
        VMAllocation vmAllocation = allocationRepository.getAll().getFirst();
        VMAllocation vmAllocation2 = allocationRepository.getAll().getLast();

        assertNotEquals(vmAllocation, vmAllocation2);

        System.out.println(vmAllocation);
        System.out.println(vmAllocation2);
        assertNotNull(vmAllocation);
        assertNotNull(vmAllocation2);

        List<VMAllocation> allocations = new ArrayList<VMAllocation>();
        allocations.add(vmAllocation);
        allocations.add(vmAllocation2);

        List<String> ids = new ArrayList<>();
        ids.add(vmAllocation.getId());
        ids.add(vmAllocation2.getId());

        assertEquals(allocations, allocationRepository.findById(ids));
    }

    /* U   U
       U   U
       U   U
       U   U
        UUU  */

    @Test
    void finishAllocation() {
        VirtualMachine vm = resourceRepository.getAll().getFirst();
        int initialActives = allocationRepository.getActive(vm).size();
        int initialPasts = allocationRepository.getPast(vm).size();
        VMAllocation vmAllocation = allocationRepository.getActive(vm).getFirst();

        assertTrue(allocationRepository.finishAllocation(vmAllocation.getId()));

        assertEquals(initialActives - 1, allocationRepository.getActive(vm).size());
        assertEquals(initialPasts + 1, allocationRepository.getPast(vm).size());
    }

    /* DDD
       D  D
       D  D
       D  D
       DDD  */

    @Test
    void deletePositive() {
        int initialSize = allocationRepository.getAll().size();
        VMAllocation activeAllocation = allocationRepository.getActive(resourceRepository.getAll().getFirst()).getFirst();

        assertNotNull(activeAllocation);

//        assertTrue(allocationRepository.delete(activeAllocation.getId()));

        assertEquals(initialSize - 1, allocationRepository.getAll().size());
    }

    @Test
    void deleteNegative() {
        int initialSize = allocationRepository.getAll().size();
        VMAllocation pastAllocation = allocationRepository.getPast(resourceRepository.getAll().getFirst()).getFirst();

        assertNotNull(pastAllocation);

//        assertFalse(allocationRepository.delete(pastAllocation.getId()));

        assertEquals(initialSize, allocationRepository.getAll().size());
    }
}