package team.four.pas.services;

import team.four.pas.services.data.resources.VirtualMachine;

import java.util.List;

public interface ResourceService {
    List<VirtualMachine> getAll();
    VirtualMachine findById(String id);
    List<VirtualMachine> findById(List<String> id);

    boolean addVM(int cpuNumber, int ram, int memory);
    boolean updateVM(String id, int cpuNumber, int ram, int memory);
    boolean deleteVM(String id);
}
