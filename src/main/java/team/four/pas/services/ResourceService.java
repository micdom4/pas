package team.four.pas.services;

import team.four.pas.exceptions.resource.*;
import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

public interface ResourceService {
    List<VirtualMachine> getAll() throws ResourceGetAllException;
    VirtualMachine findById(String id) throws ResourceFindException;

    VirtualMachine addVM(int cpuNumber, int ram, int memory) throws ResourceAddException;
    VirtualMachine updateVM(String id, int cpuNumber, int ram, int memory) throws ResourceUpdateException;
    void deleteVM(String id) throws ResourceDeleteException;
}
