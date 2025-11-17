package team.four.pas.services.implementation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.services.AllocationService;
import team.four.pas.services.data.allocations.VMAllocation;
import team.four.pas.services.data.resources.VirtualMachine;
import team.four.pas.services.data.users.Client;
import team.four.pas.services.mappers.UserToDTO;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {
    private @NonNull final AllocationRepository allocationRepository;
    private @NonNull final UserToDTO userToDTO;


    @Override
    public List<VMAllocation> getAll() {
        return allocationRepository.getAll();
    }

    @Override
    public VMAllocation findById(String id) {
        return allocationRepository.findById(id);
    }

    @Override
    public VMAllocation add(UserDTO client, VirtualMachine resource, Instant startTime) {
        if (client.type() != UserType.CLIENT) {
            //exception
        }

        if(client.active() && allocationRepository.getActive(resource).isEmpty()) {
            return allocationRepository.add(userToDTO.clientFromClientDTO(client), resource, startTime);
        } else {
            //exception
            throw new RuntimeException();
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

    @Override
    public void finishAllocation(String id) {
        allocationRepository.finishAllocation(id);
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
