package team.four.pas.repositories;

import team.four.pas.exceptions.allocation.AllocationIdException;
import team.four.pas.exceptions.allocation.AllocationNotFoundException;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.user.UserIdException;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;
import java.util.List;

public interface AllocationRepository extends Repository<VMAllocation> {

    @Override
    VMAllocation findById(String id) throws AllocationIdException, AllocationNotFoundException;

    VMAllocation add(Client client, VirtualMachine resource, Instant startTime);

    List<VMAllocation> getPast(VirtualMachine resource) throws ResourceIdException;
    List<VMAllocation> getActive(VirtualMachine resource) throws ResourceIdException;

    List<VMAllocation> getActive(Client client) throws UserIdException;
    List<VMAllocation> getPast(Client client) throws UserIdException;

    void finishAllocation(String allocationId) throws AllocationIdException, AllocationNotFoundException;
}
