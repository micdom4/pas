package team.four.pas.controllers;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.restassured.RestAssured;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.exceptions.resource.ResourceGetAllException;
import team.four.pas.exceptions.user.UserFindException;
import team.four.pas.exceptions.user.UserGetAllException;
import team.four.pas.exceptions.user.UserUpdateException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.AllocationService;
import team.four.pas.services.ResourceService;
import team.four.pas.services.UserService;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.implementation.AllocationServiceImpl;
import team.four.pas.services.implementation.ResourceServiceImpl;
import team.four.pas.services.implementation.UserServiceImpl;
import team.four.pas.services.mappers.UserToDTO;

import java.io.File;
import java.time.Instant;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
class AllocationControllerImplTest {

    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static UserService userService;
    private static AllocationService allocationService;
    private static ResourceService resourceService;
    private static AnnotationConfigApplicationContext context;
    private static MongoDatabase database;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";
        System.setProperty("pas.data.mongodb.uri", dynamicUri);
    }

    @BeforeAll
    static void each() {
        context = new AnnotationConfigApplicationContext(Config.class);
        AllocationRepository allocationRepository = context.getBean(AllocationRepository.class);
        ResourceRepository resourceRepository = context.getBean(ResourceRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        allocationService = new AllocationServiceImpl(allocationRepository, userService, resourceService, context.getBean(UserToDTO.class));
        resourceService = new ResourceServiceImpl(resourceRepository, allocationRepository);
        userService = new UserServiceImpl(userRepository, context.getBean(UserToDTO.class));
        database = context.getBean(MongoClient.class).getDatabase("pas");
        AllocationControllerImpl allocationController = new AllocationControllerImpl(allocationService);
    }

    @AfterEach
    void afterEach() {
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }


    @Test
    void getAll() throws ResourceGetAllException, UserGetAllException, UserFindException, UserUpdateException {
        String login = "HKwinto";
        assertDoesNotThrow(() -> userService.add(new UserAddDTO(login, "Henryk", "Kwinto", UserType.CLIENT)));

        assertDoesNotThrow(() -> resourceService.addVM(8, 16, 256));

        int initialSize = allocationService.getAll().size();

        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        userService.activate(userService.findByLogin(login).id());
        UserDTO userDTO = userService.getAll().getLast();

        assertDoesNotThrow(() -> allocationService.add(userDTO, virtualMachine, Instant.now()));

        RestAssured.given()
                .log().parameters()
                .when()
                .get("/allocations")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value())
                .body("client.login", hasItem("HKwinto"));
    }

}