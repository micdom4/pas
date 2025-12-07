package team.four.pas.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.DockerComposeResource;
import team.four.pas.exceptions.user.UserAlreadyExistsException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.exceptions.user.UserLoginException;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.User;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(DockerComposeResource.class)
@Testcontainers
public class UserServiceTest {

    @Inject
    UserService userService;

    @Inject
    MongoClient mongoClient;

    MongoDatabase database;

    @AfterEach
    void afterEach() {
        database = mongoClient.getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
    }

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
}