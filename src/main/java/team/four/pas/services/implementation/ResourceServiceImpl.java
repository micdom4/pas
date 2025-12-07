package team.four.pas.services.implementation;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.services.ResourceService;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final AllocationRepository allocationRepository;

    @Override
    public List<VirtualMachine> getAll() {
        return resourceRepository.getAll();
    }

    @Override
    public VirtualMachine findById(String id) throws ResourceIdException, ResourceNotFoundException {
        return resourceRepository.findById(id);
    }

    @Override
    public VirtualMachine addVM(VirtualMachine vm) throws ResourceDataException {
        return resourceRepository.addVM(vm.getCpuNumber(), vm.getRamGiB(), vm.getStorageGiB());
    }

    @Override
    public VirtualMachine updateVM(String id, int cpuNumber, int ramGiB, int storageGiB) throws ResourceIdException, ResourceNotFoundException, ResourceDataException {
        return resourceRepository.updateVM(id, cpuNumber, ramGiB, storageGiB);
    }

    @Override
    public void deleteVM(String id) throws ResourceIdException, ResourceNotFoundException, ResourceStillAllocatedException {
        if (allocationRepository.getActive(findById(id)).isEmpty()
                && allocationRepository.getPast(findById(id)).isEmpty()) {
            resourceRepository.delete(id);
        } else {
            throw new ResourceStillAllocatedException("VM with ID: " + id + " is still allocated");
        }
    }
}