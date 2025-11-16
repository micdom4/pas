package team.four.pas.repositories.implementation;

import com.mongodb.client.MongoCollection;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.entities.VMAllocationEntity;
import team.four.pas.repositories.mappers.VMAllocationMapper;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.data.users.User;
import team.four.pas.services.data.allocations.VMAllocation;

import java.time.Instant;
import java.util.List;

public class MongoAllocationRepository implements AllocationRepository {
    private final MongoCollection<VMAllocationEntity> allocationCollection;
    private final VMAllocationMapper mapper;

    public MongoAllocationRepository(MongoCollection<VMAllocationEntity> allocationCollection, VMAllocationMapper mapper) {
        this.allocationCollection = allocationCollection;
        this.mapper = mapper;
    }

    @Override
    public List<VMAllocation> getAll() {
        return List.of();
    }

    @Override
    public VMAllocation findById(String id) {
        return null;
    }

    @Override
    public List<VMAllocation> findById(List<String> id) {
        return List.of();
    }

    public boolean delete(String id) {
        return false;
    }
    /*
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

    public boolean finishAllocation(String id) {

        return true;
    }
    */
}
