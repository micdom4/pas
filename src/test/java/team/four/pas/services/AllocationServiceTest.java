//package team.four.pas.services;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.testcontainers.containers.DockerComposeContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import team.four.pas.Config;
//import team.four.pas.controllers.DTOs.UserAddDTO;
//import team.four.pas.controllers.DTOs.UserType;
//import team.four.pas.repositories.AllocationRepository;
//import team.four.pas.repositories.ResourceRepository;
//import team.four.pas.repositories.UserRepository;
//import team.four.pas.repositories.implementation.MongoAllocationRepository;
//import team.four.pas.services.data.allocations.VMAllocation;
//import team.four.pas.services.data.resources.VirtualMachine;
//import team.four.pas.services.data.users.Client;
//import team.four.pas.services.data.users.Manager;
//import team.four.pas.services.data.users.User;
//import team.four.pas.services.implementation.AllocationServiceImpl;
//import team.four.pas.services.implementation.ResourceServiceImpl;
//import team.four.pas.services.implementation.UserServiceImpl;
//import team.four.pas.services.mappers.UserToDTO;
//import team.four.pas.services.mappers.UserToDTOImpl;
//
//import javax.management.BadAttributeValueExpException;
//import java.io.File;
//import java.rmi.ServerException;
//import java.security.KeyManagementException;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@Testcontainers
//class AllocationServiceTest {
//    @Container
//    public static DockerComposeContainer<?> compose =
//            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
//                    .withExposedService("mongo", 27017);
//
//    private static AnnotationConfigApplicationContext context;
//
//    private static AllocationService allocationService;
//    private static ResourceService resourceService;
//    private static UserService userService;
//    private static MongoDatabase database;
//    @BeforeAll
//    static void each() {
//        String host = compose.getServiceHost("mongo", 27017);
//        Integer port = compose.getServicePort("mongo", 27017);
//        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";
//
//        System.setProperty("pas.data.mongodb.uri", dynamicUri);
//
//        context = new AnnotationConfigApplicationContext(Config.class);
//        AllocationRepository allocationRepository = context.getBean(MongoAllocationRepository.class);
//        ResourceRepository resourceRepository = context.getBean(ResourceRepository.class);
//        UserRepository userRepository = context.getBean(UserRepository.class);
//
//        allocationService = new AllocationServiceImpl(allocationRepository);
//        resourceService = new ResourceServiceImpl(resourceRepository, allocationRepository);
//        userService =  new UserServiceImpl(userRepository, context.getBean(UserToDTO.class));
//        database = context.getBean(MongoClient.class).getDatabase("pas");
//    }
//
//    @AfterEach
//    void afterEach() {
//        database.getCollection("users").deleteMany(new Document());
//        database.getCollection("virtualMachines").deleteMany(new Document());
//        database.getCollection("vmAllocations").deleteMany(new Document());
//    }
//
//  /* CCC
//    C
//    C
//    C
//     CCC  */
//
//
//    /*
//    @Test
//    void addPositive() {
//        String login = "HKwinto";
//        assertTrue(userService.add(login, "Henryk", "Kwinto", Client.class));
//        assertTrue(resourceService.addVM(8, 16, 256));
//
//        int initialSize = allocationService.getAll().size();
//
//        VirtualMachine virtualMachine = resourceService.getAll().getLast();
//        userService.activate(userService.findByLogin(login).getId());
//        assertTrue(allocationService.add((Client) userService.findByLogin(login), virtualMachine, Instant.now()));
//
//        assertEquals(initialSize + 1, allocationService.getAll().size());
//    }
//     */
//
//    @Test
//    void addNegative() {
//
//    }
//
//    /* RRR
//       R  R
//       RRR
//       R  R
//       R   R */
//
//    @Test
//    void getAll() throws ServerException, KeyManagementException, BadAttributeValueExpException {
//        String login = "HKwinto";
//
//        assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));
//        assertTrue(resourceService.addVM(12, 16, 256));
//
//        userService.activate(userService.findByLogin(login).getId());
//        assertTrue(allocationService.add((Client) userService.findByLogin(login), resourceService.getAll().getFirst(), Instant.now()));
//
//        assertEquals(1, allocationService.getAll().size());
//        System.out.println(allocationService.getAll());
//    }
//
//    @Test
//    void findById() throws ServerException, KeyManagementException, BadAttributeValueExpException {
//        String login = "HKwinto";
//
//        assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));
//        assertTrue(resourceService.addVM(12, 16, 256));
//
//        userService.activate(userService.findByLogin(login).getId());
//
//
//        assertTrue(allocationService.add((Client) userService.findByLogin(login), resourceService.getAll().getLast(), Instant.now()));
//        VMAllocation vmAllocation = allocationService.getAll().getFirst();
//        assertNotNull(vmAllocation);
//
//        assertEquals(vmAllocation, allocationService.findById(vmAllocation.getId()));
//    }
//
//    @Test
//    void findByIds() throws ServerException, KeyManagementException, BadAttributeValueExpException {
//        String login = "HKwinto";
//        String login2 = "HKwinto2";
//        assertNotNull(userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));
//        assertNotNull(userService.add(new UserAddDTO(login2, "Henryka", "Kwintowna", UserType.CLIENT)));
//
//        assertTrue(resourceService.addVM(12, 16, 256));
//        assertTrue(resourceService.addVM(15, 16, 256));
//
//        userService.activate(userService.findByLogin(login).getId());
//        userService.activate(userService.findByLogin(login2).getId());
//
//        List<VirtualMachine> vms = resourceService.getAll();
//        assertTrue(allocationService.add((Client) userService.findByLogin(login), vms.getFirst(), Instant.now()));
//        assertTrue(allocationService.add((Client) userService.findByLogin(login2), vms.getLast(), Instant.now()));
//
//        List<VMAllocation> vmAllocations = allocationService.getAll();
//
//        assertNotEquals(vmAllocations.getFirst(), vmAllocations.getLast());
//
//        assertNotNull(vmAllocations.getFirst());
//        assertNotNull(vmAllocations.getLast());
//
//
//        List<String> ids = new ArrayList<>();
//        ids.add(vmAllocations.getFirst().getId());
//        ids.add(vmAllocations.getLast().getId());
//
//        assertEquals(vmAllocations, allocationService.findById(ids));
//    }
//}