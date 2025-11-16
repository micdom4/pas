package team.four.pas;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.four.pas.repositories.entities.UserEntity;
import team.four.pas.repositories.entities.VMAllocationEntity;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.repositories.implementation.MongoAllocationRepository;
import team.four.pas.repositories.implementation.MongoResourceRepository;
import team.four.pas.repositories.implementation.MongoUserRepository;

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
        UuidRepresentation uuidRepresentation = UuidRepresentation.STANDARD;

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connString))
                .uuidRepresentation(uuidRepresentation)
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoUserRepository MongoUserRepository(MongoClient mongoClient) {
        MongoCollection<UserEntity> userColl = mongoClient.getDatabase(dbName).getCollection("users", UserEntity.class);

        var mongoUserRepo = new MongoUserRepository(userColl);

        mongoUserRepo.add(theOneAndOnly.getLogin(), theOneAndOnly.getName(), theOneAndOnly.getSurname(), Admin.class);

        return mongoUserRepo;
    }

    @Bean
    public MongoAllocationRepository mongoAllocationRepository(MongoClient mongoClient) {
        MongoCollection<VMAllocationEntity> vmAllocationsColl = mongoClient.getDatabase(dbName).getCollection("vmAllocations", VMAllocationEntity.class);

        var mongoAllocationRepo = new MongoAllocationRepository(vmAllocationsColl);

        //mongoAllocationRepo.addAllocation( Instant.now());

        return mongoAllocationRepo;
    }

    @Bean
    public MongoResourceRepository mongoResourceRepository(MongoClient mongoClient) {
        MongoCollection<VirtualMachineEntity> vmColl = mongoClient.getDatabase(dbName).getCollection("virtualMachines", VirtualMachineEntity.class);

        var mongoResourceRepo = new MongoResourceRepository(vmColl);

        mongoResourceRepo.addVM(strongestWdiVM.getCpuNumber(), strongestWdiVM.getRamGiB(), strongestWdiVM.getStorageGiB());

        return mongoResourceRepo;
    }

}
