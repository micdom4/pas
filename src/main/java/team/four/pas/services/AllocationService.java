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

    List<VMAllocation> getPast(VirtualMachine resource);
    List<VMAllocation> getActive(VirtualMachine resource);

    List<VMAllocation> getActive(Client client);
    List<VMAllocation> getPast(Client client);

    void finishAllocation(String id);

    boolean delete(String id);
}
