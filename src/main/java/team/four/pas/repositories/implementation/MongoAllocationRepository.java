package team.four.pas.repositories.implementation;

import com.mongodb.client.MongoCollection;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.entities.VMAllocationEntity;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class MongoAllocationRepository implements AllocationRepository {
    public final MongoCollection<VMAllocationEntity> allocationCollection;

    public MongoAllocationRepository(MongoCollection<VMAllocationEntity> allocationCollection) {
        this.allocationCollection = allocationCollection;
    }

    @Override
    public List<VMAllocation> getAll() {
        return List.of();
    }

    @Override
    public VMAllocation findById(UUID id) {
        return null;
    }

    @Override
    public List<VMAllocation> findById(List<UUID> id) {
        return List.of();
    }

    @Override
    public boolean delete(UUID id) {
        return false;
    }

    public List<VMAllocation> getActive(User user) {
    }

    public List<VMAllocation> getActive(VirtualMachine resource) {
    }

    public List<VMAllocation> getPast(User user) {
    }

    public List<VMAllocation> getPast(VirtualMachine resource) {
    }

    public boolean addAllocation(Client client, VirtualMachine resource, Instant startTime) {

        return true;
    }

    public boolean finishAllocation(UUID id) {

        return true;
    }

}
