package team.four.pas.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.exceptions.resource.*;
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
    public List<VirtualMachine> getAll() throws ResourceGetAllException {
        try {
            return resourceRepository.getAll();
        } catch (Exception ex) {
            throw new ResourceGetAllException(ex.getMessage(), ex);
        }
    }

    @Override
    public VirtualMachine findById(String id) throws ResourceFindException {
        try {
            return resourceRepository.findById(id);
        } catch (Exception ex) {
            throw new ResourceFindException(ex.getMessage(), ex);
        }
    }

    @Override
    public VirtualMachine addVM(int cpuNumber, int ram, int memory) throws ResourceAddException {
        try {
            validateCPUs(cpuNumber);
            validateRAM(ram);
            validateMemory(memory);

            return resourceRepository.addVM(cpuNumber, ram, memory);
        } catch (Exception e) {
            throw new ResourceAddException(e.getMessage(), e);
        }
    }

    @Override
    public VirtualMachine updateVM(String id, int cpuNumber, int ram, int memory) throws ResourceUpdateException {
        try {
            validateCPUs(cpuNumber);
            validateRAM(ram);
            validateMemory(memory);

            return resourceRepository.updateVM(id, cpuNumber, ram, memory);
        } catch (Exception e) {
            throw new ResourceUpdateException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteVM(String id) throws ResourceDeleteException {
        try {
            if (allocationRepository.getActive(findById(id)).isEmpty() && allocationRepository.getPast(findById(id)).isEmpty()) {
                resourceRepository.delete(id);
            } else {
                throw new ResourceStillAllocatedException("VM with ID: " + id + " is still allocated");
            }
        } catch (Exception e) {
            throw new ResourceDeleteException(e.getMessage(), e);
        }
    }

    private void validateCPUs(int cpuNumber) throws ResourceDataException {
        if (!(cpuNumber > 0 && cpuNumber <= 100)) {
            throw new ResourceDataException("cpu number must be between 1 and 100");
        }
    }

    private void validateRAM(int ram) throws ResourceDataException {
        if (!(ram > 0 && ram <= 1024)) {
            throw new ResourceDataException("ram must be between 1 and 1024 GB");
        }
    }

    private void validateMemory(int memory) throws ResourceDataException {
        if (!(memory > 0 && memory <= 1048576)) {
            throw new ResourceDataException("storage must be between 1 GiB and 1 PiB");
        }
    }
}
