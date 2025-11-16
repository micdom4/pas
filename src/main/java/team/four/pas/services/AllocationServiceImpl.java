package team.four.pas.services;

import org.springframework.stereotype.Service;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;

@Service
public class AllocationServiceImpl {
    AllocationRepository allocationRepository;

    public AllocationServiceImpl(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    public synchronized boolean addAllocation(Client client, VirtualMachine virtualMachine, Instant start) {
        if(client.isActive() && allocationRepository.getActive(virtualMachine) == null) {
            allocationRepository.add(client, virtualMachine, start);
            return true;
        }
        return false;
    }
}
