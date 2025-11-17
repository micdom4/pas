package team.four.pas.services;

import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;

import java.time.Instant;
import java.util.List;

public interface AllocationService {
    List<VMAllocation> getAll();

    VMAllocation findById(String id);

    VMAllocation add(UserDTO client, VirtualMachine resource, Instant startTime);

    List<VMAllocation> getPastVm(String id);

    List<VMAllocation> getActiveVm(String id);

    List<VMAllocation> getActiveClient(String id);

    List<VMAllocation> getPastClient(String id);

    void finishAllocation(String id);

    void delete(String id);
}
