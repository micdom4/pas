package team.four.pas.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.four.pas.controllers.DTOs.ResourceAddDTO;
import team.four.pas.controllers.DTOs.ResourceDTO;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;
import team.four.pas.repositories.AllocationRepository;
import team.four.pas.repositories.ResourceRepository;
import team.four.pas.services.ResourceService;
import team.four.pas.services.mappers.ResourceToDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final AllocationRepository allocationRepository;
    private final ResourceToDTO resourceToDTO;

    @Override
    public List<ResourceDTO> getAll() {
        return resourceToDTO.toDataList(resourceRepository.getAll());
    }

    @Override
    public ResourceDTO findById(String id) throws ResourceIdException, ResourceNotFoundException {
        return resourceToDTO.toDTO(resourceRepository.findById(id));
    }

    @Override
    public ResourceDTO addVM(ResourceAddDTO vmDTO) throws ResourceDataException {
        validateCPUs(vmDTO.cpuNumber());
        validateRAM(vmDTO.ramGiB());
        validateMemory(vmDTO.storageGiB());

        return resourceToDTO.toDTO(resourceRepository.addVM(vmDTO.cpuNumber(), vmDTO.ramGiB(), vmDTO.storageGiB()));
    }

    @Override
    public ResourceDTO updateVM(String id, ResourceAddDTO vmDTO) throws ResourceIdException, ResourceNotFoundException, ResourceDataException {
        validateCPUs(vmDTO.cpuNumber());
        validateRAM(vmDTO.ramGiB());
        validateMemory(vmDTO.storageGiB());

        return resourceToDTO.toDTO(resourceRepository.updateVM(id,vmDTO.cpuNumber(), vmDTO.ramGiB(), vmDTO.storageGiB()));
    }

    @Override
    public void deleteVM(String id) throws ResourceIdException, ResourceNotFoundException, ResourceStillAllocatedException {
        if (allocationRepository.getActive(resourceToDTO.vmFromDTO(findById(id))).isEmpty()
                && allocationRepository.getPast(resourceToDTO.vmFromDTO(findById(id))).isEmpty()) {
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
