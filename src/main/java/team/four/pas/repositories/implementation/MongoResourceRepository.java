package team.four.pas.repositories.implementation;

import com.mongodb.client.MongoCollection;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.repositories.entities.VirtualMachineEntity;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;
import java.util.UUID;

public class MongoResourceRepository implements ResourceRepository {

    public final MongoCollection<VirtualMachineEntity> resourceCollection;

    public MongoResourceRepository(MongoCollection<VirtualMachineEntity> resourceCollection) {
        this.resourceCollection = resourceCollection;
    }

    @Override
    public boolean addVM(int cpuNumber, int ram, int memory) {
        return false;
    }

    @Override
    public boolean updateVM(UUID id, int cpuNumber, int ram, int memory) {
        return false;
    }

    @Override
    public List<VirtualMachine> getAll() {
        return List.of();
    }

    @Override
    public VirtualMachine findById(UUID id) {
        return null;
    }

    @Override
    public List<VirtualMachine> findById(List<UUID> id) {
        return List.of();
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }
}
