package pl.pas.restapp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import pl.pas.restapp.model.*;
import pl.pas.restapp.repositories.OrderActorRepository;
import pl.pas.restapp.repositories.OrderRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class MongoDBTest {

    @Autowired
    private OrderRepository<Order> orderRepo;

    @Autowired
    private OrderActorRepository actorOrderRepo;

    private static Optional<Client> client = Optional.empty();
    private static Optional<Worker> worker = Optional.empty();
    private static Optional<Restaurant> restaurant = Optional.empty();
    private static Optional<Order> order = Optional.empty();

    @AfterEach
    void tearDown() {
        client.ifPresent(c -> actorOrderRepo.delete(c.getId(), Client.class));
        worker.ifPresent(w -> actorOrderRepo.delete(w.getId(), Worker.class));
        order.ifPresent(to -> orderRepo.delete(to.getId()));
    }

    /*
 _______   ______
(  ____ \ (  __  \
| (    \/ | (  \  )
| |       | |   ) |
| |       | |   | |
| |       | |   ) |
| (____/\ | (__/  )
(_______/ (______/
*/
    @Test
    void testCreateDeleteSuccess() {
       client = Optional.of(new Client(1000, "John", "Paul"));
       client.ifPresent(c -> actorOrderRepo.save(c));
       assertEquals(actorOrderRepo.findById(1000, Client.class).getSurname(), "Paul");

       actorOrderRepo.delete(1000, Client.class);
       assertNull(actorOrderRepo.findById(1000, Worker.class));
    }

    @Test
    void testCreateFailSchema() {
        client = Optional.of(new Client(1000, "invalidName", "Paul" ));
        client.ifPresent( invalidClient -> assertThrows(DataIntegrityViolationException.class, () -> actorOrderRepo.save(invalidClient)));
    }

    @Test
    void testDeleteNull() {
        assertNull(actorOrderRepo.delete(1000, Client.class));
    }
//    @Test
//    void createFoodOrder_Success_WhenWorkerAndClientAreFree() {
//        Restaurant restaurant = actorOrderRepo.findById( 501, Restaurant.class);
//
//        foodOrder= Optional.of(new FoodOrder());
//
//        foodOrder.get().setId(2004L);
//        foodOrder.get().setClientId(4);
//        foodOrder.get().setWorkerId(203L);
//        foodOrder.get().setPrice(25.00f);
//        foodOrder.get().setStartTime(LocalDateTime.now());
//        foodOrder.get().setActive(false);
//        foodOrder.get().setRestaurantId(501);
//        foodOrder.get().setDestination("Bumba street");
//        foodOrder.get().setEndTime(null);
//        foodOrder.get().setRestaurantId(501);
//        foodOrder.get().setRestaurantAddress("Pizza Street");
//
//
//        foodOrder = Optional.ofNullable((FoodOrder) orderRepo.save(foodOrder.get()));
//
//        assertTrue(foodOrder.isPresent());
//
//        System.out.println(foodOrder.get().getId());
//        assertEquals(25.00f, foodOrder.get().getPrice(), 0.0005f);
//    }
//
//    @Test
//    void createFoodOrder_Failure_WhenWorkerHasActiveOrder() {
//        Restaurant restaurant = actorOrderRepo.findById( 501, Restaurant.class);
//
//         foodOrder = Optional.of(new FoodOrder());
//
//        foodOrder.get().setId(9996L);
//        foodOrder.get().setClientId(4);         //  (freeClient)
//        foodOrder.get().setWorkerId(201L);      //  (busyWorker)
//        foodOrder.get().setPrice(25.00f);
//        foodOrder.get().setStartTime(LocalDateTime.now());
//        foodOrder.get().setActive(true);
//        foodOrder.get().setRestaurantId(501);
//        foodOrder.get().setDestination("Bumba street");
//        foodOrder.get().setEndTime(null);
//        foodOrder.get().setRestaurantAddress("Pizza Street");
//
//        // Use the save method from the target example
//        // We assume save() returns null if the worker is busy (constraint violation)
//        foodOrder = Optional.ofNullable((FoodOrder) orderRepo.save(foodOrder.get()));
//
//        // Assert failure (the opposite of the target example)
//        assertFalse(foodOrder.isPresent());
//    }


    @Test
    void createTaxiOrder_Failure_WhenClientHasActiveTaxiOrder() {
        // Setup: We assume client 3L has an active TAXI order in the test data.

        Optional<Order> taxiOrder = Optional.of(new Order());
        Long orderId = 9995L;

        taxiOrder.get().setId(orderId);
        taxiOrder.get().setClientId(3L);      // The BUSY client
        taxiOrder.get().setWorkerId(404L);    // A free worker
        taxiOrder.get().setPrice(15.00f);
        taxiOrder.get().setStartTime(LocalDateTime.now());
        taxiOrder.get().setActive(true);
        taxiOrder.get().setDestination("Central Station");

        // Action: Attempt to save the order.
        // The 'save' method should find an active taxi order for client 3L
        // and return null.
        taxiOrder = Optional.ofNullable(
                (Order) orderRepo.save(taxiOrder.get())
        );

        assertFalse(taxiOrder.isPresent());

        assertNull(orderRepo.findById(orderId));
    }

/* 8 888888888o.
   8 8888    `88.
   8 8888     `88
   8 8888     ,88
   8 8888.   ,88'
   8 888888888P'
   8 8888`8b
   8 8888 `8b.
   8 8888   `8b.
   8 8888     `88. */

    @Test
    void testReadSuccess() {
        Client client = actorOrderRepo.findById(1, Client.class);

        assertEquals(1, client.getId());
        assertEquals("John", client.getName());
        assertEquals("Doe", client.getSurname());
    }

    @Test
    void testReadNull() {
        client = Optional.ofNullable(actorOrderRepo.findById(-1, Client.class));
        assertTrue(client.isEmpty());
    }

 /* 8 8888      88
    8 8888      88
    8 8888      88
    8 8888      88
    8 8888      88
    8 8888      88
    8 8888      88
    ` 8888     ,8P
      8888   ,d8P
       `Y88888P'   */

    @Test
    void testUpdateRestaurant() {
        restaurant = Optional.ofNullable(actorOrderRepo.findById(501, Restaurant.class));
        String originalAddress = restaurant.get().getOfficeAddress();

        restaurant = Optional.ofNullable(actorOrderRepo.update(restaurant.get().getId(), "Politechniczna 2"));
        assertEquals("Politechniczna 2", restaurant.get().getOfficeAddress());

        assertEquals(originalAddress, actorOrderRepo.update(restaurant.get().getId(), originalAddress).getOfficeAddress());
    }


//    @Test
//    void updateTaxiOrder_Success() {
//        foodOrder = Optional.of(new FoodOrder());
//
//        foodOrder.get().setId(2004L);
//        foodOrder.get().setClientId(4);
//        foodOrder.get().setWorkerId(203L);
//        foodOrder.get().setPrice(25.00f);
//        foodOrder.get().setStartTime(LocalDateTime.now());
//        foodOrder.get().setActive(true);
//        foodOrder.get().setRestaurantId(501);
//        foodOrder.get().setDestination("Bumba street");
//        foodOrder.get().setEndTime(null);
//        foodOrder.get().setRestaurantId(501);
//        foodOrder.get().setRestaurantAddress("Pizza Street");
//
//        foodOrder = Optional.ofNullable((FoodOrder) orderRepo.save(foodOrder.get()));
//
//        assertEquals(true, foodOrder.get().isActive());
//
//        foodOrder = Optional.of( (FoodOrder) orderRepo.update(foodOrder.get()));
//
//        assertEquals(false, foodOrder.get().isActive());
//    }

}
