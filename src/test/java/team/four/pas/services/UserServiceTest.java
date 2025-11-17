package team.four.pas.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.User;
import team.four.pas.services.implementation.UserServiceImpl;
import team.four.pas.services.mappers.UserToDTO;

import javax.management.BadAttributeValueExpException;
import java.io.File;
import java.rmi.ServerException;
import java.security.KeyManagementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class UserServiceTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static AnnotationConfigApplicationContext context;

    private static UserService userService;
    private static MongoDatabase database;
;
    @BeforeAll
    static void each() {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);


        context = new AnnotationConfigApplicationContext(Config.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        userService = new UserServiceImpl(userRepository, context.getBean(UserToDTO.class));
        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void afterEach() {
        MongoDatabase database = context.getBean(MongoClient.class).getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
    }

  /* CCC
    C
    C
    C
     CCC  */

    /*
    @Test
    void addPassWhenFreeLogin() {
        assertTrue(userService.add("BLis2", "Bartosz", "Lis", Client.class));
    }

    @Test
    void addFailWhenLoginExists() {
        assertFalse(userService.add("BLis", "Bartosz", "Lis", Client.class));
    }

    @Test
    void addFailWhenLoginEmpty() {
        assertFalse(userService.add("", "Bartosz", "Lis", Client.class));
    }

    @Test
    void addFailWhenBadText() {
        int initialSize = userService.getAll().size();

        assertFalse(userService.add("BLis5", "bartosz", "Lis", Client.class));
        assertFalse(userService.add("BLis5", "Bart0sz", "Lis", Client.class));
        assertFalse(userService.add("BLis5", "Bartosz", "lis", Client.class));
        assertFalse(userService.add("BLis5", "Bartosz", "LiS", Client.class));
        assertFalse(userService.add("bLis5", "Bartosz", "Lis", Client.class));
        assertFalse(userService.add("BLis555555", "Bartosz", "Lis", Client.class));
        assertFalse(userService.add("Blis55555", "Bartosz", "Lis", Client.class));

        assertEquals(initialSize, userService.getAll().size());
    }
    */
    /* RRR
       R  R
       RRR
       R  R
       R   R */

//    @Test
//    void findByLogin() throws ServerException, KeyManagementException, BadAttributeValueExpException {
//        assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.CLIENT)));
//        assertEquals("Bartosz", userService.findByLogin("BLis").name());
//    }

    @Test
    void findByMatchingLogin() throws ServerException, KeyManagementException, BadAttributeValueExpException {
        assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.CLIENT)));
        assertNotNull(userService.add(new UserAddDTO("BLis2", "Bartosz", "Lis", UserType.CLIENT)));
        System.out.println(userService.findByMatchingLogin("BL"));
        assertEquals("Bartosz", userService.findByMatchingLogin("BL").getFirst().name());
    }
//
//    @Test
//    void shouldReturnCorrectType() throws ServerException, KeyManagementException, BadAttributeValueExpException {
//        assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));
//        UserDTO user = userService.findByLogin("BLis");
//        assertEquals(UserType.ADMIN, user.type());
//    }

    /* U   U
       U   U
       U   U
       U   U
        UUU  */
//
//    @Test
//    void updatePass() throws ServerException, KeyManagementException, BadAttributeValueExpException {
//        assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));
//        assertEquals("Lis", userService.findByLogin("BLis").getSurname());
//        assertTrue(userService.updateByLogin("BLis", "Lis-Nowak"));
//        assertEquals("Lis-Nowak", userService.findByLogin("BLis").getSurname());
//    }
//
//    @Test
//    void updateFail() throws ServerException, KeyManagementException, BadAttributeValueExpException {
//        assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));
//        assertEquals("Lis", userService.findByLogin("BLis").getSurname());
//
//        assertFalse(userService.updateByLogin("BLis", "Lis-nowak"));
//        assertFalse(userService.updateByLogin("BLis", "lis-Nowak"));
//        assertFalse(userService.updateByLogin("BLis", "Lis-N0wak"));
//        assertFalse(userService.updateByLogin("BLis", "Lis-Noooooooooooooooooooowak"));
//
//        assertEquals("Lis", userService.findByLogin("BLis").getSurname());
//    }
//
//    @Test
//    void activateDeactivate() throws ServerException, KeyManagementException, BadAttributeValueExpException {
//        assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));
//
//        String id = userService.getAll().getFirst().getId();
//
//        userService.activate(id);
//
//        assertTrue(userService.findByLogin("BLis").isActive());
//        assertTrue(userService.deactivate(id));
//        assertFalse(userService.findByLogin("BLis").isActive());
//        assertTrue(userService.activate(id));
//        assertTrue(userService.findByLogin("BLis").isActive());
//    }

}
