package team.four.pas.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.controllers.DTOs.UserDTO;
import team.four.pas.controllers.DTOs.UserType;
import team.four.pas.exceptions.allocation.*;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.user.UserIdException;
import team.four.pas.exceptions.user.UserNotFoundException;
import team.four.pas.exceptions.user.UserTypeException;
import team.four.pas.repositories.AllocationRepository;
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
    private final AllocationRepository allocationRepository;
    private final UserService userService;
    private final ResourceService resourceService;
    private final UserToDTO userToDTO;


    @Override
    public List<VMAllocation> getAll() {
        return allocationRepository.getAll();
    }

    @Override
    public VMAllocation findById(String id) throws AllocationIdException, AllocationNotFoundException {
        return allocationRepository.findById(id);
    }

    @Override
    public VMAllocation add(UserDTO client, VirtualMachine resource, Instant startTime) throws UserTypeException, InactiveClientException, ResourceAlreadyAllocatedException, ResourceIdException {
        if (client.type() != UserType.CLIENT) {
            throw new UserTypeException("Client must be of UserType CLIENT");
        }

        if (!client.active()) {
            throw new InactiveClientException("Client must be active in order to allocate a resource");
        }

        if (allocationRepository.getActive(resource).isEmpty()) {
            return allocationRepository.add(userToDTO.clientFromClientDTO(client), resource, startTime);
        } else {
            throw new ResourceAlreadyAllocatedException("Resource is already allocated");
        }
    }

    @Override
    public List<VMAllocation> getPastVm(String id) throws ResourceIdException, ResourceNotFoundException {
        VirtualMachine resource = resourceService.findById(id);
        return allocationRepository.getPast(resource);
    }

    @Override
    public List<VMAllocation> getActiveVm(String id) throws ResourceIdException, ResourceNotFoundException {
        VirtualMachine resource = resourceService.findById(id);
        return allocationRepository.getActive(resource);
    }

    @Override
    public List<VMAllocation> getActiveClient(String id) throws UserTypeException, UserNotFoundException, UserIdException {
        UserDTO clientDTO = userService.findById(id);
        if (clientDTO.type() != UserType.CLIENT) {
            throw new UserTypeException("Client must be of UserType CLIENT");
        }

        Client client = userToDTO.clientFromClientDTO(clientDTO);
        return allocationRepository.getActive(client);
    }

    @Override
    public List<VMAllocation> getPastClient(String id) throws UserTypeException, UserNotFoundException, UserIdException {
        UserDTO clientDTO = userService.findById(id);
        if (clientDTO.type() != UserType.CLIENT) {
            throw new UserTypeException("Client must be of UserType CLIENT");
        }
        Client client = userToDTO.clientFromClientDTO(clientDTO);
        return allocationRepository.getPast(client);
    }

    @Override
    public void finishAllocation(String id) throws AllocationIdException, AllocationNotFoundException {
        allocationRepository.finishAllocation(id);
    }

    @Override
    public void delete(String id) {
        //
    }
}
