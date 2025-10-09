package team.four.nbd;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import team.four.nbd.data.Client;
import team.four.nbd.data.Order;
import team.four.nbd.repositories.ZoltRepository;
import team.four.nbd.repositories.ZoltRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Testcontainers
public class PostgresJpaTest {
    private static ZoltRepository repo;

    @Container
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17"))
            .withDatabaseName("nbddb")
            .withUsername("nbd")
            .withPassword("nbdpassword");
    @BeforeAll
    static void init() {
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", postgres.getJdbcUrl());
        properties.put("jakarta.persistence.jdbc.user", postgres.getUsername());
        properties.put("jakarta.persistence.jdbc.password", postgres.getPassword());
        properties.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("clients", properties);
        EntityManager em = emf.createEntityManager();

        repo = new ZoltRepositoryImpl(em);
    }
    @Test
    public void testGet() {
        Client client = repo.getClients(1L);
        System.out.println(client);
    }
}
