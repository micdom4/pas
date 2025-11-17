package team.four.pas.repositories;

import team.four.pas.controllers.exceptions.service.AddVMException;
import team.four.pas.controllers.exceptions.service.DeleteVMException;
import team.four.pas.controllers.exceptions.service.UpdateVMException;
import team.four.pas.services.data.resources.VirtualMachine;

public interface ResourceRepository extends Repository<VirtualMachine> {
    VirtualMachine addVM(int cpuNumber, int ram, int memory) throws AddVMException;
    VirtualMachine updateVM(String id, int cpuNumber, int ram, int memory) throws UpdateVMException;
    VirtualMachine findById(String id);
    void delete(String id) throws DeleteVMException;
}

