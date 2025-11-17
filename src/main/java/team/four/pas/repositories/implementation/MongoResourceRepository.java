package team.four.pas.repositories.implementation;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import team.four.pas.controllers.exceptions.service.AddVMException;
import team.four.pas.controllers.exceptions.service.DeleteVMException;
import team.four.pas.controllers.exceptions.service.UpdateVMException;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.repositories.mappers.StringToObjectId;
import team.four.pas.repositories.mappers.VirtualMachineMapper;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MongoResourceRepository implements ResourceRepository {

    private final MongoCollection<VirtualMachineEntity> resourceCollection;
    private final VirtualMachineMapper mapper;
    private final StringToObjectId idMapper;

    public MongoResourceRepository(MongoCollection<VirtualMachineEntity> resourceCollection, VirtualMachineMapper mapper, StringToObjectId idMapper) {
        this.resourceCollection = resourceCollection;
        this.mapper = mapper;
        this.idMapper = idMapper;
    }

    @Override
    public VirtualMachine addVM(int cpuNumber, int ram, int memory) throws AddVMException {
        try {
            VirtualMachineEntity newVM = new VirtualMachineEntity(null, cpuNumber,
                    ram, memory);

            InsertOneResult result = resourceCollection.insertOne(newVM);
            ObjectId id = Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue();
            Bson filter = Filters.eq("_id", id);
            VirtualMachineEntity entity = resourceCollection.find(filter).first();
            return mapper.toData(entity);

        } catch (Exception e) {
            System.out.println("Failed to add VM " + e.getMessage());
            throw new AddVMException(e.getMessage(), e);
        }
    }

    @Override
    public VirtualMachine updateVM(String id, int cpuNumber, int ram, int memory) throws UpdateVMException {
        ObjectId objectId;

        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new UpdateVMException(e.getMessage(), e);
        }

        Bson filter = Filters.eq("_id", objectId);

        Bson update = Updates.combine(
                Updates.set("cpuNumber", cpuNumber),
                Updates.set("ramGiB", ram),
                Updates.set("storageGiB", memory)
        );

        try {
            UpdateResult result = resourceCollection.updateOne(filter, update);

            VirtualMachineEntity entity = resourceCollection.find(filter).first();

            if (result.getModifiedCount() != 1) {
                throw new UpdateVMException(String.format("Could not update VM %d", id));
            }
            return mapper.toData(entity);
        } catch (Exception e) {
            throw new UpdateVMException(e.getMessage(), e);
        }
    }

    @Override
    public List<VirtualMachine> getAll() {
        try {
            return resourceCollection.find()
                    .map(mapper::toData)
                    .into(new ArrayList<>());
        } catch (MongoException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public VirtualMachine findById(String id) {
        ObjectId objectId;

        try {
            objectId = idMapper.stringToObjectId(id);
            if (objectId == null) {
                return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        Bson filter = Filters.eq("_id", objectId);

        try {
            VirtualMachineEntity entity = resourceCollection.find(filter).first();

            return (entity != null) ? mapper.toData(entity) : null;
        } catch (MongoException e) {
            System.err.println("Error finding VM by ID: " + e.getMessage());
            return null;
        }
    }

    public void delete(String id) throws DeleteVMException {
        ObjectId objectId;
        try {
            objectId = idMapper.stringToObjectId(id);
            if (objectId == null) {
                throw new IllegalArgumentException("Invalid id: " + id);
            }
        } catch (IllegalArgumentException e) {
            throw new DeleteVMException(e.getMessage(), e);
        }

        Bson filter = Filters.eq("_id", objectId);

        try {
            DeleteResult result = resourceCollection.deleteOne(filter);
            if (result.getDeletedCount() != 1) {
                throw new DeleteVMException(String.format("Could not delete VM %d", id));
            }
        } catch (MongoException e) {
            System.err.println("Error deleting VM by ID: " + e.getMessage());
            throw new DeleteVMException(e.getMessage(), e);
        }
    }
}
