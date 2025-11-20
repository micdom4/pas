package team.four.pas.repositories;

import team.four.pas.exceptions.resource.ResourceInvalidIdException;
import team.four.pas.exceptions.resource.ResourceNotPresentException;
import team.four.pas.services.data.resources.VirtualMachine;

public interface ResourceRepository extends Repository<VirtualMachine> {
    VirtualMachine addVM(int cpuNumber, int ram, int memory);
    VirtualMachine updateVM(String id, int cpuNumber, int ram, int memory) throws ResourceNotPresentException, ResourceInvalidIdException;
    VirtualMachine findById(String id) throws ResourceNotPresentException, ResourceInvalidIdException;
    void delete(String id) throws ResourceNotPresentException, ResourceInvalidIdException;
}

