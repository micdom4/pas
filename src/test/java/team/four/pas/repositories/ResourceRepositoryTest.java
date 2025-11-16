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
import team.four.pas.services.data.resources.VirtualMachine;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class ResourceRepositoryTest {

    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private ResourceRepository resourceRepository;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void each(){
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);


        context = new AnnotationConfigApplicationContext(Config.class);
        resourceRepository = context.getBean(ResourceRepository.class);
    }

    @AfterEach
    void afterEach(){
        MongoDatabase database = context.getBean(MongoClient.class).getDatabase("pas");
        database.getCollection("virtualMachines").deleteMany(new Document());
    }


  /* CCC
    C
    C
    C
     CCC  */

    @Test
    void add() {
        assertTrue(resourceRepository.addVM(5, 12, 10));
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void findById() {
        VirtualMachine resource = resourceRepository.getAll().getFirst();
        System.out.println(resource.getId());
        assertEquals(resource, resourceRepository.findById(resource.getId()));
    }

    @Test
    void findAll() {
        assertEquals(2, resourceRepository.getAll().size());
    }

    /* U   U
       U   U
       U   U
       U   U
        UUU  */

    @Test
    void updatePass() {
        VirtualMachine vm = resourceRepository.getAll().getFirst();
        int ramBefore = vm.getRamGiB();
        int memory = vm.getStorageGiB();
        int cpus = vm.getCpuNumber();

        assertNotEquals(-1, ramBefore);
        assertNotEquals(-1, memory);
        assertNotEquals(-1, cpus);

        assertTrue(resourceRepository.updateVM(vm.getId(), -1, -1 ,-1));

        VirtualMachine updatedVM = resourceRepository.getAll().getFirst();
        assertEquals(-1, updatedVM.getRamGiB());
        assertEquals(-1, updatedVM.getStorageGiB());
        assertEquals(-1, updatedVM.getCpuNumber());
    }

    /* DDD
       D  D
       D  D
       D  D
       DDD  */

    @Test
    void delete() {
        List<VirtualMachine> resources = resourceRepository.getAll();
        assertNotEquals(Collections.emptyList(), resources);

        VirtualMachine resource = resourceRepository.getAll().getFirst();

        assertTrue(resourceRepository.delete(resource.getId()));
        assertNull(resourceRepository.findById(resource.getId()));
    }
}