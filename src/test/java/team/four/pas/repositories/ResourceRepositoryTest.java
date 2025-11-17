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
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ResourceRepositoryTest {

    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static ResourceRepository resourceRepository;
    private static AnnotationConfigApplicationContext context;
    private static MongoDatabase database;

    @BeforeAll
    static void each(){
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        context = new AnnotationConfigApplicationContext(Config.class);
        resourceRepository = context.getBean(ResourceRepository.class);
        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void afterEach(){
        database.getCollection("virtualMachines").deleteMany(new Document());
    }


  /* CCC
    C
    C
    C
     CCC  */

    @Test
    void add() {
        try {
            assertNotNull(resourceRepository.addVM(5, 12, 10));
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
            resourceRepository.addVM(5, 12, 10);
            VirtualMachine resource = resourceRepository.getAll().getFirst();
            assertEquals(resource, resourceRepository.findById(resource.getId()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findAll() {
        try {
            resourceRepository.addVM(5, 12, 10);
            resourceRepository.addVM(6, 12, 10);
            assertEquals(2, resourceRepository.getAll().size());
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
    void updatePass() {
        try {
            resourceRepository.addVM(5, 12, 10);
            VirtualMachine vm = resourceRepository.getAll().getFirst();
            int ramBefore = vm.getRamGiB();
            int memory = vm.getStorageGiB();
            int cpus = vm.getCpuNumber();

            assertNotEquals(2, ramBefore);
            assertNotEquals(2, memory);
            assertNotEquals(2, cpus);

            assertNotNull(resourceRepository.updateVM(vm.getId(), 2, 2, 2));

            VirtualMachine updatedVM = resourceRepository.getAll().getFirst();
            assertEquals(2, updatedVM.getRamGiB());
            assertEquals(2, updatedVM.getStorageGiB());
            assertEquals(2, updatedVM.getCpuNumber());
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
    void delete() {
        try {
            resourceRepository.addVM(5, 12, 10);
            resourceRepository.addVM(6, 12, 10);

            List<VirtualMachine> resources = resourceRepository.getAll();
            assertNotEquals(Collections.emptyList(), resources);

            VirtualMachine resource = resourceRepository.getAll().getFirst();

            resourceRepository.delete(resource.getId());
            assertNull(resourceRepository.findById(resource.getId()));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}