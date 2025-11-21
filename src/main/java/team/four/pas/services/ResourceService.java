package team.four.pas.services;

import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

public interface ResourceService {
    List<VirtualMachine> getAll();
    VirtualMachine findById(String id) throws ResourceIdException, ResourceNotFoundException;

    VirtualMachine addVM(int cpuNumber, int ram, int memory) throws ResourceDataException;
    VirtualMachine updateVM(String id, int cpuNumber, int ram, int memory) throws ResourceIdException, ResourceNotFoundException, ResourceDataException;
    void deleteVM(String id) throws ResourceIdException, ResourceNotFoundException, ResourceStillAllocatedException;
}
