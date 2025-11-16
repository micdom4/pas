package team.four.pas;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.four.pas.data.allocations.VMAllocation;
import team.four.pas.data.resources.VirtualMachine;
import team.four.pas.data.users.Admin;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.User;
import team.four.pas.repositories.MongoAllocationRepository;
import team.four.pas.repositories.MongoResourceRepository;
import team.four.pas.repositories.MongoUserRepository;

import java.time.Instant;
import java.util.UUID;

@Configuration
public class Config {

    @Value("${pas.data.mongodb.uri}")
    public String connString;

    @Value("${pas.data.mongodb.database}")
    public String dbName;

    private Admin theOneAndOnly = new Admin(UUID.randomUUID(), "BLis", "Bartosz", "Lis");
    private Client wdiStudent = new Client(UUID.randomUUID(), "JKernel", "Janek", "Kernel");
    private VirtualMachine strongestWdiVM = new VirtualMachine(UUID.randomUUID(), 1, 1, 10);

    @Bean
    public MongoClient mongoClient() {
        MongoClient mongoClient = MongoClients.create(connString);

        return mongoClient;
    }

    @Bean
    public MongoUserRepository MongoUserRepository(MongoClient mongoClient) {
        MongoCollection<User> userColl = mongoClient.getDatabase(dbName).getCollection("users", User.class);

        var mongoUserRepo = new MongoUserRepository(userColl);

        mongoUserRepo.add(theOneAndOnly.getLogin(), theOneAndOnly.getName(), theOneAndOnly.getSurname(), Admin.class);

        return mongoUserRepo;
    }

    @Bean
    public MongoAllocationRepository mongoAllocationRepository(MongoClient mongoClient) {
        MongoCollection<VMAllocation> vmAllocationsColl = mongoClient.getDatabase(dbName).getCollection("vmAllocations", VMAllocation.class);

        var mongoAllocationRepo = new MongoAllocationRepository(vmAllocationsColl);

        mongoAllocationRepo.addAllocation(, , Instant.now());

        return mongoAllocationRepo;
    }

    @Bean
    public MongoResourceRepository mongoResourceRepository(MongoClient mongoClient) {
        MongoCollection<VirtualMachine> vmColl = mongoClient.getDatabase(dbName).getCollection("virtualMachines", VirtualMachine.class);

        var mongoResourceRepo = new MongoResourceRepository(vmColl);

        mongoResourceRepo.addVM(strongestWdiVM.getCpuNumber(), strongestWdiVM.getRamGiB(), strongestWdiVM.getStorageGiB();

        return mongoResourceRepo;
    }

}
