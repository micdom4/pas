package team.four.nbd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Testcontainers
public class PostgresJpaTest {
    @Container
    private static PostgreSQLContainer postgres = (PostgreSQLContainer) new
            PostgreSQLContainer(DockerImageName.parse("postgres:17"))
            .withDatabaseName("nbddb")
            .withUsername("nbd")
            .withPassword("nbdpassword")
            .withExposedPorts(5432);
    private static EntityManagerFactory emf;
    private static EntityManager em;

    @BeforeAll
    static void beforeAll() {
        String port = postgres.getMappedPort(5432).toString();
        String dbname = postgres.getDatabaseName();
        Map<String, String> properties = new HashMap<>();
        String url = MessageFormat.format("jdbc:postgresql:</localhost:{0}/{1}", port, dbname);
        properties.put("jakarta.persistence.jdbc.url", url);
        emf = Persistence.createEntityManagerFactory("POSTGRES_RENT_PU", properties);
        em = emf.createEntityManager();

        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

}