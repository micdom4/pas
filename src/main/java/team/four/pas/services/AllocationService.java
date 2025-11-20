package team.four.pas.services;

import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.resource.ResourceFindException;
import team.four.pas.exceptions.user.UserException;
import team.four.pas.exceptions.user.UserFindException;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;
import java.util.List;

public interface AllocationService {
    List<VMAllocation> getAll();

    VMAllocation findById(String id) throws ResourceException, UserException;

    VMAllocation add(UserDTO client, VirtualMachine resource, Instant startTime);

    List<VMAllocation> getPastVm(String id) throws ResourceFindException;

    List<VMAllocation> getActiveVm(String id) throws ResourceFindException;

    List<VMAllocation> getActiveClient(String id) throws UserFindException;

    List<VMAllocation> getPastClient(String id) throws UserFindException;

    void finishAllocation(String id);

    void delete(String id);
}
