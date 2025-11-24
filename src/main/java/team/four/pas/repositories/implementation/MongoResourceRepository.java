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
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.repositories.mappers.StringToObjectId;
import team.four.pas.repositories.mappers.VirtualMachineMapper;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public VirtualMachine addVM(int cpuNumber, int ram, int storage) {
        VirtualMachineEntity newVM = new VirtualMachineEntity(null, cpuNumber, ram, storage);

        InsertOneResult result = resourceCollection.insertOne(newVM);
        ObjectId id = Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue();
        Bson filter = Filters.eq("_id", id);
        VirtualMachineEntity entity = resourceCollection.find(filter).first();

        return mapper.toData(entity);
    }

    @Override
    public VirtualMachine updateVM(String id, int cpuNumber, int ram, int storage) throws ResourceNotFoundException, ResourceIdException {
        ObjectId objectId = getObjectId(id);

        Bson filter = Filters.eq("_id", objectId);

        Bson update = Updates.combine(
                Updates.set("cpuNumber", cpuNumber),
                Updates.set("ramGiB", ram),
                Updates.set("storageGiB", storage)
        );

        UpdateResult result = resourceCollection.updateOne(filter, update);

        if (result.getModifiedCount() == 0) {
            throw new ResourceNotFoundException("No VM found with ID: " + id);
        } else if (result.getModifiedCount() != 1) {
            throw new MongoException("Error while updating VM " + id);
        }

        VirtualMachineEntity entity = resourceCollection.find(filter).first();

        return mapper.toData(entity);
    }

    @Override
    public List<VirtualMachine> getAll() {
        return resourceCollection.find()
                .map(mapper::toData)
                .into(new ArrayList<>());
    }

    @Override
    public VirtualMachine findById(String id) throws ResourceNotFoundException, ResourceIdException {
        ObjectId objectId = getObjectId(id);

        Bson filter = Filters.eq("_id", objectId);

        VirtualMachineEntity entity = resourceCollection.find(filter).first();

        if (entity == null) {
            throw new ResourceNotFoundException("No VM found with ID: " + id);
        }

        return mapper.toData(entity);
    }

    @Override
    public void delete(String id) throws ResourceNotFoundException, ResourceIdException {
        ObjectId objectId = getObjectId(id);

        Bson filter = Filters.eq("_id", objectId);

        DeleteResult result = resourceCollection.deleteOne(filter);

        if (result.getDeletedCount() == 0) {
            throw new ResourceNotFoundException("No VM found with ID " + id);
        } else if (result.getDeletedCount() != 1) {
            throw new MongoException("Error while deleting VM " + id);
        }
    }

    private ObjectId getObjectId(String id) throws ResourceIdException {
        ObjectId objectId;

        try {
            objectId = idMapper.stringToObjectId(id);
        } catch (IllegalArgumentException e) {
            throw new ResourceIdException("Invalid ID: " + id);
        }

        if (objectId == null) {
            throw new ResourceIdException("Invalid ID: " + id);
        }

        return objectId;
    }
}
