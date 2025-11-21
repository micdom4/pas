package team.four.pas.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.Config;
import team.four.pas.exceptions.AppBaseException;
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.user.UserAlreadyExistsException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.exceptions.user.UserLoginException;
import team.four.pas.exceptions.user.UserNotFoundException;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.User;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;


@Testcontainers
class UserRepositoryTest {

    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    private static UserRepository userRepository;
    private static AnnotationConfigApplicationContext context;
    private static MongoDatabase database;

    @BeforeAll
    static void each() {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);

        context = new AnnotationConfigApplicationContext(Config.class);
        userRepository = context.getBean(UserRepository.class);
        database = context.getBean(MongoClient.class).getDatabase("pas");
    }

    @AfterEach
    void afterEach() {
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
            assertEquals("BLis2", userRepository.add("BLis2", "Bartosz", "Lis", Client.class).getLogin());
        } catch (UserException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void addFailWhenLoginExists() {
        try {
            assertEquals("BLis", userRepository.add("BLis", "Bartosz", "Lis", Client.class).getLogin());
        } catch (UserException e) {
            fail(e.getMessage());
        }
        assertThrows(UserAlreadyExistsException.class, () -> userRepository.add("BLis", "Bartosz", "Lis", Client.class));
    }

    @Test
    void addFailWhenLoginEmpty() {
        assertThrows(UserLoginException.class, () -> userRepository.add(null, "Bartosz", "Lis", Client.class));
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void findByLogin() {
        try {
            userRepository.add("BLis", "Bartosz", "Lis", Client.class);
            assertEquals("Bartosz", userRepository.findByLogin("BLis").getName());
        } catch (UserException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findById() {
        try {
            User user = userRepository.add("BLis", "Bartosz", "Lis", Client.class);
            assertEquals("Bartosz", userRepository.findById(user.getId()).getName());
        } catch (AppBaseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findByIdFail() {
        String id = new ObjectId().toHexString();
        assertThrows(UserNotFoundException.class, () -> userRepository.findById(id));
    }

    @Test
    void failFindByLogin() {
        assertThrows(UserNotFoundException.class, () -> userRepository.findByLogin("BLis"));
    }

    @Test
    void findByMatchingLogin() {
        try {
            userRepository.add("BLis", "Bartosz", "Lis", Client.class);
            assertEquals("Bartosz", userRepository.findByMatchingLogin("BL").getFirst().getName());
        } catch (UserException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findByMatchingLoginShouldReturnEmptyList() {
        assertEquals(0, userRepository.findByMatchingLogin("BL").size());
    }

    @Test
    void shouldReturnCorrectType() {
        try {
            userRepository.add("BLis", "Bartosz", "Lis", Admin.class);
            User user = userRepository.findByLogin("BLis");
            assertEquals(Admin.class, user.getClass());
        } catch (UserException e) {
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
            userRepository.add("BLis", "Bartosz", "Lis", Admin.class);
            assertEquals("Lis", userRepository.findByLogin("BLis").getSurname());
            assertEquals(userRepository.findByLogin("BLis"), userRepository.update(userRepository.findByLogin("BLis").getId(), "Lis-Nowak"));
            assertEquals("Lis-Nowak", userRepository.findByLogin("BLis").getSurname());
        } catch (UserException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void updateFailWrongId() {
        assertThrows(IllegalArgumentException.class, () -> userRepository.update("123231", "Lis-Nowak"));
    }

    @Test
    void updateFailNoId() {
        assertThrows(UserLoginException.class, () -> userRepository.update("", "Lis-Nowak"));
    }

    @Test
    void activateDeactivate() {
        try {
            userRepository.add("BLis", "Bartosz", "Lis", Admin.class);
            String id = userRepository.getAll().getFirst().getId();
            userRepository.activate(id);
            assertTrue(userRepository.findByLogin("BLis").isActive());
            assertDoesNotThrow(() -> userRepository.deactivate(id));
            assertFalse(userRepository.findByLogin("BLis").isActive());
            assertDoesNotThrow(() -> userRepository.activate(id));
            assertTrue(userRepository.findByLogin("BLis").isActive());
        } catch (UserException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void activateDeactivateFailWhenUserDontExists() {
        String id = new ObjectId().toHexString();
        assertThrows(UserNotFoundException.class, () -> userRepository.activate(id));
        assertThrows(UserNotFoundException.class, () -> userRepository.deactivate(id));
    }

    @Test
    void activateDeactivateFailWhenLoginEmpty() {
        String id = "";
        assertThrows(UserLoginException.class, () -> userRepository.activate(id));
        assertThrows(UserLoginException.class, () -> userRepository.deactivate(id));
    }


}