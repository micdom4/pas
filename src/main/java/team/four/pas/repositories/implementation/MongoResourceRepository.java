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
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.repositories.mappers.VirtualMachineMapper;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MongoResourceRepository implements ResourceRepository {

    private final MongoCollection<VirtualMachineEntity> resourceCollection;
    private final VirtualMachineMapper mapper;

    public MongoResourceRepository(MongoCollection<VirtualMachineEntity> resourceCollection, VirtualMachineMapper mapper) {
        this.resourceCollection = resourceCollection;
        this.mapper = mapper;
    }

    @Override
    public boolean addVM(int cpuNumber, int ram, int memory) {
        try {
            VirtualMachineEntity newVM = new VirtualMachineEntity(null, cpuNumber,
                                                                     ram, memory);

            InsertOneResult result = resourceCollection.insertOne(newVM);

            return result.wasAcknowledged();
        } catch (MongoException e) {
            System.out.println("Failed to add VM " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateVM(String id, int cpuNumber, int ram, int memory) {
        ObjectId objectId;

        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            return false;
        }

        Bson filter = Filters.eq("_id", objectId);

        Bson update = Updates.combine(
                Updates.set("cpuNumber", cpuNumber),
                Updates.set("ramGiB", ram),
                Updates.set("storageGiB", memory)
        );

        try {
            UpdateResult result = resourceCollection.updateOne(filter, update);

            return result.getModifiedCount() == 1;
        } catch (MongoException e) {
            return false;
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
            objectId = mapper.stringToObjectId(id);
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

    @Override
    public List<VirtualMachine> findById(List<String> ids) {
        List<ObjectId> objectIds = ids.stream()
                                      .map(mapper::stringToObjectId)
                                      .filter(java.util.Objects::nonNull)
                                      .collect(Collectors.toList());

        if (objectIds.isEmpty()) {
            return Collections.emptyList();
        }

        Bson filter = Filters.in("_id", objectIds);

        try {
            return resourceCollection.find(filter)
                                     .map(mapper::toData)
                                     .into(new ArrayList<>());
        } catch (MongoException e) {
            System.err.println("Error finding VMs by ID list: " + e.getMessage());
            return Collections.emptyList();
        }    }

    public boolean delete(String id) {
        ObjectId objectId;
        try {
            objectId = mapper.stringToObjectId(id);
            if (objectId == null) {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return false;
        }

        Bson filter = Filters.eq("_id", objectId);

        try {
            DeleteResult result = resourceCollection.deleteOne(filter);
            return result.getDeletedCount() == 1;
        } catch (MongoException e) {
            System.err.println("Error deleting VM by ID: " + e.getMessage());
            return false;
        }
    }
}
