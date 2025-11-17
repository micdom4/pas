package team.four.pas.repositories;
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
import team.four.pas.controllers.exceptions.service.AddVMException;
import team.four.pas.repositories.implementation.MongoAllocationRepository;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.users.Client;

import javax.management.BadAttributeValueExpException;
import java.io.File;
import java.rmi.ServerException;
import java.security.KeyManagementException;
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

    private static MongoAllocationRepository allocationRepository;
    private static ResourceRepository resourceRepository;
    private static UserRepository userRepository;
    private static AnnotationConfigApplicationContext context;
    private static MongoDatabase database;

    @BeforeAll
    static void each(){
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        context = new AnnotationConfigApplicationContext(Config.class);
        allocationRepository = context.getBean(MongoAllocationRepository.class);
        resourceRepository = context.getBean(ResourceRepository.class);
        userRepository = context.getBean(UserRepository.class);
        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void afterEach(){
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

    @BeforeEach
    void initTwo() throws ServerException, KeyManagementException, BadAttributeValueExpException, AddVMException {
        resourceRepository.addVM(5, 12, 10);
        userRepository.add("BLis", "Bartosz", "Lis", Client.class);
    }

    @Test
    void getAll() {
        allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());
        allocationRepository.finishAllocation(allocationRepository.getAll().getFirst().getId());
        allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());
        assertEquals(2, allocationRepository.getAll().size());
    }

    @Test
    void findById() {
        allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());

        VMAllocation vmAllocation = allocationRepository.getAll().getFirst();
        assertNotNull(vmAllocation);

        assertEquals(vmAllocation, allocationRepository.findById(vmAllocation.getId()));
    }

    @Test
    void findByIds() {
        allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());
        allocationRepository.finishAllocation(allocationRepository.getAll().getFirst().getId());
        allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());

        VMAllocation vmAllocation = allocationRepository.getAll().getFirst();
        VMAllocation vmAllocation2 = allocationRepository.getAll().getLast();

        assertNotEquals(vmAllocation, vmAllocation2);

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