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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
class AllocationRepositoryTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private MongoAllocationRepository allocationRepository;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void each(){
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        context = new AnnotationConfigApplicationContext(Config.class);
        allocationRepository = context.getBean(MongoAllocationRepository.class);
    }

    @AfterEach
    void afterEach(){
        MongoDatabase database = context.getBean(MongoClient.class).getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

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

}