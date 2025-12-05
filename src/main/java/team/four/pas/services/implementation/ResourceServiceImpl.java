package team.four.pas.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.services.ResourceService;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

@Service
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
        validateCPUs(vm.getCpuNumber());
        validateRAM(vm.getRamGiB());
        validateMemory(vm.getStorageGiB());

        return resourceRepository.addVM(vm.getCpuNumber(), vm.getRamGiB(), vm.getStorageGiB());
    }

    @Override
    public VirtualMachine updateVM(String id, int cpuNumber, int ramGiB, int storageGiB) throws ResourceIdException, ResourceNotFoundException, ResourceDataException {
        validateCPUs(cpuNumber);
        validateRAM(ramGiB);
        validateMemory(storageGiB);

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

    private void validateCPUs(int cpuNumber) throws ResourceDataException {
        if (!(cpuNumber > 0 && cpuNumber <= 100)) {
            throw new ResourceDataException("cpu number must be between 1 and 100");
        }
    }

    private void validateRAM(int ram) throws ResourceDataException {
        if (!(ram > 0 && ram <= 1024)) {
            throw new ResourceDataException("ramGiB must be between 1 and 1024 GB");
        }
    }

    private void validateMemory(int memory) throws ResourceDataException {
        if (!(memory > 0 && memory <= 1048576)) {
            throw new ResourceDataException("storageGiB must be between 1 GiB and 1 PiB");
        }
    }
}
