package team.four.pas.services.implementation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.UserRepository;
import team.four.pas.services.AllocationService;
import team.four.pas.services.ResourceService;
import team.four.pas.services.UserService;
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
    private @NonNull final UserService userService;
    private @NonNull final ResourceService resourceService;
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
    public List<VMAllocation> getPastVm(String id) {
        VirtualMachine resource = resourceService.findById(id);
        return allocationRepository.getPast(resource);
    }

    @Override
    public List<VMAllocation> getActiveVm(String id) {
        VirtualMachine resource = resourceService.findById(id);
        return allocationRepository.getActive(resource);
    }

    @Override
    public List<VMAllocation> getActiveClient(String id) {
        UserDTO clientDTO = userService.findById(id);
        if (clientDTO.type() != UserType.CLIENT) {
            throw new IllegalArgumentException("User is not a client");
        }
        Client client = userToDTO.clientFromClientDTO(clientDTO);
        return allocationRepository.getActive(client);
    }

    @Override
    public List<VMAllocation> getPastClient(String id) {
        UserDTO clientDTO = userService.findById(id);
        if (clientDTO.type() != UserType.CLIENT) {
            throw new IllegalArgumentException("User is not a client");
        }
        Client client = userToDTO.clientFromClientDTO(clientDTO);
        return allocationRepository.getPast(client);
    }

    @Override
    public void finishAllocation(String id) {
        allocationRepository.finishAllocation(id);
    }

    @Override
    public void delete(String id) {
        //
    }
}
