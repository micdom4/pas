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

    private UserRepository userRepository;
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void each(){
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";

        System.setProperty("pas.data.mongodb.uri", dynamicUri);


        context = new AnnotationConfigApplicationContext(Config.class);
        userRepository = context.getBean(UserRepository.class);
    }

    @AfterEach
    void afterEach(){
        MongoDatabase database = context.getBean(MongoClient.class).getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
    }

  /* CCC
    C
    C
    C
     CCC  */

    @Test
    void addPassWhenFreeLogin() {
        assertTrue(userRepository.add("BLis2", "Bartosz", "Lis", Client.class));
    }

    @Test
    void addFailWhenLoginExists() {
        assertFalse(userRepository.add("BLis", "Bartosz", "Lis", Client.class));
    }

    @Test
    void addFailWhenLoginEmpty() {
        assertFalse(userRepository.add("", "Bartosz", "Lis", Client.class));
    }

    /* RRR
       R  R
       RRR
       R  R
       R   R */

    @Test
    void findByLogin() {
        assertEquals("Bartosz", userRepository.findByLogin("BLis").getName());
    }

    @Test
    void findByMatchingLogin() {
        assertEquals("Bartosz", userRepository.findByMatchingLogin("BL").getFirst().getName());
    }

    @Test
    void shouldReturnCorrectType() {
        User user = userRepository.findByLogin("BLis");
        assertEquals(Admin.class, user.getClass());
    }


    /* U   U
       U   U
       U   U
       U   U
        UUU  */

    @Test
    void updatePass() {
        assertEquals("Lis", userRepository.findByLogin("BLis").getSurname());
        assertTrue(userRepository.updateByLogin("BLis", "Lis-Nowak"));
        assertEquals("Lis-Nowak", userRepository.findByLogin("BLis").getSurname());
    }

    @Test
    void activateDeactivate() {
        assertTrue(userRepository.findByLogin("BLis").isActive());
        assertTrue(userRepository.deactivate("BLis"));
        assertFalse(userRepository.findByLogin("BLis").isActive());
        assertTrue(userRepository.activate("BLis"));
        assertTrue(userRepository.findByLogin("BLis").isActive());
    }
}