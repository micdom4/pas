package team.four.pas.repositories;

import com.mongodb.client.MongoCollection;
import team.four.pas.data.allocations.VMAllocation;
import team.four.pas.data.resources.VirtualMachine;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class MongoAllocationRepository implements AllocationRepository{
    public final MongoCollection<VMAllocation> allocationCollection;

    public MongoAllocationRepository(MongoCollection<VMAllocation> allocationCollection) {
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
