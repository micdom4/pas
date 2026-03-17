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
import team.four.pas.using.ResourcePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourcePort resourceRepository;
    // private final AllocationRepository allocationRepository;

    @Override
    public List<VirtualMachine> getAll() {
        return resourceRepository.findAll();
    }

    @Override
    public VirtualMachine findById(String id) throws ResourceIdException, ResourceNotFoundException {
        return resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    public VirtualMachine addVM(VirtualMachine vm) throws ResourceDataException {
        return resourceRepository.insert(vm);
    }

    @Override
    public void updateVM(String id, int cpuNumber, int ramGiB, int storageGiB) throws ResourceIdException, ResourceNotFoundException, ResourceDataException {
        resourceRepository.updateById(id, cpuNumber, ramGiB, storageGiB);
    }

    @Override
    public void deleteVM(String id) throws ResourceIdException, ResourceNotFoundException, ResourceStillAllocatedException {
//        if (allocationRepository.getActive(findById(id)).isEmpty()
//                && allocationRepository.getPast(findById(id)).isEmpty()) {
//            resourceRepository.deleteById(id);
//        } else {
//            throw new ResourceStillAllocatedException("VM with ID: " + id + " is still allocated");
//        }
    }

}
