package team.four.pas;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import team.four.pas.data.*;
import team.four.pas.repositories.ClientRepository;
import team.four.pas.repositories.ClientRepositoryImpl;
import team.four.pas.repositories.OrderRepository;
import team.four.pas.repositories.OrderRepositoryImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @BeforeEach
    void init() {
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
        assertNotNull(client);
    }


    @Test
    void createFoodOrder_Success_WhenWorkerAndClientAreFree() {
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

        assertEquals(true, orderRepo.createOrder(newOrder));

        FoodOrder persistedOrder = em.find(FoodOrder.class, newOrder.getOrderId());
        assertNotNull(persistedOrder);
        assertEquals(25.00f, persistedOrder.getPrice(), 0.0005f);
    }

    @Test
    void createFoodOrder_Success_ForClientWithActiveTaxiOrder() {
        Client client = em.find(Client.class, 3L);
        Worker worker = em.find(Worker.class, 203L);
        Restaurant restaurant = em.find(Restaurant.class, 502L);

        FoodOrder newOrder = new FoodOrder();
        newOrder.setClient(client);
        newOrder.setWorker(worker);
        newOrder.setPrice(15.50f);
        newOrder.setActive(true);
        newOrder.setRestaurant(restaurant);
        newOrder.setStartTime(LocalDateTime.now());

        assertEquals(true, orderRepo.createOrder(newOrder));

        assertNotNull(em.find(FoodOrder.class, newOrder.getOrderId()));
    }

    @Test
    void createOrder_Failure_WhenWorkerHasActiveOrder() {
        Client freeClient = em.find(Client.class, 4L);
        Worker busyWorker = em.find(Worker.class, 201L);
        Restaurant restaurant = em.find(Restaurant.class, 501L);

        FoodOrder newOrder = new FoodOrder();
        newOrder.setClient(freeClient);
        newOrder.setWorker(busyWorker);
        newOrder.setOrderId(9996L);
        newOrder.setActive(true);
        newOrder.setRestaurant(restaurant);
        newOrder.setStartTime(LocalDateTime.now());

        assertEquals(false, orderRepo.createOrder(newOrder));

        assertNull(em.find(Order.class, newOrder.getOrderId()) );
    }

    @Test
    void createTaxiOrder_Failure_WhenClientHasActiveTaxiOrder() {
        Client busyClient = em.find(Client.class, 3L);
        Worker freeWorker = em.find(Worker.class, 404L);

        TaxiOrder newOrder = new TaxiOrder();
        newOrder.setOrderId(9995L);
        newOrder.setClient(busyClient);
        newOrder.setWorker(freeWorker);
        newOrder.setActive(true);
        newOrder.setStartTime(LocalDateTime.now());

        assertEquals(false, orderRepo.createOrder(newOrder));

        assertNull(em.find(Order.class, newOrder.getOrderId()));
    }


    @Test
    void updateTaxiOrder_Success() {
        Order newOrder = orderRepo.getOrder(1001L);

        assertEquals(true, newOrder.isActive());

        orderRepo.updateOrder(1001L);

        assertEquals(false, newOrder.isActive());
    }


}
