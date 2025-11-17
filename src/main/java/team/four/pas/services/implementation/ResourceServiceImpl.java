package team.four.pas.services.implementation;

import org.springframework.stereotype.Service;
import team.four.pas.controllers.exceptions.service.AddVMException;
import team.four.pas.controllers.exceptions.service.DataValidationException;
import team.four.pas.controllers.exceptions.service.DeleteVMException;
import team.four.pas.controllers.exceptions.service.UpdateVMException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.services.ResourceService;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final AllocationRepository allocationRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository, AllocationRepository allocationRepository) {
        this.resourceRepository = resourceRepository;
        this.allocationRepository = allocationRepository;
    }

    @Override
    public List<VirtualMachine> getAll() {
        return resourceRepository.getAll();
    }

    @Override
    public VirtualMachine findById(String id) {
        return resourceRepository.findById(id);
    }

    @Override
    public List<VirtualMachine> findById(List<String> id) {
        return resourceRepository.findById(id);
    }

    @Override
    public VirtualMachine addVM(int cpuNumber, int ram, int memory) throws AddVMException {
        try {
            validateCPUs(cpuNumber);
            validateRAM(ram);
            validateMemory(memory);

            return resourceRepository.addVM(cpuNumber, ram, memory);
        } catch (Exception e) {
            throw new AddVMException(e.getMessage(), e);
        }
    }

    @Override
    public VirtualMachine updateVM(String id, int cpuNumber, int ram, int memory) throws UpdateVMException {
        try {
            validateCPUs(cpuNumber);
            validateRAM(ram);
            validateMemory(memory);

            return resourceRepository.updateVM(id, cpuNumber, ram, memory);
        } catch (Exception e) {
            throw new UpdateVMException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteVM(String id) throws DeleteVMException {
        try {
            if (allocationRepository.getActive(findById(id)).isEmpty() && allocationRepository.getPast(findById(id)).isEmpty()) {
                resourceRepository.delete(id);
            } else {
                throw new DeleteVMException("Resource is on allocation list");
            }
        } catch (Exception e) {
            throw new DeleteVMException(e.getMessage(), e);
        }
    }

    private void validateCPUs(int cpuNumber) throws DataValidationException {
        if (!(cpuNumber > 0 && cpuNumber <= 100)) {
            throw new DataValidationException("cpu number must be between 1 and 100");
        }
    }

    private void validateRAM(int ram) throws DataValidationException {
        if (!(ram > 0 && ram <= 1024)) {
            throw new DataValidationException("ram must be between 1 and 1024 GB");
        }
    }

    private void validateMemory(int memory) throws DataValidationException {
        if (!(memory > 0 && memory <= 1048576)) {
            throw new DataValidationException("storage must be between 1 GiB and 1 PiB");
        }
    }
}
