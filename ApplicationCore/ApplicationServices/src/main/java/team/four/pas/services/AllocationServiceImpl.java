package team.four.pas.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.exceptions.allocation.*;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.user.UserIdException;
import team.four.pas.exceptions.user.UserNotFoundException;
import team.four.pas.exceptions.user.UserTypeException;
import team.four.pas.outside.AllocationWebPort;
import team.four.pas.outside.ResourceWebPort;
import team.four.pas.outside.UserWebPort;
import team.four.pas.data.allocations.VMAllocation;
import team.four.pas.data.resources.VirtualMachine;
import team.four.pas.data.users.Client;
import team.four.pas.data.users.User;
import team.four.pas.inside.AllocationPersistencePort;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationWebPort {
    private final AllocationPersistencePort allocationPort;
    private final UserWebPort userService;
    private final ResourceWebPort resourceService;

    @Override
    public List<VMAllocation> getAll() {
        return allocationPort.findAll();
    }

    @Override
    public VMAllocation findById(String id) throws AllocationIdException, AllocationNotFoundException {
        return allocationPort.findById(id).orElseThrow(() -> new AllocationNotFoundException("Not found"));
    }

    @Override
    public VMAllocation add(String clientId, String resourceId, Instant startTime) throws UserTypeException, InactiveClientException, ResourceAlreadyAllocatedException, ResourceIdException {
        User client = userService.findById(clientId);
        VirtualMachine resource = resourceService.findById(resourceId);

        if (client.getClass() != Client.class) {
            throw new UserTypeException("Client must be of Type CLIENT");
        }

        if (!client.isActive()) {
            throw new InactiveClientException("Client must be active in order to allocate a vm");
        }

        if (allocationPort.findByVmIdAndEndTimeIsNull(resourceId).isEmpty()) {
            return allocationPort.insert(new VMAllocation(null, (Client) client, resource, startTime, null));
        } else {
            throw new ResourceAlreadyAllocatedException("Resource is already allocated");
        }
    }

    @Override
    public List<VMAllocation> getPastVm(String id) throws ResourceIdException, ResourceNotFoundException {
        return allocationPort.findByVmIdAndEndTimeIsNotNull(id);
    }

    @Override
    public List<VMAllocation> getActiveVm(String id) throws ResourceIdException, ResourceNotFoundException {
        return allocationPort.findByVmIdAndEndTimeIsNull(id);
    }

    @Override
    public List<VMAllocation> getActiveClient(String id) throws UserTypeException, UserNotFoundException, UserIdException {
        User user = userService.findById(id);
        if (user.getClass() != Client.class) {
            throw new UserTypeException("Client must be of Type CLIENT");
        }

        return allocationPort.findByClientIdAndEndTimeIsNull(id);
    }

    @Override
    public List<VMAllocation> getPastClient(String id) throws UserTypeException, UserNotFoundException, UserIdException {
        User user = userService.findById(id);
        if (user.getClass() != Client.class) {
            throw new UserTypeException("Client must be of Type CLIENT");
        }

        return allocationPort.findByClientIdAndEndTimeIsNotNull(id);
    }

    @Override
    public void finishAllocation(String id) throws AllocationIdException, AllocationNotFoundException {
        allocationPort.finishAllocation(id, Instant.now());
    }

    @Override
    public void delete(String id) throws AllocationIdException, AllocationNotFoundException, AllocationNotActiveException {
        if (findById(id).getEndTime() == null) {
            allocationPort.deleteById(id);
        } else {
            throw new AllocationNotActiveException("Allocation is not active");
        }
    }
}
