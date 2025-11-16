package team.four.pas;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import team.four.pas.repositories.entities.UserEntity;
import team.four.pas.repositories.entities.VMAllocationEntity;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.repositories.mappers.*;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Admin;
import team.four.pas.services.data.users.Client;
import team.four.pas.repositories.implementation.MongoAllocationRepository;
import team.four.pas.repositories.implementation.MongoResourceRepository;
import team.four.pas.repositories.implementation.MongoUserRepository;

@Configuration
public class Config {

    private Admin theOneAndOnly = new Admin(null, "BLis", "Bartosz", "Lis");
    private Client wdiStudent = new Client(null, "JKernel", "Janek", "Kernel");
    private VirtualMachine strongestWdiVM = new VirtualMachine(null , 1, 1, 10);


    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

    @Bean
    public VMAllocationMapper vmAllocationMapper() {
        return Mappers.getMapper(VMAllocationMapper.class);
    }

    @Bean
    public VirtualMachineMapper virtualMachineMapper() {
        return Mappers.getMapper(VirtualMachineMapper.class);
    }

    @Bean
    public MongoClient mongoClient(@Value("${pas.data.mongodb.uri:mongodb://localhost:27017/pas}") String connString,
                                   @Value("${pas.data.entities-package:team.four.pas.repositories.entities}") String entitiesPackage) {
        PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                .automatic(true)
                .register(entitiesPackage)
                .build();

        CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                defaultCodecRegistry,
                CodecRegistries.fromProviders(pojoCodecProvider)
        );

        UuidRepresentation uuidRepresentation = UuidRepresentation.STANDARD;

        System.out.println(connString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connString))
                .uuidRepresentation(uuidRepresentation)
                .codecRegistry(pojoCodecRegistry)
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoUserRepository MongoUserRepository(MongoClient mongoClient, UserMapper mapper, @Value("${pas.data.entities-package:pas}") String dbName) {
        MongoCollection<UserEntity> userColl = mongoClient.getDatabase(dbName).getCollection("users", UserEntity.class);

        var mongoUserRepo = new MongoUserRepository(userColl, mapper);

        mongoUserRepo.add(theOneAndOnly.getLogin(), theOneAndOnly.getName(), theOneAndOnly.getSurname(), Admin.class);

        return mongoUserRepo;
    }

    @Bean
    public MongoAllocationRepository mongoAllocationRepository(MongoClient mongoClient, VMAllocationMapper mapper, @Value("${pas.data.entities-package:pas}") String dbName) {
        MongoCollection<VMAllocationEntity> vmAllocationsColl = mongoClient.getDatabase(dbName).getCollection("vmAllocations", VMAllocationEntity.class);

        var mongoAllocationRepo = new MongoAllocationRepository(vmAllocationsColl, mapper);

        //mongoAllocationRepo.addAllocation( Instant.now());

        return mongoAllocationRepo;
    }

    @Bean
    public MongoResourceRepository mongoResourceRepository(MongoClient mongoClient, VirtualMachineMapper mapper, @Value("${pas.data.entities-package:pas}") String dbName) {
        MongoCollection<VirtualMachineEntity> vmColl = mongoClient.getDatabase(dbName).getCollection("virtualMachines", VirtualMachineEntity.class);

        var mongoResourceRepo = new MongoResourceRepository(vmColl, mapper);

        mongoResourceRepo.addVM(strongestWdiVM.getCpuNumber(), strongestWdiVM.getRamGiB(), strongestWdiVM.getStorageGiB());

        return mongoResourceRepo;
    }

}
