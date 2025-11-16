package team.four.pas.services.implementation;

import org.springframework.stereotype.Service;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.services.AllocationService;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;
import java.util.List;

@Service
public class AllocationServiceImpl implements AllocationService {
    private final AllocationRepository allocationRepository;

    public AllocationServiceImpl(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    @Override
    public List<VMAllocation> getAll() {
        return allocationRepository.getAll();
    }

    @Override
    public VMAllocation findById(String id) {
        return allocationRepository.findById(id);
    }

    @Override
    public List<VMAllocation> findById(List<String> id) {
        return allocationRepository.findById(id);
    }

    @Override
    public boolean add(Client client, VirtualMachine resource, Instant startTime) {
        if(client.isActive() && allocationRepository.getActive(resource).isEmpty()) {
            return allocationRepository.add(client, resource, startTime);
        } else {
            return false;
        }
    }

    @Override
    public List<VMAllocation> getPast(VirtualMachine resource) {
        return allocationRepository.getPast(resource);
    }

    @Override
    public List<VMAllocation> getActive(VirtualMachine resource) {
        return allocationRepository.getActive(resource);
    }

    @Override
    public List<VMAllocation> getActive(Client client) {
        return allocationRepository.getActive(client);
    }

    @Override
    public List<VMAllocation> getPast(Client client) {
        return allocationRepository.getPast(client);
    }
}
