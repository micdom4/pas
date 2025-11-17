package team.four.pas.repositories;

import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;
import java.util.List;

public interface AllocationRepository extends Repository<VMAllocation> {
    VMAllocation add(Client client, VirtualMachine resource, Instant startTime);

    List<VMAllocation> getPast(VirtualMachine resource);
    List<VMAllocation> getActive(VirtualMachine resource);

    List<VMAllocation> getActive(Client client);
    List<VMAllocation> getPast(Client client);

    void finishAllocation(String allocationId);
}
