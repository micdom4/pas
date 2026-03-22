package team.four.pas.security;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;
import team.four.pas.controllers.AuthController;
import team.four.pas.data.AuthResponse;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.data.UserRoles;
import team.four.pas.data.resources.VirtualMachine;
import team.four.pas.data.users.Admin;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.Manager;
import team.four.pas.data.users.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class SecurityConfigTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:7.0.0"));

    @LocalServerPort
    private int port;

    private final static Client clientOk = new Client(null, "MCorleone", "abc123", "Michael", "Corleone", true);
    private final static Manager managerOk = new Manager(null, "MSmolinski", "abc123", "Matthew", "Smolinski", true);
    private final static Admin adminOk = new Admin(null, "BLis", "abc123", "Bart", "Fox", true);

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    @Autowired
    private AuthController authController;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.reset();
        RestAssured.port = port;
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
        resourceRepository.deleteAll();
        allocationRepository.deleteAll();

        SecurityContextHolder.clearContext();
    }

    private void registerUser(UserAddDTO userAddDTO) {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userAddDTO)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    private Header loginUser(User user) {
        UserLoginDTO loginDTO = new UserLoginDTO(user.getLogin(), user.getPassword());

        String token = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .path("token");

        return new Header("Authorization", "Bearer " + token);
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
        registerUser(new UserAddDTO(clientOk.getLogin(), clientOk.getName(), clientOk.getPassword(), clientOk.getSurname(), UserType.CLIENT));

        Header auth = loginUser(clientOk);

        resourceService.addVM(new VirtualMachine(null, 8, 16, 256));
        VirtualMachine virtualMachine = resourceService.getAll().getLast();
        Client client = (Client) userRepository.findByLogin(clientOk.getLogin());

        AllocationAddDTO requestBody = new AllocationAddDTO(client.getId(), virtualMachine.getId());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(auth)
                .body(requestBody)
                .log().everything()
                .when()
                .post("/allocations")
                .then()
                .log().body()
                .statusCode(HttpStatus.CREATED.value())
                .body("client.login", equalTo(clientOk.getLogin()));
    }

    @Test
    void ClientCantCreateAllocationForOtherUsers() {
        registerUser(new UserAddDTO(clientOk.getLogin(), clientOk.getName(), clientOk.getPassword(), clientOk.getSurname(), UserType.CLIENT));
        registerUser(new UserAddDTO(managerOk.getLogin(), managerOk.getName(), managerOk.getPassword(), managerOk.getSurname(), UserType.CLIENT));

        Header auth = loginUser(clientOk);

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

    @Test
    void returnClaimsReturnsCorrectRoles() {
        registerUser(new UserAddDTO(clientOk.getLogin(), clientOk.getName(), clientOk.getPassword(), clientOk.getSurname(), UserType.CLIENT));
        registerUser(new UserAddDTO(managerOk.getLogin(), managerOk.getName(), managerOk.getPassword(), managerOk.getSurname(), UserType.MANAGER));
        registerUser(new UserAddDTO(adminOk.getLogin(), adminOk.getName(), adminOk.getPassword(), adminOk.getSurname(), UserType.ADMIN));

        AuthResponse auth0 = authController.login(new UserLoginDTO(clientOk.getLogin(), clientOk.getPassword())).getBody();
        AuthResponse auth1 = authController.login(new UserLoginDTO(managerOk.getLogin(), managerOk.getPassword())).getBody();
        AuthResponse auth2 = authController.login(new UserLoginDTO(adminOk.getLogin(), adminOk.getPassword())).getBody();

        assertThat(auth0.getRoles())
                .extracting(Object::toString)
                .contains(UserRoles.CLIENT);

        assertThat(auth1.getRoles())
                .extracting(Object::toString)
                .contains(UserRoles.MANAGER);

        assertThat(auth2.getRoles())
                .extracting(Object::toString)
                .contains(UserRoles.ADMIN);
    }

    @Test
    void RegisterShouldReturnCorrectJwtAndRoles() {
        UserAddDTO newUser = new UserAddDTO(clientOk.getLogin(), clientOk.getName(), clientOk.getPassword(), clientOk.getSurname(), UserType.CLIENT);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("token", not(emptyOrNullString()))
                .body("roles", not(emptyOrNullString()));
    }

    @Test
    void LoginShouldReturnCorrectJwtAndRoles() {
        UserAddDTO newUser = new UserAddDTO(clientOk.getLogin(), clientOk.getName(), clientOk.getPassword(), clientOk.getSurname(), UserType.CLIENT);
        registerUser(newUser);

        UserLoginDTO userLoginDTO = new UserLoginDTO(clientOk.getLogin(), clientOk.getPassword());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userLoginDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("roles", not(emptyOrNullString()));
    }

    @Test
    void LogoutShouldRevokeJwtToken() {
        registerUser(new UserAddDTO(adminOk.getLogin(), adminOk.getName(), adminOk.getPassword(), adminOk.getSurname(), UserType.ADMIN));

        Header auth = loginUser(adminOk);

        RestAssured.given()
                .log().parameters()
                .header(auth)
                .when()
                .get("/allocations")
                .then()
                .statusCode(HttpStatus.OK.value());

        RestAssured.given()
                .log().parameters()
                .header(auth)
                .when()
                .post("/auth/logout")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        RestAssured.given()
                .log().parameters()
                .header(auth)
                .when()
                .get("/allocations")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void ChangingPasswordShouldntAllowUnregistered() {
        RestAssured.given()
                .log().parameters()
                .contentType(ContentType.JSON)
                .body(new ChangePasswordDTO("abc123", "Iceman"))
                .when()
                .post("/auth/reset")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void ChangingPasswordShouldRevokeJwtToken() {
        registerUser(new UserAddDTO(adminOk.getLogin(), adminOk.getName(), adminOk.getPassword(), adminOk.getSurname(), UserType.ADMIN));

        Header auth = loginUser(adminOk);

        RestAssured.given()
                .log().parameters()
                .contentType(ContentType.JSON)
                .header(auth)
                .when()
                .get("/allocations")
                .then()
                .statusCode(HttpStatus.OK.value());

        RestAssured.given()
                .log().parameters()
                .contentType(ContentType.JSON)
                .header(auth)
                .body(new ChangePasswordDTO(adminOk.getPassword(), "ICEMAN"))
                .when()
                .post("/auth/reset")
                .then()
                .statusCode(HttpStatus.OK.value());

        RestAssured.given()
                .log().parameters()
                .contentType(ContentType.JSON)
                .header(auth)
                .when()
                .get("/allocations")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void ChangingPasswordShouldFailWhenLoggingWithOld() {
        registerUser(new UserAddDTO(adminOk.getLogin(), adminOk.getName(), adminOk.getPassword(), adminOk.getSurname(), UserType.ADMIN));
        Header auth = loginUser(adminOk);

        RestAssured.given()
                .log().parameters()
                .contentType(ContentType.JSON)
                .header(auth)
                .body(new ChangePasswordDTO(adminOk.getPassword(), "ICEMAN"))
                .log().everything()
                .when()
                .post("/auth/reset")
                .then()
                .log().everything()
                .statusCode(HttpStatus.OK.value());

        UserLoginDTO incorrectLoginDTO = new UserLoginDTO(adminOk.getLogin(), adminOk.getPassword());

        RestAssured.given()
                .log().parameters()
                .contentType(ContentType.JSON)
                .body(incorrectLoginDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        RestAssured.given()
                .log().parameters()
                .contentType(ContentType.JSON)
                .header(auth)
                .when()
                .get("/allocations")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void ChangingPasswordShouldWorkWhenLoggingWithNew() {
        registerUser(new UserAddDTO(adminOk.getLogin(), adminOk.getName(), adminOk.getPassword(), adminOk.getSurname(), UserType.ADMIN));

        Header auth = loginUser(adminOk);

        RestAssured.given()
                .log().parameters()
                .contentType(ContentType.JSON)
                .header(auth)
                .body(new ChangePasswordDTO(adminOk.getPassword(), "ICEMAN"))
                .when()
                .post("/auth/reset")
                .then()
                .statusCode(HttpStatus.OK.value());

        UserLoginDTO correctLoginDTO = new UserLoginDTO(adminOk.getLogin(), "ICEMAN");

        RestAssured.given()
                .log().parameters()
                .contentType(ContentType.JSON)
                .body(correctLoginDTO)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(HttpStatus.OK.value());
    }


}