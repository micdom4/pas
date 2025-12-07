package team.four.pas.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import team.four.pas.DockerComposeResource;
import team.four.pas.exceptions.allocation.AllocationException;
import team.four.pas.exceptions.allocation.AllocationIdException;
import team.four.pas.exceptions.allocation.AllocationNotFoundException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.users.Client;

import java.io.File;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(DockerComposeResource.class)
@Testcontainers
class AllocationRepositoryTest {

    @Inject
    AllocationRepository allocationRepository;
    @Inject
    ResourceRepository resourceRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    MongoClient mongoClient;

    private MongoDatabase database;

    @AfterEach
    void afterEach() {
        database = mongoClient.getDatabase("pas");
        database.getCollection("users").deleteMany(new Document());
        database.getCollection("virtualMachines").deleteMany(new Document());
        database.getCollection("vmAllocations").deleteMany(new Document());
    }

    @BeforeEach
    void initTwo() throws UserException {
        resourceRepository.addVM(5, 12, 10);
        userRepository.add("BLis", "Bartosz", "Lis", Client.class);
    }

    @Test
    void getAll() {
        try {
            allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());
            allocationRepository.finishAllocation(allocationRepository.getAll().getFirst().getId());
            allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());
            assertEquals(2, allocationRepository.getAll().size());
        } catch (AllocationException ae) {
            fail(ae.getMessage());
        }
    }

    @Test
    void updatePass() {
        try {
            Client client = (Client) userRepository.getAll().getFirst();
            VMAllocation vmAllocation = allocationRepository.add(client, resourceRepository.getAll().getFirst(), Instant.now());

            assertEquals(1, allocationRepository.getActive(client).size());
            assertEquals(0, allocationRepository.getPast(client).size());

            assertDoesNotThrow(() -> allocationRepository.finishAllocation(vmAllocation.getId()));

            assertEquals(0, allocationRepository.getActive(client).size());
            assertEquals(1, allocationRepository.getPast(client).size());
        } catch (UserException ue) {
            fail(ue.getMessage());
        }
    }

    @Test
    void updateFailInvalidId() {
        assertThrows(AllocationIdException.class, () -> allocationRepository.finishAllocation(""));
    }

    @Test
    void updateFailNotFound() {
        String id = new ObjectId().toHexString();
        assertThrows(AllocationNotFoundException.class, () -> allocationRepository.finishAllocation(id));
    }

    @Test
    void findByIdPass() {
        allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());

        VMAllocation vmAllocation = allocationRepository.getAll().getFirst();
        assertNotNull(vmAllocation);

        try {
            assertEquals(vmAllocation, allocationRepository.findById(vmAllocation.getId()));
        } catch (AllocationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void findByIdFail() {
        assertThrows(AllocationNotFoundException.class, () -> allocationRepository.findById(ObjectId.get().toHexString()));

        assertThrows(AllocationIdException.class, () -> allocationRepository.findById("wrongID"));
        assertThrows(AllocationIdException.class, () -> allocationRepository.findById(""));
        assertThrows(AllocationIdException.class, () -> allocationRepository.findById(null));
    }

    @Test
    void findByIds() {
        try {
            allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());
            allocationRepository.finishAllocation(allocationRepository.getAll().getFirst().getId());
            allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());

            VMAllocation vmAllocation = allocationRepository.getAll().getFirst();
            VMAllocation vmAllocation2 = allocationRepository.getAll().getLast();

            assertNotEquals(vmAllocation, vmAllocation2);

            assertNotNull(vmAllocation);
            assertNotNull(vmAllocation2);
        } catch (AllocationException ae) {
            fail(ae.getMessage());
        }
    }

    @Test
    void deletePass() {
        VMAllocation vmAllocation = allocationRepository.add((Client) userRepository.getAll().getFirst(), resourceRepository.getAll().getFirst(), Instant.now());

        assertEquals(1, allocationRepository.getAll().size());

        assertDoesNotThrow(() -> allocationRepository.delete(vmAllocation.getId()));

        assertEquals(0, allocationRepository.getAll().size());
    }

    @Test
    void deleteFailInvalidId() {
        assertThrows(AllocationIdException.class, () -> allocationRepository.delete(""));
    }

    @Test
    void deleteFailNotFound() {
        String id = ObjectId.get().toHexString();
        assertThrows(AllocationNotFoundException.class, () -> allocationRepository.delete(id));
    }
}