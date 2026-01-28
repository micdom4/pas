package team.four.pas.services;

import team.four.pas.exceptions.allocation.*;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.user.UserIdException;
import team.four.pas.exceptions.user.UserNotFoundException;
import team.four.pas.exceptions.user.UserTypeException;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.User;

import java.time.Instant;
import java.util.List;

public interface AllocationService {
    List<VMAllocation> getAll();

    VMAllocation findById(String id) throws AllocationIdException, AllocationNotFoundException;

    VMAllocation add(String clientId, String resourceId, Instant startTime) throws UserTypeException, InactiveClientException, ResourceAlreadyAllocatedException, ResourceIdException;

    List<VMAllocation> getPastVm(String id) throws ResourceIdException, ResourceNotFoundException;

    List<VMAllocation> getActiveVm(String id) throws ResourceIdException, ResourceNotFoundException;

    List<VMAllocation> getActiveClient(String id) throws UserTypeException, UserNotFoundException, UserIdException;

    List<VMAllocation> getPastClient(String id) throws UserTypeException, UserNotFoundException, UserIdException;

    void finishAllocation(String id) throws AllocationIdException, AllocationNotFoundException;

    void delete(String id) throws AllocationIdException, AllocationNotFoundException, AllocationNotActiveException;
}
