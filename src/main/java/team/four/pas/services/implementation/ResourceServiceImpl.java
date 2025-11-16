package team.four.pas.services.implementation;

import org.springframework.stereotype.Service;
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
    public boolean addVM(int cpuNumber, int ram, int memory) {
        if (validateCPUs(cpuNumber) && validateRAM(ram) && validateMemory(memory)) {
            return resourceRepository.addVM(cpuNumber, ram, memory);
        } else {
            return false;
        }
    }

    @Override
    public boolean updateVM(String id, int cpuNumber, int ram, int memory) {
        if (validateCPUs(cpuNumber) && validateRAM(ram) && validateMemory(memory)) {
            return resourceRepository.updateVM(id, cpuNumber, ram, memory);
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteVM(String id) {
        if (allocationRepository.getActive(findById(id)).isEmpty() && allocationRepository.getPast(findById(id)).isEmpty()) {
            return resourceRepository.delete(id);
        } else {
            return false;
        }
    }

    private boolean validateCPUs(int cpuNumber) {
        return (cpuNumber > 0 && cpuNumber <= 100);
    }

    private boolean validateRAM(int ram) {
        return (ram > 0 && ram <= 1024);
    }

    private boolean validateMemory(int memory) {
        return (memory > 0 && memory <= 1048576);
    }
}
