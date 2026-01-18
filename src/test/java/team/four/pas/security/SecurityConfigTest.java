package team.four.pas.security;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.JwtTokenizer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import team.four.pas.controllers.AuthController;
import team.four.pas.controllers.DTOs.*;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.AllocationService;
import team.four.pas.services.ResourceService;
import team.four.pas.services.UserService;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.Manager;

import java.io.File;

import static org.hamcrest.Matchers.equalTo;


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

    @Autowired
    private AuthController authController;

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
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void AllowClientWithCredentials() {
        authController.register(new UserAddDTO(clientOk.getLogin(), clientOk.getName(), clientOk.getPassword(), clientOk.getSurname(), UserType.CLIENT));
        authController.register(new UserAddDTO(managerOk.getLogin(), managerOk.getName(), managerOk.getPassword(), managerOk.getSurname(), UserType.CLIENT));

        String jwt = authController.login(new UserLoginDTO(clientOk.getLogin(), clientOk.getPassword())).getBody().getToken();

        Header auth = new Header("Authorization","Bearer " + jwt);

        resourceService.addVM(new VirtualMachine(null, 8, 16, 256));
        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        Client client = (Client) userRepository.findByLogin(clientOk.getLogin());

        AllocationAddDTO requestBody = new AllocationAddDTO(client.getId(), virtualMachine.getId());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(auth)
                .body(requestBody)
                .when()
                .post("/allocations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().body()
                .body("client.login", equalTo("MCorleone"));
    }

    @Test
    void ClientCantCreateAllocationForOtherUsers() {
        authController.register(new UserAddDTO(clientOk.getLogin(), clientOk.getName(), clientOk.getPassword(), clientOk.getSurname(), UserType.CLIENT));
        authController.register(new UserAddDTO(managerOk.getLogin(), managerOk.getName(), managerOk.getPassword(), managerOk.getSurname(), UserType.CLIENT));

        String jwt = authController.login(new UserLoginDTO(clientOk.getLogin(), clientOk.getPassword())).getBody().getToken();

        Header auth = new Header("Authorization","Bearer " + jwt);

        resourceService.addVM(new VirtualMachine(null, 8, 16, 256));
        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        Client manager = (Client) userRepository.findByLogin(managerOk.getLogin());

        AllocationAddDTO requestBody = new AllocationAddDTO(manager.getId(), virtualMachine.getId());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(auth)
                .body(requestBody)
                .when()
                .post("/allocations")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }



}