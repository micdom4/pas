package team.four.pas.repositories;

import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.services.data.resources.VirtualMachine;

public interface ResourceRepository extends Repository<VirtualMachine> {
    VirtualMachine addVM(int cpuNumber, int ram, int memory);
    VirtualMachine updateVM(String id, int cpuNumber, int ram, int memory) throws ResourceNotFoundException, ResourceIdException;
    VirtualMachine findById(String id) throws ResourceNotFoundException, ResourceIdException;
    void delete(String id) throws ResourceNotFoundException, ResourceIdException;
}

