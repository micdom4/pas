package team.four.pas.services;

import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class UserServiceTest {
//    @Container
//    public static DockerComposeContainer<?> compose =
//            new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
//                    .withExposedService("mongo", 27017);
//
//    private static AnnotationConfigApplicationContext context;
//
//    private static UserService userService;
//    private static MongoDatabase database;
//
//
//    @BeforeAll
//    static void beforeAll() {
//        String host = compose.getServiceHost("mongo", 27017);
//        Integer port = compose.getServicePort("mongo", 27017);
//        String dynamicUri = "mongodb://" + host + ":" + port + "/pas";
//
//        System.setProperty("pas.data.mongodb.uri", dynamicUri);
//
//
//        context = new AnnotationConfigApplicationContext(Config.class);
//        UserRepository userRepository = context.getBean(UserRepository.class);
//
//        userService = new UserServiceImpl(userRepository);
//        database = context.getBean(MongoClient.class).getDatabase("pas");
//    }
//
//    @AfterEach
//    void afterEach() {
//        database = context.getBean(MongoClient.class).getDatabase("pas");
//        database.getCollection("users").deleteMany(new Document());
//    }
//
//  /* CCC
//    C
//    C
//    C
//     CCC  */
//
//    @Test
//    void addPassWhenFreeLogin() {
//        try {
//            assertNotNull(userService.add(new Admin(null, "BLis2", "Bartosz", "Lis", true)));
//        } catch (UserException ue) {
//            fail(ue.getMessage());
//        }
//    }
//
//    @Test
//    void addFailWhenLoginExists() {
//        assertDoesNotThrow(() -> userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));
//        assertThrows(UserAlreadyExistsException.class, () -> userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));
//    }
//
//    @Test
//    void addFailWhenLoginEmpty() {
//        assertThrows(UserLoginException.class, () -> userService.add(new Admin(null, "", "Bartosz", "Lis", true)));
//    }
//
//    @Test
//    void findByLogin() {
//        assertDoesNotThrow(() -> userService.add(new Client("25", "BLis", "Bartosz", "Lis", false)));
//        assertDoesNotThrow(() -> assertEquals("Bartosz", userService.findByLogin("BLis").getName()));
//    }
//
//    @Test
//    void findByMatchingLogin() {
//        try {
//            assertNotNull(userService.add(new Client(null, "BLis", "Bartosz", "Lis", true)));
//            assertNotNull(userService.add(new Client(null, "BLis2", "Bartosz", "Lis", true)));
//            System.out.println(userService.findByMatchingLogin("BL"));
//            assertEquals("Bartosz", userService.findByMatchingLogin("BL").getFirst().getName());
//        } catch (UserException ue) {
//            fail(ue.getMessage());
//        }
//    }
//
//    @Test
//    void shouldReturnCorrectType() {
//        try {
//            assertNotNull(userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));
//            User user = userService.findByLogin("BLis");
//            assertTrue(user instanceof Admin);
//        } catch (UserException ue) {
//            fail(ue.getMessage());
//        }
//    }
//
//    @Test
//    void updatePass() {
//        try {
//            assertNotNull(userService.add(new Admin(null, "BLis", "Bartosz", "Lis", true)));
//            assertEquals("Lis", userService.findByLogin("BLis").getSurname());
//            assertNotNull(userService.update(userService.findByLogin("BLis").getId(), "Lis-Nowak"));
//            assertEquals("Lis-Nowak", userService.findByLogin("BLis").getSurname());
//        } catch (UserException ue) {
//            fail(ue.getMessage());
//        }
//    }

}
