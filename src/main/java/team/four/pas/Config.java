package team.four.pas;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mapstruct.factory.Mappers;
import team.four.pas.controllers.DTOs.mappers.ResourceToDTO;
import team.four.pas.controllers.DTOs.mappers.UserToDTO;
import team.four.pas.repositories.implementation.MongoAllocationRepository;
import team.four.pas.repositories.implementation.MongoResourceRepository;
import team.four.pas.repositories.implementation.MongoUserRepository;
import team.four.pas.repositories.entities.UserEntity;
import team.four.pas.repositories.entities.VMAllocationEntity;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.repositories.mappers.UserMapper;
import team.four.pas.repositories.mappers.VMAllocationMapper;
import team.four.pas.repositories.mappers.VirtualMachineMapper;
import team.four.pas.repositories.mappers.StringToObjectId;

@ApplicationScoped
public class Config {

    @Produces
    public StringToObjectId idMapper() {
        return Mappers.getMapper(StringToObjectId.class);
    }

    @Produces
    public UserToDTO userDTOMapper() {
        return Mappers.getMapper(UserToDTO.class);
    }

    @Produces
    public ResourceToDTO resourceDTOMapper() {
        return Mappers.getMapper(ResourceToDTO.class);
    }

    @Produces
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }

    @Produces
    public VMAllocationMapper vmAllocationMapper() {
        return Mappers.getMapper(VMAllocationMapper.class);
    }

    @Produces
    public VirtualMachineMapper virtualMachineMapper() {
        return Mappers.getMapper(VirtualMachineMapper.class);
    }

    @Produces
    @ApplicationScoped
    @Alternative
    @Priority(1)
    public MongoClient mongoClient(@ConfigProperty(name = "pas.data.mongodb.uri", defaultValue = "mongodb://localhost:27017/pas") String connString,
                                   @ConfigProperty(name = "pas.data.entities-package", defaultValue = "team.four.pas.repositories.entities") String entitiesPackage) {
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

    @Produces
    public MongoUserRepository mongoUserRepository(MongoClient mongoClient, UserMapper mapper, StringToObjectId idMapper, @ConfigProperty(name = "pas.data.mongodb.database", defaultValue = "pas") String dbName) {
        MongoCollection<UserEntity> userColl = mongoClient.getDatabase(dbName).getCollection("users", UserEntity.class);
        return new MongoUserRepository(userColl, mapper, idMapper);
    }

    @Produces
    public MongoAllocationRepository mongoAllocationRepository(MongoClient mongoClient, VMAllocationMapper mapper,
                                                               @ConfigProperty(name = "pas.data.mongodb.database", defaultValue = "pas") String dbName,
                                                               UserMapper userMapper, VirtualMachineMapper vmMapper,
                                                               StringToObjectId idMapper) {
        MongoCollection<VMAllocationEntity> vmAllocationsColl = mongoClient.getDatabase(dbName).getCollection("vmAllocations", VMAllocationEntity.class);
        return new MongoAllocationRepository(vmAllocationsColl, mapper, userMapper, idMapper, vmMapper);
    }

    @Produces
    public MongoResourceRepository mongoResourceRepository(MongoClient mongoClient, VirtualMachineMapper mapper, StringToObjectId idMapper, @ConfigProperty(name = "pas.data.mongodb.database", defaultValue = "pas") String dbName) {
        MongoCollection<VirtualMachineEntity> vmColl = mongoClient.getDatabase(dbName).getCollection("virtualMachines", VirtualMachineEntity.class);
        return new MongoResourceRepository(vmColl, mapper, idMapper);
    }
}