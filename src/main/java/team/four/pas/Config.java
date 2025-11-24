package team.four.pas;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.four.pas.repositories.entities.UserEntity;
import team.four.pas.repositories.entities.VMAllocationEntity;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.repositories.implementation.MongoAllocationRepository;
import team.four.pas.repositories.implementation.MongoResourceRepository;
import team.four.pas.repositories.implementation.MongoUserRepository;
import team.four.pas.repositories.mappers.StringToObjectId;
import team.four.pas.repositories.mappers.UserMapper;
import team.four.pas.repositories.mappers.VMAllocationMapper;
import team.four.pas.repositories.mappers.VirtualMachineMapper;
import team.four.pas.services.mappers.ResourceToDTO;
import team.four.pas.services.mappers.UserToDTO;

@Configuration
public class Config {


    @Bean
    public StringToObjectId idMapper() {
        return Mappers.getMapper(StringToObjectId.class);
    }

    @Bean
    public UserToDTO userDTOMapper() {
        return Mappers.getMapper(UserToDTO.class);
    }

    @Bean
    public ResourceToDTO resourceDTOMapper() {
        return Mappers.getMapper(ResourceToDTO.class);
    }

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


        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connString))
                .codecRegistry(pojoCodecRegistry)
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoUserRepository MongoUserRepository(MongoClient mongoClient, UserMapper mapper, StringToObjectId idMapper, @Value("${pas.data.mongodb.database:pas}") String dbName) {
        MongoCollection<UserEntity> userColl = mongoClient.getDatabase(dbName).getCollection("users", UserEntity.class);


        return new MongoUserRepository(userColl, mapper, idMapper);
    }

    @Bean
    public MongoAllocationRepository mongoAllocationRepository(MongoClient mongoClient, VMAllocationMapper mapper,
                                                               @Value("${pas.data.mongodb.database:pas}") String dbName,
                                                               UserMapper userMapper, VirtualMachineMapper vmMapper,
                                                               StringToObjectId idMapper) {
        MongoCollection<VMAllocationEntity> vmAllocationsColl = mongoClient.getDatabase(dbName).getCollection("vmAllocations", VMAllocationEntity.class);

        return new MongoAllocationRepository(vmAllocationsColl, mapper, userMapper, idMapper, vmMapper);
    }

    @Bean
    public MongoResourceRepository mongoResourceRepository(MongoClient mongoClient, VirtualMachineMapper mapper, StringToObjectId idMapper, @Value("${pas.data.mongodb.database:pas}") String dbName) {
        MongoCollection<VirtualMachineEntity> vmColl = mongoClient.getDatabase(dbName).getCollection("virtualMachines", VirtualMachineEntity.class);

        return new MongoResourceRepository(vmColl, mapper, idMapper);
    }

}
