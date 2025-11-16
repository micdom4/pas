package team.four.pas.services;

import org.springframework.stereotype.Service;
import team.four.pas.repositories.AllocationRepository;

import java.time.Instant;

@Service
public class AllocationServiceImpl {
    AllocationRepository allocationRepository;

    public AllocationServiceImpl(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    public synchronized boolean addAllocation(String clientId, String vmId, Instant start) {
        return false;
    }
}
