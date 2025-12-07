package team.four.pas.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.exceptions.user.UserAlreadyExistsException;
import team.four.pas.exceptions.user.UserDataException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.exceptions.user.UserLoginException;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.User;
import team.four.pas.services.implementation.UserServiceImpl;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class UserServiceTest {
    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static AnnotationConfigApplicationContext context;

    private static UserService userService;
    private static MongoDatabase database;


    @BeforeAll
    static void beforeAll() {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);


        context = new AnnotationConfigApplicationContext(Config.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        userService = new UserServiceImpl(userRepository);
        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void afterEach() {
        database = context.getBean(MongoClient.class).getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
    }

  /* CCC
    C
    C
    C
     CCC  */

    @Test
    void addPassWhenFreeLogin() {
        try {
            assertNotNull(userService.add(new Admin(null, "BLis2", "Bartosz", "Lis", true)));
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void addFailWhenLoginExists() {
        assertDoesNotThrow(() -> userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));
        assertThrows(UserAlreadyExistsException.class, () -> userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));
    }

    @Test
    void addFailWhenLoginEmpty() {
        assertThrows(UserLoginException.class, () -> userService.add(new Admin(null, "", "Bartosz", "Lis", true)));
    }

    @Test
    void addFailWhenBadText() {
        int initialSize = userService.getAll().size();

        assertThrows(UserDataException.class, () -> userService.add(new Client(null, "BLis5", "bartosz", "Lis", true)));
        assertThrows(UserDataException.class, () -> userService.add(new Client(null, "BLis5", "Bart0sz", "Lis", true)));
        assertThrows(UserDataException.class, () -> userService.add(new Client(null, "BLis5", "Bartosz", "lis", true)));
        assertThrows(UserDataException.class, () -> userService.add(new Client(null, "BLis5", "Bartosz", "LiS", true)));
        assertThrows(UserLoginException.class, () -> userService.add(new Client(null, "bLis5", "Bartosz", "Lis", true)));
        assertThrows(UserLoginException.class, () -> userService.add(new Client(null, "BLis555555", "Bartosz", "Lis", true)));
        assertThrows(UserLoginException.class, () -> userService.add(new Client(null, "Blis55555", "Bartosz", "Lis", true)));

        assertEquals(initialSize, userService.getAll().size());
    }

    @Test
    void findByLogin() {
        assertDoesNotThrow(() -> userService.add(new Client("25", "BLis", "Bartosz", "Lis", false)));
        assertDoesNotThrow(() -> assertEquals("Bartosz", userService.findByLogin("BLis").getName()));
    }

    @Test
    void findByMatchingLogin() {
        try {
            assertNotNull(userService.add(new Client(null, "BLis", "Bartosz", "Lis", true)));
            assertNotNull(userService.add(new Client(null, "BLis2", "Bartosz", "Lis", true)));
            System.out.println(userService.findByMatchingLogin("BL"));
            assertEquals("Bartosz", userService.findByMatchingLogin("BL").getFirst().getName());
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void shouldReturnCorrectType() {
        try {
            assertNotNull(userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));
            User user = userService.findByLogin("BLis");
            assertTrue(user instanceof Admin);
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void updatePass() {
        try {
            assertNotNull(userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));
            assertEquals("Lis", userService.findByLogin("BLis").getSurname());
            assertNotNull(userService.update(userService.findByLogin("BLis").getId(), "Lis-Nowak"));
            assertEquals("Lis-Nowak", userService.findByLogin("BLis").getSurname());
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void updateFail() {
        try {
            assertNotNull(userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));
            assertEquals("Lis", userService.findByLogin("BLis").getSurname());

            String userId = userService.findByLogin("BLis").getId();

            assertThrows(UserDataException.class, () -> userService.update(userId, "Lis-nowak"));
            assertThrows(UserDataException.class, () -> userService.update(userId, "lis-Nowak"));
            assertThrows(UserDataException.class, () -> userService.update(userId, "Lis-N0wak"));
            assertThrows(UserDataException.class, () -> userService.update(userId, "Lis-Noooooooooooooooooooowak"));

            assertEquals("Lis", userService.findByLogin("BLis").getSurname());
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void activateDeactivate() {
        try {
            assertNotNull(userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));

            String id = userService.getAll().getFirst().getId();

            userService.activate(id);

            assertTrue(userService.findByLogin("BLis").isActive());
            userService.deactivate(id);
            assertFalse(userService.findByLogin("BLis").isActive());
            userService.activate(id);
            assertTrue(userService.findByLogin("BLis").isActive());
        } catch (UserException e) {
            fail(e.getMessage());
        }
    }

}
