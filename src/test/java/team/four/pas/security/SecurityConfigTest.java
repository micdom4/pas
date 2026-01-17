package team.four.pas.security;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.JwtTokenizer;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.AllocationService;
import team.four.pas.services.ResourceService;
import team.four.pas.services.UserService;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class SecurityConfigTest {

    @Container
    public static DockerComposeContainer<?> compose =
            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("mongo", 27017);

    @LocalServerPort
    private int port;

    private final static Client clientOk = new Client(null, "MCorleone", "abc123","Michael", "Corleone", true);
    private final static Manager managerOk = new Manager(null, "MSmolinski", "abc123","Michael", "Corleone", true);
    private final static Admin adminOk = new Admin(null, "BLis", "abc123","Michael", "Corleone", true);

    @Autowired
    private UserService userService;
    @Autowired
    private AllocationService allocationService;
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private MongoClient mongoClient;

    private MongoDatabase database;
    @Autowired
    private UserRepository userRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        String host = compose.getServiceHost("mongo", 27017);
        Integer port = compose.getServicePort("mongo", 27017);
        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";
        System.setProperty("pas.data.mongodb.uri", dynamicUri);
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;

        this.database = mongoClient.getDatabase("pas");
        try {
            userService.add(clientOk);
            userService.add(managerOk);
            userService.add(adminOk);
            resourceService.addVM(new VirtualMachine(null, 8, 16, 256));
            userService.activate(userRepository.findByLogin(clientOk.getLogin()).getId());
            userService.activate(userRepository.findByLogin(managerOk.getLogin()).getId());
            userService.activate(userRepository.findByLogin(adminOk.getLogin()).getId());
        } catch (UserException | ResourceDataException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void afterEach() {
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

    @Test
    void RejectWithoutCredentials() {
        RestAssured.given()
                .log().parameters()
                .when()
                .get("/allocations")
                .then()
                .log().body()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void AllowClientWithCredentials() {
        String creds = clientOk.getLogin() + ":" + clientOk.getPassword();
        String encoded = Base64.getEncoder().encodeToString(creds.getBytes(StandardCharsets.UTF_8));

        Header auth = new Header("Authorization","Bearer " + encoded);

        RestAssured.given()
                .header(auth)
                .log().headers()
                .when()
                .get("/allocations")
                .then()
                .log().body()
                .statusCode(HttpStatus.OK.value());
    }



}