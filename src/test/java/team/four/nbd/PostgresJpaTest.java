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
import team.four.nbd.data.FoodOrder;
import team.four.nbd.data.Restaurant;
import team.four.nbd.data.Worker;
import team.four.nbd.repositories.ClientRepository;
import team.four.nbd.repositories.ClientRepositoryImpl;
import team.four.nbd.repositories.OrderRepository;
import team.four.nbd.repositories.OrderRepositoryImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Testcontainers
public class PostgresJpaTest {
    private static ClientRepository clientRepo;
    private static OrderRepository orderRepo;
    private static EntityManager em;
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
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("zuber", properties);
        em = emf.createEntityManager();

        clientRepo = new ClientRepositoryImpl(em);
        orderRepo = new OrderRepositoryImpl(em);
    }
    @Test
    public void testGetClient() {
        Client client = clientRepo.getClient(1L);
        System.out.println(client);
    }

    @Test
    void createFoodOrder_Success_WhenWorkerAndClientAreFree() {
        // ARRANGE: Worker 203 (Charlie) and Client 4 (Emily) are both free.
        Client client = em.find(Client.class, 4L);
        Worker worker = em.find(Worker.class, 203L);
        Restaurant restaurant = em.find(Restaurant.class, 501L);

        FoodOrder newOrder = new FoodOrder();
        newOrder.setClient(client);
        newOrder.setWorker(worker);
        newOrder.setPrice(25.00f);
        newOrder.setStartTime(LocalDateTime.now());
        newOrder.setActive(true);
        newOrder.setRestaurant(restaurant);

        // ACT & ASSERT: The method should execute without throwing an exception.
        assertEquals(true, orderRepo.createOrder(newOrder));

        // VERIFY: The new order should exist in the database.
        FoodOrder persistedOrder = em.find(FoodOrder.class, newOrder.getOrderId());
        assertNotNull(persistedOrder);
        assertEquals(25.00f, persistedOrder.getPrice(), 0.0005f);
    }
}
