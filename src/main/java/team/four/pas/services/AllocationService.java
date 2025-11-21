package team.four.pas.services;

import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.exceptions.allocation.*;
import team.four.pas.exceptions.resource.ResourceFindException;
import team.four.pas.exceptions.user.UserFindException;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;

import java.time.Instant;
import java.util.List;

public interface AllocationService {
    List<VMAllocation> getAll();

    VMAllocation findById(String id) throws AllocationIdException, AllocationNotFoundException;

    VMAllocation add(UserDTO client, VirtualMachine resource, Instant startTime) throws AllocationClientException, AllocationResourceException, InactiveClientException, ResourceAlreadyAllocatedException;

    List<VMAllocation> getPastVm(String id) throws ResourceFindException, AllocationResourceException;

    List<VMAllocation> getActiveVm(String id) throws ResourceFindException, AllocationResourceException;

    List<VMAllocation> getActiveClient(String id) throws UserFindException, AllocationClientException;

    List<VMAllocation> getPastClient(String id) throws UserFindException, AllocationClientException;

    void finishAllocation(String id) throws AllocationIdException, AllocationNotFoundException;

    void delete(String id);
}
