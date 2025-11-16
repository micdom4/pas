package team.four.pas.repositories.implementation;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.entities.UserEntity;
import team.four.pas.repositories.entities.VMAllocationEntity;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.repositories.mappers.StringToObjectId;
import team.four.pas.repositories.mappers.UserMapper;
import team.four.pas.repositories.mappers.VMAllocationMapper;
import team.four.pas.repositories.mappers.VirtualMachineMapper;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MongoAllocationRepository implements AllocationRepository {
    private final MongoCollection<VMAllocationEntity> allocationCollection;
    private final VMAllocationMapper VMAllocationMapper;
    private final UserMapper userMapper;
    private final VirtualMachineMapper vmMapper;
    private final StringToObjectId idMapper;

    public MongoAllocationRepository(MongoCollection<VMAllocationEntity> allocationCollection, VMAllocationMapper mapper, UserMapper userMapper, StringToObjectId idMapper, VirtualMachineMapper virtualMachineMapper) {
        this.allocationCollection = allocationCollection;
        this.VMAllocationMapper = mapper;
        this.userMapper = userMapper;
        this.vmMapper = virtualMachineMapper;
        this.idMapper = idMapper;
    }

    @Override
    public List<VMAllocation> getAll() {
        try {
            return allocationCollection.find()
                    .map(VMAllocationMapper::toData)
                    .into(new ArrayList<>());
        } catch (MongoException e) {
            System.err.println("Error getting all allocations: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public VMAllocation findById(String id) {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) return null;

        Bson filter = Filters.eq("_id", objectId);
        try {
            VMAllocationEntity entity = allocationCollection.find(filter).first();
            return VMAllocationMapper.toData(entity);
        } catch (MongoException e) {
            System.err.println("Error finding allocation by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<VMAllocation> findById(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<ObjectId> objectIds = ids.stream()
                                      .map(idMapper::stringToObjectId)
                                      .filter(Objects::nonNull)
                                      .collect(Collectors.toList());

        if (objectIds.isEmpty()) {
            return Collections.emptyList();
        }

        Bson filter = Filters.in("_id", objectIds);
        try {
            return allocationCollection.find(filter)
                    .map(VMAllocationMapper::toData)
                    .into(new ArrayList<>());
        } catch (MongoException e) {
            System.err.println("Error finding allocations by ID list: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<VMAllocation> getActive(Client user) {
        Bson filter = Filters.and(
                Filters.eq("client._id", idMapper.stringToObjectId(user.getId())),
                Filters.eq("endTime", null)
        );
        try {
            return allocationCollection.find(filter)
                    .map(VMAllocationMapper::toData)
                    .into(new ArrayList<>());
        } catch (MongoException e) {
            System.err.println("Error getting active allocations for user: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<VMAllocation> getActive(VirtualMachine resource) {
        Bson filter = Filters.and(
                Filters.eq("vm._id", idMapper.stringToObjectId(resource.getId())),
                Filters.eq("endTime", null)
        );
        try {
            return allocationCollection.find(filter)
                    .map(VMAllocationMapper::toData)
                    .into(new ArrayList<>());
        } catch (MongoException e) {
            System.err.println("Error getting active allocations for VM: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<VMAllocation> getPast(Client user) {
        Bson filter = Filters.and(
                Filters.eq("client._id", idMapper.stringToObjectId(user.getId())),
                Filters.ne("endTime", null)
        );
        try {
            return allocationCollection.find(filter)
                    .map(VMAllocationMapper::toData)
                    .into(new ArrayList<>());
        } catch (MongoException e) {
            System.err.println("Error getting past allocations for user: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<VMAllocation> getPast(VirtualMachine resource) {
        Bson filter = Filters.and(
                Filters.eq("vm._id", idMapper.stringToObjectId(resource.getId())),
                Filters.ne("endTime", null)
        );
        try {
            return allocationCollection.find(filter)
                    .map(VMAllocationMapper::toData)
                    .into(new ArrayList<>());
        } catch (MongoException e) {
            System.err.println("Error getting past allocations for VM: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean add(Client client, VirtualMachine resource, Instant startTime) {
        try {
            UserEntity clientEntity = userMapper.toEntity(client);
            VirtualMachineEntity vmEntity = vmMapper.toEntity(resource);

            System.out.println(clientEntity);
            System.out.println(vmEntity);
            VMAllocationEntity entity = new VMAllocationEntity(
                    null, clientEntity, vmEntity, startTime, null
            );

            InsertOneResult result = allocationCollection.insertOne(entity);
            return result.wasAcknowledged();
        } catch (MongoException e) {
            System.err.println("Error adding allocation: " + e.getMessage());
            return false;
        }
    }

    public boolean finishAllocation(String id) {
        ObjectId objectId = idMapper.stringToObjectId(id);
        if (objectId == null) return false;

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("endTime", Instant.now());

        try {
            UpdateResult result = allocationCollection.updateOne(filter, update);
            return result.getModifiedCount() == 1;
        } catch (MongoException e) {
            System.err.println("Error finishing allocation: " + e.getMessage());
            return false;
        }
    }

}

