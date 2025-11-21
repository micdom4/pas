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
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.exceptions.user.UserAlreadyExistsException;
import team.four.pas.exceptions.user.UserDataException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.exceptions.user.UserLoginException;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.implementation.UserServiceImpl;
import team.four.pas.services.mappers.UserToDTO;

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
            assertNotNull(userService.add(new UserAddDTO("BLis2", "Bartosz", "Lis", UserType.ADMIN)));
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void addFailWhenLoginExists() {
        assertDoesNotThrow(() -> userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));
        assertThrows(UserAlreadyExistsException.class, () -> userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));
    }

    @Test
    void addFailWhenLoginEmpty() {
        assertThrows(UserLoginException.class, () -> userService.add(new UserAddDTO("", "Bartosz", "Lis", UserType.ADMIN)));
    }

    @Test
    void addFailWhenBadText() {
        int initialSize = userService.getAll().size();

        assertThrows(UserDataException.class, () -> userService.add(new UserAddDTO("BLis5", "bartosz", "Lis", UserType.CLIENT)));
        assertThrows(UserDataException.class, () -> userService.add(new UserAddDTO("BLis5", "Bart0sz", "Lis", UserType.CLIENT)));
        assertThrows(UserDataException.class, () -> userService.add(new UserAddDTO("BLis5", "Bartosz", "lis", UserType.CLIENT)));
        assertThrows(UserDataException.class, () -> userService.add(new UserAddDTO("BLis5", "Bartosz", "LiS", UserType.CLIENT)));
        assertThrows(UserDataException.class, () -> userService.add(new UserAddDTO("bLis5", "Bartosz", "Lis", UserType.CLIENT)));
        assertThrows(UserDataException.class, () -> userService.add(new UserAddDTO("BLis555555", "Bartosz", "Lis", UserType.CLIENT)));
        assertThrows(UserDataException.class, () -> userService.add(new UserAddDTO("Blis55555", "Bartosz", "Lis", UserType.CLIENT)));

        assertEquals(initialSize, userService.getAll().size());
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void findByLogin() {
        assertDoesNotThrow(() -> userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.CLIENT)));
        assertDoesNotThrow(() -> assertEquals("Bartosz", userService.findByLogin("BLis").name()));
    }

    @Test
    void findByMatchingLogin() {
        try {
            assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.CLIENT)));
            assertNotNull(userService.add(new UserAddDTO("BLis2", "Bartosz", "Lis", UserType.CLIENT)));
            System.out.println(userService.findByMatchingLogin("BL"));
            assertEquals("Bartosz", userService.findByMatchingLogin("BL").getFirst().name());
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void shouldReturnCorrectType() {
        try {
            assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));
            UserDTO user = userService.findByLogin("BLis");
            assertEquals(UserType.ADMIN, user.type());
        } catch (UserException ue) {
            fail(ue.getMessage());
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
            assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));
            assertEquals("Lis", userService.findByLogin("BLis").surname());
            assertNotNull(userService.update(userService.findByLogin("BLis").id(), "Lis-Nowak"));
            assertEquals("Lis-Nowak", userService.findByLogin("BLis").surname());
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void updateFail() {
        try {
            assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));
            assertEquals("Lis", userService.findByLogin("BLis").surname());

            String userId = userService.findByLogin("BLis").id();

            assertThrows(UserDataException.class, () -> userService.update(userId, "Lis-nowak"));
            assertThrows(UserDataException.class, () -> userService.update(userId, "lis-Nowak"));
            assertThrows(UserDataException.class, () -> userService.update(userId, "Lis-N0wak"));
            assertThrows(UserDataException.class, () -> userService.update(userId, "Lis-Noooooooooooooooooooowak"));

            assertEquals("Lis", userService.findByLogin("BLis").surname());
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void activateDeactivate() {
        try {
            assertNotNull(userService.add(new UserAddDTO("BLis", "Bartosz", "Lis", UserType.ADMIN)));

            String id = userService.getAll().getFirst().id();

            userService.activate(id);

            assertTrue(userService.findByLogin("BLis").active());
            userService.deactivate(id);
            assertFalse(userService.findByLogin("BLis").active());
            userService.activate(id);
            assertTrue(userService.findByLogin("BLis").active());
        } catch (UserException e) {
            fail(e.getMessage());
        }
    }

}
