//package team.four.pas.repositories;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//import org.bson.types.ObjectId;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.testcontainers.containers.DockerComposeContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import team.four.pas.Config;
//import team.four.pas.exceptions.resource.ResourceException;
//import team.four.pas.exceptions.resource.ResourceIdException;
//import team.four.pas.exceptions.resource.ResourceNotFoundException;
//import team.four.pas.services.data.resources.VirtualMachine;
//
//import java.io.File;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Testcontainers
//class ResourceRepositoryTest {
//
//    @Container
//    public static DockerComposeContainer<?> compose =
//            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
//                    .withExposedService("mongo", 27017);
//
//    private static ResourceRepository resourceRepository;
//    private static MongoDatabase database;
//
//    @BeforeAll
//    static void beforeAll() {
//        String host = compose.getServiceHost("mongo", 27017);
//        Integer port = compose.getServicePort("mongo", 27017);
//        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";
//
//        System.setProperty("pas.data.mongodb.uri", dynamicUri);
//
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
//        resourceRepository = context.getBean(ResourceRepository.class);
//        database = context.getBean(MongoClient.class).getDatabase("pas");
//    }
//
//    @AfterEach
//    void afterEach() {
//        database.getCollection("virtualMachines").deleteMany(new Document());
//    }
//
//
//  /* CCC
//    C
//    C
//    C
//     CCC  */
//
//    @Test
//    void add() {
//        assertNotNull(resourceRepository.addVM(5, 12, 10));
//    }
//
//    /* RRR
//       R  R
//       RRR
//       R  R
//       R   R */
//
//    @Test
//    void findByIdPass() {
//        try {
//            resourceRepository.addVM(5, 12, 10);
//            VirtualMachine resource = resourceRepository.getAll().getFirst();
//            assertEquals(resource, resourceRepository.findById(resource.getId()));
//        } catch (ResourceException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    void findByIdFailEmpty() {
//        assertThrows(ResourceIdException.class, () -> resourceRepository.findById(""));
//    }
//
//    @Test
//    void findByIdFailNotPresent() {
//        String id = new ObjectId().toHexString();
//        assertThrows(ResourceNotFoundException.class, () -> resourceRepository.findById(id));
//    }
//
//    @Test
//    void findAll() {
//        resourceRepository.addVM(5, 12, 10);
//        resourceRepository.addVM(6, 12, 10);
//        assertEquals(2, resourceRepository.getAll().size());
//    }
//
//    @Test
//    void findAllShouldReturnEmptyList() {
//        assertEquals(0, resourceRepository.getAll().size());
//    }
//
//    /* U   U
//       U   U
//       U   U
//       U   U
//        UUU  */
//
//    @Test
//    void updatePass() {
//        try {
//            resourceRepository.addVM(5, 12, 10);
//            VirtualMachine vm = resourceRepository.getAll().getFirst();
//            int ramBefore = vm.getRamGiB();
//            int memory = vm.getStorageGiB();
//            int cpus = vm.getCpuNumber();
//
//            assertNotEquals(2, ramBefore);
//            assertNotEquals(2, memory);
//            assertNotEquals(2, cpus);
//
//            assertNotNull(resourceRepository.updateVM(vm.getId(), 2, 2, 2));
//
//            VirtualMachine updatedVM = resourceRepository.getAll().getFirst();
//            assertEquals(2, updatedVM.getRamGiB());
//            assertEquals(2, updatedVM.getStorageGiB());
//            assertEquals(2, updatedVM.getCpuNumber());
//        } catch (ResourceException e) {
//            fail(e.getMessage());
//        }
//    }
//
//
//    @Test
//    void updateFailEmptyId() {
//        assertThrows(ResourceIdException.class, () -> resourceRepository.updateVM("", 2, 2, 2));
//    }
//
//    @Test
//    void updateFailNotPresent() {
//        String id = new ObjectId().toHexString();
//        assertThrows(ResourceNotFoundException.class, () -> resourceRepository.updateVM(id, 2, 2, 2));
//    }
//    /* DDD
//       D  D
//       D  D
//       D  D
//       DDD  */
//
//    @Test
//    void deletePass() {
//        try {
//            resourceRepository.addVM(5, 12, 10);
//            resourceRepository.addVM(6, 12, 10);
//
//            List<VirtualMachine> resources = resourceRepository.getAll();
//            assertNotEquals(Collections.emptyList(), resources);
//
//            VirtualMachine resource = resourceRepository.getAll().getFirst();
//
//            resourceRepository.delete(resource.getId());
//            assertThrows(ResourceNotFoundException.class, () -> resourceRepository.findById(resource.getId()));
//        } catch (ResourceException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    void deleteFailInvalidId() {
//        assertThrows(ResourceIdException.class, () -> resourceRepository.delete(""));
//    }
//
//    @Test
//    void deleteFailNotFound() {
//        String id = new ObjectId().toHexString();
//        assertThrows(ResourceNotFoundException.class, () -> resourceRepository.delete(id));
//    }
//}