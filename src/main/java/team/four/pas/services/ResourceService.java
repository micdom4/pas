package team.four.pas.services;

import team.four.pas.controllers.exceptions.service.AddVMException;
import team.four.pas.controllers.exceptions.service.DeleteVMException;
import team.four.pas.controllers.exceptions.service.UpdateVMException;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

public interface ResourceService {
    List<VirtualMachine> getAll();
    VirtualMachine findById(String id);

    VirtualMachine addVM(int cpuNumber, int ram, int memory) throws AddVMException;
    VirtualMachine updateVM(String id, int cpuNumber, int ram, int memory) throws UpdateVMException;
    void deleteVM(String id) throws DeleteVMException;
}
