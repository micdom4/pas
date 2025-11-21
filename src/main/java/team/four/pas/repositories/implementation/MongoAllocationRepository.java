package team.four.pas.repositories.implementation;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import team.four.pas.exceptions.allocation.AllocationClientException;
import team.four.pas.exceptions.allocation.AllocationIdException;
import team.four.pas.exceptions.allocation.AllocationNotFoundException;
import team.four.pas.exceptions.allocation.AllocationResourceException;
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
import java.util.List;

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
        return allocationCollection.find()
                .map(VMAllocationMapper::toData)
                .into(new ArrayList<>());
    }

    @Override
    public VMAllocation findById(String id) throws AllocationIdException, AllocationNotFoundException {
        try {
            ObjectId objectId = idMapper.stringToObjectId(id);
            if (objectId == null) {
                throw new AllocationIdException("Allocation ID cannot be empty");
            }


            Bson filter = Filters.eq("_id", objectId);
            VMAllocationEntity entity = allocationCollection.find(filter).first();

            if (entity == null) {
                throw new AllocationNotFoundException("No Allocation found with ID: " + id);
            }


            return VMAllocationMapper.toData(entity);
        } catch (IllegalArgumentException e) {
            throw new AllocationNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<VMAllocation> getActive(Client user) throws AllocationClientException {
        ObjectId objectId = idMapper.stringToObjectId(user.getId());

        if (objectId == null) {
            throw new AllocationClientException("Client cannot be null");
        }

        Bson filter = Filters.and(
                Filters.eq("client._id", objectId),
                Filters.eq("endTime", null)
        );

        return allocationCollection.find(filter)
                .map(VMAllocationMapper::toData)
                .into(new ArrayList<>());
    }

    @Override
    public List<VMAllocation> getActive(VirtualMachine resource) throws AllocationResourceException {
        ObjectId objectId = idMapper.stringToObjectId(resource.getId());

        if (objectId == null) {
            throw new AllocationResourceException("Resource cannot be null");
        }

        Bson filter = Filters.and(
                Filters.eq("vm._id", objectId),
                Filters.eq("endTime", null)
        );

        return allocationCollection.find(filter)
                .map(VMAllocationMapper::toData)
                .into(new ArrayList<>());
    }

    @Override
    public List<VMAllocation> getPast(Client user) throws AllocationClientException {
        ObjectId objectId = idMapper.stringToObjectId(user.getId());

        if (objectId == null) {
            throw new AllocationClientException("Client cannot be null");
        }

        Bson filter = Filters.and(
                Filters.eq("client._id", objectId),
                Filters.ne("endTime", null)
        );

        return allocationCollection.find(filter)
                .map(VMAllocationMapper::toData)
                .into(new ArrayList<>());
    }

    @Override
    public List<VMAllocation> getPast(VirtualMachine resource) throws AllocationResourceException {
        ObjectId objectId = idMapper.stringToObjectId(resource.getId());

        if (objectId == null) {
            throw new AllocationResourceException("Resource cannot be null");
        }

        Bson filter = Filters.and(
                Filters.eq("vm._id", objectId),
                Filters.ne("endTime", null)
        );

        return allocationCollection.find(filter)
                .map(VMAllocationMapper::toData)
                .into(new ArrayList<>());
    }

    @Override
    public VMAllocation add(Client client, VirtualMachine resource, Instant startTime) {
        UserEntity clientEntity = userMapper.toEntity(client);
        VirtualMachineEntity vmEntity = vmMapper.toEntity(resource);

        VMAllocationEntity entity = new VMAllocationEntity(
                null, clientEntity, vmEntity, startTime, null
        );

        InsertOneResult result = allocationCollection.insertOne(entity);
        if (!result.wasAcknowledged()) {
            throw new MongoException("Error while creating allocation");
        }

        String id = idMapper.objectIdToString(result.getInsertedId().asObjectId().getValue());
        try {
            return findById(id);
        } catch (AllocationIdException | AllocationNotFoundException e) {
            throw new MongoException("Error while creating allocation");
        }
    }

    @Override
    public void finishAllocation(String id) throws AllocationIdException, AllocationNotFoundException {
        ObjectId objectId = idMapper.stringToObjectId(id);

        if (objectId == null) {
            throw new AllocationIdException("Allocation ID cannot be empty");
        }

        Bson filter = Filters.eq("_id", objectId);
        Bson update = Updates.set("endTime", Instant.now());

        UpdateResult result = allocationCollection.updateOne(filter, update);
        if (result.getModifiedCount() == 0) {
            throw new AllocationNotFoundException("No Allocation found with ID: " + id);
        } else if (result.getModifiedCount() != 1) {
            throw new MongoException("Error while finishing allocation with ID = " + id);
        }
    }

}

